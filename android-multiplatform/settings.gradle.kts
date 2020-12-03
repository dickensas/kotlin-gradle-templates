pluginManagement {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
        google()
        jcenter()
        gradlePluginPortal()
        mavenLocal()
    }
}
include(":app")
include(":mylib")
rootProject.name = "android-multiplatform"

