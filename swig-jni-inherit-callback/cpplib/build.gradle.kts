plugins {
    `cpp-library`
    `cpp-unit-test`
}

library {
    targetMachines.add(machines.windows.x86_64)
    linkage.set(listOf(Linkage.SHARED))
}

val javaHome = System.getenv("JAVA_HOME")

val swigTask: Exec by tasks.creating(Exec::class) {
	commandLine (
		"swig",
		"-c++",
		"-java",
		"-cppext",
		"cpp",
		"-addextern",
		"-directors",
		"-module",
		"${project.name}",
		"src\\main\\swig\\${project.name}.i"
	)
}
        
tasks.withType(CppCompile::class.java).configureEach {
    dependsOn(swigTask)
    doFirst {
    	copy {
    		from("${project.rootDir}/${project.name}/src/main/swig")
    		into("${project.rootDir}/${project.name}/src/main/cpp")
    		exclude("*.java")
    		exclude("*.i")
    	}
    }
	compilerArgs.addAll(toolChain.map { toolChain ->
        when (toolChain) {
            is Gcc, is Clang -> listOf(
            		"-c",
            		"-v",
            		"-fpic",
            		"-I/usr/java/include",
            		"-I$javaHome/include",
            		"-I$javaHome/include/win32"
            )
            else -> listOf()
        }
    })
}


tasks.withType(LinkSharedLibrary::class.java).configureEach {
	linkerArgs.addAll(toolChain.map { toolChain ->
		when (toolChain) {
            is Gcc, is Clang -> listOf(
            	"-v",
            	"-g",
            	"-Wl,--out-implib=${project.name}.dll.a", 
            	"-Wl,--export-all-symbols",
            	"-Wl,--enable-auto-import",
            	"-Wl,--output-def=${project.name}.def"
            )
            else -> listOf()
        }
    })
    doLast {
    	copy {
    		from("${project.rootDir}/${project.name}/build/lib/main/debug")
    		into("${project.rootDir}")
    		exclude("*.java")
    		exclude("*.i")
    		exclude("*.obj")
    		exclude("*.h")
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/src/main/swig")
    		into("${project.rootDir}/src/main/java")
    		exclude("*.i")
    		exclude("*.cpp")
    		exclude("*.c")
    		exclude("*.h")
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/${project.name}.dll.a")
    		into("${project.rootDir}")
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/${project.name}.def")
    		into("${project.rootDir}")
    	}
    }
}
tasks.withType(Delete::class) {
	delete(fileTree("${project.rootDir}/${project.name}").matching {
		include("${project.name}.d*")
		include("${project.name}.d*.*")
	})
    delete(fileTree("src/main/swig").matching {
        include("*.java")
        include("*.c*")
    })
    delete(fileTree("src/main/cpp").matching {
		include("${project.name}_wrap.*")
	})
}