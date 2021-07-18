pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
    }
}
include(":cpplib")

rootProject.name = "swig-jni"
