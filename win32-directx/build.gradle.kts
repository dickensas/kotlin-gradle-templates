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
                linkerOpts("-Wl,--subsystem,windows")
            }
        }
        compilations["main"].cinterops {
            val directx by creating {
                includeDirs {
                    allHeaders(
                    "C:\\msys64\\mingw64\\include",
                    "C:\\MyFiles\\SDKJune2010\\Include"
                    )
                }
            }
        }
    }
}