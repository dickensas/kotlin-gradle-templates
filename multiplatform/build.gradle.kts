plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.3.70-dev-730")
}

repositories {
    jcenter()
	maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

kotlin {
	mingwX64("wasm32")
}

