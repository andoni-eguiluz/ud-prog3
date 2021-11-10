package es.deusto.prog3.cap06;

import java.awt.BorderLayout;
import javax.swing.*;

import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;

public class VentanaPorDentro {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize( 400, 300 );
		JPanel p = new JPanel();
		p.add( new JButton("1") );
		p.add( new JButton("2") );
		p.add( new JButton("3") );
		f.getContentPane().add( new JLabel("l"), BorderLayout.NORTH );
		f.getContentPane().add( new JScrollPane( new JTextArea() ), BorderLayout.CENTER );
		f.getContentPane().add( p, BorderLayout.SOUTH );
		VentanaBancoDePruebas vent = new VentanaBancoDePruebas( f );
		f.setVisible( true );
		vent.setVisible( true );
	}
	
}
