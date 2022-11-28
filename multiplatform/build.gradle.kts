plugins {
    kotlin("multiplatform").version(""1.7.21"")
}

repositories {
    mavenCentral()
	mavenLocal()
    google()
}

kotlin {
	mingwX64("wasm32")
}

