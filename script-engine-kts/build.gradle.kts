plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly("org.jetbrains.kotlin:kotlin-main-kts:1.8.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.8.0")
}

application {
    mainClass.set("com.zigma.AppKt")
}
