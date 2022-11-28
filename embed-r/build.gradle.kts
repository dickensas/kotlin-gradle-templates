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
            
            val R by creating {
                includeDirs {
                    allHeaders(
                        "C:/ROpen/R353/include"
                    )
                }
            }
            
        }
    }
}