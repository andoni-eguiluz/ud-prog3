package es.deusto.prog3.cap06.pr0506resuelta;

import java.util.ArrayList;

public class AccesoAPersonasAL implements ProcesoProbable {

	private ArrayList<Persona> l;

	@Override
	public void init(int tamanyoTest) {
		l = new ArrayList<>();
		for (int i=0; i<tamanyoTest; i++) {
			l.add( new Persona( i*2+1, "Nombre " + i, "Apellido " + i ));
		}
	}

	public int cont;  // Se hace el contador atributo para que la actualización del contador del test no pueda ser optimizada (y eliminada) por el compilador
	@Override
	public Object test() {
		cont = 0;
		for (int i=0; i<l.size(); i++) {
			if (l.contains( new Persona(i,"","") )) cont++;
		}
		// System.out.println( "Número personas encontradas: " + cont );
		return l;
	}

	/** Método de prueba de la clase
	 * @param args
	 */
	public static void main(String[] args) {
		AccesoAPersonasAL proc = new AccesoAPersonasAL();
		long tiempo = BancoDePruebas.realizaTest( proc, 50000 );
		int espacio = BancoDePruebas.getTamanyoTest();
		System.out.println( "Prueba ArrayList de 50000 -- tiempo: " + tiempo + " msgs. / espacio = " + espacio + " bytes.");
	}

}
