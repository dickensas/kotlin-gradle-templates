import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.8.10"
}

val userHome = File(System.getenv("USERPROFILE") ?: System.getenv("HOME"))

repositories {
    mavenCentral()
	mavenLocal()
    google()
}

kotlin {
    mingwX64("libgnuplot") {
        binaries {
            executable {
                entryPoint = "plot.main"
                linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}")
            }
        }
        compilations["main"].cinterops {
            val libxml2 by creating {
               includeDirs {
                  allHeaders(
					 "C:\\msys64\\mingw64\\include",
                     "C:\\msys64\\mingw64\\include\\libxml2"
                  )
               }
            }
            
            val libxslt by creating {
               includeDirs {
                  allHeaders(
					 "C:\\msys64\\mingw64\\include",
					 "C:\\msys64\\mingw64\\include\\libxml2"
                  )
               }
            }
        }
    }
}