import kotlin.browser.*
import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.coroutines.*

fun main() {
    var detectDiv = GlobalScope.launch {
        delay(2000L) //wait for 2 second browser ready hack
        val myDiv = document.create.div {
            p { +"some text " }
            div { +"my div tag" }
        }

        var root = document.getElementById("root")
        root?.append(myDiv)
    }

}