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
                    allHeaders("C:/msys64/mingw64/include")
                }
            }
            val pango by creating {
                includeDirs {
                    allHeaders(
                        "C:\\msys64\\mingw64\\include",
                        "C:\\msys64\\mingw64\\lib\\glib-2.0\\include",
                        "C:\\msys64\\mingw64\\include\\glib-2.0",
                        "C:\\msys64\\mingw64\\include\\glib-2.0\\include",
                        "C:\\msys64\\mingw64\\include\\glib-2.0\\glib",
                        "C:\\msys64\\mingw64\\include\\pango-1.0",
                        "C:\\msys64\\mingw64\\include\\cairo"
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
        }
    }
}