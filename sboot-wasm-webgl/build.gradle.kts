import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile
import com.badlogic.gdx.graphics.*
import java.io.FileOutputStream

buildscript {
    dependencies {
        classpath("com.badlogicgames.gdx:gdx:1.9.10")
    }
}

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
val webglKlibFile = buildDir.resolve("klib").resolve("webgl.klib")

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
	         implementation(files(webglKlibFile))
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

val stubgenerator by tasks.creating() {
    var workingDir = projectDir
    var fields = GL20::class.java.fields
    
    var ktsb:StringBuilder = StringBuilder()
    ktsb.append("package webgl\r\n\r\n")
    ktsb.append("import kotlinx.wasm.jsinterop.*\r\n\r\n")
    ktsb.append("@SymbolName(\"getGLContext\")\r\n")
    ktsb.append("external public fun getGLContext(arena: Int, index: Int, contextPtr: Int, contextLen: Int, resultArena: Int): Int\r\n")
    
    var ktsbc:StringBuilder = StringBuilder()
    
    var sb:StringBuilder = StringBuilder()
    sb.append("konan.libraries.push ({")
    sb.append("""
    getGLContext: function(arena, obj, contextPtr, contextLen, resultArena) {
      var context = toUTF16String(contextPtr, contextLen);
      var result = kotlinObject(arena, obj).getContext(context);
      return toArena(resultArena, result);
    },
""")
    
    for(f in fields){
      var fname = ""
      sb.append("    ")
      if(f.getName().startsWith("GL_")){
        fname = f.getName().substring(3)
      }else{
        fname = f.getName()
      }
      ktsb.append("@SymbolName(\"_GL_" + fname + "\")\r\n")
      sb.append("_GL_"+fname)
      sb.append(": function(arena, obj) {\r\n")
      sb.append("       return kotlinObject(arena, obj)." + fname + ";\r\n")
      sb.append("    },\r\n")
      
      ktsb.append("external public fun _GL_" + fname + "(arena: Int, index: Int): Int\r\n\r\n")
      
      ktsbc.append("    val GL_"+fname+": Int\r\n")
      ktsbc.append("       get() {\r\n")
      ktsbc.append("          return _GL_" + fname + "(this.arena, this.index)\r\n");
      ktsbc.append("       }\r\n")
    }
    
    var methods = GL20::class.java.methods
    for(f in methods){
      var fname = f.getName()
      ktsb.append("@SymbolName(\"_" + fname + "\")\r\n")
      sb.append("    _" + fname)
      var params = f.getParameters()
      var strParams = ""
      var ktStrParams = ""
      for(p in params){
          strParams = strParams + p.getName() + ", "
          ktStrParams = ktStrParams + p.getName() +":"
          if(p.getType().getName().toLowerCase().contains("int"))
            ktStrParams = ktStrParams + " Int"
          else if(p.getType().getName().toLowerCase().contains("float"))
            ktStrParams = ktStrParams + " Float"
          else
            ktStrParams = ktStrParams + " JsValue"
            
          ktStrParams = ktStrParams+ ","
      }
      if(!strParams.trim().equals(""))
          strParams = strParams.substring(0,strParams.length-2)
      if(!ktStrParams.trim().equals(""))
          ktStrParams = ktStrParams.substring(0,ktStrParams.length-1)
          
      sb.append(": function(arena, obj " + (if (strParams.trim().equals("")) "" else ", "+strParams) + ") {\r\n")
      sb.append("       kotlinObject(arena, obj)." + (fname.substring(2,3).toLowerCase() + fname.substring(3)) + "(" + strParams + ");\r\n")
      sb.append("    },\r\n")
      
      ktsbc.append("    fun "+fname+"(" + ktStrParams + "):")
      
      
      ktsb.append("external public fun _" + fname + "(arena: Int, index: Int" + (if (ktStrParams.trim().equals("")) "" else ", "+ktStrParams) + "): ")
      if(f.getReturnType().getName().toLowerCase().contains("void")){
          ktsb.append("Unit\r\n\r\n")
          ktsbc.append("Unit {\r\n")
          ktsbc.append("       _" + fname + "(this.arena, this.index" + (if (strParams.trim().equals("")) "" else ", "+strParams) + ")\r\n")
          ktsbc.append("    }\r\n")
      }else{
          ktsb.append("Int\r\n\r\n")
          ktsbc.append("Int {\r\n")
          ktsbc.append("       return _" + fname + "(this.arena, this.index" + (if (strParams.trim().equals("")) "" else ", "+strParams) + ")\r\n")
          ktsbc.append("    }\r\n")
      }
    }
    sb.deleteCharAt(sb.length-1)
    sb.deleteCharAt(sb.length-1)
    sb.deleteCharAt(sb.length-1)
    sb.append("\r\n})\r\n")
    
    
    
    ktsb.append("open class GLContext(arena: Int, index: Int): JsValue(arena, index) {\r\n")
    ktsb.append("constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)\r\n")
    ktsb.append(ktsbc)
    ktsb.append("}\r\n\r\n")
    
ktsb.append("""
open class GLCanvas(arena: Int, index: Int): JsValue(arena, index) {
  constructor(jsValue: JsValue): this(jsValue.arena, jsValue.index)
  fun  getContext(context: String): GLContext {
    val wasmRetVal = getGLContext(this.arena, this.index, stringPointer(context), stringLengthBytes(context), ArenaManager.currentArena)
    return GLContext(ArenaManager.currentArena, wasmRetVal)
  }

  companion object {
  }
}
val JsValue.asGLCanvas: GLCanvas
  get() {
    return GLCanvas(this.arena, this.index)
  }
""")
FileOutputStream(file(projectDir).resolve("src/webglMain/js/stub.js")).write(sb.toString().toByteArray())
FileOutputStream(file(projectDir).resolve("src/webglMain/kotlin/webgl/App.kt")).write(ktsb.toString().toByteArray())
    
}

val stubcompiler by tasks.creating(Exec::class) {
    dependsOn(stubgenerator)
	workingDir = projectDir
	
	val ext = if (isWindows) ".bat" else ""
    val distributionPath = System.getProperty("user.home") as String? + "/.konan/kotlin-native-windows-1.7.21"
    
    val kotlincCommand = file(distributionPath).resolve("bin").resolve("kotlinc$ext")
    
    inputs.property("kotlincCommand", kotlincCommand)
    outputs.file(webglKlibFile)
    
    val ktFile = file(workingDir).resolve("src/webglMain/kotlin/webgl/App.kt")
    val jsStubFile = file(workingDir).resolve("src/webglMain/js/stub.js")
    
    commandLine(
        kotlincCommand,
        "-include-binary", jsStubFile,
        "-produce", "library",
        "-o", webglKlibFile,
        "-target", "wasm32",
        ktFile
    )
}

val jsinterop by tasks.creating(Exec::class) {
	dependsOn(stubcompiler)
	
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
