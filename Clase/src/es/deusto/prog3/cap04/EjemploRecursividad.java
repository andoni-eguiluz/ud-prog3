package es.deusto.prog3.cap04;

/** Primeros pasos con recursividad - clase de II+TDE y II+IEIA
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploRecursividad {

	public static void main(String[] args) {
		// visuHastaMil( 1 );
		// visuParesDescHasta2( 1000 );
		// factorial(14, 0, 1);
		// factorial2(14, 1);
		// System.out.println( factorial3(14) );
		// System.out.println( fib(46) );
		// Búsqueda binaria (ver BusquedaBinaria.java)
		// hanoi( 4, 'a', 'c', 'b' );
		// comb1( 5, "" ); // Comb de 3 caracteres A o B
		// comb2( 5, "" ); // Comb de 3 caracteres A o B o C
		comb3( 5, new char[] { 'a', 'b', 'c', 'd' }, "" );
		System.out.println( cont );
	}

	private static void comb3( int n, char[] cars, String combinacion ) {
		cont++;
		if (n==0) {
			System.out.println( combinacion );
		} else {
			for (char c : cars) {
				comb3( n-1, cars, combinacion + c );
			}
		}
	}
	
	private static void comb2( int n, String combinacion ) {
		cont++;
		if (n==0) {
			System.out.println( combinacion );
		} else {
			comb2( n-1, combinacion + "A" );
			comb2( n-1, combinacion + "B" );
			comb2( n-1, combinacion + "C" );
		}
	}
	
	
	private static int cont=0;
	// comb1 N caracteres A y B =
	//    cadena A + todas las cadenas de N-1 caracteres de A y B
	//    cadena B + todas las ...        N-1
	// Caso base: N=0 ya tengo las cadenas finales
	private static void comb1( int n, String combinacion ) {
		cont++;
		if (n==0) {
			System.out.println( "  " + combinacion );
		} else {
			System.out.println( combinacion );
			comb1( n-1, combinacion + "A" );
			comb1( n-1, combinacion + "B" );
		}
	}
	
	
	
	
	// Resolver torre de hanoi de tamaño N de varilla o -> d, auxiliar a
	// - Resolver torre N-1 o -> a, aux d
	// - Mover disco N o -> d
	// - Resolver torre N-1 a -> d, aux o
	private static void hanoi( int n, char origen, char destino, char auxiliar ) {
		if (n==1) {
			System.out.println( "Mover 1 de " + origen + " a " + destino );
		} else {
			hanoi( n-1, origen, auxiliar, destino );
			System.out.println( "Mover " + n + " de " + origen + " a " + destino );
			hanoi( n-1, auxiliar, destino, origen );
		}
	}
	
		
	/** Calcula y devuelve el número fibonacci correspondiente
	 * fib(n) = fib(n-1) + fib(n-2), siendo fib(1)=1 y fib(2)=1
	 * @param n	Valor de fibonacci. Debe ser n>=1
	 * @return
	 */
	public static long fib( int n ) {
		if (n==1) {
			return 1;
		} else if (n==2) {
			return 1;
		} else {
			return fib(n-1) + fib(n-2);
		}
	}
	
	public static void envoltoriofactorial( int n ) {
		factorial( n, 0, 1 );
	}
	private static void factorial(int objetivo, int num, int valor) {
		if (num==objetivo) {
			System.out.println( valor );
		} else {
			int siguiente = (num+1) * valor; // f(n) = f(n-1) * n
			factorial( objetivo, num+1, siguiente );
		}
	}
	
	private static void factorial2(int objetivo, int valor) {
		if (objetivo==0) {
			System.out.println( valor );
		} else {
			int siguiente = objetivo * valor; // valor f(n) --> f(n-1)
			factorial2( objetivo-1, siguiente );
		}
	}
	
	// f(n) = n * f(n-1)  si n>=1   y f(n) = 1 si n==0
	// NO SE DEBE LLAMAR CON < 0 
	private static int factorial3( int n ) {
		if (n==0) {
			return 1;
		} else {
			return n * factorial3( n-1 );
		}
	}
	
	
	
	// Método que visualiza de num hasta 1000
	private static void visuHastaMil( int num ) {
		// Sentencias previas a la llamada
		if (num<1000) {
			visuHastaMil( num + 1 );
		}
		// Sentencias posteriores a la llamada
		System.out.println( num );
	}
	
	private static void visuParesDescHasta2( int num ) {
		if (num>2) {
			// Ocurre n veces al derecho
			System.out.println( num );
			visuParesDescHasta2( num - 2 );
			// Ocurre n veces al revés
		} else {
			// Ocurre 1 vez
			System.out.println( 2 );
		}
	}
	
}
