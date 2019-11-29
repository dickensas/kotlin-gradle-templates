package plot

import kotlinx.cinterop.*
import tcl.*

fun main() 
{
    var tcli = Tcl_CreateInterp()
    if(Tcl_Eval(tcli, "set a 1")==TCL_OK) {
        Tcl_EvalFile(tcli, "hello.tcl")
    }
    Tcl_DeleteInterp(tcli)
}