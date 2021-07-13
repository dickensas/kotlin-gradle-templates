plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.5.20"
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
}

kotlin {
    mingwX64("libgnuplot") {
        binaries {
            executable {
                entryPoint = "plot.main"
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