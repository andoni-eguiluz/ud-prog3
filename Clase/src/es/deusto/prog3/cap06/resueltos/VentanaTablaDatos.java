package es.deusto.prog3.cap06.resueltos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;

public class VentanaTablaDatos extends JFrame {

	private JTable tablaDatos;
	private DataSetMunicipios datosMunis;
	private MiTableModel modeloDatos;
	
	public VentanaTablaDatos( JFrame ventOrigen ) {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		setLocationRelativeTo( null );
		
		tablaDatos = new JTable();
		add( new JScrollPane( tablaDatos ), BorderLayout.CENTER );
		
		JPanel pInferior = new JPanel();
		JButton bAnyadir = new JButton( "Añadir" );
		JButton bBorrar = new JButton( "Borrar" );
		pInferior.add( bAnyadir );
		pInferior.add( bBorrar );
		add( pInferior, BorderLayout.SOUTH );
		
		// Paso 2
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				ventOrigen.setVisible( false );
			}
			@Override
			public void windowClosed(WindowEvent e) {
				ventOrigen.setVisible( true );
			}
		});
	}
	
	public void setDatos( DataSetMunicipios datosMunis ) {
		this.datosMunis = datosMunis;
		modeloDatos = new MiTableModel();
		tablaDatos.setModel( modeloDatos );
	}
	
	private class MiTableModel implements TableModel {

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			System.out.println( "getColumnClass" + columnIndex );
			return Object.class;
		}

		@Override
		public int getColumnCount() {
			System.out.println( "getColumnCount" );
			return 5;
		}

		@Override
		public int getRowCount() {
			System.out.println( "getRowCount" );
			return 10;
		}

		private String[] cabeceras = { "Código", "Nombre", "Habitantes", "Provincia", "Autonomía" };
		@Override
		public String getColumnName(int columnIndex) {
			// System.out.println( "getColumnName " + columnIndex );
			return cabeceras[columnIndex];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// System.out.println( "getValueAt " + rowIndex + "," + columnIndex );
			switch (columnIndex) {
			case 0:
				return datosMunis.getListaMunicipios().get(rowIndex).getCodigo();
			case 1:
				return datosMunis.getListaMunicipios().get(rowIndex).getNombre();
			case 2:
				return datosMunis.getListaMunicipios().get(rowIndex).getHabitantes();
			case 3:
				return datosMunis.getListaMunicipios().get(rowIndex).getProvincia();
			case 4:
				return datosMunis.getListaMunicipios().get(rowIndex).getAutonomia();
			default:
				return null;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
