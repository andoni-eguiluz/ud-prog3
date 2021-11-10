package es.deusto.prog3.cap06;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/** 
 * Ejemplo de JTable con modelo de datos propio
 */
@SuppressWarnings("serial")
public class MiJTable extends JTable {

	// Modelo de datos propio
	private static MiTableModel datos;

	// [02] Renderers para alinear distinto que a la izquierda
	private static DefaultTableCellRenderer rendererDerecha = new DefaultTableCellRenderer();
	private static DefaultTableCellRenderer rendererCentrado = new DefaultTableCellRenderer();
	static {
		rendererDerecha.setHorizontalAlignment( JLabel.RIGHT );
		rendererCentrado.setHorizontalAlignment( JLabel.CENTER );
	}

	public MiJTable( MiTableModel modelo ) {
		super(modelo);
    	// Fijar tamaño preferido de la tabla
        setPreferredScrollableViewportSize(new Dimension(500, 70));
        // [02] Asignar renderers de alineación horizontal
        getColumn("Nombre").setCellRenderer( rendererDerecha );
        getColumn("Deporte").setCellRenderer( rendererCentrado );
        // [03] Asignar anchuras iniciales
		getColumn( "Apellidos" ).setMinWidth( 200 );
		getColumn( "Deporte" ).setPreferredWidth( 100 );
		getColumn( "Años" ).setPreferredWidth( 50 );
	}
	
	public void setModel( TableModel t ) {
		if (t instanceof MiTableModel)
			super.setModel( t );
		else
			throw new IncompatibleClassChangeError( 
				"No se puede asignar cualquier modelo de datos a una MIJTable" );
	}
	
	public MiTableModel getMiTableModel() {
		return (MiTableModel) getModel();
	}
	
	/**
     * Crear la ventana y mostrarla. Para seguridad de hilos,
     * este método debería invocarse desde el hilo de gestión de eventos
     */
    private static void crearYMostrarGUI() {
    	// Crear datos de prueba
    	datos = new MiTableModel( Deportista.nombresAtributos, Deportista.atributosEditables );
    	datos.insertar( new Deportista( "Mary", "Campione",
    	         "Snowboard", new Integer(5), new Boolean(false) ) );
    	datos.insertar( new Deportista( "Alison", "Huml",
    	         "Remo", new Integer(3), new Boolean(true) ) );
    	datos.insertar( new Deportista( "Kathy", "Walrath",
    	         "Esgrima", new Integer(2), new Boolean(false) ) );
    	datos.insertar( new Deportista( "Sharon", "Zakhour",
    	         "Natación", new Integer(20), new Boolean(true) ) );
    	datos.insertar( new Deportista( "Philip", "Milne",
    	         "Baloncesto", new Integer(10), new Boolean(false) ) );

    	// Crear la tabla y el scrollpane
    	final MiJTable tabla = new MiJTable( datos );
        JScrollPane scrollPane = new JScrollPane( tabla );

        //Crear e inicializar la ventana con la tabla central
        JFrame frame = new JFrame("Demo de JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( scrollPane, "Center" );
        
        // Crear e inicializar la botonera
        JPanel botonera = new JPanel();
        JButton botonInsertar = new JButton( "Insertar nueva fila" );
        JButton botonBorrar = new JButton( "Borrar fila" );
        botonera.add( botonInsertar );
        botonera.add( botonBorrar );
        frame.add(botonera, "South");

        // Acciones de botones
        botonInsertar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	datos.insertar( new Deportista( "Nuevo", "Dato",
		    	         "?????", new Integer(0), new Boolean(true) ) );
				tabla.updateUI();
			}
        });
        botonBorrar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tabla.getSelectedRow() >= 0 && tabla.getSelectedRow() < datos.getRowCount() ) {
					datos.borrar( tabla.getSelectedRow() );
					tabla.updateUI();
				} else {
					JOptionPane.showMessageDialog( null, 
							"Selecciona una fila antes de borrarla", "Error en borrado", 
							JOptionPane.INFORMATION_MESSAGE );
				}
			}
        });
        
        // Ajusta el tamaño de la ventana y la muestra
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Mandar trabajo a Swing
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                crearYMostrarGUI();
            }
        });
    }
}

// [01] Clase de datos de base para el modelo
class Deportista implements DatoParaTabla {
	public static String[] nombresAtributos = new String[] {
		"Nombre", "Apellidos", "Deporte", "Años", "Vegetariano" };
	public static boolean[] atributosEditables = new boolean[] {
		true, true, false, true, true };
	private String nombre;
	private String apellidos;
	private String deporte;
	private int anyos;
	private boolean vegetariano;
	public Deportista( String nom, String apes, String dep, int numAnyos, boolean esVegetariano ) {
		nombre = nom;
		apellidos = apes;
		deporte = dep;
		anyos = numAnyos;
		vegetariano = esVegetariano;
	}
    public int getNumColumnas() {
    	return 5;
    }
    public Object getValor(int col) {
    	switch (col) {
	    	case 0: { return nombre; }
	    	case 1: { return apellidos; }
	    	case 2: { return deporte; }
	    	case 3: { return anyos; }
	    	case 4: { return vegetariano; }
    	}
    	return null;
    }
    public void setValor(Object value, int col) {
    	try {
	    	switch (col) {
		    	case 0: { nombre = (String) value; break; }
		    	case 1: { apellidos = (String) value; break; }
		    	case 2: { deporte = (String) value; break; }
		    	case 3: { anyos = (Integer) value; break; }
		    	case 4: { vegetariano = (Boolean) value; break; }
	    	}
    	} catch (Exception e) {
    		// Error en conversión. Intentando asignar un tipo incorrecto
    		e.printStackTrace();
    	}
    }
}

interface DatoParaTabla {
    public int getNumColumnas();  // Número de columnas (campos del dato)
    public Object getValor(int col);  // Devolver valor de columna col
    public void setValor(Object value, int col);  // Asignar valor a columna col
}

// [01] Clase que implementa un modelo de datos para usar en JTables
class MiTableModel extends AbstractTableModel {
    // Lista principal de datos del modelo:
    private ArrayList<DatoParaTabla> datos = new ArrayList<DatoParaTabla>();
    private String[] nombresColumnas;  // Nombres de columnas
    private boolean[] columnasEditables;  // Columnas editables o no
	private static final long serialVersionUID = 7026825539532562011L;
	private boolean DEBUG = true;

    /** Constructor de modelo de tabla
     * @param nombresColumnas	Nombres de las columnas
     * @param colsEditables	Array de valores lógicos si las columnas son editables (true) o no (false)
     */
    public MiTableModel( String[] nombresColumnas, boolean[] colsEditables ) {
    	this.nombresColumnas = nombresColumnas;
    	this.columnasEditables = colsEditables;
    }
    
    /** Añade un dato al final del modelo
     * @param dato	Dato a añadir
     */
    public void insertar( DatoParaTabla dato ) {
    	datos.add( dato );
    }

    /** Elimina un dato del modelo
     * @param dato	Dato a borrar
     */
    public void borrar( DatoParaTabla dato ) {
    	datos.remove( dato );
    }

    /** Elimina un dato del modelo, indicado por su posición
     * @param ind	Posición del dato a borrar
     */
    public void borrar( int ind ) {
    	datos.remove( ind );
    }

    /* [01] MODELO: Devuelve el número de columnas
     */
    public int getColumnCount() {
        return nombresColumnas.length;
    }

    /* [01] MODELO: Devuelve el número de filas
     */
    public int getRowCount() {
        return datos.size();
    }

    /* [01] MODELO: Devuelve el nombre de la columna
     */
    public String getColumnName(int col) {
        return nombresColumnas[col];
    }

    /* [01] MODELO: Devuelve el valor de la celda indicada
     */
    public Object getValueAt(int row, int col) {
        return datos.get(row).getValor(col);
    }

    /* [01] MODELO: Este método devuelve el renderer/editor por defecto
     * para cada celda, identificado por la columna. Si no cambiáramos
     * este método la última columna se vería como un String en lugar
     * de un checkbox (renderer/editor por defecto para Boolean)
     */
    public Class<?> getColumnClass(int c) {
    	if (datos.size()==0) return String.class;  // por defecto String
        return datos.get(0).getValor(c).getClass();  // Si hay datos, la clase correspondiente
    }

    /* [01] MODELO: Si la tabla es editable, este método identifica
     * qué celdas pueden editarse
     */
    public boolean isCellEditable(int row, int col) {
    	if (col >= 0 && col < columnasEditables.length)
    		return columnasEditables[col];
    	else return false;
    }

    /* [01] MODELO: Método que sólo hay que implementar si la tabla
     * puede cambiar (editar) valores de sus celdas
     */
    public void setValueAt(Object value, int row, int col) {
        if (DEBUG) {
            System.out.println("Poniendo valor en (" + row + "," + col
                               + ") = " + value + " (instancia de "
                               + value.getClass() + ")");
        }
        datos.get(row).setValor( value, col );
        fireTableCellUpdated(row, col);  // Notifica a escuchadores de cambio de celda
    }

}
