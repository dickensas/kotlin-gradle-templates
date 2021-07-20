class KotlinCallback : Callback {
	constructor(): super() {
		
	}
	override fun run()
    {
    	println("From Kotlin KotlinCallback::run()");
    }
}

class App {
	fun demo() {
		var callback = KotlinCallback()
		var caller = Caller()
		caller.setCallback(callback)
    	caller.call()
    	caller.delCallback()
	}
        
	companion object {
        init {
            System.loadLibrary("cpplib")
        }
    }
}

fun main(args: Array<String>) {
    App().demo()
    
}