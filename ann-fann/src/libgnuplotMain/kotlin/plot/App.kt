package plot

import fann.*
import kotlin.math.*
import kotlinx.cinterop.*
import platform.posix.*

fun main() 
{

    println("Example app to load a arbitrary library from kotlin")
    
    var ann = fann_create_standard(4, 2, 8, 9, 1)
    
    fann_print_connections(ann)
    
    fann_destroy(ann)
}