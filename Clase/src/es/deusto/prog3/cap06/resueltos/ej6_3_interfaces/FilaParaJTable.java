package es.deusto.prog3.cap06.resueltos.ej6_3_interfaces;

/** Interfaz para modelar cualquier objeto que quiera trabajarse en una JTable
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public interface FilaParaJTable {

	/** Devuelve el tipo de datos de la columna indicada
	 * @param columnIndex	Índice de columna (0 a n-1)
	 * @return	Tipo de esa columna
	 */
	public Class<?> getColumnClass(int columnIndex);

	/** Devuelve el número de columnas
	 * @return	Número de atributos que se van a mostrar en la tabla visual
	 */
	public int getColumnCount();
	
	/** Devuelve el nombre a visualizar en la columna indicada
	 * @param columnIndex	Índice de columna (0 a n-1)
	 * @return	Nombre que quiere mostrarse de esa columna en la cabecera de la tabla
	 */
	public String getColumnName(int columnIndex);

	/** Devuelve valor de un dato particular del objeto
	 * @param columnIndex	Número de atributo/columna (0 a n-1)
	 * @return	Valor de ese atributo
	 * @throws IndexOutOfBoundsException	Error generado si la columna tiene un valor erróneo
	 */
	public Object getValueAt(int columnIndex) throws IndexOutOfBoundsException;

	/** Modifica el valor de un dato particular del objeto
	 * @param aValue	Nuevo valor a asignar
	 * @param columnIndex	Número de atributo/columna (0 a n-1)
	 * @throws ClassCastException	Error generado si el valor suministrado no es compatible con el número de atributo-columna indicado
	 * @throws IndexOutOfBoundsException	Error generado si la columna tiene un valor erróneo
	 */
	public void setValueAt(Object aValue, int columnIndex) throws ClassCastException, IndexOutOfBoundsException;	
	
}
