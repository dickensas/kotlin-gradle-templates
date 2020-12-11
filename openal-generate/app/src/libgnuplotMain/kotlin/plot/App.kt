package plot
import platform.posix.sleep
import kotlinx.cinterop.*
import kotlin.math.*
import openal.*

fun main() {
   memScoped {
      
      var defname = alcGetString(null, ALC_DEFAULT_DEVICE_SPECIFIER)
      var dev = alcOpenDevice(defname?.toKString())
      var ctx = alcCreateContext(dev, null)
      alcMakeContextCurrent(ctx)
      
      var buf = alloc<ALuintVar>()
      alGenBuffers(1, buf.ptr)
      
      var freq = 2000.0f;
      var seconds = 1;
      var sample_rate = 22050;
      var buf_size = seconds * sample_rate;
      var samples = ShortArray(buf_size)
      for (i in 0..buf_size-1) {
         samples[i] = (32760 * sin( (2.0f*PI*freq)/sample_rate * i )).toInt().toShort()
      }
      
      samples.usePinned {
         alBufferData(buf.value, AL_FORMAT_MONO16, it.addressOf(0), (buf_size * sizeOf<ShortVar>()).toInt(), sample_rate.toInt())
      }
      
      var src = alloc<ALuintVar>()
      alGenSources(1.toInt(), src.ptr)
      alSourcei(src.value, AL_BUFFER, buf.value.toInt())
      alSourcePlay(src.value)
      
      sleep(seconds.toUInt())
      
      alSourcei(src.value, AL_BUFFER, 0)
      alDeleteSources(1, src.ptr)
      alDeleteBuffers(1, buf.ptr)
      
      alcMakeContextCurrent(null)
      alcDestroyContext(ctx)
      alcCloseDevice(dev)
      
   }
}
