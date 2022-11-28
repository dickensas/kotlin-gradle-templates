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
            staticLib {
                
            }
        }
        compilations["main"].cinterops {
            val avr by creating {
                includeDirs {
                    allHeaders(
                    "C:\\arduino\\hardware\\tools\\avr\\avr\\include",
                    "C:\\arduino\\hardware\\tools\\avr\\i686-w64-mingw32\\avr\\include",
                    "C:\\arduino\\hardware\\tools\\avr\\lib\\gcc\\avr\\7.3.0\\include"
                    )
                }
            }
        }
    }
}