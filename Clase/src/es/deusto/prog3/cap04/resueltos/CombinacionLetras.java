package es.deusto.prog3.cap04.resueltos;

/** Ejercicio: cálculo combinatorio de letras
 */
public class CombinacionLetras {

	public static void main(String[] args) {
		char[] letras = { 'A', 'F', 'G', 'L' };
		// combinatoriaDe3Iterativa( letras );
		combinatoria( letras, 5 );
	}
	
	// Así sería iterativo pero... ¿cómo se generaliza?
	private static void combinatoriaDe3Iterativa( char[] letras ) {
		String comb0 = "";
		for (char letra1 : letras) {
			String comb1 = comb0 + letra1;
			for (char letra2 : letras) {
				String comb2 = comb1 + letra2;
				for (char letra3 : letras) {
					String comb3 = comb2 + letra3;
					System.out.println( comb3 );
				}
			}
		}
	}
	
	/** Visualiza en consola todas las combinaciones
	 * posibles con repetición de una serie de letras
	 * @param letras	Letras a combinar
	 * @param num	Número de letras a obtener en la combinación
	 */
	public static void combinatoria( char[] letras, int num ) {
		combinatoriaRec( num, "", letras );
	}
	
	/** Método de combinatoria recursivo
	 * @param num	Número de caracteres que quedan por probar para una combinación final
	 * @param comb	Combinación actual
	 * @param letras	Letras a combinar
	 */
	private static void combinatoriaRec( int num, String comb, char[] letras ) {
		if (num==0) {
			System.out.println( comb );
		} else {
			for (char miLetra : letras) {
				String miComb = comb + miLetra;
				combinatoriaRec( num-1, miComb, letras );
			}
		}
	}
	
		private static int contLetras( char letra, String frase ) {
			int cont = 0;
			for (int i=0; i<frase.length(); i++) {
				char l = frase.charAt(i);
				if (l==letra) cont++;
			}
			return cont;
		}

}
