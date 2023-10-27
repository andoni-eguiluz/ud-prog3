package es.deusto.prog3.cap05.resueltos;

import java.util.*;

/* Ejercicio 5.2
 */

public class SistemaElectoral {

	private static String[] nombres = { "Luis", "María", "Elena", "Andoni", "Isabel", "Asier", "Andoni", "Luis", "Carlos", "Elena", "Luis", "Aitziber", "Luis" };

	public static void main(String[] args) {
		LinkedList<String> ll = new LinkedList<>( Arrays.asList( nombres ) );
		System.out.println( ll );
		Iterator<String> li = ll.descendingIterator();
		ArrayList<String> al = new ArrayList<>();
		while (li.hasNext()) {
			al.add( li.next() );
		}
		System.out.println( al );
		// Cuenta con hashset de strings
		HashSet<String> hs = new HashSet<>();
		for (String nombre : al) {
			hs.add( nombre );
		}
		System.out.println( hs );
		System.out.println( "Número nombres: " + hs.size() );
		// Cuenta de cuántos nombres de cada con hs y la lista origen
		Iterator<String> hsi = hs.iterator();
		while (hsi.hasNext()) {
			String nomUnico = hsi.next();
			int cont = 0;
			for (String nombre : al) {
				if (nombre.equals( nomUnico )) {
					cont++;
				}
			}
			System.out.print( nomUnico + "-" + cont + " ");
		}
		System.out.println();
		// Cuenta de cuántos nombres de cada con hashset de objetos
		HashSet<CuentaNombre> hn = new HashSet<>();
		for (String nombre : al) {
			CuentaNombre cn = new CuentaNombre( nombre );
			if (hn.contains(cn)) {
				for (CuentaNombre cn2 : hn) {  // Perdemos la eficiencia de la búsqueda hash - hay que recorrer
					if (cn2.nombre.equals( nombre )) {
						cn2.inc();
						break;
					}
				}
			} else {
				hn.add( cn );
			}
		}
		System.out.println( hn );
		// VentanaBancoDePruebas vista = new VentanaBancoDePruebas( hs, "Hashset de CuentaNombre" );
		// VentanaBancoDePruebas vista2 = new VentanaBancoDePruebas( hn, "Hashset de String" );
		// vista.setVisible( true );
		// vista2.setVisible( true );
		
		TreeSet<CuentaNombre> ts = new TreeSet<>( hn );
		System.out.println( ts );
		
		HashMap<String,CuentaNombre> hm = new HashMap<>();
		for (String nombre : al) {
			if (!hm.containsKey( nombre )) {
				hm.put( nombre, new CuentaNombre( nombre ) );
			} else {
				hm.get( nombre ).inc();
			}
		}
		System.out.println( hm );
		ArrayList<CuentaNombre> ln = new ArrayList<>();
		for (CuentaNombre cn : hm.values()) {
			ln.add( cn );
		}
		ln.sort( null );
		System.out.println( ln );
		// ¿Con un treemap? No tiene mucho sentido pero se puede
		TreeMap<CuentaNombre,CuentaNombre> tm = new TreeMap<>();
		for (CuentaNombre cn : hm.values()) {
			tm.put( cn, cn );
		}
		System.out.println( tm );
	}

	private static class CuentaNombre implements Comparable<CuentaNombre> {
		String nombre;
		int contador;
		public CuentaNombre( String nombre ) {
			this.nombre = nombre;
			contador = 1;
		}
		public void inc() {
			contador++;
		}
		@Override
		public String toString() {
			return nombre + "-" + contador;
		}
		// OJO!
		@Override
		public int hashCode() {
			return nombre.hashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CuentaNombre) {
				CuentaNombre cn = (CuentaNombre) obj;
				return cn.nombre.equals( nombre );
			} else {
				return false;
			}
		}
		@Override
		public int compareTo(CuentaNombre o) {
			int comp = o.contador - contador;
			if (comp!=0) {
				return comp;
			}
			return nombre.compareTo( o.nombre );
		}
	}
}
