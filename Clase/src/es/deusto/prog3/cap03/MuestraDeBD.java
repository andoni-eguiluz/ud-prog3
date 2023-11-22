package es.deusto.prog3.cap03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MuestraDeBD {
	public static void main(String[] args) {
		// 1.- Decidir el driver
		try {
			Class.forName( "org.sqlite.JDBC" );
		} catch (ClassNotFoundException e) {
			System.out.println( "No está conectada la librería" );
			return;
		}
		// 2.- Crear una conexión
		try {
			// Ojo - hacerlo pocas veces
			Connection connection = DriverManager.getConnection( "jdbc:sqlite:ejemplo.db" );
			// 3.- Crear tantas statements (sentencia) como necesitemos
			Statement statement = connection.createStatement();
			
			// Y trabajar...
			// Si ya tuviéramos tablas creadas podríamos ya acceder
			// Si no, lo podemos crear directamente desde código
			statement.executeUpdate( "drop table if exists usuario" );
			String sent = "create table usuario (id integer, nombre string)";
			System.out.println( "Sentencia BD: " + sent );
			statement.executeUpdate( sent );
			
			// Hagamos un poco de CRUD
			// C
			sent = "insert into usuario values(1, 'Aritz')";
			System.out.println( "Sentencia BD: " + sent );
			int result = statement.executeUpdate(sent);
			System.out.println( "Resultado: " + result );
			sent = "insert into usuario values(2, 'Aintzane')";
			System.out.println( "Sentencia BD: " + sent );
			statement.executeUpdate(sent);
			
			visualizar( statement );
			
			// U
			sent = "update usuario set nombre='Koldo' where id=1";
			System.out.println( "Sentencia BD: " + sent );
			result = statement.executeUpdate(sent);
			System.out.println( "Resultado: " + result );
			String nombreAModificar = "Andoni";
			int idAModificar = 1;
			sent = "update usuario set nombre='" + nombreAModificar + "' where id=" + idAModificar;
			System.out.println( "Sentencia BD: " + sent );
			result = statement.executeUpdate(sent);
			System.out.println( "Resultado: " + result );

			visualizar( statement );
			
			// D
			sent = "delete from usuario where nombre='Koldo'";
			System.out.println( "Sentencia BD: " + sent );
			result = statement.executeUpdate(sent);
			System.out.println( "Resultado: " + result );
			sent = "delete from usuario where nombre='Andoni'";
			System.out.println( "Sentencia BD: " + sent );
			result = statement.executeUpdate(sent);
			System.out.println( "Resultado: " + result );
			
			visualizar( statement );
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println( "Error en gestión de base de datos" );
		}
	}
	
	private static ArrayList<Usuario> visualizar( Statement statement ) throws SQLException {
		ArrayList<Usuario> l = new ArrayList<>();
		String sent = "select * from usuario";
		System.out.println( sent );
		ResultSet rs = statement.executeQuery( sent );
		while (rs.next()) {
			int id = rs.getInt( "id" );
			String nom = rs.getString( "nombre" );
			System.out.println( "  id = " + id + " - nombre = " + nom );
			Usuario uNuevo = new Usuario();
			uNuevo.ident = id;
			uNuevo.nom = nom;
			l.add( uNuevo );
		}
		rs.close();
		// o if (rs.next()) si solo nos interesara la primera fila devuelta por el select
		return l;
	}
	
	private static class Usuario {
		int ident;
		String nom;
	}
}


