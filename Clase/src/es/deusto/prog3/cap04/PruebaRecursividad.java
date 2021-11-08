package es.deusto.prog3.cap04;

import java.util.Arrays;

/** Clase de pruebas de recursividad del capítulo 4
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class PruebaRecursividad {

	public static void main(String[] args) { 
		// fSubir(1);
		// fBajar(3000);
		// System.out.println( factorial( 7 ));
		// prod en función de suma m * n = m + m + m ... + m   (n veces)
		// System.out.println( producto( 25, 7 ) );
		// Recorrer un string  visualizando caracter a caracter al derecho y al revés
		// recorrerString( "Hola voy a salir al derecho y al revés" );
		// System.out.println( stringAlReves( "Hola al revés" ) );
		// System.out.println( fib(27) );  // Observa que si pones un número 40-50 empieza a notarse el largo tiempo de cálculo... ¿por qué?
	
		// Búsqueda binaria
		// int[] array = new int[] { 1, 3, 4, 5, 8, 9, 12, 18, 19, 21 };
		// // int[] array = new int[] { 1, 1, 2, 2, 2, 2, 2, 3, 4, 5 };  // ¿Y qué pasa si hay repetidos?
		// int buscado = 23;
		// int donde = buscaEnVector( buscado, array, 0, array.length-1 );
		// System.out.println( "Encontrado " + buscado + " en posición " + donde + " en array " + Arrays.toString(array) );
		
		// hanoi( 10, 'A', 'C', 'B' );
		
		sacaCadenasAB( 5 );
		sacaCadenasABC( 5, "" );
		sacaCadenas( new char[] { 'F', 'J', 'K', 'M' }, 5, "" );
	}

	private static void sacaCadenas( char[] letras, int n, String previa ) {
		// if (condicion de poda) return - aquí se podría hacer cualquier poda del recorrido exponencial
		if (n==0) {
			System.out.println( previa );
		} else {
			for (char c : letras) {
				sacaCadenas( letras, n-1, previa+c );
			}
		}
	}
	
	private static void sacaCadenasABC( int n, String previa ) {
		if (n==0) {
			System.out.println( previa );
		} else {
			sacaCadenasABC( n-1, previa+"A" );
			sacaCadenasABC( n-1, previa+"B" );
			sacaCadenasABC( n-1, previa+"C" );
		}
	}
	
	private static void sacaCadenasAB( int n ) {
		sacaCadenasAB( n, "" );
	}
	
	// Saca todas las cadenas de A y B de longitud N
	//   Añadir a la cadena previa A y sacar todas las cadenas de long N-1
	//   Añadir a la cadena previa B y sacar todas las cadenas de long N-1
	// Caso base: si N==0 se saca la cadena previa a consola
	private static void sacaCadenasAB( int n, String previa ) {
		if (n==0) {
			System.out.println( previa );
		} else {
			sacaCadenasAB( n-1, previa+"A" );
			sacaCadenasAB( n-1, previa+"B" );
		}
	}
	
	
	// Resolver torre de hanoi de tamaño N de varilla o -> d, auxiliar a
	//  - Si N == 1, mover disco 1 o -> d
	//  - Si no
	//    Resolver torre N-1 varilla o -> a, auxiliar d
	//    mover disco N o -> d
	//    Resolver torre N-1 varilla a -> d, auxiliar o
	// N NO PUEDE SER MENOR QUE 1
	private static void hanoi( int n, char origen, char destino, char auxiliar ) {
		if (n==1) {
			System.out.println( "Mover 1 de " + origen + " a " + destino );
		} else {
			hanoi( n-1, origen, auxiliar, destino );
			System.out.println( "Mover " + n + " de " + origen + " a " + destino );
			hanoi( n-1, auxiliar, destino, origen );
		}
	}
	
		private static int buscaEnVector( int valor, int[] array, int ini, int fin  ) {
			System.out.println( "llamada desde " + ini + " a " + fin );
			if (ini>fin) return -1;  // Caso base: no encontrado
			int mitad = (ini + fin) / 2;
			if (array[mitad]==valor) { // Caso base: encontrado
				return mitad;
			} else {
				if (array[mitad]>valor) {  // A la izquierda!  (el que hay en medio es > que el buscado)
					return buscaEnVector( valor, array, ini, mitad-1 );
				} else {  // A la derecha! (el que hay en medio es < que el buscado)
					return buscaEnVector( valor, array, mitad+1, fin );
				}
			}
		}

	//            1  2  3  4  5  6  7   8   9
	// Fibonacci: 1, 1, 2, 3, 5, 8, 13, 21, 34 ...
	// fib(n) = fib(n-1) + fib(n-2)
	//        = 1 si n==1
	public static long fib(long n) {
		if (n==1) {
			return 1;
		} else if (n==2) {
			return 1;
		} else {
			return fib(n-1) + fib(n-2);
		}
	}
	
	// Devolver al revés el string "Hola":
	//   - devolver al revés el string "ola" + "H"
	//   - string vacío - devolver ""
	// Devolver al revés el string s
	//   - concatenar el string s menos el primer carácter AL REVÉS + primer car de s
	//   - string vació - devolver ""
	public static String stringAlReves( String string ) {
		if (string.isEmpty()) {
			return "";
		} else {
			return stringAlReves(string.substring(1)) + string.charAt(0);
		}
	}
	
	
	/** Visualiza un string primero al derecho y luego al revés (carácter a carácter)
	 * @param s
	 */
	public static void recorrerString( String s ) {
		recorrerString( s, 0 );
	}
	
	// recString = si indice = long-1 nada que hacer
	//           = si no, 
	private static void recorrerString( String s, int indice ) {
		if (indice==s.length()) {
			System.out.println();
			return;
		} else {
			char c = s.charAt( indice );
			System.out.print( c );
			recorrerString( s, indice+1 );
			System.out.print( c );
		}
	}
	
	// Calcula el producto de dos números de la siguiente forma:
	// sumando * num = sumando + (sumando * (num-1))
	// sumando * 0 = 0
	private static int producto( int sumando, int num ) {
		if (num==0) {  // Caso base
			System.out.println( "fin de llamadas");
			return 0;
		} else {  // Caso recursivo
			System.out.println( "ahora sumo " + sumando + " y " + (num-1) );
			int ret = sumando + producto( sumando, num-1 );
			System.out.println( "ahora retorno " + ret );
			return ret;
		}
	}
	
	// Calcula el factorial:
	// factorial(n) = n * factorial(n-1)
	// factorial(0) = 1
	private static int factorial( int n ) {
		if (n==0) {
			return 1;
		} else {
			return n * factorial( n-1 );
		}
	}

	// Método que visualiza un número y todos los siguientes de 1 en 1 hasta 3000 (inclusive)
	// OJO: N NO PUEDE SER MAYOR QUE 3000!!!!!
	private static void fSubir(int n) {
		if (n==3001) {  // Caso base: NO HAY QUE HACER RECURSIVIDAD
			return;
		} else { // Caso recursivo: HAY QUE SEGUIR HACIENDO RECURSIVIDAD
			System.out.println( "mi número es: " + n );
			fSubir( n+1 );
			// Hola?
			System.out.println( "bajando es: " + n );
		}
	}
	
//	private static void fBajar(int n) {
//		if (n>=1) {
//			System.out.println( "mi número es: " + n );
//			fBajar( n-1 );
//		} else {
//			return;  // Caso base
//		}
//	}
//	
}
