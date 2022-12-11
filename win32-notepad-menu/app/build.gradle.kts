plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.20"
}

val userHome = File(System.getenv("USERPROFILE") ?: "")

repositories {
    mavenCentral()
	mavenLocal()
    google()
}

kotlin {
    mingwX64("notepadMenu") {
        binaries {
            executable {
                entryPoint = "com.zigma.notepadauto.main"
                linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}", "-Wl,--subsystem,windows")
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
