package es.deusto.prog3.cap03;

import java.sql.*;

public class TestConexionBDHeroku {

    public static void main(String[] args) throws Exception {
    	Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        // stmt.executeUpdate("DROP TABLE IF EXISTS ticks");  // Si se quiere iniciar la tabla desde 0
        try {
        	stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
        } catch (SQLException e) {
        	System.out.println( "La tabla ya existe" );
        }
        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
        while (rs.next()) {
            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
        }
    }
    
    private static Connection getConnection() {
    	Connection conn = null;
    	try {
    		Class.forName("org.postgresql.Driver");
    		// TODO Uso de datos de conexión (definidos en fichero privado - cambiarlos por los datos oportunos)
        	String username = privado.Datos.HEROKU_POSTGRES_DB_username;
        	String password = privado.Datos.HEROKU_POSTGRES_DB_password;
        	String urldB = privado.Datos.HEROKU_POSTGRES_DB_url;
        	String dbUrl = "jdbc:postgresql://" + urldB + "?sslmode=require";
    		conn = DriverManager.getConnection( dbUrl, username, password );
    	} catch (Exception e) { e.printStackTrace(); }        
    	return conn;
    }

}
