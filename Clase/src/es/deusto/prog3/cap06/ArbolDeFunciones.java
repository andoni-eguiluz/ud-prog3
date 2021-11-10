package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/** Clase que permite construir árboles generales representados automáticamente en una ventana
 * implementados con un JTree.
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ArbolDeFunciones {

	private static long ESPERA = 1000; 
	
	private class Ventana extends JFrame {
		private JTree2 tree;
		public Ventana( boolean conPausa ) {
			setTitle( "Arbol de funciones" );
			setSize( 640, 480 );
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setLocationRelativeTo( null );
			tree = new JTree2();
			JScrollPane spPrincipal = new JScrollPane(tree);
			getContentPane().add( spPrincipal, BorderLayout.CENTER );
			if (conPausa) {
				JPanel pBotonera = new JPanel();
				JButton bPausa = new JButton( "Pausa" );
				pBotonera.add( bPausa );
				getContentPane().add( pBotonera, BorderLayout.SOUTH );
				bPausa.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton b = (JButton)(e.getSource());
						if (b.getText().equals("Pausa")) {  // Estaba sin pausa - lo ponemos en pausa
							b.setText( "Reanudar" );
							enPausa = true;
						} else {  // Estaba pausado - lo quitamos
							b.setText( "Pausa" );
							enPausa = false;
						}
					}
				});
			}
		}
	}
	
	// Atributos y métodos principales de representación en árbol de llamadas
	
	private DefaultMutableTreeNode raiz;
	private DefaultTreeModel modeloArbol;
	private Ventana ventana;
	private boolean enPausa;
	
	/** Construye un nuevo árbol de representación de funciones o llamadas, que incluye una ventana de representación y la muestra en pantalla
	 * @param textoNodoRaiz	Texto del nodo principal
	 * @param conBotonPausa	Si true se crea un botón de pausa, si no solo sale el árbol
	 */
	public ArbolDeFunciones(String textoNodoRaiz, boolean conBotonPausa) {
		ventana = new Ventana( conBotonPausa );
		raiz = new DefaultMutableTreeNode(textoNodoRaiz);
		modeloArbol = new DefaultTreeModel( raiz );
		ventana.tree.setModel( modeloArbol );
		ventana.setVisible( true );
	}
	/** Crea un nuevo nodo hijo en el árbol
	 * @param texto	Texto del nodo
	 * @param padre	Nodo padre del que crear el nodo hijo. Si es null, se crea como hijo de la raíz principal del árbol
	 * @return	Nodo hijo recién creado
	 */
	public DefaultMutableTreeNode anyadeNodoHijo( String texto, DefaultMutableTreeNode padre ) {
		DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode( texto );
		if (padre==null) {
			raiz.add( nuevo );
			modeloArbol.nodesWereInserted( raiz, new int[] { raiz.getChildCount()-1 } );
		} else {
			padre.add( nuevo );
			modeloArbol.nodesWereInserted( padre, new int[] { padre.getChildCount()-1 } );
		}
		ventana.tree.setES( new TreePath(nuevo.getPath()), true );
		return nuevo;
	}
	/** Crea un nuevo nodo hijo en el árbol
	 * @param texto	Texto nuevo a poner en el nodo
	 * @param nodo	Nodo del que cambiar el texto. Si es null, se modifica el texto de la raíz principal del árbol
	 */
	public void cambiaValorNodo( String texto, DefaultMutableTreeNode nodo ) {
		if (nodo==null) nodo = raiz;
		nodo.setUserObject( texto );
		modeloArbol.nodeChanged(nodo); // Lanza evento de modificación en el modelo
	}

	public boolean isPaused() { return enPausa; }
	
	//
	// Métodos para pruebas de representación en árbol
	//
	
		private static void pruebaAMano() {
			(new Thread() {
				@Override
				public void run() {
					ArbolDeFunciones arbol = new ArbolDeFunciones( "Test Manual", false );
					DefaultMutableTreeNode nodo = arbol.anyadeNodoHijo( "Prueba 1", null );
					arbol.anyadeNodoHijo( "Prueba 2", null );
					DefaultMutableTreeNode n3 = arbol.anyadeNodoHijo( "Prueba 3", nodo );
					arbol.anyadeNodoHijo( "Prueba 4", nodo );
					try { Thread.sleep( 2000 ); } catch (InterruptedException e) { }
					arbol.cambiaValorNodo( "Prueba 3 modificada", n3 );
				}
			}).start();
			
		}
		
			private static int fibonacci( int n, ArbolDeFunciones arbol, DefaultMutableTreeNode padre ) {
				/* parada */ while (arbol.isPaused()) try { Thread.sleep( 500 ); } catch (Exception e) {}
				DefaultMutableTreeNode nuevaLlamada = arbol.anyadeNodoHijo( "fib("+n+")", padre );
				if (n<=2) {
					arbol.cambiaValorNodo( "1 <- fib("+n+")", nuevaLlamada );
					try { Thread.sleep( 500 ); } catch (InterruptedException e) { }
					return 1;
				} else {
					int resultado = fibonacci(n-1,arbol,nuevaLlamada) + fibonacci(n-2,arbol,nuevaLlamada);
					arbol.cambiaValorNodo( resultado + " <- fib("+n+")", nuevaLlamada );
					try { Thread.sleep( 500 ); } catch (InterruptedException e) { }
					return resultado;
				}
			}
		private static void pruebaFibonacci() {
			(new Thread() {
				@Override
				public void run() {
					ArbolDeFunciones arbol = new ArbolDeFunciones( "Test Fibonacci", true );
					System.out.println( "Fibonacci = " + fibonacci(11,arbol,null) );
				}
			}).start();
		}

		
			private static void hanoi( int n, ArbolDeFunciones arbol, DefaultMutableTreeNode padre,
					char origen, char destino, char auxiliar ) {
					/* parada */ while (arbol.isPaused()) try { Thread.sleep( 500 ); } catch (Exception e) {}
				DefaultMutableTreeNode nuevaLlamada = arbol.anyadeNodoHijo( "hanoi("+n+")", padre );
					/* pausita */ try { Thread.sleep( ESPERA ); } catch (InterruptedException e) { }
				if (n==1) {
					arbol.cambiaValorNodo( "hanoi("+n+") -> MUEVE 1 de " + origen + " a " + destino, nuevaLlamada );
						/* pausita */ try { Thread.sleep( ESPERA ); } catch (InterruptedException e) { }
					return;
				} else {
					hanoi(n-1,arbol,nuevaLlamada,origen,auxiliar,destino);
						/* parada */ while (arbol.isPaused()) try { Thread.sleep( 500 ); } catch (Exception e) {}
					arbol.anyadeNodoHijo( "MUEVE " + n + " de " + origen + " a " + destino, nuevaLlamada );
						/* pausita */ try { Thread.sleep( ESPERA ); } catch (InterruptedException e) { }
					hanoi(n-1,arbol,nuevaLlamada,auxiliar,destino,origen);
						/* parada */ while (arbol.isPaused()) try { Thread.sleep( 500 ); } catch (Exception e) {}
					arbol.cambiaValorNodo( "hanoi("+n+") -> FIN", nuevaLlamada );
						/* pausita */ try { Thread.sleep( ESPERA ); } catch (InterruptedException e) { }
					return;
				}
			}
		private static void pruebaHanoi() {
			(new Thread() {
				@Override
				public void run() {
					ArbolDeFunciones arbol = new ArbolDeFunciones( "Test Hanoi", true );
					hanoi(7,arbol,null,'a','c','b');
				}
			}).start();
		}
	
		
			private static void visuArrayList( Object al, ArbolDeFunciones arbol, DefaultMutableTreeNode padre ) {
				if (al!=null) {
					DefaultMutableTreeNode nuevoVal = arbol.anyadeNodoHijo( al.toString(), padre ); 
					if (al instanceof ArrayList)
						for (Object o : (ArrayList)al) {
							visuArrayList(o,arbol,nuevoVal);
						}
				}
			}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static void pruebaArrayList() {
			ArrayList l1 = new ArrayList();
			ArrayList l1a = new ArrayList();
			ArrayList l1b = new ArrayList();
			ArrayList l1b2 = new ArrayList();
			l1a.add( "Marta" ); l1a.add( "Luis" ); l1a.add( "Mikel" );
			l1b2.add( "Yolanda" ); l1b2.add( "Aitziber" );
			l1b.add( "Florencia" ); l1b.add( "Robustiano" ); l1b.add( l1b2 );
			l1.add( "Jaime" ); l1.add( l1a ); l1.add( l1b );
			ArbolDeFunciones arbol = new ArbolDeFunciones( "Test ArrayList", false );
			visuArrayList(l1,arbol,null);
		}
		
	// Llamadas a las pruebas
		
	public static void main(String[] args) {
		JFrame vP = new JFrame();
		vP.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );  // Los hilos de fib/hanoi no están programados para pararse. Paramos desde la ventana principal todo 
		JPanel p = new JPanel();
		p.setLayout( new GridLayout(10,1) );
		vP.getContentPane().add( p, BorderLayout.CENTER );
		JButton b;
		p.add( b = new JButton( "Visualización arraylist") );
			b.addActionListener( (e) -> { pruebaArrayList(); } ); 
		p.add( b = new JButton( "JTree manual") );
			b.addActionListener( (e) -> { pruebaAMano(); } );
		p.add( b = new JButton( "Hanoi") );
			b.addActionListener( (e) -> { pruebaHanoi(); } );
		p.add( b = new JButton( "Fibonacci") );
			b.addActionListener( (e) -> { pruebaFibonacci(); } );  // ver clase JPanelConFondo
		vP.pack();
		vP.setVisible( true );
	}

}

/** Clase de utilidad que expone de forma pública el método #setES
 * (equivalente al #setExpandedState, que es protegido en JTree)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
class JTree2 extends JTree {
	@Override
	protected void setExpandedState(TreePath path, boolean state) {
		super.setExpandedState(path, state);
	}
	
	public void setES(TreePath path, boolean state) {
		setExpandedState(path, state);
	}
}
