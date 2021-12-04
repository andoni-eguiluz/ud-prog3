package es.deusto.prog3.cap05;

import java.util.ArrayList;

import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasAL;
import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasHS;
import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasLL;
import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasTS;
import es.deusto.prog3.cap06.pr0506resuelta.ProcesoProbable;
import es.deusto.prog3.cap06.pr0506resuelta.Utils;
import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;

/** Prueba combinada de distintas estructuras de datos y distintos tamaños
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class PruebaEstructurasPersonaVentana {

	public static void main(String[] args) {
		Utils.muestraThreadsActivos();
		String[] pruebas = { "ArrayList", "LinkedList", "HashSet", "TreeSet" };
		ArrayList<ProcesoProbable> procs = new ArrayList<ProcesoProbable>();
		procs.add( new AccesoAPersonasAL() );
		procs.add( new AccesoAPersonasLL() );
		procs.add( new AccesoAPersonasHS() );
		procs.add( new AccesoAPersonasTS() );
		VentanaBancoDePruebas vent = new VentanaBancoDePruebas();
		vent.setProcesos( pruebas, procs );
		vent.setTamanyos( 0, 20000, 1000 );
		vent.setVisible( true );
		Utils.muestraThreadsActivos();
	}

}
