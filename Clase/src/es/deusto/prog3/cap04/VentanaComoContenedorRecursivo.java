package es.deusto.prog3.cap04;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;

/** Clase que permite visualizar estructura recursiva de ventana a título ilustrativo (aspectos de ventanas corresponden al capítulo 6).
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class VentanaComoContenedorRecursivo {

	/** Método principal - definirlo con la ventana cuya estructura se quiere visualizar
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		VentanaBancoDePruebas v = new VentanaBancoDePruebas();
		v.setVisible( true );
		visualizarEstructuraVentana( v );
	}

	@SuppressWarnings("serial")
	private class Ventana extends JFrame {
		private JTree2 tree;
		public Ventana( boolean conPausa ) {
			setTitle( "Información jerárquica" );
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
	public VentanaComoContenedorRecursivo(String textoNodoRaiz, boolean conBotonPausa) {
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
	
			// Devuelve una descripción del componente en formato String
			private static String descComponente( Component comp ) {
				return comp.getClass().getSimpleName();
			}
		
			private static void recorreVentana( Component comp, VentanaComoContenedorRecursivo arbol, DefaultMutableTreeNode padre ) {
				/* parada */ while (arbol.isPaused()) try { Thread.sleep( 500 ); } catch (Exception e) {}
				DefaultMutableTreeNode nuevoComp = arbol.anyadeNodoHijo( descComponente(comp), padre );
				if (comp instanceof Container) {
					Component[] lc = ((Container)comp).getComponents();
					if (lc.length > 0) {
						for (Component c : lc) {
							recorreVentana( c, arbol, nuevoComp );
						}
						arbol.cambiaValorNodo( descComponente(comp) + " (" + lc.length + " comps.)", nuevoComp );
					}
				}
			}
			
		public static void visualizarEstructuraVentana( JFrame vent ) {
			(new Thread() {
				@Override
				public void run() {
					VentanaComoContenedorRecursivo arbol = new VentanaComoContenedorRecursivo( "Estructura de ventana", false );
					arbol.ventana.setSize( 600, 1000 );
					arbol.ventana.setLocationRelativeTo( null );
					recorreVentana( vent.getContentPane(), arbol, null );
				}
			}).start();
		}	
		
	/** Clase de utilidad que expone de forma pública el método #setES
	 * (equivalente al #setExpandedState, que es protegido en JTree)
	 * @author andoni.eguiluz @ ingenieria.deusto.es
	 */
	@SuppressWarnings("serial")
	private class JTree2 extends JTree {
		@Override
		protected void setExpandedState(TreePath path, boolean state) {
			super.setExpandedState(path, state);
		}
		
		public void setES(TreePath path, boolean state) {
			setExpandedState(path, state);
		}
	}	
}
