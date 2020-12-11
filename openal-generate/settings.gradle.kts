pluginManagement {
    repositories {
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://maven.google.com/")
        maven(url = "https://plugins.gradle.org/m2/")
        google()
        jcenter()
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "openal-generate"
include("app")
