package es.deusto.prog3.cap06;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/** Clase que visualiza el espacio ocupado en disco en sus carpetas,
 * utilizando el componente visual JTree modificado para mostrar
 * una barra de progreso además de una etiqueta (nombre y espacio de la carpeta)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EspacioEnDiscoConJTree {

	public static void main(String[] args) {
		String dirInicial = "c:/";
		if (args.length>0) dirInicial = args[0];
		initVentana( dirInicial );
		visualizarEspacioEnDiscoConJTree( dirInicial );  // Se pueden pasar varias carpetas
	}

	// ---------------------------------
	// Métodos visuales
	// ---------------------------------
	
	private static JFrame vent = null;
	private static JTree2 tree;
	private static NodoConTamanyo raiz;
	private static DefaultTreeModel modeloArbol;
	private static void initVentana( String fRaiz ) {
		vent = new JFrame();
		vent.setTitle( "Espacio ocupado en disco desde " + fRaiz );
		vent.setSize( 640, 480 );
		vent.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		vent.setLocationRelativeTo( null );
		tree = new JTree2();
		JScrollPane spPrincipal = new JScrollPane(tree);
		vent.getContentPane().add( spPrincipal, BorderLayout.CENTER );
		raiz = new NodoConTamanyo( "Total", new Object[] { 0, 0, 0L } );
		modeloArbol = new DefaultTreeModel( raiz );
		tree.setModel( modeloArbol );
		tree.setCellRenderer( new TreeNodeTamanyoRenderer() );
		vent.setVisible( true );
	}

	/** Crea un nuevo nodo hijo en el árbol
	 * @param texto	Texto del nodo
	 * @param datos	Datos del nodo
	 * @param padre	Nodo padre del que crear el nodo hijo. Si es null, se crea como hijo de la raíz principal del árbol
	 * @return	Nodo hijo recién creado
	 */
	private static NodoConTamanyo anyadeNodoHijo( String texto, Object[] datos, NodoConTamanyo padre ) {
		NodoConTamanyo nuevo = new NodoConTamanyo( texto, datos );
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					if (padre==null) {
						raiz.add( nuevo );
						modeloArbol.nodesWereInserted( raiz, new int[] { raiz.getChildCount()-1 } );
					} else {
						padre.add( nuevo );
						modeloArbol.nodesWereInserted( padre, new int[] { padre.getChildCount()-1 } );
					}
					tree.setES( new TreePath(nuevo.getPath()), true );
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nuevo;
	}
	
	// ---------------------------------
	// Recorrido de ficheros
	// ---------------------------------
	
	/** Visualiza el espacio en disco ocupado por cada carpeta del directorio indicado
	 * @param dirIni	Directorios a explorar. Si no es una carpeta correcta el método no hace nada.
	 */
	private static void visualizarEspacioEnDiscoConJTree( String... dirIni ) {
		for (String dir : dirIni) {
			File ini = new File( dir );
			if (!ini.exists() || !ini.isDirectory()) return;
			Object[] parcial = recorridoUnidad( ini, 0, raiz );
			raiz.datos[0] = (Integer)raiz.datos[0] + (Integer)parcial[0];  // Número de ficheros
			raiz.datos[1] = (Integer)raiz.datos[1] + (Integer)parcial[1];  // Número de carpetas
			raiz.datos[2] = (Long)raiz.datos[2] + (Long)parcial[2]  ;      // Tamaño de los ficheros
			modeloArbol.nodeChanged( raiz );  // Lanza evento de modificación en el modelo
		}
	}
	
	private static Object[] recorridoUnidad( File dir, int nivel, NodoConTamanyo padre ) {
		if (dir==null) return new Object[] { 0, 0, 0L };  // No hay ficheros, no hay carpetas, tamaño cero bytes
		NodoConTamanyo nodoNuevo = null;
		File[] files = null;
		if (dir.isDirectory()) {
			files = dir.listFiles();
			String nom = dir.getName();
			if (dir.getName().isEmpty()) nom = dir.getAbsolutePath();  // Caso especial para raíz del disco
			nodoNuevo = anyadeNodoHijo( nom, new Object[] { 0, 0, 0L }, padre );
		} else {
			return new Object[] { 1, 0, dir.length() };  // 1 fichero, no hay carpetas, tamaño bytes del fichero
		}
		if (files==null) return new Object[] { 0, 1, 0L };  // No hay ficheros, una carpeta -sin contenido-, tamaño cero bytes
		final NodoConTamanyo miNodo = nodoNuevo;
		ArrayList<File> al = new ArrayList<>( Arrays.asList( files ));
		al.sort( new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				String n1 = o1.getName();
				String n2 = o2.getName();
				return n1.compareTo( n2 );
			}
		});
		Object[] total = miNodo.datos;
		for (File o2 : al) {
			Object[] parcial = recorridoUnidad( o2, nivel+1, miNodo );
			total[0] = (Integer)total[0] + (Integer)parcial[0];  // Número de ficheros
			total[1] = (Integer)total[1] + (Integer)parcial[1];  // Número de carpetas
			total[2] = (Long)total[2] + (Long)parcial[2]  ;      // Tamaño de los ficheros
			miNodo.datos[0] = total[0];
			miNodo.datos[1] = total[1];
			miNodo.datos[2] = total[2];
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					modeloArbol.nodeChanged( miNodo );  // Lanza evento de modificación en el modelo
				}
			});
		}
		total[1] = (Integer)total[1] + 1; // Actualizar el número de carpetas para retornar (las que haya dentro + la actual)
		SwingUtilities.invokeLater( new Runnable() { // Colapsar la carpeta y así el árbol va teniendo un tamaño aceptable
			@Override
			public void run() {
				tree.collapsePath( new TreePath( miNodo.getPath() ) );
			}
		});
		return total;
	}

	/** Clase de utilidad que expone de forma pública el método #setES
	 * (equivalente al #setExpandedState, que es protegido en JTree)
	 * @author andoni.eguiluz @ ingenieria.deusto.es
	 */
	@SuppressWarnings("serial")
	private static class JTree2 extends JTree {
		@Override
		protected void setExpandedState(TreePath path, boolean state) {
			super.setExpandedState(path, state);
		}
		
		public void setES(TreePath path, boolean state) {
			setExpandedState(path, state);
		}
	}
	
	@SuppressWarnings("serial")
	private static class NodoConTamanyo extends DefaultMutableTreeNode {
		private Object[] datos;
		public NodoConTamanyo( String nombre, Object[] datos ) {
			super( nombre );
			this.datos = datos;
		}
	}
	
	@SuppressWarnings("serial")
	private static class TreeNodeTamanyoRenderer extends DefaultTreeCellRenderer {
		private JPanel panel;
		private JLabel lTexto;
		private JProgressBar pbTam;
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			if (panel==null) {
				panel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
				panel.setOpaque( false );
				panel.setPreferredSize( new Dimension( 600, 22 ) );
				lTexto = new JLabel();
				pbTam = new JProgressBar( 0, 20 );
				pbTam.setOpaque( false );
				panel.add( pbTam );
				panel.add( lTexto );
			}
			JLabel normal = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			lTexto.setFont( normal.getFont() );
			NodoConTamanyo nodo = (NodoConTamanyo) value;
			double subidaLogaritmica = (Long)(nodo.datos[2])/1024.0;
			if (subidaLogaritmica<1) subidaLogaritmica = 0; else subidaLogaritmica = Math.log( subidaLogaritmica );
			// Escala logaritmica: 0 si es < 1024 bytes, de 0.0 a ln(bytes) si es >=.
			pbTam.setValue( (int) Math.round( subidaLogaritmica ) );
			int val = (int) (200 * ((Long)nodo.datos[2]) / 50000000000L);  // 230 = 50 Gb aprox
			if (val>230) val = 230;  val = 230-val;  // Se invierte el color (cuanto más lleno más rojo)
			pbTam.setForeground( new Color( 255, val, val ));
			lTexto.setText( normal.getText() + String.format( " (%1$,d Kb)", (Long)(nodo.datos[2])/1024L ) );
			return panel;
		}
		
	}
	
}
