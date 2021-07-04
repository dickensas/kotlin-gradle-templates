plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.5.20"
}

version = "1.0.0"

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
}

kotlin {
    js() {
        nodejs() {
           runTask {
                enabled = false
           }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("stdlib-js"))
            }
        }
        
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("stdlib-js"))
            }
        }
    }
}
