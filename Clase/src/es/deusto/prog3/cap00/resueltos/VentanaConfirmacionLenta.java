package es.deusto.prog3.cap00.resueltos;

import java.util.Random;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** Ejercicio de hilos con ventanas. Programa esta clase para que se cree una ventana
 * que pida un dato de texto al usuario y un botón de confirmar para que se confirme.
 * Haz que al pulsar el botón de confirmación se llame al procesoConfirmar()
 * que simula un proceso de almacenamiento externo que tarda unos segundos.
 * Observa que hasta que ocurre esta confirmación la ventana no responde.
 * 1. Arréglalo para que la ventana no se quede "frita" hasta que se acabe de confirmar.
 * 2. Haz que el botón de "confirmar" no se pueda pulsar dos veces mientras el proceso
 * de confirmación se esté realizando
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class VentanaConfirmacionLenta {

		private static Random r = new Random();
	// Este método simula un proceso que tarda un tiempo en hacerse (entre 5 y 10 segundos)
	private static void procesoConfirmar() {
		try {
			Thread.sleep( 5000 + 1000*r.nextInt(6) );
		} catch (InterruptedException e) {}
	}
	
	public static void main(String[] args) {
		// Configuración de ventana
		JFrame ventana = new JFrame();
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		JTextField tfDato = new JTextField( "<Introduce aquí el nombre>", 50 );
		JPanel pSuperior = new JPanel();
		pSuperior.setBackground( Color.RED );
		pSuperior.add( tfDato );
		ventana.add( pSuperior, BorderLayout.NORTH );
		JButton bConfirmar = new JButton( "Confirmar" );
		ventana.add( bConfirmar, BorderLayout.SOUTH );
		ventana.setLocation( 2000, 100 );
		ventana.setSize( 600, 400 );
		// Eventos
		// bConfirmar.addActionListener( new MiAccion() );
		bConfirmar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println( "botón" );
				procesoConfirmar();
			}
		} );
		// Lanzamiento
		ventana.setVisible( true );
		System.out.println( "Fin" );
	}

}

class MiAccion implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// acciones
	}
}
