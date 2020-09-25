plugins {
    kotlin("multiplatform").version(""1.3.72"")
}

repositories {
    jcenter()
	maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

kotlin {
	mingwX64("wasm32")
}

