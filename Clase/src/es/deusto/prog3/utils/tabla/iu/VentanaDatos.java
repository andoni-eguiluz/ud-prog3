package es.deusto.prog3.utils.tabla.iu;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import es.deusto.prog3.utils.tabla.ConvertibleEnTabla;
import es.deusto.prog3.utils.tabla.Tabla;

/** Clase de ventana para muestra de datos de cualquier tabla {@link Tabla}
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
@SuppressWarnings("serial")
public class VentanaDatos extends JInternalFrame {
	
	private JTable tDatos;    // JTable de datos de la ventana
	private Tabla tablaDatos; // Tabla de datos de la ventana
	private JScrollPane spDatos; // Scrollpane de la jtable
	private JLabel lMensaje;  // Label de mensaje
	private JPanel pBotonera; // Panel de botones
	private VentanaGeneral ventMadre;  // Ventana madre
	private String tipo;  // Tipo de la ventana (usado para la configuración de la misma)

	private EventoEnCelda dobleClick;
	private EventoEnCelda dobleClickHeader;
	private EventoEnCelda enter;
	
	/** Añade un botón a la ventana
	 * @param texto	Texto del botón
	 * @param runnable	Objeto runnable con código a ejecutar (run()) cuando el botón se pulse
	 */
	public void addBoton( String texto, Runnable runnable ) {
		JButton b = new JButton( texto );
		pBotonera.add( b );
		b.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (runnable==null) return;
				if (runnable instanceof Threadable) (new Thread(runnable)).start();
				else runnable.run();
			}
		});
	}
	
	public JTable getJTable() { return tDatos; }
	
	public VentanaGeneral getVentMadre() { return ventMadre; }

	/** Crea una nueva ventana con posición
	 */
	public VentanaDatos( VentanaGeneral ventMadre, String tipo, String titulo, int posX, int posY ) {
		this( ventMadre, tipo, titulo );
		if (getX()==0 && getY()==0) setLocation( posX, posY );
	}
	/** Crea una nueva ventana
	 */
	public VentanaDatos( VentanaGeneral ventMadre, String tipo, String titulo ) {
	    super( titulo, true, true, true, true ); //  resizable, closable, maximizable, iconifiable
	    this.ventMadre = ventMadre;
	    this.tipo = tipo;
		// Configuración general
		setTitle( titulo );
		setSize( 800, 600 ); // Tamaño por defecto
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
		// Creación de componentes y contenedores
		pBotonera = new JPanel();
		tDatos = new JTable();
		lMensaje = new JLabel( " " );
		lMensaje.setOpaque( true );
		tDatos.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Asignación de componentes
		spDatos = new JScrollPane( tDatos );
		getContentPane().add( spDatos, BorderLayout.CENTER );
		getContentPane().add( pBotonera, BorderLayout.SOUTH );
		getContentPane().add( lMensaje, BorderLayout.NORTH );
		// Eventos
		tDatos.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()>=2) {
					int fila = tDatos.rowAtPoint( e.getPoint() );
					int columna = tDatos.columnAtPoint( e.getPoint() );
					if (dobleClick!=null) dobleClick.evento( fila, columna );
				}
			}
		});
		tDatos.addMouseMotionListener( new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int fila = tDatos.rowAtPoint( e.getPoint() );
				int columna = tDatos.columnAtPoint( e.getPoint() );
				if (fila>=0 && columna>=0) {
					Object valor = tDatos.getValueAt( fila, columna );
					if (valor!=null && ventMadre!=null) ventMadre.setMensajeSinCambioColor( valor.toString() );
				}
			}
		});
		tDatos.getTableHeader().addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()>=2) {
					int columna = tDatos.columnAtPoint( e.getPoint() );
					if (dobleClickHeader!=null) dobleClickHeader.evento( -1, columna );
				}
			}
		});
		tDatos.getTableHeader().addMouseMotionListener( new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int columna = tDatos.columnAtPoint( e.getPoint() );
				if (columna>=0) {
					Object valor = tDatos.getTableHeader().getColumnModel().getColumn(columna).getHeaderValue().toString();
					if (valor!=null && ventMadre!=null) ventMadre.setMensajeSinCambioColor( valor.toString() );
				}
			}
		});
		tDatos.getSelectionModel().addListSelectionListener( new ListSelectionListener() {  // Salta en el cambio de fila
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int fila = tDatos.getSelectedRow();
				int columna = tDatos.getSelectedColumn();
				if (fila>=0 && columna>=0) {
					Object valor = tDatos.getValueAt( fila, columna );
					if (valor!=null && ventMadre!=null) ventMadre.setMensajeSinCambioColor( valor.toString() );
				}
			}
		});
		tDatos.getColumnModel().getSelectionModel().addListSelectionListener( new ListSelectionListener() {  // Salta en el cambio de columna
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int fila = tDatos.getSelectedRow();
				int columna = tDatos.getSelectedColumn();
				if (fila>=0 && columna>=0) {
					Object valor = tDatos.getValueAt( fila, columna );
					if (valor!=null && ventMadre!=null) ventMadre.setMensajeSinCambioColor( valor.toString() );
				}
			}
		});
		tDatos.addKeyListener( new KeyAdapter() {
			boolean ultimaBusquedaEnCol = false; // Indica si la última búsqueda ha sido en columna (true) o en tabla (false)
			String ultimaBusqueda = ""; // Texto de la última búsqueda
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (enter!=null && tDatos!=null) {
						enter.evento( tDatos.getSelectedRow(), tDatos.getSelectedColumn() );
						e.consume();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {  // Ctrl+F = Buscar en la columna actual
					ultimaBusqueda = JOptionPane.showInputDialog( VentanaDatos.this, "Texto a buscar en la columna actual:", ultimaBusqueda );
					ultimaBusquedaEnCol = true;
					hacerBusqueda();
				} else if (e.getKeyCode() == KeyEvent.VK_B && e.isControlDown()) {  // Ctrl+B = Buscar en toda la tabla (primero hacia abajo luego hacia la derecha)
					ultimaBusqueda = JOptionPane.showInputDialog( VentanaDatos.this, "Texto a buscar en la tabla desde la posición actual:", ultimaBusqueda );
					ultimaBusquedaEnCol = false;
					hacerBusqueda();
				} else if (e.getKeyCode() == KeyEvent.VK_K && e.isControlDown()) {  // Ctrl+K = Buscar de nuevo (repetir la última búsqueda)
					hacerBusqueda();
				} else if (e.getKeyCode() == KeyEvent.VK_PLUS && e.isControlDown()) {  // Ctrl+K = Buscar de nuevo (repetir la última búsqueda)
					Font f = tDatos.getFont();
					tDatos.setFont( new Font( f.getFontName(), f.getStyle(), f.getSize()+1 ) );
					tDatos.repaint();
				} else if (e.getKeyCode() == KeyEvent.VK_MINUS && e.isControlDown()) {  // Ctrl+K = Buscar de nuevo (repetir la última búsqueda)
					Font f = tDatos.getFont();
					tDatos.setFont( new Font( f.getFontName(), f.getStyle(), f.getSize()-1 ) );
					tDatos.repaint();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_DOWN ) {  // Ctrl+cursor abajo - busca siguiente dato vacío si está en uno no vacío, y viceversa
					// Buscar siguiente dato vacío de la columna actual
					int colActual = tDatos.getSelectedColumn();
					int row = tDatos.getSelectedRow();
					if (colActual!=-1 && row>=0) {
						boolean estoyEnVacio = tDatos.getValueAt( row,  colActual ).toString().isEmpty();
						row++;
						while (row<tDatos.getModel().getRowCount()) {
							if ((!estoyEnVacio && tDatos.getValueAt( row, colActual ).toString().isEmpty()) ||
							   (estoyEnVacio && !tDatos.getValueAt( row, colActual ).toString().isEmpty()) 
							) {
								hacerScrollHastaFila( row, colActual, true );  // Seleccionar fila encontrada en la ventana
								return;
							}
							row++;
						}
					}
				} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_UP ) {  // Ctrl+cursor arriba - busca anterior dato vacío si está en uno no vacío, y viceversa
					// Buscar siguiente dato vacío de la columna actual
					int colActual = tDatos.getSelectedColumn();
					int row = tDatos.getSelectedRow();
					if (colActual!=-1 && row>0) {
						boolean estoyEnVacio = tDatos.getValueAt( row,  colActual ).toString().isEmpty();
						row--;
						while (row>=0) {
							if ((!estoyEnVacio && tDatos.getValueAt( row, colActual ).toString().isEmpty()) ||
							   (estoyEnVacio && !tDatos.getValueAt( row, colActual ).toString().isEmpty()) 
							) {
								hacerScrollHastaFila( row, colActual, false );  // Seleccionar fila encontrada en la ventana
								return;
							}
							row--;
						}
					}
				}
			}
			private void hacerScrollHastaFila( int row, int col, boolean haciaAbajo ) {  // Mostrar en el scrollpane la fila,col indicada
				tDatos.getSelectionModel().setSelectionInterval( row, row );
				Rectangle rect = tDatos.getCellRect( row, col, true );
				if (haciaAbajo)
					rect.setLocation(rect.x, rect.y+25);  // Y un poquito más abajo (para que no quede demasiado justo)
				else
					rect.setLocation(rect.x, rect.y-25);  // Y un poquito más arriba (para que no quede demasiado justo)
				tDatos.scrollRectToVisible( rect );
			}
			private void hacerBusqueda() {
				if (ultimaBusqueda==null || ultimaBusqueda.isEmpty()) return;
				int col = tDatos.getSelectedColumn();
				int row = tDatos.getSelectedRow();
				if (col<0) col = 0; if (row<0) row = 0;
				String aBuscar = ultimaBusqueda.toUpperCase();
				row++;
				if (ultimaBusquedaEnCol) {
					while (row<tDatos.getModel().getRowCount()) {
						if (tDatos.getValueAt( row, col ).toString().toUpperCase().contains( aBuscar )) {
							hacerScrollHastaFila( row, col, true );  // Seleccionar fila encontrada en la ventana
							return;
						}
						row++;
					}
				} else {
					while (row<tDatos.getModel().getRowCount() && col<tDatos.getModel().getColumnCount()) {
						if (tDatos.getValueAt( row, col ).toString().toUpperCase().contains( aBuscar )) {
							hacerScrollHastaFila( row, col, true );  // Seleccionar fila encontrada en la ventana
							return;
						}
						row++;
						if (row>=tDatos.getModel().getRowCount()) {
							row = 0;
							col = col + 1;
						}
					}
				}
			}
		} );
		// Cierre
		// setLocationRelativeTo( null );  // Centra la ventana en el escritorio  (solo se puede con JFrame)
		/* Renderer posible
		tDatos.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
			private JProgressBar pb = new JProgressBar( 0, 500 );
			private JLabel lVacia = new JLabel( "" );
			private JLabel lError = new JLabel( "ERROR" );
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if (column==2 || column==3) {
					try {
						if (value.toString().isEmpty()) return lVacia;
						double val = Double.parseDouble( value.toString() ); 
						pb.setValue( (int) val*100 );
						return pb;
					} catch (Exception e) {}
					return new JLabel( "Error" );
				} else {
					Component comp = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
					if (value instanceof String) {
						String string = (String) value;
						comp.setBackground( Color.white );
						comp.setForeground( Color.black );
						((JLabel)comp).setHorizontalAlignment( JLabel.LEFT );
						if ("0".equals(string)) {
							comp.setForeground( Color.LIGHT_GRAY ); 
							((JLabel)comp).setHorizontalAlignment( JLabel.CENTER );
						}
						if (column==1 || column>=4 && column<=9) {
							((JLabel)comp).setHorizontalAlignment( JLabel.CENTER );
						}
					}
					return comp;
				}
			}
		} );
		tDatos.addMouseMotionListener( new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int fila = tDatos.rowAtPoint( e.getPoint() );
				int columna = tDatos.columnAtPoint( e.getPoint() );
				if (columna==2 || columna==3) {
					if (tDatos.getModel().getValueAt( fila, columna ).toString().isEmpty()) {
						lMensaje.setText( " " );
					} else {
						lMensaje.setText( "Valor de satisfacción: " + tDatos.getModel().getValueAt( fila, columna )  );
					}
				} else {
					lMensaje.setText( " " );
				}
			}
		});
		tDatos.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()>=2) { // Doble click
					int fila = tDatos.rowAtPoint( e.getPoint() );
					int columna = tDatos.columnAtPoint( e.getPoint() );
					if (columna==0) {
						String centro = tDatos.getModel().getValueAt( fila, columna ).toString();
						CentroEd centroEd = Datos.centros.get( centro );
						JOptionPane.showMessageDialog( VentanaDatos.this, "Centro " + centro + "\nAbreviaturas: " + centroEd.getlAbrevs(), "Información de centro", JOptionPane.INFORMATION_MESSAGE );
						centroEd.getlAbrevs();
					} else {
						lMensaje.setText( " " );
					}
				}
			}
		});
		*/
	}
	
	/** Crea una nueva ventana partiendo de una lista de datos. Crea también la tabla asociada a esa lista de datos (recuperable con {@link #getTabla()}
	 * @param ventMadre	Ventana principal
	 * @param listaDatos	Lista de datos de la que crear la tabla
	 * @param tipo	Tipo nominal de la ventana/tabla
	 * @param titulo	Título de la ventana
	 * @param posX	Posición x en la ventana principal
	 * @param posY	Posición y en la ventana principal
	 * @return	Ventana creada
	 */
	public static VentanaDatos crearVentanaYTabla( VentanaGeneral ventMadre, ArrayList<? extends ConvertibleEnTabla> listaDatos, String tipo, String titulo, int posX, int posY ) {
		VentanaDatos ret = new VentanaDatos( ventMadre, tipo, titulo );
		if (ret.getX()==0 && ret.getY()==0) ret.setLocation( posX, posY );
		ventMadre.addVentanaInterna( ret, tipo );
		ret.setTabla( Tabla.linkTablaToList( listaDatos ) );
		return ret;
	}
	

	public void setMensaje( String mens, Color... colorFondo ) {
		Color fondo = (colorFondo.length > 0) ? colorFondo[0] : VentanaGeneral.COLOR_GRIS_CLARITO;
		if (mens==null || mens.isEmpty()) mens = " ";
		lMensaje.setText( mens );
		lMensaje.setBackground( fondo );
	}
	
	/** Asigna una tabla de datos a la JTable principal de la ventana
	 * @param tabla	Tabla de datos a visualizar
	 */
	public void setTabla( Tabla tabla ) {
		tablaDatos = tabla;
		tDatos.setModel( tabla.getTableModel() );
	}
	
	/** Devuelve la tabla de datos asignada a la ventana
	 * @return	tabla de datos asignada, null si no la hay
	 */
	public Tabla getTabla() {
		return tablaDatos;
	}
	
	/** Devuelve el tipo de la ventana
	 * @return	Tipo de la ventana
	 */
	public String getTipo() {
		return tipo;
	}
		
	/** Oculta las columnas indicadas en la visual
	 * @param colD	columna inicial (0 a n-1)
	 * @param colH	columna final (0 a n-1)
	 */
	public void ocultaColumnas( final int colD, final int colH ) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (int i=colH; i>=colD; i--)
					tDatos.removeColumn(tDatos.getColumnModel().getColumn( i ));
			}
		};
		if (SwingUtilities.isEventDispatchThread()) r.run(); else SwingUtilities.invokeLater( r );
	}
	
	/** Oculta las columnas indicadas en la visual
	 * @param cols	columnas a ocultar en orden CRECIENTE (0 a n-1)
	 */
	public void ocultaColumnas( ArrayList<Integer> cols ) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (int i=cols.size()-1; i>=0; i--) {
					tDatos.removeColumn(tDatos.getColumnModel().getColumn( cols.get(i) ));
				}
			}
		};
		if (SwingUtilities.isEventDispatchThread()) r.run(); else SwingUtilities.invokeLater( r );
	}
	
	public void setDobleClickCelda( EventoEnCelda evento ) {
		dobleClick = evento;
	}
	
	public void setDobleClickHeader( EventoEnCelda evento ) {
		dobleClickHeader = evento;
	}
	
	public void setEnterCelda( EventoEnCelda evento ) {
		enter = evento;
	}
	
	public int getColumnWithHeader( String nomCol, boolean nomExacto ) {
		TableColumnModel cols = tDatos.getTableHeader().getColumnModel();
		for (int col = 0; col<cols.getColumnCount(); col++) {
			String nom = cols.getColumn(col).getHeaderValue() + "";
			if (nomExacto && nom.equals( nomCol )) return col;
			if (!nomExacto && nom.toUpperCase().startsWith( nomCol.toUpperCase() ) ) return col;
		}
		return -1;
	}
	
	public interface EventoEnCelda {
		public void evento( int fila, int columna );
	}

	public void setRenderer( Class<?> clase, TableCellRenderer renderer ) {
		tDatos.setDefaultRenderer( clase, renderer );
	}
		
}
