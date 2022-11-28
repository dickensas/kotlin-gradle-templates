import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
                runTask?.environment("G_ENABLE_DEBUG" to "1")
            }
        }
        compilations["main"].cinterops {
            val mesa by creating {
                includeDirs {
                    allHeaders(
                        "C:\\msys64\\mingw64\\include\\mesa",
                        "C:\\msys64\\mingw64\\include"
                    )
                }
            }
        }
    }
}