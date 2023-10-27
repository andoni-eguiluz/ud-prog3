package es.deusto.prog3.cap05;

import java.util.*;

/** Revisión de trabajos habituales con mapas con ejemplos de datos sencillos
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class TrabajosConMapas {
	// Ejemplo: usuarios que tienen registro de sesiones
	private static String[] usuarios   =   { "Emilio", "Isma", "Aitziber", "Lucía", "Ana", "Lucía", "Emilio", "Aitana", "Lucía" };
	private static int[] tiemposSesionSg = {       96,    240,        125,     392,  1240,     855,      243,       25,    1435 };
	private static int[] numOpsSesion   =  {        3,      5,          3,       7,    15,       6,        4,        0,      12 };
	
	private static HashMap<String,ArrayList<InfoSesion>> mapa;
	
	public static void main(String[] args) {
		// 1.- Creación
		mapa = new HashMap<>();
		
		// 2.- Carga
		for (int i=0; i<usuarios.length; i++) {
			String usuario = usuarios[i];
			int tiempoSesion = tiemposSesionSg[i];
			int opsSesion = numOpsSesion[i];
			// a) ¿Está ya la clave en el mapa?
			if (!mapa.containsKey( usuario )) {
				// b) Si no está, añadir la clave y el nuevo valor
				ArrayList<InfoSesion> listaSesiones = new ArrayList<>();
				listaSesiones.add( new InfoSesion( usuario, tiempoSesion, opsSesion ) );
				mapa.put( usuario, listaSesiones );
			} else {
				// c) Si ya está, modificar el valor ya existente como proceda
				ArrayList<InfoSesion> listaSesiones = mapa.get( usuario );
				listaSesiones.add( new InfoSesion( usuario, tiempoSesion, opsSesion ) );
				// mapa.put( usuario, listaSesiones );  <-- Observa que no hace falta porque la lista ya está en el mapa
			}
			// Otra manera alternativa en este ejemplo es una sola rama de condicional y la operación de añadir 
			// hacerla en los dos casos
			// ArrayList<InfoSesion> listaSesiones = mapa.get(usuario);
			// if (listaSesiones==null) { // O da igual (!mapa.containsKey( usuario ))
			// 	listaSesiones = new ArrayList<>();
			// 	mapa.put( usuario, listaSesiones );
			// }
			// listaSesiones.add( new InfoSesion( usuario, tiempoSesion, opsSesion ) );
		}
		
		// 3.- Proceso particular
		String usuarioEjemplo = "Lucía";
		if (mapa.containsKey( usuarioEjemplo )) {
			System.out.println( "Usuario " + usuarioEjemplo + " tiene las sesiones: " + mapa.get(usuarioEjemplo) );
		} else {
			System.out.println( "Usuario " + usuarioEjemplo + " no tiene sesiones." );
		}
		
		// 4.- Proceso-recorrido general
		for (String clave : mapa.keySet()) {
			ArrayList<InfoSesion> valor = mapa.get(clave);
			System.out.println( "Usuario " + clave + " - sesiones " + valor );
		}
	}
	
	
	private static class InfoSesion {
		private String usuario;     // Nombre usuario de la sesión
		private int duracion;       // Duración de la sesión en segundos
		private int numOperaciones; // Nº operaciones por sesión
		public InfoSesion(String usuario, int duracion, int numOperaciones) {
			this.usuario = usuario;
			this.duracion = duracion;
			this.numOperaciones = numOperaciones;
		}
		public String getUsuario() {
			return usuario;
		}
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		public int getDuracion() {
			return duracion;
		}
		public void setDuracion(int duracion) {
			this.duracion = duracion;
		}
		public int getNumOperaciones() {
			return numOperaciones;
		}
		public void setNumOperaciones(int numOperaciones) {
			this.numOperaciones = numOperaciones;
		}
		@Override
		public String toString() {
			return usuario + " [" + duracion + "/" + numOperaciones + "]";
		}
	}
	
}
