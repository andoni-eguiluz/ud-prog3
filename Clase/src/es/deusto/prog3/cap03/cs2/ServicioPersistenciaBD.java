package es.deusto.prog3.cap03.cs2;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import javax.swing.ImageIcon;

/** Gestor de persistencia basado en base de datos local
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ServicioPersistenciaBD implements ServicioPersistenciaProductos {

	private Logger logger = null;

	private Connection connection;
	private Statement statement;
	private static Exception lastError = null;  // Información de último error SQL ocurrido
	
	/** Crea un servicio de persistencia basado en BD local
	 */
	public ServicioPersistenciaBD() {
	}
	
	/** Inicializa una BD SQLITE y devuelve una conexión con ella
	 * @param nombrePersistencia	Nombre de fichero de la base de datos
	 * @param configPersistencia	Configuración de conexión a base de datos (previa al nombre de base de datos) si procede
	 * @return	true si se inicia correctamente, false en caso contrario
	 */
	@Override
	public boolean init(String nombrePersistencia, String configPersistencia) {
		try {
		    Class.forName("org.sqlite.JDBC");
		    connection = DriverManager.getConnection("jdbc:sqlite:" + configPersistencia + nombrePersistencia );
			log( Level.INFO, "Conectada base de datos " + nombrePersistencia, null );
			statement = connection.createStatement();
			statement.setQueryTimeout( 10 );
			statement.executeUpdate("create table if not exists producto (id integer, nombre string, precio double, foto string)");
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			lastError = e;
			connection = null;
			log( Level.SEVERE, "Error en conexión de base de datos " + nombrePersistencia, e );
			return false;
		}
	}
	
	/** Inicializa una BD SQLITE y devuelve una conexión con ella
	 * @param nombrePersistencia	Nombre de fichero de la base de datos
	 * @param configPersistencia	Configuración de conexión a base de datos (previa al nombre de base de datos) si procede
	 * @return	true si se inicia correctamente, false en caso contrario
	 */
	@Override
	public boolean initDatosTest(String nombrePersistencia, String configPersistencia) {
		boolean ret = init( nombrePersistencia, configPersistencia );
		if (ret) {
			try {
				statement.executeUpdate("drop table producto");
				statement.executeUpdate("create table producto (id integer, nombre string, precio double, foto string)");
				statement.executeUpdate("insert into producto (id, nombre, precio, foto) values ( 1, 'HTC Vive', 304.12, 'fotosInit/htc-vive.jpg')" );
				statement.executeUpdate("insert into producto (id, nombre, precio, foto) values ( 2, 'Meta Quest 2', 449.99, 'fotosInit/meta-quest-2.jpg')" );
				statement.executeUpdate("insert into producto (id, nombre, precio, foto) values ( 3, 'Meta Quest Pro', 1799.99, 'fotosInit/meta-quest-pro.jpg')" );
				log( Level.INFO, "Creada base de datos con datos de prueba " + nombrePersistencia, null );
			} catch (SQLException e) {
				lastError = e;
				connection = null;
				log( Level.SEVERE, "Error en manipulación de base de datos de test " + nombrePersistencia, e );
				return false;
			}
		}
		return ret;
	}

	@Override
	public void close() {
		try {
			if (statement!=null) statement.close();
			if (connection!=null) connection.close();
			log( Level.INFO, "Cierre de base de datos", null );
		} catch (SQLException e) {
			lastError = e;
			log( Level.SEVERE, "Error en cierre de base de datos", e );
			e.printStackTrace();
		}
	}

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	// No se considera la imagen en almacenamiento local
	@Override
	public boolean insertar(Producto producto, ImageIcon imagen) {
		String sentSQL = "";
		try {
			actualizaFotoSiProcede( producto );
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

		private void actualizaFotoSiProcede( Producto producto ) {
			if (!producto.getRutaFoto().startsWith("fotosProductos/")) {
				// Si la foto está en otra ruta, se copia a la ruta de todas las fotos
				File fOrigen = new File( producto.getRutaFoto() );
				if (!fOrigen.exists()) {
					log( Level.SEVERE, "Fichero " + producto.getRutaFoto() + " no encontrado al intentar guardar producto " + producto, null );
					return;
				}
				File carpeta = new File( "fotosProductos" );
				try {
					Files.createDirectory( carpeta.toPath() );
					String nuevoPath = "fotosProductos/" + fOrigen.getName();
					Files.copy( fOrigen.toPath(), (new File( nuevoPath )).toPath() );
					producto.setRutaFoto( nuevoPath );
				} catch (IOException e) {
					log( Level.SEVERE, "Fichero " + producto.getRutaFoto() + " no se ha podido respaldar en el servicio de persistencia para el producto " + producto, e );
				}
			}
		}
	
	// No se considera la imagen en almacenamiento local
	@Override
	public boolean actualizar(Producto producto, ImageIcon imagen ) {
		String sentSQL = "";
		try {
			actualizaFotoSiProcede( producto );
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

	@Override
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

	@Override
	public void setLogger( Logger logger ) {
		this.logger = logger;
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

	// Logging
	
	// Método local para loggear (si no se asigna un logger externo, se asigna uno local)
	private void log( Level level, String msg, Throwable excepcion ) {
		if (logger==null) {  // Logger por defecto local:
			logger = Logger.getLogger( "BD-local" );  // Nombre del logger
			logger.setLevel( Level.ALL );  // Loguea todos los niveles
			try {
				logger.addHandler( new FileHandler( "bd.log.xml", true ) );  // Y saca el log a fichero xml
			} catch (Exception e) {
				logger.log( Level.SEVERE, "No se pudo crear fichero de log", e );
			}
		}
		if (excepcion==null)
			logger.log( level, msg );
		else
			logger.log( level, msg, excepcion );
	}

	/** Devuelve la información de excepción del último error producido por cualquiera 
	 * de los métodos de gestión de base de datos
	 */
	public Exception getLastError() {
		return lastError;
	}
		
}
