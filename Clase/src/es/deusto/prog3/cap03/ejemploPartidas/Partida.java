package es.deusto.prog3.cap03.ejemploPartidas;

public class Partida {
	protected Usuario usuario;
	protected long fechaPartida;
	protected int puntuacion;
	public Partida(Usuario usuario, long fechaPartida, int puntuacion) {
		super();
		this.usuario = usuario;
		this.fechaPartida = fechaPartida;
		this.puntuacion = puntuacion;
	}
	// Constructor local para el paquete
	protected Partida() {
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public long getFechaPartida() {
		return fechaPartida;
	}
	public void setFechaPartida(long fechaPartida) {
		this.fechaPartida = fechaPartida;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
}
