package es.deusto.prog3.cap04;

// Basado en el algoritmo de Heron de Alejandría:
// Para calcular raíz cuadrada de x
// - Haz un intento: G
// - Mejora el intento calculando la media de G y X/G
// - Sigue mejorando el intento hasta que sea suficientemente bueno
//
// Redefinición recursiva:
// La raíz cuadrada de x es la raíz cuadrada de x con el intento g  (1 vez)
// La raíz cuadrada de x con el intento g es:  (recursivo)
// - el intento si está suficientemente cerca (g*g casi== x) 
// - la raíz cuadrada de x con el intento (g + x/g)/2

public class RaizCuadrada {

	public static double raizCuadradaDe( double x ) {
		numLlamadas = 0; // Para información interna
		return raizCuadradaDe( x, 1 );
	}
	private static int numLlamadas; // Para información interna
	public static double raizCuadradaDe( double x, double intento ) {
		numLlamadas++; // Para información interna
		System.out.println( "   Llamada " + numLlamadas + " - intento " + x ); // Para información interna
		System.out.println( "       Dif = " + Math.abs(intento*intento - x) );
		if ( Math.abs(intento*intento - x) < 0.000000000001 )  // Caso base: el intento es suficientemente bueno
			return intento;
		else {
			return raizCuadradaDe( x, (intento + x/intento)/2 );  // (1)
		}
	}
	
	public static void main(String[] args) {
		System.out.println( raizCuadradaDe( 2510 ) );
		System.out.println( raizCuadradaDe( 1600 ) );
		System.out.println( raizCuadradaDe( 1000000 ) );
		System.out.println( raizCuadradaDe( 29330957451.24895 ));  // Ojo: por qué el error?   Precisión de double
		// System.out.println( raizCuadradaDe_v2( 29330957451.24895 ) );  // Solución a la aproximación recursiva de doubles
	}

	
	// Soluciona el error cambiar (1) por (2):
	public static double raizCuadradaDe_v2( double x ) {
		numLlamadas = 0; // Para información interna
		return raizCuadradaDe_v2( x, 1 );
	}
	public static double raizCuadradaDe_v2( double x, double intento ) {
		numLlamadas++; // Para información interna
		System.out.println( "   Llamada " + numLlamadas + " - intento " + intento ); // Para información interna
		if ( Math.abs(intento*intento - x) < 0.000000000001 )  // Caso base: el intento es suficientemente bueno
			return intento;
		else {
			// (2)
			double nuevoIntento = (intento + x/intento)/2;
			if (nuevoIntento==intento) return intento;  // Si no hay progresión es que hemos llegado a la precisión máxima del double
			return raizCuadradaDe_v2( x, nuevoIntento );
		}
	}
	
	// (3) 
	// Otra solución es limitar "a mano" la profundidad de llamada, por ejemplo si numLlamadas == 100 caso base, devolver lo que haya
	
}
