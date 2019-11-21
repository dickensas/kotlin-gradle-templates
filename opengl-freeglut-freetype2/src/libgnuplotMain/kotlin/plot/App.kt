package plot

import glew.*
import freetype.*
import freeglut.*
import kotlin.math.*
import kotlinx.cinterop.*
import kotlinx.cinterop.internal.*

var textvertexSource = 
"""
#version 120
#define lowp
#define mediump
#define highp

attribute vec4 coord;
varying vec2 texpos;

void main(void) {
  gl_Position = vec4(coord.xy, 0, 1);
  texpos = coord.zw;
}
"""

var textfragmentSource =
"""
#version 120

varying vec2 texpos;
uniform sampler2D tex;
uniform vec4 color;

void main(void) {
  gl_FragColor = vec4(1, 1, 1, texture2D(tex, texpos).a) * color;
}
"""

fun get_attrib( program:UInt,  name:String):Int = memScoped {
    var attribute = glGetAttribLocation!!(program, name.cstr.getPointer(memScope))
    if(attribute == -1)
        throw Error("Could not bind attribute ${name}")
    return attribute;
}

fun get_uniform( program:UInt,  name:String):Int = memScoped {
    var uniform = glGetUniformLocation!!(program, name.cstr.getPointer(memScope))
    if(uniform == -1)
        throw Error("Could not bind uniform ${name}");
    return uniform;
}

var pId:UInt = 0u
var attribute_coord = 0
var uniform_tex = 0
var uniform_color = 0
var vbo:UInt = 0u
var TEXT = "Hello World!"
var FONT_WITH_MANUAL_SIZE = "Sans 32"
var face:FT_Face? = null

data class point(var x:GLfloat,var y:GLfloat, var s:GLfloat,var t:GLfloat)

fun main(args: Array<String>) = memScoped {

    glutInit(cValuesOf(0),null)
    glutInitContextVersion(2,0);
    glutInitDisplayMode(GLUT_RGB);
    glutInitWindowSize(640, 480);
    glutCreateWindow("Basic Text");
    
    glewExperimental = 1u

    if (glewInit() != 0u) {
        throw Error("Failed to initialize GLEW")
    }
    
    var ft = alloc<FT_LibraryVar>()

    if(FT_Init_FreeType(ft.ptr) == 1 ) {
      throw Error("Could not init freetype library")
    }
    
    face = memScoped {
        var output = alloc<FT_FaceVar>()
        if(FT_New_Face(ft.value, "FreeSans.ttf", 0, output.ptr) == 1) {
          throw Error("Could not open font")
        }
        output.value
    }
    
    FT_Set_Pixel_Sizes(face, 0, 48)
    
    if(FT_Load_Char(face, 'X'.toInt().toUInt(), FT_LOAD_RENDER)==1) {
      throw Error("Could not load character X")
    }
    
    pId = memScoped {
        val vsId:UInt = glCreateShader!!(GL_VERTEX_SHADER.toUInt())
        val fsId:UInt = glCreateShader!!(GL_FRAGMENT_SHADER.toUInt())

        glShaderSource!!(vsId, 1, arrayOf(textvertexSource).toCStringArray(memScope), null)
        glCompileShader!!(vsId)

        glShaderSource!!(fsId, 1, arrayOf(textfragmentSource).toCStringArray(memScope), null)
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
    
    attribute_coord = get_attrib(pId, "coord")
    uniform_tex = get_uniform(pId, "tex")
    uniform_color = get_uniform(pId, "color")
    
    vbo = memScoped {
        var output1 = alloc<UIntVar>()
        glGenBuffers!!(1, output1.ptr)
        output1.value
    }
    
    
    glutDisplayFunc(staticCFunction<Unit> { 
           
        glUseProgram!!(pId)
           
        glClearColor(1f, 1f, 1f, 1f)
        glClear(GL_COLOR_BUFFER_BIT.toUInt())
        
        glEnable(GL_BLEND.toUInt())
        glBlendFunc(GL_SRC_ALPHA.toUInt(), GL_ONE_MINUS_SRC_ALPHA.toUInt())
        
        memScoped {
            var black = allocArrayOf( 0f, 0f, 0f, 1f )
            
            FT_Set_Pixel_Sizes(face, 0, 48)
            glUniform4fv!!(uniform_color, 1, black)
            
            var g = face?.pointed?.glyph!!
            
            //draw the text
            var tex:UIntVar= alloc<UIntVar>()
            glActiveTexture!!(GL_TEXTURE0.toUInt())
            glGenTextures(1, tex.ptr)
            glBindTexture(GL_TEXTURE_2D.toUInt(), tex.value)
            glUniform1i!!(uniform_tex, 0)
            
            glPixelStorei(GL_UNPACK_ALIGNMENT.toUInt(), 1)
            
            glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_WRAP_S.toUInt(), GL_CLAMP_TO_EDGE.toInt())
            glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_WRAP_T.toUInt(), GL_CLAMP_TO_EDGE.toInt())
            
            glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_MIN_FILTER.toUInt(), GL_LINEAR.toInt())
            glTexParameteri(GL_TEXTURE_2D.toUInt(), GL_TEXTURE_MAG_FILTER.toUInt(), GL_LINEAR.toInt())
            
            glEnableVertexAttribArray!!(attribute_coord.toUInt())
            glBindBuffer!!(GL_ARRAY_BUFFER.toUInt(), vbo)
            glVertexAttribPointer!!(attribute_coord.toUInt(), 4, GL_FLOAT.toUInt(), false.toByte().toUByte(), 0, null)
            
            var sx = (2.0 / glutGet(GLUT_WINDOW_WIDTH)).toFloat()
            var sy = (2.0 / glutGet(GLUT_WINDOW_HEIGHT)).toFloat()
            var x  = (-1 + 8 * sx).toFloat()
            var y  = (1 - 50 * sy).toFloat()
                
            for (i in 0 until TEXT.length) {
                if (FT_Load_Char(face, TEXT[i].toInt().toUInt(), FT_LOAD_RENDER)==1){
                    println("Load char ${TEXT[i]} failed")
                }
                
                glTexImage2D(
                    GL_TEXTURE_2D.toUInt(), 
                    0, 
                    GL_ALPHA, 
                    g.pointed.bitmap.width.toInt(), 
                    g.pointed.bitmap.rows.toInt(), 
                    0, 
                    GL_ALPHA.toUInt(), 
                    GL_UNSIGNED_BYTE.toUInt(), 
                    g.pointed.bitmap.buffer
                )
                
                
                var x2 = (x + g.pointed.bitmap_left * sx).toFloat()
                var y2 = (-y - g.pointed.bitmap_top * sy).toFloat()
                var w  = (g.pointed.bitmap.width.toFloat() * sx).toFloat()
                var h  = (g.pointed.bitmap.rows.toFloat() * sy).toFloat()
                
                val vertex_array = floatArrayOf(
                        x2, -y2,     0.0f, 0.0f,
                    x2 + w, -y2,     1.0f, 0.0f,
                        x2, -y2 - h, 0.0f, 1.0f,
                    x2 + w, -y2 - h, 1.0f, 1.0f
                )
                
                vertex_array.usePinned {
                    glBufferData!!(GL_ARRAY_BUFFER.toUInt(), (vertex_array.size * FloatVar.size).signExtend(), it.addressOf(0), GL_DYNAMIC_DRAW.toUInt())
                }
                glDrawArrays(GL_TRIANGLE_STRIP.toUInt(), 0, 4)
                x = x+ (g.pointed.advance.x shr 6) * sx;
                y = y+ (g.pointed.advance.y shr 6) * sy;
            }
            glDisableVertexAttribArray!!(attribute_coord.toUInt())
            glDeleteTextures(1, tex.ptr)
        }
        
        glutSwapBuffers()
       
    });
    
    glutMainLoop()
    
}