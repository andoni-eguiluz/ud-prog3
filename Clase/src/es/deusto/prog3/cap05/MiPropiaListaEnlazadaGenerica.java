package es.deusto.prog3.cap05;

public class MiPropiaListaEnlazadaGenerica<Tipo> {
	MiNodoGenerico<Tipo> inicio;
	
	public MiPropiaListaEnlazadaGenerica() {
		inicio = null;
	}
	
	public void insertaPrimero( Tipo t ) {
		MiNodoGenerico<Tipo> nuevoNodo = new MiNodoGenerico<Tipo>();
		nuevoNodo.valor = t;
		nuevoNodo.siguiente = inicio;
		inicio = nuevoNodo;
	}
	
	public String toString() {
		String ret = "(";
		MiNodoGenerico<Tipo> rec = inicio;
		while (rec != null)  {
			ret += (" " + rec.valor.toString());
			rec = rec.siguiente;
		}
		return ret + " )";
	}
	
	// Prueba de la lista
	public static void main(String[] args) {
		MiPropiaListaEnlazadaGenerica<Integer> l = new MiPropiaListaEnlazadaGenerica<Integer>();
		l.insertaPrimero( 5 );
		l.insertaPrimero( 2 );
		l.insertaPrimero( 3 );
		System.out.println( l.toString() );
	}

}

class MiNodoGenerico<Tipo> {
	Tipo valor;
	MiNodoGenerico<Tipo> siguiente;
}