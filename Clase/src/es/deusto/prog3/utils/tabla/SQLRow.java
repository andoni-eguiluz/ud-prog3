package es.deusto.prog3.utils.tabla;

import java.util.ArrayList;

public class SQLRow implements ConvertibleEnTabla {
	
	private ArrayList<String> datos;
	private ArrayList<String> cabs;
	private ArrayList<Class<?>> tipos;
	
	public SQLRow( ArrayList<String> cabs, ArrayList<Class<?>> tipos ) {
		datos = new ArrayList<>();
		this.cabs = cabs;
		this.tipos = tipos;
	}
	
	public SQLRow( ArrayList<String> datos, ArrayList<String> cabs, ArrayList<Class<?>> tipos ) {
		this.datos = datos;
		this.cabs = cabs;
		this.tipos = tipos;
	}
	
	/** Debe hacerse el mismo número de adds que de columnas tiene la fila
	 * @param dato
	 */
	public void addField( String dato ) {
		datos.add( dato );
	}
	
	public String get( String nomCab ) {
		for (int i=0; i<cabs.size(); i++) {
			if (cabs.get(i).equalsIgnoreCase( nomCab )) return datos.get(i);
		}
		return null;
	}
	
	public String get( int numCab ) {
		if (numCab<0 || numCab>=datos.size()) return null;
		else return datos.get(numCab);
	}
	
	public int size() { return cabs.size(); }
	
	public ArrayList<String> getCabs() { return cabs; }
	
	public ArrayList<Class<?>> getTipos() { return tipos; }
	
	public ArrayList<String> getFila() { return datos; }

	@Override
	public int getNumColumnas() {
		return cabs.size();
	}

	@Override
	public String getValorColumna(int col) {
		return datos.get( col );
	}

	@Override
	public void setValorColumna(int col, String valor) {
		datos.set( col, valor );
	}

	@Override
	public String getNombreColumna(int col) {
		return cabs.get( col );
	}

	@Override
	public String toString() {
		return datos.toString();
	}
}
