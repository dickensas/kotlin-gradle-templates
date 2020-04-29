package mymod

import kotlinx.wasm.jsinterop.*

@SymbolName("sayHello")
external public fun sayHello(arena: Int, index: Int, idPtr: Int, idLen: Int, resultArena: Int): Unit

@SymbolName("getMyModule")
external public fun getMyModule(resultArena: Int): Int

@SymbolName("getInnerHTML")
external public fun getInnerHTML(arena: Int, index: Int, resultArena: Int): String

@SymbolName("setInnerHTML")
external public fun setInnerHTML(arena: Int, index: Int, idPtr: Int, idLen: Int, resultArena: Int): Unit

open class MyModule(arena: Int, index: Int): JsValue(arena, index) {
	constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)
	fun sayHello(id: String): Unit {
      sayHello(this.arena, this.index, stringPointer(id), stringLengthBytes(id), ArenaManager.currentArena)
    }
	companion object {
    }
}

val JsValue.asMyModule: MyModule
    get() {
      return MyModule(this.arena, this.index)
    }
  
val mod: MyModule
    get() {
      val wasmRetVal = getMyModule(ArenaManager.currentArena)
      return MyModule(ArenaManager.currentArena, wasmRetVal)
    }
    
open class DOMDiv(arena: Int, index: Int): JsValue(arena, index) {
	constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)
	var innerHTML: String
	    get() {
	      val wasmRetVal = getInnerHTML(this.arena, this.index, ArenaManager.currentArena)
	      return wasmRetVal
	    }
	
	    set(value: String) {
	      setInnerHTML(this.arena, this.index, stringPointer(value), stringLengthBytes(value), ArenaManager.currentArena)
	    }
	companion object {
    }
}

val JsValue.asDOMDiv: DOMDiv
  get() {
    return DOMDiv(this.arena, this.index)
  }