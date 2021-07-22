import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
                linkerOpts("-L${project.rootDir}")
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