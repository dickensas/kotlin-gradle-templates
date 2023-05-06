# kotlin-gradle-templates / kotlin-libxml2
Quick start kotlin multiplatform Reading From XML and applying XSLT

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

You will get output and show the second xml child which is filtered by XSLT

     > Task :app:runDebugExecutableLibgnuplot
     <?xml version="1.0"?>
     <book>Book 2</book>


**Important Note:**
  * You need to install libxml2 library via MSYS2

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
