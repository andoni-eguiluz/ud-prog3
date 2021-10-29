package es.deusto.prog3.cap03;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EjemploFileYRandom {

	public static void main(String[] args) {
		verFicheros();
		guardarBinario();
		leerLongRandom();
		probarPath();
	}
	
	// Ejemplo con file ver ficheros en carpetas
	private static void verFicheros() {
		File f = new File( "d://data/graphics" );
		for (File f2 : f.listFiles()) {
			System.out.println( f2.getName() + (f2.isDirectory() ? " D" : " F") );
			if (f2.isDirectory()) {
				for (File f3 : f2.listFiles()) {
					System.out.println( "   " + f3.getAbsolutePath() );
				}
			}
		}
	}

	// Guarda los impares hasta 2000 en binario en formato long (8 bytes)
	private static void guardarBinario() {
		try {
			FileOutputStream fos = new FileOutputStream( "pruebarandom.dat" );
			for (long l=1; l<2000; l=l+2) {
				byte[] bytes = new byte[8];
				bytes = longToBytes(l);
				fos.write( bytes );
			}
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	public static byte[] longToBytes(long l) {
		buffer.clear();
	    buffer.putLong(l);
	    return buffer.array();
	}

	// Si hiciera falta... ahora no hace porque directamente se puede leer el long desde un RAF
	public static long bytesToLong(byte[] bytes) {
		buffer.clear();
	    buffer.put(bytes);
	    buffer.flip();//need flip 
	    return buffer.getLong();
	}
	
	private static void leerLongRandom() {
		try {
			RandomAccessFile raf = new RandomAccessFile( "pruebarandom.dat", "r" );
			raf.seek( 8*1 );  // Segundo dato  (n-1)
			long l = raf.readLong();
			System.out.println( "El segundo impar es: " + l );
			raf.seek( 8*499 );  // El impar número 500 es...
			l = raf.readLong();
			System.out.println( "El impar número 500 es: " + l );
			// O bien
			System.out.println( "El impar número 35 es:" + leerLongRandom(raf, 34) );
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		// O bien
		private static long leerLongRandom (RandomAccessFile raf, int pos) throws IOException {
			raf.seek( 8 * pos );
			return raf.readLong();
		}
	
	private static void probarPath() {
		File f = new File( "d:\\data\\graphics\\Univ\\ComoLlegarAMiDespacho1.gif" );
		System.out.println( f.getAbsolutePath() );
		Path p = f.toPath();
		System.out.println( p.getRoot() );
		for (int i=0; i<p.getNameCount(); i++) {
			System.out.println( "  " + p.getName(i) );
		}
	}
	
}
