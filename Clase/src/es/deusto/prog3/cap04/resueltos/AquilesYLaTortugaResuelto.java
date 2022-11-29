package es.deusto.prog3.cap04.resueltos;

public class AquilesYLaTortugaResuelto {
	
	public static final double VEL_AQUILES = 10;   // metros / sg
	public static final double VEL_TORTUGA = 0.05; // m/sg (0.05m/sg = 1 metro cada 20 segs) 
	
	public static final double INICIO_AQUILES = 0;    // Aquiles empieza en el metro 0
	public static final double INICIO_TORTUGA = 1000; // La tortuga tiene 1 km de ventaja
	
	/** Devuelve la posición de Aquiles en la carrera, dado el tiempo transcurrido
	 * @param t	Tiempo transcurrido de carrera (en sgs)
	 * @return	Posición de Aquiles (en m)
	 */
	public static double dondeEstaAquiles( double t ) {
		return INICIO_AQUILES + VEL_AQUILES * t;
	}

	/** Devuelve la posición de la tortuga en la carrera, dado el tiempo transcurrido
	 * @param t	Tiempo transcurrido de carrera (en sgs)
	 * @return	Posición de la tortuga (en m)
	 */
	public static double dondeEstaLaTortuga( double t ) {
		return INICIO_TORTUGA + VEL_TORTUGA * t;
	}
	
	// Algoritmo recursivo
	// tIni es un tiempo en el que Aquiles todavía no ha alcanzado a la tortuga
	// tFin es un tiempo en el que Aquiles ha pasado a la tortuga
	public static double cuandoSeEncuentran( double tIni, double tFin ) {
		numLlams++; // Auxiliar para contar el número de llamadas
		
		// Varias condiciones posibles de caso base (habría que dejar solo una)
		if (numLlams>20) {  // Si queremos limitar el tiempo de cálculo  
			return tIni;
		}
		if (Math.abs(tIni-tFin) < 0.001) {  // Si queremos asegurar la precisión temporal
			return tIni;
		}
		// Esta otra opción habría que hacerla después de calcular las posiciones de Aquiles y la tortuga
		// if (Math.abs(sAquiles-sTortuga) < 0.001) {  // Si queremos asegurar la exactitud de localización
		// 	return tIni;
		// } 
		
		else {  // Caso recursivo
			double tMedio = (tIni+tFin)/2;
			double sAquiles = dondeEstaAquiles( tMedio );
			double sTortuga = dondeEstaLaTortuga( tMedio );
			System.out.println( tIni + "," + tFin );
			System.out.println( "   " + sAquiles + "," + sTortuga );
			if (sAquiles <= sTortuga) {
				return cuandoSeEncuentran( tMedio , tFin );
			} else {
				return cuandoSeEncuentran( tIni, tMedio );
			}
		}
	}
	
	private static int numLlams = 0;
	public static void main(String[] args) {
		double t = 50000;
//		System.out.println( "Ejemplo. Tiempo = " + t + " segundos" );
//		System.out.println( " Aquiles está en " + dondeEstaAquiles(t));
//		System.out.println( " La tortuga está en " + dondeEstaLaTortuga(t));
//		System.out.println( "Solución:" );
		try {
			double tSol = cuandoSeEncuentran( 0.0, t );
			System.out.println( "Tiempo de encuentro = " + tSol );
			System.out.println( "  Distancia de encuentro = " + dondeEstaAquiles(tSol));
			System.out.println( "Número de llamadas: " + numLlams );
		} catch (StackOverflowError e) {
			System.out.println( "Stack overflow!! " + numLlams );
		}
		
	}
	
}
