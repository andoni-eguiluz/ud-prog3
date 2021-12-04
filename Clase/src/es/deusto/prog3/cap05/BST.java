package es.deusto.prog3.cap05;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.*;

/** Ejemplo de BST (Binary Search Tree) sobre cualquier tipo base Comparable
 * Incluye visualización de árbol en ventana
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class BST<T extends Comparable<T>> {
	NodoBST<T> raiz;
	
	@SuppressWarnings("unchecked")
	public void insertar( T... nuevos ) {
		for (T nuevo : nuevos) insertarRec( null, raiz, nuevo );
	}
	public void insertar( T nuevo ) {
		insertarRec( null, raiz, nuevo );
	}
		private void insertarRec( NodoBST<T> padre, NodoBST<T> bst, T nuevo ) {
			if (bst==null) {  // Caso base: hay que insertar
				NodoBST<T> nuevoNodo = new NodoBST<T>( nuevo, null, null );
				if (padre==null) // Insertar en raíz
					raiz = nuevoNodo;
				else if (padre.elemento.compareTo(nuevo)<0)
					padre.derecho = nuevoNodo;
				else
					padre.izquierdo = nuevoNodo;
			} else {  // Caso general
				int comp = (bst.elemento.compareTo(nuevo));
				if (comp==0) // Caso base: elemento ya existe -se podría insertar iz o de pero siempre igual. O no insertar si no se permiten repeticiones (es lo que hacemos ahora)
					; // Nada que hacer - retorno
				else if (comp<0) // Insertar a la derecha
					insertarRec( bst, bst.derecho, nuevo );
				else
					insertarRec( bst, bst.izquierdo, nuevo );
			}
		}
		
	/** Busca un elemento en el árbol
	 * @param aBuscar	Elemento a buscar
	 * @return	true si está, false en caso contrario
	 */
	public boolean contains( T aBuscar ) {
		return contains( raiz, aBuscar );
	}
		private boolean contains( NodoBST<T> nodo, T aBuscar ) {
			if (nodo==null) {
				return false;
			} else if (nodo.elemento.compareTo( aBuscar ) == 0) {
				return true;
			} else if (nodo.elemento.compareTo( aBuscar ) < 0) {
				return contains( nodo.derecho, aBuscar ); 
			} else {
				return contains( nodo.izquierdo, aBuscar );
			}
		}
	
	/** Busca y recupera un elemento en el árbol
	 * @param aBuscar	Elemento a buscar
	 * @return	el elemento del árbol si está, null en caso contrario
	 */
	public T get( T aBuscar ) {
		return get( raiz, aBuscar );
	}
		private T get( NodoBST<T> nodo, T aBuscar ) {
			if (nodo==null) {
				return null;
			} else if (nodo.elemento.compareTo( aBuscar ) == 0) {
				return nodo.elemento;
			} else if (nodo.elemento.compareTo( aBuscar ) < 0) {
				return get( nodo.derecho, aBuscar ); 
			} else {
				return get( nodo.izquierdo, aBuscar );
			}
		}
	
	public void recorrerInOrden( Consumer<T> c ) {
		recorrerIOrec( raiz, c );
	}
		private void recorrerIOrec( NodoBST<T> nodo, Consumer<T> c ) {
			if (nodo!=null) {   // Si no caso base
				marca( nodo, true );  // Marca visual de recorrido (solo para ejecutar paso a paso e ir viendo lo que ocurre: no afecta al algoritmo)
				recorrerIOrec( nodo.izquierdo, c );
				c.accept( nodo.elemento );
				recorrerIOrec( nodo.derecho, c );
				marca( nodo, false );  // Acaba marca visual de recorrido
			}
		}
	
	public void recorrerPreOrden( Consumer<T> c ) {
		recorrerPOrec( raiz, c );
	}
		private void recorrerPOrec( NodoBST<T> nodo, Consumer<T> c ) {
			if (nodo!=null) {   // Si no caso base
				c.accept( nodo.elemento );
				recorrerPOrec( nodo.izquierdo, c );
				recorrerPOrec( nodo.derecho, c );
			}
		}
	
	public void recorrerPostOrden( Consumer<T> c ) {
		recorrerPtOrec( raiz, c );
	}
		private void recorrerPtOrec( NodoBST<T> nodo, Consumer<T> c ) {
			if (nodo!=null) {   // Si no caso base
				recorrerPtOrec( nodo.izquierdo, c );
				recorrerPtOrec( nodo.derecho, c );
				c.accept( nodo.elemento );
			}
		}
	
	public void recorrerAnchura( Consumer<T> c ) {
		for (int nivel = 0; nivel<altura(); nivel++) 
			recorrerAncrec( raiz, c, nivel );
	}
		private void recorrerAncrec( NodoBST<T> nodo, Consumer<T> c, int nivel ) {
			if (nodo!=null && nivel>=0) {   // Si no caso base
				recorrerAncrec( nodo.izquierdo, c, nivel-1 );
				if (nivel==0)
					c.accept( nodo.elemento );
				recorrerAncrec( nodo.derecho, c, nivel-1 );
			}
		}
		
	public int altura() {
		return alturaRec( raiz );
	}
		public int alturaRec( NodoBST<T> bst ) {
			if (bst==null)
				return 0;
			else {
				return 1 + Math.max( alturaRec( bst.izquierdo ), alturaRec( bst.derecho ) );
			}
		}
		
	public int size() {
		return sizeRec( raiz );
	}
		private int sizeRec( NodoBST<T> nodo ) {
			if (nodo==null)
				return 0;
			else
				return 1 + sizeRec( nodo.izquierdo ) + sizeRec( nodo.derecho );
		}

		private volatile ArrayList<StringBuffer> lineas;
	@Override
	public String toString() {
		lineas = new ArrayList<>(); lineas.add( new StringBuffer("") );
		toStringRec( raiz, 0 );
		String ret = "";
		for (StringBuffer linea : lineas) if (!linea.toString().isEmpty()) ret += (linea + "\n");
		return ret;
	}
		private void toStringRec( NodoBST<T> nodo, int nivel ) {
			if (nodo!=null) {   // Si no caso base
				if (lineas.size() <= nivel+1) lineas.add( new StringBuffer("") ); 
				toStringRec( nodo.izquierdo, nivel+1 );
				int largoInferior = lineas.get(nivel+1).length();
				toStringRec( nodo.derecho, nivel+1 );
				rellenaEspacios( nivel, largoInferior, lineas.get(nivel+1).length(), nodo.elemento.toString() );
			}
		}
		private void rellenaEspacios( int nivel, int ancho1, int ancho2, String elem ) {
			int faltanEspacios = (ancho1 + ancho2) / 2;
			faltanEspacios = faltanEspacios - lineas.get(nivel).length();
			for (int i=0; i<faltanEspacios-1; i++) lineas.get(nivel).append( " " );
			lineas.get(nivel).append( elem ); lineas.get(nivel).append( " " );
		}
	
	private static BST<Integer>.VentanaArbol v;
	public static void main(String[] args) {
		BST<Integer> bst = new BST<>();
		bst.insertar( 6, 4, 9, 1, 5, 7, 10 );
		// Si se quiere sacarlo en consola: System.out.print( bst );
		// Se saca dibujado en ventana
		v = bst.new VentanaArbol();
		v.setAlwaysOnTop(true);
		v.setVisible( true );
		System.out.println( "Altura árbol: " + bst.altura() );
		System.out.print( "Recorrido árbol inorden = { ");
		bst.recorrerInOrden( (Integer i) -> { System.out.print( i + " " ); } );
		// O en sintaxis Java 7
		// bst.recorrerInOrden( new Consumer<Integer>() {
		// 	@Override
		// 	public void accept(Integer t) {
		// 		System.out.print( t + " " );
		// 	}
		// });
		System.out.println( "}");
		System.out.print( "Recorrido árbol preorden = { ");
			bst.recorrerPreOrden( (Integer i) -> { System.out.print( i + " " ); } );
		System.out.println( "}");
		System.out.print( "Recorrido árbol postorden = { ");
			bst.recorrerPostOrden( (Integer i) -> { System.out.print( i + " " ); } );
		System.out.println( "}");
		System.out.print( "Recorrido árbol anchura = { ");
			bst.recorrerAnchura( (Integer i) -> { System.out.print( i + " " ); } );
		System.out.println( "}");
		System.out.println();
		System.out.println( "Obsérvese la diferencia entre los dos árboles" );
		bst = new BST<>();
		bst.insertar( 1, 4, 5, 6, 7, 9, 10 );
		// Esto cargaría un árbol aleatorio Random r = new Random(); for (int i=0; i<100; i++) { bst.insertar( r.nextInt(100)); }
		v = bst.new VentanaArbol();
		v.setAlwaysOnTop(true);
		v.setVisible( true );
		System.out.println( "Altura árbol: " + bst.altura() );
		System.out.print( "Recorrido árbol inorden = { ");
		bst.recorrerInOrden( (Integer i) -> { System.out.print( i + " " ); } );
		System.out.println( "}");
		System.out.print( "Recorrido árbol preorden = { ");
			bst.recorrerPreOrden( (Integer i) -> { System.out.print( i + " " ); } );
		System.out.println( "}");
		System.out.print( "Recorrido árbol postorden = { ");
			bst.recorrerPostOrden( (Integer i) -> { System.out.print( i + " " ); } );
		System.out.println( "}");
		System.out.print( "Recorrido árbol anchura = { ");
			bst.recorrerAnchura( (Integer i) -> { System.out.print( i + " " ); } );
		System.out.println( "}");
	}
	
	private void marca( NodoBST<T> nodo, boolean marca ) {
		nodo.marca = marca;
		v.repaint();
	}
	
	/** Ventana que visualiza el árbol con los valores que tenga en cada momento
	 * @author andoni.eguiluz @ ingenieria.deusto.es
	 */
	@SuppressWarnings("serial")
	public class VentanaArbol extends JFrame {
		private int ANCH_NODO = 60;  // Píxels por nodo
		private int ANCH_REF = 15;  // Píxels por referencia (puntero del nodo)
		private int ALT_NODO = 20;  // Píxels alto por nodo
		private int ALT_NIVEL = 40;  // Píxels entre nivel y nivel del árbol
		private int MIN_LATERAL = 10;  // Píxels mínimos entre nodos 
		private int ALT_TEXTO = 15;  // Píxels alto de texto
		private Font font = new Font( "Arial", Font.PLAIN, 14 );
		VentanaArbol() {
			super( "Visualización de BST" );
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 800, 600 );
			add( new JPanel() {
				{
					setBackground( Color.white );
				}
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					dibujaRec( raiz, (Graphics2D) g, 20, 20, new ArrayList<Integer>(), 0, new Point(-1,-1) );
				}
				private int dibujaRec( NodoBST<T> nodo, Graphics2D g, int xActual, int yActual, ArrayList<Integer> xNiveles, int nivel, Point pDibujado ) {
					if (nodo==null) return xActual; // Caso base
					// Dibuja recursivamente por la izquierda
					xActual = dibujaRec( nodo.izquierdo, g, xActual, yActual + ALT_NIVEL, xNiveles, nivel+1, pDibujado );
					// Calcula x de nodo raíz
					if (xNiveles.size()>nivel && xNiveles.get(nivel)>xActual) xActual = xNiveles.get(nivel);  // Calcula x de dibujado para que no haya solapamiento
					if (nodo.izquierdo!=null && pDibujado.x + ANCH_NODO/2 + MIN_LATERAL/2 > xActual) xActual = pDibujado.x + ANCH_NODO/2 + MIN_LATERAL/2;  // Calcula x de dibujado para que las flechas izquierdas al hijo queden siempre hacia el lado izquierdo
					while (xNiveles.size()<=nivel) xNiveles.add( new Integer(0) );
					if (nivel>0 && xNiveles.get(nivel-1)-ANCH_NODO/2-MIN_LATERAL/2 > xActual) xActual = xNiveles.get(nivel-1)-ANCH_NODO/2-MIN_LATERAL/2;  // Calcula x de dibujado para que las flechas derechas al hijo queden siempre hacia el lado derecho
					int miX = xActual;  // Guarda x donde se dibujará el nodo
					xNiveles.set( nivel, miX + ANCH_NODO + MIN_LATERAL );
					int xDibujadoIzq = pDibujado.x;  // Guarda x del nodo dibujado por la izquierda
					// Dibuja recursivamente por la derecha
					xActual = dibujaRec( nodo.derecho, g, xActual + MIN_LATERAL, yActual + ALT_NIVEL, xNiveles, nivel+1, pDibujado );
					// Dibuja nodo y enlaces
					if (nodo.marca) g.setColor( Color.YELLOW ); else g.setColor( Color.WHITE );  // Fondo del nodo (si está marcado, amarillo)
					g.fillRect(miX, yActual, ANCH_NODO, ALT_NODO );
					g.setColor( Color.BLACK );
					g.drawRect( miX, yActual, ANCH_NODO, ALT_NODO );
					g.drawRect( miX, yActual, ANCH_REF, ALT_NODO );
					g.drawRect( miX + ANCH_NODO - ANCH_REF, yActual, ANCH_REF, ALT_NODO );
					g.setColor( Color.BLUE );
					if (nodo.izquierdo==null) g.drawLine( miX, yActual + ALT_NODO, miX + ANCH_REF, yActual );
					else dibuFlecha( g, miX + ANCH_REF/2, yActual + ALT_NODO/2, xDibujadoIzq + ANCH_NODO/2, pDibujado.y );
					if (nodo.derecho==null) g.drawLine( miX + ANCH_NODO - ANCH_REF, yActual + ALT_NODO, miX + ANCH_NODO, yActual );
					else dibuFlecha( g, miX + ANCH_NODO - ANCH_REF/2, yActual + ALT_NODO/2, pDibujado.x + ANCH_NODO/2, pDibujado.y );
					g.setColor( Color.MAGENTA );
					drawStringCentrado( g, nodo.elemento.toString(), new Rectangle( miX + ANCH_REF, yActual, ANCH_NODO-2*ANCH_REF, ALT_TEXTO ), font );
					// Calcula punto a devolver
					pDibujado.setLocation( miX, yActual );
					return xActual;
				}
				private void dibuFlecha( Graphics2D g, int x1, int y1, int x2, int y2 ) {
					g.drawLine(x1, y1, x2, y2);
					g.fillOval(x1-3, y1-3, 6, 6);
					double ang = Math.atan2( y1-y2, x1-x2 );
					int x3 = (int) Math.round(x2 + 10*Math.cos(ang+0.3));
					int y3 = (int) Math.round(y2 + 10*Math.sin(ang+0.3));
					int x4 = (int) Math.round(x2 + 10*Math.cos(ang-0.3));
					int y4 = (int) Math.round(y2 + 10*Math.sin(ang-0.3));
					g.fillPolygon( new int[]{x2,x3,x4}, new int[]{y2,y3,y4}, 3 );
				}
				private void drawStringCentrado(Graphics g, String text, Rectangle rect, Font font) {
				    FontMetrics metrics = g.getFontMetrics(font); // Mirar las métricas del tipo de letra
				    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2; // Coordenada X centrada para el texto
				    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + 4; // Idem y (añadimos ascent y un offset para que no se pegue arriba)
					g.setFont( font );
				    g.drawString(text, x, y);
				}
			});
		}
	}
}

class NodoBST<T extends Comparable<T>> {
	T elemento;
	NodoBST<T> izquierdo;
	NodoBST<T> derecho;
	boolean marca; // Atributo utilizado para marcar visualmente recorridos
	public NodoBST(T elemento, NodoBST<T> izquierdo, NodoBST<T> derecho) {
		this.elemento = elemento;
		this.izquierdo = izquierdo;
		this.derecho = derecho;
	}
	
}
