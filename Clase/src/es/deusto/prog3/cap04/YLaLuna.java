package es.deusto.prog3.cap04;

/** Cómo está de lejos la luna? A tiro de folio?
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class YLaLuna {

	public static void main(String[] args) {
		aLaLuna( 0.0001, 384400000.0, 0 );
	}
	
	// Calcula el número de dobleces de un folio hasta la luna de forma recursiva
	// cálculo (grosor, distancia, num dobleces) -> 
	//   cálculo( grosor*2, distancia, num dobleces + 1 ) si grosor < distancia
	//   num dobleces si grosor >= distancia
	private static void aLaLuna( double grosor, double dist, long numDob ) {
		if (grosor >= dist) {
			System.out.println( "Luna encontrada en " + numDob + " dobleces!" );
		} else {
			aLaLuna( grosor*2, dist, numDob+1 );
		}
	}

}
