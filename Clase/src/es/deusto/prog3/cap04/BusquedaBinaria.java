package es.deusto.prog3.cap04;

import java.util.Arrays;

/** Varias soluciones de búsqueda binaria, iterativa y recursivas
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class BusquedaBinaria {
	private static int TAMANYO_ARRAY = 100000;
	private static int[] array;
	
	/** Método de prueba de búsqueda binaria
	 * @param args
	 */
	public static void main(String[] args) {
		// Inicialización
		array = inicializaVector( TAMANYO_ARRAY ); // Inicializa del 1 al n

		System.out.println( "Vector: [1, 3, 5, 7..., " + (TAMANYO_ARRAY*2 - 1) + "]" + " (" + array.length + " elementos)" );
		// Test de búsqueda de un valor
		int val = 10;  // Valor a buscar
		contLlamadas = 0; contComparaciones = 0;
		System.out.println( "Está el valor " + val + " en posición " + buscaEnVector( array, val ) ); // Pos
		System.out.println( "  Búsqueda: " + contLlamadas + " llamadas y " + contComparaciones + " comparaciones." );
		int val2 = 11579;  // Valor a buscar 2
		contLlamadas = 0; contComparaciones = 0;
		System.out.println( "Está el valor " + val2 + " en posición " + buscaEnVector( array, val2 ) ); // Pos
		System.out.println( "  Búsqueda: " + contLlamadas + " llamadas y " + contComparaciones + " comparaciones." );
		
		System.out.println( "\nTest de llamadas y comparaciones:");

		// Test con versión iterativa
		contLlamadas = 0; contComparaciones = 0;
		for (int i=0; i<= TAMANYO_ARRAY; i++) buscaEnVectorLineal( array, i );
		System.out.println( "Versión lineal: " + contLlamadas + " llamadas y " + contComparaciones + " comparaciones." );

		// Test con versión 1
		contLlamadas = 0; contComparaciones = 0;
		for (int i=0; i<= TAMANYO_ARRAY; i++) buscaEnVector( array, i );
		System.out.println( "Versión binaria 1: " + contLlamadas + " llamadas y " + contComparaciones + " comparaciones." );

		// Test con versión 2
		contLlamadas = 0; contComparaciones = 0;
		for (int i=0; i<= TAMANYO_ARRAY; i++) buscaEnVector2( array, i, 0, TAMANYO_ARRAY-1 );
		System.out.println( "Versión binaria 2: " + contLlamadas + " llamadas y " + contComparaciones + " comparaciones." );

		// Test con versión 3
		contLlamadas = 0; contComparaciones = 0;
		for (int i=0; i<= TAMANYO_ARRAY; i++) buscaEnVector3( array, i, 0, TAMANYO_ARRAY-1  );
		System.out.println( "Versión binaria 3: " + contLlamadas + " llamadas y " + contComparaciones + " comparaciones." );
		// Chequeo de corrección si se quisiera hacer (por ejemplo para test de unidad):
		// for (int i=0; i<= TAMANYO_ARRAY; i++) { 
		// 		int b = buscaEnVector3( array, i, 0, TAMANYO_ARRAY-1  ); 
		//		if ((i%2==0 && b!=-1) || (i%2!=0 && b==-1)) 
		//			System.out.println( "Error en búsqueda de " + i + " con retorno " + b );
		// }
		
		// Segundo test: Cómo se busca cuando hay duplicidades en los datos
		System.out.println();
		array = new int[] { 1, 1, 2, 2, 2, 2, 2, 3, 4, 5 };
		int donde2 = buscaEnVector( array, 2, 0, array.length-1 );
		System.out.println( "Dividiendo por > | == | <");
		System.out.println( "  El 2 se encuentra en la posición " + donde2 + " en " + Arrays.toString(array) );
		System.out.println( "  (nota: donde se encuentra por primera vez -coincidencia-)" );
		donde2 = buscaEnVector3( array, 2, 0, array.length-1 );
		System.out.println( "Dividiendo por >= | <");
		System.out.println( "  El 2 se encuentra en la posición " + donde2 + " en " + Arrays.toString(array) );
		donde2 = buscaEnVector3b( array, 2, 0, array.length-1 );
		System.out.println( "Dividiendo por > | <=");
		System.out.println( "  El 2 se encuentra la posición " + donde2 + " en " + Arrays.toString(array) );
	}

		private static int[] inicializaVector( int tam ) {
			array = new int[tam];
			for (int i=0; i<tam; i++) {
				array[i] = i*2+1;   // Mete los "n" primeros impares
			}
			return array;
		}		
	
	/** Busca un valor en un vector de enteros ORDENADO de forma ITERATIVA (lineal)
	 * @param array	Array de valores ordenado
	 * @param valor	Valor que se busca
	 * @return	Posición en el que el valor está, -1 si no existe
	 */
	public static int buscaEnVectorLineal( int[] array, int valor ) {
		for (int i=0; i<array.length; i++) {
			if (array[i]>=valor) {  // Encontrado o pasado
				contComparaciones++; contComparaciones++;
				if (array[i]==valor)
					return i;
				else
					return -1;
			}
			contComparaciones++;
		}
		return -1;
	}
	
	/** Busca un valor en un vector de enteros ORDENADO
	 * @param array	Array de valores ordenado
	 * @param valor	Valor que se busca
	 * @return	Posición en el que el valor está, -1 si no existe
	 */
	// Método shell (no recursivo)
	public static int buscaEnVector( int[] array, int valor ) {
		// Previo
		return buscaEnVector( array, valor, 0, array.length-1 );
		// Posterior
	}
		private static long contLlamadas = 0;
		private static long contComparaciones = 0;
		private static int buscaEnVector( int[] array, int valor, int ini, int fin  ) {
										contLlamadas++;
			// System.out.println( "Busco en " + ini + "," + fin );
										contComparaciones++;
			if (ini>fin) return -1;  // Caso base: no encontrado
			int mitad = (ini + fin) / 2;
										contComparaciones++;
			if (array[mitad]==valor) { // Caso base: encontrado
				return mitad;
			} else {
										contComparaciones++;
				if (array[mitad]>valor) {  // Izquierda
					return buscaEnVector( array, valor, ini, mitad-1 );
				} else {  // Derecha
					return buscaEnVector( array, valor, mitad+1, fin );
				}
			}
		}

		// Versión 2: Alguna comparación menos (atrasando el == que es el que menos veces pasa)
		private static int buscaEnVector2( int[] array, int valor, int ini, int fin  ) {
										contLlamadas++;
										contComparaciones++;
			if (ini>fin) return -1;
			int mitad = (ini + fin) / 2;
										contComparaciones++;
			if (array[mitad]>valor) {
				return buscaEnVector2( array, valor, ini, mitad-1 );
			} else {
										contComparaciones++;
				if (array[mitad]==valor) {
					return mitad;
				} else {
					return buscaEnVector2( array, valor, mitad+1, fin );
				}
			}
		}
		
		// Versión 3: Dividiendo el vector siempre en mitades (no en 3 partes) SIN COMPARAR el elemento intermedio
		// Versión < por un lado y >= por otro
		private static int buscaEnVector3( int[] array, int valor, int ini, int fin  ) {
										contLlamadas++;
										contComparaciones++;
			if (ini==fin) {
										contComparaciones++;
				if (array[ini]==valor) {
					return ini;
				} else {
					return -1;
				}
			} else {
				int mitad = (ini + fin) / 2;
										contComparaciones++;
				if (array[mitad]>=valor) {
					return buscaEnVector3( array, valor, ini, mitad );  // Donde va el == va la mitad
				} else {  // <
					return buscaEnVector3( array, valor, mitad+1, fin ); // Donde no, el de la mitad se desprecia (2 partes)
				}
			}
		}
		
		// Versión 3b: Dividiendo el vector siempre en mitades (no en 3 partes) SIN COMPARAR el elemento intermedio
		// Versión <= por un lado y > por otro
		private static int buscaEnVector3b( int[] array, int valor, int ini, int fin  ) {
										contLlamadas++;
										contComparaciones++;
			if (ini==fin) {
										contComparaciones++;
				if (array[ini]==valor) {
					return ini;
				} else {
					return -1;
				}
			} else {
				int mitad = (ini + fin + 1) / 2;  // Ojo aquí que si calculamos la media sin +1 al volver a llamar con la mitad por la parte posterior nos arriesgamos a un stackoverflow (sin progresión: ini,fin = mitad,fin)
										contComparaciones++;
				if (array[mitad]>valor) {
					return buscaEnVector3b( array, valor, ini, mitad-1 );  // Si no es igual, se desprecia la mitad
				} else {  // <=
					return buscaEnVector3b( array, valor, mitad, fin ); // Si es <= se incluye la mitad
				}
			}
		}
		
}
