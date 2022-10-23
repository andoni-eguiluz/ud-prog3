package es.deusto.prog3.cap03.cs2;

import java.io.Serializable;

/** Clase de datos principal del ejemplo: permite representar productos
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int codigo;
	private String nombre;
	private double precio;
	private String rutaFoto;

	/** Constructor de producto
	 * @param codigo	Código (único) de producto
	 * @param nombre	Nombre de producto
	 * @param precio	Precio en euros
	 * @param rutaFoto	Ruta de la foto del producto (compatible con el servicio de persistencia)
	 */
	public Producto(int codigo, String nombre, double precio, String rutaFoto) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.rutaFoto = rutaFoto;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getRutaFoto() {
		return rutaFoto;
	}
	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}
	public int getCodigo() {
		return codigo;
	}

	@Override
	public String toString() {
		return "[" + codigo + "] " + nombre;
	}
}
