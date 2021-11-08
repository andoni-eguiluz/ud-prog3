package es.deusto.prog3.cap04;

/** Ejemplo de recursividad por la cola
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class RecursividadFinal {

	public static void main(String[] args) {
		int[] array = { 2, 4, 6, 8, 10 };
		visuArray( array, 0 );
	}

	// Visualizar un array es:
	//  - sacar a consola el primer elemento y rec. sacar el resto
	//  - caso base: acabo el array (indice > última posición)
	private static void visuArray( int[] array , int indice ) {
		if (indice>=array.length) return;
		System.out.println( array[indice] );
		visuArray( array, indice+1 );
	}

	// Al ser recursividad por la cola se puede transformar fácilmente
	// en un algoritmo puramente iterativo:
	private static void visuArrayIt( int[] array , int indice ) {
		while (indice<array.length) {
			System.out.println( array[indice] );
			indice = indice+1;
		}
	}

}
