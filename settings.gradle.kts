pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}
rootProject.name="kotlin-gradle-templates"
include("kotlin-libxml2:app")
include("kotlin-cpp:cpplib")
include("kotlin-cpp:cpplib_c")
include("kotlin-cpp")
include("sboot-mockito-integration")
include("vulkan-triangle-glfw")
