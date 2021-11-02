package es.deusto.prog3.cap03.resueltos.miniAmazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class BaseDatos {
	private static Connection conexion;
	
	/** Abre conexión con la base de datos
	 * @param nombreBD	Nombre del fichero de base de datos
	 * @return	true si la conexión ha sido correcta, false en caso contrario
	 */
	public static boolean abrirConexion( String nombreBD ) {
		try {
			System.out.println( "Conexión abierta" );
			Class.forName("org.sqlite.JDBC");  // Carga la clase de BD para sqlite
			conexion = DriverManager.getConnection("jdbc:sqlite:" + nombreBD );
			
			// No lo pide el ejercicio, pero si se quiere crear la base de datos si no existe desde el propio programa habría que hacer esto:
			// creación bd
			Statement statement = conexion.createStatement();
			String sent = "CREATE TABLE IF NOT EXISTS producto (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre varchar(100), precio real);";
			System.out.println( sent );
			statement.executeUpdate( sent );
			sent = "CREATE TABLE IF NOT EXISTS compra (id INTEGER PRIMARY KEY AUTOINCREMENT, idProducto int, fecha bigint, cantidad int);";
			System.out.println( sent );
			statement.executeUpdate( sent );
			try {
				sent = "insert into producto (id, nombre, precio) values (1,'Jamón para profesor',345);";
				System.out.println( sent );
				statement.executeUpdate( sent );
				sent = "insert into producto (id, nombre, precio) values (2,'Crucifijo rezos pre-examen',42);";
				System.out.println( sent );
				statement.executeUpdate( sent );
				sent = "insert into producto (id, nombre, precio) values (3,'Asesor programación Java (hora)',25);";
				System.out.println( sent );
				statement.executeUpdate( sent );
			} catch(Exception e) {}  // Es normal que haya error en los inserts si ya existen las claves
			// fin creación bd
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** Cierra la conexión abierta de base de datos ({@link #abrirConexion(String)})
	 */
	public static void cerrarConexion() {
		try {
			conexion.close();
			System.out.println( "Conexión cerrada" );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** Lee los productos de la conexión de base de datos abierta
	 * @return	Lista completa de productos, null si hay algún error
	 */
	public static ArrayList<Producto> getProductos() {
		try (Statement statement = conexion.createStatement()) {
			ArrayList<Producto> ret = new ArrayList<>();
			String sent = "select * from producto;";
			System.out.println( sent );
			ResultSet rs = statement.executeQuery( sent );
			while( rs.next() ) { // Leer el resultset
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				int precio = rs.getInt("precio");
				ret.add( new Producto ( id, nombre, precio, new ArrayList<Compra>() ) );
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Inserta una compra en la base de datos abierta
	 * Actualiza el id de la compra insertada
	 * @param compra	Compra a insertar
	 * @return	true si la inserción es correcta, false en caso contrario
	 */
	public static boolean insertarCompra( Compra compra ) {
		try (Statement statement = conexion.createStatement()) {
			String sent = "insert into compra (idProducto, fecha, cantidad) values (" + compra.getProducto().getId() + "," + compra.getFecha() + "," + compra.getCantidad() + ");";
			System.out.println( sent );
			int insertados = statement.executeUpdate( sent );
			if (insertados!=1) return false;  // Error en inserción

			// 11 - Búsqueda de la fila insertada
			// Para ello hay que recuperar la clave autogenerada. Hay varias maneras, vemos dos diferentes:
			// TODO Dejar solo una de las dos
			
			// (1) Utilizando un método del propio objeto statement:
			ResultSet rrss = statement.getGeneratedKeys();  // Genera un resultset ficticio con las claves generadas del último comando
			rrss.next();  // Avanza a la única fila 
			int pk = rrss.getInt( 1 );  // Coge la única columna (la primary key autogenerada)
			compra.setId( pk );
			
			// (2) Haciendo nueva consulta con el propio sql (buscando la fila que acabamos de crear - ojo, tenemos que tener manera de hacer una búsqueda ÚNICA):
			sent = "select id from compra where idProducto=" + compra.getProducto().getId() + " and fecha=" + compra.getFecha() + ";";
			System.out.println( sent );
			ResultSet rs = statement.executeQuery( sent );
			if (rs.next()) { // Leer el resultset y poner el id
				int id = rs.getInt( "id" );
				compra.setId( id );
			}
			
			// (3) Se podría hacer de otras variantes de select del registro insertado, p ej.
			// SELECT * FROM compra ORDER BY id DESC LIMIT 1
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** Lee las compras de un producto dado de la conexión de base de datos abierta
	 * @return	Lista completa de compras de ese producto, vacía si no hay ninguna, null si hay algún error
	 */
	public static ArrayList<Compra> getCompras( Producto producto ) {
		try (Statement statement = conexion.createStatement()) {
			ArrayList<Compra> ret = new ArrayList<>();
			String sent = "select * from compra where idProducto=" + producto.getId() + ";";
			System.out.println( sent );
			ResultSet rs = statement.executeQuery( sent );
			while( rs.next() ) { // Leer el resultset
				int id = rs.getInt("id");
				long fecha = rs.getLong("fecha");
				int cantidad = rs.getInt("cantidad");
				ret.add( new Compra( id, fecha, cantidad, producto ) );
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Borra una compra en la base de datos abierta
	 * @param compra	Compra a borrar
	 * @return	true si el borrado es correcto, false en caso contrario
	 */
	public static boolean borrarCompra( Compra compra ) {
		try (Statement statement = conexion.createStatement()) {
			String sent = "delete from compra where id=" + compra.getId() + ";";
			System.out.println( sent );
			int borrados = statement.executeUpdate( sent );
			return (borrados==1);
		} catch (Exception e) {
			return false;
		}
	}
	
	
}
