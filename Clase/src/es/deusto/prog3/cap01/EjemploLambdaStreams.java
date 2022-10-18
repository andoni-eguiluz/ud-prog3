package es.deusto.prog3.cap01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Ejemplo de streams en Java Funcional
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploLambdaStreams {
	public static void main(String[] args) {
		ArrayList<Integer> l1 = new ArrayList<>(
				Arrays.asList( 1, 2, 7, -2, 9, 11, -7, 13, 12, 20, -21, 27, -2, 28, 0, 8, 31 ) );
		System.out.println( tratamientoConvencional( l1 ) );
		System.out.println( tratamientoConStreams( l1 ) );
	}
	
	private static double tratamientoConvencional( ArrayList<Integer> l1 ) {
		// a) No considerar los negativos
		// b) Solo tomar en cuenta los crecientes (si alguno es inferior al anterior entonces no considerarlo)
		// c) Visualizar los crecientes
		// d) Devolver la media
		int ultimoCreciente = -1;
		double suma = 0;
		int contador = 0;
		for (int i : l1) {
			if (i<0) {  // a) Quitamos los negativos
				continue;
			} else {
				if (i <= ultimoCreciente) {  // b) Quitamos los no crecientes
					continue;
				} else {
					ultimoCreciente = i;
					System.out.print( i + " " );  // c) Visualizar
					contador++;  // d) Cálculo de media
					suma += i;
				}
			}
		}
		System.out.println();
		return suma/contador;
	}
	
	private static int ultimoCreciente = -1;
	private static double tratamientoConStreams( ArrayList<Integer> l1 ) {
		// a) No considerar los negativos
		// b) Solo tomar en cuenta los crecientes (si alguno es inferior al anterior entonces no considerarlo)
		// c) Visualizar los crecientes
		// d) Devolver la media
		Stream<Integer> stream = 
			l1.stream()
				.filter( (i) -> { return (i>=0); } )  // a)
				.filter( (i) -> { if (i<=ultimoCreciente) return false; ultimoCreciente = i; return true; } );  // b)
		ArrayList<Integer> crecientes = stream.collect( Collectors.toCollection( ArrayList::new ) );  // Volcar a estructura final
		crecientes.stream().forEach( (i) -> { System.out.print( i + " " ); } ); // c) Visualización  (uso 1 estructura final)
		System.out.println(  );
		return 1.0 * crecientes.stream().reduce( 0, (suma, i) -> { return suma + i; } ) / crecientes.stream().count();  // d) Media (uso 2 estructura final)
	}
	
}
