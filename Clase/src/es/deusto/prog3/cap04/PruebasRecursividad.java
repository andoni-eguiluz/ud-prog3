package es.deusto.prog3.cap04;

import java.util.ArrayList;
import java.util.Arrays;

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
		// System.out.println( "Dobleces para luna:" );
		// doblecesLuna( 0.0001, 384400000.0, 0 );
		// Recursividad múltiple:
		// System.out.println( fib(11) );
		// System.out.println( "Número de sumas:" + contF );
		// System.out.println( "Número de llamadas:" + llF );
		// Divide y vencerás:
		// int[] v = { 1, 2, 3, 7, 9, 11, 12, 14, 17, 18, 21 };
		// System.out.println( busquedaBinaria( 19, v ) );
		// hanoi( 4, 'a', 'b', 'c' );
		char[] letras = { 'a', 'b', 'c' };
		combinaLetras( letras );
		combinaLetrasRec( letras, 7, "" );
		ArrayList<String> combs = combinaYDevLetrasRec( letras, 7, "" );
		System.out.println( combs.size() );
		System.out.println( combs );
		ArrayList<String> combs2 = new ArrayList<String>();
		combinaYAgrupaLetrasRec( letras, 7, "", combs2 );
		System.out.println( combs2 );
		System.out.println( combs2.size() );
		combinaLetrasRecConFiltro( letras, 7, "" );
	}

	private static void combinaLetrasRecConFiltro( char[] letras, int tamanyo, String combBase ) {
		if (tamanyo==0) {
			System.out.println( combBase );
		} else {
			for (char c : letras) {
				if (cumpleRequisitos(combBase+c)) {
					combinaLetrasRecConFiltro( letras, tamanyo-1, combBase + c );
				}  // else poda
			}
		}
	}
	
	private static boolean cumpleRequisitos( String comb ) {
		int numAes = 0;
		for (int i=0; i<comb.length(); i++) {
			if (comb.charAt(i)=='a') {
				numAes++;
			}
		}
		return (numAes<=2);
	}
	
	private static void combinaYAgrupaLetrasRec( char[] letras, int tamanyo, String combBase, ArrayList<String> combs ) {
		if (tamanyo==0) {
			// System.out.println( combBase );
			// combs = new ArrayList<String>();
			combs.add( combBase );
		} else {
			for (char c : letras) {
				combinaYAgrupaLetrasRec( letras, tamanyo-1, combBase + c, combs );
			}
		}
	}
	
	
	private static ArrayList<String> combinaYDevLetrasRec( char[] letras, int tamanyo, String combBase ) {
		if (tamanyo==0) {
			ArrayList<String> ret = new ArrayList<String>();
			ret.add( combBase );
			// ArrayList<String> ret = new ArrayList<String>( Arrays.asList( new String[] { combBase } ) );
			// System.out.println( combBase );
			return ret;
		} else {
			ArrayList<String> ret = new ArrayList<String>();
			for (char c : letras) {
				ret.addAll( combinaYDevLetrasRec( letras, tamanyo-1, combBase + c ) );
			}
			return ret;
		}
	}
	

	// Versión recursiva mucho más generalizable
	// Combinaciones recursivas de letras
	// Por cada una de mis letras:
	//   Cojo esa letra y la combino recursivamente con el resto de tamaño-1
	// Si el tamaño es 0, visualizo la combinación
	private static void combinaLetrasRec( char[] letras, int tamanyo, String combBase ) {
		if (tamanyo==0) {
			System.out.println( combBase );
		} else {
			for (char c : letras) {
				combinaLetrasRec( letras, tamanyo-1, combBase + c );
			}
		}
	}

	// Versión iterativa... ¿cómo hacerla con más letras? ¿cómo varío el número de "fors"?
	private static void combinaLetras( char[] letras ) {
		for (char c : letras) {
			for (char c2 : letras) {
				for (char c3 : letras) {
					System.out.println( "" + c + c2 + c3 );
				}
			}
		}
	}
	
	// Resolver una torre de hanoi de tamaño n con las varillas or, aux, dest:
	// - Si el tamaño es 1 -> caso base -> mover el disco 1 de la or a la dest
	// - Si no:
	//   - Resolver recursivamente la torre n-1 con las varillas or, dest, aux
	//   - Mover el disco n de la or a la dest
	//   - Resolver recursivamente la torre n-1 con las varillas aux, or, dest
	private static void hanoi( int tam, char origen, char aux, char destino ) {
		if (tam==1) {
			System.out.println( "Mueve disco 1 de " + origen + " a " + destino );
		} else {
			hanoi( tam-1, origen, destino, aux );
			System.out.println( "Mueve disco " + tam + " de " + origen + " a " + destino );
			hanoi( tam-1, aux, origen, destino );
		}
	}
	
	
	/** Busca un elemento en un vector
	 * @param valor	Elemento a buscar
	 * @param vector	Vector en el que buscar
	 * @return	Posición en la que se encuentra
	 */
	public static int busquedaBinaria( int valor, int[] vector ) {
		return busquedaBinaria( valor, vector, 0, vector.length-1 );
	}

	// Buscar un elemento valor en un vector de tamaño n:
	//  - ¿Es el vector vacío? -> caso base -> devolver -1
	//  - Coger el elemento de la mitad (n/2)
	//    - ¿Es == al valor? --> caso base --> lo tenemos --> devolvemos su posición
	//    - ¿Es < al valor? --> caso recursivo -> buscar binaria en la mitad de la derecha
	//    - ¿Es > al valor? --> caso recursivo -> buscar binaria en la mitad de la izquierda
	private static int busquedaBinaria( int valor, int[] vector, int ini, int fin ) {
		System.out.println( "Buscando en " + ini + "," + fin );
		if (ini>fin)  { // Caso base no éxito
			return -1;
		}
		int mitad = (ini + fin) / 2;
		if (vector[mitad]==valor) {
			return mitad;  // Caso base
		} else if (vector[mitad] < valor) {  // Caso recursivo hacia la derecha
			return busquedaBinaria( valor, vector, mitad+1, fin );
		} else {  // Rec por la izquierda
			return busquedaBinaria( valor, vector, ini, mitad-1 );
		}
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
