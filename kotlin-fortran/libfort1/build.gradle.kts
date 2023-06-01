plugins {
    `cpp-library`
}

val hostOs = System.getProperty("os.name")

class CompiterArgumentsAction : Action<MutableList<String>> {
	override fun execute(args: MutableList<String>) {
		args.clear()
		args.add("-v")
		args.add("-c")
        args.add("-fPIC")
		args.add("-shared")
		args.add("-o")
		if (hostOs.startsWith("Linux")) {
			args.add("${project.name}.so")
		}
		if (hostOs.startsWith("Windows")) {
			args.add("${project.name}.dll")
        	args.add("-Wl,--out-implib,${project.name}.dll.a")
	        args.add("-Wl,--output-def,${project.name}.def")
	        args.add("-Wl,--export-all-symbols")
	        args.add("-Wl,--enable-auto-import")
        }
	}
}

class PlatformToolChainAction : Action<GccPlatformToolChain> {
    override fun execute(toolChain: GccPlatformToolChain) {
    	toolChain.getCppCompiler().setExecutable("gfortran")
    	toolChain.getCppCompiler().withArguments(CompiterArgumentsAction())
    	toolChain.getcCompiler().setExecutable("gfortran")
    	toolChain.getcCompiler().withArguments(CompiterArgumentsAction())
    	toolChain.getLinker().setExecutable("gfortran")
    }
}

fun buildFile(path: String) = layout.buildDirectory.file(path)

var linkArgs = listOf("")

library {
	if (hostOs == "Mac OS X") {
        targetMachines.add(machines.macOS.x86_64)
        linkArgs = listOf(
        	"-v",
        	"-shared",
        	"-o",
        	"${project.name}.so"
        )
    }
    if (hostOs == "Linux") {
        targetMachines.add(machines.linux.x86_64)
        linkArgs = listOf(
        	"-v",
        	"-shared",
        	"-o",
        	"${project.name}.so"
        )
    }
    if (hostOs.startsWith("Windows")) {
        targetMachines.add(machines.windows.x86_64)
        linkArgs = listOf(
        	"-v",
        	"-shared",
        	"-o",
        	"${project.name}.dll",
        	"-Wl,--out-implib=${project.name}.dll.a", 
            "-Wl,--export-all-symbols",
            "-Wl,--enable-auto-import",
            "-Wl,--output-def=${project.name}.def"
        )
    }
    linkage.set(listOf(Linkage.SHARED))
    toolChains.configureEach {
    	when (this) {
    		is GccCompatibleToolChain -> {
    			this.eachPlatform(PlatformToolChainAction() as Action<in GccPlatformToolChain>)
    		}
    		else -> {}
    	}
	}	
}

tasks.withType(CppCompile::class.java).configureEach {
	compilerArgs.addAll(toolChain.map { toolChain ->
        when (toolChain) {
            is Gcc, is Clang -> listOf()
            else -> listOf()
        }
    })
    
    source.from(fileTree("${project.rootDir}/${project.name}/src/main/fortran").matching {
		include("*.f")
	})
}


tasks.withType(LinkSharedLibrary::class.java).configureEach {
    doFirst {
        copy {
            from(fileTree("${project.rootDir}/${project.name}/build/obj/main/debug").matching {
                include("${project.name}.d*")
                include("${project.name}.so")
                include("${project.name}.d*.*")
            })
            into("${project.rootDir}/${project.name}")
        }
    }
	linkerArgs.addAll(toolChain.map { toolChain ->
		when (toolChain) {
            is Gcc, is Clang -> linkArgs
            else -> listOf()
        }
    })
    doLast {
        copy {
            from(fileTree("${project.rootDir}/${project.name}").matching {
                include("${project.name}.d*")
                include("${project.name}.so")
                include("${project.name}.d*.*")
            })
            into("${project.rootDir}")
        }
        if (hostOs.startsWith("Linux")) {
        	copy {
	        	from(fileTree("${project.rootDir}/${project.name}").matching {
	                include("${project.name}.d*")
	                include("${project.name}.so")
	                include("${project.name}.d*.*")
	            })
	            into("/usr/lib")
            }
        }
    }
}
tasks.withType(Delete::class.java) {
	if (hostOs.startsWith("Linux")) {
		delete(fileTree("/usr/lib").matching {
		    include("${project.name}.d*")
		    include("${project.name}.so")
			include("${project.name}.d*.*")
		})
	}
	delete(fileTree("${project.rootDir}/${project.name}").matching {
	    include("${project.name}.d*")
	    include("${project.name}.so")
		include("${project.name}.d*.*")
	})
}