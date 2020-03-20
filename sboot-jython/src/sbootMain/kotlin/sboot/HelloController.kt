package sboot

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

import org.python.util.PythonInterpreter
import org.python.core.*


@RestController
class HelloController {
    val interp:PythonInterpreter = PythonInterpreter()
    
    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name:String):Greeting {
        interp.exec("import sys")
        interp.exec("print sys")
        
        interp.set("a", PyString(name));
        interp.exec("a = \"Hello! \" + a + \", from jython\"")
        var a:PyObject = interp.get("a")
        return Greeting(a.toString())
    }
}