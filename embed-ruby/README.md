# kotlin-gradle-templates / embed-ruby
Quick start kotlin multiplatform Embed Ruby in Kotlin

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


**Important Note:**
  * Verify libx64-msvcrt-ruby260.dll.a and header files in in MSYS2 C:\msys64\mingw64\include\ruby-2.6.0\x64-mingw32, install ruby accordingly by installing mingw-w64-x86_64-ruby

## References

 **Web** [Ruby Official Document](https://docs.ruby-lang.org/en/2.4.0/extension_rdoc.html#label-Invoke+Ruby+Method+from+C)
