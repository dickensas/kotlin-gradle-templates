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
		"-java",
		"-cppext",
		"c",
		"-addextern",
		"-module",
		"${project.name}",
		"src\\main\\swig\\${project.name}.i"
	)
}
        
tasks.withType(CppCompile::class.java).configureEach {
    dependsOn(swigTask)
    doFirst {
    	copy {
    		from("${project.rootDir}/${project.name}/src/main/swig") {
    			rename("${project.name}_wrap.c", "${project.name}_wrap.cpp")
    		}
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
            		"-x",
            		"c",
            		"-fpic",
            		"-std=gnu99",
            		"-Wall",
            		"-Werror",
            		"-Wno-unused",
            		"-nodefaultlibs",
            		"-lc",
            		"-g",
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
    	}
    	
    	copy {
    		from("${project.rootDir}/${project.name}/src/main/swig")
    		into("${project.rootDir}/src/main/java")
    		exclude("*.i")
    		exclude("*.cpp")
    		exclude("*.c")
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
	delete("${project.name}.dll")
    delete("${project.name}.def")
    delete("${project.name}.dll.a")
    delete(fileTree("src/main/swig").matching {
        include("*.java")
        include("*.cpp")
        include("*.c")
    })
    delete("src/main/cpp/${project.name}_wrap.cpp")
}