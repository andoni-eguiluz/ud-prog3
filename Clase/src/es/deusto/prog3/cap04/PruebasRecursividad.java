package es.deusto.prog3.cap04;

public class PruebasRecursividad {
	public static void main(String[] args) {
		// Recursividad lineal:
		// f();
		// conteo = 1;
		// contarHasta100();
		// contar(1,50);
		// producto en función de suma: M*N = M+M+M+...+M (n veces) = M+ (M*(N-1))
		// System.out.println( productoConSumas(5, 4));
		// System.out.println( factorial(15) );
		// System.out.println( factorial2(15,1) );
		// System.out.println( factorial(-15) );
		// System.out.println( stringSinVocales( "Hola soy un string con vocales" ) );
		// System.out.println( stringSinVocales2Rec( "Hola soy un string con vocales", 0 ) );
		// System.out.println( invertirString( "Hola soy un string al derecho" ) );
		// doblecesLuna( 0.0001, 384400000.0, 0 );
		// Recursividad múltiple:
		// System.out.println( fib(11) );
		// System.out.println( "Número de sumas:" + contF );
		// System.out.println( "Número de llamadas:" + llF );
		// System.out.println( "Dobleces para luna:" );
		// Divide y vencerás:
		int[] v = { 1, 2, 3, 7, 9, 11, 12, 14, 17, 18, 21 };
		System.out.println( busquedaBinaria( 7, v ) );
	}
	
	private static int busquedaBinaria( int valor, int[] vector ) {
		// TODO 
		return -1;
	}
	
	private static void doblecesLuna( double grosor, double distancia, int dobleces ) {
		if (grosor >= distancia) {
			System.out.println( "He llegado en " + dobleces + " dobleces" );
		} else {
			doblecesLuna( grosor*2, distancia, dobleces+1 );
		}
	}

	private static int contF = 0;
	private static int llF = 0;
	
	private static long fib( int n ) {
		llF++;
		if (n==1) {
			return 1;
		} else if (n==2) {
			return 1;
		} else {
			contF++;
			return fib(n-1) + fib(n-2);
		}
	}
	
	
	public static String invertirString( String s ) {
		if (s.length()==0) {
			return "";  // SOLO UNA VEZ
		} else {
			// ANTES DE REC "n" veces
			char primerCar = s.charAt(0);
			String resto = s.substring(1);
			String ret = invertirString(resto) 
					+ primerCar;  // DESPUES DE REC "n" veces
			System.out.println( "  " + ret );
			return ret;
		}
	}
	
	// SHELL
	/** Quita las vocales de un string
	 * @param s	String original
	 * @return	String sin vocales
	 */
	public static String stringSinVocales2( String s ) {
		return stringSinVocales2Rec( s, 0 );
	}
	
	private static String stringSinVocales2Rec( String s, int primero ) {
		if (primero>=s.length()) {
			return "";
		} else {
			String primerCar = s.charAt(primero) + "";
			if ("aeiouáéíóúAEIOUÁÉÍÓÚÜü".contains(primerCar)) {
				return stringSinVocales2Rec( s, primero+1 );
			} else {
				return primerCar + stringSinVocales2Rec( s, primero+1 );			
			}
		}
	}
	
	private static String stringSinVocales( String s ) {
		if (s.length()==0) {
			return "";
		} else {
			String primerCar = s.charAt(0) + "";
			String resto = s.substring(1);
			if ("aeiouáéíóúAEIOUÁÉÍÓÚÜü".contains(primerCar)) {
				return stringSinVocales( resto );
			} else {
				return primerCar + stringSinVocales( resto );			
			}
		}
	}
	
	private static long factorial2( int n, long resul ) {
		if (n==0) {
			return resul;
		} else {
			return factorial2( n-1, resul*n );
		}
	}
	
	/** Devuelve el factorial de un número
	 * @param n	Número entero. DEBE SER 0 O POSITIVO
	 * @return	factorial de ese número
	 * @throws NumberFormatException	Si el número no es positivo o cero
	 */
	private static long factorial(int n) throws NumberFormatException {
		if (n<0) {
			throw new NumberFormatException("Factorial negativo");
		}
		if (n==0) {
			return 1;
		} else {
			return n * factorial(n-1);
		}
	}
	
	private static int productoConSumas(int m, int n) {
		if (n==0) {
			return 0;
		} else {
			return m + productoConSumas(m, n-1);
		}
	}
	
	private static void contar(int desde, int hasta) {
		if (desde==hasta) { // Caso base
			System.out.println( hasta );
		} else {  // Caso recursivo
			System.out.println( "LL " + desde );
			contar( desde+1, hasta );
			System.out.println( "RET " +desde );
		}
	}
	
	private static int conteo;
	private static void contarHasta100() {
		if (conteo==101) {  // Caso base
			// Nada
		} else {
			System.out.println( conteo );
			conteo++;
			contarHasta100();
		}
	}
	
	private static int cont = 0;
	public static void f() {
		cont++;
		System.out.println( cont );
		f();
	}
}
