plugins {
    `cpp-library`
}

class CompiterArgumentsAction : Action<MutableList<String>> {
	override fun execute(args: MutableList<String>) {
		args.clear()
		args.add("-v")
		args.add("-c")
        args.add("-fPIC")
		args.add("-shared")
		args.add("-o")
		args.add("${project.name}.dll")
        args.add("-Wl,--out-implib,${project.name}.dll.a")
        args.add("-Wl,--output-def,${project.name}.def")
        args.add("-Wl,--export-all-symbols")
        args.add("-Wl,--enable-auto-import")
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

library {
    targetMachines.add(machines.windows.x86_64)
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
                include("${project.name}.d*.*")
            })
            into("${project.rootDir}/${project.name}")
        }
    }
	linkerArgs.addAll(toolChain.map { toolChain ->
		when (toolChain) {
            is Gcc, is Clang -> listOf(
            	"-v",
            	"-shared",
            	"-o",
            	"${project.name}.dll",
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
            from(fileTree("${project.rootDir}/${project.name}").matching {
                include("${project.name}.d*")
                include("${project.name}.d*.*")
            })
            into("${project.rootDir}")
        }
    }
}
tasks.withType(Delete::class) {
	delete(fileTree("${project.rootDir}/${project.name}").matching {
	    include("${project.name}.d*")
		include("${project.name}.d*.*")
	})
}