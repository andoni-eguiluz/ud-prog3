package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/** Ejemplo de JTable sencilla con lo mínimo para entender el modelo de datos, el renderer y el editor
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class JTableModeloRendererEditor {

	static JFrame ventana;
	static JTable jTable;
	static DefaultTableModel mTable;
	static String[] cabeceras = { "Nombre", "Socio", "Edad", "Puntuación" };
	static Object[][] datos = {
			{ "Andoni", false, 50, 123.48 },
			{ "Itxaso", true, 32, 415.2 },
	};
	static ArrayList<Socio> lSocios = new ArrayList<>( Arrays.asList( new Socio( "Andoni", false, 50, 123.48 ), new Socio( "Itxaso", true, 32, 415.2 ) ) );
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		// Creamos la tabla y la ventana
		ventana = new JFrame( "Ejemplo JTable" );
		ventana.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		ventana.setSize( 800, 600 );
		// 1.- JTable con datos directos
		jTable = new JTable( datos, cabeceras );
		ventana.add( new JScrollPane( jTable ) );
		ventana.setVisible( true );
		datos[0][0] = "Alberto";
		JOptionPane.showMessageDialog( null, "Primera tabla: datos directos. Pulsa para continuar..." );
		// 2.- JTable desde modelo de datos
		mTable = new DefaultTableModel( datos, cabeceras );
		jTable.setModel( mTable );
		mTable.setValueAt( "Alberto", 0, 0 );  // Cambiar valores...
		mTable.addRow( new Object[] { "Marta", true, 28, 320.5 } ); // Añadir filas...
		JOptionPane.showMessageDialog( null, "Segunda tabla: modelo de datos. Pulsa para continuar..." );
		// 3.- Modelo personalizado
		ListaSocios lista = new ListaSocios();
		lista.getLista().addAll( Arrays.asList( new Socio( "Andoni", false, 50, 123.48 ), new Socio( "Itxaso", true, 32, 415.2 ) ) );
		jTable.setModel( lista );
		JOptionPane.showMessageDialog( null, "Tercara tabla: modelo personalizado. Pulsa para continuar..." );
		// Observa que esta tercera tabla pone checkboxes en lugar de strings en los booleanos. ¿Por qué? Porque el método getColumnClass devuelve el tipo boolean
		
		// RENDERER
		// Vamos a cambiar la manera en la que se ve una columna particular: la de puntuación (queremos que se vea roja si es menor que 200)
		jTable.setDefaultRenderer( Double.class, new DefaultTableCellRenderer() {  // Pone el renderer para todos los valores que sean Double (puntuación)
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// Calcula el componente normal (es un JLabel con el valor como texto)
				Component normal = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// Lo modificamos como queramos
				if ((Double)value < 200) {
					normal.setBackground( Color.red );
				} else {
					normal.setBackground( Color.white );
				}
				return normal;
			}
		});
		jTable.repaint();  // Redibuja para refrescar este modelo
		
		// EDITOR
		// Vamos a cambiar la manera en la que se edita la edad para validar que está en el rango 0-120
		jTable.setDefaultEditor( Integer.class, new TableCellEditor() {
			SpinnerNumberModel mSpinner = new SpinnerNumberModel( 0, 0, 120, 1 );
			JSpinner spinner = new JSpinner( mSpinner );
			@Override
			public boolean stopCellEditing() { // Llamado cuando se acaba la edición
				ChangeEvent ce = new ChangeEvent(this);
				for (int i=0; i<lListeners.size(); i++) lListeners.get(i).editingStopped( ce );
				return true;
			}
			@Override
			public boolean shouldSelectCell(EventObject anEvent) {
				return true;
			}
			ArrayList<CellEditorListener> lListeners = new ArrayList<>();
			@Override
			public void removeCellEditorListener(CellEditorListener l) { // Hay que implementarlo para que Swing suscriba un escuchador
				lListeners.remove( l );
			}
			@Override
			public void addCellEditorListener(CellEditorListener l) { // Idem
				lListeners.add( l );
			}
			@Override
			public boolean isCellEditable(EventObject anEvent) { // No lo usamos en el ejemplo
				return true;
			}
			@Override
			public void cancelCellEditing() { // No lo usamos en el ejemplo
				ChangeEvent ce = new ChangeEvent(this);
				for (int i=0; i<lListeners.size(); i++) lListeners.get(i).editingCanceled( ce );
			}
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { // Componente que se pone en la tabla al editar una celda
				mSpinner.setValue( (Integer) value );
				return spinner;
			}
			@Override
			public Object getCellEditorValue() { // Valor que se retorna al acabar la edición
				return spinner.getValue();
			}
		});
	}

}

// Clases para modelo de datos
class ListaSocios implements TableModel {
	private ArrayList<Socio> lista;
	public ListaSocios() {
		lista = new ArrayList<>();
	}
	public ArrayList<Socio> getLista() { return lista; }
	// Los siguientes métodos corresponden al interfaz del modelo
	@Override
	public int getRowCount() {
		return lista.size();  // Número de filas
	}
	@Override
	public int getColumnCount() {
		return 4;  // Número de columnas
	}
	@Override
	public String getColumnName(int columnIndex) {
		return JTableModeloRendererEditor.cabeceras[ columnIndex ];
	}
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex==0) return String.class;
		else if (columnIndex==1) return Boolean.class;
		else if (columnIndex==2) return Integer.class;
		else if (columnIndex==3) return Double.class;
		return Object.class;
	}
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex==2) return true;  // Por ejemplo hacemos editable la tercera columna (edad)
		return false;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Socio socio = lista.get(rowIndex);
		if (columnIndex==0) return socio.getNombre();
		else if (columnIndex==1) return socio.isEsSocio();
		else if (columnIndex==2) return socio.getEdad();
		else if (columnIndex==3) return socio.getPuntuacion();
		return null;
	}
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Socio socio = lista.get(rowIndex);
		if (columnIndex==0) socio.setNombre( (String) aValue );
		else if (columnIndex==1) socio.setEsSocio( (Boolean) aValue );
		else if (columnIndex==2) socio.setEdad( (Integer) aValue );
		else if (columnIndex==3) socio.setPuntuacion( (Double) aValue );
	}
	private ArrayList<TableModelListener> lTML = new ArrayList<>();
	@Override
	public void addTableModelListener(TableModelListener l) {
		lTML.add( l );
	}
	@Override
	public void removeTableModelListener(TableModelListener l) {
		lTML.remove( l );
	}
}

class Socio {
	private String nombre;
	private boolean esSocio;
	private int edad;
	private double puntuacion;
	public Socio(String nombre, boolean esSocio, int edad, double puntuacion) {
		super();
		this.nombre = nombre;
		this.esSocio = esSocio;
		this.edad = edad;
		this.puntuacion = puntuacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public boolean isEsSocio() {
		return esSocio;
	}
	public void setEsSocio(boolean esSocio) {
		this.esSocio = esSocio;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public double getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
}