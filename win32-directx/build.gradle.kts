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