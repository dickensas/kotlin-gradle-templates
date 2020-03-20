# kotlin-gradle-templates / sboot-jython
Quick start kotlin multiplatform Jython Hello World!

## Usage
The code is ready to execute provided the folder name is not changed

     make sure the folder name is "sboot-jython"

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start spring boot

     .\gradlew run

Then launch index.html as http://localhost:8080/index.html
 
The AJAX request parameter "name" will be received by jython code and converted to 
Hello! &lt;name&gt;, from jython
it will print on the html output
