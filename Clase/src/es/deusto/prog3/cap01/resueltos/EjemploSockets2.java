package es.deusto.prog3.cap01.resueltos;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.net.ServerSocket;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** Modificacion de EjemploSockets para varios clientes.
 * Obsérvense los comentarios *VARIOS CLIENTES* 
 * Ejemplo de utilización de sockets para comunicar un programa "servidor"
 * con VARIOS "clientes" en el mismo equipo. El cliente puede enviar textos
 * al servidor, que envía un mensaje de confirmación con cada texto.
 * Si se manda un mensaje "hola" se rebota a todos los clientes
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploSockets2 {

	private static String HOST = "localhost";  // IP de conexión para la comunicación
	private static int PUERTO = 4000;          // Puerto de conexión
	
	private static VentanaServidor vs;
	public static void main(String[] args) {
		vs = new VentanaServidor();
		vs.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				vs.lanzaServidor();
			}
		}).start();
		VentanaCliente vc = new VentanaCliente( "A" );
		vc.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				vc.lanzaCliente();
			}
		}).start();
		// *VARIOS CLIENTES* Lanzamos más clientes tras pausita
		try {Thread.sleep(2000); } catch (InterruptedException e) {} 
		VentanaCliente vc2 = new VentanaCliente( "B" );
		vc2.setLocation( vc2.getLocation().x, vc2.getLocation().y + 200 );  // Un poco más abajo
		vc2.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				vc2.lanzaCliente();
			}
		}).start();
		try {Thread.sleep(2000); } catch (InterruptedException e) {} 
		VentanaCliente vc3 = new VentanaCliente( "C" );
		vc3.setLocation( vc3.getLocation().x, vc3.getLocation().y + 400 );  // Más abajo aún
		vc3.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				vc3.lanzaCliente();
			}
		}).start();
	}

	@SuppressWarnings("serial")
	public static class VentanaCliente extends JFrame {
		private JTextArea taEstado = new JTextArea();
		private JTextField tfMensaje = new JTextField( "Introduce tu mensaje y pulsa <Enter>" );
		private PrintWriter outputAServer;
        private boolean finComunicacion = false;
        private String nombre;
		public VentanaCliente( String nombre ) {
			this.nombre = nombre;
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 400, 300 );
			setLocation( 0, 0 );
			setTitle( "Ventana cliente " + nombre + " - 'fin' acaba y 'hola' saluda" );
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
					if (tfMensaje.getText().equals("fin")) {
						finComunicacion = true;
					}
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
	            	String feedback = inputDesdeServer.readLine();  // Ojo-bloqueante. Devuelve mensaje de servidor o null cuando se cierra la comunicación
	            	if (feedback!=null) {
	            		taEstado.append( feedback + "\n" );
	            	} else {  // Comunicación cortada por el servidor o por error en comunicación
	            		finComunicacion = true;
	            	}
	            } while (!finComunicacion);
	        } catch (IOException e) {
            	taEstado.append( "Error en cliente: " + e.getMessage() + "\n" );
	        }
	        taEstado.append( "Fin de proceso de cliente.\n" );
	        System.out.println( "Cerrando ventana cliente " + nombre + " en 2 segundos..." );
	        if (finComunicacion) {
	        	try { Thread.sleep( 2000 ); } catch (InterruptedException e) {}
	        	dispose();
	        }
	    }
	}
	    
	@SuppressWarnings("serial")
	public static class VentanaServidor extends JFrame {
		JLabel lEstado = new JLabel( " " );
		JTextArea taMensajes = new JTextArea( 10, 1 );
        boolean finComunicacion = false;
		// *VARIOS CLIENTES*
		// Como el servidor va a gestionar varios clientes hacemos una lista de sockets en lugar de solo uno, y una lista de salidas para mensajes de difusión
        ArrayList<Socket> lSockets = new ArrayList<>(); 
        ArrayList<PrintWriter> lSalidas = new ArrayList<>();
        int numCliente = 0;  // Añadimos un número de cliente para saber cuántos se conectan
		public VentanaServidor() {
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 400, 300 );
			setLocation( 400, 0 );
			setTitle( "Ventana servidor" );
			taMensajes.setEditable( false );
			getContentPane().add( new JScrollPane(taMensajes), BorderLayout.CENTER );
			getContentPane().add( lEstado, BorderLayout.SOUTH );
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						// *VARIOS CLIENTES*
						// Se cierran todos los sockets abiertos 
						for (Socket socket : lSockets) socket.close();
					} catch (IOException e1) {
			    		lEstado.setText("Error en servidor: " + e1.getMessage());
					}
					finComunicacion = true;
				}
			});
		}
		public void lanzaServidor() {
			// *VARIOS CLIENTES*
			// Como el servidor va a gestionar varios clientes, en lugar de abrir solo una conexión, abre repetidamente conexiones hasta final
			try(ServerSocket serverSocket = new ServerSocket( PUERTO )) {
				serverSocket.setSoTimeout( 5000 );  // Para que haya un timeout en el accept - por si cerramos la aplicación para que no se quede esperando de forma infinita
				while (!finComunicacion) {
					try {
						Socket socket = serverSocket.accept(); // Se queda esperando a una conexión con timeout
						// *VARIOS CLIENTES*
						// Cada vez que un cliente se conecta, se genera un HILO que hace la comunicación (la lectura) con ese cliente. Y el servidor sigue ejecutando para esperar a otro cliente
						lSockets.add( socket );
						numCliente++;
						Thread t = new Thread ( new Runnable() { @Override public void run() {
							int numC = numCliente;
							try {
								lEstado.setText( "Cliente " + numC + " conectado" );
								BufferedReader inputDesdeCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								PrintWriter outputACliente = new PrintWriter(socket.getOutputStream(), true);
								lSalidas.add( outputACliente );  // Para mensajes de difusión
								while(!finComunicacion) {  // ciclo de lectura desde el cliente hasta que acabe la comunicación
									String textoRecibido = inputDesdeCliente.readLine();  // Ojo: bloqueante (este hilo se queda esperando)
									if(textoRecibido.equals("fin")) {
										break;
									}
									lEstado.setText( "Recibido de cliente " + numC + ": [" + textoRecibido + "]" );
									taMensajes.append( "[" + numC + "] " + textoRecibido + "\n" );
									taMensajes.setSelectionStart( taMensajes.getText().length() );
									if (textoRecibido.equals("hola")) {
										for (PrintWriter outputCl : lSalidas) {
											if (outputACliente != outputCl) {
												outputCl.println( "El cliente " + numC + " saluda a todos." );
											}
										}
									} else if (textoRecibido.equals("hora")) {
										outputACliente.println( "Hora: " + (new Date()) );
									} else {
										outputACliente.println("Recibido: [" + textoRecibido + "]" );
									}
								}
								lEstado.setText( "El cliente " + numC + " se ha desconectado." );
								socket.close();
								lSockets.remove( socket );
								lSalidas.remove( outputACliente );
							} catch(IOException e) {
								if (finComunicacion) {
									System.out.println( "Cerrada comunicación con cliente " + numC + " por cierre de servidor." );
								} else {
									e.printStackTrace();
								}
							}
						} } );
						t.start();
					} catch (SocketTimeoutException e) {
						// Timeout en socket servidor - se reintenta (en el mismo while)
					}
				}
			} catch(IOException e) {
				lEstado.setText("Error en servidor: " + e.getMessage());
			}

		}
	}

}
