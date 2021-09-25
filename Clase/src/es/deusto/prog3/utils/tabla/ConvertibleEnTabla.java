package es.deusto.prog3.utils.tabla;

/** Interfaz para las clases que pueden pasarse de forma automática a objetos de tipo Tabla
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public interface ConvertibleEnTabla {
	/** Devuelve el número de columnas que se representan para el objeto
	 * @return	Número de columnas
	 */
	public int getNumColumnas();
	/** Devuelve el valor String del objeto correspondiente a una columna concreta
	 * @param col	Número de columna (de 0 a getNumColumnas()-1)
	 * @return	Valor string de ese dato del objeto
	 */
	public String getValorColumna( int col );
	/** Modifica el valor String del objeto correspondiente a una columna concreta
	 * @param col	Número de columna (de 0 a getNumColumnas()-1)
	 * @param valor	Nuevo valor de objeto para esa columna
	 */
	public void setValorColumna( int col, String valor );
	/** Devuelve el nombre de la columna
	 * @param col	Número de columna (de 0 a getNumColumnas()-1)
	 * @return	Nombre de título de ese dato del objeto
	 */
	public String getNombreColumna( int col );
}
