package es.deusto.prog3.cap04;

public class PruebasDeRecursividad {
	
	private static int numLlamadas;
	
	public static void main(String[] args) {
		numLlamadas = 0;
		int varPrueba = 7;
		// f( varPrueba );
		conteo( 1 );
	}
	
	// Contar (sacar a consola) de i a 4000:
	//    - Caso recursivo:
	//      - sacar a consola i
	//      - Contar de i+1 a 4000
	//    - Caso base: si i==4000
	//      - sacar a consola 4000
	private static void conteo( int i ) {
		if (i==4000) {
			System.out.println( 4000 );
		} else {
			conteo( i+1 );
			System.out.println( i );
		}
	}
	
	private static void f( int param ) {
		param = param + numLlamadas;
		numLlamadas++;
		System.out.println( "soy f en la llamada " + numLlamadas + " cálculo " + param );
		f( param );
	}
}
