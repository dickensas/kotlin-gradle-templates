pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}
rootProject.name="kotlin-gradle-templates"
include("kotlin-cpp:cpplib")
include("kotlin-cpp:cpplib_c")
include("kotlin-cpp")
include("sboot-mockito-integration")
