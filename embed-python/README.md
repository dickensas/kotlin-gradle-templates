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
     
You will get the output like this

Today is Sat Nov 23 11:47:45 2019
Will compute 3 times 3
Result of call: 9


**Important Note:**
  * You need to execute code executable from the MSYS2 or cygwin terminal, otherwise many dll files need to be copied to this exe folder
  * Verify libpython3.7.dll.a and Python.h in MSYS, install python accordingly
  * You need to specify the PYTHONPATH and PYTHONHOME with current directory included as "./" 

## References

 **Web** [Python Official Document](https://docs.python.org/3/extending/embedding.html#pure-embedding)
