package es.deusto.prog3.utils.comunicacion;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** Ejemplo de utilización de sockets para comunicar un programa "servidor"
 * con un "cliente" en el mismo equipo. El cliente puede enviar textos
 * al servidor, que envía un mensaje de confirmación con cada texto.
 * 
 * Ejemplo del tema 1 de la asignatura
 * 
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploSockets {

	private static String HOST = "localhost";  // IP de conexión para la comunicación
	private static int PUERTO = 4000;          // Puerto de conexión
	
	private static VentanaServidor vs;
	private static VentanaCliente vc;
	public static void main(String[] args) {
		vs = new VentanaServidor();
		vs.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				vs.lanzaServidor();
			}
		}).start();
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
		private JTextArea taEstado = new JTextArea();
		JTextField tfMensaje = new JTextField( "Introduce tu mensaje y pulsa <Enter>" );
        PrintWriter outputAServer;
        boolean finComunicacion = false;
		public VentanaCliente() {
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 400, 300 );
			setLocation( 0, 0 );
			setTitle( "Ventana cliente - 'fin' acaba" );
			taEstado.setEditable( false );
			getContentPane().add( tfMensaje, BorderLayout.NORTH );
			getContentPane().add( taEstado, BorderLayout.CENTER );
			tfMensaje.addFocusListener( new FocusAdapter() { // Selecciona todo el texto del cuadro en cuanto se le da el foco del teclado
				@Override
				public void focusGained(FocusEvent e) {
					tfMensaje.selectAll();
				}
			});
			tfMensaje.addActionListener( new ActionListener() { // Evento de <enter> de textfield
				@Override
				public void actionPerformed(ActionEvent e) {
					outputAServer.println( tfMensaje.getText() );
					if (tfMensaje.getText().equals("fin"))
						finComunicacion = true;
					tfMensaje.setText( "" );
				}
			});
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					outputAServer.println( "fin" );
					finComunicacion = true;
				}
			});
		}
	    public void lanzaCliente() {
	        try (Socket socket = new Socket( HOST, PUERTO )) {
	            BufferedReader inputDesdeServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            outputAServer = new PrintWriter(socket.getOutputStream(), true);
	            do { // Ciclo de lectura desde el servidor hasta que acabe la comunicación
	            	String feedback = inputDesdeServer.readLine();  // Devuelve mensaje de servidor o null cuando se cierra la comunicación (BLOQUEANTE - Se queda esperando)
	            	if (feedback!=null) {
	            		taEstado.append( feedback + "\n" );
	            	} else {  // Comunicación cortada por el servidor o por error en comunicación
	            		finComunicacion = true;
	            	}
	            } while(!finComunicacion);
	        } catch (IOException e) {
	        	taEstado.append( "Error en cliente: " + e.getMessage() + "\n" );
	        }
	        taEstado.append( "Fin de proceso de cliente.\n" );
	    }
	}
	    
	@SuppressWarnings("serial")
	public static class VentanaServidor extends JFrame {
		JLabel lEstado = new JLabel( " " );
		JTextArea taMensajes = new JTextArea( 10, 1 );
        boolean finComunicacion = false;
        Socket socket;
		public VentanaServidor() {
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 400, 300 );
			setLocation( 400, 0 );
			setTitle( "Ventana servidor" );
			getContentPane().add( new JScrollPane(taMensajes), BorderLayout.CENTER );
			getContentPane().add( lEstado, BorderLayout.SOUTH );
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						socket.close();
					} catch (IOException e1) {
			    		lEstado.setText("Error en servidor: " + e1.getMessage());
					}
					finComunicacion = true;
				}
			});
		}
	    public void lanzaServidor() {
	    	try(ServerSocket serverSocket = new ServerSocket( PUERTO )) {
	    		socket = serverSocket.accept();  // Bloqueante
	    		lEstado.setText( "Cliente conectado" );
	    		BufferedReader inputDesdeCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    		PrintWriter outputACliente = new PrintWriter(socket.getOutputStream(), true);
	    		while(!finComunicacion) {  // ciclo de lectura desde el cliente hasta que acabe la comunicación
	    			String textoRecibido = inputDesdeCliente.readLine();  // Ojo: bloqueante (este hilo se queda esperando)
	    			if(textoRecibido.equals("fin")) {
	    				break;
	    			}
	    			lEstado.setText( "Recibido de cliente: [" + textoRecibido + "]" );
	    			taMensajes.append( textoRecibido + "\n" );
	    			taMensajes.setSelectionStart( taMensajes.getText().length() );
	    			outputACliente.println("Recibido: [" + textoRecibido + "]" );
	    		}
	    		lEstado.setText( "El cliente se ha desconectado." );
	    		socket.close();
	    	} catch(IOException e) {
	    		lEstado.setText("Error en servidor: " + e.getMessage());
	    	}
	    }
	}

}