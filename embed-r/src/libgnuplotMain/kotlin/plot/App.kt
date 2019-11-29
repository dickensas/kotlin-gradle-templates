package plot

import kotlinx.cinterop.*
import R.*

fun main() 
{
    memScoped { 
        Rf_initEmbeddedR(2, arrayOf("R","--silent").toCStringArray(memScope))
    }
    
    //load R file myfunc.R
    var e = Rf_lang2(  Rf_install("source"),  Rf_mkString("myfunc.R") )
    Rf_protect(e)
    R_tryEval(e, R_GlobalEnv, null)
    Rf_unprotect(1)
    
    //prepare parameters
    var array = intArrayOf( 5, 8, 1, 0, 3 )
    var arg = Rf_allocVector(INTSXP, array.size.toLong())
    Rf_protect(arg)
    array.usePinned { buf ->
        platform.posix.memcpy(INTEGER(arg), buf.addressOf(0), (array.size * sizeOf<IntVar>()).toULong() )
    }
    
    //prepare function "myfunc"
    var myfunc = Rf_lang2(  Rf_install("myfunc"), arg)
    
    // Execute the function
    memScoped {
        var error = alloc<IntVar>()
        var ret = R_tryEval(myfunc, R_GlobalEnv, error.ptr)
        
        if(error.value != 1) {
            print("Return values from R: ")
            var arr1 = REAL(ret)
            for (i in 0 until array.size)
                print("${arr1!![i]} ")
        } else {
            println("Unable to call R function")
        }
    }
    
    Rf_unprotect(2)
    
    Rf_endEmbeddedR(0)
    
}