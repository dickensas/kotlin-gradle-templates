plugins {
    kotlin("multiplatform").version(""1.5.20"")
}

repositories {
    jcenter()
	maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

kotlin {
	mingwX64("wasm32")
}

