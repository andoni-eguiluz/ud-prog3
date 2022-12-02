package es.deusto.prog3.cap05.resueltos;

import java.util.*;

public class EjemploACorregirSets {
	static HashSet<Fantasma> hS;
	static TreeSet<Fantasma> hSS;
	public static void main(String[] args) {
		hS = new HashSet<>();
		Fantasma f1 = new Fantasma( "casper", 1, 2); hS.add( f1 );
		Fantasma f2 = new Fantasma( "opera phantom", 4, 2); hS.add( f2 );
		Fantasma f3 = new Fantasma( "casper", 1, 2); hS.add( f3 );
		Fantasma f4 = new Fantasma( "myrtle", 5, 10); hS.add( f4 );
		System.out.println( f1.hashCode() + " - " + f3.hashCode() );
		System.out.println( hS );  // ¿Por qué añade dos veces a casper?  
		   // Resp: Porque no estaban definido hashcode / equals y entonces usaba los de Object
		   // Y dos objects solo son "equals" si son EL MISMO (misma referencia)
		   // y su hashcode es su referencia.
		hSS = new TreeSet<>();
		hSS.add( new Fantasma( "casper", 1, 2) );
		hSS.add( new Fantasma( "opera phantom", 4, 2) );
		hSS.add( new Fantasma( "casper", 1, 2) );
		hSS.add( new Fantasma( "myrtle", 5, 10) );
		System.out.println( hSS ); // ¿Por qué da error en ejecución?
		   // Porque no implementaba Comparable
	}
	
	private static class Fantasma implements Comparable<Fantasma> {
		String nombre;
		int x;
		int y;
		public Fantasma(String nombre, int x, int y) {
			super();
			this.nombre = nombre;
			this.x = x;
			this.y = y;
		}	
		@Override
		public String toString() {
			return nombre + " (" + x + "," + y + ")";
		}
		@Override
		public int hashCode() {
			return nombre.hashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Fantasma) {
				return nombre.equals( ((Fantasma)obj).nombre );
			} else {
				return false;
			}
		}
		@Override
		public int compareTo(Fantasma o) {
			return nombre.compareTo( o.nombre );
		}
	}
	
}
