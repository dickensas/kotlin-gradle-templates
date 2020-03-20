import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    kotlin("multiplatform") version "1.3.71-release-360"
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("plugin.spring") version "1.3.71-release-360"
}

repositories {
    jcenter()
    maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

kotlin {
    jvm("sboot")
    
    sourceSets {
       val sbootMain by getting {
          dependencies {
             implementation("org.springframework.boot:spring-boot-starter")
			 implementation("org.springframework.boot:spring-boot-starter-web")
			 implementation("org.springframework:spring-webmvc")
			 implementation("org.python:jython-standalone:2.7.2rc1")
			 implementation("org.webjars:webjars-locator:0.39")
			 implementation("org.webjars:jquery:3.4.1")
			 implementation("javax.servlet:javax.servlet-api")
			 implementation(kotlin("reflect"))
			 implementation(kotlin("stdlib-jdk8"))
          }
       }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val run by tasks.creating(JavaExec::class) {
    val sboot: KotlinJvmTarget by kotlin.targets
    val sbootMain = sboot.compilations["main"]

    main = "sboot.AppKt"
    classpath = files(sbootMain.output) + sbootMain.runtimeDependencyFiles
}
