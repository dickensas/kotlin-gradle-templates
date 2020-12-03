import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    `maven-publish`
}

group = "com.example.mylib"
version = "1.0"

val localRepo = rootProject.file("build/.m2-local")

publishing {
    repositories {
        maven("file://$localRepo")
    }
}

val cleanLocalRepo by tasks.creating(Delete::class) {
    delete(localRepo)
}

kotlin {
    jvm()
    iosX64()
    targets.withType<KotlinNativeTarget> {
        sourceSets["${targetName}Main"].apply {
            kotlin.srcDir("src/main/kotlin")
        }
        mavenPublication {
            pom {
                 withXml {
                     val root = asNode()
                     root.appendNode("name", "My library")
                     root.appendNode("description", "My Library")
                 }
            }
        }
    }

    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
    }
}
