# kotlin-gradle-templates / gtk3-glade3
Quick start kotlin multiplatform GTK3 and GLADE3

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

export PATH=$PATH:/usr/x86_64-w64-mingw32/sys-root/mingw/bin

make sure the dependencies are installed using setup-x86_64.exe 

below are the dependencies

mingw64-x86_64-gtk3 3.22.28-1
glade               3.20.3-1
xorg-server         1.20.4-1
xinit               1.4.1-1

you can edit the file "window_main.glade" by yourself using the glade tool

