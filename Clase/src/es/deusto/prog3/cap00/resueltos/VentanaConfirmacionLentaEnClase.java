package es.deusto.prog3.cap00.resueltos;

import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

// Ejercicio 0.2 solución realizada en clase

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
public class VentanaConfirmacionLentaEnClase {

		private static Random r = new Random();
	// Este método simula un proceso que tarda un tiempo en hacerse (entre 5 y 10 segundos)
	private static void procesoConfirmar() {
		try {
			Thread.sleep( 5000 + 1000*r.nextInt(6) );
		} catch (InterruptedException e) {}
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setTitle( "Ej 0.2");
		f.setSize( 300, 200 );
		f.setLayout( null );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JButton b = new JButton( "confirmar" );
		b.setSize( 800, 600 );
		f.add( b );
		f.setVisible( true );
		b.addActionListener( new ActionListener() {
			// NUEVA b (*)
			@Override
			public void actionPerformed(ActionEvent evento) {
				// marca C1
				b.setEnabled( false );
				(new Thread() {
					// NUEVA b (*)
					@Override
					public void run() {
						// marca C2
						System.out.println( "Antes de confirmar");
						procesoConfirmar();
						// marca C2bis
						b.setEnabled( true );
						System.out.println( "Después de confirmar");
					}
				}).start();
				// marca C3
			}
		});
	}

	// ¿En qué orden se ejecutan las marcas?  C1 -> C3, C1->C2, y C2 -> C2bis. Pero no podemos asegurar nada de C2 o C2b con respecto a C3!
	// Ámbitos ajenos a la variable b - se crean nuevas variables "copia"  (*)
}
