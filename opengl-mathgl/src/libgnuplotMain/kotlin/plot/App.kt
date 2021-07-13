package plot

import glew.*
import glfw.*
import mgl.*
import kotlin.math.*
import kotlinx.cinterop.*
import kotlinx.cinterop.internal.*

var WINDOW_WIDTH = 640
var WINDOW_HEIGHT = 480

var rnd:HMDT? = null;
var in1:HMDT? = null;
var res:HMDT? = null;
var gr:HMGL? = null;

fun main(args: Array<String>) = memScoped {

    if (glfwInit() == GL_FALSE) {
        throw Error("Failed to initialize GLFW")
    }

    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)
    val window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Learn With Dickens", null, null) ?:
        throw Error("Failed to open GLFW window. If you have an Intel GPU, they are not 3.3 compatible. Try the 2.1 version of the tutorials.")

    glfwMakeContextCurrent(window)

    glewExperimental = 1u
    if (glewInit() != 0u) {
        throw Error("Failed to initialize GLAD")
    }
    
    //#0 Enable Blending and prepare the OpenGL
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
    glDisable(GL_DEPTH_TEST.toUInt())
    glEnable(GL_BLEND.toUInt())
    glBlendFunc(GL_SRC_ALPHA.toUInt(), GL_ONE_MINUS_SRC_ALPHA.toUInt())
    glEnable(GL_TEXTURE_2D.toUInt())
    glViewport(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT)
    glMatrixMode(GL_PROJECTION.toUInt())
    glLoadIdentity()
    glOrtho(0.toDouble(), WINDOW_WIDTH.toDouble(), WINDOW_HEIGHT.toDouble(), 0.toDouble(), (-1).toDouble(), 1.toDouble())
    glMatrixMode(GL_MODELVIEW.toUInt())
    glLoadIdentity()
    
    rnd = mgl_create_data_size(100,1,1);
    in1 = mgl_create_data_size(100,1,1);
    gr = mgl_create_graph(WINDOW_WIDTH, WINDOW_HEIGHT);
    mgl_data_modify(rnd,"0.4*rnd+0.1+sin(6*pi*x)",0);
	mgl_data_modify(in1,"0.3+sin(6*pi*x)",0);
	mgl_plot(gr,rnd,". ","");
	mgl_plot(gr,in1,"b","");
	mgl_box(gr);
	
	val w=mgl_get_width(gr)
	val h=mgl_get_height(gr);
    
    var channels = 4
    
    var surface_data = allocArray<UByteVar>((channels * w * h).toInt() * (sizeOf<UByteVar>()).toInt())
    
	var buf = mgl_get_rgba(gr);
	platform.posix.memcpy(surface_data, buf,(channels * w * h).toULong());
        
    println("awidth: " + w)
    println("aheight: " + h)
    
    //#9 create a opengl texture ID
    var tId:UIntVar= alloc<UIntVar>()
    glGenTextures(1, tId.ptr)
    glEnable(GL_TEXTURE_2D.toUInt())
    glBindTexture(GL_TEXTURE_2D.toUInt(), tId.value)
    
    //#10 load the text in to OpenGL as texture for any shape
    glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_BASE_LEVEL.toUInt(), 0);
    glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_MAX_LEVEL.toUInt(), 0);
    glTexImage2D(
          GL_TEXTURE_2D.toUInt(),
          0,
          GL_RGBA,
          w,
          h,
          0,
          GL_BGRA.toUInt(),
          GL_UNSIGNED_BYTE.toUInt(),
          surface_data
    )
    
    glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)
    while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && glfwWindowShouldClose(window) == 0) {
        
        //#11 paint a rectangle with the above texture
        glBegin(GL_QUADS.toUInt())
            glTexCoord2f(0.0f, 0.0f)
            glVertex2f(0.0f, 0.0f)
            glTexCoord2f(1.0f, 0.0f)
            glVertex2f(w.toFloat(), 0.0f)
            glTexCoord2f(1.0f, 1.0f)
            glVertex2f(w.toFloat() , h.toFloat())
            glTexCoord2f(0.0f, 1.0f)
            glVertex2f(0.0f, h.toFloat())
        glEnd()
    
        glfwSwapBuffers(window)
        glfwPollEvents()
    }
    
    glfwTerminate()
    
}