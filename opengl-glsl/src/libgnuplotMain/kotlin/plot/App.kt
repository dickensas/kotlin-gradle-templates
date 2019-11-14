package plot

import kotlinx.cinterop.*
import glew.*
import glfw.*

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

fun main() {

    glewExperimental = 1u
    
    if (glfwInit() == GL_FALSE) {
        throw Error("Failed to initialize GLFW")
    }

    glfwWindowHint(GL_SAMPLES, 4)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    val window = glfwCreateWindow(640, 480, "OpenGL", null, null) ?:
    throw Error("Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.")

    glfwMakeContextCurrent(window)
    glewExperimental = 1u

    if (glewInit() != 0u) {
        throw Error("Failed to initialize GLEW")
    }
    
    var version = glGetString(GL_VERSION)?.pointed?.value as UByte;
    
    glfwSetWindowTitle(window, "OpenGL " + version.toByte().toChar());
    
    val vao = memScoped {
        val output = alloc<UIntVar>()
        glGenVertexArrays!!(1, output.ptr)
        output.value
    }
    glBindVertexArray!!(vao)
    
    val vbo = memScoped {
        val output = alloc<UIntVar>()
        glGenBuffers!!(1, output.ptr)
        output.value
    }
    glBindBuffer!!(GL_ARRAY_BUFFER.toUInt(), vbo)

    glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)
    
    val vertex_array = floatArrayOf(
        -0.8f, -0.8f, 0.0f,
         0.8f, -0.8f, 0.0f,
         0.0f,  0.8f, 0.0f
    )
    
    vertex_array.usePinned {
        glBufferData!!(GL_ARRAY_BUFFER.toUInt(), vertex_array.size.toLong() * 4, it.addressOf(0), GL_STATIC_DRAW.toUInt())
    }

    val pId = memScoped {
        val vsId = glCreateShader!!(GL_VERTEX_SHADER.toUInt())
        val fsId = glCreateShader!!(GL_FRAGMENT_SHADER.toUInt())

        glShaderSource!!(vsId.toUInt(), 1, arrayOf(vertex_shader).toCStringArray(memScope), null)
        glCompileShader!!(vsId.toUInt())

        glShaderSource!!(fsId.toUInt(), 1, arrayOf(fragment_shader).toCStringArray(memScope), null)
        glCompileShader!!(fsId.toUInt())

        val pId = glCreateProgram!!()
        
        glAttachShader!!(pId.toUInt(), vsId.toUInt())
        glAttachShader!!(pId.toUInt(), fsId.toUInt())
        glLinkProgram!!(pId)

        glDetachShader!!(pId.toUInt(), vsId.toUInt())
        glDetachShader!!(pId.toUInt(), fsId.toUInt())

        glDeleteShader!!(vsId.toUInt())
        glDeleteShader!!(fsId.toUInt())

        pId
    }

    while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && glfwWindowShouldClose(window) == 0) {
        glClear(GL_COLOR_BUFFER_BIT.toUInt() or GL_DEPTH_BUFFER_BIT.toUInt())
        glClearColor(0.1f, 0.2f, 0.3f, 1.0f)
        
        glUseProgram!!(pId)
        
        glEnableVertexAttribArray!!(0U)
        glBindBuffer!!(GL_ARRAY_BUFFER.toUInt(), vbo)
        glVertexAttribPointer!!(
            0U,
            3,
            GL_FLOAT.toUInt(),
            false.toByte().toUByte(),
            0,
            0L.toCPointer<CPointed>()
        )
        
        glDrawArrays(GL_TRIANGLES.toUInt(), 0, 3)
        glDisableVertexAttribArray!!(0U)
        
        glfwSwapBuffers(window)
        glfwPollEvents()
    }

    glfwTerminate()
}
