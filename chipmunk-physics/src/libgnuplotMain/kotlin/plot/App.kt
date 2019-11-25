package plot

import glew.*
import glfw.*
import rsvg.*
import chipmunk.*
import kotlin.math.*
import kotlinx.cinterop.*
import platform.posix.*

var WINDOW_WIDTH = 320
var WINDOW_HEIGHT = 240
var ballX = 0.0f
var ballY = 0.0f
var mass = 1.0
var UPDATE_INTERVAL = 1.0/60.0
var timeAccumulator = 3.0

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
    
    var dimension_data = alloc<RsvgDimensionData>()
    rsvg_handle_get_dimensions(handle, dimension_data.ptr)
    
    var awidth = dimension_data.width
    var aheight = dimension_data.height
    
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
  
  //#set gravitiy
  var gravity = cpv(0.0, -WINDOW_HEIGHT.toDouble()/2.0)
  var space = cpSpaceNew()
  cpSpaceSetGravity(space, gravity)
 
  //#create a body for the shape
  var body = cpBodyNew(mass, cpMomentForBox(mass, awidth.toDouble(), aheight.toDouble()))
  cpBodySetPosition(body, cpv(WINDOW_WIDTH.toDouble()/2.0, WINDOW_HEIGHT.toDouble()-aheight.toDouble()))
  cpSpaceAddBody(space, body)
  
  var radius = aheight.toDouble()/2.0
  
  //#create the shape and add it to the body
  var shape = cpBoxShapeNew(body, awidth.toDouble(), aheight.toDouble(), radius)
  cpShapeSetElasticity(shape, 0.4)
  cpShapeSetFriction(shape, 0.5)
  cpSpaceAddShape(space, shape)
  
  run {
      //#create a fixed ground
      var lowerLeft = cpv(0.0, -30.0)
      var lowerRight = cpv(WINDOW_WIDTH.toDouble(), -30.0)
      var groundBody = cpBodyNewStatic()
      
      var line = cpSegmentShapeNew(groundBody, lowerLeft, lowerRight, radius)
      cpShapeSetElasticity(line, 1.0)
      cpShapeSetFriction(line, 1.0)
      cpSpaceAddShape(space, line)
  }
  
  run {
      //#create a secong fixed ground
      var lowerLeft = cpv(0.0, -30.0)
      var lowerRight = cpv(0.0, (WINDOW_HEIGHT.toDouble()+30.0))
      var groundBody = cpBodyNewStatic()
      
      var line = cpSegmentShapeNew(groundBody, lowerLeft, lowerRight, radius)
      cpShapeSetElasticity(line, 1.0)
      cpShapeSetFriction(line, 1.0)
      cpSpaceAddShape(space, line)
  }
  
  run {
      //#create a third fixed ground
      var lowerLeft = cpv(WINDOW_WIDTH.toDouble(), -30.0)
      var lowerRight = cpv(WINDOW_WIDTH.toDouble(), (WINDOW_HEIGHT.toDouble()+30.0))
      var groundBody = cpBodyNewStatic()
      
      var line = cpSegmentShapeNew(groundBody, lowerLeft, lowerRight, radius)
      cpShapeSetElasticity(line, 1.0)
      cpShapeSetFriction(line, 1.0)
      cpSpaceAddShape(space, line)
  }
    
  var pos = cpBodyGetPosition(body)
  ballX = pos.useContents{x}.toFloat()
  ballY = pos.useContents{y}.toFloat()
  
  while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && glfwWindowShouldClose(window) == 0) {
    glClear( (GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    
    //#Move the ball
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
    
    if (timeAccumulator >= UPDATE_INTERVAL) {
        timeAccumulator = timeAccumulator - UPDATE_INTERVAL
        cpSpaceStep(space, UPDATE_INTERVAL.toDouble())
    }
    
    if( timeAccumulator < UPDATE_INTERVAL ) {
        cpBodySetPosition(body, cpv(WINDOW_WIDTH/2.0, WINDOW_HEIGHT.toDouble() -aheight.toDouble()))
        timeAccumulator = 3.0
    }
    
    pos = cpBodyGetPosition(body)
    ballX = pos.useContents{x}.toFloat()
    ballY = pos.useContents{y}.toFloat()
    
  }
  
  glfwTerminate()
}