package es.deusto.prog3.cap03;

import java.sql.*;

public class PruebaBD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.exit(0);
		}
		Connection conn = null;
		String sent = "";
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:test.db");
			Statement statement = conn.createStatement();
			
			// Error esperable y lógico: si la tabla existe SQLException
			try {
				sent = "create table prueba (id integer, nombre string)";
				statement.executeUpdate( sent );
			} catch (SQLException e) {
				System.out.println( "La tabla prueba ya estaba creada" );
				//
			}
			
			int valor = 2;
			String nombre = "Elena";
			sent = "insert into prueba values(" + valor + ", '" + nombre + "')";
			statement.executeUpdate( sent );
			
			sent = "select * from prueba where id = 1";
			ResultSet rs = statement.executeQuery( sent );
			int fila = 0;
			while (rs.next()) {
				int val = rs.getInt( "id" );
				String nom = rs.getString( "nombre" );
				fila++;
				System.out.println( "Fila " + fila + " = " + val + " - " + nom );
			}
			
			rs.close();
			statement.close();
			conn.close();
			
			
		} catch (SQLException e) {
			System.out.println( sent );
			e.printStackTrace();
		}
	}

}
