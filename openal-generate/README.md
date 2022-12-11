# kotlin-gradle-templates / openal-generate
Quick start kotlin multiplatform OpenAL

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
  * Verify libopenal.a, libopenal.dll.a available in the lib folder, install libraries accordingly by installing mingw-w64-x86_64-openal

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
