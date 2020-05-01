package mymod

import kotlinx.wasm.jsinterop.*

@SymbolName("getMyModule")
external public fun getMyModule(resultArena: Int): Int

@SymbolName("domEval")
external public fun domEval(arena: Int, index: Int, idPtr: Int, idLen: Int, rdPtr: Int, rdLen: Int, resultArena: Int): Unit

open class MyModule(arena: Int, index: Int): JsValue(arena, index) {
	constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)
    fun domEval(js: String, rdPtr: Int, rdLen: Int): Unit {
      domEval(this.arena, this.index, stringPointer(js), stringLengthBytes(js), rdPtr, rdLen, ArenaManager.currentArena)
    }
	companion object {
    }
}

val mod: MyModule
    get() {
      val wasmRetVal = getMyModule(ArenaManager.currentArena)
      return MyModule(ArenaManager.currentArena, wasmRetVal)
    }