package plot

import platform.posix.*
import kotlinx.cinterop.*
import perl.*

fun main(args: Array<String>) {
    memScoped { 
    	val argc = alloc<IntVar>()
        argc.value = args.size
        
        val argv = alloc<CPointerVar<CPointerVar<ByteVar>>>()
        argv.value = args.map { it.cstr.ptr }.toCValues().ptr
        
        val env = alloc<CPointerVar<CPointerVar<ByteVar>>>()
        env.value = arrayOf<String>().map { it.cstr.ptr }.toCValues().ptr
        
        val embedding = alloc<CPointerVar<CPointerVar<ByteVar>>>()
        embedding.value = arrayOf<String>( "", "-e", "0" ).map { it.cstr.ptr }.toCValues().ptr
        
        _PERL_SYS_INIT3 ( argc.value, argv.value, env.value )
        
        my_perl = perl_alloc()
        perl_construct(my_perl)
        setFlags()
        
        
        perl_parse(my_perl, null, 3, embedding.value, null);
        perl_run(my_perl);
        
        Perl_eval_pv(my_perl, '$' + "a = 3;", 1)
        var a = _get_sv("a".cstr)
        
        println("a is: " + a);
        
        Perl_eval_pv(my_perl, "$" + "a **= 2;", 1)
        
        println("$a power 2 is : " + _get_sv("a".cstr) );
        
        perl_destruct(my_perl)
        perl_free(my_perl)
        _PERL_SYS_TERM ()

    }
}
