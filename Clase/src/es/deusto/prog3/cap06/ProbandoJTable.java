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
		tabla = new JTable( modelo ) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
			}
		};
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

		} );
		
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
		MouseAdapter ma = new MouseAdapter() {  // MouseAdapter implementa MOuseListener y MouseMotionListener
			private int filaPul;
			private int colPul;
			@Override
			public void mousePressed(MouseEvent e) {
				filaPul = tabla.rowAtPoint( e.getPoint() );
				colPul = tabla.columnAtPoint( e.getPoint() );
				// mouseReleased - aquí no se puede gestionar
				System.out.println( "Pressed en " + filaPul + "," + colPul );
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				int filaSuel = tabla.rowAtPoint( e.getPoint() );
				int colSuel = tabla.columnAtPoint( e.getPoint() );
				// Tendrá que haber habido un press
				System.out.println( "Released en " + filaSuel + "," + colSuel );
				if (colPul==0 && colSuel==0 && filaSuel!=filaPul && filaSuel>=0 && filaPul>=0) {
					Object temp = modelo.getValueAt(filaPul, colPul);
					modelo.setValueAt( modelo.getValueAt(filaSuel, colSuel), filaPul, colPul );
					modelo.setValueAt( temp, filaSuel, colSuel );
				}
				tabla.repaint(); // Para que se repinte y quede bien toda la tabla tras el dibujado del drag
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				// System.out.println( "MM " + e );
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				// System.out.println( "MD " + e );
				if (colPul==0) {
					Graphics g = tabla.getGraphics();
					// tabla.repaint(); // No funciona porque no es síncrona la llamada, pero sí se pueden pintar las celdas que queramos antes del drag, por ejemplo:
					// Dibujar toda la tabla de nuevo:
					for (int fila=0; fila<modelo.getRowCount(); fila++) {
						for (int col= 0; col<modelo.getColumnCount(); col++) {
							Rectangle rectCelda = tabla.getCellRect( fila, col, false );
							Graphics gCelda = g.create(rectCelda.x, rectCelda.y, rectCelda.width, rectCelda.height );
							Component cCelda = tabla.getDefaultRenderer(Object.class).getTableCellRendererComponent(tabla, modelo.getValueAt(fila, col), false, false, fila, col );
							cCelda.setSize( rectCelda.width, rectCelda.height );
							cCelda.paint( gCelda );  // Dibujo de componente
							gCelda.drawRect( 0, 0, rectCelda.width, rectCelda.height );  // Dibujo de borde
						}
					}
					// Dibujar la etiqueta que se mueve con el drag:
					JLabel label = new JLabel( modelo.getValueAt( filaPul, colPul ).toString() );
					label.setOpaque( true );
					label.setSize( tabla.getColumnModel().getColumn(0).getWidth(), tabla.getRowHeight() );
					// label.setLocation( 50, 50 );   Esto no funciona porque el render siempre se dibuja en 0,0
					Graphics g2 = g.create( e.getX() - label.getWidth()/2, e.getY() - label.getHeight()/2, label.getWidth(), label.getHeight() );
					// g.translate( e.getX(), e.getY() );  Otra manera de hacerlo
					label.paint( g2 );
					// g.drawLine( 0, 0, e.getX(), e.getY() );  // Si quisiéramos pintar líneas o cualquier otra cosa
				}
			}
		};
		tabla.addMouseListener( ma );
		tabla.addMouseMotionListener( ma );

		// Y eventos de teclado? - por ejemplo tecla supr para borrar el contenido de la celda donde se pulsa
		
		tabla.addKeyListener( new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// Supr no se puede usar con keytyped, hay teclas no visibles al "typear"
				System.out.println( e.getKeyCode() );
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				// Aquí sí funciona
				System.out.println( e.getKeyCode() );
				if (e.getKeyCode()==KeyEvent.VK_DELETE) {
					int fila = tabla.getSelectedRow();
					int col = tabla.getSelectedColumn();
					String val = "0";
					if (col==0) val = "";
					if (col>=0 && fila>=0) modelo.setValueAt( val, fila, col );  // Borramos la celda seleccionada
				}
			}
		});
		
		vent.setVisible( true );
		
	}
	
}
