package es.deusto.prog3.utils.comunicacion;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Vector;
import java.net.ServerSocket;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** Ejemplo de utilización de sockets para comunicar un programa "servidor"
 * con VARIOS "clientes", en el mismo equipo o en la misma red
 * (en la UD es probable que no funcione porque los puertos están limitados)
 * Cada cliente puede enviar textos o iconos al servidor
 * El servidor envía un mensaje de confirmación al recibir cada mensaje a todos los clientes conectados.
 * Además envía el mensaje al resto de los clientes
 * 
 * Ejecutar primero el servidor (este programa) y luego lanzar los clientes que se deseen
 * 
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploServidor {

	// Atributos de comunicación
	private static Vector<HiloComunicacion> listaHilos; // Lista de hilos de comunicación  
	private static Vector<Socket> listaSockets;         // Lista de sockets de comunicación (correspondientes a los hilos)
	private static Vector<ObjectOutputStream> listaOOS; // Lista de flujos de salida (correspondientes a los sockets)
    private static boolean finComunicacion = false;
    private static int numTotalClientes = 0;
	
	// Atributos visuales
	private static VentanaServidor vs;  // Ventana del servidor
	private static JLabel lEstado = new JLabel( " " );
	private static JTextArea taMensajes = new JTextArea( 10, 1 );
	
	public static void main(String[] args) {
		listaHilos = new Vector<>();
		listaSockets = new Vector<>();
		listaOOS = new Vector<>();
		vs = new VentanaServidor();
		vs.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				vs.lanzaServidor();
			}
		}).start();
	}

	@SuppressWarnings("serial")
	public static class VentanaServidor extends JFrame {
		public VentanaServidor() {
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			setSize( 500, 300 );
			setLocation( 400, 0 );
			setTitle( "Ventana servidor" );
			getContentPane().add( new JScrollPane(taMensajes), BorderLayout.CENTER );
			getContentPane().add( lEstado, BorderLayout.SOUTH );
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					finComunicacion = true;
				}
			});
		}
	    public void lanzaServidor() {
	    	try(ServerSocket serverSocket = new ServerSocket( ConfigCS.PUERTO )) {
	    		serverSocket.setSoTimeout( 5000 );  // Pone un timeout de 5 milisegundos
		    	while (!finComunicacion) {
		    		// Escucha una conexión por parte de cliente y se queda bloqueado hasta que no se recibe
		    		try {
		    			Socket socket = serverSocket.accept();   
			    		// Ya hay un cliente conectado. Lanza un hilo que gestiona a partir de ahora la conexión con ese cliente
			    		// Nombra a cada cliente con el número secuencial que le corresponde
			    		numTotalClientes++;
			    		HiloComunicacion hilo = new HiloComunicacion( socket, "" + numTotalClientes );
			    		hilo.start();
		    		} catch (SocketTimeoutException e) {}  // Si es un timeout no es un error, simplemente se vuelve a intentar salvo que la ventana se cierre (finComunicacion)
		    	}
		    	serverSocket.close();  // Cierra el socket de servidor
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    		lEstado.setText("Error en servidor: " + e.getMessage());
	    	}
	    }
	}
	
	private static class HiloComunicacion extends Thread {
		private Socket socket;  // socket de comunicación con cada cliente
		private String nomCliente; // nombre de cada cliente
		public HiloComunicacion(Socket socket, String nomCliente) {
			this.socket = socket;
			this.nomCliente = nomCliente;
		}
		@Override
		public void run() {
	    	try {
	    		lEstado.setText( "Nuevo cliente conectado: " + nomCliente );
	    		socket.setSoTimeout( 1000 ); // Pone el timeout para que no se quede eternamente en la espera (1)
	    		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());  // Canal de entrada de socket (leer del cliente)
	    		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());  // Canal de salida de socket (escribir al cliente)
	    		listaHilos.add( this );
	    		listaSockets.add( socket );
	    		listaOOS.add( output );
	    		while(!finComunicacion) {  // Bucle de comunicación de tiempo real con el cliente
	    			try {
		    			Object objRecibido = input.readObject();  // Espera a recibir petición de cliente (1) - se acaba con timeout
		    			if (objRecibido==null) {  // Si se recibe un objeto nulo es un error
		    	    		lEstado.setText( "ERROR: El cliente " + nomCliente + " ha enviado null." );
		    			} else if(objRecibido.equals( ConfigCS.FIN )) { // Si se recibe fin se acaba el proceso con este cliente
			    			output.writeObject( ConfigCS.RECIBIDO ); // Confirma al cliente el fin
		    				break;
		    			}
		    			lEstado.setText( "Recibido de cliente " + nomCliente + ": [" + objRecibido + "]" );
		    			taMensajes.append( "[" + nomCliente + "] " + objRecibido + "\n" );
		    			taMensajes.setSelectionStart( taMensajes.getText().length() );  // Pone el cursor al final del textarea
		    			output.writeObject( ConfigCS.RECIBIDO ); // Confirma al cliente
		    			// Envía el mensaje al resto de clientes
		    			for (ObjectOutputStream oos : listaOOS) {
		    				if (oos!=output) {  // Al cliente actual no, solo al resto
		    					try {
			    					oos.writeObject( ConfigCS.RECIBIDO_DE );
			    					oos.writeObject( nomCliente );
			    					oos.writeObject( objRecibido );
		    					} catch (Exception e) {
		    			    		e.printStackTrace();
		    			    		lEstado.setText("Error en envío a cliente " + nomCliente + ": " + e.getClass().getName() + " - " + e.getMessage());
		    					}
		    				}
		    			}
	    			} catch (SocketTimeoutException e) {} // Excepción de timeout - no es un problema
	    		}
	    		lEstado.setText( "El cliente " + nomCliente + " se ha desconectado." );
	    		socket.close();
	    		// Quita el cliente de las listas para que no se use en lo sucesivo
	    		int clienteACerrar = listaSockets.indexOf( socket );
	    		listaHilos.removeElementAt( clienteACerrar );
	    		listaSockets.removeElementAt( clienteACerrar );
	    		listaOOS.removeElementAt( clienteACerrar );
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    		lEstado.setText("Error en comunicación con cliente " + nomCliente + ": " + e.getClass().getName() + " - " + e.getMessage());
	    	}
		}
	}

}
