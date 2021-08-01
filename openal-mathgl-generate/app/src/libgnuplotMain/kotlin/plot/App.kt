package plot
import kotlinx.cinterop.internal.*
import platform.posix.sleep
import kotlinx.cinterop.*
import kotlin.math.*
import openal.*
import mgl.*

fun main() {
   memScoped {

	  var defname = alcGetString(null, ALC_DEFAULT_DEVICE_SPECIFIER)
	  var dev = alcOpenDevice(defname?.toKString())
	  var ctx = alcCreateContext(dev, null)
	  alcMakeContextCurrent(ctx)
	  
	  var buf = alloc<ALuintVar>()
	  alGenBuffers(1, buf.ptr)
	  
	  var d = 1.0f;
	  var sr = 44100;
	  var y = mgl_create_data_size(sr.toInt(),1,0)
	  mgl_data_modify(y,"10000.0*(sin((2.0*pi*(440.0*pow(2.0,-7.0/66.0)))*x)*cos((pi*x)/(2.0*1.0)))",0);
	  var samples = ShortArray(sr.toInt())
	  for(i in 0..sr-1) {
         samples[i] = mgl_data_get_value(y,i,0,0).toInt().toShort()
      }
	  
	  samples.usePinned {
		 alBufferData(buf.value, AL_FORMAT_MONO16, it.addressOf(0), (sr * sizeOf<ShortVar>()).toInt(), sr.toInt())
	  }
	  
	  var src = alloc<ALuintVar>()
	  alGenSources(1.toInt(), src.ptr)
	  alSourcei(src.value, AL_BUFFER, buf.value.toInt())
	  alSourcePlay(src.value)
	  
	  sleep(d.toUInt())
	  
	  alSourcei(src.value, AL_BUFFER, 0)
	  alDeleteSources(1, src.ptr)
	  alDeleteBuffers(1, buf.ptr)
	  
	  alcMakeContextCurrent(null)
	  alcDestroyContext(ctx)
	  alcCloseDevice(dev)
      
   }
}
