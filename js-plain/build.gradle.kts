plugins {
    kotlin("js").version(""1.5.20"")
}

repositories {
    jcenter()
	maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

dependencies {
    implementation(kotlin("stdlib-js"))
}

