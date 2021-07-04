plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.5.20"
    application
}

repositories {
    mavenCentral()
    jcenter()
}

var currentOS = org.gradle.internal.os.OperatingSystem.current()
var platform = "win"
if (currentOS.isWindows()) {
    platform = "win"
} else if (currentOS.isLinux()) {
    platform = "linux"
} else if (currentOS.isMacOsX()) {
    platform = "mac"
}

val openjfx by configurations.creating

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.guava:guava:29.0-jre")
    
    implementation("org.openjfx:javafx-base:14:${platform}")
    implementation("org.openjfx:javafx-graphics:14:${platform}")
    implementation("org.openjfx:javafx-controls:14:${platform}")
    implementation("org.openjfx:javafx-fxml:14:${platform}")
    implementation("org.openjfx:javafx-media:14:${platform}")
    implementation("org.openjfx:javafx-web:14:${platform}")
    
    openjfx("org.openjfx:javafx-base:14:${platform}")
    openjfx("org.openjfx:javafx-graphics:14:${platform}")
    openjfx("org.openjfx:javafx-controls:14:${platform}")
    openjfx("org.openjfx:javafx-fxml:14:${platform}")
    openjfx("org.openjfx:javafx-media:14:${platform}")
    openjfx("org.openjfx:javafx-web:14:${platform}")
    
}

application {
    mainClassName = "com.zigma.App"
    applicationDefaultJvmArgs = listOf(
        "--module-path=fxlib",
        "--add-modules=javafx.controls,javafx.fxml,javafx.web"
    )
}
tasks.withType<JavaExec> {
    main  = "com.zigma.App"
    dependsOn("copyFXLibs")
}

tasks {
    register("copyFXLibs", Copy::class) {
        from(openjfx)
        into("fxlib")
    }
}
