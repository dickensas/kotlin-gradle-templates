class App {
	val greeting: String
        get() {
            return Greeter().greeting()
        }
        
	companion object {
        init {
            System.loadLibrary("cpplib")
        }
    }
}

fun main(args: Array<String>) {
    println(App().greeting)
}