package plot

import mesa.*
import kotlinx.cinterop.*

val WIDTH = 50
val HEIGHT = 50
var buffer:CValuesRef<UShortVar>? = null
var context:OSMesaContext? = null

fun main() = memScoped {

    var channels = 4
    
    var lbuffer = allocArray<UShortVar>((channels * WIDTH * HEIGHT).toInt() * (sizeOf<UShortVar>()).toInt())
    
    context = OSMesaCreateContext(mesa.GL_RGBA, null)
    if(context==null)
        throw Error("Mesa NULL")
        
    var success = OSMesaMakeCurrent(context, lbuffer, mesa.GL_UNSIGNED_SHORT, WIDTH, HEIGHT)
    if(success.toInt()!=1)
        throw Error("Mesa Make Current Failed")
    
    mesa.glBegin(mesa.GL_LINES.toUInt())
        mesa.glVertex2d(0.5, 0.5)
        mesa.glVertex2d(-0.5, -0.5)
    mesa.glEnd()
      
    mesa.glFinish()
    OSMesaDestroyContext(context)
    
    var width = WIDTH
    var height = HEIGHT
    
    var y = height - 1
    while (y>=0) {
        var x = 0
        while (x<width) {
            var i = ((y*width + x) * 4).toInt()
            var byte = lbuffer[i+2].toInt() shr 8;
            if( byte == 0 ) print(" ")
            else print("a")
            x = x + 1
        }
        println()
        y = y - 1
    }
}