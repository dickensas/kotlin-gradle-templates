package wasm

import kotlinx.interop.wasm.dom.*
import kotlinx.wasm.jsinterop.*
import webgl.*

fun main() {
    val glCanvas = document.getElementById("myCanvas").asGLCanvas
    val glCtx = glCanvas.getContext("webgl")
    with(glCtx) {
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)
        
        glEnable(GL_SCISSOR_TEST)
        
        glScissor(20,20,50,50)
        glClearColor(0.0f, 1.0f, 0.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)
        
        glScissor(70,70,50,50)
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)
        
        glScissor(120,120,50,50)
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        glClear(GL_COLOR_BUFFER_BIT)
        
        glDisable(GL_SCISSOR_TEST)
	}
}
