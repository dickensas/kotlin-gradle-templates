import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.21"
}
val userHome = File(System.getenv("USERPROFILE") ?: System.getenv("HOME"))
repositories {
    mavenCentral()
	mavenLocal()
    google()
}

kotlin {
	val hostOs = System.getProperty("os.name")
    if (hostOs == "Mac OS X") {
        macosX64()
    }
    if (hostOs == "Linux") {
        linuxX64()
    }
    if (hostOs.startsWith("Windows")) {
        mingwX64()
    }
    targets.withType(KotlinNativeTarget::class.java) {
        binaries {
            executable {
                entryPoint = "plot.main"
				when (preset) {
					presets["macosX64"] -> linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}")
					presets["linuxX64"] -> linkerOpts("-L${project.rootDir}")
				}
            }
        }
        compilations["main"].cinterops {
            val python by creating {
				when (preset) {
	                presets["mingwX64"] -> includeDirs("C:/msys64/mingw64/include", "C:/msys64/mingw64/include/python3.10")
					presets["linuxX64"] -> includeDirs("/usr/include", "/usr/include/python3.10")
				}
            }
        }
    }
}