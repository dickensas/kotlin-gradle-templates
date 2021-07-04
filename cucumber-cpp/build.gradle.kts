import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    kotlin("jvm").version("1.5.20")
    application
    java
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    testImplementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    testImplementation("org.seleniumhq.selenium:selenium-api:3.141.59")
    testImplementation("org.seleniumhq.selenium:selenium-chrome-driver:3.141.59")
    

    testImplementation("io.cucumber:cucumber-java:4.8.0")
    testImplementation("io.cucumber:cucumber-java8:4.8.0")
    testImplementation("io.cucumber:cucumber-jvm:4.8.0")
    testImplementation("io.cucumber:cucumber-junit:4.8.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.5.2")
}

tasks.withType(AbstractCompile::class) {
    dependsOn(":cpplib:assemble")
}

application {
    mainClassName = "AppTestKt"
}