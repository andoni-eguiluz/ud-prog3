package es.deusto.prog3.cap03.ejemploPartidas;

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
	// Modificador de nick solo permitido dentro del paquete
	protected void setNick( String nick ) {
		this.nick = nick;
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
	
	/** Constructor protected, sólo para uso interno dentro del paquete
	 */
	protected Usuario() {
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
	
}
