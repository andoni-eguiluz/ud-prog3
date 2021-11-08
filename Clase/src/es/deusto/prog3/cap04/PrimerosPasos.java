package es.deusto.prog3.cap04;

/** Primeros pasos con recursividad - clase de Ing. Inf.
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class PrimerosPasos {

	public static void main(String[] args) {
		// visualizarHasta100( 1 );
		// visualizarParesDesde( 200 );
		// visualizaFactorial1( 11, 0, 1 );
		// visualizaFactorial2( 11 );
		// System.out.println( factorial(11) );
		// System.out.println( fib(48) );
		// 3 varillas 'a','b','c'  'a' origen 'c' destino
		// Búsqueda binaria (ver BusquedaBinaria.java)
		// hanoi( 10, 'a', 'c', 'b' );  
		// Combinatoria:
		// 1: Combinaciones A B de 5 caracteres: AAAAA,AAAAB,AAABA...
		// comb1( 8, "" );
		// 2: Combinaciones A B C de 5 caracteres: AAAAA,AAAAB,AAAAC...
		// comb2( 5, "" );
		// 3: Combinaciones cars cualesquiera de 5 caracteres: AAAAA,AAAAB,AAAAC...
		char[] cars = { 'A', 'B', 'C', 'D' };
		comb3( cars, 5, "" );
	}
	
	private static void comb3( char[] cars, int longi, String combAct ) {
		if (longi==0) {
			System.out.println( combAct );
		} else {
			for (char c : cars) {
				comb3( cars, longi-1, combAct + c );
			}
		}
	}
	
	private static void comb2( int longi, String combAct ) {
		if (longi==0) {
			System.out.println( combAct );
		} else {
			comb2( longi-1, combAct + "A" );
			comb2( longi-1, combAct + "B" );
			comb2( longi-1, combAct + "C" );
		}
	}
	
	// Combinaciones de A B de longitud N =
	//    A concatenado con combinaciones A B long N-1
	//    B concatenado con combinaciones A B long N-1
	private static void comb1( int longi, String combAct ) {
		if (longi==0) {
			System.out.println( combAct );
		} else {
			comb1( longi-1, combAct + "A" );
			comb1( longi-1, combAct + "B" );
		}
	}
	
	
	
	
	private static void hanoi( int n, char origen, char dest, char aux ) {
		if (n==1) {
			System.out.println( "Mover disco 1 de " + origen + " a " + dest );
		} else {
			// Soluc torre n-1 origen a auxi
			hanoi( n-1, origen, aux, dest );
			// Mover disco n de origen a dest
			System.out.println( "Mover disco " + n + " de " + origen + " a " + dest );
			// Soluc torre n-1 auxi a dest
			hanoi( n-1, aux, dest, origen );
		}
	}
	
	public static long fib( int num ) {
		if (num==1) {
			return 1;
		} else if (num==2) {
			return 1;
		} else {
			return fib(num-1) + fib(num-2);
		}
	}
	
	// factorial(n) = n * factorial(n-1)
	//                1 si n == 0
	public static int factorial( int num ) {
		if (num==0) {
			return 1;
		} else {
			return num * factorial(num-1);
		}
	}
	
	/** Visualiza el factorial de un num dado en consola
	 * @param objetivo	número del que calcular su factorial
	 */
	public static void visualizaFactorial2( int objetivo ) {
		// visualizaFactorial2rec( objetivo, 0, 1 );
		visualizaFactorial3rec( objetivo, 1 );
	}
	
	private static void visualizaFactorial3rec( int num, int calc ) {
		if (0==num) {
			System.out.println( calc );
		} else {
			int calcSig = calc * num;
			int numAnt = num - 1;
			visualizaFactorial3rec( numAnt, calcSig );
		}
	}
	
	private static void visualizaFactorial2rec( int objetivo, int num, 
			int factNum ) {
		if (objetivo==num) {
			System.out.println( factNum );
		} else {
			int numSig = num + 1;
			int factSig = factNum * numSig;
			visualizaFactorial2rec( objetivo, numSig, factSig );
		}
	}
	
	private static void visualizaFactorial1( int objetivo, int num, 
			int factNum ) {
		if (objetivo==num) {
			System.out.println( factNum );
		} else {
			int numSig = num + 1;
			int factSig = factNum * numSig;
			visualizaFactorial1( objetivo, numSig, factSig );
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	// Visualiza los pares desde num hasta 2 en consola =
	//    visualizo num
	//    visualizo los pares desde num-2 hasta 2 en consola
	private static void visualizarParesDesde( int num ) {
		if (num==0) {
			// No hace falta System.out.println( num );
		} else {
			System.out.println( num );
			visualizarParesDesde( num-2 );
		}
	}
	
	private static void visualizarHasta100( int num ) {
		if (num<=100) {
			// al derecho
			visualizarHasta100( num+1 );
			// al revés
			System.out.println( num );
		} // else {}
//		while (num<=100) {
//			syso
//			num = num + 1
//		}
	}

}
