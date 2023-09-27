package es.deusto.prog3.cap00.resueltos;

import java.util.*;

import javax.swing.*;

/** Programa un par de hilos según la especificación y comprueba que hay problemas.
 * Resuelve esos problemas con una estructura synchronized
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ErrorAccesoConcurrente {

	private static long CONPAUSA = 1; // msgs de pausa en los hilos
	
	private static JTextArea taSalida = new JTextArea();
	// TODO
	// Probar con esta estructura y ver que hay problemas:
	// private static ArrayList<Long> listaNums = new ArrayList<>();
	// TODO Sustituirla por una estructura synchronized
	private static Vector<Long> listaNums = new Vector<>();
	
	public static void main(String[] args) {
		// Ventana de salida
		JFrame f = new JFrame();
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); // EXIT - Sale del programa al cerrar la ventana (acabando los hilos)  (ojo que esto no es algo que se pueda hacer siempre)
		f.setSize( 1000, 800 );
		taSalida.setEditable( false );
		f.add( new JScrollPane( taSalida ) );
		f.setVisible( true );
		println( "Test" ); // Mensaje de prueba
		// Empezamos con la lista [0]
		listaNums.add( 0L ); 
		
		// TODO Programa un hilo que solo va añadiendo números incrementales a la lista por el final
		// Haz que el hilo visualice en la ventana lo que va haciendo y espere un poquito en cada iteración:
		// println( "Añadido: " + listaNums.toString() );
		// if (CONPAUSA>0) try { Thread.sleep(CONPAUSA); } catch (InterruptedException ex) {}
		Thread hilo1 = new Thread() {
			public void run() {
				long numero = 0;
				while (true) {
					numero++;
					listaNums.add( numero );
					println( "Añadido: " + listaNums.toString() );
					if (CONPAUSA>0) try { Thread.sleep(CONPAUSA); } catch (InterruptedException ex) {}
				}
			}
		};
		hilo1.start();
		
		// TODO Programa otro hilo que solo va quitando números por el principio
		// Haz que el hilo visualice en la ventana lo que va haciendo y espere un poquito en cada iteración:
		// println( "Borrado: " + listaNums.toString() );
		// if (CONPAUSA>0) try { Thread.sleep(CONPAUSA); } catch (InterruptedException ex) {}
		Thread hilo2 = new Thread() {
			public void run() {
				while (true) {
					if (!listaNums.isEmpty()) {
						long primero = listaNums.remove(0);
						println( "Eliminado: " + primero );
					}
					if (CONPAUSA>0) try { Thread.sleep(CONPAUSA); } catch (InterruptedException ex) {}
				}
			}
		};
		hilo2.start();
		
		// A partir de ahora se tiene que ir viendo en pantalla una lista donde se añaden números por el final 
		// y se quitan por el principio... salvo que haya algún problema de concurrencia y uno de los dos 
		// hilos deje de hacer bien su trabajo
	}

	// Método auxiliar para sacar información en la ventana
	// Lo hacemos syncrhonized para que no haya interferencia entre los hilos a la hora de visualizar
	// probar que si se quita el synchronized hay problemas - no los hay si se hace respetando el thread-unsafe de Swing con invokeLater)
	private static synchronized void println( String mens ) {
		taSalida.append( mens + "\n" );
		taSalida.setSelectionStart( taSalida.getText().length() );  // Para ir viendo el texto último añadido (scroll al último punto del texto)
		if (taSalida.getText().length()>100000) {  // Para que no se llene la textarea vamos quitando de vez en cuando
			taSalida.replaceRange( "", 0, 50000 );
		}
		// Aunque si se hace synchronized no va a haber problema, sería más correcto hacer esto para respetar a Swing (que no es Thread-safe):
		// try {
		//  	SwingUtilities.invokeAndWait( new Runnable() {  // Se podría hacer invokeLater pero con pausa 0 Swing no puede seguir a los hilos que generan texto a toda velocidad
		// 		@Override
		// 		public void run() {
		// 			taSalida.append( mens + "\n" );
		// 			taSalida.setSelectionStart( taSalida.getText().length() );
		// 			taSalida.setSelectionEnd( taSalida.getText().length() );
		// 			if (taSalida.getText().length()>100000) {  // Para que no se llene la textarea vamos quitando de vez en cuando
		// 				taSalida.replaceRange( "", 0, 50000 );
		// 			}
		// 		}
		// 	});
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
	}

}
