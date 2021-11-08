package es.deusto.prog3.cap06.pr0506resuelta;

/** Clase de datos de una persona, utilizada para pruebas de eficiencia de estructuras de datos en este paquete.
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class Persona 
// PASO 3
implements Comparable<Persona> {
	private int dni;
	private char letraDni;
	private String nombre;
	private String apellidos;

		private static final String NIF_STRING_ASOCIATION = "TRWAGMYFPDXBNJZSQVHLCKE";
		/**
		 * Devuelve un NIF completo a partir de un DNI. Es decir, añade la letra del NIF<p>
		 * Algoritmo obtenido de http://es.wikibooks.org/wiki/Algoritmo_para_obtener_la_letra_del_NIF#Java
		 * @param dni dni al que se quiere añadir la letra del NIF
		 * @return NIF completo.
		 */
		public static char getLetraDNI(int dni) {
			return NIF_STRING_ASOCIATION.charAt(dni % 23);
		}
	  
	public Persona( int dni, String nombre, String apellidos ) {
		this.dni = dni;
		this.letraDni = getLetraDNI(dni);
		this.nombre = nombre;
		this.apellidos = apellidos;
	}
	
	public int getDni() {
		return dni;
	}
	public void setDni(int dni) {
		this.dni = dni;
	}
	public char getLetraDni() {
		return letraDni;
	}
	public void setLetraDni(char letraDni) {
		this.letraDni = letraDni;
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

	@Override
	public String toString() {
		return dni + "-" + letraDni + ": " + nombre + " " + apellidos;
	}

	@Override
	public boolean equals( Object o ) {
		if (o instanceof Persona) {
			return dni==((Persona)o).dni;
		} else
			return false;
	}

	// PASO 3
	@Override
	public int hashCode() {
		return dni;  // El hashcode debe ser coherente con el equals
	}

	@Override
	public int compareTo(Persona o) {
		return dni-o.dni;
	}

	
}
