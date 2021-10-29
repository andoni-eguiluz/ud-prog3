package es.deusto.prog3.cap03.cs;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** Programa cliente (ejecutar tantos como se quieran después de ejecutar el servidor)
 * Ver EjemploServidor.java en este mismo paquete
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploCliente {

	private static ObjectOutputStream flujoOut;
	private static boolean finComunicacion = false;
	private static String miHost = ConfigCS.HOST;
	
	private static VentanaCliente vc;
	private static JLabel lEstado = new JLabel( " " );
	private static JTextField tfMensaje = new JTextField( "Introduce tu mensaje y pulsa <Enter>" );
	private static JTextArea taMensajes = new JTextArea( 10, 1 );
	
	public static void main(String[] args) {
		// Pedir host de conexión por si cambia
		miHost = JOptionPane.showInputDialog( "Introduce servidor", miHost );
		if (miHost==null || miHost.isEmpty()) return; // Acaba si no se mete
		vc = new VentanaCliente();
		vc.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				vc.lanzaCliente();
			}
		}).start();
	}

	@SuppressWarnings("serial")
	public static class VentanaCliente extends JFrame {
		public VentanaCliente() {
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 500, 300 );
			setTitle( "Ventana cliente - 'fin' acaba" );
			getContentPane().add( tfMensaje, BorderLayout.NORTH );
			getContentPane().add( new JScrollPane(taMensajes), BorderLayout.CENTER );
			getContentPane().add( lEstado, BorderLayout.SOUTH );
			tfMensaje.addFocusListener( new FocusAdapter() { // Selecciona todo el texto del cuadro en cuanto se le da el foco del teclado
				@Override
				public void focusGained(FocusEvent e) {
					tfMensaje.selectAll();
				}
			});
			tfMensaje.addActionListener( new ActionListener() { // Evento de <enter> de textfield
				@Override
				public void actionPerformed(ActionEvent e) {
					if (finComunicacion) {
						lEstado.setText( "Comunicación acabada. Reinicia: no se pueden enviar más mensajes" );
						return;
					}
					try {
						if (tfMensaje.getText().equals( ConfigCS.FIN ))
							finComunicacion = true;
						flujoOut.writeObject( tfMensaje.getText() );
						tfMensaje.setText( "" );
					} catch (IOException e1) {  // Error en writeObject
						e1.printStackTrace();
					}
				}
			});
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					finComunicacion = true;
				}
			});
		}
	    public void lanzaCliente() {
	    	// Hilo que abre el socket y se va a quedar escuchando los mensajes del servidor
	        try (Socket socket = new Socket( miHost, ConfigCS.PUERTO )) {
	    		socket.setSoTimeout( 1000 ); // Pone el timeout para que no se quede eternamente en la espera (1)
	            flujoOut = new ObjectOutputStream(socket.getOutputStream());
	            ObjectInputStream echoes = new ObjectInputStream(socket.getInputStream());
	            do { // Bucle de espera a mensajes del servidor
	            	try {
	            		Object feedback = echoes.readObject();  // Devuelve mensaje de servidor o null cuando se cierra la comunicación
		            	if (feedback==null) { // Comunicación cortada por el servidor o por error en comunicación
		            		finComunicacion = true;
		            	} else if (feedback.equals( ConfigCS.RECIBIDO )) { // Confirmación de recepción
		            		lEstado.setText( feedback.toString() );
		            	} else if (feedback.equals( ConfigCS.RECIBIDO_DE )) { // Envío de mensajes de otros clientes
			            	Object feedback2 = echoes.readObject();  // Se espera el nombre del cliente que envía
			            	Object feedback3 = echoes.readObject();  // Se espera el objeto que ese cliente envía
			    			taMensajes.append( "Mensaje de " + feedback2 + ": " + feedback3 + "\n" );
			    			taMensajes.setSelectionStart( taMensajes.getText().length() );  // Pone el cursor al final del textarea
		            	}
	    			} catch (SocketTimeoutException e) {} // Excepción de timeout - no es un problema
	            } while(!finComunicacion);
				flujoOut.writeObject( ConfigCS.FIN );
		        lEstado.setText( "Fin de proceso de cliente." );
	        } catch (Exception e) {
	        	e.printStackTrace();
            	lEstado.setText( "Perdida comunicación: " + e.getClass().getName() + " - " + e.getMessage());
            	finComunicacion = true;
	        }
	    }
	}

}
