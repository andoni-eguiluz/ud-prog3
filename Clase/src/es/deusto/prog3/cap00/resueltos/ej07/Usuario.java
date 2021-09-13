package es.deusto.prog3.cap00.resueltos.ej07;

public class Usuario {
	String nombre;
	String password;
	public Usuario(String nombre, String contraseña) {
		this.nombre = nombre;
		this.password = contraseña;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String contraseña) {
		this.password = contraseña;
	}
	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", contraseña=" + password + "]";
	}
	
}
