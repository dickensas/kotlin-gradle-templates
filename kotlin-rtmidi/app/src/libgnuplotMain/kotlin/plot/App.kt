package plot

import kotlin.native.concurrent.*
import platform.posix.sleep
import kotlinx.cinterop.*
import kotlin.text.*
import rtmidi.*


lateinit var midiPtr: RtMidiInPtr

fun midi_callback(
                 timeStamp: Double, 
                 message: CArrayPointer<UByteVar>,
                 messageSize: size_t, 
                 userData: COpaquePointerVar
) = memScoped {
    initRuntimeIfNeeded()
    
    var key = message[1].toString()
    println(key)
}

fun main() {
    midiPtr = rtmidi_in_create_default()!!
    
    rtmidi_in_set_callback(midiPtr, staticCFunction {  
            timeStamp: Double, 
            message: CArrayPointer<UByteVar>,
            messageSize: size_t, 
            userData: COpaquePointerVar
            -> midi_callback( timeStamp, message, messageSize, userData  )
        }.reinterpret(),
        null
    )

    var c = rtmidi_get_port_count(midiPtr)
    
    for(i in 0..c.toInt()-1){
        var portName = rtmidi_get_port_name(midiPtr, i.toUInt())!!.toKString()
        rtmidi_open_port(
           midiPtr!!, 
           i.toUInt(), 
           portName
        )
    }
    println("waiting for 5 seconds")
    sleep(5)
    println("closing all the midi ports")
    rtmidi_in_cancel_callback(midiPtr)
    rtmidi_close_port(midiPtr)
    rtmidi_in_free(midiPtr)
}