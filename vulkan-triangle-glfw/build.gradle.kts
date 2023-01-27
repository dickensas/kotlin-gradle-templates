import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.8.0"
}

val userHome = File(System.getenv("USERPROFILE") ?: "")

repositories {
    mavenCentral()
	mavenLocal()
    google()
}

kotlin {
	val hostOs = System.getProperty("os.name")
    if (hostOs == "Mac OS X") {
        macosX64("libgnuplot")
    }
    if (hostOs == "Linux") {
        linuxX64("libgnuplot")
    }
    if (hostOs.startsWith("Windows")) {
        mingwX64("libgnuplot")
    }
    targets.withType<KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "plot.main"
                when (preset) {
					presets["mingwX64"] -> linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}")
					presets["linuxX64"] -> linkerOpts("-L${project.rootDir}")
					presets["macosX64"] -> linkerOpts("-L${project.rootDir}")
				}
            }
        }
        compilations["main"].cinterops {
            val glfw by creating {
				when (preset) {
					presets["mingwX64"] -> includeDirs("C:/msys64/mingw64/include")
					presets["linuxX64"] -> includeDirs("/usr/include")
					presets["macosX64"] -> includeDirs("/usr/include")
				}
            }
            val vulkan by creating {
				when (preset) {
	                presets["mingwX64"] -> includeDirs("C:/msys64/mingw64/include")
					presets["linuxX64"] -> includeDirs("/usr/include")
					presets["macosX64"] -> includeDirs("/usr/include")
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