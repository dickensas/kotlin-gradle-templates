import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.20"
    application
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks.withType(Delete::class) {
	dependsOn(":cpplib:clean")
    delete(rootProject.buildDir)
    delete("cpplib.dll")
    delete("cpplib.def")
    delete("cpplib.dll.a")
    delete(fileTree("src/main/java").matching {
        include("*.java")
    })
}

tasks.withType(AbstractCompile::class) {
    dependsOn(":cpplib:assemble")
}

application {
    mainClassName = "AppKt"
}