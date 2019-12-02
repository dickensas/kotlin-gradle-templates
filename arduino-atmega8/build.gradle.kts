import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.70-dev-2153"
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
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