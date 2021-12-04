package es.deusto.prog3.cap05;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import es.deusto.prog3.cap06.pr0506resuelta.ProcesoProbable;
import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;

/** Prueba visual de eficiencia de algoritmos de ordenación
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class PruebaAlgoritmosOrdenacion {

	public static void main(String[] args) {
		String[] pruebas = { "BubbleSort", "MergeSort", "QuickSort" };
		ArrayList<ProcesoProbable> procs = new ArrayList<ProcesoProbable>();
		procs.add( new OrdenarBubbleSort( "zigzag" ) );
		procs.add( new OrdenarMergeSort( "zigzag" ) );
		procs.add( new OrdenarQuickSort( "zigzag" ) );
		VentanaBancoDePruebas vent = new VentanaBancoDePruebas();
		vent.setProcesos( pruebas, procs );
		vent.setTamanyos( 0, 100000, 5000 );
		vent.setVisible( true );
	}

	public static class OrdenarBubbleSort implements ProcesoProbable {
		private int[] v;
		private String variante;
		// 
		/** Crea el objeto de ordenación de array, preparando el tipo de orden inicial del array de acuerdo a la variante indicada. 
		 * @param variante	"ordenado", "inverso", "zigzag" ... si no, aleatorio
		 */
		public OrdenarBubbleSort( String variante ) {
			this.variante = variante;
		}
		@Override
		public void init(int tamanyoTest) {
			v = new int[tamanyoTest];
			initArray(v, variante);
		}
		@Override
		public Object test() {
			for (int i=0; i<v.length-1; i++) {
				for (int j=v.length-1; j>i; j--) {
					if (v[j]<v[j-1]) intercambiarEnArray( v, j-1, j );
				}
			}
			return v;
		}
	}
	
	public static class OrdenarMergeSort implements ProcesoProbable {
		private int[] v;
		private int[] destino;
		private String variante;
		// 
		/** Crea el objeto de ordenación de array, preparando el tipo de orden inicial del array de acuerdo a la variante indicada. 
		 * @param variante	"ordenado", "inverso", "zigzag" ... si no, aleatorio
		 */
		public OrdenarMergeSort( String variante ) {
			this.variante = variante;
		}
		@Override
		public void init(int tamanyoTest) {
			v = new int[tamanyoTest];
			destino = new int[tamanyoTest];  // array temporal necesario para el merge
			initArray(v, variante);
		}
		@Override
		public Object test() {
			mergeSort( v, 0, v.length-1 );
			return this;  // Objeto con los dos arrays, el original y el destino
		}
		
		private void mergeSort( int[] nums, int ini, int fin ) {
			if (ini>=fin) {
				return;  // Caso base, nada que ordenar
			} else {
				int med = (ini+fin)/2;
				mergeSort(nums,ini,med);
				mergeSort(nums,med+1,fin);
				mezclaMergeSort(nums,ini,med,fin);
			}
		}
			// Algoritmo de mezcla (no recursivo)
			// Mezcla en nums las mitades ya ordenadas (ini1 a fin1) con (fin1+1 a fin2)
			private void mezclaMergeSort( int[] nums, int ini1, int fin1, int fin2 ) {
				int initotal = ini1; // Guardamos el inicio
				int ini2 = fin1+1; // Inicio segunda mitad
				// Mezclar las dos mitades. Primero llevarlas mezcladas a un array intermedio:
				// int[] destino = new int[fin1-ini1+fin2-ini2+2];  // No hace falta crearlo, ya hay uno creado
				int posDest = 0;
				while (ini1<=fin1 || ini2<=fin2) {  // Van subiendo ini1 e ini2 hasta acabar (fin1 y fin2)
					// Hay que comparar ini1 con ini2
					boolean menorEsIni1 = true;  // Suponemos en principio que es <= ini1 
					if (ini1>fin1) // No hay ya elementos en la primera mitad
						menorEsIni1 = false; // En este caso no lo es
					else if (ini2<=fin2 && nums[ini1]>nums[ini2])
						menorEsIni1 = false;  // En este caso tampoco
					if (menorEsIni1) { // Si es menor el de la mitad 1 se lleva de 1
						destino[posDest] = nums[ini1];
						ini1++;
					} else {  // Si es menor el de la mitad 2 se lleva de 2
						destino[posDest] = nums[ini2];
						ini2++;
					}
					posDest++;
				}
				// Copiar el array intermedio a la listaOriginal
				posDest = 0;
				for( int i=initotal; i<=fin2; i++ ) {
					nums[i] = destino[posDest];
					posDest++;
				}
			}
	}
	
	public static class OrdenarQuickSort implements ProcesoProbable {
		private int[] v;
		private String variante;
		// 
		/** Crea el objeto de ordenación de array, preparando el tipo de orden inicial del array de acuerdo a la variante indicada. 
		 * @param variante	"ordenado", "inverso", "zigzag" ... si no, aleatorio
		 */
		public OrdenarQuickSort( String variante ) {
			this.variante = variante;
		}
		@Override
		public void init(int tamanyoTest) {
			v = new int[tamanyoTest];
			initArray(v, variante);
			// showArray(v);
		}
		@Override
		public Object test() {
			quickSort( v );
			return v;
		}

			static void quickSort( int[] l ) {
				// num = 0;
				// max = 0;
				try {
					// salida = new PrintStream( "quicksort.log" );
					// visuProgresoQS( l, -1, -1, 0, l.length-1, "Array inicial: " );
					quickSort( l, 0, l.length-1 );
					// System.out.println( "Tamaño " + l.length + " -> rec.máxima " + max );
					// salida.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			// static int num = 0;
			// static int max = 0;
			static void quickSort( int[] l, int desde, int hasta ) {
				// num++;
				// if (num>max) max = num;
				if (desde < hasta) {
					// Si hay más de un elemento por ordenar,
					// Dividir el vector en dos obteniendo el punto de corte
					int corte = quickSortDividir( l, desde, hasta );
					// Recursivamente, ordenar las dos partes (el pivote ya queda ordenado)
					quickSort( l, desde, corte-1 );
					quickSort( l, corte+1, hasta );
				} // caso base: desde>=hasta  (nada que hacer)
				// num--;
			}
			static int quickSortDividir( int[] l, int desde, int hasta ) {
				// int posPivote = mediana(l,desde,hasta);  // Cogemos de pivote el intermedio entre primero, intermedio y mayor. Podría cogerse aleatoriamente o el primero... cuanto más centrado de valor salga el pivote, mejor.
				int posPivote = medianaAleatoria(l,desde,hasta);
				// System.out.println( "Mediana entre " + desde + " ("+v[desde]+") " + " y " + hasta + " ("+v[hasta]+") " + " = " + posPivote + " ("+v[posPivote]+")" ); 
				if (desde!=posPivote) intercambiarEnArray( l, desde, posPivote );  // Ponemos el pivote al principio
				int pivote = l[desde];
				int menoresOIguales = desde+1;
				int mayores = hasta;
				// visuProgresoQSParcial( l, -1, -1, desde, hasta, "Dividiendo: pivote " + pivote );
				do {
					// Mover ref de menores o iguales hasta encontrar un mayor o cruzar refs
					while ((menoresOIguales <= mayores) && pivote >= l[menoresOIguales])  {
						menoresOIguales++;
						// visuProgresoQSParcial( l, menoresOIguales, -1, desde, hasta, "Pivote: " + pivote + " (sube menores)" );
					}
					// Mover ref de mayores hasta encontrar un menor o cruzar refs
					while ((menoresOIguales <= mayores) && pivote < l[mayores]) {
						mayores--;
						// visuProgresoQSParcial( l, mayores, -1, desde, hasta, "Pivote: " + pivote + " (baja mayores)" );
					}
					// Si no se han cruzado aún, intercambiar los elementos
					if (menoresOIguales < mayores) {
						intercambiarEnArray( l, menoresOIguales, mayores );
						// visuProgresoQSParcial( l, -1, -1, desde, hasta, "Intercambio " + l[menoresOIguales] + " y " + l[mayores] );
					}
				} while (menoresOIguales <= mayores);
				// Intercambiar elemento pivote con el del punto de división
				intercambiarEnArray( l, desde, mayores );
				// visuProgresoQSParcial( l, -1, -1, desde, hasta, "Intercambio pivote con " + l[desde] );
				return mayores;  // Se devuelve la referencia del pivote
			}
			
			private static PrintStream salida = System.out;
			// Auxiliares para ver quickSort lo que hace
			@SuppressWarnings("unused")
			private static void visuProgresoQS( int[] l, int marca, int marcaInt, int desde, int hasta, String mens ) {
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
			@SuppressWarnings("unused")
			private static void visuProgresoQSParcial( int[] l, int marca, int marcaInt, int desde, int hasta, String mens ) {
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
						if (!recien) { salida.print( l[i] + " (...) " ); recien = true; }
					if (i==marcaInt)
						{ salida.print( "<> " ); recien = false; }
				}
				if (l.length==marca)
					salida.print( "** " );
				salida.println("]  " + mens);
		}
			
		
	}
	
	// Utilidades 
	@SuppressWarnings("unused")
	private static void showArray( int[] v ) {
		System.out.print( "[ " );
		for (int i=0; i<v.length; i++) { System.out.print( v[i] + " " ); if (i%100==0) {System.out.println(); System.out.print( "  " ); }}
		System.out.println( "]" );
	}
	
	@SuppressWarnings("unused")
	private static void showArray( int[] v, int desde, int hasta ) {
		System.out.print( " -> " + desde + " a " + hasta +": [ " );
		for (int i=desde; i<=hasta; i++) { System.out.print( v[i] + " " ); if (i%100==0) {System.out.println(); System.out.print( "  " ); }}
		System.out.println( "]" );
	}
	
	@SuppressWarnings("unused")
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
		} else {  // Random
			for (int i=0; i<v.length; i++) {
				v[i] = r.nextInt( v.length*2 );
			}
		}
	}
}
