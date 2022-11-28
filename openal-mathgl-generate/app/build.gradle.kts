plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.7.21-RC"
}

val konanPath = System.getenv("KONAN_DATA_DIR")?.let { File(it) }
    ?: File(System.getProperty("user.home")).resolve(".konan")

kotlin {
   mingwX64("libgnuplot") {
      binaries {
         executable {
              entryPoint = "plot.main"
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
