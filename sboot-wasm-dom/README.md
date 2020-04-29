# kotlin-gradle-templates / sboot-wasm-dom
Quick start kotlin multiplatform Web Assembly DOM access using advanced usage of `kotlinc` and `jsinterop` via gradle

This code explains how to generate a `.klib` from using both `jsinterop` as well as `kotlinc` commandline

This code explains how to stub a browser JavaScript to WebAssembly and communicate browser HTML DOM tags directly from Korlin Native WebAssembly code

## Usage
The code is ready to execute provided the folder name is not changed, reason being the folder name is coded in spring boot mappings and gradle fies

     make sure the folder name is "sboot-wasm-dom"
     make sure the folder exists "C:\Users\<user usename>\.konan\kotlin-native-windows-1.3.72\bin"
     make sure the Kotlin vesion is "1.3.72"

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start spring boot

     .\gradlew run

Then launch index.html as http://localhost:8080/index.html
 
All your println from Web Assembly will print on the browser console<br/>
The Div tag will be modified using .innerHTML
