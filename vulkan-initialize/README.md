# kotlin-gradle-templates / vulkan-initialize
Quick start kotlin multiplatform Vulkan Initialize and Check

## Usage
The code is ready to execute 

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start the app

     .\gradlew copyDlls
     .\gradlew runReleaseExecutableLibgnuplot

The app will try to initiate Vulkan and print success in console or error accordingly

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
