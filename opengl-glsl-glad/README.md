# kotlin-gradle-templates / opengl-glsl-glad
Quick start kotlin multiplatform OpenGL GLEW+GLAD Basic Rectangle using GLSL

## Usage
The code is ready to execute

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start the OpenGL application

     .\gradlew copyDlls
     .\gradlew runReleaseExecutableLibgnuplot

GLFW Based window with GLAD based Rectangle will be displayed

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
