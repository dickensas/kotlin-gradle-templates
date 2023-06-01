plugins {
    `cpp-library`
    `cpp-unit-test`
}

val hostOs = System.getProperty("os.name")
var linkArgs = listOf("")

library {
    if (hostOs == "Mac OS X") {
        targetMachines.add(machines.macOS.x86_64)
        linkArgs = listOf(
        	"-v",
        	"-shared",
        	"-lcpplib",
            "-L../cpplib"
        )
    }
    if (hostOs == "Linux") {
        targetMachines.add(machines.linux.x86_64)
        linkArgs = listOf(
        	"-v",
        	"-shared",
        	"-lcpplib",
            "-L../cpplib"
        )
    }
    if (hostOs.startsWith("Windows")) {
        targetMachines.add(machines.windows.x86_64)
        linkArgs = listOf(
        	"-v",
        	"-shared",
        	"-lcpplib",
        	"-L../cpplib",
        	"-Wl,--out-implib=${project.name}.dll.a", 
        	"-Wl,--export-all-symbols",
        	"-Wl,--enable-auto-import",
        	"-Wl,--output-def=${project.name}.def"
        )
    }
    linkage.set(listOf(Linkage.SHARED))
}

tasks.withType(CppCompile::class.java).configureEach {
	compilerArgs.addAll(toolChain.map { toolChain ->
        when (toolChain) {
            is Gcc, is Clang -> listOf(
        		"-c",
        		"-v",
        		"-shared",
        		"-fPIC",
        		"-lstdc++",
        		"-lcpplib",
        		"-L../cpplib",
        		"-I../cpplib/src/main/public"
            )
            else -> listOf()
        }
    })
}

tasks.withType(LinkSharedLibrary::class.java).configureEach {
	linkerArgs.addAll(toolChain.map { toolChain ->
		when (toolChain) {
            is Gcc, is Clang -> linkArgs
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
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/build/lib/main/debug")
    		into("${project.rootDir}/${project.name}")
    		exclude("*.java")
    		exclude("*.i")
    		exclude("*.obj")
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/${project.name}.dll.a")
    		into("${project.rootDir}")
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/${project.name}.def")
    		into("${project.rootDir}")
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/lib${project.name}.so")
    		into("${project.rootDir}")
    	}
    }
}