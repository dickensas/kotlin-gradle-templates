# kotlin-gradle-templates / cucumber-cpp
Quick start kotlin jvm project for cucumber Windows Keyboard keys with WIN32 C++ backing with auto SWIG Stub enabled

No need to write JNI code and Stub code

## Usage
The code is ready to execute provided you keep your cursor in any text editor application with a line of text

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start test automation

     .\gradlew test

keep your cursor in any text editor application with a line of text

the automation script will select the line using below sequence of keyboard strokes 

1.) HOME
2.) SHIFT + END
3.) CTRL + C 
4.) END
5.) ENTER
6.) CTRL + V

