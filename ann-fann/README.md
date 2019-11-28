# kotlin-gradle-templates / ann-fann
Quick start kotlin multiplatform ANN using FANN library

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

The application will print ANN routing

[example output](http://dickens.co.in/kotlin-fann)

**Important Note:**
  * You need to execute this code executable from the MSYS2
  * Install FANN via pacman using below command
    * pacman -S mingw-w64-x86_64-fann

