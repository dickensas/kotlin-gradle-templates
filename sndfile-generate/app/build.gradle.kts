plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.72"
}

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
   mingwX64("libgnuplot") {
      binaries {
         executable {
              entryPoint = "plot.main"
          }
      }
      compilations["main"].cinterops {
          val sndfile by creating {
              includeDirs {
                 allHeaders(
                    "C:\\msys64\\mingw64\\include"
                 )
              }
          }
      }
   }
}
