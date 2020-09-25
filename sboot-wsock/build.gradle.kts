import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    kotlin("multiplatform") version "1.3.72"
    id("org.springframework.boot") version "2.2.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("plugin.spring") version "1.3.72"
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
			 implementation("org.springframework.boot:spring-boot-starter-websocket")
			 implementation("org.springframework:spring-webmvc")
			 implementation("org.webjars:webjars-locator:0.39")
			 implementation("org.webjars:sockjs-client:1.1.2")
			 implementation("org.webjars:stomp-websocket:2.3.3-1")
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
