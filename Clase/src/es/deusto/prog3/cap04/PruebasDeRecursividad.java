package es.deusto.prog3.cap04;

public class PruebasDeRecursividad {
	
	private static long numLlamadas;
	
	public static void main(String[] args) {
		numLlamadas = 0;
		// int varPrueba = 7;
		// f( varPrueba );
		// conteo( 1 );
		// System.out.println( factorial( 16 ) );
		// System.out.println( fib(45) );
		// System.out.println( numLlamadas );
		// combinacionesAyB( "", 5 );
		// combinaciones( "ABCDEFG", "", 3 );
		// hanoi( 10, 'a', 'c', 'b' );
		int[] vector = { 1, 3, 7, 11, 15, 19, 21, 23, 31, 35, 39, 42, 48, 51 };
		System.out.println( buscarValor( vector, 0, vector.length-1, 21 ) );
	}
	
	// Devuelvo la posición si se encuentra, -1 si no se encuentra
	// ¿Qué es buscarValor recursivo del vector desde inicio hasta fin?
	// - calcular mitad
	//   - comparar elemento en la mitad con el que busco:
	//     - si son iguales, caso base de éxito: devuelvo la posición mitad
	//     - si buscado > elemento de la mitad, devuelvo buscarValor desde mitad+1 hasta fin
	//     - si buscado < elemento de la mitad, devuelvo buscarValor desde inicio hasta mitad-1
	// - caso base de fracaso: inicio > fin  [vector vacío]
	private static int buscarValor( int[] vector, int inicio, int fin, int buscado ) {
		System.out.println( "Buscando valor " + buscado + " entre " + inicio + " y " + fin );
		if (inicio > fin) {
			return -1;
		} else {
			int mitad = (inicio + fin) / 2;
			if (buscado == vector[mitad]) {
				return mitad;
			} else if (buscado > vector[mitad]) {
				return buscarValor( vector, mitad+1, fin, buscado );
			} else {
				return buscarValor( vector, inicio, mitad-1, buscado );
			}
		}
	}
	
	
	
	private static void hanoi( int numDiscos, char origen, char destino, char auxiliar ) {
		if (numDiscos==1) {
			System.out.println( "Mover disco 1 de " + origen + " a " + destino );
		} else {
			hanoi( numDiscos-1, origen, auxiliar, destino );
			System.out.println( "Mover disco " + numDiscos + " de " + origen + " a " + destino );
			hanoi( numDiscos-1, auxiliar, destino, origen );
		}
	}
	
	
	private static void combinaciones( String opciones, String combEnCurso, int longCombAConseguir ) {
		if (longCombAConseguir==0) {
			System.out.println( combEnCurso );
		} else {
			for (int i=0; i<opciones.length(); i++) {
				combinaciones( opciones, combEnCurso + opciones.charAt(i),
						longCombAConseguir-1 );
			}
		}
	}
	
	// Generar combinaciones de 5 letras de A y B:
	// Caso recursivo:
	//   Empezando en A concatenarla a la generación de combinaciones de 4 letras de A y B
	//   Empezando en B    "  "  " " " " " " 
	// Caso base: si longCombAConseguir es 0 --> sacar a consola la combinación actual
	private static void combinacionesAyB( String combEnCurso, int longCombAConseguir ) {
		if (longCombAConseguir==1) {
			System.out.println( combEnCurso + "A" );
			System.out.println( combEnCurso + "B" );
		} else {
			combinacionesAyB( combEnCurso + "A", longCombAConseguir-1 );
			combinacionesAyB( combEnCurso + "B", longCombAConseguir-1 );
		}
	}
	
	// 1,1,2,3,5,8,13,21...
	// si n > 1, fib (n) = fib(n-1) + fib(n-2)
	// si n = 1, fib(1) = 1
	public static long fib( int n ) {
		numLlamadas++;
		if (n==1) {
			return 1;
		} else if (n==2) {
			return 1;
		} else {
			return fib(n-1) + fib(n-2);
		}
	}
	
	
	
	// n! = 1 * 2 * 3 * 4 * ... * (n-2) * (n-1) * n
	// si n > 1 --> n! = n * (n-1)!     [factorial(n) returns n * factorial(n-1)]
	//    n == 0 --> n! = 1
	/** Calcula el factorial de un número
	 * @param n	Número del que calcular el factorial, debe ser mayor o igual a cero
	 * @return	Factorial matemático de n
	 */
	public static long factorial( int n ) {
		if (n==0) {  // Caso base
			return 1;
		} else {  // Caso recursivo
			return n * factorial( n-1 );
		}
	} 

	
	
	
	// Contar (sacar a consola) de i a 4000:
	//    - Caso recursivo:
	//      - sacar a consola i
	//      - Contar de i+1 a 4000
	//    - Caso base: si i==4000
	//      - sacar a consola 4000
	private static void conteo( int i ) {
		if (i==5) {
			System.out.println( i );
		} else {
			System.out.println( i );
			conteo( i+1 );
			System.out.println( i );
		}
	}
	
	@SuppressWarnings("unused")
	private static void f( long param ) {
		param = param + numLlamadas;
		numLlamadas++;
		System.out.println( "soy f en la llamada " + numLlamadas + " cálculo " + param );
		f( param );
	}
}
