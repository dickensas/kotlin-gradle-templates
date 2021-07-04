pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        mavenLocal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
    }
}

rootProject.name = "sndfile-generate"
include("app")
