package es.deusto.prog3.cap06;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class SimpleTableModel implements TableModel {
	Object datosEjemplo[][] = 
		{ { "Andoni", "García", 14, "diciembre", 1968 },
		  { "Jennifer", "Lawrence",  15, "agosto", 1990 },
		  { "Leonardo", "di Caprio", 11, "noviembre", 1974 }
		};
	String columnas[] = { "Nombre", "Apellido", "dia", "mes", "año" };
	Class tipos[] = { String.class, String.class, Integer.class, String.class, Integer.class };
	
	private static JTable tPrueba;
	private static SimpleTableModel modelo;
	
	public static void main(String[] args) {
		JFrame v = new JFrame();
		v.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		tPrueba = new JTable( 5, 2 );
		modelo = new SimpleTableModel();
		tPrueba.setModel( modelo );
		JButton b = new JButton( "Test" );
		b.addActionListener( (e) -> {
			modelo.datosEjemplo[0][1] = "Eguíluz";
			// El cambio programático no se detecta solo (hay independencia de la vista y el modelo)
			// tPrueba.tableChanged( new TableModelEvent( modelo, 0, 0, 1, 1));
			// O mejor modelo.cambio( 0, 1, "Eguíluz" );
		});
		v.getContentPane().add( new JScrollPane( tPrueba ), BorderLayout.CENTER );
		v.getContentPane().add( b, BorderLayout.SOUTH );
		v.setSize( 600, 250 );
		v.setVisible( true );
	}

	// Todos los cambios deberían pasar por aquí
	// para informar a los escuchadores en lugar de cambiar directamente el array
	public void cambio( int fila, int columna, Object valor ) {
		datosEjemplo[fila][columna] = valor;
		for (TableModelListener l : myListeners) {
			l.tableChanged( new TableModelEvent( this, fila, fila, columna, columna ) );
		}
	}
	
	@Override
	public int getRowCount() {
		return datosEjemplo.length;
	}
	@Override
	public int getColumnCount() {
		return datosEjemplo[0].length;
	}
	@Override
	public String getColumnName(int columnIndex) {
		return columnas[columnIndex];
	}
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return tipos[columnIndex];
		// return datosEjemplo[0][columnIndex].getClass();  // Si queremos depender del dato
	}
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex==0) return false;
		return true;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		System.out.println( "Me piden (" + rowIndex + "," + columnIndex + ")" );
		return datosEjemplo[rowIndex][columnIndex];
	}
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		System.out.println( "Me cambian (" + rowIndex + "," + columnIndex + ") a " + aValue + " (" + aValue.getClass().getName() + ")" );
		datosEjemplo[rowIndex][columnIndex] = aValue;
		// TODO 
		// Prueba a modificar un dato del modelo con este setValue en caliente (cuando la tabla ya esté en pantalla)
		// y comprueba que la tabla no se refresca automáticamente 
		// Porque el modificador de dato debe notificar ese cambio a la JTable, para eso están los escuchadores
		// (observar quién y cuándo llama al addTableModelListener de abajo)
		// Por ejemplo podríamos añadir esto:
		// for (TableModelListener l : myListeners) {
		// 	l.tableChanged( new TableModelEvent( this, rowIndex, rowIndex, columnIndex ));
		// }
	}
		private ArrayList<TableModelListener> myListeners = new ArrayList<>();
	@Override
	public void addTableModelListener(TableModelListener l) {
		System.out.println( "Me llaman (escuchador)!" );
		myListeners.add( l );
	}
	@Override
	public void removeTableModelListener(TableModelListener l) {
		myListeners.remove( l );
	}

}
