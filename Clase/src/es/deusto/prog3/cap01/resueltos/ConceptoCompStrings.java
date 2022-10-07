package es.deusto.prog3.cap01.resueltos;

/** Clase de prueba relacionada con el ejemplo de conversión de comparación de UtilsString
 * 
 * @author andoni.eguiluz @ ingenieria.deusto.es
 *
 */
public class ConceptoCompStrings {
	public static void main(String[] args) {
		System.out.println( "panza".compareTo( "papa" ) );   // Negativo: alfabéticamente bien comparado
		System.out.println( "panza".compareTo( "Papa" ) );   // Positivo: mal
		System.out.println( "pánza".compareTo( "papa" ) );   // Positivo: mal
		System.out.println( "panza".compareTo( "pañal" ) );  // Negativo: bien
		System.out.println( "pañal".compareTo( "papa" ) );   // Positivo: mal
		System.out.println( "panzzal".compareTo( "papa" ) ); // Negativo: bien
	}
}
