package es.deusto.prog3.cap05;

import java.util.*;

import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;

public class PruebaSets {
	static HashSet<Punto> hS;
	static HashSet<String> hSS;
	static TreeSet<String> tSS;
	public static void main(String[] args) {
		System.out.println( "A1".hashCode() );
		System.out.println( "A2".hashCode() );
		hS = new HashSet<>();
		hS.add( new Punto(1,2) );
		hS.add( new Punto(2,3) );
		hS.add( new Punto(2,3) );
		hS.add( new Punto(3,2) );
		System.out.println( hS );
		hSS = new HashSet<>();
		hSS.add( "Andoni" );
		hSS.add( "Marta" );
		hSS.add( "Emilio" );
		hSS.add( "Marta" );
		hSS.add( "a1" );
		hSS.add( "a2" );
		System.out.println( hSS );
		tSS = new TreeSet<>();
		tSS.add( "Andoni" );
		tSS.add( "Marta" );
		tSS.add( "Emilio" );
		tSS.add( "Marta" );
		tSS.add( "a1" );
		tSS.add( "a2" );
		System.out.println( tSS );
		// Exploración visual de los sets
		VentanaBancoDePruebas exploraSet = new VentanaBancoDePruebas( hS, "HashSet de puntos" );
		exploraSet.setVisible( true );
		exploraSet = new VentanaBancoDePruebas( hSS, "HashSet de strings" );
		exploraSet.setVisible( true );
		exploraSet = new VentanaBancoDePruebas( tSS, "TreeSet de strings" );
		exploraSet.setVisible( true );
	}
}

class Punto {
	int x;
	int y;
	public Punto(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
/* Probar a comentar estos dos métodos y ver lo que pasa. ¿Por qué? */
	
	@Override
	public int hashCode() {
		return x + y;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Punto)) return false;
		Punto p2 = (Punto) obj;
		return x==p2.x && y==p2.y;
	}
	
}
