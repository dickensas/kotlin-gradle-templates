# kotlin-gradle-templates / js-nodejs
Quick start kotlin stdlib-js gradle DSL targetting Node.JS

## Usage

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble
     
To run the code

      npm start
      
You will get the output

       > js-nodejs@1.0.0 start C:\<your folder name>\js-nodejs
       > node build\js\packages\js-nodejs\kotlin\js-nodejs.js

       hello
       hello test1
 
All your println from kotlin will print on the command prompt via Node.JS


**Important Note:**
  * You need to install Node.JS otherwise gradle will automatically install Node.JS in a different folder 