package plot

import vulkan.*
import kotlinx.cinterop.*

data class Result(var value:VkInstance?,var result:Int)

fun main() {

    val res = memScoped {
        val acInfo = alloc<VkInstanceCreateInfo>().apply {
            sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO
            pNext = null
            pApplicationInfo = null
            enabledLayerCount = 0u
            ppEnabledLayerNames = null
            enabledExtensionCount = 0U
            ppEnabledExtensionNames = null
        }
        
        val output = alloc<VkInstanceVar>()
        var res = vkCreateInstance(acInfo.ptr, null, output.ptr)
        Result(output.value,res)
    }
    
    if(res.result != VK_SUCCESS) {
        throw Error("Failed to initialize Vulkan")
    }
    
    println("Vulkan successfully initialized")
}