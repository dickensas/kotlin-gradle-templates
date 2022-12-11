# kotlin-gradle-templates / embed-r
Quick start kotlin multiplatform call R in Kotlin

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

You will get output similar to below

Incoming value from Kotlin:  5 8 1 0 3
Return value from R: 7.0 10.0 3.0 2.0 5.0


**Important Note:**
  * Verify R.dll and RBlas.dll available in the bin folder, please install [Microsoft Open R](https://mran.microsoft.com/open)
  * Please add the bin of Microsoft open R to PATH environment variable

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
