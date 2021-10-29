package es.deusto.prog3.cap03.ejemploPartidas;

import java.sql.*;
import java.util.*; 
import java.util.logging.*;

//Documentación particular de foreign keys en sqlite en
//https://www.sqlite.org/foreignkeys.html
//Si se quiere hacer sin foreign keys, quitar las líneas marcadas con (1) y sustituirlas por las (2) en BD.java

/** Clase de gestión de base de datos del sistema de usuarios - partidas
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class BD {

	private static Exception lastError = null;  // Información de último error SQL ocurrido
	
	/** Inicializa una BD SQLITE y devuelve una conexión con ella
	 * @param nombreBD	Nombre de fichero de la base de datos
	 * @return	Conexión con la base de datos indicada. Si hay algún error, se devuelve null
	 */
	public static Connection initBD( String nombreBD ) {
		try {
		    Class.forName("org.sqlite.JDBC");
		    Connection con = DriverManager.getConnection("jdbc:sqlite:" + nombreBD );
		    Statement st = con.createStatement();      // (1) Solo para foreign keys
		    st.execute( "PRAGMA foreign_keys = ON" );  // (1)
		    st.close();                                // (1)
			log( Level.INFO, "Conectada base de datos " + nombreBD, null );
		    return con;
		} catch (ClassNotFoundException | SQLException e) {
			lastError = e;
			log( Level.SEVERE, "Error en conexión de base de datos " + nombreBD, e );
			e.printStackTrace();
			return null;
		}
	}
	
	/** Devuelve statement para usar la base de datos
	 * @param con	Conexión ya creada y abierta a la base de datos
	 * @return	sentencia de trabajo si se crea correctamente, null si hay cualquier error
	 */
	public static Statement usarBD( Connection con ) {
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);  // poner timeout 30 msg
			return statement;
		} catch (SQLException e) {
			lastError = e;
			log( Level.SEVERE, "Error en uso de base de datos", e );
			e.printStackTrace();
			return null;
		}
	}
	
	/** Crea las tablas de la base de datos. Si ya existen, las deja tal cual
	 * @param con	Conexión ya creada y abierta a la base de datos
	 * @return	sentencia de trabajo si se crea correctamente, null si hay cualquier error
	 */
	public static Statement usarCrearTablasBD( Connection con ) {
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);  // poner timeout 30 msg
			try {
				statement.executeUpdate("create table usuario " +
					// "(nick string "  // (2) Esto sería sin borrado en cascada ni relación de claves ajenas
					"(nick string PRIMARY KEY" // (1) Solo para foreign keys
					+ ", password string, nombre string, apellidos string" +
					", telefono integer, fechaultimologin bigint, tipo string" +
					", emails string)");
			} catch (SQLException e) {} // Tabla ya existe. Nada que hacer
			try {
				statement.executeUpdate("create table partida " +
					"(usuario_nick string REFERENCES usuario(nick) ON DELETE CASCADE, fechapartida bigint, puntuacion integer)"); // (1) Solo para foreign keys
					// "(usuario_nick string, fechapartida bigint, puntuacion integer)"); // (2) Esto sería sin borrado en cascada
			} catch (SQLException e) {} // Tabla ya existe. Nada que hacer
			log( Level.INFO, "Creada base de datos", null );
			return statement;
		} catch (SQLException e) {
			lastError = e;
			log( Level.SEVERE, "Error en creación de base de datos", e );
			e.printStackTrace();
			return null;
		}
	}
	
	/** Reinicia en blanco las tablas de la base de datos. 
	 * UTILIZAR ESTE MÉTODO CON PRECAUCIÓN. Borra todos los datos que hubiera ya en las tablas
	 * @param con	Conexión ya creada y abierta a la base de datos
	 * @return	sentencia de trabajo si se borra correctamente, null si hay cualquier error
	 */
	public static Statement reiniciarBD( Connection con ) {
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);  // poner timeout 30 msg
			statement.executeUpdate("drop table if exists usuario");
			statement.executeUpdate("drop table if exists partida");
			log( Level.INFO, "Reiniciada base de datos", null );
			return usarCrearTablasBD( con );
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en reinicio de base de datos", e );
			lastError = e;
			e.printStackTrace();
			return null;
		}
	}
	
	/** Cierra la base de datos abierta
	 * @param con	Conexión abierta de la BD
	 * @param st	Sentencia abierta de la BD
	 */
	public static void cerrarBD( Connection con, Statement st ) {
		try {
			if (st!=null) st.close();
			if (con!=null) con.close();
			log( Level.INFO, "Cierre de base de datos", null );
		} catch (SQLException e) {
			lastError = e;
			log( Level.SEVERE, "Error en cierre de base de datos", e );
			e.printStackTrace();
		}
	}
	
	/** Devuelve la información de excepción del último error producido por cualquiera 
	 * de los métodos de gestión de base de datos
	 */
	public static Exception getLastError() {
		return lastError;
	}
	
	/////////////////////////////////////////////////////////////////////
	//                      Operaciones de usuario                     //
	/////////////////////////////////////////////////////////////////////
	
	/** Añade un usuario a la tabla abierta de BD, usando la sentencia INSERT de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al usuario)
	 * @param u	Usuario a añadir en la base de datos
	 * @return	true si la inserción es correcta, false en caso contrario
	 */
	public static boolean usuarioInsert( Statement st, Usuario u ) {
		String sentSQL = "";
		try {
			String listaEms = "";
			String sep = "";
			for (String email : u.getListaEmails()) {
				listaEms = listaEms + sep + secu(email);
				sep = ",";
			}
			sentSQL = "insert into usuario values(" +
					"'" + secu(u.getNick()) + "', " +
					"'" + secu(u.getPassword()) + "', " +
					"'" + secu(u.getNombre()) + "', " +
					"'" + secu(u.getApellidos()) + "', " +
					u.getTelefono() + ", " +
					u.getFechaUltimoLogin() + ", " +
					"'" + u.getTipo() + "', " +
					"'" + listaEms + "')";
			// System.out.println( sentSQL );  // para ver lo que se hace en consola
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD añadida " + val + " fila\t" + sentSQL, null );
			if (val!=1) {  // Se tiene que añadir 1 - error si no
				log( Level.SEVERE, "Error en insert de BD\t" + sentSQL, null );
				return false;  
			}
			return true;
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return false;
		}
	}

	/** Realiza una consulta a la tabla abierta de usuarios de la BD, usando la sentencia SELECT de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al usuario)
	 * @param codigoSelect	Sentencia correcta de WHERE (sin incluirlo) para filtrar la búsqueda (vacía si no se usa)
	 * @return	lista de usuarios cargados desde la base de datos, null si hay cualquier error
	 */
	public static ArrayList<Usuario> usuarioSelect( Statement st, String codigoSelect ) {
		String sentSQL = "";
		ArrayList<Usuario> ret = new ArrayList<>();
		try {
			sentSQL = "select * from usuario";
			if (codigoSelect!=null && !codigoSelect.equals(""))
				sentSQL = sentSQL + " where " + codigoSelect;
			// System.out.println( sentSQL );  // Para ver lo que se hace en consola
			ResultSet rs = st.executeQuery( sentSQL );
			while (rs.next()) {
				Usuario u = new Usuario();
				u.setNick( rs.getString( "nick" ) );
				u.setPassword( rs.getString( "password" ) );
				u.setNombre( rs.getString( "nombre" ) );
				u.setApellidos( rs.getString( "apellidos" ) );
				u.setTelefono( rs.getInt( "telefono" ) );
				u.setFechaUltimoLogin( rs.getLong( "fechaultimologin" ) );
				u.setTipo( TipoUsuario.valueOf( rs.getString( "tipo" ) ) );
				ArrayList<String> listaEmails = new ArrayList<String>();
				StringTokenizer stt = new StringTokenizer( rs.getString("emails"), "," );
				while (stt.hasMoreTokens()) {
					listaEmails.add( stt.nextToken() );
				}
				u.setListaEmails( listaEmails );
				ret.add( u );
			}
			rs.close();
			log( Level.INFO, "BD\t" + sentSQL, null );
			return ret;
		} catch (IllegalArgumentException e) {  // Error en tipo usuario (enumerado)
			log( Level.SEVERE, "Error en BD en tipo de usuario\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return null;
		}
	}

	/** Modifica un usuario en la tabla abierta de BD, usando la sentencia UPDATE de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al usuario)
	 * @param u	Usuario a modificar en la base de datos. Se toma su nick como clave
	 * @return	true si la modificación es correcta, false en caso contrario
	 */
	public static boolean usuarioUpdate( Statement st, Usuario u ) {
		String sentSQL = "";
		try {
			String listaEms = "";
			String sep = "";
			for (String email : u.getListaEmails()) {
				listaEms = listaEms + sep + email;
				sep = ",";
			}
			sentSQL = "update usuario set" +
					// " nick='" + u.getNick() + "', " +  // No hay que actualizar el nick, solo el resto de campos
					" password='" + u.getPassword() + "', " +
					" nombre='" + u.getNombre() + "', " +
					" apellidos='" + u.getApellidos() + "', " +
					" telefono=" + u.getTelefono() + ", " +
					" fechaultimologin=" + u.getFechaUltimoLogin() + ", " +
					" tipo='" + u.getTipo() + "', " +
					" emails='" + listaEms + "'" +
					" where nick='" + u.getNick() + "'";
			// System.out.println( sentSQL );  // para ver lo que se hace en consola
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD modificada " + val + " fila\t" + sentSQL, null );
			if (val!=1) {  // Se tiene que modificar 1 - error si no
				log( Level.SEVERE, "Error en update de BD\t" + sentSQL, null );
				return false;  
			}
			return true;
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return false;
		}
	}

	/** Borrar un usuario de la tabla abierta de BD, usando la sentencia DELETE de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al usuario)
	 * @param u	Usuario a borrar de la base de datos  (se toma su nick para el borrado)
	 * @return	true si el borrado es correcto, false en caso contrario
	 */
	public static boolean usuarioDelete( Statement st, Usuario u ) {
		String sentSQL = "";
		try {
			sentSQL = "delete from usuario where nick='" + secu(u.getNick()) + "'";
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD borrada " + val + " fila\t" + sentSQL, null );
			return (val==1);
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return false;
		}
	}

	/////////////////////////////////////////////////////////////////////
	//                      Operaciones de partida                     //
	/////////////////////////////////////////////////////////////////////
	
	/** Añade una partida a la tabla abierta de BD, usando la sentencia INSERT de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente a la partida)
	 * @param p	Partida a añadir en la base de datos
	 * @return	true si la inserción es correcta, false en caso contrario
	 */
	public static boolean partidaInsert( Statement st, Partida p ) {
		String sentSQL = "";
		try {
			sentSQL = "insert into partida values(" +
					"'" + p.getUsuario().getNick() + "', " +
					"" + p.getFechaPartida() + ", " +
					"" + p.getPuntuacion() + ")";
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD añadida " + val + " fila\t" + sentSQL, null );
			if (val!=1) return false;  // Se tiene que añadir 1 - error si no
			return true;
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return false;
		}
	}
	
	/** Realiza una consulta a la tabla abierta de partidas de la BD, usando la sentencia SELECT de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente a la partida)
	 * @param u	Usuario del cual se quieren cargar las partidas (si es null no se considera)
	 * @param codigoSelect	Sentencia correcta de WHERE (sin incluirlo) para filtrar la búsqueda (vacía si no se usa)
	 * @param lUsuarios	Lista de usuarios reconocidos. Necesaria si se quieren consultar partidas de cualquier usuario (no necesario si el usuario u no es null).
	 *   Si el usuario leído no está en esta lista, esa partida no se devuelve y se saca un mensaje a la salida de error estándar.
	 * @return	lista de partidas cargadas desde la base de datos, null si hay cualquier error SQL
	 */
	public static ArrayList<Partida> partidaSelect( Statement st, Usuario u, String codigoSelect, List<Usuario> lUsuarios ) {
		String sentSQL = "";
		ArrayList<Partida> ret = new ArrayList<>();
		try {
			sentSQL = "select * from partida";
			if (u!=null) {
				String whereUsuario = "usuario_nick='" + u.getNick() + "'";
				if (codigoSelect!=null && !codigoSelect.equals(""))
					sentSQL = sentSQL + " where " + whereUsuario + " AND " + codigoSelect;
				else
					sentSQL = sentSQL + " where " + whereUsuario;
			} else {
				if (codigoSelect!=null && !codigoSelect.equals(""))
					sentSQL = sentSQL + " where " + codigoSelect;
			}
			// System.out.println( sentSQL );  // Para ver lo que se hace en consola
			ResultSet rs = st.executeQuery( sentSQL );
			while (rs.next()) {
				Partida p = new Partida();
				boolean encontrado = true;
				String nick = rs.getString( "usuario_nick" );
				if (u!=null)
					p.setUsuario( u );
				else {
					encontrado = false;
					if (lUsuarios!=null) {
						for (Usuario uBusq : lUsuarios) {
							if (uBusq.getNick().equals( nick )) {
								p.setUsuario( uBusq );
								encontrado = true;
								break;
							}
						}
					}
				}
				if (encontrado) {
					p.setFechaPartida( rs.getLong( "fechapartida" ) );
					p.setPuntuacion( rs.getInt( "puntuacion" ) );
					ret.add( p );
				} else {  // No se encuentra en la lista, no se puede crear el objeto partida
					System.err.println( "Usuario leído de base de datos " + nick + " no está presente en la lista de usuarios suministrada." );
				}
			}
			rs.close();
			log( Level.INFO, "BD\t" + sentSQL, null );
			return ret;
		} catch (IllegalArgumentException e) {  // Error en tipo usuario (enumerado)
			log( Level.SEVERE, "Error en BD en tipo de usuario\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return null;
		}
	}

	/** Borra una partida de la tabla abierta de BD, usando la sentencia DELETE de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente a la partida)
	 * @param p	Partida a borrar en la base de datos
	 * @return	true si se borra una fila correctamente, false en caso contrario
	 */
	public static boolean partidaDelete( Statement st, Partida p ) {
		String sentSQL = "";
		try {
			sentSQL = "delete from partida where usuario_nick=" +
					"'" + p.getUsuario().getNick() + "' AND " +
					"fechapartida=" + p.getFechaPartida();
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD borrada " + val + " fila\t" + sentSQL, null );
			return (val==1);  // Se tiene que borrar 1 - error si no
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD (delete partida)\t" + sentSQL, e );
			lastError = e;
			e.printStackTrace();
			return false;
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////
	//                      Métodos privados                           //
	/////////////////////////////////////////////////////////////////////

	// Devuelve el string "securizado" para volcarlo en SQL
	// (Implementación 1) Sustituye ' por '' y quita saltos de línea
	// (Implementación 2) Mantiene solo los caracteres seguros en español
	// TODO OJO - FALTA algo importante por hacer en la implementación actual... ¿no?
	private static String secu( String string ) {
		// Implementación (1)
		// return string.replaceAll( "'",  "''" ).replaceAll( "\\n", "" );
		// Implementación (2)
		StringBuffer ret = new StringBuffer();
		for (char c : string.toCharArray()) {
			if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZñÑáéíóúüÁÉÍÓÚÚ.,:;-_(){}[]-+*=<>'\"¿?¡!&%$@#/\\0123456789 ".indexOf(c)>=0) ret.append(c);
		}
		return ret.toString();
	}
	

	/////////////////////////////////////////////////////////////////////
	//                      Logging                                    //
	/////////////////////////////////////////////////////////////////////
	
	private static Logger logger = null;
	// Método público para asignar un logger externo
	/** Asigna un logger ya creado para que se haga log de las operaciones de base de datos
	 * @param logger	Logger ya creado
	 */
	public static void setLogger( Logger logger ) {
		BD.logger = logger;
	}
	// Método local para loggear (si no se asigna un logger externo, se asigna uno local)
	private static void log( Level level, String msg, Throwable excepcion ) {
		if (logger==null) {  // Logger por defecto local:
			logger = Logger.getLogger( BD.class.getName() );  // Nombre del logger - el de la clase
			logger.setLevel( Level.ALL );  // Loguea todos los niveles
			try {
				// logger.addHandler( new FileHandler( "bd-" + System.currentTimeMillis() + ".log.xml" ) );  // Y saca el log a fichero xml
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
	
	
	
}
