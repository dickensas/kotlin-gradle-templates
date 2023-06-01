import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.8.0"
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
					presets["mingwX64"] -> linkerOpts("-L${project.rootDir}/libfort1","-L${project.rootDir}","-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib")
					presets["linuxX64"] -> linkerOpts("-L${project.rootDir}/libfort1","-L${project.rootDir}","-L${userHome}/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/x86_64-unknown-linux-gnu/lib64","-L${userHome}/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/sysroot/usr/lib64")
					presets["macosX64"] -> linkerOpts("-L${project.rootDir}")
				}
            }
        }
        compilations["main"].cinterops {
            val libfort1 by creating {
                when (preset) {
					presets["mingwX64"] -> includeDirs("C:/msys64/mingw64/include")
					presets["linuxX64"] -> includeDirs("/usr/include")
					presets["macosX64"] -> includeDirs("/usr/include")
                }
            }
        }
    }
}
tasks.withType(Delete::class) {
    dependsOn(":libfort1:clean")
    delete(rootProject.buildDir)
    delete(fileTree("${project.rootDir}").matching {
        include("libfort1.d*")
        include("libfort1.d*.*")
    })
}
tasks.withType(AbstractCompile::class) {
    dependsOn(":libfort1:assemble")
}