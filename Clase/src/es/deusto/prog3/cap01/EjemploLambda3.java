package es.deusto.prog3.cap01;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** Ejemplo de programación funcional con Java 8
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploLambda3 {

	public static void main(String[] args) {
		// Ejemplo 1
		// Expresiones puramente funcionales (variables y parámetros "función")
		Consumer<String> f = aplica( EjemploLambda3::visuEntero, EjemploLambda3::stringAInt );
			// (Nota) Observa que las expresiones lambda que devuelven un valor se pueden hacer con return...
			// Consumer<String> f = aplica( EjemploLambda3::visuEntero, 
			// 		(s) -> {
			// 				return stringAInt( s );
			// 		} );
			// O también si son expresiones simples, sin return
			// Consumer<String> f = aplica( EjemploLambda3::visuEntero, 
			// 		(s) -> stringAInt( s ) );
		f.accept("5");
		f.accept("Hola");
		// O bien 
		consumeStrings( f, "5", "Hola" );
		
		// Ejemplo 2: Suppliers
		aplica( EjemploLambda3::damePrimo, EjemploLambda3::visuEntero, 20 );
	}
	
	/** Devuelve una función consumidora de un string, aplicando primero
	 * una función transformadora de string a int y luego una consumidora de int
	 * @param procesadora	Función que procesa un int
	 * @param transformadora	Función que transforma un String en un int 
	 * @return	Nueva función consumidora de string
	 */
	public static Consumer<String> aplica( Consumer<Integer> procesadora, Function<String,Integer> transformadora ) {
		return (String s) -> { procesadora.accept(transformadora.apply(s)); };
	}
	
	public static void consumeStrings( Consumer<String> f, String...strings ) {
		for (String s : strings) f.accept(s);
	}

	private static void visuEntero( int i ) {
		System.out.println( i );
	}
	
	private static int stringAInt( String s ) {
		try {
			return Integer.parseInt( s );
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
		private static int ultimoPrimo = -1;  // Para que el siguiente sea +1 (se va solo por impares)
	// Suppliers
	private static int damePrimo() {
		boolean tieneDivisores;
		do {
			tieneDivisores = false;
			ultimoPrimo += 2;  // Siguiente impar
			for (int divisor=3;divisor<=Math.sqrt(ultimoPrimo);divisor+=2) {
				if (ultimoPrimo % divisor == 0) { tieneDivisores = true; break; }
			}
		} while (tieneDivisores);
		return ultimoPrimo;
	}

	/** Genera y consume una serie de ints
	 * @param supp	Función que suministra un int
	 * @param cons	Función que consume un int 
	 * @param numVals	Número de ints a procesar (obtener de supp y luego consumir con cons)
	 */
	public static void aplica( Supplier<Integer> supp, Consumer<Integer> cons, int numVals ) {
		for (int i=0; i<numVals; i++)
			cons.accept( supp.get() );
	}
	
	
}
