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
            val opencl by creating {
                includeDirs {
                    allHeaders(
                        "C:/msys64/mingw64/include"
                    )
                }
            }
        }
    }
}