package es.deusto.prog3.cap01;

/** Clase de prueba para depurar con Eclipse (Debug = F11)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class PruebaDebug {

	private static String miMens = "";
	
	private static void mensajeaNumeros(int i) {
		for (int j=0; j<i; j++)
			sacaMens( "Número " + j );
	}
	private static void sacaMens( String mens ) {
		System.out.println( "Mensaje: " + mens + " (longitud " + mens.length() + ")" );
	}
	public static void main(String[] args) {
		mensajeaNumeros(7);
		miMens = null;
		sacaMens(miMens);
	}

}
