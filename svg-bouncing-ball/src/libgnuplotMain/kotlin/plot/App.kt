package plot

import glew.*
import glfw.*
import rsvg.*
import kotlin.math.*
import kotlinx.cinterop.*
import platform.posix.*

var WINDOW_WIDTH = 320
var WINDOW_HEIGHT = 240
var aspect = WINDOW_WIDTH.toFloat()/WINDOW_HEIGHT.toFloat()
var ballRadious = 0.5f
var ballX = 0.0f
var ballY = 0.0f
var clipAreaXLeft = -1.0f * aspect
var clipAreaXRight = 1.0f * aspect
var clipAreaYBottom = -1.0f
var clipAreaYTop = 1.0f
var ballXMin = clipAreaXLeft + ballRadious
var ballXMax = (clipAreaXRight + ballRadious) * 160f
var ballYMin = clipAreaYBottom + ballRadious
var ballYMax = (clipAreaYTop + ballRadious) * 145f
var xSpeed = 1.2f
var ySpeed = 1.2f
var refreshMillis = 30

fun main() = memScoped {

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
    glClearColor(1.0f, 1.0f, 1.0f, 0.0f)
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
    
    //#read the svg file
    var error = alloc<CPointerVar<GError>>()
    var handle = rsvg_handle_new_from_file ("ball.svg", error.ptr);
    if(handle==null)
        throw Error("unable to load ball.svg")
    if(error.value!=null)
        throw Error("unable to process ball.svg")
    
    var awidth = 25
    var aheight = 25
    
    //#6 create cairo render context
    var channels = 4
    
    var surface_data = allocArray<UByteVar>((channels * awidth * aheight).toInt() * (sizeOf<UByteVar>()).toInt())
    var surface = cairo_image_surface_create_for_data (surface_data, CAIRO_FORMAT_ARGB32, awidth, aheight, channels * awidth)
    var render_context = cairo_create (surface)
    
    //#7 paint the svg in cairo which will affect the surface_data
    if(rsvg_handle_render_cairo(handle, render_context)!=1)
        throw Error("drawing failed")
    
    //#8 We have the surface_data, now destroy cairo
    cairo_destroy (render_context)
    cairo_surface_destroy (surface)
    
    println("awidth: " + awidth)
    println("aheight: " + aheight)
    
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
          GL_RGBA8,
          awidth,
          aheight,
          0,
          GL_BGRA.toUInt(),
          GL_UNSIGNED_BYTE.toUInt(),
          surface_data
    )
        
  glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)
  
  
  while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && glfwWindowShouldClose(window) == 0) {
    glClear( (GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    
    glTranslatef(ballX, ballY, 0.0f)
    
    //#11 paint a rectangle with the above texture
    glBegin(GL_QUADS.toUInt())
        glTexCoord2f(0.0f, 0.0f)
        glVertex2f(0.0f, 0.0f)
        glTexCoord2f(1.0f, 0.0f)
        glVertex2f(awidth.toFloat(), 0.0f)
        glTexCoord2f(1.0f, 1.0f)
        glVertex2f(awidth.toFloat(), aheight.toFloat())
        glTexCoord2f(0.0f, 1.0f)
        glVertex2f(0.0f, aheight.toFloat())
    glEnd()
        
    glfwSwapBuffers(window)
    glfwPollEvents()
    
    ballX = ballX + xSpeed
    ballY = ballY + ySpeed
    if(ballX > ballXMax) {
        ballX = ballXMax
        xSpeed = -xSpeed
    }else if(ballX < ballXMin) {
        ballX = ballXMin
        xSpeed = -xSpeed
    }else if(ballY > ballYMax) {
        ballY = ballYMax
        ySpeed = -ySpeed
    }else if(ballY < ballYMin) {
        ballY = ballYMin
        ySpeed = -ySpeed
    }
    
  }
  
  glfwTerminate()
}