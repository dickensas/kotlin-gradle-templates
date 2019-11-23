# kotlin-gradle-templates / embed-lua
Quick start kotlin multiplatform call Lua in Kotlin

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

You will get output as

$ ./build/bin/libgnuplot/releaseExecutable/klua.exe
Lua result value: 36.0
f1 Lua function called from kotlin.
square Lua called from Kotlin, with parameter=6.0


**Important Note:**
  * You need to execute code executable from the MSYS2 or Cygwin terminal, otherwise many dll files need to be copied to this exe folder
  * Verify liblua.a and lua.h in MSYS or Cygwin, install lua accordingly mingw-w64-x86_64-lua

## References

 **Web** [Lua Official Document](https://www.lua.org/pil/24.html)
