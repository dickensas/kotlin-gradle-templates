package plot

import glad.*
import glfw.*
import kotlinx.cinterop.*

const val vertex_shader = """
#version 410 core
layout(location = 0) in vec3 vertexPosition_modelspace;
void main() {
    gl_Position.xyz = vertexPosition_modelspace;
    gl_Position.w = 1.0;
}
"""

const val fragment_shader = """
#version 410 core
out vec3 color;
void main(){
  color = vec3(1,0,0);
}
"""

fun main(args: Array<String>) {
    if (glfwInit() == GL_FALSE) {
        throw Error("Failed to initialize GLFW")
    }

    glfwWindowHint(GL_SAMPLES, 4)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    val window = glfwCreateWindow(640, 480, "Learn With Dickens", null, null) ?:
        throw Error("Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.")

    glfwMakeContextCurrent(window)
    
    if (gladLoadGL() != 1) {
        throw Error("Failed to initialize GLAD")
    }
    
    val vao:UInt = memScoped {
        val output = alloc<UIntVar>()
        glGenVertexArrays!!(1, output.ptr)
        output.value
    }
    glBindVertexArray!!(vao)
    
    val vbo:UInt = memScoped {
        val output = alloc<UIntVar>()
        glGenBuffers!!(1, output.ptr)
        output.value
    }
    glBindBuffer!!(GL_ARRAY_BUFFER.toUInt(), vbo)
    
    val ebo:UInt = memScoped {
        val output = alloc<UIntVar>()
        glGenBuffers!!(1, output.ptr)
        output.value
    }
    glBindBuffer!!(GL_ELEMENT_ARRAY_BUFFER.toUInt(), ebo)

    glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)
    
    val vertex_array = floatArrayOf(
         0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,
         0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,
        -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,
        -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f
    )
    
    val index_array: IntArray = intArrayOf(
        0, 1, 3,
        1, 2, 3 
    )

    vertex_array.usePinned {
        glBufferData!!(GL_ARRAY_BUFFER.toUInt(), (vertex_array.size * FloatVar.size).signExtend(), it.addressOf(0), GL_STATIC_DRAW.toUInt())
    }
    
    index_array.usePinned {
        glBufferData!!(GL_ELEMENT_ARRAY_BUFFER.toUInt(), (index_array.size * FloatVar.size).signExtend(), it.addressOf(0), GL_STATIC_DRAW.toUInt())
    }
    
    glVertexAttribPointer!!(0u, 3, GL_FLOAT.toUInt(), false.toByte().toUByte(), (8 * FloatVar.size).toInt(), null)
    glEnableVertexAttribArray!!(0U);

    val colorStartPosition: Long = 6 * FloatVar.size
    glVertexAttribPointer!!(1U, 3, GL_FLOAT.toUInt(), false.toByte().toUByte(), (8 * FloatVar.size).toInt(), colorStartPosition.toCPointer<COpaque>())
    glEnableVertexAttribArray!!(1U);

    val textureStartPosition: Long = 6 * FloatVar.size
    glVertexAttribPointer!!(2U, 2, GL_FLOAT.toUInt(), false.toByte().toUByte(), (8 * FloatVar.size).toInt(), textureStartPosition.toCPointer<COpaque>())
    glEnableVertexAttribArray!!(2U);

    val pId:UInt = memScoped {
        val vsId:UInt = glCreateShader!!(GL_VERTEX_SHADER.toUInt())
        val fsId:UInt = glCreateShader!!(GL_FRAGMENT_SHADER.toUInt())

        glShaderSource!!(vsId, 1, arrayOf(vertex_shader).toCStringArray(memScope), null)
        glCompileShader!!(vsId)

        glShaderSource!!(fsId, 1, arrayOf(fragment_shader).toCStringArray(memScope), null)
        glCompileShader!!(fsId)

        val pId:UInt = glCreateProgram!!()
        
        glAttachShader!!(pId, vsId)
        glAttachShader!!(pId, fsId)
        glLinkProgram!!(pId)

        glDetachShader!!(pId, vsId)
        glDetachShader!!(pId, fsId)

        glDeleteShader!!(vsId)
        glDeleteShader!!(fsId)

        pId
    }

    while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && glfwWindowShouldClose(window) == 0) {
        glClear!!(GL_COLOR_BUFFER_BIT.toUInt() or GL_DEPTH_BUFFER_BIT.toUInt())
        glClearColor!!(0.1f, 0.2f, 0.3f, 1.0f)
        
        glUseProgram!!(pId)
        
        glBindVertexArray!!(vao)
        glDrawElements!!(GL_TRIANGLES.toUInt(), index_array.size.toInt(), GL_UNSIGNED_INT.toUInt(), null)
        
        glfwSwapBuffers(window)
        glfwPollEvents()
    }

    glfwTerminate()
}