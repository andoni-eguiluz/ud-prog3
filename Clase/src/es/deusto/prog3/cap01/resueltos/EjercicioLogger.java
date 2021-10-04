package es.deusto.prog3.cap01.resueltos;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.logging.*;

import javax.swing.*;

/** Ejercicio de logger: sacar a fichero XML el registro de lo que hace el programa, al menos estas cosas:
 * - Cuando se lanza la ventana (nivel FINEST)
 * - Cuando se pulsa el botón (nivel FINE)
 * - Cuando se edita el textfield (nivel FINE)
 * - Cada carpeta que se visualiza el número de ficheros (nivel INFO)
 * - Observa que hay un posible error si la carpeta no existe. Atrapa esta excepción y añádela al log indicando un mensaje al usuario (nivel SEVERE)
 * - Prueba cómo se comportan los mensajes si cambias el nivel de registro del log
s * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjercicioLogger {
	
	// Resuelto
	private static Logger logger;
	
	private static JTextField tfEntrada = new JTextField( 60 );
	private static JLabel lMensaje = new JLabel( " " );
	public static void main(String[] args) {
		// Activación de logger para fichero xml
		try {
			logger = Logger.getLogger( "es.deusto.prog3.cap01.resueltos.EjercicioLogger" );  // Devuelve o crea el logger con ese nombre
			Handler h = new FileHandler( "prueba-logger.xml", true );
			logger.addHandler( h ); // Y también a un xml
			logger.setLevel( Level.FINEST );  // Activa que nuestro logger saque finest o superior
			h.setLevel( Level.FINEST );  // Activa que el handler de fichero saque finest o superior
			// Truco particular para que el handler por defecto (consola de error) saque logs menores que INFO:
			logger.getParent().getHandlers()[0].setLevel( Level.FINEST );  // Activa que el handler por defecto (consola de error) también saque finest o superior
		} catch (Exception e) {}
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
				logger.log( Level.FINE, "Editado textfield" );
				sacaFicheros();
			}
		});
		bBuscar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.log( Level.FINE, "Pulsado botón Buscar" );
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
		logger.log( Level.FINEST, "Ventana iniciada" );
	}
	
	// Saca el número de ficheros de la carpeta indicada
	private static  void sacaFicheros() {
		try {
			File f = new File( tfEntrada.getText() );
			File[] listDir = f.listFiles();
			
			lMensaje.setText( "Ficheros+directorios en la carpeta: " + listDir.length );
			logger.log( Level.INFO, "Carpeta buscada:" + f.getAbsolutePath() );
		} catch (Exception e) {
			JOptionPane.showMessageDialog( null, "Carpeta errónea. Introduce otra" );
			e.printStackTrace();
			logger.log( Level.SEVERE, "Error en sacaFicheros", e );
		}
	}

}
