# kotlin-gradle-templates / opengl-cairo-rsvg
Quick start kotlin multiplatform OpenGL libcairo and librsvg which offers SVG Rendering

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

GLFW Based window with GLEW, libcairo and librsvg based SVG Rendering

**Important Note:**
  * Verify libcairo.a, librsvg-2.a available in the lib folder, install libraries accordingly by installing mingw-w64-x86_64-librsvg, mingw-w64-x86_64-cairo, mingw-w64-x86_64-glew, mingw-w64-x86_64-glfw


## References

**GitHub** [GNOME librsvg](https://github.com/GNOME/librsvg/blob/2.46.3/tests/api.c)

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
