package es.deusto.prog3.cap01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EjemploZip {

	public static void main(String[] args) {
		// File dirActual = new File( System.getProperty("user.dir") );
		String paquete = EjemploZip.class.getPackage().getName();
		System.out.println( paquete );
		File dirActual = new File( System.getProperty("user.dir") + "/src/" + 
				 paquete.replaceAll( "\\.", "/" ) + "/" );
		System.out.println("Directorio de trabajo: " + dirActual );
		System.out.println( "Ficheros en esta carpeta: ");
		File[] fics = dirActual.listFiles();
		for (File f : fics) 
			if (f.isFile()) 
				System.out.println( "  " + f.getName() );
		System.out.println( "Comprimiendo estos ficheros a fichero test.zip..." );
		comprimirAZip( "test.zip", fics );
	}
	
	private static void comprimirAZip( String nomZip, File[] listaFics ) {
	    // Crear un buffer para la lectura de ficheros
	    byte[] buffer = new byte[1024];
	    try {
	        // Crear el fichero Zip
	        ZipOutputStream ficZip = new ZipOutputStream(new FileOutputStream(nomZip) );
	        // Comprimir cada fichero en ese zip
			for (File f : listaFics) 
				if (f.isFile()) {
					System.out.println( "Comprimiendo " + f.getName() + "..." );
		            FileInputStream in = new FileInputStream( f );
		            // Añade punto de entrada zip (entry)
		            ficZip.putNextEntry(new ZipEntry(f.getName()));
		            // Mueve los bytes al zip a través del buffer
		            int dato;
		            while ((dato = in.read(buffer)) > 0) {
		                ficZip.write(buffer, 0, dato);
		            }
		            // Completar la entrada
		            ficZip.closeEntry();
		            in.close();
		        }
	        // Cerrar el fichero zip
	        ficZip.close();
	        System.out.println( "Fichero zip " + nomZip + " acabado correctamente.");
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }	
    }

}
