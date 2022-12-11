
plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.21"
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
                linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}")
            }
        }
        compilations["main"].cinterops {
            val glew by creating {
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

tasks {
    register("copyDlls", Copy::class) {
        from("C:\\msys64\\mingw64\\bin") {
            include("glew32.dll")
            include("glfw3.dll")
        }
        into("build\\bin\\libgnuplot\\releaseExecutable")
    }
}