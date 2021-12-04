package es.deusto.prog3.cap05;

import java.util.ArrayList;
import java.util.List;

/** Algunos algoritmos con listas
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class AlgoritmosListas {

	private static final boolean DEBUG = true;
	
		// Auxiliar para ver quickSort lo que hace
		private static void visuLista( List l, int marca, int marcaInt, int desde, int hasta, String mens ) {
			if (DEBUG) {
				System.out.print( "   [ ");
				for (int i=0; i<l.size(); i++) {
					if (i==marca)
						System.out.print( "*" + l.get(i) + "* " );
					else if (i==desde)
						System.out.print( "->" + l.get(i) + " " );
					else if (i==hasta)
						System.out.print( l.get(i) + "<- " );
					else
						System.out.print( l.get(i) + " " );
					if (i==marcaInt)
						System.out.print( "<> " );
				}
				if (l.size()==marca)
					System.out.print( "** " );
				System.out.println("]  " + mens);
			}
		}
		
	static int quickSortDividir( List l, int desde, int hasta ) {
		Comparable pivote = (Comparable) l.get(desde);  // Cogemos de pivote el primero. Podría cogerse aleatoriamente o una mediana entre varios... cuanto más centrado de valor salga el pivote, mejor.
		int menoresOIguales = desde+1;
		int mayores = hasta;
		do {
			// Mover ref de menores o iguales hasta encontrar un mayor o cruzar refs
			visuLista( l, -1, -1, desde, hasta, "Dividiendo: pivote " + pivote );
			while ((menoresOIguales <= mayores) && 
					pivote.compareTo( l.get(menoresOIguales) )>=0)  {
				menoresOIguales++;
				visuLista( l, menoresOIguales, -1, desde, hasta, "Pivote: " + pivote + " (sube menores)" );
			}
			// Mover ref de mayores hasta encontrar un menor o cruzar refs
			while ((menoresOIguales <= mayores) &&
				    pivote.compareTo( l.get(mayores) )<0) {
				mayores--;
				visuLista( l, mayores, -1, desde, hasta, "Pivote: " + pivote + " (baja mayores)" );
			}
			// Si no se han cruzado aún, intercambiar los elementos
			if (menoresOIguales < mayores) {
				Comparable temp = (Comparable) l.get(menoresOIguales);
				l.set( menoresOIguales, l.get(mayores) );
				l.set( mayores, temp );
				visuLista( l, -1, -1, desde, hasta, "Intercambio " + l.get(menoresOIguales) + " y " + l.get(mayores) );
			}
		} while (menoresOIguales <= mayores);
		// Intercambiar elemento pivote con el del punto de división
		Comparable temp = (Comparable) l.get(mayores);
		l.set( mayores, l.get(desde) );
		l.set( desde, temp );
		visuLista( l, -1, -1, desde, hasta, "Intercambio pivote con " + l.get(desde) );
		return mayores;  // Se devuelve la referencia del pivote
	}
	
	static void quickSort( List l, int desde, int hasta ) {
		if (desde < hasta) {
			// Si hay más de un elemento por ordenar,
			// Dividir el vector en dos obteniendo el punto de corte
			int corte = quickSortDividir( l, desde, hasta );
			// Recursivamente, ordenar las dos partes (el pivote ya queda ordenado)
			quickSort( l, desde, corte-1 );
			quickSort( l, corte+1, hasta );
		} else {  // caso base: desde>=hasta  (nada que hacer)
			if (desde==hasta)
				visuLista( l, desde, -1, -1, -1, "Caso base - elemento suelto ya ordenado!" );
			else
				visuLista( l, -1, hasta, -1, -1, "Caso base - no hay elementos para ordenar!" );
		}
	}
	static void quickSort( List l ) {
		if (!l.isEmpty())
			quickSort( l, 0, l.size()-1 );
	}
	
	// Pre: desde <= hasta  +  desde>=0  +  hasta<l.size()
	// Pre: l está ordenada
	static int busquedaBinaria( List<?> l, Comparable valor, int desde, int hasta ) {
		if (desde==hasta) {
			if (l.get(desde).equals(valor))
				return desde; // Caso base (encontrado)
			else
				return -1; // Caso base (no encontrado)
		} else {
			int mitad = (desde + hasta) / 2;
			if (valor.compareTo(l.get(mitad))<=0) {
				return busquedaBinaria(l, valor, desde, mitad);
			} else {
				return busquedaBinaria(l, valor, mitad+1, hasta);
			}
		}
	}

	/** Método de añadido en una lista que la mantiene siempre ordenada
	 * Pre: La lista l está ya ordenada antes de la ejecución de este método
	 * Post: l sigue ordenada
	 * @param l	Lista en la que insertar el nuevo valor
	 * @param valor	Elemento nuevo a insertar en la lista
	 */
	static void addEnOrden( List<?> l, Comparable<?> valor ) {
		addEnOrden( l, valor, 0, l.size()-1 );
	}
	
	// Pre: desde>=0  +  desde<=l.size()  +  hasta<l.size()
	// Pre: l está ordenada
	static void addEnOrden( List l, Comparable valor, int desde, int hasta ) {
		if (desde>=hasta) {  // Desde>hasta contempla el caso de lista vacía inicial
			if (desde>hasta || valor.compareTo( l.get(desde) )>0 )
				l.add( desde+1, valor );
			else
				l.add( desde, valor );
		} else {
			int mitad = (desde + hasta) / 2;
			if (valor.compareTo(l.get(mitad))<=0) {
				addEnOrden(l, valor, desde, mitad);
			} else {
				addEnOrden(l, valor, mitad+1, hasta);
			}
		}
	}
	
	// Pre: !l.isEmpty()
	// Pre: l está ordenada
	static int busquedaBinariaIt( List<?> l, Comparable valor ) {
		int desde = 0;
		int hasta = l.size() - 1;
		while (desde!=hasta) {
			int mitad = (desde + hasta) / 2;
			if (valor.compareTo(l.get(mitad))<=0) {
				hasta = mitad;
			} else {
				desde = mitad+1;
			}
		}
		if (l.get(desde).equals(valor))
			return desde; // Caso base (encontrado)
		else
			return -1; // Caso base (no encontrado)
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> l = new ArrayList<>();
		for (int i : new int[] { 5, 3, 1, 2, 12, 30, 18, 9, 23 } ) {
			l.add( i );
		}
		System.out.println( "Lista: " + l );
		System.out.println( "Lista ordenada por quick sort: " );
		quickSort( l );
		System.out.println( "  " + l );
		int aBuscar = 18;
		System.out.println( "Lista: " + l );
		System.out.println( "Dónde está el " + aBuscar + "? " +
				busquedaBinaria( l, new Integer(aBuscar), 0, l.size()-1 ) );
		aBuscar = 30;
		System.out.println( "Dónde está el " + aBuscar + "? " +
				busquedaBinariaIt( l, new Integer(aBuscar) ) );
		Integer aInsertar = new Integer(17);
		addEnOrden( l, aInsertar );
		System.out.println( "Tras insertar " + aInsertar + ": " + l );
	}
}
