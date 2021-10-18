package es.deusto.prog3.utils.comunicacion;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** Clase de ejemplo de comunicación de hilos... para entender las bases de cómo comunicaríamos programas
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ComunicacionDeHilos {
	public static void main(String[] args) {
		// TODO Descomentar la que se quiera probar
		// conVariable();
		// conVariableBidireccional();
		conEnvioYRecepcionAsincrona();
	}

	// Ejemplo de comunicación de 2 hilos con una variable compartida
	private static Random random = new Random();
	private static Vector<Integer> listaNums;
	private static void conVariable() {
		listaNums = new Vector<>();
		// Hilo 1: produce nums
		Thread hilo1 = new Thread() {
			@Override
			public void run() {
				while (true) {
					try { Thread.sleep(100); } catch (InterruptedException e) {}  // Pausita
					if (random.nextDouble()<0.33) {  // Produce un número de 0 a 100 con una posibilidad del 33%
						listaNums.add( random.nextInt(101));
						System.out.println( "Hilo 1 produce: " + listaNums );
					}
				}
			}
		};
		// Hilo 2: recibe y consume nums
		Thread hilo2 = new Thread() {
			@Override
			public void run() {
				while (true) {
					try { Thread.sleep(500); } catch (InterruptedException e) {}  // Pausita
					while (!listaNums.isEmpty()) {
						System.out.println( "Hilo 2 consume: " + listaNums.remove(0) );
					}
				}
			}
		};
		hilo1.start();
		hilo2.start();
	}
	
	// Ejemplo de comunicación bidireccional de 2 hilos con dos variables compartidas
	// Recepción y envío síncrono... no es muy lógico en el mundo real
	private static Vector<String> mens1A2;
	private static Vector<String> mens2A1;
	private static void conVariableBidireccional() {
		mens1A2 = new Vector<>();
		mens2A1 = new Vector<>();
		// Hilo 1: produce mensajes y responde a los mensajes de hilo 2 
		Thread hilo1 = new Thread() {
			@Override
			public void run() {
				while (true) {
					try { Thread.sleep(100); } catch (InterruptedException e) {}  // Pausita
					// 1.- Enviamos
					if (random.nextDouble()<0.33) {  // Produce un saludo 0-100 con una posibilidad del 33%
						mens1A2.add( "Hola " + random.nextInt(101) );
						System.out.println( "Hilo 1 saluda: " + mens1A2 );
					}
					// 2.- Recibimos
					while (!mens2A1.isEmpty()) {
						System.out.println( "Hilo 1 responde a hilo 2: RECIBIDO " + mens2A1.remove(0) );
					}
				}
			}
		};
		// Hilo 2: produce mensajes y responde a los mensajes de hilo 1  (obsérvese que podrían programarse con el mismo código cambiando su identificación)
		Thread hilo2 = new Thread() {
			@Override
			public void run() {
				while (true) {
					try { Thread.sleep(100); } catch (InterruptedException e) {}  // Pausita
					// 1.- Enviamos
					if (random.nextDouble()<0.33) {  // Produce un saludo 0-100 con una posibilidad del 33%
						mens2A1.add( "Hola " + random.nextInt(101) );
						System.out.println( "Hilo 2 saluda: " + mens2A1 );
					}
					// 2.- Recibimos
					while (!mens1A2.isEmpty()) {
						System.out.println( "Hilo 2 responde a hilo 1: RECIBIDO " + mens1A2.remove(0) );
					}
				}
			}
		};
		hilo1.start();
		hilo2.start();
	}

	// Ejemplo de comunicación bidireccional de 2 hilos con dos variables compartidas
	// Con recepción y envío asíncronos (envío con interacción en ventana)
	private static JTextField tfHilo1;
	private static JTextArea taHilo1;
	private static JTextField tfHilo2;
	private static JTextArea taHilo2;
	private static void conEnvioYRecepcionAsincrona() {
		mens1A2 = new Vector<>();
		mens2A1 = new Vector<>();
		// Hilo 1: produce mensajes interactivos y responde a los mensajes de hilo 2 
		Thread hilo1 = new Thread() {
			@Override
			public void run() {
				// 1.- Generamos envío interactivo (en hilo de Swing)
				JFrame vent = new JFrame( "Ventana hilo 1" );
				vent.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				vent.setSize( 800, 400 );
				vent.setLocation( 0, 0 );
				tfHilo1 = new JTextField( 30 );
				taHilo1 = new JTextArea();
				JPanel panel1 = new JPanel();
				panel1.add( new JLabel( "Introduce mensaje y pulsa <Intro>" ) );
				panel1.add( tfHilo1 );
				vent.add( panel1, BorderLayout.NORTH );
				vent.add( new JScrollPane( taHilo1 ) , BorderLayout.CENTER );
				tfHilo1.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Este es el envío
						mens1A2.add( tfHilo1.getText() );
					}
				});
				vent.setVisible( true );
				while (true) {
					try { Thread.sleep(100); } catch (InterruptedException e) {}  // Pausita
					// 2.- Recibimos - y este es ahora el único bucle forever
					if (!mens2A1.isEmpty()) {
						taHilo1.append( mens2A1.remove(0) + "\n" );
					}
				}
			}
		};
		// Hilo 2: produce mensajes interactivos y responde a los mensajes de hilo 1 
		// (obsérvese que podrían programarse con el mismo código cambiando su identificación)
		Thread hilo2 = new Thread() {
			@Override
			public void run() {
				// 1.- Generamos envío interactivo (en hilo de Swing)
				JFrame vent = new JFrame( "Ventana hilo 2" );
				vent.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
				vent.setSize( 800, 400 );
				vent.setLocation( 600, 400 );
				tfHilo2 = new JTextField( 30 );
				taHilo2 = new JTextArea();
				JPanel panel2 = new JPanel();
				panel2.add( new JLabel( "Introduce mensaje y pulsa <Intro>" ) );
				panel2.add( tfHilo2 );
				vent.add( panel2, BorderLayout.NORTH );
				vent.add( new JScrollPane( taHilo2 ) , BorderLayout.CENTER );
				tfHilo2.addActionListener( new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Este es el envío
						mens2A1.add( tfHilo2.getText() );
					}
				});
				vent.setVisible( true );
				while (true) {
					try { Thread.sleep(100); } catch (InterruptedException e) {}  // Pausita
					// 2.- Recibimos - y este es ahora el único bucle forever
					if (!mens1A2.isEmpty()) {
						taHilo2.append( mens1A2.remove(0) + "\n");
					}
				}
			}
		};
		hilo1.start();
		hilo2.start();
	}

}
