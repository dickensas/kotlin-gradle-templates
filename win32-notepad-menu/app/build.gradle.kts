plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.20"
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
}

kotlin {
    mingwX64("notepadMenu") {
        binaries {
            executable {
                entryPoint = "com.zigma.notepadauto.main"
            }
        }
        compilations["main"].cinterops {
            
            val win32api by creating {
                includeDirs {
                    allHeaders(
                        "C:/msys64/mingw64/include",
                        "C:/msys64/usr/include",
                        "C:/msys64/usr/include/win32api"
                    )
                }
            }
            
        }
    }
}
