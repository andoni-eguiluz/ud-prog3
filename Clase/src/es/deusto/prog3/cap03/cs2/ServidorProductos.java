package es.deusto.prog3.cap03.cs2;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/** Ejemplo de utilización de sockets para comunicar un programa "servidor"
 * con VARIOS "clientes", en el mismo equipo o en la misma red
 * (en la UD es probable que no funcione porque los puertos están limitados)
 * Cada cliente puede enviar textos o iconos al servidor
 * El servidor envía un mensaje de confirmación al recibir cada mensaje a todos los clientes conectados.
 * Además envía el mensaje al resto de los clientes
 * 
 * El servidor guarda una base de datos con todos los mensajes recibidos
 * 
 * Ejecutar primero el servidor (este programa) y luego lanzar los clientes que se deseen
 * 
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ServidorProductos {

	private static Logger logger = null;
	static {
		logger = Logger.getLogger( "BD-server-ejemplocs" );  // Nombre del logger
		logger.setLevel( Level.ALL );  // Loguea todos los niveles
		try {
			logger.addHandler( new FileHandler( "bd-server.log.xml", true ) );  // Y saca el log a fichero xml
		} catch (Exception e) {
			logger.log( Level.SEVERE, "No se pudo crear fichero de log", e );
		}
	}
	
	// Atributos de comunicación
	private static Vector<HiloComunicacion> listaHilos; // Lista de hilos de comunicación  
	private static Vector<Socket> listaSockets;         // Lista de sockets de comunicación (correspondientes a los hilos)
	private static Vector<ObjectOutputStream> listaOOS; // Lista de flujos de salida (correspondientes a los sockets)
    private static boolean finComunicacion = false;
    private static int numTotalClientes = 0;
	
	// Atributos visuales
	private static VentanaServidor ventana;  // Ventana del servidor
	private static JLabel lEstado = new JLabel( " " );
	private static JTextArea taMensajes = new JTextArea( 10, 1 );
	
	// Base de datos
	private static BDServidor bd;
    
	public static void main(String[] args) {
		bd = new BDServidor( "productos-servidor.db" );
		listaHilos = new Vector<>();
		listaSockets = new Vector<>();
		listaOOS = new Vector<>();
		ventana = new VentanaServidor();
		ventana.setVisible( true );
		(new Thread() {
			@Override
			public void run() {
				ventana.lanzaServidor();
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
					bd.close();
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
		    			if (finComunicacion) {
		    				return;
		    			}
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
	    	bd.close();
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
    			// Guarda la conexión en base de datos
	    		bd.insertarComunicacion( "[CONEXIÓN]", nomCliente, System.currentTimeMillis() );
	    		lEstado.setText( "Nuevo cliente conectado: " + nomCliente );
	    		socket.setSoTimeout( 5000 ); // Pone el timeout para que no se quede eternamente en la espera (1)
	    		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());  // Canal de entrada de socket (leer del cliente)
	    		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());  // Canal de salida de socket (escribir al cliente)
	    		listaHilos.add( this );
	    		listaSockets.add( socket );
	    		listaOOS.add( output );
	    		while(!finComunicacion) {  // Bucle de comunicación de tiempo real con el cliente
	    			try {
		    			Object objRecibido = input.readObject();  // Espera a recibir petición de cliente (1) - se acaba con timeout
		    			lEstado.setText( "Recibido de cliente " + nomCliente + ": [" + objRecibido + "]" );
		    			taMensajes.append( "Recibido de cliente " + nomCliente + ": [" + objRecibido + "]\n" );
		    			// Guarda el registro de comunicación también en base de datos
		    			bd.insertarComunicacion( "[RECIBIDO] " + nomCliente, objRecibido==null ? "null" : objRecibido.toString(), System.currentTimeMillis() );
		    			if (objRecibido==null) {  // Si se recibe un objeto nulo es un error
		    	    		lEstado.setText( "ERROR: El cliente " + nomCliente + " ha enviado null." );
		    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado null", null );
		    			} else if (objRecibido.equals( ConfigCS.LOGIN )) {
		    				// Login de inicio - va seguido de dos strings: USUARIO y CONTRASEÑA y el servidor lo responde con un OK o NO_OK
			    			Object usuario = input.readObject();
			    			Object password = input.readObject();
		    				// TODO No está implementado el proceso de login - se devuelve siempre ok
			    			taMensajes.append( "Recibido usuario-contraseña de cliente " + usuario + " / " + password + "\n" );
			    			output.writeObject( ConfigCS.OK ); // Confirma login al cliente
			    			taMensajes.append( "Contestado a " + nomCliente + ": [" + "ok" + "]\n" );
		    			} else if (objRecibido.equals( ConfigCS.FIN )) {
		    				// Fin de comunicación
			    			bd.insertarComunicacion( "[FIN]", nomCliente, System.currentTimeMillis() );
		    				break;  // Acaba el proceso con el cliente
		    			} else if (objRecibido.equals( ConfigCS.GET_NUMERO_PRODUCTOS )) {
		    				// Petición de número de productos - el servidor responde con un Integer
		    				int numProds = bd.getNumeroProductos();
			    			taMensajes.append( "Recibido num productos de cliente " + nomCliente + ": " + numProds + "\n" );
		    				output.writeObject( new Integer( numProds ) );
			    			taMensajes.append( "Contestado a " + nomCliente + ": [" + numProds + "]\n" );
		    			} else if (objRecibido.equals( ConfigCS.BUSCAR_CODIGO )) {
		    				// Petición de producto por código - va seguido de un Integer (código), el servidor responde con un Producto (o null)
			    			Object codigo = input.readObject();
			    			taMensajes.append( "Recibido código de cliente " + nomCliente + ": " + codigo + "\n" );
			    			if (codigo instanceof Integer) {
			    				Producto producto = bd.buscarCodigo( (Integer) codigo );
			    				output.writeObject( producto );
				    			taMensajes.append( "Contestado a " + nomCliente + ": [" + producto + "]\n" );
			    			} else {
			    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado mensaje incorrecto: " + codigo, null );
			    			}
		    			} else if (objRecibido.equals( ConfigCS.BUSCAR_NOMBRE )) {
		    				// Petición de producto por nombre - va seguido de un String (nombre), el servidor responde con un Producto (o null)
			    			Object nombre = input.readObject();
			    			taMensajes.append( "Recibido nombre de cliente " + nomCliente + ": " + nombre + "\n" );
			    			if (nombre instanceof String) {
			    				Producto producto = bd.buscarNombre( (String) nombre );
			    				output.writeObject( producto );
				    			taMensajes.append( "Contestado a " + nomCliente + ": [" + producto + "]\n" );
			    			} else {
			    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado mensaje incorrecto: " + nombre, null );
			    			}
		    			} else if (objRecibido.equals( ConfigCS.BUSCAR_TODOS )) {
		    				// Petición de productos - el servidor responde con la lista de todos los productos List<Producto>
		    				List<Producto> productos = bd.buscarTodos();
		    				output.writeObject( productos );
			    			taMensajes.append( "Contestado a " + nomCliente + ": Lista de " + productos.size() + "productos\n" );
		    			} else if (objRecibido.equals( ConfigCS.BUSCAR_PARTE_NOMBRE )) {
		    				// Petición de productos por texto - va seguido de un String (texto), el servidor responde con List<Producto>
			    			Object texto = input.readObject();
			    			taMensajes.append( "Recibido texto de cliente " + nomCliente + ": " + texto + "\n" );
			    			if (texto instanceof String) {
			    				List<Producto> productos = bd.buscarParteNombre( (String) texto );
			    				output.writeObject( productos );
				    			taMensajes.append( "Contestado a " + nomCliente + ": Lista de " + productos.size() + "productos\n" );
			    			} else {
			    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado mensaje incorrecto: " + texto, null );
			    			}
		    			} else if (objRecibido.equals( ConfigCS.INSERTAR )) {
		    				// Petición de inserción de producto - va seguido de un Producto y de un ImageIcon, el servidor responde con un boolean
			    			Object producto = input.readObject();
			    			Object imagen = input.readObject();
			    			taMensajes.append( "Recibido de cliente " + nomCliente + ": " + producto + " / " + imagen.getClass() + "\n" );
			    			if (producto instanceof Producto && (imagen == null || imagen instanceof ImageIcon)) {
			    				boolean ret = bd.insertar( (Producto) producto, (ImageIcon) imagen );
			    				output.writeObject( ret ? producto : null );
				    			taMensajes.append( "Contestado a " + nomCliente + ": [" + producto + "]\n" );
			    			} else {
			    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado mensaje incorrecto: " + producto + " / " + imagen.getClass(), null );
			    			}
		    			} else if (objRecibido.equals( ConfigCS.ACTUALIZAR )) {
		    				// Petición de actualización de producto - va seguido de un Producto y de un ImageIcon, el servidor responde con un boolean
			    			Object producto = input.readObject();
			    			Object imagen = input.readObject();
			    			taMensajes.append( "Recibido de cliente " + nomCliente + ": " + producto + " / " + imagen.getClass() + "\n" );
			    			if (producto instanceof Producto && (imagen == null || imagen instanceof ImageIcon)) {
			    				boolean ret = bd.actualizar( (Producto) producto, (ImageIcon) imagen );
			    				output.writeObject( ret ? producto : null );
				    			taMensajes.append( "Contestado a " + nomCliente + ": [" + producto + "]\n" );
			    			} else {
			    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado mensaje incorrecto: " + producto + " / " + imagen.getClass(), null );
			    			}
		    			} else if (objRecibido.equals( ConfigCS.CARGA_IMAGEN )) {
		    				// Petición de imagen de producto - va seguido de un Producto, el servidor responde con un objeto ImageIcon
			    			Object producto = input.readObject();
			    			taMensajes.append( "Recibido de cliente " + nomCliente + ": " + producto + "\n" );
			    			if (producto instanceof Producto) {
			    				ImageIcon ii = bd.cargaImagen( (Producto) producto );
			    				output.writeObject( ii );
				    			taMensajes.append( "Contestado a " + nomCliente + ": [" + (ii==null ? "null" : "ImageIcon") + "]\n" );
			    			} else {
			    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado mensaje incorrecto: " + producto, null );
			    			}
		    			} else if (objRecibido.equals( ConfigCS.BORRA_PRODUCTOS )) {
		    				bd.borraProductos();
		    			} else {
		    				// Recepción errónea (no se puede interpretar lo recibido del cliente)
		    				log( Level.WARNING, "Cliente " + nomCliente + " ha enviado mensaje incorrecto: " + objRecibido, null );
		    			}
		    			taMensajes.setSelectionStart( taMensajes.getText().length() );  // Pone el cursor al final del textarea
	    			} catch (SocketTimeoutException e) {} // Excepción de timeout - no es un problema
	    		}
	    		lEstado.setText( "El cliente " + nomCliente + " se ha desconectado." );
	    		quitaClienteDeListas( socket );
	    		socket.close();
	    	} catch(Exception e) {
	    		lEstado.setText("Error en comunicación con cliente " + nomCliente + ": " + e.getClass().getName() + " - " + e.getMessage());
	    		log( Level.WARNING, "Error en comunicación con cliente " + nomCliente, e );
	    	}
		}
	}
	
	// Quita el cliente de las listas para que dejar de usarlo
	private synchronized static void quitaClienteDeListas( Socket socket ) {
		int clienteACerrar = listaSockets.indexOf( socket );
		listaHilos.removeElementAt( clienteACerrar );
		listaSockets.removeElementAt( clienteACerrar );
		listaOOS.removeElementAt( clienteACerrar );
	}

	
	// Método local para loggear (si no se asigna un logger externo, se asigna uno local)
	private static void log( Level level, String msg, Throwable excepcion ) {
		if (excepcion==null)
			logger.log( level, msg );
		else
			logger.log( level, msg, excepcion );
	}
	

	//--------------------------------------
	// Métodos de acceso a base de datos en el servidor - clase interna BDServidor
	//--------------------------------------
	
	private static class BDServidor {

		private static Exception lastError = null;  // Información de último error SQL ocurrido
		
		private Connection connection;
		private Statement statement;
		
		public BDServidor( String nombreBD ) {
			try {
			    Class.forName("org.sqlite.JDBC");
			    connection = DriverManager.getConnection("jdbc:sqlite:" + nombreBD );
				log( Level.INFO, "Conectada base de datos " + nombreBD, null );
				statement = connection.createStatement();
				statement.setQueryTimeout( 10 );
				statement.executeUpdate("create table if not exists producto (id integer, nombre string, precio double, foto string)");
				statement.executeUpdate("create table if not exists comunicacion (tipo string, dato string, hora bigint)");
			} catch (ClassNotFoundException | SQLException e) {
				lastError = e;
				connection = null;
				log( Level.SEVERE, "Error en conexión de base de datos " + nombreBD, e );
			}
		}
		
		public void borraProductos() {
			try {
				statement.executeUpdate("drop table producto");
				statement.executeUpdate("create table producto (id integer, nombre string, precio double, foto string)");
			} catch (SQLException e) {
				lastError = e;
				connection = null;
				log( Level.SEVERE, "Error en borrado de tabla de productos", e );
			}
		}
		
		public void insertarComunicacion( String tipo, String dato, long fechaHora ) {
			String sql = null;
			try {
				sql = "insert into comunicacion (tipo, dato, hora) values ( "
						+ "'" + tipo + "'" 
						+ ",'" + dato + "'"
						+ "," + fechaHora + ")";
				statement.executeUpdate( sql );
			} catch (SQLException e) {
				lastError = e;
				log( Level.SEVERE, "Error en sentencia de base de datos\t" + sql, e );
			}
		}

		public void close() {
			try {
				if (statement!=null) statement.close();
				if (connection!=null) connection.close();
				statement = null;
				connection = null;
				log( Level.INFO, "Cierre de base de datos", null );
			} catch (SQLException e) {
				lastError = e;
				log( Level.SEVERE, "Error en cierre de base de datos", e );
			}
		}
		
		public int getNumeroProductos() {
			try {
				ResultSet rs = statement.executeQuery( "select count(*) from producto" );
				rs.next();
				return rs.getInt(1);
			} catch (SQLException e) {
				lastError = e;
				log( Level.SEVERE, "Error en manipulación de base de datos", e );
				return 0;
			}
		}

		public Producto buscarCodigo(int codigo) {
			try {
				ResultSet rs = statement.executeQuery( "select * from producto where id = " + codigo );
				if (rs.next()) {
					Producto producto = new Producto( rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getString("foto") );
					return producto;
				} else {
					return null;
				}
			} catch (SQLException e) {
				lastError = e;
				log( Level.SEVERE, "Error en manipulación de base de datos", e );
				return null;
			}
		}

		public Producto buscarNombre(String nombre) {
			try {
				ResultSet rs = statement.executeQuery( "select * from producto where nombre = '" + nombre + "'" );
				if (rs.next()) {
					Producto producto = new Producto( rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getString("foto") );
					rs.close();
					return producto;
				} else {
					rs.close();
					return null;
				}
			} catch (SQLException e) {
				lastError = e;
				log( Level.SEVERE, "Error en búsqueda de base de datos", e );
				return null;
			}
		}

		public List<Producto> buscarTodos() {
			ArrayList<Producto> l = new ArrayList<>();
			try {
				ResultSet rs = statement.executeQuery( "select * from producto" );
				while (rs.next()) {
					Producto producto = new Producto( rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getString("foto") );
					l.add( producto );
				}
				rs.close();
			} catch (SQLException e) {
				lastError = e;
				log( Level.SEVERE, "Error en búsqueda de base de datos", e );
			}
			return l;
		}

		public List<Producto> buscarParteNombre(String parteNombre) {
			ArrayList<Producto> l = new ArrayList<>();
			try {
				ResultSet rs = statement.executeQuery( "select * from producto where nombre like '%" + secu(parteNombre) + "%'" );
				while (rs.next()) {
					Producto producto = new Producto( rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), rs.getString("foto") );
					l.add( producto );
				}
				rs.close();
			} catch (SQLException e) {
				lastError = e;
				log( Level.SEVERE, "Error en búsqueda de base de datos", e );
			}
			return l;
		}

		public boolean insertar(Producto producto, ImageIcon imagen) {
			String sentSQL = "";
			try {
				boolean ok = actualizaFotoSiProcede( producto, imagen );
				if (!ok) return false;
				sentSQL = "insert into producto (id, nombre, precio, foto) values (" +
						producto.getCodigo() + ", " +
						"'" + secu(producto.getNombre()) + "', " +
						producto.getPrecio() + ", " +
						"'" + secu(producto.getRutaFoto()) + "'" +
						")";
				int val = statement.executeUpdate( sentSQL );
				log( Level.INFO, "Añadida " + val + " fila a base de datos\t" + sentSQL, null );
				if (val!=1) {  // Se tiene que añadir 1 - error si no
					log( Level.WARNING, "Error en insert de base de datos\t" + sentSQL, null );
					return false;  
				}
				return true;
			} catch (SQLException e) {
				log( Level.SEVERE, "Error en inserción de base de datos\t" + sentSQL, e );
				lastError = e;
				return false;
			}
		}

			private boolean actualizaFotoSiProcede( Producto producto, ImageIcon imagen ) {
				if (imagen==null) {  // Si la imagen es nula, se supone que no hay cambios
					File fImagen = new File( producto.getRutaFoto() );
					boolean existe = fImagen.exists();
					if (!existe) {
						log( Level.WARNING, "Error en creación de recurso de imagen, fichero anterior no existente en servidor\t" + producto.getRutaFoto(), null );
					}
					return existe;
				}
				if (!producto.getRutaFoto().startsWith("fotosProductosServidor/")) {
					// Si la foto está en otra ruta, se modifica a la ruta de todas las fotos
					int ultBarra = producto.getRutaFoto().lastIndexOf( "/" );
					if (ultBarra==-1) { ultBarra = producto.getRutaFoto().lastIndexOf( "\\" ); }
					String nombreFic = (ultBarra==-1) ? producto.getRutaFoto() : producto.getRutaFoto().substring( ultBarra+1 );
					producto.setRutaFoto( "fotosProductosServidor/" + nombreFic );
				}
				BufferedImage nuevaImagen = new BufferedImage( imagen.getImage().getWidth(null), imagen.getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB );
				Graphics2D g = nuevaImagen.createGraphics();
				g.drawImage(imagen.getImage(), 0, 0, null);
				g.dispose();
				
				File carpeta = new File( "fotosProductosServidor" );
				String tipoGrafico = producto.getRutaFoto().substring( producto.getRutaFoto().length()-3 );
				if (tipoGrafico.equals("png") || tipoGrafico.equals("jpg") || tipoGrafico.equals("gif")) {
					try {
						if (!carpeta.exists()) {
							Files.createDirectory( carpeta.toPath() );
						}
						ImageIO.write( nuevaImagen, tipoGrafico, new File( producto.getRutaFoto() ) );
						return true;
					} catch (IOException e1) {
						log( Level.WARNING, "Error en creación de recurso de imagen\t" + producto.getRutaFoto(), e1 );
						return false;
					}
				} else {
					log( Level.WARNING, "Extensión de imagen no soportada\t" + producto.getRutaFoto(), null );
					return false;
				}
			}
		
		public boolean actualizar(Producto producto, ImageIcon imagen) {
			String sentSQL = "";
			try {
				boolean ok = actualizaFotoSiProcede( producto, imagen );
				if (!ok) return false;
				sentSQL = "update producto set " +
						"nombre = '" + secu(producto.getNombre()) + "', " +
						"precio = " + producto.getPrecio() + ", " +
						"foto = '" + secu(producto.getRutaFoto()) + "'" +
						"where id = " + producto.getCodigo();
				int val = statement.executeUpdate( sentSQL );
				log( Level.INFO, "Modificada " + val + " fila en base de datos\t" + sentSQL, null );
				if (val!=1) {  // Se tiene que modificar 1 - error si no
					log( Level.WARNING, "Error en update de base de datos\t" + sentSQL, null );
					return false;  
				}
				return true;
			} catch (SQLException e) {
				log( Level.SEVERE, "Error en actualización de base de datos\t" + sentSQL, e );
				lastError = e;
				return false;
			}
		}

		public ImageIcon cargaImagen(Producto producto) {
			File fic = new File( producto.getRutaFoto() );
			if (!fic.exists()) {  // Si no se encuentra como fichero se busca como recurso
				URL url = ServicioPersistenciaFicheros.class.getResource( producto.getRutaFoto() );
				if (url==null) {
					log( Level.WARNING, "Error en carga de imagen " + producto + ": " + producto.getRutaFoto(), null );
					return null;
				} else {
					return new ImageIcon( url );
				}
			} else {
				return new ImageIcon( fic.getAbsolutePath() );
			}
		}
		
		/** Devuelve la información de excepción del último error producido por cualquiera 
		 * de los métodos de gestión de base de datos
		 */
		@SuppressWarnings("unused")
		public Exception getLastError() {
			return lastError;
		}

		// Métodos privados

		// Devuelve el string "securizado" para volcarlo en SQL:
		// Mantiene solo los caracteres seguros en español y sustituye ' por ''
		private static String secu( String string ) {
			StringBuffer ret = new StringBuffer();
			for (char c : string.toCharArray()) {
				if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZñÑáéíóúüÁÉÍÓÚÚ.,:;-_(){}[]-+*=<>'\"¿?¡!&%$@#/\\0123456789 ".indexOf(c)>=0) ret.append(c);
			}
			return ret.toString().replaceAll( "'", "''" );
		}	
		
	}
}

