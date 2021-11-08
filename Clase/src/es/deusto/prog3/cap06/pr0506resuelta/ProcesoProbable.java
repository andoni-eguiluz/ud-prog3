package es.deusto.prog3.cap06.pr0506resuelta;

/** Interfaz Java para un proceso que puede probarse (en tiempo y espacio de memoria) en el banco de pruebas.
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public interface ProcesoProbable {
	
	/** Método de inicialización de test (si es necesario)
	 * @param tamanyoTest	Tamaño del test a realizar (típicamente, tamaño de la estructura de datos a manejar)
	 */
	public void init( int tamanyoTest );
	
	/** Realización de la prueba. Debe llamarse antes al método init (cuando la inicialización sea necesaria).
	 * @param objetoProducido	Objeto que se devuelve como resultado del test, relacionado con el uso de memoria que se quiere medir.
	 */
	public Object test();

}
