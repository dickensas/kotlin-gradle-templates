# kotlin-gradle-templates / swig-jni-cpointer
kotlin JVM with C JNI Pointers and Arrays

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
	calling C add function
	printing result
	37 + 42 = 79
	
	print_array
	[0] = 0
	[1] = 2
	[2] = 4
	[3] = 6
	[4] = 8
	[5] = 10
	[6] = 12
	[7] = 14
	[8] = 16
	[9] = 18
	
	modify_array
	
	print_array
	[0] = 0
	[1] = 6
	[2] = 12
	[3] = 18
	[4] = 24
	[5] = 30
	[6] = 36
	[7] = 42
	[8] = 48
	[9] = 54
	
	copy_array
	
	print_array
	[0] = 0
	[1] = 2
	[2] = 4
	[3] = 6
	[4] = 8
	[5] = 10
	[6] = 12
	[7] = 14
	[8] = 16
	[9] = 18

**Important Note:**
  * You need to install SWIG via MSYS2 or manually


## References

**Web** [Official SWIG](http://www.swig.org/Doc1.3/Java.html#compilation_problems_cpp)<br/>
**Web** [Official Gradle CPP](https://docs.gradle.org/current/userguide/building_cpp_projects.html#sec:custom_cpp_compile_link)

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
