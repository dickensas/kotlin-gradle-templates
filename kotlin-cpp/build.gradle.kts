import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.*

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.8.0"
}
val userHome = File(System.getenv("USERPROFILE") ?: System.getenv("HOME"))
repositories {
    mavenCentral()
	mavenLocal()
    google()
}

kotlin {
	val hostOs = System.getProperty("os.name")
    if (hostOs == "Mac OS X") {
        macosX64("libgnuplot")
    }
    if (hostOs == "Linux") {
        linuxX64("libgnuplot")
    }
    if (hostOs.startsWith("Windows")) {
        mingwX64("libgnuplot")
    }
    targets.withType<KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "plot.main"
				when (preset) {
					presets["mingwX64"] -> linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}", "-L${project.rootDir}/cpplib_c", "-L${project.rootDir}/kotlin-cpp/cpplib_c")
					presets["linuxX64"] -> linkerOpts("-L${project.rootDir}", "-L${project.rootDir}/cpplib", "-L${project.rootDir}/kotlin-cpp/cpplib_c")
					presets["macosX64"] -> linkerOpts("-L${project.rootDir}", "-L${project.rootDir}/cpplib", "-L${project.rootDir}/kotlin-cpp/cpplib_c")
				}
            }
        }
        compilations["main"].cinterops {
            val cpplib by creating {
				when (preset) {
	                presets["mingwX64"] -> includeDirs("C:/msys64/mingw64/include", "${project.rootDir}/cpplib_c/src/main/public","${project.rootDir}/kotlin-cpp/cpplib_c/src/main/public")
					presets["linuxX64"] -> includeDirs("/usr/include", "${project.rootDir}/cpplib_c/src/main/public","${project.rootDir}/kotlin-cpp/cpplib_c/src/main/public")
					presets["macosX64"] -> includeDirs("/usr/include", "${project.rootDir}/cpplib_c/src/main/public","${project.rootDir}/kotlin-cpp/cpplib_c/src/main/public")
				}
            }
        }
    }
}