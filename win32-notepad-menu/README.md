# kotlin-gradle-templates / win32-notepad-menu
Quick start kotlin multiplatform WIN32 API based windows Notepad detection code

## Usage
The code is ready to execute 

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start the app

     .\gradlew runReleaseExecutableNotepadMenu

if a blank notepad not exists it will display

      please open a blank notepad window

If a blank nitepad open it will display the list of menus in the notepad window

      &File
      &Edit
      F&ormat
      &View
      &Help


**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64

