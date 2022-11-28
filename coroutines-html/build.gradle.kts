plugins {
    kotlin("js").version("1.3.50")
}

repositories {
    mavenCentral()
	mavenLocal()
    google()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-common:0.6.11")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.6.11")
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.6.11")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.2")
}