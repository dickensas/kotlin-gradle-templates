import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    kotlin("multiplatform") version "1.5.20"
    id("org.springframework.boot") version "2.2.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("plugin.spring") version "1.5.20"
}

repositories {
    jcenter()
    maven{setUrl("https://dl.bintray.com/kotlin/kotlin-dev" )}
}

kotlin {
   	wasm32("wasm") {
        binaries {
           executable {
               entryPoint = "wasm.main"
           }
       }
    }

    jvm("sboot")
    
    sourceSets {
       val wasmMain by getting {
          dependencies {
	         implementation(kotlin("stdlib"))
	         implementation(kotlin("stdlib-common"))
          }
       }
        
       val sbootMain by getting {
          dependencies {
             implementation("org.springframework.boot:spring-boot-starter")
			 implementation("org.springframework.boot:spring-boot-starter-web")
			 implementation("org.springframework:spring-webmvc")
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
