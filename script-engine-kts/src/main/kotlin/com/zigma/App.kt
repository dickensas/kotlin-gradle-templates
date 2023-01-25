package com.zigma

import javax.script.ScriptEngineManager

class App {
}

fun main() {
	val engine = ScriptEngineManager().getEngineByExtension("main.kts")!!
    val res1 = engine.eval("""
		val x = 3
        x
    """)
	println(res1)
}
