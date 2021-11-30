package es.deusto.prog3.cap06;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Clase para probar y entender el funcionamiento de la JTable
 * con su modelo de datos, y sus renderers y editors
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ProbandoJTable {
	private static JFrame vent;
	private static JTable tabla;
	private static DefaultTableModel modelo;
	
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		// Ventana rápida
		vent = new JFrame();
		vent.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		vent.setSize( 400, 300 );
		// Para investigar la pantalla clase Toolkit y GraphicsEnvironment: 
		// Toolkit.getDefaultToolkit().getScreenSize()
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()
		// Por ejemplo:
		GraphicsDevice[] pantallas = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices(); // Array de pantallas
		int xUltimaPant = pantallas[ pantallas.length-1 ].getDefaultConfiguration().getBounds().x;  // Bounds de la última pantalla
		vent.setLocation(xUltimaPant + 100, 0);  // 100 de la izquierda de la última pantalla
		
		// Tabla en ventana
		modelo = new DefaultTableModel( new Object[] { "Nom", "Cod" }, 0 );
		tabla = new JTable( modelo );
		vent.getContentPane().add( new JScrollPane( tabla ), BorderLayout.CENTER );
		
		// Añadir datos
		modelo.addRow( new Object[] { "Itziar", 70 } );
		Vector<Object> v = new Vector<>();
		v.add( "Andoni" );
		v.add( 120 );
		modelo.addRow( v );
		modelo.addRow( new Object[] { "Xabi", 220 } );
		
		// Cambios de anchura
		tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
		tabla.getColumnModel().getColumn(1).setMaxWidth(100);
		tabla.getColumnModel().getColumn(1).setMinWidth(80);
		
		// Investiguemos el modelo
		modelo = new DefaultTableModel( new Object[] { "Nom", "Cod" }, 0 ) { // Herencia

//			@Override
//			public Class<?> getColumnClass(int columnIndex) {
//				// TODO Auto-generated method stub
//				return super.getColumnClass(columnIndex);
//			}

			@Override
			public boolean isCellEditable(int row, int column) {
				boolean val = super.isCellEditable(row, column);
				System.out.println( "IsCellEditable " + row + "," + column + " -> " + val);
				if (column==0) return false;
				else return super.isCellEditable(row, column);
			}

			@Override
			public Object getValueAt(int row, int column) {
				Object val = super.getValueAt(row, column);
				System.out.println( "getValueAt " + row + "," + column + " <- " + val );
				return val;
			}

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				System.out.println( "setValueAt " + row + "," + column + " -> " + aValue );
				if (column==0) {  // En columna 0 funciona normal
					super.setValueAt(aValue, row, column);
				} else {  // En columna 1 comprueba el valor
					try {
						int valor = Integer.parseInt( aValue + "" );
						if (valor<=255) {
							super.setValueAt(aValue, row, column);
						}
					} catch (NumberFormatException e) {
						// No cambiar nada
					}
				}
			} 
			
		};
		modelo.addRow( new Object[] { "Itziar", 70 } );
		modelo.addRow( new Object[] { "Andoni", 120 } );
		modelo.addRow( new Object[] { "Xabi", 220 } );
		tabla.setModel( modelo );
	
		tabla.getTableHeader().setReorderingAllowed(false);  // Prohibe el movimiento de columnas del usuario
		
		tabla.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// System.out.println( (comp instanceof JLabel) + " -> " + comp );
				JLabel label = (JLabel) comp;
				if (column==0) {
					comp.setBackground( Color.white );
				} else {
					int valor = (Integer) modelo.getValueAt(row, column);
					// int valor = Integer.parseInt( value.toString() );
					comp = new JSlider( 0, 255, valor );
				}
				if (isSelected) {
					comp.setBackground( Color.LIGHT_GRAY );
					if (hasFocus) {
						label.setBorder( BorderFactory.createLineBorder( Color.cyan, 3 ));
					}
				}
				return comp;
			}
			
		});
		
		tabla.setDefaultEditor( Object.class, new DefaultCellEditor( new JTextField() ) {
			Component ret;
			int valorAnt;
			JTextField tf;
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
					int column) {
				ret = super.getTableCellEditorComponent(table, value, isSelected, row, column);
				tf = (JTextField) ret;
				if (column==1) {
					tf = null;
					int valor = Integer.parseInt( value.toString() );
					ret = new JSlider( 0, 255, valor );
					((JSlider)ret).addChangeListener( new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							valorAnt = ((JSlider)ret).getValue();
						}
					});
				}
				return ret;
			}

			@Override
			public Object getCellEditorValue() {
				if (tf!=null) {
					return tf.getText();
				} else {
					return new Integer( valorAnt );
				}
			}
			
		});

		// Y eventos de ratón? La base...
		tabla.addMouseListener( new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tabla.rowAtPoint( e.getPoint() );
				int col = tabla.columnAtPoint( e.getPoint() );
				System.out.println( "Click en fila " + fila + "," + col );
			}
		});

		
		vent.setVisible( true );
	}
}
