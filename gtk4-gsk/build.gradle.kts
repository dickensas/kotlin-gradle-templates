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
            
            val gtk4 by creating {
                includeDirs {
                    allHeaders(
                        "C:\\msys64\\mingw64\\lib\\glib-2.0\\include",
                        "C:\\msys64\\mingw64\\lib\\graphene-1.0\\include",
                        "C:\\msys64\\mingw64\\include\\atk-1.0",
                        "C:\\msys64\\mingw64\\include\\gdk-pixbuf-2.0",
                        "C:\\msys64\\mingw64\\include\\cairo",
                        "C:\\msys64\\mingw64\\include\\harfbuzz",
                        "C:\\msys64\\mingw64\\include\\pango-1.0",
                        "C:\\msys64\\mingw64\\include\\gtk-4.0",
                        "C:\\msys64\\mingw64\\include\\glib-2.0",
                        "C:\\msys64\\mingw64\\include\\graphene-1.0",
                        "C:\\msys64\\mingw64\\include"
                    )
                }
            }
            
        }
    }
}