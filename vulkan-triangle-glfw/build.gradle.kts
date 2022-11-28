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
            val glfw by creating {
                includeDirs {
                    allHeaders("C:\\msys64\\mingw64\\include")
                }
            }
            val vulkan by creating {
                includeDirs {
                    allHeaders("C:\\msys64\\mingw64\\include")
                }
            }
        }
    }
}

tasks {
    register("copyDlls", Copy::class) {
        from("C:\\msys64\\mingw64\\bin") {
            include("glfw3.dll")
            include("libvulkan-1.dll")
        }
        into("build\\bin\\libgnuplot\\releaseExecutable")
    }
}