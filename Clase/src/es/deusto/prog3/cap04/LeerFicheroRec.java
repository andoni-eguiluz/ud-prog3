package es.deusto.prog3.cap04;

import java.io.*;

public class LeerFicheroRec {

	@SuppressWarnings("unused")
	private static void leerRecursoNormal( BufferedReader br ) throws IOException {
		String linea = br.readLine();
		while (linea!=null) {
			if (!linea.isEmpty()) System.out.println( linea );
			linea = br.readLine();
		}
	}
	private static void leerRecursoRec( BufferedReader br ) throws IOException {
		String linea = br.readLine();
		if (linea!=null) {
			// Al derecho
			if (!linea.isEmpty()) System.out.println( linea );
			leerRecursoRec( br );
			// Al revés?
		}
	}
	// Método Shell - no recursivo - inicializar y llamar primera vez al recursivo
	private static void leerRecurso( String nombreRecurso ) {
		try {
			BufferedReader br = new BufferedReader( new InputStreamReader( 
				LeerFicheroRec.class.getResourceAsStream( nombreRecurso ) ) );
			leerRecursoRec( br );
			// leerRecursoNormal( br );  // Esto sería en interativo
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		leerRecurso( "verbosRegulares.txt" );
	}

}
