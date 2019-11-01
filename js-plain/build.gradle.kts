plugins {
    kotlin("js").version("1.3.70-dev-730")
}

repositories {
    jcenter()
	maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

dependencies {
    implementation(kotlin("stdlib-js"))
}

