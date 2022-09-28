package es.deusto.prog3.cap00.resueltos;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

/** Clase de contador para ejercicio de concurrencia con hilos 0.10 - resuelto
 * @author andoni.eguiluz @ ingenieria.deusto.es
 * 
 * Permite instanciar contadores que incrementan o decrementan su valor
 */
public class Ej10Contador {
	// Parte static - main de prueba (ejercicio de hilo)
	
	private static long MS_PAUSA = 1; // Milisegundos de pausa en el incremento / decremento
	private static int NUM_REPETICIONES = 10000;

	// Barras de progreso
	private static JProgressBar pbProgresoHilo1;
	private static JProgressBar pbProgresoHilo2;
	
	public static void main(String[] args) {
		// Creamos un contador
		Ej10Contador cont = new Ej10Contador();
		System.out.println( "Valor inicial de contador: " + cont );
		// Incrementamos 1.000 veces (en un hilo)
		Thread hilo1 = new Thread() {
			public void run() {
				for (int i=0; i<NUM_REPETICIONES; i++) {
					pbProgresoHilo1.setValue( i );  // Para ver el progreso en la ventana
					cont.inc(1);
					if (MS_PAUSA>0) try { Thread.sleep(MS_PAUSA); } catch (InterruptedException e) { 
						break;  // Cierre prematuro
					} // Pausa entre iteraciones si procede
					if (interrupted()) break;  // Cierre prematuro
				}
			}
		};
		// Decrementamos 1.000 veces (en otro hilo)
		Thread hilo2 = new Thread() {
			public void run() {
				for (int i=0; i<NUM_REPETICIONES; i++) {
					pbProgresoHilo2.setValue( i );  // Para ver el progreso en la ventana
					cont.dec(1);
					if (MS_PAUSA>0) try { Thread.sleep(MS_PAUSA); } catch (InterruptedException e) { 
						break;  // Cierre prematuro
					} // Pausa entre iteraciones si procede
					if (interrupted()) break;  // Cierre prematuro
				}
			}
		};
		// Ventana de cierre 
		JFrame vc = new JFrame( "Ventana de cierre - ciérrala para acabar prematuramente los hilos" );
		vc.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		vc.setSize( 600, 100 );
		pbProgresoHilo1 = new JProgressBar( 0, NUM_REPETICIONES-1 );
		pbProgresoHilo1.setStringPainted( true ); // Saca el %
		pbProgresoHilo2 = new JProgressBar( 0, NUM_REPETICIONES-1 );
		pbProgresoHilo2.setStringPainted( true ); // Saca el %
		vc.add( pbProgresoHilo1, BorderLayout.NORTH );
		vc.add( pbProgresoHilo2, BorderLayout.SOUTH );
		vc.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				hilo1.interrupt();
				hilo2.interrupt();
			}
		});
		vc.setVisible( true );
		// Lanzamos los dos hilos
		hilo1.start();
		hilo2.start();
		// Esperamos a que acaben
		try {
			hilo1.join();
			hilo2.join();
		} catch (InterruptedException e) {
		}
		System.out.println( "Valor final de contador: " + cont );
	}
	
	// Clase para instancias - parte no static
	
	/** Crea un contador con valor inicializado a 0
	 */
	public Ej10Contador() {
		valor = 0;
	}
	
	// TODO Quita el synchronized de los métodos inc y dec y observa la diferencia de comportamiento
	
	private int valor;
	/** Incrementa el valor del contador
	 * @param incremento	Cantidad a incrementar (debe ser positiva)
	 */
	public synchronized void inc( int incremento ) {
		int temporal = valor;
		temporal = temporal + incremento;
		valor = temporal;
	}
	/** Decrementa el valor del contador
	 * @param decremento	Cantidad a decrementar (debe ser positiva, se restará del valor)
	 */
	public synchronized void dec( int decremento ) {
		int temporal = valor;
		temporal = temporal - decremento;
		valor = temporal;
	}
	
	@Override
	public String toString() {
		return "" + valor;
	}
}
