import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.21"
}

val userHome = File(System.getenv("USERPROFILE") ?: "")

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
                linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}")
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
        }
    }
}