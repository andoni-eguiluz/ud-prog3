package es.deusto.prog3.cap03.cs;

/** Clase de configuración de 
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ConfigCS {

	// Configuración de conexión
	static String HOST = "localhost";  // IP de conexión para la comunicación del CLIENTE
	static int PUERTO = 4000;          // Puerto de conexión para cliente y servidor
	
	// Diccionario de comunicación - protocolo definido para este ejemplo
	// el servidor devuelve un objeto recibido
	static String RECIBIDO = "recibido";
	// el servidor envía un objeto recibido de otro cliente
	static String RECIBIDO_DE = "recibido-de";
		// luego envía quién es el cliente que lo envía (String)
		// y luego envía el objeto que ese cliente envió (Object)
	// el cliente acaba mandando objeto fin:
	static String FIN = "fin";  
}
