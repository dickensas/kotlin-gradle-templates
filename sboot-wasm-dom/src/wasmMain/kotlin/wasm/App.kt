package wasm

import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*
import kotlinx.cinterop.*
import mymod.*

fun main() {

	//simple method call to a kotlin custom code
    mod.sayHello(" hello")
    
    //library method from "kotlinx.interop.wasm.dom"
	var div = document.getElementById("mydiv")
	
	//stub the div as object from "mymod"
	div = div.asDOMDiv
	
	//advanced property method call from "mymod"
    div.innerHTML = "Kotlin WebAssembly Accessing HTML DOM"
    
    
}
