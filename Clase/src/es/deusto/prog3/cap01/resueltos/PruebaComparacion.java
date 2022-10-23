package es.deusto.prog3.cap01.resueltos;

/** Prueba de cómo se hace la comparación entre Strings...
 * para sugerir una utilidad nueva (UtilsString.convierteAOrdenable) que permita ordenar correctamente en castellano
 * @author andoni.eguiluz @ ingenieria.deusto.es
 *
 */
public class PruebaComparacion {
	public static void main(String[] args) {
		String s1 = "Hola";
		String s2 = "Adiós";
		System.out.println( s1 + " comp con " + s2 + " = " + s1.compareTo( s2 ) );
		System.out.println( UtilsString.convierteAOrdenable(s1) + " comp con " + UtilsString.convierteAOrdenable(s2) 
		+ " = " + UtilsString.convierteAOrdenable(s1).compareTo( UtilsString.convierteAOrdenable(s2) ) );
		s1 = "Hola";
		s2 = "adiós";
		System.out.println( s1 + " comp con " + s2 + " = " + s1.compareTo( s2 ) );
		System.out.println( UtilsString.convierteAOrdenable(s1) + " comp con " + UtilsString.convierteAOrdenable(s2) 
		+ " = " + UtilsString.convierteAOrdenable(s1).compareTo( UtilsString.convierteAOrdenable(s2) ) );
		s1 = "adius";
		s2 = "adiós";
		System.out.println( s1 + " comp con " + s2 + " = " + s1.compareTo( s2 ) );
		System.out.println( UtilsString.convierteAOrdenable(s1) + " comp con " + UtilsString.convierteAOrdenable(s2) 
		+ " = " + UtilsString.convierteAOrdenable(s1).compareTo( UtilsString.convierteAOrdenable(s2) ) );
		s1 = "caña";
		s2 = "capo";
		System.out.println( s1 + " comp con " + s2 + " = " + s1.compareTo( s2 ) );
		System.out.println( UtilsString.convierteAOrdenable(s1) + " comp con " + UtilsString.convierteAOrdenable(s2) 
			+ " = " + UtilsString.convierteAOrdenable(s1).compareTo( UtilsString.convierteAOrdenable(s2) ) );
	}
}
