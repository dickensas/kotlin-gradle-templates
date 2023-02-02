
plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.8.0"
}

val userHome = File(System.getenv("USERPROFILE") ?: "")

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
                linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}", "-mwindows")
            }
        }
        compilations["main"].cinterops {
            val gles by creating {
                includeDirs {
                    allHeaders("C:\\msys64\\mingw64\\include")
                }
            }
            val glfw by creating {
                includeDirs {
                    allHeaders("C:\\msys64\\mingw64\\include")
                }
            }
        }
    }
}