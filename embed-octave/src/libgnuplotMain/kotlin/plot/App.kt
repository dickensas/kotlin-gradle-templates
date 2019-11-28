package plot

import octave.*
import kotlin.math.*
import kotlinx.cinterop.*
import platform.posix.*

fun main() {

    println("Example app to call octave from kotlin")
    
    octave_main(0,null,1)
    mexPrintf("Hello, World!\n");
    
}