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
            
            val perl by creating {
                includeDirs {
                    allHeaders(
                        "C:/msys64/mingw64/include",
                        "C:/msys64/mingw64/lib/perl5/core_perl/CORE"
                    )
                }
            }
            
        }
    }
}