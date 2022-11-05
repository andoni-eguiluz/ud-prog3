package es.deusto.prog3.cap06.resueltos;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/** Clase de ejemplo para entender el concepto de renderers y editors
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploRenderersYEditors {

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		JFrame ventana = new JFrame( "Ejemplo de comprensión de renderers/editors" );
		ventana.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		ventana.setSize( 800, 600 );
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
								if (isSelected) {
									g.setColor( Color.LIGHT_GRAY );
								} else {
									g.setColor( Color.WHITE );
								}
								g.fillRect( 0,  0,  getWidth(),  getHeight() );
								ImageIcon img = new ImageIcon( EjemploRenderersYEditors.class.getResource( "world.png" ) );
								g.drawImage( img.getImage(), 0, 0, getWidth(), getHeight(), null );
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
				return l;
			}
		} );

		
		ventana.setVisible( true );
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
