package es.deusto.prog3.cap01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class EjemploLambda1 {
	private static JFrame ventana;
	private static JButton boton;
	public static void creaVentana() {
		ventana = new JFrame();
		boton = new JButton();
		ventana.add( boton );
		boton.addActionListener( 
//			new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//				}
//			}
			(e) -> { System.out.println( e.getWhen() );}
		);
		ventana.setVisible( true );
	}
	public static void main(String[] args) {
		// ...
		SwingUtilities.invokeLater(
//				new Runnable() {
//					public void run() {
//						creaVentana();
//					}
//				}
				// () -> { creaVentana(); }  // Interfaz
				EjemploLambda1::creaVentana
		);
	}
}
