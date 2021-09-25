package es.deusto.prog3.utils.tabla;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import es.deusto.prog3.utils.Contador;
import es.deusto.prog3.utils.tabla.iu.Threadable;
import es.deusto.prog3.utils.tabla.iu.VentanaDatos;
import es.deusto.prog3.utils.tabla.iu.VentanaDatos.EventoEnCelda;
import es.deusto.prog3.utils.tabla.iu.VentanaGeneral;

/** Clase que permite editar cualquier tabla SQL de forma rápida sin conocer específicamente su formato
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EdicionRapidaTabla {
	
	public static String FIC_CONFIGURACION = "EdicionRapidaTabla.xml";
	
	private static boolean MULTI_CLAVE = true;  // Activar si los campos tienen claves separadas por comas
	
	public static enum Config { PATH_BD, NOMBRE_TABLA };
	public static String[] CONFIG_DEFECTO = {
		"d:/desarrollo/Andoni/AEapps/data/inspira.bd"
		, "cientificasos" 
	};
	public static String[] CONFIG_MENSAJES = { 
		"Path de base de datos:"  // URL_PRINCIPAL
		, "Nombre de tabla"
	};
	public static String[] CONFIG_TIPOS = { "L30", "L10" };
	
	private static String pathBD = "d:/desarrollo/Andoni/AEapps/data/inspira.bd";
	private static String nombreTabla = "cientificasos";
	
	private static Connection conn;
	private static Statement stat;
	
	private static ArrayList<SQLRow> listaFilas;
	private static ArrayList<String> colNames;
	private static ArrayList<Class<?>> colTypes;
	private static ArrayList<Integer> camposClave = new ArrayList<>( Arrays.asList( 0 ) );
	
	private static VentanaGeneral vg;
	private static VentanaDatos ventanaTabla;
	private static Tabla tabla;
	
	public static void main(String[] args) {
		conn = BDTabla.initBD( pathBD );
		stat = BDTabla.nuevoStatBD( conn );
		listaFilas = BDTabla.creaTablaDesdeBD( stat, nombreTabla );
		colNames = BDTabla.getColNames();
		colTypes = BDTabla.getColTypes();
		if (colNames!=null && colTypes!=null) {
			boolean hayDup = checkClavesDuplicadas( null );
			while (hayDup) {
				cambiaClaves();
				hayDup = checkClavesDuplicadas( null );
			}
			vg = new VentanaGeneral();
			vg.setTitle( "Edición rápida de tabla de base de datos" );
			vg.setSize( 1000, 800 );
			vg.setLocationRelativeTo( null );
			vg.setEnCierre( () -> { BDTabla.cerrarBD( conn, stat ); } );
			vg.setVisible( true );
			ventanaTabla = VentanaDatos.crearVentanaYTabla( vg, listaFilas, "Datos", "Datos de la tabla " + nombreTabla, 0, 0 );
			tabla = ventanaTabla.getTabla();
			EventoEnCelda ev = new DobleClickCelda( tabla, ventanaTabla );
			ventanaTabla.setDobleClickCelda( ev );
			ventanaTabla.setEnterCelda( ev );
			ventanaTabla.setDobleClickHeader( new EventoEnCelda() {
				@Override
				public void evento(int fila, int columna) {
					ordenarPorColumna( columna );
				}
			} );
			ventanaTabla.addBoton( "Borrar todo", new Threadable() { @Override public void run() { borrarTablaCompleta(); } });
			ventanaTabla.addBoton( "Borrar fila sel", new Threadable() { @Override public void run() { borrarFilaSel(); } });
			ventanaTabla.addBoton( "Insertar grupos de datos", new Threadable() { @Override public void run() { introduccionDatosConTabs(); } });
			ventanaTabla.addBoton( "Conteo por columna", new Threadable() { @Override public void run() { contarPorColumna(); } });
			ventanaTabla.addBoton( "Copy clipboard", new Threadable() { @Override public void run() { copiarAClipboard(); } });
			ventanaTabla.getJTable().addKeyListener( new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_DELETE) {
						borrarFilaSel();
					}
				}
			});
			ventanaTabla.setVisible( true );
		}
	}
	
		private static int ultimaColumna = -1;
		private static boolean ordenAscendente = true;
	private static void ordenarPorColumna( int columna ) {
		if (columna<0) return;
		if (columna==ultimaColumna) ordenAscendente = !ordenAscendente;
		else ordenAscendente = true;
		listaFilas.sort( new Comparator<SQLRow>() {
			@Override
			public int compare(SQLRow o1, SQLRow o2) {
				if (ordenAscendente) return o1.get(columna).compareTo(o2.get(columna));
				else return o2.get(columna).compareTo(o1.get(columna));
			}
		});
		ventanaTabla.repaint();
		ultimaColumna = columna;
	}
	
	private static void borrarTablaCompleta() {
		int resp = JOptionPane.showInternalConfirmDialog( ventanaTabla, "Se va a borrar todo ¿estás segura/o?", "Confirmación de borrado", JOptionPane.YES_NO_OPTION );
		if (resp==0) {
			BDTabla.deleteAllTabla( stat, nombreTabla );
			listaFilas.clear();
			ventanaTabla.getJTable().repaint();
		}
	}
	
	private static void borrarFilaSel() {
		int fila = ventanaTabla.getJTable().getSelectedRow();
		if (fila<0) return;
		int resp = JOptionPane.showInternalConfirmDialog( ventanaTabla, "Se va a borrar la fila\n" + listaFilas.get(fila) + "\n¿Estás segura/o?", "Confirmación de borrado", JOptionPane.YES_NO_OPTION );
		if (resp==0) {
			ArrayList<String> nombresCols = new ArrayList<>();
			ArrayList<String> valores = new ArrayList<>();
			for (int campoClave : camposClave) {
				nombresCols.add( colNames.get(campoClave) );
				valores.add( listaFilas.get(fila).get(campoClave) );
			}
			boolean ok = BDTabla.deleteRow(stat, nombreTabla, nombresCols, valores );
			if (ok) {
				listaFilas.remove( fila );
				ventanaTabla.getJTable().repaint();
			}
		}
	}
	
	private static JTextArea ta = new JTextArea();
	private static void introduccionDatosConTabs() {
		JFrame f = new JFrame( "Introduce datos separados por tabs" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.add( new JScrollPane( ta ), BorderLayout.CENTER );
		JButton b = new JButton( "Carga a BD" );
		f.add( b,  BorderLayout.SOUTH );
		b.addActionListener( (e) -> {
			procesoLista();
			f.dispose();
		} );
		f.setSize( 800,  800 );
		f.setLocationRelativeTo( null );
		f.setVisible( true );
	}

	private static void procesoLista() {
		StringTokenizer lins = new StringTokenizer( ta.getText(), "\n" );
		ArrayList<SQLRow> listaNuevos = new ArrayList<>();
		while (lins.hasMoreTokens()) {
			String lin = lins.nextToken();
			StringTokenizer tabs = new StringTokenizer( lin, "\t" );
			ArrayList<String> listaDatos = new ArrayList<>();
			while (tabs.hasMoreTokens()) {
				listaDatos.add( tabs.nextToken() );
			}
			SQLRow fila = new SQLRow( listaDatos, colNames, colTypes );
			listaNuevos.add( fila );
			listaFilas.add( fila );
			ventanaTabla.getJTable().repaint();
		}
		BDTabla.insertAllEnTabla( stat, nombreTabla, listaNuevos, colNames, null );
	}
	
	private static void contarPorColumna() {
		int col = ventanaTabla.getJTable().getSelectedColumn();
		if (col<0) return;
		HashMap<String,Contador> mapa = new HashMap<>();
		for (SQLRow fila : listaFilas) {
			String dato = fila.get( col );
			if (dato==null) dato = "";
			if (!mapa.containsKey( dato )) {
				mapa.put( dato, new Contador(1) );
			} else {
				mapa.get( dato ).inc();
			}
		}
		ArrayList<DatoConCont> listaConts = new ArrayList<>();
		for (String dato : mapa.keySet()) listaConts.add( new DatoConCont( dato, mapa.get(dato) ) );
		listaConts.sort( new Comparator<DatoConCont>() {
			@Override
			public int compare(DatoConCont o1, DatoConCont o2) {
				return o2.cont.get() - o1.cont.get();
			}
		});
		VentanaDatos vd = VentanaDatos.crearVentanaYTabla( vg, listaConts, "Datos", "Conteo de la tabla " + nombreTabla + " - columna " + colNames.get(col), 0, 0 );
		vd.setVisible( true );
	}
		private static class DatoConCont implements ConvertibleEnTabla {
			String dato;
			Contador cont;
			public DatoConCont( String dato, Contador cont ) { this.dato = dato; this.cont = cont; }
			@Override
			public int getNumColumnas() {
				return 2;
			}
			@Override
			public String getValorColumna(int col) {
				if (col==0) return dato; else return cont.toString();
			}
			@Override
			public void setValorColumna(int col, String valor) {}
			@Override
			public String getNombreColumna(int col) {
				if (col==0) return "Dato"; else return "Conteo";
			}
			
		}
		
	private static void copiarAClipboard() {
		StringBuffer texto = new StringBuffer();
		for (int col=0; col<tabla.getWidth(); col++) texto.append( tabla.getHeader( col ) + "\t" );
		for (int fila=0; fila<tabla.size(); fila++) {
			ventanaTabla.setMensaje( "Copiando contenido a portapapeles... " + (fila+1) + " de " + tabla.size(), Color.yellow );
			texto.append( "\n" );
			for (int col=0; col<tabla.getWidth(); col++) {
				if (tabla.getType(col) == Double.class) {
					texto.append( tabla.get( fila, col ).replaceAll("\\.",",") + "\t" );
				} else {
					texto.append( tabla.get( fila, col ) + "\t" );
				}
			}
		}
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents( new StringSelection(texto.toString()),null);				
		ventanaTabla.setMensaje( "Contenido de tabla copiado al portapapeles." );
	}

		private static final String MARCA_SEP_CLAVE = "]+[";
	// Si listaNuevas != null chequea también la lista además de las ya existentes
	// Devuelve true si hay duplicadas
	private static boolean checkClavesDuplicadas( ArrayList<SQLRow> listaNuevas ) {
		boolean ret = false;
		String duplicaciones = "";
		HashSet<String> claves = new HashSet<>();
		if (MULTI_CLAVE && camposClave.size()==1) {  // Si hay un solo campo clave y es multiclave (separado por comas)
			for (SQLRow row : listaFilas) {
				String multiClave = row.get(camposClave.get(0));
				StringTokenizer st = new StringTokenizer( multiClave, "," );
				while (st.hasMoreTokens()) {
					String clave = st.nextToken().trim(); 
					String nuevaClave = MARCA_SEP_CLAVE + clave + MARCA_SEP_CLAVE;
					if (claves.contains(nuevaClave)) {
						ret = true;
						duplicaciones += clave + "\n";
					} else {
						claves.add( nuevaClave );
					}
				}
			}
			if (listaNuevas!=null) {
				for (SQLRow row : listaNuevas) {
					String multiClave = row.get(camposClave.get(0));
					StringTokenizer st = new StringTokenizer( multiClave, "," );
					while (st.hasMoreTokens()) {
						String clave = st.nextToken().trim(); 
						String nuevaClave = MARCA_SEP_CLAVE + clave + MARCA_SEP_CLAVE;
						if (claves.contains(nuevaClave)) {
							ret = true;
							duplicaciones += clave + "\n";
						} else {
							claves.add( nuevaClave );
						}
					}
				}
			}
		} else {
			for (SQLRow row : listaFilas) {
				String nuevaClave = MARCA_SEP_CLAVE;
				for (int i : camposClave) {
					nuevaClave += row.get(i) + MARCA_SEP_CLAVE;
				}
				if (claves.contains(nuevaClave)) {
					ret = true;
					duplicaciones += nuevaClave + "\n";
				} else {
					claves.add( nuevaClave );
				}
			}
			if (listaNuevas!=null) {
				for (SQLRow row : listaNuevas) {
					String nuevaClave = MARCA_SEP_CLAVE;
					for (int i : camposClave) {
						nuevaClave += row.get(i) + MARCA_SEP_CLAVE;
					}
					if (claves.contains(nuevaClave)) {
						ret = true;
						duplicaciones += nuevaClave + "\n";
					} else {
						claves.add( nuevaClave );
					}
				}
			}
		}
		if (ret) JOptionPane.showMessageDialog( null, "Hay claves duplicadas en la definición actual de campos clave\n" +  duplicaciones, "Reconfiguración necesaria", JOptionPane.ERROR_MESSAGE );
		return ret;
	}
	
	private static void cambiaClaves() {
		String[] cabs = colNames.toArray( new String[ colNames.size() ] );
		JList<String> listaClaves = new JList<>( cabs );
		listaClaves.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
		listaClaves.requestFocus();
		JOptionPane.showMessageDialog( null, listaClaves, "Selecciona los campos clave", JOptionPane.QUESTION_MESSAGE );
		int[] camposSel = listaClaves.getSelectedIndices();
		camposClave.clear();
		for (int i : camposSel) camposClave.add( i );
		if (camposClave.isEmpty()) camposClave.add( 0 );
	}

	
	private static class DobleClickCelda implements VentanaDatos.EventoEnCelda {
		private Tabla tabla;
		private VentanaDatos ventana;
		public DobleClickCelda( Tabla tabla, VentanaDatos ventana ) {
			this.tabla = tabla;
			this.ventana = ventana;
			// colCodigo = ventana.getColumnWithHeader( "CodCentro", false );
		}
		@Override
		public void evento(int fila, int columna) {
			// if (columna==colCodigo) {  // Doble click en celda de código de centro
//				MiJComboBox optionList = new MiJComboBox( arraySexos );
//				optionList.setSelectedIndex( -1 );
//				String datoAnt = Datos.tablaCientificasos.get( fila, colSexo );
//				int esta = Arrays.asList( arraySexos ).indexOf( datoAnt );
//				if (esta!=-1) optionList.setSelectedIndex( esta ); else optionList.setSelectedItem("M");
//				optionList.requestFocus();
//				JOptionPane.showInternalMessageDialog( ventana, optionList, "Elige sexo", JOptionPane.QUESTION_MESSAGE );
//				if (!optionList.isEscaped() && optionList.getSelectedItem()!=null && !optionList.getSelectedItem().isEmpty() ) {
//					Datos.tablaCientificasos.set( fila, colSexo, optionList.getSelectedItem() );
//					cambiosEnDatosCient = true;
//				}
			ArrayList<String> nombresCols = new ArrayList<>();
			ArrayList<String> valores = new ArrayList<>();
			for (int campoClave : camposClave) {
				nombresCols.add( colNames.get(campoClave) );
				valores.add( listaFilas.get(fila).get(campoClave) );
			}
			String datoAnt = tabla.get( fila, columna ); if (datoAnt==null) datoAnt = "";
			String nuevoDato = (String) JOptionPane.showInternalInputDialog( ventana, "Cambia el dato", "Edición", JOptionPane.QUESTION_MESSAGE, null, null, datoAnt );
			if (nuevoDato!=null) {
				tabla.set( fila, columna, nuevoDato );
				BDTabla.updateRow(stat, nombreTabla, nombresCols, valores, colNames.get(columna), nuevoDato );
				ventanaTabla.getJTable().repaint();
			}
		}
	}
	
}


