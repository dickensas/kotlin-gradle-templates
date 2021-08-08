# kotlin-gradle-templates / kotlin-rtmidi
Quick start kotlin multiplatform Reading From MIDI Port using rtmidi

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

You will get output like this, 21 is the first key of a generic midi keyboard

     > Task :app:runDebugExecutableLibgnuplot
     waiting for 5 seconds
     21
     21
     closing all the midi ports

**Important Note:**
  * You need to install rtmidi library via MSYS2

