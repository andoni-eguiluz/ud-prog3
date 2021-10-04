package es.deusto.prog3.cap01.ejercicios;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

/** Ejercicio de logger: sacar a fichero XML el registro de lo que hace el programa, al menos estas cosas:
 * - Cuando se lanza la ventana (nivel FINEST)
 * - Cuando se pulsa el botón (nivel FINE)
 * - Cuando se edita el textfield (nivel FINE)
 * - Cada carpeta que se visualiza el número de ficheros (nivel INFO)
 * - Observa que hay un posible error si la carpeta no existe. Atrapa esta excepción y añádela al log indicando un mensaje al usuario (nivel SEVERE)
 * - Prueba cómo se comportan los mensajes si cambias el nivel de registro del log
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjercicioLogger {
	
	// TODO Añadir logger y registrar lo indicado en la cabecera (y más si se quiere)
	
	private static JTextField tfEntrada = new JTextField( 60 );
	private static JLabel lMensaje = new JLabel( " " );
	public static void main(String[] args) {
		// Ventana de ejemplo para el ejercicio
		final JFrame f = new JFrame( "Ventana rápida para ejercicio logger" );  // Ventana a visualizar
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize( 800, 150 );
		f.setLocationRelativeTo( null );
		JPanel pSuperior = new JPanel();
		JPanel pInferior = new JPanel();
		JButton bBuscar = new JButton( "Buscar" );
		pSuperior.add( new JLabel("Indica carpeta") ); 
		pSuperior.add( tfEntrada );
		pSuperior.add( bBuscar );
		pInferior.add( lMensaje );
		f.add( pSuperior, BorderLayout.CENTER );
		f.add( pInferior, BorderLayout.SOUTH );
		tfEntrada.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sacaFicheros();
			}
		});
		bBuscar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Saca un diálogo de búsqueda de fichero con JFileChooser
				JFileChooser f = new JFileChooser();
				f.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				int cod = f.showOpenDialog( f );
				if (cod==JFileChooser.APPROVE_OPTION) {
					File dir = f.getSelectedFile();
					tfEntrada.setText( dir.getAbsolutePath() );
					sacaFicheros();
				}
			}
		});
		f.setVisible( true );
	}
	
	// Saca el número de ficheros de la carpeta indicada
	private static  void sacaFicheros() {
		File f = new File( tfEntrada.getText() );
		File[] listDir = f.listFiles();
		lMensaje.setText( "Ficheros+directorios en la carpeta: " + listDir.length );
	}

}
