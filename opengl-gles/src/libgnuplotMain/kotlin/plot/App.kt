package plot

import kotlinx.cinterop.*
import glfw.*
import gles.*

const val vertex_shader = """
attribute vec3 position;
void main() {
    gl_Position = vec4(position, 1.0);
}
"""

const val fragment_shader = """
precision mediump float;
void main(){
  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
"""

fun main() {

    if (glfwInit() == gles.GL_FALSE) {
        throw Error("Failed to initialize GLFW")
    }
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0)
	glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API)
    glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_EGL_CONTEXT_API)
    val window = glfwCreateWindow(640, 480, "OpenGLESv2", null, null) ?:
    throw Error("Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.")

    glfwMakeContextCurrent(window)
	
    glfwSetWindowTitle(window, "OpenGLESv2 2.0 " )
	
	glfwSetInputMode(window, GLFW_STICKY_KEYS, gles.GL_TRUE)
	
    val pId = memScoped {
        val vsId = gles.glCreateShader(gles.GL_VERTEX_SHADER.toUInt())
        val fsId = gles.glCreateShader(gles.GL_FRAGMENT_SHADER.toUInt())

        gles.glShaderSource(vsId.toUInt(), 1, arrayOf(vertex_shader).toCStringArray(memScope), null)
        gles.glCompileShader(vsId.toUInt())

        gles.glShaderSource(fsId.toUInt(), 1, arrayOf(fragment_shader).toCStringArray(memScope), null)
        gles.glCompileShader(fsId.toUInt())

        val pId = gles.glCreateProgram()
        
        gles.glAttachShader(pId.toUInt(), vsId.toUInt())
        gles.glAttachShader(pId.toUInt(), fsId.toUInt())
        gles.glLinkProgram(pId)

        gles.glDetachShader(pId.toUInt(), vsId.toUInt())
        gles.glDetachShader(pId.toUInt(), fsId.toUInt())

        gles.glDeleteShader(vsId.toUInt())
        gles.glDeleteShader(fsId.toUInt())

        pId
    }
    
	var pos = gles.glGetAttribLocation(pId, "position").toUInt()
	
	gles.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
	gles.glViewport(0, 0, 640, 480)
	
	val vbo = memScoped {
        val output = alloc<UIntVar>()
        gles.glGenBuffers(1, output.ptr)
        output.value
    }
    gles.glBindBuffer(gles.GL_ARRAY_BUFFER.toUInt(), vbo)
    
    val vertex_array = floatArrayOf(
        0.0f,  0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
       -0.5f, -0.5f, 0.0f
    )
	
	gles.glBufferData(gles.GL_ARRAY_BUFFER.toUInt(), vertex_array.size.toLong() * 4, vertex_array.refTo(0), gles.GL_STATIC_DRAW.toUInt())
	gles.glVertexAttribPointer(pos, 3, gles.GL_FLOAT, gles.GL_FALSE, 0, 0L.toCPointer<CPointed>());
    gles.glEnableVertexAttribArray(pos);
    gles.glBindBuffer(gles.GL_ARRAY_BUFFER, 0);
    
    while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && glfwWindowShouldClose(window) == 0) {
        gles.glClear(gles.GL_COLOR_BUFFER_BIT.toUInt())
		
		gles.glUseProgram(pId)
        gles.glDrawArrays(gles.GL_TRIANGLES.toUInt(), 0, 3)
        
        glfwSwapBuffers(window)
        glfwPollEvents()
    }
	
    glfwTerminate()
}
