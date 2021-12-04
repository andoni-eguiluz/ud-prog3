package es.deusto.prog3.cap05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CopiaProfundaArrayList {

	// Una solución: copia profunda
	public static ArrayList<Persona> cloneProfundo( ArrayList<Persona> lp ) {
		ArrayList<Persona> ret = new ArrayList<Persona>();
		for (Persona p : lp) {
			ret.add( (Persona) p.clone() );  
			// Si no hay clone se usaría el constructor: 
			// ret.add( new Persona( p.dni, p.letraDni, p.nombre, p.apellidos ) );  
		}
		return ret;
	}
	
	public static void main(String[] args) {
		ArrayList<Persona> lPers = new ArrayList<>();
		lPers.add( new Persona( 17042552, 'S', "Hermione", "Granger" ) );
		lPers.add( new Persona( 16056077, 'H', "Harry", "Potter" ) );
		lPers.add( new Persona(   666666, 'R', "Tom", "Riddle" ) );
		lPers.add( new Persona( 42455913, 'A', "Alfredo", "Pótter" ) );
		Collections.sort( lPers, new Comparator<Persona>() {  // En java 8 tb se podría directamente en el AL lPers.sort( ... )
				@Override
				public int compare(Persona p1, Persona p2) {
					return p1.dni - p2.dni;  // - si p1<p2, + si p1>p2, = si iguales
				}
			});
		System.out.println( "Personas ordenadas por DNI: " );
		System.out.println( " " + lPers );
		ArrayList<Persona> lPers2 = (ArrayList<Persona>) lPers.clone();
		Collections.sort( lPers2, new Comparator<Persona>() {
			@Override
			public int compare(Persona p1, Persona p2) {
				return (p1.apellidos+", "+p1.nombre).compareTo( 
							p2.apellidos+", "+p2.nombre);
			}
		});
		System.out.println( "Personas ordenadas por Apellido: " );
		System.out.println( " " + lPers2 );
		System.out.println( "Quitamos las tildes y ponemos mayúsculas para ordenar");
		for (Persona p : lPers2) {
			p.apellidos = p.apellidos.replaceAll( "ó", "o" ).toUpperCase();
			p.nombre = p.nombre.replaceAll( "ó", "o" ).toUpperCase();
			//  TODO: Se haría lo mismo con ñs y el resto de tildes
		}
		Collections.sort( lPers2, new Comparator<Persona>() {
			@Override
			public int compare(Persona p1, Persona p2) {
				return (p1.apellidos+", "+p1.nombre).compareTo( 
							p2.apellidos+", "+p2.nombre);
			}
		});
		// Con el Comparator se podría ordenar por cualquier criterio. P ej:
		// Comparator<Persona> otroComparador = new ComparatorApellNombre();
		// lPers2.sort( otroComparador );
		System.out.println( "A ver ahora... " );
		System.out.println( " " + lPers2 );
		System.out.println();
		System.out.println( "¿Y cómo va la lista inicial?" );
		System.out.println( " " + lPers );
		System.out.println( "Pero... ¿no habíamos hecho un clone()?" );
		
		// Solución 1: Copia profunda
		//    Implementada en el método cloneProfundo
		
		// Solución 2: Hacer el cambio en la ordenación -sustitución de strings- sobre la marcha,
		// sin que afecte a los datos
		//    (coste computacional: hay que hacerlo 'n*logn' veces * m ordenaciones)
		//    Impleméntala para ver cómo funcionaría
		
		// Solución 3: Duplicar los atributos que requieran ordenación
		//    P ej un atributo adicional para nombre y apellidos que siempre tenga mayúsculas y con tildes eliminadas
		//    (fácil de hacer con métodos set* --- cada set* se encarga de actualizar el campo asociado)
		//    (coste computacional: duplicamos el espacio necesitado por esos atributos)
		//    Impleméntala para ver cómo funcionaría
		
	}

}

class Persona implements Comparable<Persona> {
	int dni;
	char letraDni;
	String nombre;
	String apellidos;
	public Persona(int dni, char letraDni, String nombre, String apellidos) {
		super();
		this.dni = dni;
		this.letraDni = letraDni;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}
	@Override
	public Object clone() {
		return new Persona( dni, letraDni, nombre, apellidos );
	}
	@Override
	public String toString() {
		return ""+dni+letraDni+" - "+nombre+" "+apellidos;
	}
	@Override
	public int compareTo(Persona o) {
		return this.dni - o.dni;
	}
}

class ComparatorApellNombre implements Comparator<Persona> {
	@Override
	public int compare(Persona o1, Persona o2) {
		int comp = o1.apellidos.compareTo( o2.apellidos );
		if (comp==0) {
			comp = o1.nombre.compareTo( o2.nombre );
			if (comp==0) {
				comp = o1.dni - o2.dni;
			}
		}
		return comp;
	}
}
