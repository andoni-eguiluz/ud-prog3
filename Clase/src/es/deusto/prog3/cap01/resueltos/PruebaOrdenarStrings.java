package es.deusto.prog3.cap01.resueltos;

public class PruebaOrdenarStrings {
	public static void main(String[] args) {
		compararStrings( "a", "b" );
		compararStrings( "b", "a" );
		compararStrings( "a", "a" );
		compararStrings( "A", "b" );
		compararStrings( "ala", "Beta" );
		compararStrings( "caña", "capa" );
	}
	
	private static void compararStrings( String s1, String s2 ) {
		int comparacion = UtilsString.convierteOrd(s1).compareTo( UtilsString.convierteOrd(s2) );
		if (comparacion < 0) {
			System.out.println( s1 + " es anterior a " + s2 );
		} else if (comparacion > 0) {
			System.out.println( s1 + " es posterior a " + s2 );
		} else {
			System.out.println( s1 + " es igual a " + s2 );
		}
	}
}
