package es.deusto.prog3.cap03.cs2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.*;

import javax.swing.ImageIcon;

/** Gestor de persistencia basado en base de datos y servidor de ficheros remotos
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ServicioPersistenciaBDRemota implements ServicioPersistenciaProductos {

	private Logger logger = null;
	private HiloEscuchaServidorRemoto hilo = null;
	private ObjectOutputStream flujoOut = null;
	private boolean finComunicacion = false;
	private long TIMEOUT_ESPERA_SERVIDOR = 5000;  // Máximo tiempo a esperar respuesta del servidor
	
	private Vector<Object> respuestasServidor = new Vector<>();  // Respuestas del servidor encoladas para ser procesadas según procedan
	
	/** Crea un servicio de persistencia basado en BD remota
	 */
	public ServicioPersistenciaBDRemota() {
	}

	// Hilo para escucha asíncrona del servidor
	// En este ejemplo se podría haber hecho la escucha síncrona porque el servidor siempre responde a una petición que le hacemos desde este cliente
	// Pero esta manera de programarlo permite gestionar cualquier tipo de comunicación asíncrona también (envíos del servidor que no responden a peticiones)
	private class HiloEscuchaServidorRemoto extends Thread {
		private String server;
		private int puerto;
		HiloEscuchaServidorRemoto( String server, int puerto ) {
			this.server = server;
			this.puerto = puerto;
		}
		@Override
		public void run() {
	    	// Hilo que abre el socket y se va a quedar escuchando los mensajes del servidor
	        try (Socket socket = new Socket( server, puerto )) {
	    		socket.setSoTimeout( 1000 ); // Pone el timeout para que no se quede eternamente en la espera (1)
	            flujoOut = new ObjectOutputStream(socket.getOutputStream());
	            ObjectInputStream flujoIn = new ObjectInputStream(socket.getInputStream());
	            do { // Bucle de espera a mensajes del servidor
	            	try {
	            		Object respuesta = flujoIn.readObject();  // Devuelve mensaje de servidor o null cuando se cierra la comunicación
	            		respuestasServidor.add( respuesta );
	            		log( Level.INFO, "Respuesta del servidor " + respuesta, null );
	    			} catch (SocketTimeoutException e) {} // Excepción de timeout - no es un problema
	            } while(!finComunicacion);
				flujoOut.writeObject( ConfigCS.FIN );
        		log( Level.INFO, "Fin hilo lectura desde servidor de proceso de cliente de persistencia" , null );
	        } catch (Exception e) {
        		log( Level.WARNING, "Fin hilo lectura desde servidor por excepción de comunicación de proceso de cliente de persistencia" , e );
            	finComunicacion = true;
	        }
		}
	}
	
	/** Inicializa el servicio de persistencia sobre servidor remoto
	 * @param nombrePersistencia	IP de servidor
	 * @param configPersistencia	
	 * @return
	 */
	@Override
	public boolean init(String nombrePersistencia, String configPersistencia) {
		try {
			int puerto = Integer.parseInt( configPersistencia );
			hilo = new HiloEscuchaServidorRemoto( nombrePersistencia, puerto );
			hilo.setDaemon( true );
			hilo.start();
			log( Level.INFO, "Conectando a servidor " + nombrePersistencia + ":" + puerto, null );
			// TODO proceso de login con usuario y contraseña real (se usan nombres por defecto)
			// Espera a conexión con el servidor
			long time = System.currentTimeMillis();
			while (flujoOut==null && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a conexión de servidor", null );
				return false;
			}
			flujoOut.writeObject( ConfigCS.LOGIN );
			flujoOut.writeObject( "USUARIO" );
			flujoOut.writeObject( "PASSWORD" );
			// Espera a respuesta del servidor
			time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a login de servidor", null );
				return false;
			}
			String resp = (String) respuestasServidor.remove(0);
			log( Level.INFO, "Login en servidor completado :" + resp, null );
			return resp.equals( ConfigCS.OK );
		} catch (NumberFormatException e) {
			log( Level.WARNING, "Error en conexión con servidor, puerto incorrecto " + configPersistencia, e );
			return false;
		} catch (Exception e) {
			log( Level.WARNING, "Error en conexión o login con servidor " + nombrePersistencia + ":" + configPersistencia, e );
			return false;
		}
	}
	
	@Override
	public boolean initDatosTest(String nombrePersistencia, String configPersistencia) {
		boolean ret = true;
		if (flujoOut==null) {  // Si no está ya inicializada la comunicación, inicializarla primerao
			ret = init( nombrePersistencia, configPersistencia );
		}
		if (ret) {
			try {
				flujoOut.writeObject( ConfigCS.BORRA_PRODUCTOS );
				Producto prod1 = new Producto( 1, "HTC Vive", 304.12, "fotosInit/htc-vive.jpg" );
				ImageIcon imagen = new ImageIcon( ServicioPersistenciaBDRemota.class.getResource( "fotosInit/htc-vive.jpg" ) );
				flujoOut.writeObject( ConfigCS.INSERTAR );
				flujoOut.writeObject( prod1 );
				flujoOut.writeObject( imagen );
				Producto prod2 = new Producto( 2, "Meta Quest 2", 449.99, "fotosInit/meta-quest-2.jpg" );
				imagen = new ImageIcon( ServicioPersistenciaBDRemota.class.getResource( "fotosInit/meta-quest-2.jpg" ) );
				flujoOut.writeObject( ConfigCS.INSERTAR );
				flujoOut.writeObject( prod2 );
				flujoOut.writeObject( imagen );
				Producto prod3 = new Producto( 3, "Meta Quest Pro", 1799.99, "fotosInit/meta-quest-pro.jpg" );
				imagen = new ImageIcon( ServicioPersistenciaBDRemota.class.getResource( "fotosInit/meta-quest-pro.jpg" ) );
				flujoOut.writeObject( ConfigCS.INSERTAR );
				flujoOut.writeObject( prod3 );
				flujoOut.writeObject( imagen );
				log( Level.INFO, "Creada base de datos con datos de prueba " + nombrePersistencia, null );
				// Espera a respuestas del servidor
				long time = System.currentTimeMillis();
				while (respuestasServidor.size() < 3 && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
					Thread.sleep( 100 );
				}
				if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
					log( Level.WARNING, "Timeout en espera a inserción de datos de prueba en servidor", null );
					return false;
				}
				prod1.setRutaFoto( ((Producto) respuestasServidor.get(0)).getRutaFoto() );
				prod2.setRutaFoto( ((Producto) respuestasServidor.get(1)).getRutaFoto() );
				prod3.setRutaFoto( ((Producto) respuestasServidor.get(2)).getRutaFoto() );
				respuestasServidor.clear();  // Se borran los productos recibidos del servidor tras actualizarlos
			} catch (Exception e) {
				log( Level.SEVERE, "Error en inicialización de base de datos remota de test " + nombrePersistencia, e );
				return false;
			}
		}
		return ret;
	}

	@Override
	public void close() {
		try {
			flujoOut.writeObject( ConfigCS.FIN );
			finComunicacion = true;
			log( Level.INFO, "Cierre de conexión con base de datos remota", null );
		} catch (Exception e) {
			log( Level.SEVERE, "Error en cierre de base de datos remota", e );
			e.printStackTrace();
		}
	}

	@Override
	public int getNumeroProductos() {
		try {
			flujoOut.writeObject( ConfigCS.GET_NUMERO_PRODUCTOS );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a número de productos en servidor", null );
				return 0;
			}
			int numero = (Integer) respuestasServidor.remove(0);
			return numero;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en consulta de número productos a servidor remoto", e );
			return 0;
		}
	}

	@Override
	public Producto buscarCodigo(int codigo) {
		try {
			flujoOut.writeObject( ConfigCS.BUSCAR_CODIGO );
			flujoOut.writeObject( new Integer( codigo ) );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a búsqueda de código en servidor", null );
				return null;
			}
			Producto producto = (Producto) respuestasServidor.remove(0);
			return producto;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en consulta de producto por código a servidor remoto", e );
			return null;
		}
	}

	@Override
	public Producto buscarNombre(String nombre) {
		try {
			flujoOut.writeObject( ConfigCS.BUSCAR_NOMBRE );
			flujoOut.writeObject( nombre );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a búsqueda de nombre en servidor", null );
				return null;
			}
			Producto producto = (Producto) respuestasServidor.remove(0);
			return producto;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en consulta de producto por nombre a servidor remoto", e );
			return null;
		}
	}

	@Override
	public List<Producto> buscarTodos() {
		try {
			flujoOut.writeObject( ConfigCS.BUSCAR_TODOS );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a búsqueda de productos en servidor", null );
				return new ArrayList<Producto>();
			}
			@SuppressWarnings("unchecked")
			List<Producto> lista = (List<Producto>) respuestasServidor.remove(0);
			return lista;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en consulta de todos los productos a servidor remoto", e );
			return new ArrayList<Producto>();
		}
	}

	@Override
	public List<Producto> buscarParteNombre(String parteNombre) {
		try {
			flujoOut.writeObject( ConfigCS.BUSCAR_PARTE_NOMBRE );
			flujoOut.writeObject( parteNombre );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a búsqueda de productos filtrados en servidor", null );
				return new ArrayList<Producto>();
			}
			@SuppressWarnings("unchecked")
			List<Producto> lista = (List<Producto>) respuestasServidor.remove(0);
			return lista;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en consulta de productos por parte de nombre a servidor remoto", e );
			return new ArrayList<Producto>();
		}
	}

	@Override
	public boolean insertar( Producto producto, ImageIcon imagen ) {
		try {
			flujoOut.reset();
			flujoOut.writeObject( ConfigCS.INSERTAR );
			flujoOut.writeObject( producto );
			flujoOut.writeObject( imagen );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a inserción de producto en servidor", null );
				return false;
			}
			Producto ok = (Producto) respuestasServidor.remove(0);
			producto.setRutaFoto( ok.getRutaFoto() );  // Pone la ruta del servidor
			return ok!=null;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en inserción de producto " + producto + " en servidor remoto", e );
			return false;
		}
	}

	@Override
	public boolean actualizar( Producto producto, ImageIcon imagen ) {
		try {
			flujoOut.reset();
			flujoOut.writeObject( ConfigCS.ACTUALIZAR );
System.out.println( producto + " -> " + producto.getRutaFoto() );
			flujoOut.writeObject( producto );
			flujoOut.writeObject( imagen );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a actualización de producto en servidor", null );
				return false;
			}
			Producto ok = (Producto) respuestasServidor.remove(0);
			producto.setRutaFoto( ok.getRutaFoto() );  // Pone la ruta del servidor
			return ok!=null;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en actualización de producto " + producto + " en servidor remoto", e );
			return false;
		}
	}

	@Override
	public ImageIcon cargaImagen(Producto producto) {
		try {
			flujoOut.writeObject( ConfigCS.CARGA_IMAGEN );
			flujoOut.writeObject( producto );
			// Espera a respuesta del servidor
			long time = System.currentTimeMillis();
			while (respuestasServidor.isEmpty() && (System.currentTimeMillis()-time < TIMEOUT_ESPERA_SERVIDOR)) {
				Thread.sleep( 100 );
			}
			if (System.currentTimeMillis()-time >= TIMEOUT_ESPERA_SERVIDOR) {  // Timeout
				log( Level.WARNING, "Timeout en espera a carga de imagen en servidor", null );
				return null;
			}
			ImageIcon icon = (ImageIcon) respuestasServidor.remove(0);
			return icon;
		} catch (Exception e) {
			log( Level.SEVERE, "Error en carga de imagen de producto " + producto + " desde servidor remoto", e );
			return null;
		}
	}

	@Override
	public void setLogger( Logger logger ) {
		this.logger = logger;
	}

	
	// Logging
	
	// Método local para loggear (si no se asigna un logger externo, se asigna uno local)
	private void log( Level level, String msg, Throwable excepcion ) {
		if (logger==null) {  // Logger por defecto local:
			logger = Logger.getLogger( "BD-cliente-ejemplocs" );  // Nombre del logger
			logger.setLevel( Level.ALL );  // Loguea todos los niveles
			try {
				logger.addHandler( new FileHandler( "bd-cliente.log.xml", true ) );  // Y saca el log a fichero xml
			} catch (Exception e) {
				logger.log( Level.SEVERE, "No se pudo crear fichero de log", e );
			}
		}
		if (excepcion==null)
			logger.log( level, msg );
		else
			logger.log( level, msg, excepcion );
	}

}
