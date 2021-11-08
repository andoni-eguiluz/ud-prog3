package es.deusto.prog3.cap04;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.deusto.prog3.utils.VisualizaProceso;

/** Ejemplo de algoritmo quicksort recursivo
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class QuickSortEnVentana {

	private static int[] v;
	private static int TAM_ARRAY = 30;
	private static VisualizaProceso visual;  // Muestra información de ejecución del algoritmo
	private transient static int prof, profMax;       // Variables de debug
	private static int posPivote;         // Variables hechas globales para visualizar la evolución del quicksort
	private static int menoresOIguales;   // id
	private static int mayores;           // id
	private static int desde;             // id
	private static int hasta;             // id
	
	public static void main(String[] args) {
		visualizar();
		visualVariables( -1, -1, -1, 0, TAM_ARRAY-1 );
		v = new int[TAM_ARRAY];
		initArray( v, "randomsinrep" );
		visual.hazPausa( "Array inicial antes de ordenar" );
		quickSort( v );
		visual.setRunningMode( 0 );
		visualVariables( -1, -1, -1, 0, TAM_ARRAY-1 );
		visual.hazPausa( "Tamaño " + v.length + " -> prof.rec.máxima " + profMax );
		visual.hazPausa( "Array después de ordenar" );
		visual.kill();
	}
	
		private static JPanel panelQS;
		private static JLabel[][] lQS;
		private static void visualizar() {
			visual = new VisualizaProceso();
			visual.getVentana().setSize( 1000, 400 );
			panelQS = new JPanel();
			panelQS.setLayout( new GridLayout( 2, TAM_ARRAY ));
			lQS = new JLabel[2][TAM_ARRAY];
			Font font = new Font( "Arial", Font.BOLD, 18 );
			for (int fila=0; fila<2; fila++) {
				for (int col=0; col<TAM_ARRAY; col++) {
					JLabel l = new JLabel( " ", JLabel.CENTER );
					l.setOpaque( true );
					l.setFont( font );
					if (fila==0) l.setBackground( Color.white );
					if (fila==0) l.setBorder( BorderFactory.createLineBorder( Color.black, 2 ));
					lQS[fila][col] = l;
					panelQS.add( l );
				}
			}
			visual.setProcesoRepintado( () -> {
				for (int col=0; col<TAM_ARRAY; col++) {
					lQS[0][col].setText( v[col] + "" );
					if (desde!=-1 && col>=desde && col<=hasta) {
						lQS[0][col].setBackground( Color.cyan );
					} else {
						lQS[0][col].setBackground( Color.white );
					}
					if (col==posPivote) {
						lQS[0][col].setBorder( BorderFactory.createLineBorder( Color.red, 4 ));
					} else {
						lQS[0][col].setBorder( BorderFactory.createLineBorder( Color.black, 2 ));
					}
				}
				for (int col=0; col<TAM_ARRAY; col++) {
					if (col==menoresOIguales && col==mayores) {
						lQS[1][col].setBackground( Color.orange );
						lQS[1][col].setText( "<=>" );
					} else if (col==menoresOIguales) {
						lQS[1][col].setBackground( Color.green );
						lQS[1][col].setText( "<=" );
					} else if (col==mayores) {
						lQS[1][col].setBackground( Color.red );
						lQS[1][col].setText( ">" );
					} else {
						lQS[1][col].setBackground( Color.lightGray );
						lQS[1][col].setText( " " );
					}
				}
			});
			visual.getPanelPrincipal().setLayout( new BorderLayout() );
			visual.getPanelPrincipal().add( panelQS, BorderLayout.CENTER );
		}
		private static void visualVariables( int posPivote, int menoresOIguales, int mayores, int desde, int hasta ) {
			QuickSortEnVentana.posPivote = posPivote;
			QuickSortEnVentana.menoresOIguales = menoresOIguales;
			QuickSortEnVentana.mayores = mayores;
			QuickSortEnVentana.desde = desde;
			QuickSortEnVentana.hasta = hasta;			
		}
	
		static void quickSort( int[] l ) {
			prof = 0; profMax = 0;  // Para depuración
			try {
				quickSort( l, 0, l.length-1 );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		static void quickSort( int[] l, int desde, int hasta ) {
			prof++; if (prof>profMax) profMax = prof;  // Para depuración
			if (desde < hasta) {
				// Si hay más de un elemento por ordenar,
				// Dividir el vector en dos obteniendo el punto de corte
				int corte = quickSortDividir( l, desde, hasta );
				// Recursivamente, ordenar las dos partes (el pivote ya queda ordenado)
				quickSort( l, desde, corte-1 );
				quickSort( l, corte+1, hasta );
			} // caso base: desde>=hasta  (nada que hacer)
			prof--;  // Para depuración
		}
		static int quickSortDividir( int[] l, int desde, int hasta ) {
			int posPivote = mediana(l,desde,hasta);  // Cogemos de pivote el intermedio entre primero, intermedio y mayor. Podría cogerse aleatoriamente o el primero... cuanto más centrado de valor salga el pivote, mejor.
				// Variable hecha global para visualizar el proceso del Quicksort
			// int posPivote = medianaAleatoria(l,desde,hasta);  // Esto cogería el pivote aleatorio
			visual.ponMensaje( "Ordenando array entre " + desde + " ("+v[desde]+") " + " y " + hasta + " ("+v[hasta]+") " + " -> Pivote " + posPivote + " ("+v[posPivote]+")" ); 
			if (desde!=posPivote) intercambiarEnArray( l, desde, posPivote );  // Ponemos el pivote al principio
			int pivote = l[desde]; posPivote = desde;
			int menoresOIguales = desde+1;
			int mayores = hasta;
			visualVariables( posPivote, menoresOIguales, mayores, desde, hasta );
			visual.hazPausa( "Dividiendo: pivote " + pivote );
			do {
				// Mover ref de menores o iguales hasta encontrar un mayor o cruzar refs
				while ((menoresOIguales <= mayores) && pivote >= l[menoresOIguales])  {
					menoresOIguales++;
					visualVariables( posPivote, menoresOIguales, mayores, desde, hasta );
					visual.hazPausa( "Pivote: " + pivote + " (sube menores)" );
				}
				// Mover ref de mayores hasta encontrar un menor o cruzar refs
				while ((menoresOIguales <= mayores) && pivote < l[mayores]) {
					mayores--;
					visualVariables( posPivote, menoresOIguales, mayores, desde, hasta );
					visual.hazPausa( "Pivote: " + pivote + " (baja mayores)" );
				}
				// Si no se han cruzado aún, intercambiar los elementos
				if (menoresOIguales < mayores) {
					intercambiarEnArray( l, menoresOIguales, mayores );
					visualVariables( posPivote, menoresOIguales, mayores, desde, hasta );
					visual.hazPausa( "Intercambio " + l[menoresOIguales] + " y " + l[mayores] );
				}
			} while (menoresOIguales <= mayores);
			// Intercambiar elemento pivote con el del punto de división
			intercambiarEnArray( l, desde, mayores );
			visualVariables( posPivote, menoresOIguales, mayores, desde, hasta );
			visual.hazPausa( "Intercambio pivote con " + l[desde] );
			return mayores;  // Se devuelve la referencia del pivote
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
			for (int i=0; i<v.length; i++) lDatos.add( i );
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
