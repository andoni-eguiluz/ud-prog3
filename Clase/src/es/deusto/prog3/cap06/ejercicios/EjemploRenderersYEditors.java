package es.deusto.prog3.cap06.ejercicios;

import java.awt.*;
import javax.swing.*;

/** Clase de ejemplo para entender el concepto de renderers y editors
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploRenderersYEditors {

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
