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
            }
        }
        compilations["main"].cinterops {
            val rtmidi by creating {
               includeDirs {
                  allHeaders(
                     "C:\\msys64\\mingw64\\include"
                  )
               }
            }
        }
    }
    
    sourceSets {
        val libgnuplotMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-mingwx64:1.5.1-native-mt")
            }
        }
    }
}