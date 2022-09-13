package es.deusto.prog3.cap00;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

/** Ejemplo de error por pintado tardío  (basado en uno de vuestros problemas resolviendo la práctica 0 del coche) 
 * @author andoni.eguiluz @ ingenieria.deusto.es  
 */
public class ErrorPintadoTardio {

	public static void main(String[] args) {
		// Creamos una ventana de ejemplo solo para meter un coche
		JFrame f = new JFrame( "Ejemplo pintado tardío" );
		f.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		f.getContentPane().setLayout( null ); // Layout nulo para posicionado manual de componentes
		f.setSize( 500, 400 );
		f.setLocation( 2000, 0 );
		
		JLabel lCoche = new JLabel();
		// lCoche.setIcon( new ImageIcon( ErrorPintadoTardio.class.getResource("coche.png") ) );  // (a) Opción de coger una imagen como recurso
		lCoche.setIcon( new ImageIcon( "bin/es/deusto/prog3/cap00/coche.png" ) );  // (b) Opción de coger una imagen como fichero
		lCoche.setBounds( 100, 50, 300, 300 );
		f.getContentPane().add( lCoche );  // (2)
		
		f.setVisible( true );  // (1) Visualizamos (Quizás DEMASIADO PRONTO!)
		System.out.println( "Fin" );
		
		f.addWindowListener( new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				System.out.println( "OPENED" );
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				System.out.println( "DEACTIVATED" );
			}
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println( "CLOSING" );
				f.dispose();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println( "CLOSED" );
			}
			@Override
			public void windowActivated(WindowEvent e) {
				System.out.println( "ACTIVATED" );
			}
		});
		  // LANZA SWING!!!!!
		
		// Problema: si se lanza lo normal es que el pintado de la ventana lo haga Swing (1)
		// ANTES de que se cargue y se dibuje el coche en el panel (2)
		// Ojo: esto no es seguro porque el pintado de la ventana de Swing es asíncrono:
		// sabemos que el hilo de Swing se va a lanzar justo cuando hacemos (1),
		// pero no sabemos si llegará a pintar o no la ventana antes de que se meta en ella el coche (2)
		// Al haber una operación que tarda unos pocos milisegundos (cargar un fichero .png), lo habitual
		// es que le de tiempo a Swing a pintar antes cuando todavía la ventana no tiene el coche
		// y entonces la ventana que vemos NO TIENE COCHE.
		
		// Solución 1:
		// f.getContentPane().repaint();
		// El repaint() redibuja el componente que se indica - en este caso el panel completo
		
		// A veces cuando el cambio de estructura es importante hace falta revalidar el panel
		// para que se recalcule todo su layout - en este caso no hace falta:
		// f.getContentPane().revalidate();
		
		// Solución 2:
		// Hacer la visibilización (1) después de haber construido toda la ventana (2), y no antes
		// Esto asegura que lo que pinta Swing ya es la ventana completa.
		// En cualquier caso si vamos a cambiar luego "en caliente" el panel por ejemplo añadiendo
		// más labels, hará falta un repaint() o un revalidate().
		// (no hace falta repaint() si solo se cambian posiciones o tamaños con setLocation o equivalente)
		
	}

}
