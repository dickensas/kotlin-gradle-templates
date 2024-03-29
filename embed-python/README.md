# kotlin-gradle-templates / embed-python
Quick start kotlin multiplatform Embed Python in Kotlin

## Usage
The code is ready to execute

If you have gradle in path, then invoke gradle as

     gradle assemble -Dpython_version=3.10

If you have wrapper for linux

     ./gradlew assemble -Dpython_version=3.10

If you have wrapper for windows

     .\gradlew assemble -Dpython_version=3.10

Then execute bellow task to start the OpenGL application

     .\gradlew runReleaseExecutableLibgnuplot -Dpython_version=3.10
     
You can change the version 3.10 to 3.9 or 3.8 or 3.7

You will get the output like this

Today is Sat Nov 23 11:47:45 2019
Will compute 3 times 3
Result of call: 9


**Important Note:**
  * You need to execute code executable from the MSYS2 or cygwin terminal, otherwise many dll files need to be copied to this exe folder
  * Verify libpython3.8.dll.a and Python.h in MSYS, install python accordingly
  * You need to specify the PYTHONPATH and PYTHONHOME with current directory included as "./"
  
**Setting PYTHONPATH**

      export PYTHONPATH=/mingw64/lib/python38.zip:/mingw64/lib/python3.8:/mingw64/lib/python3.8/lib-dynload:/mingw64/lib/python3.8/dist-packages:./

## References

 **Web** [Python Official Document](https://docs.python.org/3/extending/embedding.html#pure-embedding)

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
