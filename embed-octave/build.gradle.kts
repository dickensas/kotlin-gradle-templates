plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.72"
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
            val octave by creating {
                includeDirs {
                    allHeaders(
                        "C:/octave/mingw64/include/octave-5.1.0",
                        "C:/octave/mingw64/include"
                    )
                }
            }
        }
    }
}