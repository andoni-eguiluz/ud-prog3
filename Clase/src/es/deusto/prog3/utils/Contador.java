package es.deusto.prog3.utils;

/** Clase para gestionar contadores
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class Contador {
	
	private int cont = 0;  // Contador entero
	
	/** Inicializa un contador a cero
	 */
	public Contador() {
	}
	/** Inicializa un contador con el valor indicado
	 * @param valor	Valor de inicio del contador
	 */
	public Contador( int valor ) {
		cont = valor;
	}
	/** Devuelve el valor del contador
	 * @return	Valor actual del contador
	 */
	public int get() { return cont; }
	/** Incrementa el contador
	 */
	public void inc() { cont++; }
	
	public void inc( int inc ) { cont += inc; }
	
	@Override
	public String toString() { return "" + cont; }
	
}
