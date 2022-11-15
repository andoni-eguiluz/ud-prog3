package es.deusto.prog3.cap06.resueltos;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/** Clase de ejemplo para entender el concepto de renderers y editors
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploRenderersYEditors {

	private static int filaEnClick = -1;
	private static int colEnClick = -1;
	
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		JFrame ventana = new JFrame( "Ejemplo de comprensión de renderers/editors" );
		ventana.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		ventana.setSize( 800, 600 );
		ventana.setLocation( 2000, 0 );  // TODO quitar si solo se tiene una ventana
		JComboBox<String> cb = new JComboBox<>( new String[] { "Admins", "Gestores", "Usuarios" } );
		ventana.add( cb, BorderLayout.NORTH );
		JList<Usuario> listaUsuarios = new JList<>( 
			new Usuario[] {
				new Usuario( 1, "Olatz" ),
				new Usuario( 2, "Asier" ),
				new Usuario( 3, "Arantza" ),
				new Usuario( 4, "Pablo" ),
				new Usuario( 5, "Marian" )
			}
		);
		ventana.add( new JScrollPane( listaUsuarios ), BorderLayout.WEST );
		Object[] columns = { "Val 1", "Val 2", "Val 3", "Val 4" };
		Object[][] datos = new Object[13][4];
		for (int i=0; i<13; i++) {
			for (int j=0; j<4; j++) {
				datos[i][j] = (int) (Math.random()*1000);
			}
		}
		JTable tabla = new JTable( datos, columns );
		
		ventana.add( new JScrollPane( tabla ), BorderLayout.CENTER );

		cb.setRenderer( new ListCellRenderer<String>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
					boolean isSelected, boolean cellHasFocus) {
				System.out.println( value + " - " + index );
				JLabel etiqueta = new JLabel( value );
				etiqueta.setOpaque( true );
				if (isSelected || cellHasFocus) {
					JButton boton = new JButton( value );  // Se pueden poner componentes interactivos pero OJO solo se usan para dibujar
					return boton;
					// JCheckBox check = new JCheckBox( value );
					// return check;
				} else {
					etiqueta.setBackground( Color.white );
				}
				return etiqueta;
			}
		});
		
		listaUsuarios.setCellRenderer( new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				// System.out.println( c.getClass().getSuperclass().getSuperclass() );
				JLabel l = (JLabel) c;  // Siempre es un JLabel
				l.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
				if (index % 2 ==0) {
					l.setBackground( Color.yellow );
				}
				return c;
			}
		});
		
		tabla.setRowHeight( 100 ); // Cambiar la altura de las filas
		tabla.setCellSelectionEnabled( true );  // Selección solo de celda
		tabla.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component ret = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				// System.out.println( ret.getClass().getSuperclass().getSuperclass() );
				JLabel l = (JLabel) ret;
				l.setIcon( null ); // (**) Esto para evitar que se dupliquen los icons de la línea de abajo
				l.setBackground( Color.WHITE );  // Lo mismo
				// El cambio de tamaño del componente renderer no afecta a la celda (el tamaño de la celda lo determina la tabla, no el renderer que dibuja en el espacio que tiene)
				// l.setMinimumSize( new Dimension( 200, 150 ) );  // No afecta
				// l.setPreferredSize( new Dimension( 200, 150 ) );  // No afecta
				// l.setSize( new Dimension( 200, 150 ) );  // No afecta
				l.setHorizontalAlignment( JLabel.CENTER );
				l.setFont( new Font( "Arial", Font.PLAIN, 40 ) );
				if ( (row + column)%2==0 ) {
					l.setForeground( Color.BLUE );
				}
				else {
					l.setForeground( Color.BLACK );
				}
				if (value instanceof Integer) {
					int valor = (Integer) value;
					if (valor <= 150) {
						l.setBackground( Color.RED );
					} else {
						// Obsérvese qué pasa si no hay else
						l.setBackground( Color.WHITE );
					}
					if (valor > 900) {
						// Para dibujar distinto hay que cambiar el dibujado del componente: no nos vale el JLabel normal
						JLabel lCambiada = new JLabel( value.toString() ) {
							protected void paintComponent(Graphics g) {
								// System.out.println( "paintComponent " + value.toString() );
								if (isSelected) {
									g.setColor( Color.LIGHT_GRAY );
								} else {
									g.setColor( Color.WHITE );
								}
								g.fillRect( 0,  0,  getWidth(),  getHeight() );
								ImageIcon img = new ImageIcon( EjemploRenderersYEditors.class.getResource( "world.png" ) );
								g.drawImage( img.getImage(), 0, 0, getWidth(), getHeight(), null );
								g.drawLine( 0, 0, getWidth(), getHeight());
								super.paintComponent( g );
							}
						};
						lCambiada.setHorizontalAlignment( JLabel.CENTER );
						lCambiada.setFont( new Font( "Arial", Font.PLAIN, 40 ) );
						lCambiada.setForeground( Color.MAGENTA );
						return lCambiada;
					} else if (valor > 800) {
						l.setIcon( new ImageIcon( EjemploRenderersYEditors.class.getResource( "world2.png" ) ) );
						// Obsérvese qué pasa si no hacemos la línea (**) de arriba
					}
				}
				if (isSelected) {
					l.setBackground( Color.LIGHT_GRAY );
				}
				// Comunicación entre el renderer y el mouselistener
				if (row == filaEnClick && column == colEnClick) {
					l.setForeground( Color.GREEN );
				}
				
				// Prueba posterior: añadir un color para las filas con false en la columna 3
				// System.out.println( "VALOR DE COL 3 en fila " + row + " = " + tabla.getModel().getValueAt( row, 3 ) );
				Object valorCol3 = tabla.getModel().getValueAt( row, 3 );
				if (valorCol3 instanceof Boolean) {
					Boolean valorBool = (Boolean) valorCol3;
					if (!valorBool) {
						l.setBackground( Color.YELLOW );
					}
				}
				return l;
			}
		} );

		// EDITORES
		tabla.setDefaultEditor( Object.class, new DefaultCellEditor( new JTextField() ) {
			// TODO - Cambiar solo lo que quiera que se haga distinto
		});
		
		tabla.setDefaultEditor( Object.class, new TableCellEditor() {
			private ArrayList<CellEditorListener> lCEL = new ArrayList<>();
			
			@Override
			public boolean isCellEditable(EventObject anEvent) {
				// Si se quiere que no se entre con click, sino con doble click...
				if (anEvent instanceof MouseEvent) {
					return ((MouseEvent) anEvent).getClickCount() >= 2;
				}
				return true;
			}
			@Override
			public void addCellEditorListener(CellEditorListener l) {
				lCEL.add( l );
				// System.out.println( "addCellEditorListener" );
			}
			@Override
			public void removeCellEditorListener(CellEditorListener l) {
				lCEL.remove( l );
				// System.out.println( "removeCellEditorListener" );
			}
			@Override
			public boolean stopCellEditing() {
				for (int i = lCEL.size()-1; i>=0; i--) {
					lCEL.get(i).editingStopped( new ChangeEvent(this) );
				}
				return true;
			}
			@Override
			public void cancelCellEditing() {
				for (int i = lCEL.size()-1; i>=0; i--) {
					lCEL.get(i).editingCanceled( new ChangeEvent(this) );
				}
			}
			private JTextField tf;
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
				tf = new JTextField( value.toString() );
				tf.setFont( new Font( "Arial", Font.PLAIN, 40 ) );
				tf.setForeground( Color.GRAY );
				tf.setHorizontalAlignment( JTextField.CENTER );
				return tf;
			}
			@Override
			public Object getCellEditorValue() {
				try {
					int valor = Integer.parseInt( tf.getText() );
					return valor;
				} catch (NumberFormatException e) {
					// Si no es int, probamos si es double
					try {
						double valor = Double.parseDouble( tf.getText() );
						return valor;
					} catch (NumberFormatException e2) {
						// Si no es double, entonces lo dejamos como string
						return tf.getText();
					}
				}
			}
			@Override
			public boolean shouldSelectCell(EventObject anEvent) {
				return true;
			}
		});
		
		// MODELOS
		Object[][] datosNuevos = new Object[][] {
			{ 900, 200.5, "Itziar", false },
			{ 400, 20.0, "Ignacio", true },
			{ 300, 50.5, "Aitor", false },
			{ 850, 100.0, "Luis", false }
		};
		String[] columnasNuevas = new String[] { "Id", "Saldo", "Nick", "Premium" };
		MiModeloDeTabla modelo = new MiModeloDeTabla( datosNuevos );
		tabla.setModel( modelo );

		tabla.addMouseListener( new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// System.out.println( "MReleased en " + e );
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// System.out.println( "MPressed en " + e );
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// System.out.println( "MExited en " + e );
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// System.out.println( "MEntered en " + e );
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// int antFila = filaEnClick; int antCol = colEnClick;  // Si hace falta (nota de abajo) (***)
				System.out.println( "MClicked en " + e );
				filaEnClick = tabla.rowAtPoint( e.getPoint() );
				colEnClick = tabla.columnAtPoint( e.getPoint() );
				System.out.println( "Click en tabla fila,columna = " + filaEnClick + "," + colEnClick );
				tabla.repaint();
				// (***)
				// Se podría repintar solo una celda, pero hay que contar entonces con el modelo:
				// Hacer como si el modelo actualizara un valor
				// modelo.fireTableChanged( new TableModelEvent( modelo, filaEnClick, filaEnClick, colEnClick ) );
				// Ojo porque si queremos pintar una de verde y que la que antes estaba de verde se quite,
				// entonces también habría que refrescar la que ha "perdido el verde":
				// modelo.fireTableChanged( new TableModelEvent( modelo, antFila, antFila, antCol ) );
				// En resumen, es más cómodo repintar toda la tabla aunque más eficiente refrescar solo
				// las celdas que cambian
			}
			
		});

		// Tarea intercambio
		MouseAdapter ma = new MouseAdapter() {  // MouseAdapter implementa tanto ML como MML
			int filaPul;
			int colPul;
			@Override
			public void mousePressed(MouseEvent e) {
				filaPul = tabla.rowAtPoint( e.getPoint() );
				colPul = tabla.columnAtPoint( e.getPoint() );
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				int filaSuel = tabla.rowAtPoint( e.getPoint() );
				int colSuel = tabla.columnAtPoint( e.getPoint() );
				// Trabajo del intercambio
				if (colPul == 2 && colSuel == 2 && filaSuel!=filaPul) {
					Object temporal = modelo.getValueAt(filaSuel, colSuel);
					modelo.setValueAt( modelo.getValueAt(filaPul, colPul), filaSuel, colSuel);
					modelo.setValueAt( temporal, filaPul, colPul );
					tabla.repaint();
				}
			}
			// MouseAdapter -> Mouse + MouseMotion (moved, dragged)
			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println( "Drag: " + e.getPoint() );
				// TODO hacer aquí lo que se quiera con el drag - por ejemplo que se pinte 
				// encima de la tabla la celda que se está moviendo
			}
		};
		tabla.addMouseListener( ma );
		tabla.addMouseMotionListener( ma );
				
		// Tarea tecla supr
		KeyListener kl = new KeyListener() {
			private boolean ctrlPulsado = false;
			private final int COD_SUPR = 127;
			@Override
			public void keyTyped(KeyEvent e) {
				// System.out.println( "Typed " + e );
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// System.out.println( "Released " + e );
				if (e.getKeyCode()==KeyEvent.VK_DELETE) { // No tiene sentido && e.getKeyCode()==KeyEvent.VK_CONTROL) {
					System.out.println( "Pulsada DEL");
					// e.isControlDown() - se puede usar en lugar del booleano
					if (tabla.getSelectedColumn() == 2 && ctrlPulsado) {
						int fila = tabla.getSelectedRow();
						int col = tabla.getSelectedColumn();
						modelo.setValueAt( "", fila, col);
						tabla.repaint();
					}
				} else if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
					ctrlPulsado = false;
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println( "Pressed " + e );
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
					ctrlPulsado = true;
				}
			}
		};
		tabla.addKeyListener( kl );
		// cb.addKeyListener( kl );
		// listaUsuarios.addKeyListener( kl );
		
		
		ventana.setVisible( true );
	}

	// Clase privada para el modelo de datos 
	
	private static class MiModeloDeTabla implements TableModel {
		// Datos con los que trabajar
		Object[][] misDatos;
		
		// Inicialización en el constructor
		public MiModeloDeTabla( Object[][] datos ) {
			misDatos = datos;
		}
		
		// Los listeners
		ArrayList<TableModelListener> lista = new ArrayList<>();
		@Override
		public void removeTableModelListener(TableModelListener l) {
			lista.remove( l );
		}
		@Override
		public void addTableModelListener(TableModelListener l) {
			lista.add( l );
		}

		// Sin estas no hay nada que hacer (ver que sin tocarlas no se ve la tabla o casca)
		@Override
		public int getRowCount() {
			return misDatos.length;
		}
		@Override
		public int getColumnCount() {
			return misDatos[0].length;
		}
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// Podríamos devolver un tipo distinto por cada columna, por ejemplo
			// (esto nos podría servir para configurar renderers distintos por columna)
			// El de String coge el de Object si no hay particular de String, sin embargo el de Boolean coge un checkbox por defecto
			if (columnIndex==2) {
				return String.class;
			} else if (columnIndex==3) {
				return Boolean.class;
			}
			return Object.class;
		}

		// Nada de esto sirve si el modelo no sabe QUÉ datos devolver
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// System.out.println( "getValue " + rowIndex + "," + columnIndex );
			return misDatos[rowIndex][columnIndex];
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			// System.out.println( "setValue " + rowIndex + "," + columnIndex + " = " + aValue );
			misDatos[rowIndex][columnIndex] = aValue;
			// ¿Cómo hacer que un entero siga siendo un entero?
			// Lo podemos hacer en el propio editor, o forzarlo aquí en el modelo. Por ejemplo
			// if (columnIndex==0) {
			// 	try {
			// 		int valor = Integer.parseInt( (String) aValue );
			// 		datosNuevos[rowIndex][columnIndex] = Integer.valueOf( valor );
			// 	} catch (NumberFormatException e) {}
			// } else if (columnIndex==1) {
			// 	try {
			// 		double valor = Double.parseDouble( (String) aValue );
			// 		datosNuevos[rowIndex][columnIndex] = Double.valueOf( valor );
			// 	} catch (NumberFormatException e) {}
			// }
		}
		
		// Y esto no funciona si la celda no es editable (para poder editar tienen que ser true los dos isCellEditable, el editor y el modelo de datos)
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex==2) {
				return false;
			}
			return true;
		}
		
		// Y habíamos perdido las cabeceras!  Van también con el modelo: aquí están.
		@Override
		public String getColumnName(int columnIndex) {
			return (new String[] { "Dato 1", "Dato 2", "Nombre", "¿Director/a?" })[columnIndex];
		}
		
		// Y aunque el interfaz no lo obligue, se puede hacer un método para que se propaguen eventos 
		// a los escuchadores. Por ejemplo (DefaultTableModel lo hace así):
	    public void fireTableChanged(TableModelEvent e) {
	        // Procesar los escuchadores al revés, notificando el evento
	        for (int i = lista.size()-1; i>=0; i--) {
	        	lista.get(i).tableChanged( e );
	        }
	    }
	}
	
}

// Clase de ejemplo
class Usuario {
	private int id;
	private String nick;
	public Usuario(int id, String nick) {
		super();
		this.id = id;
		this.nick = nick;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	@Override
	public String toString() {
		return nick + " (" + id + ")";
	}
}
