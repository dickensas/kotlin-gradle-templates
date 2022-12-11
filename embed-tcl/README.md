# kotlin-gradle-templates / embed-r
Quick start kotlin multiplatform call TCl in Kotlin

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

You will get output similar to below from TCL

Hello, world!

You can change the TCL and also you can write TCL direclty in double quotes to trigger via eval code

**Important Note:**
  * you need install tcl via MSYS2 using the command
    * pacman -S mingw-w64-x86_64-tcl mingw-w64-x86_64-tk

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
