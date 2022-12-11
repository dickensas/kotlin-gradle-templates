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
            val python by creating {
                includeDirs {
                    allHeaders("C:/msys64/mingw64/include", "C:/msys64/mingw64/include/python3.10")
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
                    allHeaders("/usr/include", "/usr/include/python3.10")
                }
            }
        }
    }
}