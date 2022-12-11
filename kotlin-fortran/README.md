# kotlin-gradle-templates / kotlin-fortran
Combine Kotlin and Fortran in using kotlin gradle kts

This gradle kts demonstrates how to change the toolchain exe using gradle kts

## Usage
The code is ready to execute

If you have gradle in path, then invoke gradle as

     gradle runDebugExecutableLibgnuplot

If you have wrapper for linux

     ./gradlew runDebugExecutableLibgnuplot

If you have wrapper for windows

     .\gradlew runDebugExecutableLibgnuplot
     
You will see the below output
  
      > Task :runDebugExecutableLibgnuplot
      I am a fortran subroutine

**Important Note:**
  * You need to install mingw-w64-x86_64-gcc-libgfortran and mingw-w64-x86_64-gcc-fortran via pacman inside MSYS2


## References

**Gradle** [Forum](https://discuss.gradle.org/t/native-support-for-fortran-and-c-c-mixed/21414)<br/>

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
