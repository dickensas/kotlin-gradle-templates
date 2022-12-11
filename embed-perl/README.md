# kotlin-gradle-templates / embed-perl
Embed PERL inside kotlin navtive and get the value from the PERL and display via kotlin

## Usage
The code is ready to execute provided the folder name is not changed

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to run PERL code via C routines through kotlin

     .\gradlew runDebugExecutableLibgnuplot

You will get the below output
 
      > Task :runDebugExecutableLibgnuplot
      a is: 3
      3 power 2 is : 9
      
**Important Note:**
  * You need to execute this code executable from the MSYS264 or cygwin terminal, otherwise many dll files need to be copied to this exe folder


## References

 **Perl Doc** [Evaluating a Perl statement from your C program](https://perldoc.perl.org/perlembed#Evaluating-a-Perl-statement-from-your-C-program)

**Important Note:**
  * You need to execute this code executable from the MSYS2 terminal, otherwise in won't work
  * MSYS2 needs to be installed as c:\msys64
