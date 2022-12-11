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
            val libfort1 by creating {
                includeDirs {
                    allHeaders(
                        "C:/msys64/mingw64/include"
                    )
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