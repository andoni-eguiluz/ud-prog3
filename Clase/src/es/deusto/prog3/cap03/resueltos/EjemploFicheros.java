package es.deusto.prog3.cap03.resueltos;

import java.io.*;
import java.util.ArrayList;
import java.sql.*;

// Ver test JUnit para ejemplo de funcionamiento:
// TestEjemploFicheros (en este mismo paquete)

/** Ejemplo de ficheros con la clase Usuario.
 * Guardado y cargado de datos de esta clase con varias opciones de ficheros distintas
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class EjemploFicheros {
	
	/** Lee los usuarios de un fichero de datos separado por comas y los devuelve
	 * @param nomFic	Nombre de fichero
	 * @return	Lista de usuarios del fichero, sólo formada por los usuarios leídos correctamente
	 */
	public static ArrayList<Usuario> leerDeFicheroConComas( String nomFic ) {
		ArrayList<Usuario> ret = new ArrayList<Usuario>();
		BufferedReader brFich = null;
		try {
			brFich = new BufferedReader( new
					InputStreamReader( new FileInputStream(nomFic) ) );
			String linea = brFich.readLine();
			while (linea != null) {
				// Proceso de línea
				Usuario u = Usuario.crearDeLinea(linea);
				if (u!=null) ret.add( u );
				linea = brFich.readLine();
			}
		} catch (Exception e) {  // FileNotFound, IO
			e.printStackTrace();
		} finally {
			if (brFich!=null)
				try {
					brFich.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return ret;
	}
	
	/** Escribe los usuarios de una lista de usuarios a un fichero de texto,
	 * en formato de datos de usuario separado por comas (una línea por usuario)
	 * @param nomFic	Nombre de fichero
	 * @param l	Lista de usuarios a escribir al fichero
	 */
	public static void escribirAFicheroConComas( String nomFic, ArrayList<Usuario> l ) {
		PrintStream fich = null;
		try {
			fich = new PrintStream(new FileOutputStream(nomFic));
			for (Usuario u : l) {
				fich.println( u.aLinea() );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fich!=null) fich.close();
		}
	}

	/** Lee los usuarios de un fichero de datos binario serializado de usuarios
	 * @param nomFic	Nombre de fichero
	 * @return	Lista de usuarios del fichero, sólo formada por los usuarios leídos correctamente
	 */
	public static ArrayList<Usuario> leerDeFicheroSerializado( String nomFic ) {
		ArrayList<Usuario> ret = new ArrayList<Usuario>();
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream( new FileInputStream(nomFic) );
			while (true) {
				// Lectura hasta final de fichero (excepción)
				// Tb a veces se graba un null al final y se usa ese null para acabar
				Usuario u = (Usuario) ois.readObject();
				ret.add( u );
			}
		} catch (EOFException e) {  // FileNotFound, IO, EOF, classcast
			// Ok - final de bucle
		} catch (Exception e) {  // FileNotFound, IO, EOF, classcast
			e.printStackTrace();
		} finally {
			if (ois!=null)
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return ret;
	}
	
	/** Escribe los usuarios de una lista de usuarios a un fichero binario,
	 * serializado por cada usuario
	 * @param nomFic	Nombre de fichero
	 * @param l	Lista de usuarios a escribir al fichero
	 */
	public static void escribirAFicheroSerializado( String nomFic, ArrayList<Usuario> l ) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(nomFic));
			for (Usuario u : l) {
				oos.writeObject( u );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos!=null)
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/** Lee los usuarios de un fichero de datos de usuarios con tags, y los devuelve
	 * @param nomFic	Nombre de fichero
	 * @return	Lista de usuarios del fichero, sólo formada por los usuarios leídos correctamente
	 */
	public static ArrayList<Usuario> leerDeFicheroConTags( String nomFic ) {
		ArrayList<Usuario> ret = new ArrayList<Usuario>();
		BufferedReader brFich = null;
		try {
			brFich = new BufferedReader( new
					InputStreamReader( new FileInputStream(nomFic) ) );
			String linea = brFich.readLine();
			while (linea != null) {
				// Proceso de línea
				if ("[USUARIO]".equals(linea)) {
					Usuario u = Usuario.crearDeLineasConTags( brFich );
					if (u!=null) ret.add( u );
				}
				linea = brFich.readLine();
			}
		} catch (Exception e) {  // FileNotFound, IO
			e.printStackTrace();
		} finally {
			if (brFich!=null)
				try {
					brFich.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return ret;
	}

	/** Escribe los usuarios de una lista de usuarios a un fichero de texto,
	 * en formato de usuario con tags:<p>
	 * [USUARIO]<p>
	 * [nick] nick\n<p>
	 * [password] password\n<p>
	 * [nombre] nombre\n<p>
	 * [apellidos] apellidos\n<p>
	 * [telefono] telefono\n<p>
	 * [fechaUltimoLogin] fechaUltimoLogin(msgs.)\n<p>
	 * [tipo] tipo\n<p>
	 * [emails] email1,email2...\n<p>
	 * [FINUSUARIO]<p>
	 * ... resto de usuarios con el mismo formato
	 * @param nomFic	Nombre de fichero
	 * @param l	Lista de usuarios a escribir al fichero
	 */
	public static void escribirAFicheroConTags( String nomFic, ArrayList<Usuario> l ) {
		PrintStream fich = null;
		try {
			fich = new PrintStream(new FileOutputStream(nomFic));
			for (Usuario u : l) {
				fich.println( "[USUARIO]" );
				fich.println( u.aLineasConTags() );
				fich.println( "[FINUSUARIO]" );
			}
			fich.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fich!=null) fich.close();
		}
	}


	/** Escribe los usuarios de una lista de usuarios a una base de datos,
	 * a la tabla usuario (que se crea)
	 * @param nomFic	Nombre de base de datos 
	 * @param l	Lista de usuarios a escribir a la tabla usuario
	 * @return	Número de registros añadidos
	 */
	public static int escribirABD( String nomBD, ArrayList<Usuario> l ) {
		Statement st = null;
		Connection con = Usuario.initBD( nomBD );
		int cont = 0;
		if (con!=null) {
			st = Usuario.crearTablaBD( con );
			if (st!=null) {
				for (Usuario u : l) {
					boolean anyadir = u.anyadirFilaATabla(st);
					if (anyadir) cont++;
				}
			}
		}
		Usuario.cerrarBD( con, st );
		return cont;
	}

	/** Lee y devuelve algunos usuarios de una base de datos,
	 * de la tabla usuario (que se crea)
	 * @param nomBD	Nombre de base de datos 
	 * @param sentSelect	Sentencia SQL de selección (ejemplo: "nombre = 'buzz'")
	 * @return	lista de usuarios, null si hay cualquier error
	 */
	public static ArrayList<Usuario> leerDeBD( String nomBD, String sentSelect ) {
		Statement st = null;
		Connection con = Usuario.initBD( nomBD );
		if (con != null) {
			try {
				st = con.createStatement();
				ArrayList<Usuario> ret = Usuario.consultaATabla( st, sentSelect );
				Usuario.cerrarBD( con, st );
				return ret;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
