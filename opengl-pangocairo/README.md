# kotlin-gradle-templates / opengl-pangocairo
Quick start kotlin multiplatform OpenGL libpangocairo which offers Text Rendering

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
  * You need to execute this code executable from the MSYS2 or cygwin terminal, otherwise many dll files need to be copied to this exe folder


## References

 **GitHub** [GNOME pango](https://github.com/GNOME/pango/blob/mainline/examples/cairosimple.c)
 **Khronos** [Common Mistakes](https://www.khronos.org/opengl/wiki/Common_Mistakes#Creating_a_complete_texture)

