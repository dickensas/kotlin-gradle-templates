package wasm

import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*
import kotlinx.cinterop.*
import kotlin.text.*
import mymod.*

fun main() {
	mod.wasmReady()
	document.setter("onmouseup") { arguments: ArrayList<JsValue> ->
		val event = ButtonMouseEvent(arguments[0])
		if(event.target.id == "clickme") {
			println("Button clickme is pressed")
			var t1 = document.getElementById("t1").asDOM.value
			var t2 = document.getElementById("t2").asDOM.value
			var sb = StringBuilder(t1 + " " + t2)
			
			document.getElementById("mydiv").asDOM.innerHTML = "Reverse: " + sb.reverse()
		}
	}
	
}
