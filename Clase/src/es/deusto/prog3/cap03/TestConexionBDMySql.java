package es.deusto.prog3.cap03;

import java.sql.*;

// TODO Enlazar la librería externa de mysql, por ejemplo
// mysql-connector-java-8.0.11.jar

/** Prueba de base de datos con mysql en lugar de sqlite
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class TestConexionBDMySql {
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";  // Driver de jdbc para mysql

    public static void main(String[] args) {
        String host = "h1.host.filess.io";       // TODO Aquí el host que aloja la bd mysql
        String puerto = "3307";
        String baseDatos = "test_xxxxxxxxx";     // TODO Aquí el nombre de base de datos
        String username = "user_test_xxxxxxx";   // TODO Aquí el nombre de usuario
        String password = "xxxxxxxxxxxxx";       // Aquí la password

        try {
            Class.forName(DRIVER);
            String url = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println( "Conectado a BD mysql" );
            Statement statement = conn.createStatement();
            String sent = "";

            // Crea tabla si no existe
			try {
				sent = "create table prueba (id int, nombre varchar(40))";
				statement.executeUpdate( sent );
			} catch (SQLException e) {
				System.out.println( "La tabla prueba ya estaba creada" );
			}
			
			// Añade dos filas de ejemplo
			int valor = 1;
			String nombre = "admin";
			sent = "insert into prueba values(" + valor + ", '" + nombre + "')";
			statement.executeUpdate( sent );
			valor = 2;
			nombre = "user";
			sent = "insert into prueba values(" + valor + ", '" + nombre + "')";
			statement.executeUpdate( sent );
			
			// Consulta las filas existentes
			sent = "select * from prueba";
			ResultSet rs = statement.executeQuery( sent );
			int fila = 0;
			while (rs.next()) {
				int val = rs.getInt( "id" );
				String nom = rs.getString( "nombre" );
				fila++;
				System.out.println( "Fila " + fila + " = " + val + " - " + nom );
			}
			
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}