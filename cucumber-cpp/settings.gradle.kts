pluginManagement {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
        mavenCentral()
        jcenter()
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-dev")
    }
}
include(":cpplib")
rootProject.name = "cucumber-cpp"
