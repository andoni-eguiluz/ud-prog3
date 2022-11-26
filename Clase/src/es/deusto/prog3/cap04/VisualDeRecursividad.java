package es.deusto.prog3.cap04;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/** Clase que permite visualizar procesos recursivos en ventana
 * a título ilustrativo (aspectos de ventanas corresponden al capítulo 6).
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class VisualDeRecursividad {

	private static long ESPERA = 1000; 
	private static int font = 12;
	
	@SuppressWarnings("serial")
	private class Ventana extends JFrame {
		private JTree2 tree;
		public Ventana( boolean conPausa ) {
			setTitle( "Arbol de funciones" );
			setSize( 640, 480 );
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setLocationRelativeTo( null );
			tree = new JTree2();
			tree.addKeyListener( new KeyAdapter() {  // Para ampliar font con Ctrl++
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_PLUS) {
						font += 2;
						tree.setFont( new Font( "Arial", Font.PLAIN, font ) );
						
					}
				}
			});
			JScrollPane spPrincipal = new JScrollPane(tree);
			getContentPane().add( spPrincipal, BorderLayout.CENTER );
			if (conPausa) {
				JPanel pBotonera = new JPanel();
				JButton bPausa = new JButton( "Pausa" );
				pBotonera.add( bPausa );
				JButton bPasoAPaso = new JButton( "1 Paso" );
				pBotonera.add( bPasoAPaso );
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
							unPaso = false;
						}
					}
				});
				bPasoAPaso.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						enPausa = true;
						unPaso = true;
					}
				});
			}
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					enPausa = false;
					unPaso = false;
				}
			});
		}
	}
	
	// Atributos y métodos principales de representación en árbol de llamadas
	
	private DefaultMutableTreeNode raiz;
	private DefaultTreeModel modeloArbol;
	private Ventana ventana;
	private boolean enPausa;
	private boolean unPaso;
	
	/** Construye un nuevo árbol de representación de funciones o llamadas, que incluye una ventana de representación y la muestra en pantalla
	 * @param textoNodoRaiz	Texto del nodo principal
	 * @param conBotonPausa	Si true se crea un botón de pausa, si no solo sale el árbol
	 */
	public VisualDeRecursividad(String textoNodoRaiz, boolean conBotonPausa) {
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

	public boolean isPaused() { 
		if (unPaso) {
			unPaso = false;
			return false;  // La siguiente vez se devolverá true
		}
		return enPausa; 
	}
	
	//
	// Métodos para pruebas de representación en árbol
	//
	
		
			private static int fibonacci( int n, VisualDeRecursividad arbol, DefaultMutableTreeNode padre ) {
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
					VisualDeRecursividad arbol = new VisualDeRecursividad( "Test Fibonacci", true );
					System.out.println( "Fibonacci = " + fibonacci(11,arbol,null) );
				}
			}).start();
		}

		
			private static void hanoi( int n, VisualDeRecursividad arbol, DefaultMutableTreeNode padre,
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
					VisualDeRecursividad arbol = new VisualDeRecursividad( "Test Hanoi", true );
					hanoi(7,arbol,null,'a','c','b');
				}
			}).start();
		}
	
			private static long factorial( int n, VisualDeRecursividad arbol, DefaultMutableTreeNode padre ) {
				/* parada */ while (arbol.isPaused()) try { Thread.sleep( 500 ); } catch (Exception e) {}
				DefaultMutableTreeNode nuevaLlamada = arbol.anyadeNodoHijo( "fact("+n+")", padre );
				if (n==0) {
					arbol.cambiaValorNodo( "1 <- fact("+n+")", nuevaLlamada );
					try { Thread.sleep( 500 ); } catch (InterruptedException e) { }
					return 1;
				} else {
					try { Thread.sleep( 500 ); } catch (InterruptedException e) { }
					long resultado = n * factorial(n-1,arbol,nuevaLlamada);
					arbol.cambiaValorNodo( resultado + " <- fact("+n+")", nuevaLlamada );
					try { Thread.sleep( 500 ); } catch (InterruptedException e) { }
					return resultado;
				}
			}
		private static void pruebaFactorial() {
			(new Thread() {
				@Override
				public void run() {
					VisualDeRecursividad arbol = new VisualDeRecursividad( "Test Factorial", true );
					System.out.println( "Factorial 11 = " + factorial(11,arbol,null) );
				}
			}).start();
		}
		
	// Llamadas a las pruebas
		
	public static void main(String[] args) {
		JFrame vP = new JFrame();
		vP.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );  // Los hilos de fib/hanoi no están programados para pararse. Paramos desde la ventana principal todo 
		JPanel p = new JPanel();
		p.setLayout( new GridLayout(10,1) );
		vP.getContentPane().add( p, BorderLayout.CENTER );
		JButton b;
		p.add( b = new JButton( "Factorial") );
			b.addActionListener( (e) -> { pruebaFactorial(); } );
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
@SuppressWarnings("serial")
class JTree2 extends JTree {
	@Override
	protected void setExpandedState(TreePath path, boolean state) {
		super.setExpandedState(path, state);
	}
	
	public void setES(TreePath path, boolean state) {
		setExpandedState(path, state);
	}
}
