import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.*
plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.8.0"
}
val userHome = File(System.getenv("USERPROFILE") ?: System.getenv("HOME"))
repositories {
    mavenCentral()
	mavenLocal()
    google()
}

kotlin {
	val hostOs = System.getProperty("os.name")
    if (hostOs == "Mac OS X") {
        macosX64("libgnuplot")
    }
    if (hostOs == "Linux") {
        linuxX64("libgnuplot")
    }
    if (hostOs.startsWith("Windows")) {
        mingwX64("libgnuplot")
    }
    targets.withType<KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "plot.main"
				when (preset) {
					presets["mingwX64"] -> linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}")
					presets["linuxX64"] -> linkerOpts("-L${project.rootDir}")
					presets["macosX64"] -> linkerOpts("-L${project.rootDir}")
				}
            }
        }
        compilations["main"].cinterops {
            val python by creating {
				var verson = System.getenv("python_version")
				try {
					var builder = ProcessBuilder("python3","--version")
					builder.redirectErrorStream(true)
	                var process = builder.start()
					var processStdout = process.getErrorStream()
	                var reader = BufferedReader(InputStreamReader(processStdout))
					var line = ""
					while (line != null) {
						try{
							line = reader.readLine()
						}catch(e1:Exception){
							println(e1.getMessage())
							break;
						}
					    println(line)
					    if (line.contains("Python 3.10")) {
					       verson = "3.10"
					    } else if (line.contains("Python 3.9")) {
					       verson = "3.9"
					    } else if (line.contains("Python 3.8")) {
					       verson = "3.8"
					    } else if (line.contains("Python 3.7")) {
					       verson = "3.7"
					    }
					}
					reader.close()
				} catch (e:Exception) {
					e.printStackTrace()
				}
				defFile("${project.rootDir}/embed-python/src/nativeInterop/cinterop/python${verson}.def")
				
				

				when (preset) {
	                presets["mingwX64"] -> includeDirs("C:/msys64/mingw64/include", "C:/msys64/mingw64/include/python${verson}")
					presets["linuxX64"] -> includeDirs("/usr/include", "/usr/include/python${verson}")
					presets["macosX64"] -> includeDirs("/usr/include", "/usr/include/python${verson}")
				}
            }
        }
    }
}