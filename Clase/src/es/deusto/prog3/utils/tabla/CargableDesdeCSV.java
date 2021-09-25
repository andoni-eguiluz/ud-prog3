package es.deusto.prog3.utils.tabla;

import java.util.ArrayList;

/** Interfaz para las clases que pueden cargarse desde CSV y mostrarse de forma automática como objetos de tipo Tabla
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public interface CargableDesdeCSV extends ConvertibleEnTabla {
	/** Intenta cargar los datos del objeto desde una lista de valores obtenidos de CSV
	 * @param cabeceras	Lista de cabeceras CSV
	 * @param valores	Lista de valores de línea CSV
	 * @return	true si se ha podido hacer la carga, false si ha habido algún error
	 */
	public boolean cargaDesdeCSV( ArrayList<String> cabeceras, ArrayList<String> valores );

}
