package es.deusto.prog3.cap06.pr0506resuelta.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import es.deusto.prog3.cap06.pr0506resuelta.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class VentanaBancoDePruebas extends JFrame {
	// Datos de lógica
	private String[] nombresPruebas;
	private ArrayList<ProcesoProbable> procs;
	private ArrayList<Object> objetosPrueba;
	// Componentes manejables de la ventana
	private JTextField tfTamMin = new JTextField( "0", 5 );
	private JTextField tfTamMax = new JTextField( "1000", 5 );
	private JTextField tfIncTam = new JTextField( "100", 3 );
	private JRadioButton rbIncSuma = new JRadioButton( "suma" );
	private JRadioButton rbIncProd = new JRadioButton( "prod" );
	private JButton bCalcular = new JButton( "Calcular" );
	// Componentes de visualización
	private JTable tDatos = new JTable();
	private JTree tArbol = new JTree(new Object[0]);
	private PanelDibujoBancoDePruebas pDibujo = new PanelDibujoBancoDePruebas( 3000, 2000, false );
	// Contenedores manejables de la ventana
	private JPanel pConfig = new JPanel();
	// Hilo de cálculo
	private Thread miHilo = null;
	
	/** Construye una ventana de banco de pruebas normal, con datos de tamaño de prueba mínimo y máximo modificables
	 */
	public VentanaBancoDePruebas() {
		// Configuración de ventana
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		setTitle( "Banco de pruebas" );
		// Configuración de componentes
		ButtonGroup bg = new ButtonGroup();
		bg.add( rbIncSuma );
		bg.add( rbIncProd );
		rbIncSuma.setSelected( true );
		tDatos.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );  // Si se pone esto las columnas de la tabla no se redimensionan
		tDatos.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, 
					boolean isSelected, boolean hasFocus, int row, int column) {
				// System.out.println( "GetRenderer " + row + ", " + column );
				Component def = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column==0) {
					def.setForeground( pDibujo.getColor(row % procs.size()) ); 
				} else def.setForeground( Color.black );
				return def;
			}
		});
		// Contenedores privados
		JSplitPane spIzquierdo = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
		JSplitPane spDatos = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		// Asignación de componentes a contenedores
		spIzquierdo.setTopComponent( new JScrollPane( tDatos, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED ) );
		spIzquierdo.setBottomComponent( new JScrollPane( tArbol ) );
		spIzquierdo.setDividerLocation( 200 );
		spDatos.setLeftComponent( spIzquierdo );
		spDatos.setRightComponent( pDibujo );
		pConfig.add( new JLabel("Tam.min:") );
		pConfig.add( tfTamMin );
		pConfig.add( new JLabel("Tam.max:") );
		pConfig.add( tfTamMax );
		pConfig.add( new JLabel("Incr.:") );
		pConfig.add( tfIncTam );
		pConfig.add( rbIncSuma );
		pConfig.add( rbIncProd );
		pConfig.add( bCalcular );
		// add( spDatos, BorderLayout.CENTER ); == gCP().add
		getContentPane().add( spDatos, BorderLayout.CENTER );
		getContentPane().add( pConfig, BorderLayout.SOUTH );
		// Eventos
		bCalcular.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bCalcular.setEnabled( false );
				// Esto es lo habitual (objeto de clase interna anónima):
				miHilo = new Thread() { public void run() 
					{ calcular(); } };
				// Pero en Java 8 se puede hacer con notación lambda:
				// miHilo = new Thread( () -> { calcular(); } );
					// Es como crear un objeto  Runnable r = () -> {calcular();};
					// Antes de Java 8, Runnable r = new Runnable() { public void run() { calcular(); } };
				miHilo.start();
			}
		});
		pDibujo.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				pDibujo.reiniciarDibujo();
				pDibujo.getParent().repaint();
			}
		});
		tDatos.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					pDibujo.marcaLineas( tDatos.getSelectedRows() );
					pDibujo.getParent().repaint();
				}
			}
		});
		tDatos.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {  
				if (e.isAltDown()) {   // Reacciona a Alt+Click
					int fila = tDatos.rowAtPoint( e.getPoint() );
					fila = fila % procs.size();
					if (fila>=0 && objetosPrueba.size()>fila) {
						DefaultMutableTreeNode raizTemporal = new DefaultMutableTreeNode( "" );
						raizTemporal.add( new DefaultMutableTreeNode( "Calculando..." ) );
						DefaultTreeModel arbolTemp = new DefaultTreeModel( raizTemporal );
						tArbol.setModel( arbolTemp );
						tArbol.repaint();
						final Object alArbol = objetosPrueba.get(fila);
						Thread hilo = new Thread( () -> { 
							if (atributosAVer==null || atributosAVer.isEmpty())
								tArbol.setModel( ExploradorObjetos.atributosYValoresToTree( alArbol ) );
							else 
								tArbol.setModel( ExploradorObjetos.atributosYValoresToTree( alArbol, atributosAVer ) );
							tArbol.repaint();
						} );
						hilo.start();
					}
				}
			}
		});
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (miHilo!=null && miHilo.isAlive()) miHilo.interrupt();
			}
		});
		KeyListener kl = new KeyAdapter() {
			boolean pulsadoCtrl = false;
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) pulsadoCtrl = false;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) pulsadoCtrl = true;
				else if (pulsadoCtrl) {
					if (e.getKeyCode()==KeyEvent.VK_PLUS) {  // Aumentar zoom
						if (e.getSource()==tArbol) {
							Font font = tArbol.getFont();
							font = new Font( font.getName(), font.getStyle(), font.getSize() + 4 );
							tArbol.setFont( font );							
						} else {
							pDibujo.cambiarZoom( true );
							pDibujo.getParent().repaint();
						}
					} else if (e.getKeyCode()==KeyEvent.VK_MINUS) {  // Disminuir zoom
						if (e.getSource()==tArbol) {
							Font font = tArbol.getFont();
							font = new Font( font.getName(), font.getStyle(), font.getSize() - 4 );
							tArbol.setFont( font );							
						} else {
							pDibujo.cambiarZoom( false );
							pDibujo.getParent().repaint();
						}
					}
				}
			}
		};
		tfIncTam.addKeyListener( kl );  // Se añade el keylistener a todos los componentes focusables
		tfTamMax.addKeyListener( kl );
		tfTamMin.addKeyListener( kl );
		bCalcular.addKeyListener( kl );
		tDatos.addKeyListener( kl );
		tArbol.addKeyListener( kl );
		rbIncProd.addKeyListener( kl );
		rbIncSuma.addKeyListener( kl );
	}
	
	/** Construye una ventana de banco de pruebas simplificada, que solo muestra la estructura de árbol de atributos del objeto indicado
	 * @param o	Objeto del cual mostrar la estructura
	 * @param titulos	Títulos opcionales para la barra de cabecera de la ventana
	 */
	public VentanaBancoDePruebas( Object o, String... titulos ) {
		// Configuración de ventana
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		String titulo = "Observador de objeto";
		if (titulos.length>0) titulo += ":";
		for (String s : titulos) titulo += (" " + s);
		setTitle( titulo );
		// Asignación de componentes a contenedores
		getContentPane().add( new JScrollPane( tArbol ), BorderLayout.CENTER );
		// Cálculo de árbol de atributos del objeto
		setObjeto( o );
		// Escuchadores
		KeyListener kl = new KeyAdapter() {
			boolean pulsadoCtrl = false;
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) pulsadoCtrl = false;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) pulsadoCtrl = true;
				else if (pulsadoCtrl) {
					if (e.getKeyCode()==KeyEvent.VK_PLUS) {  // Aumentar zoom
						Font font = tArbol.getFont();
						font = new Font( font.getName(), font.getStyle(), font.getSize() + 4 );
						tArbol.setFont( font );							
					} else if (e.getKeyCode()==KeyEvent.VK_MINUS) {  // Disminuir zoom
						Font font = tArbol.getFont();
						font = new Font( font.getName(), font.getStyle(), font.getSize() - 4 );
						tArbol.setFont( font );							
					}
				}
			}
		};
		tArbol.addKeyListener( kl );
	}
	
	/** Modifica el objeto de la estructura de la ventana
	 * @param o	Objeto del cual mostrar la estructura
	 */
	public void setObjeto( Object o ) {
		if (o!=null) {
			DefaultMutableTreeNode raizTemporal = new DefaultMutableTreeNode( "" );
			raizTemporal.add( new DefaultMutableTreeNode( "Calculando..." ) );
			DefaultTreeModel arbolTemp = new DefaultTreeModel( raizTemporal );
			tArbol.setModel( arbolTemp );
			tArbol.repaint();
			final Object alArbol = o;
			Thread hilo = new Thread( () -> { 
				try { Thread.sleep(100); } catch (InterruptedException e) {}
				if (atributosAVer==null || atributosAVer.isEmpty())
					tArbol.setModel( ExploradorObjetos.atributosYValoresToTree( alArbol ) );
				else 
					tArbol.setModel( ExploradorObjetos.atributosYValoresToTree( alArbol, atributosAVer ) );
				tArbol.repaint();
			} );
			hilo.start();
		}
	}
	
	public void setProcesos( String[] pruebas, ArrayList<ProcesoProbable> procs ) {
		this.nombresPruebas = pruebas;
		this.procs = procs;
	}
	
	/** Reinicia los tamaños de ejecución
	 * @param tamMin	Tamaño mínimo
	 * @param tamMax	Tamaño máximo
	 * @param incTam	Incremento de tamaño
	 */
	public void setTamanyos( int tamMin, int tamMax, int incTam ) {
		tfTamMin.setText( "" + tamMin );
		tfTamMax.setText( "" + tamMax );
		tfIncTam.setText( "" + incTam );
	}
	
		private volatile String atributosAVer = null;
	/** Activa los atributos que se quieren ver en esta ventana. Por defecto se ven todos
	 * @param atributos	Lista de atributos a ver en un string, por ejemplo "raiz#elemento#izquierdo#derecho"
	 */
	public void setAtributosAVer( String atributos ) {
		atributosAVer = atributos;
	}
	
		private DefaultTableModel dtm;
	private void calcular() {
		if (procs==null) return;
		objetosPrueba = new ArrayList<Object>();
		int tamanyo = 0;
		int tamMax = 0;
		int incTam = 0;
		try {
			tamanyo = Integer.parseInt( tfTamMin.getText() );
			tamMax = Integer.parseInt( tfTamMax.getText() );
			incTam = Integer.parseInt( tfIncTam.getText() );
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog( this, "Algún dato de tamaño no es numérico", "Error en datos", JOptionPane.ERROR_MESSAGE );
			return;  // No se calcula
		}
		pDibujo.iniciarDibujo( tamMax, nombresPruebas );
		int numTam = 0;
		// Crear tabla inicial de JTable (primera columna)
			dtm = new MiDTM( new Object[] { "Prueba" }, 0 );
			for (int fila=0; fila<procs.size(); fila++) dtm.addRow( new Object[] { "t. " + nombresPruebas[fila] } );
			for (int fila=0; fila<procs.size(); fila++) dtm.addRow( new Object[] { "esp. " + nombresPruebas[fila] } );
			tDatos.setModel( dtm );
			tDatos.getColumnModel().getColumn(0).setPreferredWidth( 120 );
		while (tamanyo <= tamMax) {
			// Añade columna de JTable (nuevo tamaño)
				try {
					final String t = "" + tamanyo;
					SwingUtilities.invokeAndWait( new Runnable() {
						@Override
						public void run() {
							dtm.addColumn( t, new Object[procs.size()*2] ); // Añade columna  (en EDT para evitar errores en el redibujado)
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				tDatos.getColumnModel().getColumn(0).setPreferredWidth( 120 ); // Anchura preferida de primera columna (nombre)
				for (int col=1; col<=numTam+1; col++) // Hay que poner todas las anchuras porque la tabla se recalcula en cada cambio
					tDatos.getColumnModel().getColumn(col).setPreferredWidth( 40 ); // Anchura preferida de columnas de datos (tamaño) 
			for (int prueba=0; prueba<procs.size(); prueba++) {
				// Realizar prueba
				long tiempo = BancoDePruebas.realizaTest( procs.get(prueba), tamanyo );
				int espacio = BancoDePruebas.getTamanyoTest();
				if (objetosPrueba.size()<=prueba) objetosPrueba.add( BancoDePruebas.getTestResult() );
				else objetosPrueba.set( prueba, BancoDePruebas.getTestResult() );
				// Añadir dato a tabla
				dtm.setValueAt( tiempo, prueba, numTam+1 );
				dtm.setValueAt( espacio/1024+"k", prueba+procs.size(), numTam+1 );
				// Añadir dato a dibujo
				pDibujo.anyadirYDibujarTiempo( prueba, numTam, tamanyo, tiempo );
				pDibujo.anyadirYDibujarEspacio( prueba, numTam, tamanyo, espacio );
				pDibujo.getParent().repaint();
				// Cancelar el hilo si se interrumpe desde fuera
				if (Thread.interrupted()) return;  
			}
			if (rbIncSuma.isSelected()) tamanyo += incTam; else tamanyo *= incTam;
			numTam++;
		}
		bCalcular.setEnabled( true );  // Ya ha acabado el hilo, se puede volver a activar
		Utils.muestraThreadsActivos();
	}

}


// Prueba de nuevo DefaultTableModel

@SuppressWarnings("serial")
class MiDTM extends DefaultTableModel {
	public MiDTM( Object[] aObjetos, int numFilas ) {
		super( aObjetos, numFilas );
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
//		if (column==0) return false;
//		return true;
	}
	
}
