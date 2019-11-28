# kotlin-gradle-templates / embed-octave
Quick start kotlin multiplatform Embed Octave Mex in Kotlin

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
     
You will get the output like this

Example app to call octave from kotlin
Hello, World!

This hello world is actually coming from Mex

mexPrintf("Hello, World!\n");


**Important Note:**
  * You need to execute code executable from the Octave MSYS2 terminal
  * Verify liboctinterp-7.dll Ocatve it should be PATH, otherwise it wont work 

## References

 **Web** [Octave Mex Official Document](https://octave.org/doc/v4.0.1/Getting-Started-with-Mex_002dFiles.html#Getting-Started-with-Mex_002dFiles)
