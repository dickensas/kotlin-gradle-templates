package mymod

import kotlinx.wasm.jsinterop.*

@SymbolName("sayHello")
external public fun sayHello(arena: Int, index: Int, idPtr: Int, idLen: Int, resultArena: Int): Unit

@SymbolName("wasmReady")
external public fun wasmReady(arena: Int, index: Int, resultArena: Int): Unit

@SymbolName("getMyModule")
external public fun getMyModule(resultArena: Int): Int

@SymbolName("getId")
external public fun getId(arena: Int, index: Int, resultArena: Int): Int

@SymbolName("getValue")
external fun getValue(arena: Int, index: Int, resultArena: Int): Int

@SymbolName("setValue")
external fun setValue(arena: Int, index: Int, idPtr: Int, idLen: Int, resultArena: Int): Int

@SymbolName("getInnerHTML")
external public fun getInnerHTML(arena: Int, index: Int, resultArena: Int): Int

@SymbolName("setInnerHTML")
external public fun setInnerHTML(arena: Int, index: Int, idPtr: Int, idLen: Int, resultArena: Int): Unit

@SymbolName("getTarget")
external public fun getTarget(arena: Int, index: Int, resultArena: Int): Int

@SymbolName("getChar")
external fun getChar(arena: Int, index: Int, charIndex: Int): Int

open class JsString(arena: Int, index: Int) : JsValue(arena, index) {
    constructor(jsValue: JsValue) : this(jsValue.arena, jsValue.index)

    fun getString(): String {
        val length = this.getInt("length")
        var result = ""
        for (i in 0 until length) {
            result += getChar(arena, index, i).toChar()
        }
        return result;
    }
}

val JsValue.asJsString: JsString
    get() {
      return JsString(this.arena, this.index)
    }

open class MyModule(arena: Int, index: Int): JsValue(arena, index) {
	constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)
	fun sayHello(id: String): Unit {
      sayHello(this.arena, this.index, stringPointer(id), stringLengthBytes(id), ArenaManager.currentArena)
    }
    fun wasmReady(): Unit {
      wasmReady(this.arena, this.index, ArenaManager.currentArena)
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
    
open class DOM(arena: Int, index: Int): JsValue(arena, index) {
	constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)
	var innerHTML: String
	    get() {
	      val wasmRetVal = getInnerHTML(this.arena, this.index, ArenaManager.currentArena)
	      return JsString(ArenaManager.currentArena, wasmRetVal).getString()
	    }
	
	    set(value: String) {
	      setInnerHTML(this.arena, this.index, stringPointer(value), stringLengthBytes(value), ArenaManager.currentArena)
	    }
	var id: String
	    get() {
	      val wasmRetVal = getId(this.arena, this.index, ArenaManager.currentArena)
	      return JsString(ArenaManager.currentArena, wasmRetVal).getString()
	    }
	    set(value: String) { }
	var value: String
		get() {
	      val wasmRetVal = getValue(this.arena, this.index, ArenaManager.currentArena)
	      return JsString(ArenaManager.currentArena, wasmRetVal).getString()
	    }
	    set(value: String) { 
	      setValue(this.arena, this.index, stringPointer(value), stringLengthBytes(value), ArenaManager.currentArena)
	    }
	companion object {
    }
}

val JsValue.asDOM: DOM
  get() {
    return DOM(this.arena, this.index)
  }

open class ButtonMouseEvent(arena: Int, index: Int): JsValue(arena, index) {
  constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)
  val target: DOM
    get() {
      val wasmRetVal = getTarget(this.arena, this.index, ArenaManager.currentArena)
      return DOM(ArenaManager.currentArena, wasmRetVal)
    }
}

val JsValue.asButtonMouseEvent: ButtonMouseEvent
  get() {
    return ButtonMouseEvent(this.arena, this.index)
  }