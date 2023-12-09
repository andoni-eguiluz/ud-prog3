package es.deusto.prog3.utils;

/** Ejemplo de clase de traducción para proyectos que quieran plantear GUIs multilingües
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class Traduccion {

	public static enum Idioma { ESP, EUS, ENG };  // Tabla de idiomas - definir todos los necesarios
	// Base: tabla de traducción. Todos los strings en el idioma origen y en todos los idiomas destino, en orden
	// Podría cargarse de un csv en lugar de definirla en memoria
	private static String[][] traducciones = {
		{ "Hola", "Kaixo", "Hello" }
		,{ "Adiós", "Agur", "Bye" }
	};
	
	public static String getTrad( String original, Idioma idioma ) {
		for (int i=0; i<traducciones.length; i++) {
			if (traducciones[i][0].equalsIgnoreCase(original)) {
				return traducciones[i][idioma.ordinal()];
			}
		}
		return "NO-TRADUCIDO: " + original;
	}
	
	/** Prueba
	 * @param args	No usado
	 */
	public static void main(String[] args) {
		System.out.println( getTrad( "Hola", Idioma.ENG ) );
		System.out.println( getTrad( "Adiós", Idioma.EUS ) );
	}
}
