# kotlin-gradle-templates / openal-mathgl-generate
Generate a math buffer using MathGL and play using OpenAL

## Usage
The code is ready to execute

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start the OpenGL application

     .\gradlew runReleaseExecutableLibgnuplot

A beep sound will play

**Important Note:**
  * Verify libopenal.a, libopenal.dll.a available in the lib folder, install libraries accordingly by installing mingw-w64-x86_64-openal and mingw-w64-x86_64-mathgl

## References

 **manpages** [manpages](http://www.manpagez.com/info/mathgl/mathgl-1.8/mathgl_441.php)
