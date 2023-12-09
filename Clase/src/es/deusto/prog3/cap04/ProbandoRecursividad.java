package es.deusto.prog3.cap04;

public class ProbandoRecursividad {
	private static int llamadas = 0;
	public static void main(String[] args) {
		// prueba1(0);
		// prueba2(0);
		// prueba3(0);
		// visualizaDesde0HastaN( 0, 100 );
		// System.out.println( factorial( 21 ) );
		// System.out.println( fib(51) );
		combinaABC();
		combinaABClongNRec( 2, "" );
		System.out.println( "\nCombinaciones de 3 letras");
		combinaABClongNRec( 5, "" );
		// Combinar cualquier longitud cualquier set de caracteres
		combinaRec( new char[] { 'A', 'E', 'I', 'O', 'U' }, 8, "" );
		System.out.println( "Nº llamadas: " + llamadas );
	}

	private static void combinaRec( char[] cars, int longitud, String concat ) {
		llamadas++;
		if (longitud==0) {
			System.out.println( concat );
		} else {
			for (char letra : cars) {
				combinaRec( cars, longitud-1, concat + letra );
			}
		}
	}
	
	
	
	// Generar combinaciones de A,B,C de longitud n:
	//  - A con las combinaciones generadas de A,B,C de longitud n-1
	//  - B con " " " 
	//  - C con " " " 
	//  - Nada (ya lo tengo -> a consola) si longitud = 0
	private static void combinaABClongNRec( int longitud, String concat ) {
		if (longitud==0) {
			System.out.println( concat );
		} else {
			combinaABClongNRec(longitud-1, concat + "A" );
			combinaABClongNRec(longitud-1, concat + "B" );
			combinaABClongNRec(longitud-1, concat + "C" );
		}
	}
	
	// A, B, C longitud 2
	private static void combinaABC() {
		char[] letras = { 'A', 'B', 'C' };
		for (char l1 : letras) {
			for (char l2 : letras) {
				for (char l3 : letras) {
					System.out.println( "" + l1 + l2 + l3 );
				}
			}
		}
	}
	
	// Fib(n) = fib(n-1)+fib(n-2)
	//          1 si n==0
	//          1 si n==1  (porque progresa de 2 maneras -1 y -2)
	private static long fib( int n ) {
		// System.out.println( "Entrada " + n );
		if (n==0) {
			return 1;
		} else if (n==1) {
			return 1;
		} else {
			return fib(n-1) + fib(n-2);
		}
	}
	
	/** Devuelve el factorial de un número POSITIVO o 0. No funciona si el número es negativo.
	 * @param n
	 * @return
	 */
	private static long factorial( int n ) {
		if (n==0) {
			return 1;
		} else {
			return n * factorial( n-1 );
		}
	}
	
	private static void visualizaDesde0HastaN( int i, int hasta ) {
		if (i<=hasta) {
			System.out.println( i );
			visualizaDesde0HastaN( i+1, hasta );
		}
	}
	
	// cont = 0;
	// while (cont <= 100) {
	//     syso cont
	//     cont = cont + 1;
	// }
	
	private static void prueba3(int cont) {
		// cont = 0;  // NO - inicialización fuera de la recursividad
		if (cont<=100) {
			System.out.println( cont );
			prueba3( cont+1 );
		}
	}
	
	private static void prueba2(int cont) {
		System.out.println( cont );
		if (cont<100) {
			prueba2( cont+1 );
		}
	}
	
	private static void prueba1(int cont) {
		if (cont>100) {
			return;
		}
		System.out.println( cont ); // Al derecho
		prueba1( cont+1 );
		// System.out.println( "Retorno " + cont );
		System.out.println( cont );  // Al revés
	}
}
