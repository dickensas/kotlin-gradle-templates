pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
    }
}
include(":libfort1")

rootProject.name = "kotlin-fortran"
