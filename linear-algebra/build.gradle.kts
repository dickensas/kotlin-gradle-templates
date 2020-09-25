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
            val lapack by creating {
                includeDirs {
                    allHeaders(
                        "C:/msys64/mingw64/include",
                        "C:/msys64/mingw64/include/OpenBLAS"
                    )
                }
            }
        }
    }
}