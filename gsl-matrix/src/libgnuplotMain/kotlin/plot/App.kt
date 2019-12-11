package plot

import platform.posix.*
import kotlinx.cinterop.*
import gsl.*

fun main() {

    var m1 = gsl_matrix_float_alloc(4,4)
    var m2 = gsl_matrix_float_alloc(4,4)
    
    gsl_matrix_float_set_zero(m1)
    gsl_matrix_float_set_zero(m2)
    
    gsl_matrix_float_set(m1, 0, 0, 1.0f)
    gsl_matrix_float_set(m2, 0, 0, 6.0f)
    
    gsl_matrix_float_add(m1,m2)
    
    gsl_matrix_float_add_diagonal(m2, 3.0)
    
    for(i in 0 until 4) {
        for(j in 0 until 4) {
            print("${gsl_matrix_float_get(m2,i.toULong(),j.toULong())} ")
        }
        println()
    }
    
    gsl_matrix_float_free(m1)
    gsl_matrix_float_free(m2)
    
    
}