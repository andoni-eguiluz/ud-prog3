package es.deusto.prog3.cap06.resueltos.ej6_3_interfaces;

/** Permite crear objetos municipio con información de población, provincia y comunidad autónoma
 */
public class Municipio implements FilaParaJTable {  // Especializa un comportamiento de cualquier clase que podamos querer como fila en una JTable
	private int codigo;
	private String nombre;
	private int habitantes;
	private String provincia;
	private String autonomia;

	/** Crea un municipio
	 * @param codigo	Código único del municipio (1-n)
	 * @param nombre	Nombre oficial
	 * @param habitantes	Número de habitantes
	 * @param provincia	Nombre de su provincia
	 * @param autonomia	Nombre de su comunidad autónoma
	 */
	public Municipio(int codigo, String nombre, int habitantes, String provincia, String autonomia) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.habitantes = habitantes;
		this.provincia = provincia;
		this.autonomia = autonomia;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getHabitantes() {
		return habitantes;
	}

	public void setHabitantes(int habitantes) {
		this.habitantes = habitantes;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getAutonomia() {
		return autonomia;
	}

	public void setAutonomia(String autonomia) {
		this.autonomia = autonomia;
	}

	@Override
	public String toString() {
		return "[" + codigo + "] " + nombre + ", " + habitantes + " en " + provincia + " (" + autonomia + ")";
	}

	// Implementación de FilaParaJTable

	private static final Class<?>[] CLASES_COLS = { Integer.class, String.class, Integer.class, String.class, String.class };
	private static final String[] CABECERAS_COLS = { "Código", "Nombre", "Habitantes", "Provincia", "Autonomía" };
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return CLASES_COLS[columnIndex];
	}
	
	@Override
	public int getColumnCount() {
		return CLASES_COLS.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return CABECERAS_COLS[columnIndex];
	}

	@Override
	public Object getValueAt(int columnIndex) throws IndexOutOfBoundsException {
		switch (columnIndex) {
			case 0:
				return getCodigo();
			case 1:
				return getNombre();
			case 2:
				return getHabitantes();
			case 3:
				return getProvincia();
			case 4:
				return getAutonomia();
			default:
				throw new IndexOutOfBoundsException( "Columna incorrecta: " + columnIndex );
		}
	}

	@Override
	public void setValueAt(Object aValue, int columnIndex) throws ClassCastException, IndexOutOfBoundsException {
		switch (columnIndex) {
			case 0:
				setCodigo( (Integer) aValue );  // Se puede producir ClassCastException (igual que en el resto de casts)
				break;
			case 1:
				setNombre( (String) aValue );
				break;
			case 2:
				setHabitantes( (Integer) aValue );
				break;
			case 3:
				setProvincia( (String) aValue );
				break;
			case 4:
				setAutonomia( (String) aValue );
				break;
			default:
				throw new IndexOutOfBoundsException( "Columna incorrecta: " + columnIndex );
		}
	}
	
	
}

