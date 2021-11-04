package es.deusto.prog3.cap01.ejercicios;

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
		return s.replaceAll( "\n", "|" ).replaceAll( "\t", "|" );
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

}
