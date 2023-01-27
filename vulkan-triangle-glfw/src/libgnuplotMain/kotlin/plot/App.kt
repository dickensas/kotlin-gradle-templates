package plot

import platform.posix.*
import platform.windows.GetModuleHandle
import kotlinx.cinterop.*
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.attach

import vulkan.*
import glfw.*
import plot.*

fun loadShader(fileName:String, vk_ldevice:VkDevice) = memScoped {
    fun fail(): Nothing = throw Error("Cannot read input file $fileName")

    val size = alloc<stat>().also { if (stat(fileName, it.ptr) != 0) fail() }.st_size
    val file = platform.posix.fopen(fileName, "rb") ?: fail()
    val buf = ByteArray(size.toInt()).also {
        try {
            fread(it.refTo(0), 1, size.convert(), file)
        } finally {
            fclose(file)
        }
    }
    
    val shaderModule = alloc<VkShaderModuleVar>()
    buf.usePinned {
        var createInfo = alloc<VkShaderModuleCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO
            pNext = null
            codeSize = size.toULong()
            @Suppress("UNCHECKED_CAST")
            pCode = it.addressOf(0) as CPointer<UIntVarOf<UInt>>
            flags = 0u
        }
        if(vkCreateShaderModule(vk_ldevice, createInfo.ptr, null, shaderModule.ptr)!=VK_SUCCESS) 
            throw Error("Failed to load shader vertex shader")
    }
    shaderModule.value
}

fun ver(a: UInt, b: UInt, c: UInt): UInt = ((a shl 22) or (b shl 12) or c)
fun ma(version: UInt): UInt = (version shr 22)
fun mi(version: UInt): UInt = ((version shr 12) and (0x3ff).toUInt())
fun pa(version: UInt): UInt = (version and (0xfff).toUInt())

fun findMemIndex(lbits:UInt, memoryProperties:VkPhysicalDeviceMemoryProperties, properties:VkMemoryPropertyFlags):UInt {
    var memIndex = 0u
    var bits = lbits
    for (i in 0u until memoryProperties.memoryTypeCount) {
        if ((bits and 1u) == 1u) {
            if ((memoryProperties.memoryTypes[i.toInt()].propertyFlags and properties) == properties) {
                memIndex = i
                break
            }
        }
        bits = bits shr (1)
    }
    return memIndex
}

fun createBufferAndMemory(
    scope:MemScope,
    ibuffer:VkBufferVar,
    imemory:VkDeviceMemoryVar,
    vk_ldevice:VkDevice,
    vk_pdevice:VkPhysicalDevice,
    isize:ULong, 
    obuffer: VkBufferVar, 
    omemory: VkDeviceMemoryVar,
    buffer: Any,
    bitUsage: VkBufferUsageFlagBits
    ) {
    with(scope) {
        
        val vertexBufferInfo = alloc<VkBufferCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO
            size = isize
            usage = VK_BUFFER_USAGE_TRANSFER_SRC_BIT
        }
        
        if (
                vkCreateBuffer(
                    vk_ldevice,
                    vertexBufferInfo.ptr,
                    null,
                    ibuffer.ptr
                )
            != VK_SUCCESS
        )
        throw RuntimeException("Failed to create buffer")

        val memReqs: VkMemoryRequirements = alloc()
        vkGetBufferMemoryRequirements(vk_ldevice, ibuffer.value, memReqs.ptr)
    
        val memoryAllocateInfo = alloc<VkMemoryAllocateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO
            allocationSize = memReqs.size
        }
        
        var memoryProperties: VkPhysicalDeviceMemoryProperties = alloc()
        vkGetPhysicalDeviceMemoryProperties(vk_pdevice, memoryProperties.ptr)
        
        memoryAllocateInfo.memoryTypeIndex = findMemIndex(
            memReqs.memoryTypeBits,
            memoryProperties,
            VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT or VK_MEMORY_PROPERTY_HOST_COHERENT_BIT
        )
        
        val mapped = alloc<COpaquePointerVar>()
        
        if (
                vkAllocateMemory(
                    vk_ldevice,
                    memoryAllocateInfo.ptr,
                    null,
                    imemory.ptr
                )
            != VK_SUCCESS
        )
        throw RuntimeException("Faild allocate memory")
    
        if (
                vkMapMemory(
                    vk_ldevice,
                    imemory.value,
                    0u,
                    memoryAllocateInfo.allocationSize,
                    0u,
                    mapped.ptr
                )
            !=VK_SUCCESS
        )
        throw RuntimeException("Faild map memory")
            
        if(buffer is FloatArray) {
            buffer.usePinned { buf ->
                platform.posix.memcpy(mapped.value, buf.addressOf(0), isize)
            }
        }else if(buffer is UIntArray) {
            buffer.usePinned { buf ->
                platform.posix.memcpy(mapped.value, buf.addressOf(0), isize)
            }
        }
    
        vkUnmapMemory(vk_ldevice, imemory.value)
        
        if (
                vkBindBufferMemory(
                    vk_ldevice,
                    ibuffer.value,
                    imemory.value,
                    0u
                )
            != VK_SUCCESS
        )
        throw RuntimeException("failed bind memory")
    
        vertexBufferInfo.usage = bitUsage
    
        if (vkCreateBuffer(vk_ldevice, vertexBufferInfo.ptr, null, obuffer.ptr)!=VK_SUCCESS)
            throw RuntimeException("Failed to create buffer")
        vkGetBufferMemoryRequirements(vk_ldevice, obuffer.value, memReqs.ptr)
    
        memoryAllocateInfo.allocationSize = memReqs.size
        memoryAllocateInfo.memoryTypeIndex = findMemIndex(
            memReqs.memoryTypeBits,
            memoryProperties,
            VK_MEMORY_PROPERTY_DEVICE_LOCAL_BIT
        )
    
        if (vkAllocateMemory(vk_ldevice, memoryAllocateInfo.ptr, null, omemory.ptr)!= VK_SUCCESS)
            throw RuntimeException("Faild allocate memory")
    
        if (vkBindBufferMemory(vk_ldevice, obuffer.value, omemory.value, 0u)!= VK_SUCCESS)
            throw RuntimeException("failed bind memory")
        
    }
}

val modes: ArrayList<VkPresentModeKHR> = ArrayList()
var presentetionMode: VkPresentModeKHR = VK_PRESENT_MODE_FIFO_KHR
var colorSpace: VkColorSpaceKHR = UInt.MAX_VALUE
var swapChainFormat: VkFormat = UInt.MAX_VALUE

fun main() = memScoped {

    //#1 create GLFW instance
    if (glfwInit() == GLFW_FALSE) {
        throw Error("Failed to initialize GLFW")
    }
    
    glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

    val window = glfwCreateWindow(640, 480, "Vulkan", null, null) ?:
    throw Error("Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.")

    glfwMakeContextCurrent(window)
    
    val extensions: MutableList<String> = ArrayList()
    
        memScoped {
            var count = alloc<UIntVar>()
            var ext = glfwGetRequiredInstanceExtensions(count.ptr);
            extensions.addAll(Array(count.value.toInt()) { ext!![it]!!.toKString() })
        }
        
         memScoped {
            val extensionsCount = alloc<UIntVar>()
            extensionsCount.value = 0u
            var result: VkResult
            do {
                result = vkEnumerateInstanceExtensionProperties(null, extensionsCount.ptr, null)
                if ( result != VK_SUCCESS ) throw RuntimeException("Could not enumerate instance extensions.")

                if (extensionsCount.value == 0u) break

                val buffer = allocArray<VkExtensionProperties>(extensionsCount.value.toInt())
                result = vkEnumerateInstanceExtensionProperties(null, extensionsCount.ptr, buffer)

                for (i in 0 until extensionsCount.value.toInt()) {
                    val ext = buffer[i].extensionName.toKString()
                    if (!extensions.contains(ext))
                        extensions.add(ext)
                }
            } while (result == VK_INCOMPLETE)
        }
        
        println(extensions)
        
        val debugSupported = extensions.contains("VK_EXT_debug_report")
        
        val availableLayers = mutableListOf<String>()
        var callback = alloc<VkDebugReportCallbackEXTVar>()
        
        //#2 create Vulcan instance and debug callback
        
        val vk_instance = memScoped {
            val acInfo = alloc<VkInstanceCreateInfo>().apply {
                sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO
                pNext = null
                pApplicationInfo = null
                enabledLayerCount = 0U
                ppEnabledLayerNames = null
                enabledExtensionCount = extensions.size.toUInt()
                ppEnabledExtensionNames = extensions.toCStringArray(memScope)
            }
            
            if(debugSupported) {
                val layers = arrayOf(
                    "VK_LAYER_GOOGLE_threading",
                    "VK_LAYER_LUNARG_parameter_validation",
                    "VK_LAYER_LUNARG_object_tracker",
                    "VK_LAYER_LUNARG_core_validation",
                    "VK_LAYER_GOOGLE_unique_objects",
                    "VK_LAYER_LUNARG_standard_validation"
                )
                val layersCount = alloc<UIntVar>()

                var result1: VkResult
                
                run failure@{
                    do {
                        result1 = vkEnumerateInstanceLayerProperties(layersCount.ptr, null)
                        if (result1 != VK_SUCCESS) {
                            println("Failed to enumerate debug layers")
                            availableLayers.clear()
                            return@failure // failed to get layers break the loop
    
                        } else {
    
                            val buffer = allocArray<VkLayerProperties>(layersCount.value.toInt())
    
                            result1 = vkEnumerateInstanceLayerProperties(layersCount.ptr, buffer)
                            if (result1 !=VK_SUCCESS) {
                                println("Filed to enumerate Debug Layers to buffer")
                                availableLayers.clear()
                                return@failure // failed to get layers break the loop
    
                            }
    
                            for (i in 0 until layersCount.value.toInt()) {
                                val layer = buffer[i].layerName.toKString()
                                println("Found $layer layer")
                                if (!availableLayers.contains(layer) && layers.contains(layer)) {
                                    availableLayers.add(layer)
                                    println("$layer added")
                                }
                            }
                        }
                    } while (result1 == VK_INCOMPLETE)
                }
                
                if (availableLayers.size > 0) {
                    if (availableLayers.contains("VK_LAYER_LUNARG_standard_validation"))
                        availableLayers.removeAll {
                            it != "VK_LAYER_LUNARG_standard_validation"
                    }
                    else {
                        availableLayers.sortBy {
                            layers.indexOf(it)
                        }
                    }
                    println("Setting up Layers:")
                    availableLayers.forEach {
                        println(it)
                    }
    
                    acInfo.enabledLayerCount = availableLayers.size.toUInt()
                    acInfo.ppEnabledLayerNames = availableLayers.toCStringArray(memScope)
    
                }
            }
            
            val output = alloc<VkInstanceVar>()
            var result = vkCreateInstance(acInfo.ptr, null, output.ptr)
            if(result!=VK_SUCCESS) 
                throw Error("Error initializing Vulkan")
                
            if(debugSupported && availableLayers.size > 0) {
                val dbgCreateInfo = alloc<VkDebugReportCallbackCreateInfoEXT>().apply {

                    sType = VK_STRUCTURE_TYPE_DEBUG_REPORT_CALLBACK_CREATE_INFO_EXT.toUInt()
                    flags = VK_DEBUG_REPORT_INFORMATION_BIT_EXT or VK_DEBUG_REPORT_WARNING_BIT_EXT or
                            VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT or VK_DEBUG_REPORT_ERROR_BIT_EXT or
                            VK_DEBUG_REPORT_DEBUG_BIT_EXT
    
                    pfnCallback = staticCFunction { flags, _, _, _, msgCode, pLayerPrefix, pMsg, _ ->
    
                        var prefix = "kvarc-"
    
                        when {
    
                            flags and VK_DEBUG_REPORT_ERROR_BIT_EXT > 0u -> prefix += "ERROR:"
                            flags and VK_DEBUG_REPORT_WARNING_BIT_EXT > 0u -> prefix += "WARNING:"
                            flags and VK_DEBUG_REPORT_PERFORMANCE_WARNING_BIT_EXT > 0u -> prefix += "PERFORMANCE:"
                            flags and VK_DEBUG_REPORT_INFORMATION_BIT_EXT > 0u -> prefix += "INFO:"
                            flags and VK_DEBUG_REPORT_DEBUG_BIT_EXT > 0u -> prefix += "DEBUG:"
    
                        }
    
                        val debugMessage =
                            "$prefix [${pLayerPrefix?.toKString() ?: ""}] Code $msgCode:${pMsg?.toKString() ?: ""}"
    
                        if (flags and VK_DEBUG_REPORT_ERROR_BIT_EXT > 0.toUInt()) {
                            println(debugMessage)
                        } else {
                            println(debugMessage)
                        }
    
                        VK_FALSE.toUInt()
                    }
    
                }
    
                println("Creating debug callback")
                var kvkCreateDebugReportCallbackEXT: PFN_vkCreateDebugReportCallbackEXT? = null
    
                vkGetInstanceProcAddr(output.value, "vkCreateDebugReportCallbackEXT")?.let { voidFun ->
    
                    @Suppress("UNCHECKED_CAST")
                    kvkCreateDebugReportCallbackEXT = voidFun as PFN_vkCreateDebugReportCallbackEXT
    
                }
    
                kvkCreateDebugReportCallbackEXT?.let {
                    if (
                            it(
                                output.value,
                                dbgCreateInfo.ptr,
                                null,
                                callback.ptr
                            )
                            !=VK_SUCCESS
                    ) {
                        println("Add layers callback failed.")
                    }
                } ?: kotlin.run {
                    println("Add layers callback failed.")
                }
            }
            
            output.value
        }
        
        println("Vulkan successfully initialized")
        
        val vk_surface:VkSurfaceKHR? = surfaceInit(extensions, window, vk_instance)
        
        println("Vulkan surface initialized")
        
        //#4 create Vulcan physical surface
        
        val vk_pdevice = memScoped {
            var result = VK_INCOMPLETE
            val gpuCount = alloc<UIntVar>()
            var output = alloc<VkPhysicalDeviceVar>()
            var buffer: CArrayPointer<VkPhysicalDeviceVar>? = null
            
            while (result == VK_INCOMPLETE) {
                
                result = vkEnumeratePhysicalDevices(vk_instance, gpuCount.ptr, null)
                if(result != VK_SUCCESS){
                    throw Error("Could not enumerate GPUs.")
                }
                
                buffer?.let {
                    nativeHeap.free(it)
                    buffer = null
                }
                
                buffer = nativeHeap.allocArray(gpuCount.value.toInt())
    
                result = vkEnumeratePhysicalDevices(vk_instance, gpuCount.ptr, buffer)
                if (result != VK_INCOMPLETE && result != VK_SUCCESS)
                    throw Error("Could not enumerate GPUs.")
                            
            }
            output.value = buffer!![0]
            
            for (i in 0 until gpuCount.value.toInt()) {
    
                val props1 = alloc<VkPhysicalDeviceProperties>()
                vkGetPhysicalDeviceProperties(buffer!![i], props1.ptr)
    
                if (props1.deviceType == VK_PHYSICAL_DEVICE_TYPE_DISCRETE_GPU) {
                    output.value = buffer!![i]
                    println("Selected device number ${i} - success")
                    break
                }
    
            }
    
            buffer?.let {
                nativeHeap.free(it)
                buffer = null
            }
            
            output.value
        }
        
        println("Vulkan physical device initialized")
        
        val props: VkPhysicalDeviceProperties by lazy {
            var output = alloc<VkPhysicalDeviceProperties>()
            vkGetPhysicalDeviceProperties(vk_pdevice, output.ptr)
            output
        }
        
        val vk_devftrs by lazy {
            var output = alloc<VkPhysicalDeviceFeatures>()
            vkGetPhysicalDeviceFeatures(vk_pdevice, output.ptr)
            output
        }
        
        val caps: VkSurfaceCapabilitiesKHR by lazy {
            var output = alloc<VkSurfaceCapabilitiesKHR>()
            val result = vkGetPhysicalDeviceSurfaceCapabilitiesKHR(vk_pdevice, vk_surface, output.ptr)
            if(result != VK_SUCCESS){
                throw Error("Failed to get surface capabilities")
            }
            output
        }

        val vk_extprops = memScoped {
            val output: ArrayList<String> = ArrayList()
            val count = alloc<UIntVar>()
            vkEnumerateDeviceExtensionProperties(vk_pdevice, null, count.ptr, null)
            val arr = allocArray<VkExtensionProperties>(count.value.toInt())
            vkEnumerateDeviceExtensionProperties(vk_pdevice, null, count.ptr, arr)
            for (i in 0 until count.value.toInt())
                output.add(arr[i].extensionName.toKString())
            output
        }
        
        val queueFamilies by lazy {
            var output: ArrayList<VkQueueFamilyProperties> = ArrayList()
            memScoped {
                val count: UIntVar = alloc()
                vkGetPhysicalDeviceQueueFamilyProperties(vk_pdevice, count.ptr, null)
                assert(count.value > 0u)
    
                val buffer = allocArray<VkQueueFamilyProperties>(count.value.toInt())
                vkGetPhysicalDeviceQueueFamilyProperties(vk_pdevice, count.ptr, buffer)
    
                for (i in 0 until count.value.toInt()) {
                    output.add(buffer[i])
                }
    
            }
            output
        }
        
        var foundGraphicsQueueFamily = false
        var foundPresentQueueFamily = false
        var graphicsQueueFamily = 0u
        var presentQueueFamily = 0u
        
        for (i in 0u until queueFamilies.size.toUInt()) {
            var presentSupport = alloc<VkBool32Var>();
            vkGetPhysicalDeviceSurfaceSupportKHR(vk_pdevice, i, vk_surface, presentSupport.ptr)
            
            if (
                queueFamilies[i.toInt()].queueCount.toInt() > 0
                && 
                ( 
                queueFamilies[i.toInt()].queueFlags.toInt()
                and 
                VK_QUEUE_GRAPHICS_BIT.toInt()
                ) 
                > 0
                ) {
                
                graphicsQueueFamily = i
                foundGraphicsQueueFamily = true

                if (presentSupport.value == 1u) {
                    presentQueueFamily = i
                    foundPresentQueueFamily = true
                    break
                }
            }
            
            if (!foundPresentQueueFamily && presentSupport.value == 1u) {
                presentQueueFamily = i
                foundPresentQueueFamily = true
            }
        }
        
        if (foundGraphicsQueueFamily) {
            println("queue family #${graphicsQueueFamily} supports graphics")

            if (foundPresentQueueFamily) {
                println("queue family #${presentQueueFamily} supports presentation")
            } else {
                throw Error("could not find a valid queue family with present support")
            }
        } else {
            throw Error("could not find a valid queue family with graphics support")
        }
        
        var devFeats: VkPhysicalDeviceFeatures = vk_devftrs
        var devExts: ArrayList<String> = vk_extprops
        val queueCInfos: ArrayList<VkDeviceQueueCreateInfo> = ArrayList()
        
        //#5 create Vulcan logical device
        val vk_ldevice = memScoped {
            val ldevice: VkDeviceVar = alloc<VkDeviceVar>()
            val queuePriority = allocArrayOf(1.0f)
            
            val queueInfo = alloc<VkDeviceQueueCreateInfo>().apply {
                sType = VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO
                queueCount = 1u
                pQueuePriorities = queuePriority
            }
            
            queueCInfos.add(queueInfo)
            
            if (!devExts.contains(VK_KHR_SWAPCHAIN_EXTENSION_NAME))
                devExts.add(VK_KHR_SWAPCHAIN_EXTENSION_NAME)
            
            var idx = 0
            val queueInfos = allocArray<VkDeviceQueueCreateInfo>(queueCInfos.size) {
                val info = queueCInfos[idx++]
                this.flags = info.flags
                this.sType = info.sType
                this.flags = info.flags
                this.pNext = info.pNext
                this.pQueuePriorities = info.pQueuePriorities
                this.queueCount = info.queueCount
                this.queueFamilyIndex = info.queueFamilyIndex
            }
            
            val dInfo = alloc<VkDeviceCreateInfo>().apply {
                sType = VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO
                pNext = null
                queueCreateInfoCount = queueCInfos.size.toUInt()
                pQueueCreateInfos = queueInfos
                pEnabledFeatures = devFeats.ptr
                enabledLayerCount = 0U
            }
            
            if (devExts.size > 0) {
                dInfo.enabledExtensionCount = devExts.size.toUInt()
                dInfo.ppEnabledExtensionNames = devExts.toCStringArray(memScope)
            }
                
            var result = vkCreateDevice(vk_pdevice, dInfo.ptr, null, ldevice.ptr)
            
            if(result!=VK_SUCCESS) throw Error("Logical device failure")
            
            ldevice.value
        }
        
        println("Vulkan logical device initialized")
        
        
        val graphicsQueue = alloc<VkQueueVar>()
        val presentQueue = alloc<VkQueueVar>()
        
        vkGetDeviceQueue(vk_ldevice, graphicsQueueFamily.toUInt(), 0u, graphicsQueue.ptr)
        vkGetDeviceQueue(vk_ldevice, presentQueueFamily.toUInt(), 0u, presentQueue.ptr)

        println("Created graphicsQueue ${graphicsQueue.value.toLong().toString(16)}")
        println("Created presentQueue ${presentQueue.value.toLong().toString(16)}")
        
        var memoryProperties: VkPhysicalDeviceMemoryProperties = alloc()
        vkGetPhysicalDeviceMemoryProperties(vk_pdevice, memoryProperties.ptr)
        
        //#6 create Semaphores
        
        var imageAvailableSemaphore = alloc<VkSemaphoreVar>()
        var renderingFinishedSemaphore = alloc<VkSemaphoreVar>()
        
        memScoped {
            val semaphoreCreateInfo = alloc<VkSemaphoreCreateInfo>().apply {
                sType = VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO
                pNext = null
            }
    
            if (
                    (
                        vkCreateSemaphore(
                            vk_ldevice,
                            semaphoreCreateInfo.ptr,
                            null,
                            imageAvailableSemaphore.ptr
                        ) != VK_SUCCESS
                        
                            ||
                            
                        vkCreateSemaphore(
                            vk_ldevice,
                            semaphoreCreateInfo.ptr,
                            null,
                            renderingFinishedSemaphore.ptr
                        ) != VK_SUCCESS
                    )
                
            )
            throw  RuntimeException("Failed to create imageAvailableSemaphore or renderingFinishedSemaphore")
        }
        
        
        //#7 Create graphics command pool 
        val commandPoolVar = alloc<VkCommandPoolVar>()
        
        val commandPoolCreateInfo = alloc<VkCommandPoolCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO
            queueFamilyIndex = graphicsQueueFamily.toUInt()
            flags = 0U
        }
        
        if ( vkCreateCommandPool(vk_ldevice, commandPoolCreateInfo.ptr, null, commandPoolVar.ptr) != VK_SUCCESS)
                throw RuntimeException("Failed to create command pool")
                
        println("Created command pool ${commandPoolVar.value.toLong().toString(16)}")
        
        //#8 Setup vertices buffers
        val vertexBuffer = floatArrayOf(
            -0.8f, -0.8f, 0.0f,
             0.8f, -0.8f, 0.0f,
             0.0f,  0.8f, 0.0f
        )
        
        var indexBuffer = uintArrayOf(0u,2u,1u)
        
        val vertexBufferSize = (vertexBuffer.size * sizeOf<FloatVar>()).toULong();
        var indexBufferSize = (indexBuffer.size * sizeOf<UIntVar>()).toULong()
        val indicesCount by lazy { indexBuffer.size }
        
        var vIBuffer = alloc<VkBufferVar>()
        var vIMemory = alloc<VkDeviceMemoryVar>()
        var vOBuffer = alloc<VkBufferVar>()
        var vOMemory = alloc<VkDeviceMemoryVar>()

        createBufferAndMemory(
            memScope, 
            vIBuffer, 
            vIMemory, 
            vk_ldevice!!, 
            vk_pdevice!!, 
            vertexBufferSize,
            vOBuffer, 
            vOMemory,
            vertexBuffer,
            VK_BUFFER_USAGE_VERTEX_BUFFER_BIT or VK_BUFFER_USAGE_TRANSFER_DST_BIT
        )
        
        var iIBuffer = alloc<VkBufferVar>()
        var iIMemory = alloc<VkDeviceMemoryVar>()
        var iOBuffer = alloc<VkBufferVar>()
        var iOMemory = alloc<VkDeviceMemoryVar>()
        
        createBufferAndMemory(
            memScope, 
            iIBuffer, 
            iIMemory, 
            vk_ldevice, 
            vk_pdevice,
            indexBufferSize, 
            iOBuffer, 
            iOMemory,
            indexBuffer,
            VK_BUFFER_USAGE_INDEX_BUFFER_BIT or VK_BUFFER_USAGE_TRANSFER_DST_BIT
        )
        
        val copyCmdBufInfo = alloc<VkCommandBufferAllocateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO
            commandPool = commandPoolVar.value
            level = VK_COMMAND_BUFFER_LEVEL_PRIMARY
            commandBufferCount = 1u
            pNext = null
        }
        
        var copyCommandBuffer = allocArray<VkCommandBufferVar>(1)
        
        if ( vkAllocateCommandBuffers(vk_ldevice, copyCmdBufInfo.ptr, copyCommandBuffer) != VK_SUCCESS)
            throw RuntimeException("Failed to create copy command buffer")
        
        val copyBeginBufInfo: VkCommandBufferBeginInfo = alloc<VkCommandBufferBeginInfo>().apply {
            sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO
            pNext = null
            flags = VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT
        }
        
        if ( vkBeginCommandBuffer(copyCommandBuffer[0], copyBeginBufInfo.ptr) != VK_SUCCESS)
                throw RuntimeException("Failed to start begin command buffer")
                
        val copyRegion = alloc<VkBufferCopy>()

        copyRegion.size = vertexBufferSize.toULong()
        
        vkCmdCopyBuffer(
            copyCommandBuffer[0],
            vIBuffer.value,
            vOBuffer.value,
            1u,
            copyRegion.ptr
        )

        copyRegion.size = indexBufferSize.toULong()
        
        vkCmdCopyBuffer(
            copyCommandBuffer[0],
            iIBuffer.value,
            iOBuffer.value,
            1u,
            copyRegion.ptr
        )
        
        memScoped {
        
            val copyCommand = alloc<VkCommandBufferVar>()
            copyCommand.value = copyCommandBuffer[0]
            
            if (vkEndCommandBuffer(copyCommand.value) != VK_SUCCESS)
                throw RuntimeException("Failed to end copy command buffer")

            val submitInfo = alloc<VkSubmitInfo>().apply {
                sType = VK_STRUCTURE_TYPE_SUBMIT_INFO
                commandBufferCount = 1u
                pCommandBuffers = copyCommand.ptr
            }
            
            if (vkQueueSubmit(graphicsQueue.value, 1u, submitInfo.ptr, null)!=VK_SUCCESS)
                throw Error("Failed submit copy queue")
                
            vkQueueWaitIdle(graphicsQueue.value)
            
            vkFreeCommandBuffers(vk_ldevice, commandPoolVar.value, 1u, copyCommand.ptr)
            
            vkDestroyBuffer(vk_ldevice, vIBuffer.value, null)
            vkFreeMemory(vk_ldevice, vIMemory.value, null)
    
            vkDestroyBuffer(vk_ldevice, iIBuffer.value, null)
            vkFreeMemory(vk_ldevice, iIMemory.value, null)
            
        }
        
        
        //#10 Setup swap chain
        var swapChainExtent: VkExtent2D
        
        if (caps.currentExtent.width == (-1).toUInt()) {
            swapChainExtent = alloc<VkExtent2D>()
            swapChainExtent.width = 640u
            swapChainExtent.height = 480u
        } else {
            swapChainExtent = caps.currentExtent
        }
        
        println("""
        Device: ${props.deviceName.toKString()}
        Driver: ${ma(props.driverVersion)}.${mi(props.driverVersion)}.${pa(props.driverVersion)}
        Vulkan: ${ma(props.apiVersion)}.${mi(props.apiVersion)}.${pa(props.apiVersion)}
        GLFW Size: ${caps.currentExtent.width}x${caps.currentExtent.height}
        """)
        
        
        val formatsCount = alloc<UIntVar>()
        memScoped {
            var result: VkResult
            val modesCount = alloc<UIntVar>()
            var buffer1: CArrayPointer<VkPresentModeKHRVar>? = null
            
            do {
                result = vkGetPhysicalDeviceSurfacePresentModesKHR(vk_pdevice, vk_surface, modesCount.ptr, null)
                if ( result != VK_SUCCESS) {
                    throw RuntimeException("Could not get surface present modes.")
                }
                
                if (modesCount.value == 0u) break

                buffer1?.let {
                    nativeHeap.free(it)
                    buffer1 = null
                }

                buffer1 = nativeHeap.allocArray(modesCount.value.toInt())

                result = vkGetPhysicalDeviceSurfacePresentModesKHR(vk_pdevice, vk_surface, modesCount.ptr, buffer1)
                if (result != VK_SUCCESS) {
                    throw RuntimeException("Could not get surface present modes.")
                }
            } while (result == VK_INCOMPLETE)
            
            for (i in 0 until modesCount.value.toInt())
                modes.add(buffer1!![i])

            buffer1?.let {
                nativeHeap.free(it)
            }
        }
        
        var swapChainImages = ArrayList<VkImage>()
        var swapChainImageViews = ArrayList<VkImageView>()
        var swapChainFramebuffers = ArrayList<VkFramebuffer>()
        val swapChainImagesCount: UIntVar = alloc()
        
        val vk_swapchain = memScoped {
            println("display chosen for swapchain ${swapChainExtent.width}x${swapChainExtent.height}")
            val swapchain: VkSwapchainKHRVar = alloc()
            var result: VkResult
            var buffer: CArrayPointer<VkSurfaceFormatKHR>? = null
            
            do {
                result = vkGetPhysicalDeviceSurfaceFormatsKHR(vk_pdevice, vk_surface, formatsCount.ptr, null)
                if (result != VK_SUCCESS) {
                    throw Error("Surface formats failure!")
                }

                if (formatsCount.value == 0u) break

                buffer?.let {
                    nativeHeap.free(it)
                    buffer = null
                }

                buffer = nativeHeap.allocArray(formatsCount.value.toInt())
                result =
                    vkGetPhysicalDeviceSurfaceFormatsKHR(
                        vk_pdevice,
                        vk_surface,
                        formatsCount.ptr,
                        buffer!!.getPointer(memScope)
                    )
                if (result != VK_SUCCESS) {
                    throw Error("Surface formats failure!")
                }
                
            } while (result == VK_INCOMPLETE)
            
            if (formatsCount.value == 1u) {
                    swapChainFormat = buffer!![0].format
                    colorSpace = buffer!![0].colorSpace
                    println("only one formatsCount ${formatsCount.value}")
            } else {

                var chosenFormat: UInt? = null

                for (i in 0u until formatsCount.value) {
                    if (buffer!![i.toInt()].format == VK_FORMAT_R8G8B8A8_UNORM) {
                        chosenFormat = i
                        break
                    }
                }

                chosenFormat?.let {
                    swapChainFormat = buffer!![it.toInt()].format
                    colorSpace = buffer!![it.toInt()].colorSpace
                } ?: kotlin.run {
                    swapChainFormat = buffer!![0].format
                    colorSpace = buffer!![0].colorSpace
                }

            }

            nativeHeap.free(buffer!!)
            
            run mail@{
                modes.forEach {
                    if (it == VK_PRESENT_MODE_MAILBOX_KHR) {
                        presentetionMode = VK_PRESENT_MODE_MAILBOX_KHR
                        return@mail
                    }

                    if ((presentetionMode != VK_PRESENT_MODE_MAILBOX_KHR) && (it == VK_PRESENT_MODE_IMMEDIATE_KHR))
                        presentetionMode = VK_PRESENT_MODE_IMMEDIATE_KHR
                }
            }
            
            var imagesNeeded = caps.maxImageCount + 1u
            if ((caps.maxImageCount > 0u) && (imagesNeeded > caps.maxImageCount))
                imagesNeeded = caps.maxImageCount

            val surfaceTransform: VkSurfaceTransformFlagsKHR =
                if ((caps.supportedTransforms and VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR) > 0u)
                    VK_SURFACE_TRANSFORM_IDENTITY_BIT_KHR
                else
                    caps.currentTransform

            var compositeAlphaBit = VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR
            val compositeAlphaFlags = arrayOf(
                VK_COMPOSITE_ALPHA_OPAQUE_BIT_KHR,
                VK_COMPOSITE_ALPHA_PRE_MULTIPLIED_BIT_KHR,
                VK_COMPOSITE_ALPHA_POST_MULTIPLIED_BIT_KHR,
                VK_COMPOSITE_ALPHA_INHERIT_BIT_KHR
            )

            kotlin.run found@{
                compositeAlphaFlags.forEach {
                    if ((caps.supportedCompositeAlpha and it) > 0u) {
                        compositeAlphaBit = it
                        return@found
                    }
                }
            }

            val discardSwapchain = alloc<VkSwapchainKHRVar>()

            var swapchainCreateInfo: VkSwapchainCreateInfoKHR = alloc<VkSwapchainCreateInfoKHR>().apply {
                sType = VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR
                pNext = null
                this.surface = vk_surface
                minImageCount = imagesNeeded
                imageFormat = swapChainFormat
                imageColorSpace = colorSpace
                imageExtent.width = swapChainExtent.width
                imageExtent.height = swapChainExtent.height
                imageUsage = VK_IMAGE_USAGE_COLOR_ATTACHMENT_BIT
                imageArrayLayers = 1u
                preTransform = surfaceTransform
                queueFamilyIndexCount = 0u
                pQueueFamilyIndices = null
                presentMode = presentetionMode
                oldSwapchain = discardSwapchain.value
                clipped = 1u
                compositeAlpha = compositeAlphaBit
            }

            swapchainCreateInfo.imageSharingMode = VK_SHARING_MODE_EXCLUSIVE

            if ((caps.supportedUsageFlags and VK_IMAGE_USAGE_TRANSFER_SRC_BIT) > 0u)
                swapchainCreateInfo.imageUsage = swapchainCreateInfo.imageUsage or VK_IMAGE_USAGE_TRANSFER_SRC_BIT

            if ((caps.supportedUsageFlags and VK_IMAGE_USAGE_TRANSFER_DST_BIT) > 0u)
                swapchainCreateInfo.imageUsage = swapchainCreateInfo.imageUsage or VK_IMAGE_USAGE_TRANSFER_DST_BIT

            if (vkCreateSwapchainKHR(vk_ldevice, swapchainCreateInfo.ptr, null, swapchain.ptr) != VK_SUCCESS)
                throw Error("Failed to create swapchain")
                
            println("Swap chain created successfully")
                
            if (discardSwapchain.value != null) {
                vkDestroySwapchainKHR(vk_ldevice, discardSwapchain.value, null)
            }

            if (vkGetSwapchainImagesKHR(vk_ldevice, swapchain.value, swapChainImagesCount.ptr, null) != VK_SUCCESS )
                throw RuntimeException("Failed to initialize vulkan. No images")

            var localImagesBuffer = allocArray<VkImageVar>(swapChainImagesCount.value.toInt())

            if ( vkGetSwapchainImagesKHR(vk_ldevice, swapchain.value, swapChainImagesCount.ptr, localImagesBuffer) != VK_SUCCESS)
                throw Error("No vulkan images")
                
            for (i in 0 until swapChainImagesCount.value.toInt()) {
                swapChainImages.add(localImagesBuffer[i]!!)
            }

            swapchain.value
        }
        
        println("Swap chain images count " + swapChainImages.size);
        
        //#11 Create Render pass
        val attachments = allocArray<VkAttachmentDescription>(1)
        
        attachments[0].apply {
            this.format = swapChainFormat
            this.finalLayout = VK_IMAGE_LAYOUT_PRESENT_SRC_KHR
            this.initialLayout = VK_IMAGE_LAYOUT_UNDEFINED
            this.loadOp = VK_ATTACHMENT_LOAD_OP_CLEAR
            this.samples = VK_SAMPLE_COUNT_1_BIT
            this.stencilLoadOp = VK_ATTACHMENT_LOAD_OP_DONT_CARE
            this.stencilStoreOp = VK_ATTACHMENT_STORE_OP_DONT_CARE
            this.storeOp = VK_ATTACHMENT_STORE_OP_STORE
        }
        
        val colorReference = alloc<VkAttachmentReference>()
        colorReference.attachment = 0u
        colorReference.layout = VK_IMAGE_LAYOUT_COLOR_ATTACHMENT_OPTIMAL
        
        val subpass = alloc<VkSubpassDescription>()
        subpass.pipelineBindPoint = VK_PIPELINE_BIND_POINT_GRAPHICS
        subpass.colorAttachmentCount = 1u;
        subpass.pColorAttachments = colorReference.ptr
        
        val dependencies = allocArray<VkSubpassDependency>(1).apply {
            this[0].srcSubpass = VK_SUBPASS_EXTERNAL
            this[0].dstSubpass = 0u
            this[0].srcStageMask = VK_PIPELINE_STAGE_BOTTOM_OF_PIPE_BIT
            this[0].dstStageMask = VK_PIPELINE_STAGE_COLOR_ATTACHMENT_OUTPUT_BIT
            this[0].srcAccessMask = VK_ACCESS_MEMORY_READ_BIT
            this[0].dstAccessMask = VK_ACCESS_COLOR_ATTACHMENT_READ_BIT or VK_ACCESS_COLOR_ATTACHMENT_WRITE_BIT
            this[0].dependencyFlags = VK_DEPENDENCY_BY_REGION_BIT
        }

        val renderPassInfo = alloc<VkRenderPassCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_RENDER_PASS_CREATE_INFO
            pNext = null
            pAttachments = attachments
            attachmentCount = 1u
            subpassCount = 1u
            pSubpasses = subpass.ptr
            dependencyCount = 1u
            pDependencies = dependencies
        }
        
        var renderPassRef = alloc<VkRenderPassVar>()

        if ( vkCreateRenderPass(vk_ldevice, renderPassInfo.ptr, null, renderPassRef.ptr) != VK_SUCCESS)
            throw RuntimeException("Failed to create Renderpass")
        
        println("after vkCreateRenderPass")
        
        
        //#12 Create Image Views
        memScoped {
            for (i in 0 until swapChainImagesCount.value.toInt()) {
                val imageViewCreateInfo: VkImageViewCreateInfo = alloc<VkImageViewCreateInfo>().apply {
                    sType = VK_STRUCTURE_TYPE_IMAGE_VIEW_CREATE_INFO
                    viewType = VK_IMAGE_VIEW_TYPE_2D
                    format = swapChainFormat
                    components.r = VK_COMPONENT_SWIZZLE_IDENTITY
                    components.g = VK_COMPONENT_SWIZZLE_IDENTITY
                    components.b = VK_COMPONENT_SWIZZLE_IDENTITY
                    components.a = VK_COMPONENT_SWIZZLE_IDENTITY
                    subresourceRange.aspectMask = VK_IMAGE_ASPECT_COLOR_BIT
                    subresourceRange.baseMipLevel = 0u
                    subresourceRange.levelCount = 1u
                    subresourceRange.baseArrayLayer = 0u
                    subresourceRange.layerCount = 1u
                    pNext = null
                    flags = 0u
                    image = swapChainImages[i]
                }
                
                val imageView = alloc<VkImageViewVar>()
                
                if (vkCreateImageView(vk_ldevice, imageViewCreateInfo.ptr, null, imageView.ptr) != VK_SUCCESS)
                    throw Error("image view(${i}) error!!")
                
                swapChainImageViews.add(imageView.value!!)
            }
        }
        
        println("Swap chain view count " + swapChainImageViews.size);
        
        //#13 Create Frame Buffers
        memScoped {
            
            for (i in 0 until swapChainImageViews.size) {
                val attachments1 = allocArray<VkImageViewVar>(1)
                attachments1[0] = swapChainImageViews[i]
                
                val frameBufferCreateInfo = alloc<VkFramebufferCreateInfo>().apply {
                    sType = VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO
                    renderPass = renderPassRef.value
                    attachmentCount = 1u
                    pAttachments = attachments1
                    width = swapChainExtent.width
                    height = swapChainExtent.height
                    layers = 1u
                }
                
                var framebuffer = alloc<VkFramebufferVar>()
                
                if (    vkCreateFramebuffer(
                                vk_ldevice,
                                frameBufferCreateInfo.ptr,
                                null,
                                framebuffer.ptr
                         ) != VK_SUCCESS)
                    throw Error("failed to create framebuffer(${i}) !");
                
                swapChainFramebuffers.add(framebuffer.value!!)
                
            }
            
            println("after vkCreateFramebuffer")
        }
        
        
        //#14 Create Graphics Pipeline
        
        val cwd = ByteArray(1024)
        cwd.usePinned {
            getcwd(it.addressOf(0), 1024)
        }
        
        var vertShaderModule = loadShader("${cwd.toKString()}\\shaders\\vert.spv", vk_ldevice)
        var fragShaderModule = loadShader("${cwd.toKString()}\\shaders\\frag.spv", vk_ldevice)
        
        val shaderStages = allocArray<VkPipelineShaderStageCreateInfo>(2)

        shaderStages[0].sType = VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO
        shaderStages[0].stage = VK_SHADER_STAGE_VERTEX_BIT
        shaderStages[0].module = vertShaderModule
        shaderStages[0].pName = "main".cstr.ptr
        
        shaderStages[1].sType = VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO
        shaderStages[1].stage = VK_SHADER_STAGE_FRAGMENT_BIT
        shaderStages[1].module = fragShaderModule
        shaderStages[1].pName = "main".cstr.ptr
        
        val vertexInputInfo = alloc<VkPipelineVertexInputStateCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO
            vertexBindingDescriptionCount = 0u
            pVertexBindingDescriptions = null
            vertexAttributeDescriptionCount = 0u
            pVertexAttributeDescriptions = null
        }
        
        val inputAssembly = alloc<VkPipelineInputAssemblyStateCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_PIPELINE_INPUT_ASSEMBLY_STATE_CREATE_INFO
            topology = VK_PRIMITIVE_TOPOLOGY_TRIANGLE_LIST
            primitiveRestartEnable = VK_FALSE.toUInt()
        }
        
        val viewport1 = alloc<VkViewport>().apply {
            x = 0.0f
            y = 0.0f
            width = swapChainExtent.width.toFloat()
            height = swapChainExtent.height.toFloat()
            minDepth = 0f
            maxDepth = 1f
        }

        val scissor1 = alloc<VkRect2D>().apply {
            offset.x = 0
            offset.y = 0
            extent.width = swapChainExtent.width
            extent.height = swapChainExtent.height
        }

        val viewportState = alloc<VkPipelineViewportStateCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_PIPELINE_VIEWPORT_STATE_CREATE_INFO
            viewportCount = 1u
            scissorCount = 1u
            pViewports = viewport1.ptr
            pScissors = scissor1.ptr
        }

        val rasterizer = alloc<VkPipelineRasterizationStateCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_PIPELINE_RASTERIZATION_STATE_CREATE_INFO
            depthClampEnable = VK_FALSE.toUInt()
            rasterizerDiscardEnable = VK_FALSE.toUInt()
            polygonMode = VK_POLYGON_MODE_FILL
            cullMode = VK_CULL_MODE_BACK_BIT
            frontFace = VK_FRONT_FACE_COUNTER_CLOCKWISE
            depthBiasEnable = VK_FALSE.toUInt()
            lineWidth = 1.0f
        }

        val multisampling = alloc<VkPipelineMultisampleStateCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_PIPELINE_MULTISAMPLE_STATE_CREATE_INFO
            rasterizationSamples = VK_SAMPLE_COUNT_1_BIT
            pSampleMask = null
            sampleShadingEnable = VK_FALSE.toUInt()
        }

        val blendAttachmentState = allocArray<VkPipelineColorBlendAttachmentState>(1)
        
        blendAttachmentState[0].apply {
            blendEnable = VK_FALSE.toUInt()
            srcColorBlendFactor = VK_BLEND_FACTOR_ONE
            dstColorBlendFactor = VK_BLEND_FACTOR_ZERO
            colorBlendOp = VK_BLEND_OP_ADD
            srcAlphaBlendFactor = VK_BLEND_FACTOR_ONE
            dstAlphaBlendFactor = VK_BLEND_FACTOR_ZERO
            alphaBlendOp = VK_BLEND_OP_ADD
            colorWriteMask = VK_COLOR_COMPONENT_R_BIT or VK_COLOR_COMPONENT_G_BIT or VK_COLOR_COMPONENT_B_BIT or VK_COLOR_COMPONENT_A_BIT
        }

        val colorBlending = alloc<VkPipelineColorBlendStateCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_PIPELINE_COLOR_BLEND_STATE_CREATE_INFO
            attachmentCount = 1u
            pAttachments = blendAttachmentState
            logicOpEnable = VK_FALSE.toUInt()
            logicOp = VK_LOGIC_OP_COPY
            blendConstants[0] = 0.0f
            blendConstants[1] = 0.0f
            blendConstants[2] = 0.0f
            blendConstants[3] = 0.0f
        }
        
        val pipelineLayoutCreateInfo = alloc<VkPipelineLayoutCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO
            pNext = null
            setLayoutCount = 0u
            pSetLayouts = null
        }
        
        var pipelineLayout: VkPipelineLayoutVar = alloc()

        if (
                vkCreatePipelineLayout(
                    vk_ldevice,
                    pipelineLayoutCreateInfo.ptr,
                    null,
                    pipelineLayout.ptr
                )
            != VK_SUCCESS
        )
        throw Error("Pipeline layout failed!!")
            
        println("after vkCreatePipelineLayout")
        
        val pipelineCreateInfo = alloc<VkGraphicsPipelineCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_GRAPHICS_PIPELINE_CREATE_INFO
            stageCount = 2u
            pStages = shaderStages
            pVertexInputState = vertexInputInfo.ptr
            pInputAssemblyState = inputAssembly.ptr
            pViewportState = viewportState.ptr
            pRasterizationState = rasterizer.ptr
            pMultisampleState = multisampling.ptr
            pColorBlendState = colorBlending.ptr
            pDepthStencilState = null
            pDynamicState = null
            layout = pipelineLayout.value
            renderPass = renderPassRef.value
            basePipelineHandle = null
            basePipelineIndex = (-1).toInt()
        }

        var graphicsPipeline = alloc<VkPipelineVar>()
        
        if (
                vkCreateGraphicsPipelines(
                    vk_ldevice,
                    null,
                    1u,
                    pipelineCreateInfo.ptr,
                    null,
                    graphicsPipeline.ptr
                )
            != VK_SUCCESS
        )
        throw RuntimeException("failed create pipeline")
        
        vkDestroyShaderModule(vk_ldevice, shaderStages[0].module, null)
        vkDestroyShaderModule(vk_ldevice, shaderStages[1].module, null)
        
        println("Graphics pipeline created successfully")
        
        //#15 Create Descriptor Pool
        
        //IGNORED for this simple demo
        
        //#16 Create Descriptor Set
        
        //IGNORED for this simple demo

        //#17 Create Command Buffers
        
        var graphicsCommandBuffers = allocArray<VkCommandBufferVar>(swapChainImagesCount.value.toInt())
        
        val commandBufferAllocateInfo = alloc<VkCommandBufferAllocateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO
            commandPool = commandPoolVar.value
            level = VK_COMMAND_BUFFER_LEVEL_PRIMARY
            commandBufferCount = swapChainImagesCount.value.toUInt()
        }
        println("before vkAllocateCommandBuffers")
        
        if ( vkAllocateCommandBuffers(vk_ldevice, commandBufferAllocateInfo.ptr, graphicsCommandBuffers) != VK_SUCCESS)
            throw RuntimeException("Failed to create graphicsCommandBuffers")
            
        val graphicsCmdBufferBeginInfo = alloc<VkCommandBufferBeginInfo>().apply {
            sType = VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO
            flags = VK_COMMAND_BUFFER_USAGE_SIMULTANEOUS_USE_BIT
            pInheritanceInfo = null
        }
        
        val clearColor = allocArray<VkClearValue>(1)
        clearColor[0].color.float32.apply {
            this[0] = 0.0f
            this[1] = 0.0f
            this[2] = 0.2f
            this[3] = 1.0f
        }
        
        for (i in 0 until swapChainImages.size) {
            vkBeginCommandBuffer(graphicsCommandBuffers[i], graphicsCmdBufferBeginInfo.ptr)

            val renderPassBeginInfo = alloc<VkRenderPassBeginInfo>().apply {
                sType = VK_STRUCTURE_TYPE_RENDER_PASS_BEGIN_INFO
                pNext = null
                renderPass = renderPassRef.value
                renderArea.offset.x = 0
                renderArea.offset.y = 0
                renderArea.extent.width = swapChainExtent.width.toUInt()
                renderArea.extent.height = swapChainExtent.height.toUInt()
                clearValueCount = 1u
                pClearValues = clearColor
                framebuffer = swapChainFramebuffers[i]
            }
            
            vkCmdBeginRenderPass(graphicsCommandBuffers[i], renderPassBeginInfo.ptr, 
            VK_SUBPASS_CONTENTS_INLINE)
            
            vkCmdBindPipeline(graphicsCommandBuffers[i], VK_PIPELINE_BIND_POINT_GRAPHICS, 
            graphicsPipeline.value)
            
            val offset = allocArray<VkDeviceSizeVar>(1) { _: Int ->
                this.value = 0u
            }
            
            vkCmdBindVertexBuffers(graphicsCommandBuffers[i], 0u, 1u, vOBuffer.ptr, offset)

            vkCmdBindIndexBuffer(graphicsCommandBuffers[i], iOBuffer.value, 0u, VK_INDEX_TYPE_UINT32)

            vkCmdDrawIndexed(graphicsCommandBuffers[i], indicesCount.toUInt(), 1u, 0u, 0, 1u)
            
            vkCmdEndRenderPass(graphicsCommandBuffers[i])
            
            if (vkEndCommandBuffer(graphicsCommandBuffers[i]) != VK_SUCCESS) {
                throw Error("failed to record command buffer");
            }
        }
        println("recorded command buffers")
        vkDestroyPipelineLayout(vk_ldevice, pipelineLayout.value, null)
        
        
        //#18 Draw
        while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && glfwWindowShouldClose(window) == 0) {
            
            var imageIndex = alloc<UIntVar>()
            
            var res1 =  vkAcquireNextImageKHR(
                vk_ldevice,
                vk_swapchain,
                vulkan.UINT64_MAX,
                imageAvailableSemaphore.value,
                null,
                imageIndex.ptr
            )
            
            if(res1 != VK_SUCCESS) {
                throw Error("failed to acquire image")
            }
            
            var drawSubmitInfo = alloc<VkSubmitInfo>()
            drawSubmitInfo.sType = VK_STRUCTURE_TYPE_SUBMIT_INFO
    
            drawSubmitInfo.waitSemaphoreCount = 1u
            drawSubmitInfo.pWaitSemaphores = imageAvailableSemaphore.ptr
    
            drawSubmitInfo.signalSemaphoreCount = 1u
            drawSubmitInfo.pSignalSemaphores = renderingFinishedSemaphore.ptr
            
            val drawStageWaitFlags = alloc<VkPipelineStageFlagsVar>()
            drawStageWaitFlags.value = VK_PIPELINE_STAGE_TOP_OF_PIPE_BIT
            
            drawSubmitInfo.pWaitDstStageMask = drawStageWaitFlags.ptr
            
            var graphicsCmdBuffer = alloc<VkCommandBufferVar>()
            graphicsCmdBuffer.value = graphicsCommandBuffers[imageIndex.value.toInt()]
            
            drawSubmitInfo.commandBufferCount = 1u
            drawSubmitInfo.pCommandBuffers = graphicsCmdBuffer.ptr
            
            if (vkQueueSubmit(graphicsQueue.value, 1u, drawSubmitInfo.ptr, null) != VK_SUCCESS) {
                throw Error("failed to submit draw command buffer")
            }
            
            var drawPresentInfo = alloc<VkPresentInfoKHR>()
            drawPresentInfo.sType = VK_STRUCTURE_TYPE_PRESENT_INFO_KHR
            drawPresentInfo.waitSemaphoreCount = 1u
            drawPresentInfo.pWaitSemaphores = renderingFinishedSemaphore.ptr
            
            val swapChains = allocArray<VkSwapchainKHRVar>(1.toInt()) {
                this.value = vk_swapchain
            }
                            
            drawPresentInfo.swapchainCount = 1u
            drawPresentInfo.pSwapchains = swapChains
            drawPresentInfo.pImageIndices = imageIndex.ptr;
            
            if (vkQueuePresentKHR(presentQueue.value, drawPresentInfo.ptr) != VK_SUCCESS) {
                throw Error("failed to submit present command buffer")
            }
            
            glfwSwapBuffers(window)
            glfwPollEvents()
        }
        
        if (commandPoolVar.value != null)
            vkDestroyCommandPool(vk_ldevice, commandPoolVar.value, null)

            vkDestroyDevice(vk_ldevice, null)
            
        if (callback.value != null) {
            println("Destroying callback")
            var kvkDestroyDebugReportCallbackEXT: PFN_vkDestroyDebugReportCallbackEXT? = null

            vkGetInstanceProcAddr(vk_instance, "vkDestroyDebugReportCallbackEXT")?.let { voidFun ->
                @Suppress("UNCHECKED_CAST")
                kvkDestroyDebugReportCallbackEXT = voidFun as PFN_vkDestroyDebugReportCallbackEXT
            }

            kvkDestroyDebugReportCallbackEXT?.let {
                it(vk_instance, callback.value, null)
            }
        }
        
        vkDestroyInstance(vk_instance, null)
        
        glfwTerminate()       
}