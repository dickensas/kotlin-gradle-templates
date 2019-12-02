package plot

import platform.avr.*
import kotlinx.cinterop.internal.*
import kotlinx.cinterop.*

fun swap(a:uint8_tVar, b:uint8_tVar) = memScoped {
    var temp = a
    a.value = b.value
    b.value = temp.value
}