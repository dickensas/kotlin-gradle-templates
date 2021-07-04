package plot
import platform.posix.sleep
import kotlinx.cinterop.*
import kotlin.math.*
import sndfile.*

fun main() {
   memScoped {
      
      info.channels = 1;
      info.samplerate = 22050;
      info.format = (SF_FORMAT_FLAC or SF_FORMAT_PCM_16).toInt();
      
      if (sf_format_check(info.ptr) == 0) {
        println("Invalid encoding\n");
        return;
      }
      
      var freq = 2000.0f;
      var seconds = 1;
      var sample_rate = 22050;
      var buf_size = seconds * sample_rate;
      var samples = ShortArray(buf_size)
      for (i in 0..buf_size-1) {
         samples[i] = (32760 * sin( (2.0f*PI*freq)/sample_rate * i )).toInt().toShort()
      }
      
      var outfilename = "abcd.flac"
      
      output = sf_open(outfilename, SFM_WRITE.toInt(), info.ptr);

      sf_write_short(output, samples.toCValues(), buf_size.toLong());

      sf_close(output);
      
   }
}
