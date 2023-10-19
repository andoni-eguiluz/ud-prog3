package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/** Clase con ejemplo de JTree con cambio de nodo en caliente.
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploJTree {

	public static void main(String[] args) {
		JFrame vP = new JFrame();
		vP.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		vP.pack();
		vP.setVisible( true );
		pruebaAMano();
	}
	
	@SuppressWarnings("serial")
	private class Ventana extends JFrame {
		private JTree2 tree;
		public Ventana( boolean conPausa ) {
			setTitle( "Prueba de JTree" );
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
	public EjemploJTree(String textoNodoRaiz, boolean conBotonPausa) {
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
	
	private static void pruebaAMano() {
		EjemploJTree arbol = new EjemploJTree( "Test Manual", false );
		DefaultMutableTreeNode nodo = arbol.anyadeNodoHijo( "Prueba 1", null );
		arbol.anyadeNodoHijo( "Prueba 2", null );
		DefaultMutableTreeNode n3 = arbol.anyadeNodoHijo( "Prueba 3", nodo );
		arbol.anyadeNodoHijo( "Prueba 4", nodo );
		try { Thread.sleep( 2000 ); } catch (InterruptedException e) { }
		arbol.cambiaValorNodo( "Prueba 3 modificada", n3 );
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
