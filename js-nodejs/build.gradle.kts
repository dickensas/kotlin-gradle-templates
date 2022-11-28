plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.21"
}

version = "1.0.0"

repositories {
    mavenCentral()
	mavenLocal()
    google()
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
