# kotlin-gradle-templates / sboot-wasm-webgl
Quick start kotlin multiplatform Web Assembly WebGL using advanced usage of `kotlinc` and `jsinterop` via gradle

This code explains how to generate a `.klib` from using both `jsinterop` as well as `kotlinc` command line

This code explains how to generate a stub dynamically using reference Java Class or Interface

## Usage
The code is ready to execute provided the folder name is not changed, reason being the folder name is coded in spring boot mappings and gradle fies

     make sure the folder name is "sboot-wasm-webgl"
     make sure the folder exists "C:\Users\<user usename>\.konan\kotlin-native-windows-1.3.72\bin"
     make sure the Kotlin vesion is "1.3.72"

If you have gradle in path, then invoke gradle as

     gradle clean assemble

If you have wrapper for linux

     ./gradlew clean assemble

If you have wrapper for windows

     .\gradlew clean assemble

Then execute bellow task to start spring boot

     .\gradlew run

Then launch index.html as `http://localhost:8080/index.html`
 
All your println from Web Assembly will print on the browser console<br/>

The WebGL will draw three squares using `glScissor` and `glClear`

## References

**GitHub**: [LibGDX GL20](https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/graphics/GL20.java)
