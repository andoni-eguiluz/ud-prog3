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
					JButton boton = new JButton( value );
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
