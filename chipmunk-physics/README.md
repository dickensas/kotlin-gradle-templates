# kotlin-gradle-templates / chipmunk-physics
Quick start kotlin multiplatform OpenGL SVG based ball bounce animation using chipmunk physics with 3 rigid surface

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

The animation will start with ball touching the border and bouncing around
[Demo video](http://dickens.co.in/kotlin-svg-chipmunk)

**Important Note:**
  * You need to execute this code executable from the MSYS2 or cygwin terminal, otherwise many dll files need to be copied to this exe folder

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
