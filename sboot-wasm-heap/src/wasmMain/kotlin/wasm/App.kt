package wasm

import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*
import mymod.*

var str1 = "                                                                                        "

fun main() {
	mod.domEval("document.getElementById('mydiv').innerHTML", stringPointer(str1), stringLengthBytes(str1))
	println(str1)
}
