package plot

import kotlin.math.*
import kotlinx.cinterop.*
import platform.windows.*
import gtk3.*
import glew.*

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

var pId:UInt = 0u
var vao:UInt = 0u
var vbo:UInt = 0u
lateinit var g_wglarea:CPointer<GtkGLArea>

fun init_program() {
    
    vao = memScoped {
        val output = alloc<UIntVar>()
        glGenVertexArrays!!(1, output.ptr)
        output.value
    }
    glBindVertexArray!!(vao)
    
    vbo = memScoped {
        val output = alloc<UIntVar>()
        glGenBuffers!!(1, output.ptr)
        output.value!!
    }
    glBindBuffer!!(GL_ARRAY_BUFFER.toUInt(), vbo)
    
    val vertex_array = floatArrayOf(
        -0.8f, -0.8f, 0.0f,
         0.8f, -0.8f, 0.0f,
         0.0f,  0.8f, 0.0f
    )
    
    vertex_array.usePinned {
        glBufferData!!(GL_ARRAY_BUFFER.toUInt(), vertex_array.size.toLong() * 4, it.addressOf(0), GL_STATIC_DRAW.toUInt())
    }

    pId = memScoped {
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
}

fun draw_triangle() {
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
}

fun realize_callback(widget:CPointer<GtkGLArea>?) {
    var glcontext = gtk_gl_area_get_context(widget)
    var glwindow = gdk_gl_context_get_window(glcontext)
    gdk_gl_context_make_current(glcontext)
    
    glewExperimental = 1u
    if (glewInit() != 0u) {
        throw Error("Failed to initialize GLEW")
    }
    
    g_wglarea = widget!!
    
    init_program()
    
    var frame_clock = gdk_window_get_frame_clock(glwindow)
    g_signal_connect_data(
        frame_clock!!.reinterpret(), 
        "update", 
        staticCFunction {  
            -> gtk_gl_area_queue_render(g_wglarea)
        }.reinterpret(), 
        g_wglarea, 
        null, 
        G_CONNECT_SWAPPED
    )
    
    gdk_frame_clock_begin_updating(frame_clock)
    
}

fun render_callback(widget:CPointer<GtkGLArea>?) {
    draw_triangle()
}

fun main() = memScoped {
    var argc = alloc<IntVar>().apply { this.value = 0 }
    gtk_init(argc.ptr, null)
    
    var window = gtk_window_new(GtkWindowType.GTK_WINDOW_TOPLEVEL)!!.reinterpret<GtkWidget>()
    gtk_window_set_position(window.reinterpret<GtkWindow>(), GtkWindowPosition.GTK_WIN_POS_CENTER)
    gtk_window_set_default_size(window.reinterpret<GtkWindow>(), 400, 400)
    gtk_window_set_title(window.reinterpret<GtkWindow>(), "Demo Window")
    
    var box = gtk_box_new (GtkOrientation.GTK_ORIENTATION_VERTICAL, 0)
    g_object_set (box, "margin", 12, null)
    gtk_box_set_spacing (box!!.reinterpret<GtkBox>(), 6)
    gtk_container_add (window.reinterpret(), box)
    
    var wglarea = gtk_gl_area_new()!!.reinterpret<GtkGLArea>()
    gtk_widget_set_hexpand (wglarea.reinterpret<GtkWidget>(), 1)
    gtk_widget_set_vexpand (wglarea.reinterpret<GtkWidget>(), 1)
    
    g_signal_connect_data(
        window.reinterpret(), 
        "destroy", 
        staticCFunction {  
            -> gtk_main_quit()
        }.reinterpret(), 
        null, 
        null, 
        0u
    )
    
    gtk_widget_add_events(
        wglarea.reinterpret(),0
    )
    
    g_signal_connect_data(
        wglarea.reinterpret(), 
        "realize", 
        staticCFunction {  glarea:CPointer<GtkGLArea>?
            -> realize_callback(glarea)
        }.reinterpret(), 
        null, 
        null, 
        0u
    )
    
    gtk_widget_add_events(
        wglarea.reinterpret(),0
    )
    
    g_signal_connect_data(
        wglarea.reinterpret(), 
        "render", 
        staticCFunction {  glarea:CPointer<GtkGLArea>?
            -> render_callback(glarea)
        }.reinterpret(), 
        null, 
        null, 
        0u
    )
    
    gtk_container_add(box.reinterpret(), wglarea.reinterpret<GtkWidget>())
    
    gtk_widget_show(box.reinterpret<GtkWidget>())
    gtk_widget_show(wglarea.reinterpret<GtkWidget>())
    gtk_widget_show(window)
    gtk_main()
}


