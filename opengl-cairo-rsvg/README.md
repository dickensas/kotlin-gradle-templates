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

GLFW Based window with GLEW and libpangocairo based Text rendering

**Important Note:**
  * You need to execute this exe from the MSYS2 or cygwin terminal, otherwise many dll files need to be copied to this exe folder


## References

**GitHub** [GNOME librsvg](https://github.com/GNOME/librsvg/blob/2.46.3/tests/api.c)

