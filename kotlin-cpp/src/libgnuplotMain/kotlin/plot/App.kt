package plot

import kotlinx.cinterop.*
import cpplib.*

fun main(args: Array<String>) = memScoped {
	println(greeting()!!.toKString())
	open("textfile.txt")
	var line = getline()
	println(line!!.toKString())
	while( line!!.toKString()!="\\0" ) {
		line = getline()
		println(line!!.toKString())
	}
	close()
}