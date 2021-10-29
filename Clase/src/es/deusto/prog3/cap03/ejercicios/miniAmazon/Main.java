package es.deusto.prog3.cap03.ejercicios.miniAmazon;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JFrame{

	private static Main ventana;
	public static void main(String[] args) {
		ventana = new Main();
		ventana.setVisible( true );
	}
	
	private JTextArea taDatos;
	private ArrayList<Producto> listaProds;
	
	public Main() {
		// Punto 7
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 1000, 800 );
		setTitle( "Ejercicio 3.6 de base de datos" );
		taDatos = new JTextArea();
		taDatos.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
		JPanel pBotonera = new JPanel();
		getContentPane().add( new JScrollPane(taDatos), BorderLayout.CENTER );
		getContentPane().add( pBotonera, BorderLayout.SOUTH );

		// Punto 8
		// TODO
		
		// Punto 9
		JButton b = new JButton( "Ver productos" );
		b.setFont( new Font( "Arial", Font.PLAIN, 16 ) );
		pBotonera.add( b );
		b.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clickVerProductos();
			}
		});
		
		// Punto 10
		b = new JButton( "Compra" );
		b.setFont( new Font( "Arial", Font.PLAIN, 16 ) );
		pBotonera.add( b );
		b.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clickComprar();
			}
		});
		
		// Punto 12
		b = new JButton( "Anular compra" );
		b.setFont( new Font( "Arial", Font.PLAIN, 16 ) );
		pBotonera.add( b );
		b.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clickAnular();
			}
		});
		
	}

	// 9
	private void clickVerProductos() {
		// TODO
	}
	
	// 10
	private void clickComprar() {
		// TODO
	}
	
	// 12
	private void clickAnular() {
		// TODO
	}
		private static SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yy HH:mm:ss" );
	
}
