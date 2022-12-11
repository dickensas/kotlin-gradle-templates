plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.21-RC"
}

val userHome = File(System.getenv("USERPROFILE") ?: "")

val konanPath = System.getenv("KONAN_DATA_DIR")?.let { File(it) }
    ?: File(System.getProperty("user.home")).resolve(".konan")

kotlin {
   mingwX64("libgnuplot") {
      binaries {
         executable {
              entryPoint = "plot.main"
              linkerOpts("-L${userHome}\\.konan\\dependencies\\msys2-mingw-w64-x86_64-2\\x86_64-w64-mingw32\\lib", "-L${project.rootDir}")
         }
      }
      compilations["main"].cinterops {
          val openal by creating {
              includeDirs {
                 allHeaders(
                    "C:\\msys64\\mingw64\\include"
                 )
              }
          }
          val mgl by creating {
            includeDirs {
               allHeaders(
                  "${project.rootDir}/include",
                  "C:\\msys64\\mingw64\\include"
               )
            }
          }
      }
   }
}
