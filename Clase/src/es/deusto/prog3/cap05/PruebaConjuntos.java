package es.deusto.prog3.cap05;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

public class PruebaConjuntos {
	public static void main(String[] args) {
		ArrayList<Integer> lista = new ArrayList<Integer>();
		HashSet<Integer> conjH = new HashSet<Integer>();
		TreeSet<Integer> conjT = new TreeSet<Integer>();
		int ejemplo[] = { 1, 3, 7, 12, 4, 3, 9, 3, 4 };
		for (int e : ejemplo) {
			lista.add( e );
			conjH.add( e );
			conjT.add( e );
		}
		System.out.println( "Lista: " + lista );
		System.out.println( "Set (hash): " + conjH );
		System.out.println( "Set (tree): " + conjT );
		// Ver qué problema hay si se meten objetos sin hash bien calculado en un HashSet
		HashSet<NoHasheada> hs = new HashSet<NoHasheada>();
		hs.add( new NoHasheada( 10 ) );
		hs.add( new NoHasheada( 2 ) );
		hs.add( new NoHasheada( 5 ) );
		hs.add( new NoHasheada( 5 ) );
		System.out.println( "HashSet sin equals congruente con hash: " + hs );
		NoHasheada nh = new NoHasheada( 5 );
		System.out.println( "  Está " + nh + " en el set? " + hs.contains(nh) );
		HashSet<MalHasheada> hs2 = new HashSet<MalHasheada>();
		hs2.add( new MalHasheada( 10 ) );
		hs2.add( new MalHasheada( 2 ) );
		hs2.add( new MalHasheada( 5 ) );
		hs2.add( new MalHasheada( 5 ) );
		System.out.println( "HashSet mal hasheado: " + hs2 );
		MalHasheada nh2 = new MalHasheada( 5 );
		System.out.println( "  Está " + nh2 + " en el set? " + hs2.contains(nh2) );
		// Ver qué problema hay si se meten objetos sin orden en un TreeSet
		TreeSet<NoOrdenada> ts = new TreeSet<NoOrdenada>();
		ts.add( new NoOrdenada( "Ender" ) );
		ts.add( new NoOrdenada( "Alain" ) );
		ts.add( new NoOrdenada( "Petra" ) );
		ts.add( new NoOrdenada( "Alain" ) );
		System.out.println( "TreeSet sin orden: " + ts );
		NoOrdenada no = new NoOrdenada( "Petra" );
		System.out.println( "  Está " + no + " en el set? " + ts.contains(no) );
	}
}

class NoHasheada {
	int valor;
	public NoHasheada( int valor ) {
		this.valor = valor;
	}
	@Override
	public int hashCode() {
		return 0;  // Un mal hashcode
	}
//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof NoHasheada) {
//			return valor==((NoHasheada)obj).valor;
//		} else
//			return false;
//	}
	@Override
	public String toString() {
		return ""+valor;
	}
}

class MalHasheada {
	private static Random r = new Random();
	int valor;
	public MalHasheada( int valor ) {
		this.valor = valor;
	}
	@Override
	public int hashCode() {
		return r.nextInt(100);  // Un pésimo y desesperante hashcode
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MalHasheada) {
			return valor==((MalHasheada)obj).valor;
		} else
			return false;
	}
	@Override
	public String toString() {
		return ""+valor;
	}
}

class NoOrdenada 
// Observa que sin implementar el interfaz genera una excepción de ejecución:
	implements Comparable<NoOrdenada>
//
{
	String nick;
	public NoOrdenada( String nick ) {
		this.nick = nick;
	}
	@Override
	public String toString() {
		return nick;
	}
	public int compareTo(NoOrdenada o) {
		// Observa que si la comparación está mal el set no funciona:
		return 0;
		// return nick.compareTo(o.nick);  // esto es lo correcto
	}
}
