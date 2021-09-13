package es.deusto.prog3.cap00.resueltos.ej07;


public class UsuarioNivel extends Usuario{
	int nivel;
	public UsuarioNivel(String nombre, String contraseña, int nivel) {
		super(nombre, contraseña);
		setNivel(nivel);
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	@Override
	public String toString() {
		return "UsuarioNivel [nivel=" + nivel + ", nombre=" + nombre + ", contraseña=" + password + "]";
	}	
}
