# kotlin-gradle-templates / embed-python
Quick start kotlin multiplatform Embed Python in Kotlin

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

FreeGlut+GLEW Based window with FreeType2 based text will be displayed in a windows

**Important Note:**
  * You need to execute code executable from the MSYS2 or cygwin terminal, otherwise many dll files need to be copied to this exe folder
  * Verify libpython3.7.dll.a and Python.h in MSYS, install python accordingly

## References

 **Web** [Python Official Document](https://docs.python.org/3/extending/embedding.html#pure-embedding)
