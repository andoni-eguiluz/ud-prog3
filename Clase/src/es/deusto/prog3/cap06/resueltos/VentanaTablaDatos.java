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

	// Paso 11 - atributos
	String autonomiaSeleccionada = "";
	
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
		
		// Paso 6 - inserción
		bAnyadir.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int filaSel = tablaDatos.getSelectedRow();
				if (filaSel>=0) {
					datosMunis.anyadir( new Municipio( datosMunis.getListaMunicipios().size()+1, "Nombre", 0, "Provincia", "Autonomía" ), filaSel );
					modeloDatos.anyadeFila( filaSel );
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
		tablaDatos.setDefaultRenderer( Integer.class, new DefaultTableCellRenderer() {
			// private JProgressBar pbHabs = new JProgressBar( 0, 5000000 );
			// Paso 10:
			private JProgressBar pbHabs = new JProgressBar( 0, 5000000 ) {
				protected void paintComponent(java.awt.Graphics g) {
					super.paintComponent(g);
					g.setColor( Color.BLACK );
					g.drawString( getValue()+"", 50, 10 );
				}
			};
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				System.out.println( "getTCR " + row + "," + column );
				if (column==2) {
					// Si el dato es un Object o String sería esto
					// int valorCelda = Integer.parseInt( value.toString() );
					// pbHabs.setValue( valorCelda );
					// return pbHabs;
					// Pero si el dato está asegurado ser un Integer se puede castear:
					pbHabs.setValue( (Integer)value );
					return pbHabs;
				}
				JLabel rendPorDefecto = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				return rendPorDefecto;
			}
			
		});
		
		// Paso 11
		tablaDatos.setDefaultRenderer( String.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column );
				c.setBackground( Color.WHITE );
				if (isSelected) {  // Observa que si no se pone esto uno no se entera cuándo la fila está seleccionada
					c.setBackground( Color.LIGHT_GRAY );
				}
				if (column==4) { // Paso 11 del ejercicio
					if (autonomiaSeleccionada.equals( (String)value )) {
						c.setBackground( Color.CYAN );
					}
				}
				return c;
			}
		} );
		
		// Paso 8
		tablaDatos.addMouseMotionListener( new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int filaEnTabla = tablaDatos.rowAtPoint( e.getPoint() );
				int colEnTabla = tablaDatos.columnAtPoint( e.getPoint() );
				if (colEnTabla == 2) {
					int numHabs = datosMunis.getListaMunicipios().get(filaEnTabla).getHabitantes();
					tablaDatos.setToolTipText( String.format( "Población: %,d", numHabs ) );
				} else {
					tablaDatos.setToolTipText( null );  // Desactiva
				}
			}
		});

		// Paso 11
		tablaDatos.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int filaEnTabla = tablaDatos.rowAtPoint( e.getPoint() );
				int colEnTabla = tablaDatos.columnAtPoint( e.getPoint() );
				if (colEnTabla == 4 && filaEnTabla>=0) {
					autonomiaSeleccionada = datosMunis.getListaMunicipios().get(filaEnTabla).getAutonomia();
				} else {
					autonomiaSeleccionada = "";
				}
				tablaDatos.repaint();
			}
		});

		// Paso 12
		tablaDatos.setDefaultEditor( Integer.class, new DefaultCellEditor( new JTextField() ) {
			SpinnerNumberModel mSpinner = new SpinnerNumberModel( 200000, 200000, 5000000, 1000 );
			JSpinner spinner = new JSpinner( mSpinner );
			boolean lanzadoSpinner;
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { // Componente que se pone en la tabla al editar una celda
				if (column != 2) {
					lanzadoSpinner = false;
					return super.getTableCellEditorComponent(table, value, isSelected, row, column);
				}
				mSpinner.setValue( (Integer) value );
				lanzadoSpinner = true;
				return spinner;
			}
			@Override
			public Object getCellEditorValue() { // Valor que se retorna al acabar la edición
				if (lanzadoSpinner) {
					return spinner.getValue();
				} else {
					return Integer.parseInt( super.getCellEditorValue().toString() );
				}
			}
		});
		
	}
	
	private class MiTableModel implements TableModel {

		// Paso 7
		private final Class<?>[] CLASES_COLS = { Integer.class, String.class, Integer.class, String.class, String.class };
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// Si no se quiere especializar cada columna:
			// return Object.class;
			return CLASES_COLS[columnIndex]; // Paso 7
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
					// Cuando no estaba especializada la columna había que tratarla como Object
					// datosMunis.getListaMunicipios().get(rowIndex).setHabitantes( Integer.parseInt((String)aValue) );
					// Pero ahora puede tratarse como Integer:
					datosMunis.getListaMunicipios().get(rowIndex).setHabitantes( (Integer) aValue );
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
		
	    // Paso 5
		public void borraFila( int fila ) {
			fireTableChanged( new TableModelEvent( modeloDatos, fila, datosMunis.getListaMunicipios().size() ));
		}
		
	    // Paso 6
	    public void anyadeFila( int fila ) {
	    	fireTableChanged( new TableModelEvent( modeloDatos, fila, datosMunis.getListaMunicipios().size() ) );  // Para que detecte el cambio en todas
	    }
	    
	}
}
