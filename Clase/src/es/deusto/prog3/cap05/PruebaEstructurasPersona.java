package es.deusto.prog3.cap05;

import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasAL;
import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasHS;
import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasLL;
import es.deusto.prog3.cap06.pr0506resuelta.AccesoAPersonasTS;
import es.deusto.prog3.cap06.pr0506resuelta.BancoDePruebas;
import es.deusto.prog3.cap06.pr0506resuelta.ProcesoProbable;

/** Prueba combinada de distintas estructuras de datos y distintos tamaños
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class PruebaEstructurasPersona {

	public static void main(String[] args) {
		String[] pruebas = { "ArrayList", "LinkedList", "HashSet", "TreeSet" };
		ProcesoProbable[] procs = new ProcesoProbable[4];
		procs[0] = new AccesoAPersonasAL();
		procs[1] = new AccesoAPersonasLL();
		procs[2] = new AccesoAPersonasHS();
		procs[3] = new AccesoAPersonasTS();
		int tamanyo= 100;
		while (tamanyo <= 100000) {
			for (int prueba=0; prueba<4; prueba++) {
				long tiempo = BancoDePruebas.realizaTest( procs[prueba], tamanyo );
				int espacio = BancoDePruebas.getTamanyoTest();
				System.out.println( "Prueba " + pruebas[prueba] + " de " + tamanyo + " -- tiempo: " + tiempo + " msgs. / espacio = " + espacio + " bytes.");
			}
			tamanyo *= 10;
		}
	}

}
