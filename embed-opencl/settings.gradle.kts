pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/kotlin-dev" )
    }
}
enableFeaturePreview("GRADLE_METADATA")
rootProject.name = "embed-opencl"