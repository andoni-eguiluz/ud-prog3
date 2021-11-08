package es.deusto.prog3.cap04.ejercicios;

import java.util.Random;

public class MergeSort {

	/** Ordena recursivamente (mergesort) un array de enteros
	 * @param nums	Array de enteros a ordenar
	 * @param ini	Posición inicial de la ordenación
	 * @param fin	Posición final de la ordenación (inclusive)
	 */
	public static void mergeSort( int[] nums, int ini, int fin ) {
		// TODO
		// Si el array es de tamaño 1, nada que ordenar (caso base)
		// Si no, caso recursivo:
		//  - Buscar el punto medio
		//  - Ordenar primera mitad
		//  - Ordenar segunda mitad
		//  - Mezclar ambas mitades (iterativo)
	}

	// Algoritmo de mezcla (no recursivo)
	// Mezcla en nums las mitades ya ordenadas (ini1 a fin1) con (fin1+1 a fin2)
	private static void mezclaMergeSort( int[] nums, int ini1, int fin1, int fin2 ) {
		int ini2 = fin1+1; // Inicio segunda mitad
		int[] destino = new int[fin1-ini1+fin2-ini2+2];
		// TODO
		// 1. Recorrer a la vez la mitad ini1 a fin1
		//    Y la mitad ini2 a fin2
		//    Comparar ambos elementos
		//    El menor llevarlo a destino, y avanzar en esa mitad
		//    Y seguir hasta que acabemos con las dos mitades y llevemos todos a destino
		// 2. Copiar destino a nums[ini1] a nums[fin2]
	}
	
	public static void main(String[] args) {
		// Test de ordenación por mezcla (array de enteros)
		Random r = new Random();
		int CUANTOS_NUMS_ALEATORIOS = 8;
		int [] nums = new int[CUANTOS_NUMS_ALEATORIOS];
		for (int i=0; i<CUANTOS_NUMS_ALEATORIOS; i++) {
			nums[i] = r.nextInt( 1000000 );  // Entero entre 0 y 1000000
		}
		System.out.println( "Array sin ordenar:" );
		for (int i=0; i<CUANTOS_NUMS_ALEATORIOS; i++) System.out.print( nums[i] + "  " );
		mergeSort(nums, 0, CUANTOS_NUMS_ALEATORIOS-1);
		System.out.println();
		System.out.println( "Array ya ordenado:" );
		for (int i=0; i<CUANTOS_NUMS_ALEATORIOS; i++) System.out.print( nums[i] + "  " );
	}
}
