import cpplibJNI.*

class App {
	fun demo() {
		//Example 1
		var a = new_intp()
	    var b = new_intp()
	    var c = new_intp()
	    
	    intp_assign(a,37)
	    intp_assign(b,42)
	    
	    println("calling C add function")
	    
	    add(a,b,c)
	    
	    var res = intp_value(c)
	    println("printing result")
	    println("37 + 42 = " + res)
	    
	    //Example 2
	    var a1 = new_doubleArray(10)
	    
	    for( i in 0..10){
    		doubleArray_setitem(a1, i, 2.0 * i)
    	}
    	
		print_array(a1)
		
		delete_doubleArray(a1)
		
		//Example 3
		var x1 = new_doubleArray(10)
	    for( i in 0..10){
    		doubleArray_setitem(x1, i, 2.0 * i)
    	}
    	
		modify_array(x1)
		
		print_array(x1)
		
		delete_doubleArray(x1)
		
		//Example 4
		var x2 = new_doubleArray(10)
		var y2 = new_doubleArray(10)
	    for( i in 0..10){
    		doubleArray_setitem(x2, i, 2.0 * i)
    	}
    	
		copy_array(x2, y2)
		
		print_array(y2)
		
		delete_doubleArray(x2)
		delete_doubleArray(y2)
	    
	}
        
	companion object {
        init {
            System.loadLibrary("cpplib")
        }
    }
}

fun main(args: Array<String>) {
	var result = App().demo();
}