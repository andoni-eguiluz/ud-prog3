package es.deusto.prog3.cap03.cs2;

/** Clase de configuración de comunicación cliente-servidor para ejemplo de persistencia por servidor remoto
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ConfigCS {

	// Configuración de conexión
	static String HOST = "localhost";  // IP de conexión para la comunicación del cliente hacia el servidor
	static int PUERTO = 45678;         // Puerto de conexión para cliente y servidor
	
	// Diccionario de comunicación - protocolo definido para este ejemplo

	// -----------------------------
	// Envío de servidor a cliente
	// -----------------------------
	
	// Login correcto
	static String OK = "ok";  
	
	// Login incorrecto
	static String NO_OK = "no_ok";  
	
	// -----------------------------
	// Envío de cliente a servidor
	// (Obsérvese como hay correspondencia con cada una de las operaciones que necesita el servicio de persistencia)
	// -----------------------------
	
	// Login de inicio - va seguido de dos strings: USUARIO y CONTRASEÑA y el servidor lo responde con un OK o NO_OK
	static String LOGIN = "login";
	
	// Fin de comunicación - no se responde nada del servidor
	static String FIN = "fin";  
	
	// Petición de número de productos - el servidor responde con un Integer
	static String GET_NUMERO_PRODUCTOS = "getNumeroProductos";

	// Petición de producto por código - va seguido de un Integer (código), el servidor responde con un Producto (o null)
	static String BUSCAR_CODIGO = "buscarCodigo";  

	// Petición de producto por nombre - va seguido de un String (nombre), el servidor responde con un Producto (o null)
	static String BUSCAR_NOMBRE = "buscarNombre";  

	// Petición de productos - el servidor responde con la lista de todos los productos List<Producto>
	static String BUSCAR_TODOS = "buscarTodos";  

	// Petición de productos por texto - va seguido de un String (texto), el servidor responde con List<Producto>
	static String BUSCAR_PARTE_NOMBRE = "buscarParteNombre";
	
	// Petición de inserción de producto - va seguido de un Producto y de un ImageIcon, el servidor responde con el producto insertado (o null)
	static String INSERTAR = "insertar";  

	// Petición de actualización de producto - va seguido de un Producto y de un ImageIcon, el servidor responde con el producto modificado (o null)
	static String ACTUALIZAR = "actualizar";  
	
	// Petición de imagen de producto - va seguido de un Producto, el servidor responde con un objeto ImageIcon
	static String CARGA_IMAGEN = "cargaImagen";  
	
	// Petición especial para vaciado de tabla de productos (caso de inicio de datos de test) - no devuelve nada
	static String BORRA_PRODUCTOS =  "borra-productos";
	
}
