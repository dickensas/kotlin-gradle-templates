plugins {
    `cpp-library`
    `cpp-unit-test`
}

library {
    targetMachines.add(machines.windows.x86_64)
    linkage.set(listOf(Linkage.SHARED))
}

tasks.withType(CppCompile::class.java).configureEach {
	compilerArgs.addAll(toolChain.map { toolChain ->
        when (toolChain) {
            is Gcc, is Clang -> listOf(
            		"-c",
            		"-v",
            		"-fpic",
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
            is Gcc, is Clang -> listOf(
            	"-v",
            	"-lcpplib",
            	"-L../cpplib",
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