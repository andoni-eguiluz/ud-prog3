package es.deusto.prog3.cap03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Ejemplo mínimo de uso de Base de Datos desde Java con JDBC
 * Utilizando sqlite - debe incluirse la librería sqlite-jdbc*.jar
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class EjemploBD {

	public static void main(String[] args) throws ClassNotFoundException {

		// Carga el sqlite-JDBC driver usando el cargador de clases
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try {
			// Crear una conexión de BD
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // poner timeout 30 msg

			statement.executeUpdate("drop table if exists person");
			statement.executeUpdate("create table person (id integer, name string)");
			int res = statement.executeUpdate("insert into person values(1, 'leo')");
			System.out.println( res );
			res = statement.executeUpdate("insert into person values(2, 'yui')");
			System.out.println( res );
			ResultSet rs = statement.executeQuery("select * from person");
			while(rs.next()) {
				// Leer el resultset
				System.out.println("name = " + rs.getString("name"));
				System.out.println("id = " + rs.getInt("id"));
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch(SQLException e) {
				// Cierre de conexión fallido
				System.err.println(e);
			}
		}
	}

}
