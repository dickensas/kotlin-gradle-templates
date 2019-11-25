package plot

import lapack.*
import kotlin.math.*
import kotlinx.cinterop.*
import platform.posix.*

fun print_matrix_rowmajor( desc:String, m:Int, n:Int, mat:CArrayPointer<DoubleVar>, ldm:Int ) {
   println( desc )
  
   for( i  in 0 until m ) {
      for( j in 0 until n ) print( " " + mat[i*ldm+j].toInt() )
      println()
   }
}
 
fun print_matrix_colmajor( desc:String, m:Int, n:Int, mat:CArrayPointer<DoubleVar>, ldm:Int ) {
   println( desc )
  
   for( i  in 0 until m ) {
      for( j in 0 until n ) print( " " + mat[i+j*ldm].toInt() )
      println()
   }
}

fun main() = memScoped {

    var A = arrayOf(1,2,3,4,5,1,3,5,2,4,1,4,2,5,3)
    var B = arrayOf(-10,12,14,16,18,-3,14,12,16,16)
    
    var a = allocArray<DoubleVar>(A.size)
    var b = allocArray<DoubleVar>(B.size)
    
    for (i in 0 until A.size) 
        a[i] = A[i].toDouble()
    for (i in 0 until B.size) 
        b[i] = B[i].toDouble()
    
    var m = 5
    var n = 3
    var nrhs = 2
    var lda = 5
    var ldb = 5
    
    print_matrix_colmajor( "Entry Matrix A", m, n, a, lda )
    print_matrix_colmajor( "Right Hand Side b", n, nrhs, b, ldb )
    println( "\nLAPACKE_dgels (col-major, high-level) Example Program Results" );
    LAPACKE_dgels(LAPACK_COL_MAJOR,'N'.toInt().toByte(),m,n,nrhs,a,lda,b,ldb)
    println()
    print_matrix_colmajor( "Solution", n, nrhs, b, ldb );
    
}