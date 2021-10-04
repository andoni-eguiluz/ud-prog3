package es.deusto.prog3.cap01;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

/** Ejemplo de introducción a lambda con Java 8
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploLambda {
	
	public static void test() { System.out.println( "Soy test de clase" ); }

	@SuppressWarnings("unused")
	private void test2() { System.out.println( "Soy test de instancia" ); }

	public static void main(String[] args) {
		SwingUtilities.invokeLater(
				
			null // Aquí hay que dar algo que es... un Runnable
			// (¿Por qué es un Runnable?  Porque queremos pasar un código)
			// (Pero pasar un código no se puede... ufff... habrá que hacer un truco... o dos)
			
			// ¿Cómo dar un runnable a invokeLater?
			
			// Java 7 - clase interna anónima
//			new Runnable() {
//				@Override
//				public void run() {
//					System.out.println( "Bla bla bla" );
//				}
//			}
				
			// O bien si se resume el código en un método
//				new Runnable() {
//					@Override
//					public void run() {
//						test();
//					}
//				}
					
				
			// Java 8
			// () -> { test(); }
				
			// O tb método estático
			// EjemploLambda1::test
				
			// O tb método instancia sobre un objeto
			// (new EjemploLambda())::test2
			
			// Tiene que emparejar con Runnable porque es lo que espera invokeLater:
			//    void run () { }
			// O sea
			//    () sin parámetros -> { } código -> sin retorno (void)
			//
			// Cualquier interfaz con UN SOLO método vale para hacerlo así.  (definido retorno, definidos pars)
			
		);
		
		JButton b = new JButton("");
		b.addActionListener( 
//				new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						System.out.println( e.getWhen() );
//					}
//				}
			(e) -> { System.out.println( e.getWhen() ); }
			// También (java.awt.event.ActionEvent e) -> { System.out.println( e.getWhen() ); }
		);
	}

}
