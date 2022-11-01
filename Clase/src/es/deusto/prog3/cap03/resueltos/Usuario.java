package es.deusto.prog3.cap03.resueltos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/** Clase para gestionar usuarios. Ejemplo para ver guardado y recuperación desde ficheros
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nick;
	private String password;
	private String nombre;
	private String apellidos;
	private long telefono;
	private long fechaUltimoLogin;
	private TipoUsuario tipo;
	private ArrayList<String> listaEmails;

	public String getNick() {
		return nick;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public long getTelefono() {
		return telefono;
	}
	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}
	public long getFechaUltimoLogin() {
		return fechaUltimoLogin;
	}
	public void setFechaUltimoLogin(long fechaUltimoLogin) {
		this.fechaUltimoLogin = fechaUltimoLogin;
	}
	public TipoUsuario getTipo() {
		return tipo;
	}
	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}
	public ArrayList<String> getListaEmails() {
		return listaEmails;
	}
	public void setListaEmails(ArrayList<String> listaEmails) {
		this.listaEmails = listaEmails;
	}
	/** Devuelve los emails como un string único, en una lista separada por comas
	 * @return	Lista de emails
	 */
	public String getEmails() {
		String ret = "";
		if (listaEmails.size()>0) ret = listaEmails.get(0);
		for (int i=1; i<listaEmails.size(); i++) ret += (", " + listaEmails.get(i));
		return ret;
	}
	
	/** Constructor privado, sólo para uso interno
	 */
	private Usuario() {
	}
	
	/** Constructor principal de usuario
	 * @param nick
	 * @param password
	 * @param nombre
	 * @param apellidos
	 * @param telefono
	 * @param tipo
	 * @param listaEmails
	 */
	public Usuario(String nick, String password, String nombre,
			String apellidos, long telefono, TipoUsuario tipo,
			ArrayList<String> listaEmails) {
		super();
		this.nick = nick;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.tipo = tipo;
		this.listaEmails = listaEmails;
	}
	
	/** Constructor de usuario recibiendo los emails como una lista de parámetros de tipo String
	 * @param nick
	 * @param password
	 * @param nombre
	 * @param apellidos
	 * @param telefono
	 * @param tipo
	 * @param emails
	 */
	public Usuario(String nick, String password, String nombre,
			String apellidos, long telefono, TipoUsuario tipo,
			String... emails ) {
		this( nick, password, nombre, apellidos, telefono, tipo, 
			new ArrayList<String>( Arrays.asList(emails)) );
	}

	@Override
	public String toString() {
		return "Nick: " + nick + "\nNombre: " + nombre + " " + apellidos + 
			"\nTeléfono: " + telefono + "\nTipo de usuario: " + tipo +
			"\nEmails: " + listaEmails;
	}

	/** Devuelve los datos del usuario en una línea separados por comas<p>
	 * Formato: nick,password,nombre,apellidos,telefono,fechaUltimoLogin(msgs.),tipo,email1,email2...
	 * @return	Línea con los datos formateados
	 */
	public String aLinea() {
		String ret = nick + "," + password + "," + nombre + "," + apellidos + "," +
			telefono + "," + fechaUltimoLogin + "," + tipo;
		for (String email : listaEmails) {
			ret = ret + "," + email;
		}
		return ret;
	}

	/** Devuelve los datos del usuario en una varias líneas después de cada tag de dato<p>
	 * Formato: <p>
	 * [nick] nick\n<p>
	 * [password] password\n<p>
	 * [nombre] nombre\n<p>
	 * [apellidos] apellidos\n<p>
	 * [telefono] telefono\n<p>
	 * [fechaUltimoLogin] fechaUltimoLogin(msgs.)\n<p>
	 * [tipo] tipo\n<p>
	 * [emails] email1,email2...
	 * @return	Líneas de texto en un string con los datos formateados
	 */
	public String aLineasConTags() {
		String ret = "[nick] "            + nick 
				+ "\n[password] "         + password 
				+ "\n[nombre] "           + nombre 
				+ "\n[apellidos] "        + apellidos 
				+ "\n[telefono] "         + telefono 
				+ "\n[fechaUltimoLogin] " + fechaUltimoLogin 
				+ "\n[tipo] "             + tipo
				+ "\n[emails] ";
		String sep = "";
		for (String email : listaEmails) {
			ret = ret + sep + email;
			sep = ",";
		}
		return ret;
	}

	/** Crea y devuelve un nuevo Usuario partiendo de los datos de una línea separados por comas
	 * Formato: nick,password,nombre,apellidos,telefono,fechaUltimoLogin(msgs.),tipo,email1,email2...
	 * @param linea	Línea de texto
	 * @return	Usuario creado partiendo de la línea, null si hay cualquier error
	 */
	public static Usuario crearDeLinea( String linea ) {
		Usuario u = new Usuario();
		StringTokenizer st = new StringTokenizer( linea, "," );
		try {
			u.nick = st.nextToken();
			u.password = st.nextToken();
			u.nombre = st.nextToken();
			u.apellidos = st.nextToken();
			u.telefono = Long.parseLong( st.nextToken() );
			u.fechaUltimoLogin = Long.parseLong( st.nextToken() );
			u.tipo = TipoUsuario.valueOf( st.nextToken() );
			u.listaEmails = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				u.listaEmails.add( st.nextToken() );
			}
			return u;
		} catch (NoSuchElementException e) {  // Error en datos insuficientes (faltan campos)
			return null;
		} catch (NumberFormatException e) {  // Error en tipo long de telefono o fechaLogin
			return null;
		} catch (IllegalArgumentException e) {  // Error en tipo usuario (enumerado)
			return null;
		} catch (Exception e) {  // Cualquier otro error
			return null;
		}
	}

	/** Crea y devuelve un nuevo Usuario partiendo de los datos de un fichero
	 * en varias líneas con tags, con el formato<p>:
	 * [nick] nick\n<p>
	 * [password] password\n<p>
	 * [nombre] nombre\n<p>
	 * [apellidos] apellidos\n<p>
	 * [telefono] telefono\n<p>
	 * [fechaUltimoLogin] fechaUltimoLogin(msgs.)\n<p>
	 * [tipo] tipo\n<p>
	 * [emails] email1,email2...\n<p>
	 * [FINUSUARIO]
	 * Si se encuentra la linea 
	 * @return	Usuario creado con los valores leídos, o null si hay cualquier error
	 */
	public static Usuario crearDeLineasConTags( BufferedReader br ) {
		Usuario u = new Usuario();
		try {
			u.nick = chequearYLeerTag( br, "[nick]" );
			u.password = chequearYLeerTag( br, "[password]" );
			u.nombre = chequearYLeerTag( br, "[nombre]" );
			u.apellidos = chequearYLeerTag( br, "[apellidos]" );
			String valor = chequearYLeerTag( br, "[telefono]" );
			u.telefono = Long.parseLong( valor );
			valor = chequearYLeerTag( br, "[fechaUltimoLogin]" );
			u.fechaUltimoLogin = Long.parseLong( valor );
			valor = chequearYLeerTag( br, "[tipo]" );
			u.tipo = TipoUsuario.valueOf( valor );
			valor = chequearYLeerTag( br, "[emails]" );
			u.listaEmails = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer( valor, "," );
			while (st.hasMoreTokens()) {
				u.listaEmails.add( st.nextToken() );
			}
			chequearYLeerTag( br, "[FINUSUARIO]" );
			return u;
		} catch (IOException e) {  // Error en lectura del fichero
			return null;
		} catch (NumberFormatException e) {  // Error en tipo long de telefono o fechaLogin
			return null;
		} catch (IllegalArgumentException e) {  // Error en tipo usuario (enumerado)
			return null;
		} catch (Exception e) {  // Cualquier otro error
			return null;
		}
	}

		// Lee del fichero una línea, intentando comprobar si empieza en el tag indicado
		// y tiene un espacio después y el valor correspondiente.
		// Devuelve ese valor, o genera una excepción si hay error
		private static String chequearYLeerTag( BufferedReader br, String tag ) throws IOException, Exception {
			String val = br.readLine();
			if (val.startsWith(tag)) {
				val = val.substring( tag.length() );
				if (val.startsWith(" ")) val = val.substring(1);  // Quita el primer espacio
				return val;
			} else {
				throw new Exception("Tag incorrecto. Esperado '" + tag + " ' y encontrada línea " + val );
			}
		}
	
	/** main de prueba
	 * @param s	Parámetros estándar (no se utilizan)
	 */
	public static void main( String[] s ) {
		Usuario u = new Usuario( "buzz", "#9abbf", "Buzz", "Lightyear", 101202303, TipoUsuario.Admin, "buzz@gmail.com", "amigo.de.woody@gmail.com" );
		u.getListaEmails().add( "buzz@hotmail.com" );
		// String ape = u.getApellidos(); ape = "Apellido falso";  // esto no cambia nada
		System.out.println( u );
	}

	// Dos usuarios son iguales si TODOS sus campos son iguales
	public boolean equals( Object o ) {
		Usuario u2 = null;
		if (o instanceof Usuario) u2 = (Usuario) o;
		else return false;  // Si no es de la clase, son diferentes
		return (nick.equals(u2.nick))
			&& (password.equals(u2.password))
			&& (nombre.equals(u2.nombre))
			&& (apellidos.equals(u2.apellidos))
			&& (telefono == u2.telefono)
			&& (fechaUltimoLogin == u2.fechaUltimoLogin)
			&& (listaEmails.equals( u2.listaEmails ));
	}

	/** Inicializa una BD SQLITE y devuelve una conexión con ella
	 * @param nombreBD	Nombre de fichero de la base de datos
	 * @return	Conexión con la base de datos indicada. Si hay algún error, se devuelve null
	 */
	public static Connection initBD( String nombreBD ) {
		try {
		    Class.forName("org.sqlite.JDBC");
		    Connection con = DriverManager.getConnection("jdbc:sqlite:" + nombreBD );
		    return con;
		} catch (ClassNotFoundException | SQLException e) {
			return null;
		}
	}
	
	/** Crea la tabla de usuarios en una base de datos
	 * @param con	Conexión ya creada y abierta a la base de datos
	 * @return	sentencia de trabajo si se crea correctamente, null si hay cualquier error
	 */
	public static Statement crearTablaBD( Connection con ) {
		try {
			Statement statement = con.createStatement();
			statement.setQueryTimeout(30);  // poner timeout 30 msg
			// La borramos si ya existe:
			statement.executeUpdate("drop table if exists usuario");
			statement.executeUpdate("create table usuario " +
				"(nick string, password string, nombre string, apellidos string" +
				", telefono integer, fechaultimologin bigint, tipo string" +
				", emails string)");
			return statement;
		} catch (SQLException e) {
			return null;
		}
	}
	
	/** Añade el usuario a la tabla abierta de BD, usando la sentencia
	 * (INSERT de SQL)
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al usuario)
	 * @return	true si la inserción es correcta, false en caso contrario
	 */
	public boolean anyadirFilaATabla( Statement st ) {
		try {
			String listaEms = "";
			String sep = "";
			for (String email : listaEmails) {
				listaEms = listaEms + sep + email;
				sep = ",";
			}
			String sentSQL = "insert into usuario values(" +
					"'" + nick + "', " +
					"'" + password + "', " +
					"'" + nombre + "', " +
					"'" + apellidos + "', " +
					telefono + ", " +
					fechaUltimoLogin + ", " +
					"'" + tipo + "', " +
					"'" + listaEms + "')";
			System.out.println( sentSQL );  // (Quitar) para ver lo que se hace
			int val = st.executeUpdate( sentSQL );
			if (val!=1) return false;  // Se tiene que añadir 1 - error si no
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/** Realiza una consulta a la tabla abierta de usuarios de la BD, usando la sentencia
	 * (SELECT de SQL)
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al usuario)
	 * @param codigoSelect
	 * @return	lista de usuarios cargados desde la base de datos, null si hay cualquier error
	 */
	public static ArrayList<Usuario> consultaATabla( Statement st, String codigoSelect ) {
		ArrayList<Usuario> ret = new ArrayList<>();
		try {
			String sentSQL = "select * from usuario";
			if (codigoSelect!=null && !codigoSelect.equals(""))
				sentSQL = sentSQL + " where " + codigoSelect;
			System.out.println( sentSQL );  // (Quitar) para ver lo que se hace
			ResultSet rs = st.executeQuery( sentSQL );
			while (rs.next()) {
				Usuario u = new Usuario();
				u.nick = rs.getString( "nick" );
				u.password = rs.getString( "password" );
				u.nombre = rs.getString( "nombre" );
				u.apellidos = rs.getString( "apellidos" );
				u.telefono = rs.getInt( "telefono" );
				u.fechaUltimoLogin = rs.getLong( "fechaultimologin" );
				u.tipo = TipoUsuario.valueOf( rs.getString( "tipo" ) );
				u.listaEmails = new ArrayList<String>();
				StringTokenizer stt = new StringTokenizer( rs.getString("emails"), "," );
				while (stt.hasMoreTokens()) {
					u.listaEmails.add( stt.nextToken() );
				}
				ret.add( u );
			}
			rs.close();
			return ret;
		} catch (IllegalArgumentException e) {  // Error en tipo usuario (enumerado)
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
