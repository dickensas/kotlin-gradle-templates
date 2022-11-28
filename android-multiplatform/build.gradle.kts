buildscript {
    repositories {
        mavenCentral()
    	mavenLocal()
        google()
    }
    dependencies {

        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.21")
        classpath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.7.21")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

val localRepo = rootProject.file("build/.m2-local")

allprojects {
    repositories {
        maven("file://$localRepo")
        mavenCentral()
    	mavenLocal()
        google()
    }
}
