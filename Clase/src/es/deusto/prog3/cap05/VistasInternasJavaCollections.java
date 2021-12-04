package es.deusto.prog3.cap05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;
	
public class VistasInternasJavaCollections {

	public static void main(String[] args) {
		ArrayList<Integer> arrayList = new ArrayList<>();
		for (int i=0; i<5; i++) arrayList.add( i );
		VentanaBancoDePruebas vent = new VentanaBancoDePruebas( arrayList, "ArrayList" );
		vent.setVisible( true );
		LinkedList<Integer> linkedList = new LinkedList<>();
		for (int i=0; i<5; i++) linkedList.add( i );
		vent = new VentanaBancoDePruebas( linkedList, "LinkedList" );
		vent.setVisible( true );
		HashMap<String,Persona> hm = new HashMap<String,Persona>();
		hm.put( "11111111A", new Persona( "María", "Gómez", "11111111A" ) );
		hm.put( "22222222B", new Persona( "María", "Gómez", "11111111A" ) );
		hm.put( "33333333C", new Persona( "María", "Gómez", "11111111A" ) );
		vent = new VentanaBancoDePruebas( hm, "HashMap" );
		vent.setVisible( true );
		// Un hashmap con muchas colisiones en Java 8 pasa a usar balanced trees en lugar de linked list para las colisiones
		HashMap<Integer,Persona> hm2 = new HashMap<Integer,Persona>();
		for (int i=0; i<5000; i=i+128)
			hm2.put( i, new Persona( "Nombre"+i, "Apellido"+i, i + "X" ) );
		vent = new VentanaBancoDePruebas( hm2, "HashMap con muchas colisiones" );
		vent.setVisible( true );
		TreeMap<String,Persona> tm = new TreeMap<String,Persona>();
		for (int i=1; i<=9; i++)
			tm.put( "00000000" + i + (char)(i+64), 
				new Persona( "Nombre"+i, "Ape"+i, "00000000" + i + (char)(i+64) ) );
		vent = new VentanaBancoDePruebas( tm, "TreeMap" );
		vent.setVisible( true );
	}
	
	/** Clase de prueba para la visualización de mapas
	 * @author andoni.eguiluz @ ingenieria.deusto.es
	 */
	private static class Persona {
		String nombre;
		String apellidos;
		String dni;
		public Persona(String nombre, String apellidos, String dni) {
			super();
			this.nombre = nombre;
			this.apellidos = apellidos;
			this.dni = dni;
		}
		@Override
		public String toString() {
			return dni + " = " + nombre + " " + apellidos;
		}
		
	}
}
