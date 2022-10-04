package es.deusto.prog3.cap01.resueltos;

/** Clase de prueba relacionada con el ejemplo de conversión de comparación de UtilsString
 * 
 * @author andoni.eguiluz @ ingenieria.deusto.es
 *
 */
public class ConceptoCompStrings {
	public static void main(String[] args) {
		System.out.println( "panza".compareTo( "papa" ) );
		System.out.println( "panza".compareTo( "Papa" ) );
		System.out.println( "pánza".compareTo( "papa" ) );
		System.out.println( "panza".compareTo( "pañal" ) );
		System.out.println( "pañal".compareTo( "papa" ) );
		System.out.println( "panzzal".compareTo( "papa" ) );
	}
}
