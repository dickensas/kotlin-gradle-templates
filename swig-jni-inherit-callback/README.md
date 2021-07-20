# kotlin-gradle-templates / swig-jni-inherit-callback
kotlin JVM with C++ JNI Inheritance and callback

## Usage
The code is ready to execute

If you have gradle in path, then invoke gradle as

     gradle run

If you have wrapper for linux

     ./gradlew run

If you have wrapper for windows

     .\gradlew run
     
You will see the below output, if not works run gradle again
  
      > Task :run
	From C++ Setting callback
	From Kotlin KotlinCallback::run()
	From C++ Callback::~Callback()

Means C++ calls back your Kotlin JVM function 

**Important Note:**
  * You need to install SWIG via MSYS2 or other means


## References

**Web** [Official SWIG](http://www.swig.org/Doc1.3/Java.html#compilation_problems_cpp)<br/>
**Web** [Official Gradle CPP](https://docs.gradle.org/current/userguide/building_cpp_projects.html#sec:custom_cpp_compile_link)<br/>
**GitHub** [Offical SWIG Callback Example](https://github.com/swig/swig/tree/master/Examples/java/callback)