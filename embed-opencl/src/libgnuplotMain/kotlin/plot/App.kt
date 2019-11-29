package plot

import kotlinx.cinterop.*
import opencl.*
import platform.posix.intptr_t
import platform.posix.size_tVar

var code = 
"""
#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable
__constant char hw[] = "Hello World";
__kernel void hello(__global char * A) {
    size_t tid = get_global_id(0);
    A[tid] = hw[tid];
}
"""

fun main() {
    
    println ("code which will call opencl");
    
    memScoped {
        var platform = alloc<cl_platform_idVar>()
        var device =  alloc<cl_device_idVar>()
        var props = allocArray<cl_context_propertiesVar>(3)
        props[0] = CL_CONTEXT_PLATFORM.toLong()
        props[1] = 0.toLong()
        props[2] = 0.toLong()
        
        clGetPlatformIDs( 1, platform.ptr, null )
        var err = clGetDeviceIDs( platform.value, CL_DEVICE_TYPE_GPU, 1, device.ptr, null )
        if (err != CL_SUCCESS) { throw Error("Unable to enumerate device IDs") }
        props[1] = platform.value.toLong()
        
        var errCtx = memScoped { alloc<cl_intVar>() }
        var ctx = clCreateContext( props, 1, device.ptr, null, null, errCtx.ptr )
        if (ctx == null || errCtx.value != 0) { throw Error("Unable to create context") }
        
        var queue = clCreateCommandQueue( ctx, device.value, 0, errCtx.ptr )
        if (queue == null || errCtx.value != 0) { throw Error("Unable to create queue") }
        
        var program = clCreateProgramWithSource( ctx, 1, arrayOf(code).toCStringArray(memScope), null, errCtx.ptr)
        if (program == null || errCtx.value != 0) { throw Error("Unable to create program") }
        
        err = clBuildProgram( program, 0, null, null, null, null)
        if (err != CL_SUCCESS) { throw Error("Unable to build program") }
        
        var kernel = clCreateKernel( program, "hello", errCtx.ptr)
        if (kernel == null || errCtx.value != 0) { throw Error("Unable to create kernel") }
        
        var N = alloc<size_tVar>()
        N.value = 12.toULong()
        var B = allocArray<ByteVar>((N.value.toInt()+1 * sizeOf<ByteVar>()).toInt() )
        
        var output = alloc<cl_memVar>()
        output.value = clCreateBuffer( ctx, CL_MEM_WRITE_ONLY.toULong(), (N.value.toInt()+1 * sizeOf<ByteVar>()).toULong(), null, errCtx.ptr)
        if (errCtx.value != 0) { throw Error("Unable to create buffer") }
        
        err = clSetKernelArg(kernel, 0, (sizeOf<cl_memVar>()).toULong(), output.ptr)
        if (err != CL_SUCCESS) { throw Error("Unable to set arguments") }
        
        clEnqueueNDRangeKernel( queue, kernel, 1, null, N.ptr, N.ptr, 0, null, null)
        
        clFinish(queue)
        
        clEnqueueReadBuffer( queue, output.value, CL_TRUE, 0, (N.value.toInt()+1 * sizeOf<ByteVar>()).toULong(), B, 0, null, null );
        clFinish(queue);
       
        println(B.toKString())
        
        clReleaseMemObject( output.value )
        clReleaseCommandQueue ( queue )
        clReleaseContext ( ctx )
    }
}