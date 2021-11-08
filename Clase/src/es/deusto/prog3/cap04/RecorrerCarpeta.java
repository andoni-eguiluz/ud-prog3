package es.deusto.prog3.cap04;

import java.io.File;

/** Ejercicio de recorrer carpeta pendiente...
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class RecorrerCarpeta {

	/** visualiza los ficheros y carpetas de nivel N
	 * @param file	directorio o fichero a visualizar
	 * @param n	su nivel
	 */
	public static void recorrerRec( File f, int nivel ) {
		for (int i=0; i<nivel; i++) System.out.print( " " );
		if (f.isFile()) {
			System.out.println( f.getName() );
		} else {  // isDirectory()
			System.out.println( f.getName() + "\\" );
			for (File f2 : f.listFiles()) {
				recorrerRec( f2, nivel+ 1 );
			}
		}
	}
	
	public static void main(String[] args) {
		File f = new File( "d:/media" );
		recorrerRec( f, 0 );
	}

}
