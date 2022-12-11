# kotlin-gradle-templates / arduino-atmega8
Quick start kotlin Arduino AVR Atmega8 [Experimental Static Lib]

This code programmed for Windows using mingw

## Usage
The code is ready to execute 

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to build the library

     .\gradlew assemble

make sure you are within mingw64 terminal

Install AVR related tools and libraries as below
       
     pacman -S mingw-w64-x86_64-avr-gcc
     pacman -S mingw-w64-x86_64-avr-libc

If your msys is installed at c:\msys64 then libatmega8.a libraries will be found at the below folder

     C:\msys64\mingw64\avr\lib\avr4

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
