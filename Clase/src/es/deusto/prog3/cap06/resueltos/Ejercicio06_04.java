package es.deusto.prog3.cap06.resueltos;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Ejercicio06_04 extends JFrame{

	private static Ejercicio06_04 vent;
	public static void main(String[] args) {
		vent = new Ejercicio06_04();
		vent.prueba();
	}
	
	private MiJTree tree;
	private DefaultTreeModel modeloArbol;
	private DefaultMutableTreeNode raiz;
	
	public Ejercicio06_04() {
		setTitle( "Árbol de funciones" );
		setSize( 640, 480 );
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setLocationRelativeTo( null );
		
		tree = new MiJTree();
		JScrollPane spPrincipal = new JScrollPane( tree );
		getContentPane().add( spPrincipal, BorderLayout.CENTER );
		
		JPanel pBotonera = new JPanel();
		getContentPane().add( pBotonera, BorderLayout.SOUTH );
		
		JButton bAnyadirHijo = new JButton( "Añadir hijo" );
		pBotonera.add( bAnyadirHijo );
		JButton bAnyadirHermano = new JButton( "Añadir hermano" );
		pBotonera.add( bAnyadirHermano );
		JButton bBorrar = new JButton( "Borrar nodo" );
		pBotonera.add( bBorrar );
		
		tree.setCellRenderer( new DefaultTreeCellRenderer() {
			private JProgressBar pb = new JProgressBar( 0, 100 );
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
				if (nodo.getUserObject() instanceof Integer) {
					pb.setValue( (Integer) nodo.getUserObject() );
					return pb;
				}
				return c;
			}
		});
		
		tree.setEditable( true );
		tree.setCellEditor( new DefaultTreeCellEditor( tree, (DefaultTreeCellRenderer) tree.getCellRenderer() ) {
			private boolean esEntero = false;
			private Integer valorAnterior;
			@Override
			public boolean isCellEditable(EventObject event) {
				if (event instanceof MouseEvent) {
					if (((MouseEvent)event).getClickCount() > 1) {
						return true;
					}
				}
				return false;
			}
			@Override
			public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
					boolean leaf, int row) {
				DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
				esEntero = nodo.getUserObject() instanceof Integer;
				if (esEntero) {
					valorAnterior = (Integer) nodo.getUserObject();
				}
				return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
			}
			@Override
			public Object getCellEditorValue() {
				if (esEntero) {
					try {
						return Integer.parseInt( super.getCellEditorValue().toString() );
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog( vent, "Error - nodo numérico" );
						return valorAnterior;
					}
				}
				return super.getCellEditorValue();
			}
		});
		
		tree.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==1) {
					TreePath tp = tree.getPathForLocation( e.getX(), e.getY() );
					if (tp != null) {
						DefaultMutableTreeNode nodoSel = (DefaultMutableTreeNode) tp.getLastPathComponent();
						System.out.println( "Nodo seleccionado: " + nodoSel.getUserObject().getClass().getName() + " " + nodoSel.getUserObject() );
					}
				}
			}
		});
		
		bAnyadirHijo.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath tp = tree.getSelectionPath();
				if (tp != null) {
					DefaultMutableTreeNode nodoSel = (DefaultMutableTreeNode) tp.getLastPathComponent();
					crearNodo( "Nuevo", nodoSel, 0 );
				}
			}
		});
		
		bBorrar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath tp = tree.getSelectionPath();
				if (tp != null) {
					DefaultMutableTreeNode nodoSel = (DefaultMutableTreeNode) tp.getLastPathComponent();
					borrarNodo( nodoSel );
				}
			}
		});
		
	}
	
	private void prueba() {
		raiz = new DefaultMutableTreeNode( "Raíz" );
		modeloArbol = new DefaultTreeModel( raiz );
		tree.setModel( modeloArbol );
		
		crearNodo( "Hijo 1", raiz, 0 );
		DefaultMutableTreeNode n2 = crearNodo( "Hijo 2", raiz, 1 );
		crearNodo( new Point(10,20), raiz, 2 );
		crearNodo( Integer.valueOf(4), raiz, 3 );
		crearNodo( "Nieto 2.1", n2, 0 );
		vent.setVisible( true );
	}
	
	private DefaultMutableTreeNode crearNodo( Object dato, DefaultMutableTreeNode nodoPadre, int posi ) {
		DefaultMutableTreeNode nodo1 = new DefaultMutableTreeNode( dato );
		// raiz.add(nodo1);  -- atención, si lo hacemos así el modelo no se entera y no se refresca
		// En ese caso habría que informar explícitamente al modelo del árbol que se ha insertado un nodo, refrescando así la GUI
		// modeloArbol.nodesWereInserted( raiz, new int[] { raiz.getChildCount()-1 } );
		modeloArbol.insertNodeInto( nodo1, nodoPadre, posi ); // Este método hace las dos cosas: inserta y notifica a los escuchadores del modelo
		tree.expandir( new TreePath(nodo1.getPath()), true );
		return nodo1;
	}
	
	private void borrarNodo( DefaultMutableTreeNode nodoABorrar ) {
		DefaultMutableTreeNode nodoPadre = (DefaultMutableTreeNode) nodoABorrar.getParent();
		if (nodoPadre == null) {
			return;
		}
		modeloArbol.removeNodeFromParent( nodoABorrar );
	}

	@SuppressWarnings("unused")
	// Si hubiera que cambiar valores de nodo:
	private void cambiaValorNodo( Object dato, DefaultMutableTreeNode nodo ) {
		modeloArbol.valueForPathChanged( new TreePath( modeloArbol.getPathToRoot( nodo ) ), dato );
	}
	
	@SuppressWarnings("serial")
	public static class MiJTree extends JTree {
		public void expandir( TreePath path, boolean estado ) {
			setExpandedState( path, estado );
		}
	}
	
}
