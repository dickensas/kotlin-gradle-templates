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
            val glfw by creating {
                includeDirs {
                    allHeaders(
                        "C:\\msys64\\mingw64\\include\\GL",
                        "C:\\msys64\\mingw64\\include"
                    )
                }
            }
            val glew by creating {
                includeDirs {
                    allHeaders(
                        "C:\\msys64\\mingw64\\include\\GL",
                        "C:\\msys64\\mingw64\\include"
                    )
                }
            }
            val mgl by creating {
                includeDirs {
                    allHeaders(
                        "${project.rootDir}/include",
                        "C:\\msys64\\mingw64\\include"
                    )
                }
            }
        }
    }
}