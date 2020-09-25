import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.72"
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
}

kotlin {
    mingwX64("libgnuplot") {
        binaries {
            executable {
                entryPoint = "plot.main"
            }
        }
        compilations["main"].cinterops {
            
            val gtk3 by creating {
                includeDirs {
                    allHeaders(
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\lib\\glib-2.0\\include",
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include\\atk-1.0",
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include\\gdk-pixbuf-2.0",
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include\\cairo",
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include\\harfbuzz",
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include\\pango-1.0",
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include\\gtk-3.0",
                        "C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include\\glib-2.0"
                    )
                }
            }
            
            val glew2 by creating {
                includeDirs {
                    allHeaders("C:\\cygwin64\\usr\\x86_64-w64-mingw32\\sys-root\\mingw\\include")
                }
            }
        }
    }
}