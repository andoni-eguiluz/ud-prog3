package es.deusto.prog3.cap05;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;

import es.deusto.prog3.utils.ventanas.ventanaBitmap.VentanaGrafica;

/** Animación de concepto de BST surgiendo de la búsqueda binaria en un vector ordenado
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class BSTdesdeBusquedaBinaria {
	private static int OFFSET_X = 25;
	private static int ANCHO_NODO = 30;
	private static int ALTO_NODO = 16;
	private static int NUM_NODOS = 31;
	private static int Y_PARTIDA = 300;
	private static int Y_POR_NIVEL = 30;
	private static Font TIPO_LETRA = new Font( "Arial", Font.PLAIN, 12 );
	
	public static void main(String[] args) {
		// Creación de ventana gráfica
		final VentanaGrafica v = new VentanaGrafica( 1000, 800, "Concepto de BST - Búsqueda binaria" );
		v.setDibujadoInmediato( false );
		// Creación de árbol equilibrado con posición plana
		final BST<NumeroConPosicion> bst = new BST<NumeroConPosicion>();
		init( bst, NUM_NODOS, ANCHO_NODO );
		// Evento de redimensión de ventana
		v.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				visualizaBST( bst, v, ANCHO_NODO, ALTO_NODO );
			}
		});
		// Visualización de árbol
		visualizaBST( bst, v, ANCHO_NODO, ALTO_NODO );
		// Animación
		// 1.- Construye la primera lista de nodos a animar (el del medio)
		ArrayList<NumeroConPosicion> nodosAnimAnts = new ArrayList<NumeroConPosicion>();
		ArrayList<NumeroConPosicion> nodosAnim = new ArrayList<NumeroConPosicion>();
		NumeroConPosicion ncp = bst.get( new NumeroConPosicion(NUM_NODOS/2, 0, 0) );
		nodosAnim.add( ncp );
		do {
			System.out.println( "Animando los nodos " + nodosAnim );
			System.out.println( "    (Esperando a pulsación de barra espaciadora)" );
			while (!v.isTeclaPulsada( KeyEvent.VK_SPACE ));
			// 2.- Anima los nodos indicados
			for (int i=0; i<Y_POR_NIVEL; i++) {
				for (NumeroConPosicion nodo : nodosAnim) nodo.y--;
				for (NumeroConPosicion nodo : nodosAnimAnts) nodo.y--;
				visualizaBST( bst, v, ANCHO_NODO, ALTO_NODO );
				v.espera( 25 );
			}
			// 3.- Calcula los próximos nodos a animar (los referenciados por los anteriores)
			int espacio = nodosAnim.get(0).numero / 2;
			if (espacio<1) { // Si los nodos son los últimos ya no se animan
				nodosAnim.clear();
			} else {
				ArrayList<NumeroConPosicion> nuevos = new ArrayList<>();
				for (NumeroConPosicion nodo : nodosAnim) {
					ncp = bst.get( new NumeroConPosicion(nodo.numero-espacio-1, 0, 0) );
					nuevos.add( ncp );
					ncp = bst.get( new NumeroConPosicion(nodo.numero+espacio+1, 0, 0) );
					nuevos.add( ncp );
				}
				nodosAnimAnts.addAll( nodosAnim );
				nodosAnim = nuevos;
			}
		} while (!nodosAnim.isEmpty());  // Hasta que los nodos a animar sean los últimos
	}

	private static void init( BST<NumeroConPosicion> bst, int tam, int anchNodo ) {
		initRec( bst, anchNodo, 0, tam-1 );  // Inicializa un árbol equilibradamente - insertando siempre antes el punto medio
	}
		private static void initRec( BST<NumeroConPosicion> bst, int anchNodo, int desde, int hasta ) {
			if (desde>hasta) {
				// Caso base vacío
			} else if (desde==hasta) {
				bst.insertar( new NumeroConPosicion( desde, anchNodo*desde, Y_PARTIDA ) );  // Caso base inserción hoja
			} else {
				int medio = (desde+hasta) / 2;
				bst.insertar( new NumeroConPosicion( medio, anchNodo*medio, Y_PARTIDA ) );
				initRec( bst, anchNodo, desde, medio-1 );
				initRec( bst, anchNodo, medio+1, hasta );
			}
		}

	
	/** Visualiza un árbol esquemáticamente en una ventana gráfica
	 * @param bst	BST a visualizar
	 * @param v	Ventana donde visualizarlo
	 * @param anchoNodo	Anchura de cada nodo en píxels
	 * @param altoNodo	Altura de cada nodo en píxels
	 */
	private static void visualizaBST( BST<NumeroConPosicion> bst, VentanaGrafica v, int anchoNodo, int altoNodo ) {
		v.borra();
		v.repaint();
		visualizaBSTRec( bst.raiz, v, anchoNodo, altoNodo );
		v.repaint();
	}
		private static void visualizaBSTRec( NodoBST<NumeroConPosicion> nodo, VentanaGrafica v, int anchoNodo, int altoNodo ) {
			if (nodo==null) return;
			visualizaBSTRec( nodo.izquierdo, v, anchoNodo, altoNodo );
			visualizaBSTRec( nodo.derecho, v, anchoNodo, altoNodo );
			if (nodo.izquierdo!=null) {
				if (nodo.izquierdo.elemento.y > nodo.elemento.y)
					v.dibujaFlecha( OFFSET_X + nodo.elemento.x + anchoNodo/2, nodo.elemento.y + altoNodo/2, 
						OFFSET_X + nodo.izquierdo.elemento.x + anchoNodo/2, nodo.izquierdo.elemento.y, 
						0.75f, Color.BLUE, 5 );
			}
			if (nodo.derecho!=null) {
				if (nodo.derecho.elemento.y > nodo.elemento.y)
					v.dibujaFlecha( OFFSET_X + nodo.elemento.x + anchoNodo/2, nodo.elemento.y + altoNodo/2, 
						OFFSET_X + nodo.derecho.elemento.x + anchoNodo/2, nodo.derecho.elemento.y, 
						0.75f, Color.BLUE, 5 );
			}
			v.dibujaRect( OFFSET_X+nodo.elemento.x, nodo.elemento.y, anchoNodo, altoNodo, 1.5f, Color.GREEN, Color.WHITE );
			v.dibujaTexto( OFFSET_X+nodo.elemento.x + 5, nodo.elemento.y + ALTO_NODO - 3, nodo.elemento.numero+"", TIPO_LETRA, Color.BLACK );
		}
		
	private static class NumeroConPosicion implements Comparable<NumeroConPosicion> {
		int numero;
		int x;
		int y;
		public NumeroConPosicion(int numero, int x, int y) {
			super();
			this.numero = numero;
			this.x = x;
			this.y = y;
		}
		@Override
		public int compareTo(NumeroConPosicion o) {
			return numero - o.numero;
		}
		@Override
		public String toString() {
			return numero+"";
		}
	}
			
}
