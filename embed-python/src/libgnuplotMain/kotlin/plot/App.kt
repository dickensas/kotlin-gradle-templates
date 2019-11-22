package plot

import python.*
import kotlinx.cinterop.*

fun main(args: Array<String>) = memScoped {

    Py_SetProgramName(null)
    Py_Initialize()
    
    //Direct python script embedding
    
    PyRun_SimpleStringFlags(

"""
from time import time,ctime
print('Today is',ctime(time()))
"""

,null)

    
    //External python code located in PYTHONPATH

    //#load module multiply or multiply.py from PYTHONPATH
    var pName = PyUnicode_DecodeFSDefault("multiply")
    var pModule = PyImport_Import(pName)
    
    //#load python function named "multiply" from the code
    var pFunc = PyObject_GetAttrString(pModule, "multiply")
    var pArgs = PyTuple_New(2L)
    
    //#set 2 parameters for the python method
    PyTuple_SetItem(pArgs, 0L, PyLong_FromLong(3))
    PyTuple_SetItem(pArgs, 1L, PyLong_FromLong(3))
    
    //#call the python method and get the result value
    var pValue = PyObject_CallObject(pFunc, pArgs)
    println("Result of call: " + PyLong_AsLong(pValue));
    
    Py_Finalize()
}