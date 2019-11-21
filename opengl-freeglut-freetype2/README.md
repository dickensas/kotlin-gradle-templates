# kotlin-gradle-templates / opengl-freeglut-freetype2
Quick start kotlin multiplatform OpenGL GLEW+FreeGlut+FreeType2 Basic Hello World! text drawing using GLSL

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
  * You will be needing FreeSans.ttf, 
this font can be downloaded from the official download site [GNU Free Font](http://ftp.gnu.org/gnu/freefont/freefont-ttf.zip)
and put it inside the exe folder 
  * You need to execute this exe from the MSYS2 or cygwin terminal, otherwise many dll files need to be copied to this exe folder


## References

 **GitLab** [Wiki Books OpenGL](https://gitlab.com/wikibooks-opengl/modern-tutorials/tree/master/text01_intro)
