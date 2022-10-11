package es.deusto.prog3.cap01.resueltos;

/** Utilidades de Strings de ejemplo
 * Clase preparada para hacer con ella pruebas unitarias con JUnit
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto	
 */
public class UtilsString {

	/** Devuelve cualquier string sin saltos de línea ni tabuladores (para poder exportarlo de forma tabular sin conflicto)
	 * @param s	String con cualquier contenido
	 * @return	Mismo string sustituyendo \t con el carácter | y \n con el carácter #; Devuelve null si s es null.
	 */
	public static String quitarTabsYSaltosLinea( String s ) {
		if (s==null) {
			return null;
		}
		return s.replaceAll( "\n", "#" ).replaceAll( "\t", "|" );
	}
	
	/** Devuelve cualquier string truncado al número de caracteres indicado, con puntos suspensivos al final si se ha truncado
	 * @param s	String con cualquier contenido o null
	 * @param largo	Número máximo de caracteres de longitud. NO DEBE SER NEGATIVO
	 * @return	String recortado si ocupaba más de largo caracteres con tres puntos suspensivos al final; mismo string en caso contrario; null si s es null.
	 * @throws IndexOutOfBoundsException Generada si el largo es negativo
	 */
	public static String wrapString( String s, int largo ) throws IndexOutOfBoundsException {
		if (s==null) 
			return null;
		else if (s.length()>largo)
			return s.substring(0, largo) + "...";
		else
			return s;
	}
	
	public static void main(String[] args) {
		// Prueba convencional (no estructurada, no exhaustiva, no automatizable)
//		// Versión 1
//		String prueba = "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.";
//		System.out.println( prueba );
//		System.out.println( quitarTabsYSaltosLinea( prueba ));
//		System.out.println( wrapString( prueba, 3 ) );
//		System.out.println( wrapString( prueba, 10 ) );
//		System.out.println( wrapString( quitarTabsYSaltosLinea(prueba), 10 ) );
		
		// Versión 2
		String prueba = "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.";
		String prueba2 = "Hola#Esto es un string con tres líneas#y|varios|tabuladores.";
		// System.out.println( prueba );
		// System.out.println( quitarTabsYSaltosLinea( prueba ));
		if (prueba2.equals(quitarTabsYSaltosLinea(prueba))) {
			System.out.println( "OK" );
		} else {
			System.out.println( "FAIL" );
		}
		// System.out.println( wrapString( prueba, 3 ) );
		if ("Hol...".equals(wrapString( prueba, 3))) {
			System.out.println( "OK" );
		} else {
			System.out.println( "FAIL" );
		}
		// System.out.println( wrapString( prueba, 10 ) );
		// System.out.println( wrapString( quitarTabsYSaltosLinea(prueba), 10 ) );

	}
	
	// TODO Habría que elegir solo una de las dos cabeceras siguientes

	// Opción 1 de definición: más genérica y sin detalles de implementación (recomendable desde el punto de vista del diseño)
	
	/** Convierte un string castellano a otro string ordenable sin problemas,
	 * de modo que al comparar los strings devueltos las palabras originales puedan ser comparadas bien con el método #compareTo.
	 * Por ejemplo "caña" sería > que "cano" pero sería también > que "capa" erróneamente,
	 *  al convertirlos convierteAOrdenable("CANO") < convierteAOrdenable("CANZZA") < convierteAOrdenable("CAPA") 
	 * Por ejemplo "panza" < "papá" < "pañal", sin embargo convertidas se ordenan bien: 
	 *  convierteAOrdenable("PANZA") < convierteAOrdenable("PANZZAL") < convierteAOrdenable("PAPA")
	 * @param original	String original
	 * @return	String convertido a uno equivalente, listo para ordenar por comparación directa
	 */
	
	// Opción 2 de definición: directa a la implementación (preferible si vamos a utilizarla para otras cosas que no sea solo comparar)
	//   (en ese caso quizás su nombre sería mejor pasaMayusculasSinTildesNiEnyes() que el que tiene)
	
	/** Convierte un string castellano a otro string ordenable sin problemas con #compareTo,
	 *  sustituyendo las minúsculas por mayúsculas, eliminando tildes, diéresis, y sustituyendo las "Ñ" por "NZZ" 
	 * Por ejemplo "caña" sería > que "cano" pero sería también > que "capa" erróneamente, al convertirlos "CANO" < "CANZZA" < "CAPA" 
	 * Por ejemplo "panza" < "papá" < "pañal", sin embargo convertidas se ordenan bien: "PANZA" < "PANZZAL" < "PAPA"
	 * @param original	String original
	 * @return	String convertido a uno equivalente, listo para ordenar por comparación directa
	 */
	public static String convierteAOrdenable( String original ) {
		String ret = original;
		ret = original.toUpperCase();
		// Podría hacerse directo
		// ret = ret.replaceAll("É", "E").replaceAll("Á", "A").replaceAll("Í", "I").replaceAll("Ó", "O").replaceAll("Ú", "U");  ... etc.
		// Pero lo hacemos con dos arrays de conversión, más legible y mantenible para cambios futuros
		String[] queCambio = { "Á", "É", "Í", "Ó", "Ú", "Ü", "Ñ" };
		String[] aQue = {      "A", "E", "I", "O", "U", "U", "NZZ" };
		for (int i=0; i<queCambio.length; i++) {
			ret = ret.replaceAll( queCambio[i], aQue[i] );
		}
		return ret;
	}
	
}
