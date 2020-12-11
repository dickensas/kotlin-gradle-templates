plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.4.20"
}

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
      }
   }
}
