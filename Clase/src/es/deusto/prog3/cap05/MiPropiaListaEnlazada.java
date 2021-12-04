package es.deusto.prog3.cap05;

public class MiPropiaListaEnlazada {
	int tam;
	MiNodo inicio;
	
	public MiPropiaListaEnlazada() {
		inicio = null;
		tam = 0;
	}
	
	public void insertaPrimero( Object o ) {
		MiNodo nuevoNodo = new MiNodo();
		nuevoNodo.valor = o;
		nuevoNodo.siguiente = inicio;
		inicio = nuevoNodo;
		tam++;
	}
	
	public String toString() {
		String ret = "(";
		MiNodo rec = inicio;
		while (rec != null)  {
			ret += (" " + rec.valor.toString());
			rec = rec.siguiente;
		}
		return ret + " )    [tam=" + tam + "]";
	}
	
	// Prueba de la lista
	public static void main(String[] args) {
		MiPropiaListaEnlazada l = new MiPropiaListaEnlazada();
		l.insertaPrimero( 5 );
		l.insertaPrimero( 2 );
		l.insertaPrimero( 3 );
		System.out.println( l.toString() );
	}

}

class MiNodo {
	Object valor;
	MiNodo siguiente;
}