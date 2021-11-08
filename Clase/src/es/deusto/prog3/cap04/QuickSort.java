package es.deusto.prog3.cap04;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

/** Ejemplo de algoritmo quicksort recursivo
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class QuickSort {

	private static int[] v;
	private static final boolean DEBUG = true;  // Muestra información de depuración de progreso del algoritmo
	
	public static void main(String[] args) {
		v = new int[10];
		initArray( v, "randomsinrep" );
		System.out.println( "Array antes de ordenar:" );
		showArray( v );
		quickSort( v );
		System.out.println( "Array después de ordenar:" );
		showArray( v );
	}
	
		private transient static int prof, profMax;       // Variables de debug
		private transient static PrintStream salida;
	
		static void quickSort( int[] l ) {
			if (DEBUG) { prof = 0; profMax = 0; }
			try {
					if (DEBUG) salida = System.out;  // O si se quiere a fichero...  new PrintStream( "quicksort.log" );
					if (DEBUG) visuProgresoQS( l, -1, -1, 0, l.length-1, "Array inicial: " );
				quickSort( l, 0, l.length-1 );
					if (DEBUG) System.out.println( "Tamaño " + l.length + " -> prof.rec.máxima " + profMax );
					if (DEBUG) if (salida!=System.out) salida.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		static void quickSort( int[] l, int desde, int hasta ) {
				if (DEBUG) prof++;
				if (DEBUG) if (prof>profMax) profMax = prof;
			if (desde < hasta) {
				// Si hay más de un elemento por ordenar,
				// Dividir el vector en dos obteniendo el punto de corte
				int corte = quickSortDividir( l, desde, hasta );
				// Recursivamente, ordenar las dos partes (el pivote ya queda ordenado)
				quickSort( l, desde, corte-1 );
				quickSort( l, corte+1, hasta );
			} // caso base: desde>=hasta  (nada que hacer)
				if (DEBUG) prof--;
		}
		static int quickSortDividir( int[] l, int desde, int hasta ) {
			int posPivote = mediana(l,desde,hasta);  // Cogemos de pivote el intermedio entre primero, intermedio y mayor. Podría cogerse aleatoriamente o el primero... cuanto más centrado de valor salga el pivote, mejor.
			// int posPivote = medianaAleatoria(l,desde,hasta);  // Esto cogería el pivote aleatorio
			if (DEBUG) for (int i=0; i<prof; i++) System.out.print( "  " );
			if (DEBUG) System.out.println( "Ordenando array entre " + desde + " ("+v[desde]+") " + " y " + hasta + " ("+v[hasta]+") " + " -> Pivote " + posPivote + " ("+v[posPivote]+")" ); 
			if (desde!=posPivote) intercambiarEnArray( l, desde, posPivote );  // Ponemos el pivote al principio
			int pivote = l[desde];
			int menoresOIguales = desde+1;
			int mayores = hasta;
			if (DEBUG) visuProgresoQSParcial( l, -1, -1, desde, hasta, "Dividiendo: pivote " + pivote );
			do {
				// Mover ref de menores o iguales hasta encontrar un mayor o cruzar refs
				while ((menoresOIguales <= mayores) && pivote >= l[menoresOIguales])  {
					menoresOIguales++;
					if (DEBUG) visuProgresoQSParcial( l, menoresOIguales, -1, desde, hasta, "Pivote: " + pivote + " (sube menores)" );
				}
				// Mover ref de mayores hasta encontrar un menor o cruzar refs
				while ((menoresOIguales <= mayores) && pivote < l[mayores]) {
					mayores--;
					if (DEBUG) visuProgresoQSParcial( l, mayores, -1, desde, hasta, "Pivote: " + pivote + " (baja mayores)" );
				}
				// Si no se han cruzado aún, intercambiar los elementos
				if (menoresOIguales < mayores) {
					intercambiarEnArray( l, menoresOIguales, mayores );
					if (DEBUG) visuProgresoQSParcial( l, -1, -1, desde, hasta, "Intercambio " + l[menoresOIguales] + " y " + l[mayores] );
				}
			} while (menoresOIguales <= mayores);
			// Intercambiar elemento pivote con el del punto de división
			intercambiarEnArray( l, desde, mayores );
			if (DEBUG) visuProgresoQSParcial( l, -1, -1, desde, hasta, "Intercambio pivote con " + l[desde] );
			return mayores;  // Se devuelve la referencia del pivote
		}
		
		// Auxiliares para ver quickSort lo que hace
		private static void visuProgresoQS( int[] l, int marca, int marcaInt, int desde, int hasta, String mens ) {
			for (int i=0; i<prof; i++) System.out.print( "  " );
			salida.print( "   [ ");
			for (int i=0; i<l.length; i++) {
				if (i==marca)
					salida.print( "*" + l[i] + "* " );
				else if (i==desde)
					salida.print( "->" + l[i] + " " );
				else if (i==hasta)
					salida.print( l[i] + "<- " );
				else
					salida.print( l[i] + " " );
				if (i==marcaInt)
					salida.print( "<> " );
			}
			if (l.length==marca)
				salida.print( "** " );
			salida.println("]  " + mens);
		}
		private static void visuProgresoQSParcial( int[] l, int marca, int marcaInt, int desde, int hasta, String mens ) {
			for (int i=0; i<prof; i++) System.out.print( "  " );
			salida.print( "   " + desde + " a " + hasta + ": [ ");
			boolean recien = false;
			for (int i=desde; i<=hasta; i++) {
				if (i==marca)
					{ salida.print( "*" + l[i] + "* " ); recien = false; }
				else if (i==desde)
					{ salida.print( "->" + l[i] + " " ); recien = false; }
				else if (i==hasta)
					{ salida.print( l[i] + "<- " ); recien = false; }
				else
					if (hasta-desde<25) { salida.print( l[i] + " " ); recien = false; }
					else { if (!recien) { salida.print( l[i] + " (...) " ); recien = true; } }
				if (i==marcaInt)
					{ salida.print( "<> " ); recien = false; }
			}
			if (l.length==marca)
				salida.print( "** " );
			salida.println("]  " + mens);
		}
			
		
	// Utilidades 
	private static void showArray( int[] v ) {
		System.out.print( "[ " );
		for (int i=0; i<v.length; i++) { System.out.print( v[i] + " " ); if ((i+1)%100==0) {System.out.println(); System.out.print( "  " ); }}
		System.out.println( "]" );
	}
	
	private static int mediana( int[] v, int desde, int hasta ) {
		if (desde>=hasta-1) return desde;    // No hay intermedios
		int menor = desde; int mayor = hasta;
		if (v[mayor] < v[menor]) { menor = mayor; mayor = desde; }
		if (desde==hasta-2) { // hay solo un intermedio
			int medio = desde+1;
			if (v[medio]<v[menor]) return menor;
			if (v[medio]>v[mayor]) return mayor;
			return medio;
		} else { // hay varios intermedios
			int medio1 = desde + (hasta-desde)/3;
			int medio2 = desde + (hasta-desde)/3*2;
			// Poner medio1
			if (v[medio1]<v[menor]) { int temp = menor; menor = medio1; medio1 = temp; }
			else if (v[medio1]>v[mayor]) { int temp = mayor; mayor = medio1; medio1 = temp; }
			// Poner medio2 y decidir cuál se devuelve
			if (v[medio2]<v[menor]) { int temp = menor; menor = medio2; medio2 = temp; }
			else if (v[medio2]>v[mayor]) { int temp = mayor; mayor = medio2; medio2 = temp; }
			else if (v[medio2]<v[medio1]) { int temp = medio1; medio1 = medio2; medio2 = temp; }
			if (v[medio1]-v[menor] < v[mayor]-v[medio2]) return medio2;
			else return medio1;
		}
	}
	
	private static int medianaAleatoria( int[] v, int desde, int hasta ) {
		return desde + r.nextInt(hasta-desde+1);
	}
	
	private static void intercambiarEnArray( int[] v, int i1, int i2 ) {
		int temp = v[i1];
		v[i1] = v[i2];
		v[i2] = temp;
	}

		private static Random r = new Random();
	/** Inicializa un vector v de tamaño n con todos los valores desde 0 hasta n-1, o aleatorios
	 * @param v	Vector a inicializar (debe estar ya creado con el tamaño adecuado)
	 * @param variante	"ordenado", "inverso", "zigzag", "randomsinrep" ... o si no, aleatorio
	 */
	private static void initArray( int[] v, String variante ) {
		if (variante.equalsIgnoreCase("ordenado")) {
			for (int i=0; i<v.length; i++) {
				v[i] = i;
			}
		} else if (variante.equalsIgnoreCase("inverso")) {
			for (int i=0; i<v.length; i++) {
				v[i] = v.length-i;
			}
		} else if (variante.equalsIgnoreCase("zigzag")) {
			for (int i=0; i<v.length/2; i++) {
				v[i*2] = i;
				if (i*2+1<v.length) v[i*2+1] = v.length-i;
			}
		} else if (variante.equalsIgnoreCase("randomsinrep")) {
			ArrayList<Integer> lDatos = new ArrayList<>();
			for (int i=0; i<v.length; i++) lDatos.add( i+100 );
			for (int i=0; i<v.length; i++) {
				int posAleat = r.nextInt( lDatos.size() );
				v[i] = lDatos.get( posAleat );
				lDatos.remove( posAleat );
			}
		} else {  // Random
			for (int i=0; i<v.length; i++) {
				v[i] = r.nextInt( v.length*2 );
			}
		}
	}
	
}
