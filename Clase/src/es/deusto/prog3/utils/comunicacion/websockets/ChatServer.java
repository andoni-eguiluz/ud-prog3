package es.deusto.prog3.utils.comunicacion.websockets;
//Modificación de ejemplo de https://github.com/TooTallNate/Java-WebSocket
//Necesita librerías externas java-websockets y slf4j

/*
 * Copyright (c) 2010-2020 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class ChatServer extends WebSocketServer {

	public ChatServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public ChatServer(InetSocketAddress address) {
		super(address);
	}

	public ChatServer(int port, Draft_6455 draft) {
		super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conn.send("Welcome to the server!"); //This method sends a message to the new client
		broadcast("new connection: " + handshake
				.getResourceDescriptor()); //This method sends a message to all clients connected
		println(
				conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		broadcast(conn + " has left the room!");
		println(conn + " has left the room!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		broadcast(message);
		println(conn + ": " + message);
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		broadcast(message.array());
		println(conn + ": " + message);
	}

	private static JTextArea taSalida;
	private static JTextField tfMensaje;
	private static ChatServer s;

	public static void main(String[] args) throws UnknownHostException {
		s = null;
		
		JFrame v = new JFrame( "Servidor chat - cierra ventana para cerrar el servidor" );
		v.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		v.setSize( 800, 600 );
		taSalida = new JTextArea();
		tfMensaje = new JTextField();
		v.add( taSalida, BorderLayout.CENTER );
		v.add( tfMensaje, BorderLayout.SOUTH );
		tfMensaje.addActionListener( e -> {
			s.broadcast( tfMensaje.getText() );
			tfMensaje.setText( "" );
		} );
		v.addWindowListener( new WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent e) {
				try {
					s.stop(1000);
				} catch (InterruptedException e1) {}
			}
		} );
		v.setVisible( true );
		try { Thread.sleep( 100 ); } catch (InterruptedException e) {}
		println( "Puerto 80 (¿probar 8887 o 443?" );
		sacaInfoIP();

		int puerto = 80; // 8887; // 843 flash policy port
		try {
			puerto = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			String resp = JOptionPane.showInputDialog( "Puerto de websocket:", "" + puerto );
			try {
				puerto = Integer.parseInt( resp );
			} catch (Exception ex2) {
			}
		}
		s = new ChatServer(puerto);
		s.start();
		println("ChatServer started on port: " + s.getPort());

	}
	
		private static void sacaInfoIP() {
			Enumeration<?> e;
	        println( "Información de IPs:" );
			try {
				e = NetworkInterface.getNetworkInterfaces();
				while(e.hasMoreElements())
				{
				    NetworkInterface n = (NetworkInterface) e.nextElement();
				    Enumeration<?> ee = n.getInetAddresses();
				    while (ee.hasMoreElements())
				    {
				        InetAddress i = (InetAddress) ee.nextElement();
				        println( "    " + i.getHostAddress());
				    }
				}		
			} catch (SocketException e1) {
		        println( "Socket exception!" );
			}
		}

		private static void println( String mens ) {
			taSalida.append( mens + "\n" );
			taSalida.setSelectionStart( taSalida.getText().length() );  // Para ir viendo el texto último añadido (scroll al último punto del texto)
			if (taSalida.getText().length()>100000) {  // Para que no se llene la textarea vamos quitando de vez en cuando
				taSalida.replaceRange( "", 0, 50000 );
			}
		}


	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	@Override
	public void onStart() {
		println("Server started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}

}
