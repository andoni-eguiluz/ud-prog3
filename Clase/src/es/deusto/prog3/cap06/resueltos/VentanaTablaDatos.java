package es.deusto.prog3.cap06.resueltos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
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
	
		// Paso 5
		bBorrar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int filaSel = tablaDatos.getSelectedRow();
				if (filaSel >= 0) {
					datosMunis.quitar( datosMunis.getListaMunicipios().get(filaSel).getCodigo() );
					modeloDatos.borraFila(filaSel);
				}
			}
		});
	}
	
	public void setDatos( DataSetMunicipios datosMunis ) {
		this.datosMunis = datosMunis;
		modeloDatos = new MiTableModel();
		tablaDatos.setModel( modeloDatos );
		
		// Paso 4
		TableColumn col = tablaDatos.getColumnModel().getColumn( 0 );
		col.setMaxWidth( 50 );
		col = tablaDatos.getColumnModel().getColumn(2);
		col.setMinWidth( 150 );
		col.setMaxWidth( 150 );

		// Paso 7
		tablaDatos.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
			private JProgressBar pbHabs = new JProgressBar( 0, 5000000 );
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				System.out.println( "getTCR " + row + "," + column );
				if (column==2) {
					int valorCelda = Integer.parseInt( value.toString() );
					pbHabs.setValue( valorCelda );
					return pbHabs;
				}
				JLabel rendPorDefecto = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (row % 2 == 0) {
					rendPorDefecto.setBackground( Color.CYAN );
				}
				return rendPorDefecto;
			}
			
		});
		
	}
	
	private class MiTableModel implements TableModel {

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// System.out.println( "getColumnClass" + columnIndex );
			return Object.class;
		}

		@Override
		public int getColumnCount() {
			// System.out.println( "getColumnCount" );
			return 5;
		}

		@Override
		public int getRowCount() {
			// System.out.println( "getRowCount" );
			return datosMunis.getListaMunicipios().size();
		}

		private final String[] cabeceras = { "Código", "Nombre", "Habitantes", "Provincia", "Autonomía" };
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
			System.out.println( "isCellEditable" );
			if (columnIndex == 0) {
				return false;
			}
			return true;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			System.out.println( "setValue " + aValue + "[" + aValue.getClass().getName() + "] " + rowIndex + "," + columnIndex );
			switch (columnIndex) {
			case 0:
				datosMunis.getListaMunicipios().get(rowIndex).setCodigo( (Integer) aValue );
				break;
			case 1:
				datosMunis.getListaMunicipios().get(rowIndex).setNombre( (String) aValue );
				break;
			case 2:
				try {
					datosMunis.getListaMunicipios().get(rowIndex).setHabitantes( Integer.parseInt((String)aValue) );
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog( VentanaTablaDatos.this, "Nº de habitantes erróneo" );
				}
				break;
			case 3:
				datosMunis.getListaMunicipios().get(rowIndex).setProvincia( (String) aValue );
				break;
			case 4:
				datosMunis.getListaMunicipios().get(rowIndex).setAutonomia( (String) aValue );
				break;
			}
		}

		// Paso 5: trabajar con los escuchadores
		ArrayList<TableModelListener> listaEsc = new ArrayList<>();
		@Override
		public void addTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			System.out.println( "addTableModelListener" );
			listaEsc.add( l );
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			// TODO Auto-generated method stub
			listaEsc.remove( l );
		}
		
		// DefaultTableModel lo hace así
		public void fireTableChanged( TableModelEvent e ) {
			for (TableModelListener l : listaEsc) {
				l.tableChanged( e );
			}
		}
		
		public void borraFila( int fila ) {
			fireTableChanged( new TableModelEvent( modeloDatos, fila, datosMunis.getListaMunicipios().size() ));
		}
		
	}
}
