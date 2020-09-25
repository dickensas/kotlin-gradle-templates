plugins {
    kotlin("js").version(""1.3.72"")
}

repositories {
    jcenter()
	maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

dependencies {
    implementation(kotlin("stdlib-js"))
}

