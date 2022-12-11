# kotlin-gradle-templates / swig-jni
Quick start kotlin JVM with C++ JNI without writing Stub code 

## Usage
The code is ready to execute

If you have gradle in path, then invoke gradle as

     gradle run

If you have wrapper for linux

     ./gradlew run

If you have wrapper for windows

     .\gradlew run
     
You will see the below output
  
      > Task :run
      Hello, World! From C++

**Important Note:**
  * You need to install SWIG via MSYS2


## References

**Web** [Official SWIG](http://www.swig.org/Doc1.3/Java.html#compilation_problems_cpp)<br/>
**Web** [Official Gradle CPP](https://docs.gradle.org/current/userguide/building_cpp_projects.html#sec:custom_cpp_compile_link)

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
