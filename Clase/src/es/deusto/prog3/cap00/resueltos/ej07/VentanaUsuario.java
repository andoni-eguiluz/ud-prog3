package es.deusto.prog3.cap00.resueltos.ej07;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class VentanaUsuario extends JFrame{
	protected JPanel pan = new JPanel();
	protected JTextField tfNombre = new JTextField( "", 20 );
	protected JTextField tfPassword = new JTextField( "", 10 );
	protected JPanel pReservado = new JPanel();
	
	protected Usuario usuario;  // Usuario asociado a la ventana

	public VentanaUsuario() {
		setSize(600, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		add(pan);
		JPanel pContenido = new JPanel();
		pContenido.setLayout( new BoxLayout( pContenido , BoxLayout.Y_AXIS ));
		JPanel p1 = new JPanel();
		p1.add( new JLabel("Nombre") );
		p1.add( tfNombre );
		pContenido.add( p1 );
		JPanel p2 = new JPanel();
		p2.add( new JLabel("Contraseña") );
		p2.add( tfPassword );
		pContenido.add( p2 );
		add( pContenido, BorderLayout.NORTH );
		pReservado = new JPanel();
		add( pReservado, BorderLayout.CENTER );  // Reserva un panel en el centro (para las clases hijas)
		tfNombre.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				usuario.setNombre( tfNombre.getText() );
			}
		});
		tfPassword.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				usuario.setPassword( tfPassword.getText() );
			}
		});
	}
	
	public void muestraUsuario(Usuario u) {
		usuario = u;  // Almacena el usuario
		tfNombre.setText( u.getNombre() ); // Actualiza los datos de la ventana
		tfPassword.setText( u.getPassword() );
	}
}
