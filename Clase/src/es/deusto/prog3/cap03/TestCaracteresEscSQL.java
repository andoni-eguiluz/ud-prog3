package es.deusto.prog3.cap03;

import java.sql.*;

/**  Prueba de inserción SQL con caracteres especiales (sustituyendo ' por '').
 *  Siempre es más seguro el preparedstatement y necesita menos proceso
 */
public class TestCaracteresEscSQL {
	
	private static String safeSQL( String string ) {
		return string.replaceAll( "'",  "''" );
	}
	
	public static void main(String[] args) {
		String string = "Prueba\nSegunda línea\nTercera con\ttab\nCuarta con 'comillas'\nQuinta con \"dobles comillas\"";
		try {
			Class.forName( "org.sqlite.JDBC" );
			Connection con = DriverManager.getConnection( "jdbc:sqlite:prueba-escs.bd" );
			Statement stat = con.createStatement();
			try {
				stat.executeUpdate( "create table strings (valor string)");
			} catch (SQLException e) {}
			stat.executeUpdate( "delete from strings" );

			System.out.println( "1.- Esto no se podría hacer porque da error SQL al cerrarse la comilla simple:" );
			String sql = "insert into strings values('" + string + "')";
			System.out.println( sql );
		    // stat.executeUpdate( sql );  // NO HACERLO!
		    System.out.println();

			System.out.println( "2.- Se puede hacer si se 'securiza' el string sustituyendo ' por '':" );
			sql = "insert into strings values('" + safeSQL(string) + "')";
			System.out.println( sql );
		    stat.executeUpdate( sql );
		    System.out.println();

			System.out.println( "3.- Mejor aún con prepareStatement -no hay que sustituir nada y evita el SQL injection-" );
		    sql = "INSERT INTO strings VALUES (?)";
		    PreparedStatement prepStat = con.prepareStatement(sql);
		    prepStat.setString( 1, string );
			System.out.println( "prepared statement desde: " + sql );
		    prepStat.execute();
		    System.out.println();
		    
			ResultSet rs = stat.executeQuery("select * from strings");
			int n = 0;
			while (rs.next())
			{
				n++;
				System.out.println( "Valor leído " + n + ":\n" + rs.getString( "valor" ) );
			    System.out.println();
			}
			rs.close();
		    
			stat.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
