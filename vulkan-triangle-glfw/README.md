# kotlin-gradle-templates / vulkan-triangle-glfw
Quick start kotlin multiplatform Vulkan Triangle example using GLFW

This code programmed for Windows and MSYS2

## Usage
The code is ready to execute 

If you have gradle in path, then invoke gradle as

     gradle assemble

If you have wrapper for linux

     ./gradlew assemble

If you have wrapper for windows

     .\gradlew assemble

Then execute bellow task to start the app

     .\gradlew copyDlls
     .\gradlew runReleaseExecutableLibgnuplot

For executing the exe in windows, copy "shaders" folder to exe folder

The app will open a windows and display a triangle

**Important**: many optimization codes were removed to make the code smaller, this code is only for demo

**For better output**: fences, barrier, uniform buffer etc are mandatory as per Vulkan recommendations 

## References

**Web**: [vulkan-tutorial.com](https://vulkan-tutorial.com/Drawing_a_triangle/Setup/Base_code)

**GitHub**: [Alexander Overvoorde](https://gist.github.com/Overv/7ac07356037592a121225172d7d78f2d)

**CodeProject**: [Igor Kushnarev](https://www.codeproject.com/Articles/1288159/Vulkan-API-with-Kotlin-Native-Project-Setup)

**GitLab**: [Horribile](https://gitlab.com/Horribile/kvarc)

