package es.deusto.prog3.cap01;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/** Un par de ejemplos de anotaciones: SuppressWarnings y Override
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploAnotaciones {

	public static void main(String[] args) {
		MiVentana v = new MiVentana();
		v.setVisible( true );
	}

	@SuppressWarnings("serial")
	private static class MiVentana extends JFrame {
		public MiVentana() {
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 400, 300 );
			JButton b = new JButton( "Ciérrame!" );
			add( b, BorderLayout.CENTER );
			b.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MiVentana.this.dispose();  // o simplemente dispose();
				}
			} );
		}
	}
}
