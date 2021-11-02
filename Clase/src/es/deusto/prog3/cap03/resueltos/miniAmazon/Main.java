package es.deusto.prog3.cap03.resueltos.miniAmazon;

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
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				BaseDatos.abrirConexion( "miniamazon.db" );
			}
			@Override
			public void windowClosed(WindowEvent e) {
				BaseDatos.cerrarConexion();
			}
		});
		
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
		taDatos.append( "\nProductos disponibles:\n");
		listaProds = BaseDatos.getProductos();
		for (Producto p : listaProds) {
			taDatos.append( p + "\n" );
		}
		taDatos.setSelectionStart( taDatos.getText().length() );  // Mueve el textarea al final del scroll vertical si se llena la pantalla
	}
	
	// 10
	private void clickComprar() {
		if (listaProds==null || listaProds.isEmpty()) return;
		Producto producto = selProducto();
		if (producto==null) return;  // No elegido producto
		do {  // Ciclo de validación de cantidad (pide cantidad hasta que sea correcta o escape
			try {
				String resp = JOptionPane.showInputDialog( ventana, "Cantidad a comprar:", "1" );
				if (resp==null) return; // No definida cantidad
				int cantidad = Integer.parseInt( resp );
				if (cantidad > 0) {  // Correcto: insertar
					Compra compra = new Compra( 0, System.currentTimeMillis(), cantidad, producto );  // id 0 porque no lo sabemos
					BaseDatos.insertarCompra( compra );  // El método modifica el id al insertarlo
					taDatos.append( "Añadida compra: " + compra + "\n" );
					taDatos.setSelectionStart( taDatos.getText().length() );  // Mueve el textarea al final del scroll vertical si se llena la pantalla
					break; // Sale del ciclo
				} else {
					// Error en cantidad no positiva
					JOptionPane.showMessageDialog( ventana, "Error en cantidad, introduce un número mayor que cero" );
				}
			} catch (NumberFormatException e) {
				// Error en cantidad
				JOptionPane.showMessageDialog( ventana, "Error en cantidad, introduce un número mayor que cero" );
			}
		} while (true);
	}
		private Producto selProducto() {
			String[] opciones = new String[ listaProds.size() ];
			int i = 0;
			for (Producto p : listaProds) {
				opciones[ i ] = p.getNombre();
				i++;
			}
			Object selProducto = JOptionPane.showInputDialog( ventana, "Selecciona artículo:", "Compra", JOptionPane.QUESTION_MESSAGE, null, opciones, null );
			if (selProducto==null) return null;  // No elegido producto
			Producto producto = null;
			for (Producto p : listaProds)
				if (p.getNombre().equals( selProducto )) { producto = p; break; }
			return producto;
		}
	
	// 12
	private void clickAnular() {
		if (listaProds==null || listaProds.isEmpty()) return;
		Producto producto = selProducto();
		if (producto==null) return;  // No elegido producto
		ArrayList<Compra> listaCompras = BaseDatos.getCompras( producto );
		if (listaCompras!=null && listaCompras.size()>0) {
			producto.getListaCompras().clear();
			producto.getListaCompras().addAll( listaCompras );
			Compra compra = selCompra( producto );
			if (compra!=null) {
				BaseDatos.borrarCompra( compra );
				taDatos.append( "Borrada compra: " + compra + "\n" );
				taDatos.setSelectionStart( taDatos.getText().length() );  // Mueve el textarea al final del scroll vertical si se llena la pantalla
			}
		} else {
			taDatos.append( "No hay compras de este producto\n" );
			taDatos.setSelectionStart( taDatos.getText().length() );  // Mueve el textarea al final del scroll vertical si se llena la pantalla
		}
	}
		private static SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yy HH:mm:ss" );
		private Compra selCompra( Producto producto ) {
			String[] opciones = new String[ producto.getListaCompras().size() ];
			int i = 0;
			for (Compra c : producto.getListaCompras()) {
				opciones[ i ] = sdf.format( new Date( c.getFecha() ) ) + " (" + c.getCantidad() + " unidades)";
				i++;
			}
			Object selCompra = JOptionPane.showInputDialog( ventana, "Selecciona compra:", "Selección compra de " + producto.getNombre(), JOptionPane.QUESTION_MESSAGE, null, opciones, null );
			if (selCompra==null) return null;  // No elegida compra
			int seleccionado = 0;
			for (String texto : opciones) {
				if (texto.equals( selCompra )) { 
					break; 
				}
				seleccionado++; 
			}
			return producto.getListaCompras().get( seleccionado );
		}
	
}
