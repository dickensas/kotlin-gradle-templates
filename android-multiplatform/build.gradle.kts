buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://maven.google.com/")
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {

        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.20")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.20")
        classpath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.4.20")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

val localRepo = rootProject.file("build/.m2-local")

allprojects {
    repositories {
        maven("file://$localRepo")
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://maven.google.com")
        maven(url = "https://plugins.gradle.org/m2/")
    }
}
