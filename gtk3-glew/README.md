# kotlin-gradle-templates / gtk3-glew
Quick start kotlin multiplatform GTK3, GLEW

This code programmed for Windows using Cygwin mingw

## Usage
The code is ready to execute 

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start the app

     .\gradlew runReleaseExecutableLibgnuplot

make sure the ming64 within cygwin is in path by issuing the below command

export PATH=/usr/x86_64-w64-mingw32/bin:/usr/x86_64-w64-mingw32/sys-root/mingw/bin:$PATH

make sure the dependencies are installed using setup-x86_64.exe 

below are the dependencies

mingw64-x86_64-gtk3 3.22.28-1
mingw64-x86_64-glew 2.1.0-1
glade               3.20.3-1
xorg-server         1.20.4-1
xinit               1.4.1-1


**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
