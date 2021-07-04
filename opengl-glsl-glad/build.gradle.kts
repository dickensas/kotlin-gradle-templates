plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.5.20"
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
            val glad by creating {
                includeDirs {
                    allHeaders("${project.rootDir}/glad")
                }
            }
            val glfw by creating {
                includeDirs {
                    allHeaders("C:/msys64/mingw64/include")
                }
            }
        }
    }
}

tasks {
    register("copyDlls", Copy::class) {
        from("C:\\msys64\\mingw64\\bin") {
            include("glfw3.dll")
        }
        into("build\\bin\\libgnuplot\\releaseExecutable")
    }
}