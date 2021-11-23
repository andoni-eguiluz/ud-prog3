package es.deusto.prog3.cap04;
import java.io.*;

public class RecorridoRecursivoCarpeta {

	public static void main(String[] args) {
		System.out.println( "Visualizando ficheros:");
		verFicheros();
	}
	
	private static void verFicheros() {
		File f = new File("e:/data/web");  // TODO Sustituye esta ruta por la que quieras procesar
		System.out.println( f + " es dir? " + f.isDirectory() );
		verFicherosRec( f, 0 );
	}
	
	private static void verFicherosRec( File dir, int nivel ) {
		// System.out.println( "Llamada rec " + dir.getName() + " en nivel " + nivel );
		if (dir.isDirectory())
			for (File f : dir.listFiles()) {
				for (int i=0; i<nivel; i++) System.out.print( " " );
				System.out.println( f.getName() );
				verFicherosRec( f, nivel+1 );
			}
	}
	

}
