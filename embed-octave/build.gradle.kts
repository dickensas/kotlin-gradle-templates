plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.21"
}

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