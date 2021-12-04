package es.deusto.prog3.cap05;

import static es.deusto.prog3.cap05.AnalisisEjecucion.*;

import java.util.LinkedList;

public class PruebaPilasColas {
	public static void main(String[] args) {
		LinkedList<Integer> pila = new LinkedList<Integer>();
		LinkedList<Integer> cola = new LinkedList<Integer>();
		LinkedList<Integer> lista = new LinkedList<Integer>();
		LinkedList<Integer> lista2 = new LinkedList<Integer>();
		System.out.println( "Meter y sacar 1, 3, 5 en pila:");
		pila.push( 1 ); pila.push( 3 ); pila.push( 5 );
		while (!pila.isEmpty())
			System.out.print( " " + pila.pop() );
		System.out.println();
		System.out.println( "Meter y sacar 1, 3, 5 en cola:");
		cola.addLast( 1 ); cola.addLast( 3 ); cola.addLast( 5 );
		while (!cola.isEmpty())
			System.out.print( " " + cola.removeFirst() );
		System.out.println();
		int numEls = 100000;
		visuTiempo( "Análisis de tiempos:", true );
		for (int i=0; i<numEls; i++) lista.add(i);
		visuTiempo( "Crear lista de " + numEls + " datos" );
		for (int i=0; i<numEls; i++) { lista2.add( i/2, i ); if (i%10000==0) System.out.print( " " + i ); } System.out.println();
		visuTiempo( "Crear lista por el medio" );
		for (int i=0; i<numEls; i++) pila.push(i);
		visuTiempo( "Crear pila de " + numEls + " datos" );
		for (int i=0; i<numEls; i++) cola.addLast(i);
		visuTiempo( "Crear cola de " + numEls + " datos" );
		for (int i=numEls-1; i>=0; i--) { lista.remove( i/2 ); if (i%10000==0) System.out.print( " " + i ); } System.out.println();
		visuTiempo( "Vaciar lista por el medio" );
		for (int i=0; i<numEls; i++) pila.pop();
		visuTiempo( "Vaciar pila" );
		for (int i=0; i<numEls; i++) cola.removeFirst();
		visuTiempo( "Vaciar cola" );
	}
}
