plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.70-dev-1231-17"
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
            val freetype by creating {
                includeDirs {
                    allHeaders(
                        "C:\\msys64\\mingw64\\include\\freetype2",
                        "C:\\msys64\\mingw64\\include"
                    )
                }
            }
            val freeglut by creating {
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
        }
    }
}