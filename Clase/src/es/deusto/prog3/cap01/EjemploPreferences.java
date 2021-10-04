package es.deusto.prog3.cap01;

import java.util.prefs.*;

/**  Ejemplo de clase java.util.prefs.Preferences
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class EjemploPreferences {

	// Variable de preferencias - se crea con una instancia 
	static private Preferences prefs = 
			Preferences.userNodeForPackage(EjemploPreferences.class);
	
	// Prueba de preferencias 2017
	public static void main(String[] args) {
		String pref = "test";
		String valor = "hola";
		System.out.println( "Valor de la preferencia " + pref +
				": " + prefs.get( pref, "NO EXISTE" ));
		System.out.println( "Cambiamos la preferencia a: " + valor );
		prefs.put( pref, valor );  // Mete la preferencia
		// prefs.remove( pref );  //  Lo quita
		
		// Las preferencias se guardan en el registro de Windows o
		// en los equivalentes en otros sistemas.
		// Por tanto, se mantienen entre ejecuciones y 
		// cierres/apagados del ordenador.
		// (No es adecuado abusar)
		
		// En windows
		// regedit
		// Clave en:
		// Equipo/HKEY_CURRENT_USER/SOFTWARE/JavaSoft/Prefs/ud/prog3/cap01
	}

}
