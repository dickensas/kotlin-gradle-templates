import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
    application
}

repositories {
    mavenCentral()
	mavenLocal()
    google()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks.withType(AbstractCompile::class) {
    dependsOn(":cpplib:assemble")
}

application {
    mainClassName = "AppKt"
}