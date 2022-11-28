import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile

plugins {
    kotlin("multiplatform") version "1.7.21"
    id("org.springframework.boot") version "2.2.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("plugin.spring") version "1.7.21"
}

repositories {
    mavenCentral()
	mavenLocal()
    google()
}

val hostOs = System.getProperty("os.name")
val isWindows = hostOs.startsWith("Windows")

val packageName = "kotlinx.interop.wasm.dom"
val jsinteropKlibFile = buildDir.resolve("klib").resolve("$packageName-jsinterop.klib")
val mymoduleKlibFile = buildDir.resolve("klib").resolve("mymodule.klib")

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
	         implementation(kotlin("stdlib-js"))
	         implementation(files(mymoduleKlibFile))
	         implementation(files(jsinteropKlibFile))
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

val mymodule by tasks.creating(Exec::class) {
	workingDir = projectDir
	
	val ext = if (isWindows) ".bat" else ""
    val distributionPath = System.getProperty("user.home") as String? + "/.konan/kotlin-native-windows-1.7.21"
    
    val kotlincCommand = file(distributionPath).resolve("bin").resolve("kotlinc$ext")
    
    inputs.property("kotlincCommand", kotlincCommand)
    outputs.file(mymoduleKlibFile)
    
    val ktFile = file(workingDir).resolve("src/myjsMain/kotlin/mymod/App.kt")
    val jsStubFile = file(workingDir).resolve("src/myjsMain/js/stub.js")
    
    commandLine(
        kotlincCommand,
        "-include-binary", jsStubFile,
        "-produce", "library",
        "-o", mymoduleKlibFile,
        "-target", "wasm32",
        ktFile
    )
}

val jsinterop by tasks.creating(Exec::class) {
	dependsOn(mymodule)
	
    workingDir = projectDir

    val ext = if (isWindows) ".bat" else ""
    val distributionPath = System.getProperty("user.home") as String? + "/.konan/kotlin-native-windows-1.7.21"

    val jsinteropCommand = file(distributionPath).resolve("bin").resolve("jsinterop$ext")

    inputs.property("jsinteropCommand", jsinteropCommand)
    inputs.property("jsinteropPackageName", packageName)
    outputs.file(jsinteropKlibFile)
    
    commandLine(
        jsinteropCommand,
        "-pkg", packageName,
        "-o", jsinteropKlibFile,
        "-target", "wasm32"
    )
}

tasks.withType(AbstractKotlinNativeCompile::class).all {
    dependsOn(jsinterop)
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
