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
            val python by creating {
                includeDirs {
                    allHeaders("C:/msys64/mingw64/include", "C:/msys64/mingw64/include/python3.8")
                }
            }
        }
    }
    
    linuxX64("libgnuplot1") {
        binaries {
            executable {
                entryPoint = "plot.main"
            }
        }
        compilations["main"].cinterops {
            val python by creating {
                includeDirs {
                    allHeaders("/usr/include", "/usr/include/python3.7m")
                }
            }
        }
    }
}