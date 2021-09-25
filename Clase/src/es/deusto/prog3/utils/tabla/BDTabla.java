package es.deusto.prog3.utils.tabla;

import java.sql.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.*;

/** Clase de gestión de base de datos genérica para manipulación con tabla visual {@link Tabla}
 * Utiliza librería sqlite
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class BDTabla {
	
	private static boolean LOGGING = false;  // true loggea todos; false loguea solo los >= WARNING

	/** Inicializa una BD SQLITE y devuelve una conexión con ella
	 * @param nombreBD	Nombre de fichero de la base de datos
	 * @return	Conexión con la base de datos indicada. Si hay algún error, se devuelve null
	 */
	public static Connection initBD( String nombreBD ) {
		try {
		    Class.forName("org.sqlite.JDBC");
		    Connection con = DriverManager.getConnection("jdbc:sqlite:" + nombreBD );
			log( Level.INFO, "Conectada base de datos " + nombreBD, null );
		    return con;
		} catch (ClassNotFoundException | SQLException e) {
			log( Level.SEVERE, "Error en conexión de base de datos " + nombreBD, e );
			return null;
		}
	}
	
	/** Crea una sentencia de base de datos
	 * @param con	Conexión ya creada y abierta a la base de datos
	 * @return	sentencia de trabajo si se crea correctamente, null si hay cualquier error
	 */
	public static Statement nuevoStatBD( Connection con ) {
		try {
			Statement statement = con.createStatement();
			return statement;
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en creación de base de datos", e );
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
			log( Level.SEVERE, "Error en cierre de base de datos", e );
		}
	}
	
	/////////////////////////////////////////////////////////////////////
	//                      Operaciones sobre tablas                   //
	/////////////////////////////////////////////////////////////////////

	// Genéricas

		private static ArrayList<String> colNames = null;
		private static ArrayList<Class<?>> colTypes = null;

	/** Busca en la tabla de base de datos indicada y devuelve todas las filas de esa tabla en un objeto tabla.
	 * Deja además las cabeceras de la tabla accesibles en los métodos #getColNames() y #getColTypes()
	 * @param statement	Sentencia de BD a utilizar
	 * @param nameTabla	Nombre de la tabla que se quiere descargar
	 * @return	Nueva lista de elementos de la tabla descargada, null si hay cualquier error
	 */
	public static ArrayList<SQLRow> creaTablaDesdeBD( Statement statement, String nameTabla ) {
		String sent = "select * from " + nameTabla + ";";
		ArrayList<SQLRow> ret = null;
		try {
			ResultSet rs = statement.executeQuery( sent );
			colNames = new ArrayList<>();
			colTypes = new ArrayList<>();
			for (int col=1; col<=rs.getMetaData().getColumnCount(); col++) {
				colNames.add( rs.getMetaData().getColumnName(col) );
				String tipo = rs.getMetaData().getColumnTypeName(col).toUpperCase();
				if (tipo.contains( "VARCHAR" ) || tipo.contains( "CHAR" ) || tipo.contains( "BLOB" ) || tipo.contains( "TEXT" )) {
					colTypes.add( String.class );
				} else if (tipo.contains("BIGINT")) {
					colTypes.add( Long.class );
				} else if (tipo.contains("INT")) {
					colTypes.add( Integer.class );
				} else if (tipo.contains("DECIMAL") || tipo.contains("NUMERIC") || tipo.contains("FLOAT") || tipo.contains("DOUBLE")) {
					colTypes.add( Double.class );
				} else {  // Cualquier otro tipo (BIT, DATE, DATETIME, TIMESTAMP, TIME, YEAR, etc.) se interpreta como string
					colTypes.add( String.class );
				}
			}
			ret = new ArrayList<SQLRow>();
			while (rs.next()) {
				SQLRow row = new SQLRow( colNames, colTypes );
				for (int col=1; col<=rs.getMetaData().getColumnCount(); col++) {
					row.addField( rs.getString( col ) );
				}
				ret.add( row );
			}
			rs.close();
		} catch (SQLException e) {
			ret = null;
			e.printStackTrace();
		}
		return ret;
	}

	/** Devuelve los nombres de las columnas (llamar a este método después de {@link #creaTablaDesdeBD(Statement, String)}
	 * @return
	 */
	public static ArrayList<String> getColNames() { return colNames; }
	
	/** Devuelve los tipos de las columnas (llamar a este método después de {@link #creaTablaDesdeBD(Statement, String)}
	 * @return
	 */
	public static ArrayList<Class<?>> getColTypes() { return colTypes; }
	
	/** Añade todos los objetos SQLRow indicados a una tabla. Los campos se gestionan como strings
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al centro)
	 * @param nomTabla	Nombre de la tabla donde añadir
	 * @param lista	Lista de objetos a añadir (cada uno en una fila
	 * @param colNames	Lista de los nombre de cada una de las columnas de la tabla (deben coincidir una a una con los valores de cada objeto SQLRow)
	 * @param evitar	Si no es null, se pasa el control por cada fila para comprobar si hay que guardarlo o no y si es true no se guarda
	 * @return	true si la inserción es correcta, false en caso contrario
	 */
	public static boolean insertAllEnTabla( Statement st, String nomTabla, ArrayList<? extends SQLRow> lista, ArrayList<String> colNames, Predicate<SQLRow> evitar ) {
		String sentSQL = "";
		try {
			boolean ok = true;
			int numFilas = 0;
			String cabeceraInsert = "(";
			for (int i=0; i<colNames.size(); i++) cabeceraInsert += (colNames.get(i) + ((i<colNames.size()-1)?", ":")"));
			for (SQLRow fila : lista) {
				boolean aEvitar = false;
				if (evitar!=null) aEvitar = evitar.test( fila );
				if (!aEvitar) {
					sentSQL = "insert into " + nomTabla + " " + cabeceraInsert + " values(";
					for (int i=0; i<fila.getCabs().size(); i++) {
						String val = fila.getFila().get(i); if (val==null) val = "";
						sentSQL += ("'" + secu(val) + "'" + ((i<fila.getFila().size()-1)?", ":");"));
					}
					int val = st.executeUpdate( sentSQL );
					numFilas += val;
					if (val!=1) {  // Se tiene que añadir 1 - error si no
						log( Level.SEVERE, "Error en insert de BD\t" + sentSQL, null );
						ok = false;
					}
				}
			}
			log( Level.INFO, "BD añadida " + numFilas + " filas\t" + sentSQL, null );
			return ok;
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			return false;
		}
	}

	
	/** Borrar todos los enlaces de test de la tabla indicada de BD, usando la sentencia DELETE de SQL
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al centro)
	 * @param tableName	Nombre de la tabla a borrar
	 * @return	true si se borran filas (más de ninguna), false en caso contrario
	 */
	public static boolean deleteAllTabla( Statement st, String tableName ) {
		String sentSQL = "";
		try {
			sentSQL = "delete from " + tableName + ";";
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD borrada " + val + " fila\t" + sentSQL, null );
			return (val>0);
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			e.printStackTrace();
			return false;
		}
	}

	/** Borra todas las filas donde se cumplan las condiciones dadas
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al centro)
	 * @param tableName	Nombre de la tabla donde borrar
	 * @param listaCampos	Lista de nombres de campos que determinan el borrado
	 * @param listaValores	Lista de valores de campos para que se borre la fila (un valor por cada campo de listaCampos, en el mismo orden)
	 * @return	true si se borran filas (más de ninguna), false en caso contrario
	 */
	public static boolean deleteRow( Statement st, String tableName, ArrayList<String> listaCampos, ArrayList<String> listaValores ) {
		if (listaCampos==null || listaValores==null || listaCampos.isEmpty() || listaValores.isEmpty()) return false;
		String sentSQL = "";
		try {
			String condicion = listaCampos.get(0) + "='" + listaValores.get(0) + "'";
			for (int i=1; i<listaCampos.size(); i++)
				condicion += " AND " + listaCampos.get(i) + "='" + listaValores.get(i) + "'";
			sentSQL = "delete from " + tableName + " where " + condicion + ";";
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD borrada " + val + " fila\t" + sentSQL, null );
			return (val>0);
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			e.printStackTrace();
			return false;
		}
	}
	
	/** Actualiza las filas donde se cumplan las condiciones dadas
	 * @param st	Sentencia ya abierta de Base de Datos (con la estructura de tabla correspondiente al centro)
	 * @param tableName	Nombre de la tabla donde actualizar
	 * @param listaCampos	Lista de nombres de campos que determinan la actualización
	 * @param listaValores	Lista de valores de campos para que se actualice la fila (un valor por cada campo de listaCampos, en el mismo orden)
	 * @param nomColumna	Nombre de columna a cambiar
	 * @param nuevoValor	Valor a poner en esa columna
	 * @return	true si se actualizan filas (más de ninguna), false en caso contrario
	 */
	public static boolean updateRow( Statement st, String tableName, ArrayList<String> listaCampos, ArrayList<String> listaValores, String nomColumna, String nuevoValor ) {
		if (listaCampos==null || listaValores==null || listaCampos.isEmpty() || listaValores.isEmpty() || nomColumna==null || nomColumna.isEmpty()) return false;
		String sentSQL = "";
		try {
			String condicion = listaCampos.get(0) + "='" + listaValores.get(0) + "'";
			for (int i=1; i<listaCampos.size(); i++)
				condicion += " AND " + listaCampos.get(i) + "='" + listaValores.get(i) + "'";
			sentSQL = "update " + tableName + " set " + nomColumna + "='" + nuevoValor + "' where " + condicion + ";";
			int val = st.executeUpdate( sentSQL );
			log( Level.INFO, "BD actualizada " + val + " fila\t" + sentSQL, null );
			return (val>0);
		} catch (SQLException e) {
			log( Level.SEVERE, "Error en BD\t" + sentSQL, e );
			e.printStackTrace();
			return false;
		}
	}
	


	/////////////////////////////////////////////////////////////////////
	//                      Métodos privados                           //
	/////////////////////////////////////////////////////////////////////

	// Devuelve el string "securizado" para volcarlo en SQL
	private static String secu( String string ) {
		return string.replaceAll( "'",  "''" );
	}
	

	/////////////////////////////////////////////////////////////////////
	//                      Logging                                    //
	/////////////////////////////////////////////////////////////////////
	
	private static Logger logger = null;
	
	// Método local para loggear
	private static void log( Level level, String msg, Throwable excepcion ) {
		if (!LOGGING && level != Level.WARNING && level != Level.SEVERE) return;
		if (logger==null) {  // Logger por defecto local:
			logger = Logger.getLogger( BDTabla.class.getName() );  // Nombre del logger - el de la clase
			logger.setLevel( Level.ALL );  // Loguea todos los niveles
		}
		if (excepcion==null)
			logger.log( level, msg );
		else
			logger.log( level, msg, excepcion );
	}
	
}
