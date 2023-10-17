package es.deusto.prog3.cap06.resueltos.ej6_3_interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/** Clase para cualquier set de datos basado en ArrayList que quiera visualizarse en una JTable con el modelo ModeloJTable
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class DatasetParaJTable implements TableModel {

	// Atributo de datos (genérico para cualquier clase que implemente FilaParaJTable)
	private List<FilaParaJTable> listaDatos = new ArrayList<FilaParaJTable>();
	// Atributo con instancia de ejemplo de la que sacar información cuando proceda (aunque el dataset esté vacío)
	private FilaParaJTable instanciaEjemplo;
	// Lista de escuchadores
	private ArrayList<TableModelListener> listaEsc = new ArrayList<>();
	
	public DatasetParaJTable( FilaParaJTable instanciaEjemplo ) {
		this.instanciaEjemplo = instanciaEjemplo;
		this.listaDatos = new ArrayList<>();
	}
	
	// Métodos que delegan en la fila (los sabe cada objeto)
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return instanciaEjemplo.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return instanciaEjemplo.getColumnCount();
	}

	private final String[] cabeceras = { "Código", "Nombre", "Habitantes", "Provincia", "Autonomía" };
	@Override
	public String getColumnName(int columnIndex) {
		// System.out.println( "getColumnName " + columnIndex );
		return cabeceras[columnIndex];
	}

	// Métodos que directamente acceden al dataset
	
	@Override
	public int getRowCount() {
		return listaDatos.size();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// Por defecto suponemos que no son editables
		return false;
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		listaEsc.add( l );
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listaEsc.remove( l );
	}
	
	// Métodos que acceden a métodos del objeto particular

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) throws IndexOutOfBoundsException {
		return listaDatos.get(rowIndex).getValueAt( columnIndex );
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) throws ClassCastException, IndexOutOfBoundsException {
		listaDatos.get(rowIndex).setValueAt( aValue, columnIndex );
	}

	// Métodos propios del modelo
	
	// Lanza una notificación para que se dispare llamada a los escuchadores de cambios en el modelo
	private void fireTableChanged( TableModelEvent e ) {
		for (TableModelListener l : listaEsc) {
			l.tableChanged( e );
		}
	}
	
	/** Borra un elemento dado del dataset
	 * @param fila	Número de fila (0 a n-1) a borrar
	 */
	public void borraFila( int fila ) throws IndexOutOfBoundsException {
		listaDatos.remove( fila );
		fireTableChanged( new TableModelEvent( this, fila, listaDatos.size() ));  // Para que detecte el cambio en todas las filas que cambian
	}
	
    /** Añade un nuevo dato al dataset
     * @param fila	Número de fila (0 a n) donde insertar
     * @param dato	Dato a insertar en ese punto
     */
    public void anyadeFila( int fila, FilaParaJTable dato ) {
    	listaDatos.add( fila, dato );
    	fireTableChanged( new TableModelEvent( this, fila, listaDatos.size() ) );  // Para que detecte el cambio en todas las filas que cambian
    }
    
    /** Añade un nuevo dato al dataset, al final
     * @param dato	Dato a insertar
     */
    public void add( FilaParaJTable dato ) {
    	listaDatos.add( dato );
    	fireTableChanged( new TableModelEvent( this, listaDatos.size()-1, listaDatos.size() ) );  // Para que detecte el cambio en todas las filas que cambian
    }

    /** Devuelve la lista de datos
     * @return	Lista de datos
     */
    public List<? extends FilaParaJTable> getLista() {
    	return listaDatos;
    }
    
    /** Devuelve un dato del dataset
     * @param fila	Posición del dato (de 0 a n-1)
     * @return	Dato situado en esa posición
     * @throws IndexOutOfBoundsException	Lanzada si el índice de fila es incorrecto
     */
    public FilaParaJTable get( int fila ) throws IndexOutOfBoundsException {
    	return listaDatos.get( fila );
    }

    /** Devuelve el tamaño del dataset
     * @return	Número de elementos actualmente almacenados
     */
    public int size() {
    	return listaDatos.size();
    }
    
}
