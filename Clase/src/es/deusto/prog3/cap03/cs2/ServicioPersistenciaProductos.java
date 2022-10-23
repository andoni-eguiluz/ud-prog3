package es.deusto.prog3.cap03.cs2;

import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

/** Comportamiento generalizado de un servicio de persistencia de productos
 * Podrá ser implementado con ficheros, con bases de datos, o con conexiones a un servidor remoto de persistencia
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public interface ServicioPersistenciaProductos {
	/** Inicializa el servicio de persistencia, abriendo las conexiones y recursos necesarios
	 * @param nombrePersistencia	Nombre interno de respaldo de almacenamiento de datos
	 * @param configPersistencia	Información de configuración (si procede), dependiente del tipo de gestor de persistencia
	 * @return	true si se inicia correctamente, false en caso contrario
	 */
	public boolean init( String nombrePersistencia, String configPersistencia );
	/** Inicializa el servicio de persistencia con datos de prueba, sin cargarlos del sistema, abriendo las conexiones y recursos necesarios
	 * @param nombrePersistencia	Nombre interno de respaldo de almacenamiento de datos  (no se usa para cargar)
	 * @param configPersistencia	Información de configuración (si procede), dependiente del tipo de gestor de persistencia
	 * @return	true si se inicia correctamente, false en caso contrario
	 */
	public boolean initDatosTest( String nombrePersistencia, String configPersistencia );
	/** Cierra el servicio de persistencia y todas las conexiones y recursos que pudieran haber sido necesarias. A partir de este momento no se puede utilizar para recuperar datos
	 */
	public void close();
	/** Devuelve el número de productos almacenados en el servicio
	 * @return
	 */
	public int getNumeroProductos();
	/** Busca un producto dado su código
	 * @param codigo	Código a buscar
	 * @return	Producto con ese código, null si no se encuentra
	 */
	public Producto buscarCodigo( int codigo );
	/** Busca un producto dado su nombre
	 * @param nombre	Nombre a buscar
	 * @return	Primer producto con ese nombre (exacto), null si no se encuentra
	 */
	public Producto buscarNombre( String nombre );
	/** Recupera todos los productos
	 * @return	Lista de todos los productos del servicio (vacía si no hay ninguno)
	 */
	public List<Producto> buscarTodos();
	/** Busca productos que contengan un texto definido
	 * @param parteNombre	Parte del texto a buscar
	 * @return	Lista de todos los productos cuyos nombres contienen ese texto (vacía si no hay ninguno)
	 */
	public List<Producto> buscarParteNombre( String parteNombre );
	/** Añade un nuevo producto al servicio de persistencia. Si procede, modifica la ruta de fichero del producto, de acuerdo al servicio de persistencia
	 * @param producto	Producto a añadir
	 * @param imagen	Si es null, se considera el fichero que enlace el producto. Si no, se considera la imagen y se genera el fichero desde ella (utilizar esta opción en almacenamiento remoto)
	 * @return	true si se ha añadido correctamente, false en caso contrario
	 */
	public boolean insertar( Producto producto, ImageIcon imagen );
	/** Modifica los datos de un producto existente. Si procede, modifica la ruta de fichero del producto, de acuerdo al servicio de persistencia
	 * La imagen del producto debe ser de uno de los tres formatos: GIF, JPG, PNG
	 * @param producto	Producto a modificar (no se puede modificar el código)
	 * @param imagen	Si es null, se considera el fichero que enlace el producto. Si no, se considera la imagen y se genera el fichero desde ella (utilizar esta opción en almacenamiento remoto)
	 * @return	true si se ha modificado correctamente, false en caso contrario
	 */
	public boolean actualizar( Producto producto, ImageIcon imagen );
	/** Devuelve la imagen del sistema de persistencia. Si procede, modifica la ruta de fichero del producto, de acuerdo al servicio de persistencia
	 * La imagen del producto debe ser de uno de los tres formatos: GIF, JPG, PNG
	 * @param producto	Producto del que devolver la imagen
	 * @return	Imagen de ese producto, null si hay cualquier error
	 */
	public ImageIcon cargaImagen( Producto producto );
	/** Asigna un logger ya creado para que se haga log de las operaciones de actualización de datos. Si no se asigna, cada clase que implemente el interfaz definirá su propio logger por defecto
	 * @param logger	Logger ya creado
	 */
	public void setLogger( Logger logger );
}
