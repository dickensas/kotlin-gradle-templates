package plot

import platform.posix.*
import kotlinx.cinterop.*
import kotlin.native.concurrent.DetachedObjectGraph
import kotlin.native.concurrent.attach

import vulkan.*
import glfw.*

fun surfaceInit(extensions: MutableList<String>, window:CValuesRef<GLFWwindow>?, vk_instance:VkInstance?):VkSurfaceKHR? {
	if (
        !extensions.contains("VK_KHR_surface") || 
        !extensions.contains("VK_KHR_xcb_surface")
    )
    throw RuntimeException("Needed extensions not supported")
    
    //#3 create Vulcan surface
    
    return memScoped {
        var output = alloc<VkSurfaceKHRVar>()
        val createInfo = alloc<VkXcbSurfaceCreateInfoKHR> {
            sType = VK_STRUCTURE_TYPE_XCB_SURFACE_CREATE_INFO_KHR
            pNext = null
            flags = 0u
			hwnd = glfwGetGLXWindow(window) as vulkan.HWND
        }
        var result = vkCreateXcbSurfaceKHR(vk_instance, createInfo.ptr, null, output.ptr)
        
        if(result != VK_SUCCESS)
            throw RuntimeException("Failed to create vk surface")
            
        output.value
    }
}