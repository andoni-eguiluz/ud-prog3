package es.deusto.prog3.cap04.resueltos;

import java.util.Random;

import es.deusto.prog3.cap04.ServicioLogin;

/** Solución de ejercicio de hackeo de passwords 4.7
 * Importar la librería ServicioLogin.jar
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class HackeandoPasswords {
	
	public static void main(String[] args) {
		System.out.println( "Pruebas aleatorias:" );
		Random random = new Random();
		boolean encontrado = false;
		while (!encontrado) {
			int aleatorio0_999 = random.nextInt(1000);
			String password = String.format( "%03d", aleatorio0_999 );
			encontrado = ServicioLogin.login( "digit3", password );
		}
		System.out.println( "Tardado en encontrar la password: " + ServicioLogin.tiempoDesdePrimerLoginIncorrecto() + " msgs." );
		
		System.out.println( "Pruebas de fuerza bruta (recursiva) de solo dígitos:" );
		String charset = "0123456789";
		for (int i=3; i<=8; i++) {
			System.out.println( i + " dígitos: " + fuerzaBruta( "digit" + i, charset, "", i ) + " msgs. (" + Math.pow( charset.length(), i ) + " posibilidades)" );
		}
		System.out.println( "Pruebas fuerza bruta de dígitos, letras y símbolos:" );
		charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ()[]{}!\"'@#$&=?^-+/*\\',.;:-_";
		for (int i=3; i<=8; i++) {
			System.out.println( i + " caracteres: " + fuerzaBruta( "luds" + i, charset, "", i ) + " msgs. (" + Math.pow( charset.length(), i ) + " posibilidades)" );
		}
	}
	
	// Prueba en ServicioLogin.login todas las contraseñas posibles de longitud indicada, por fuerza bruta
	// Si longitud < indicada, añadir cada opción de carácter y recursivamente seguir probando longitud-1
	// Si longitud == indicada, probar login y devolver -1 si no ok o milisegundos tardados si ok
	private static long fuerzaBruta( String usuario, String charset, String yaConstruido, int longitud ) {
		if (yaConstruido.length()==longitud) {  // Caso base - longitud máxima
			if (ServicioLogin.login( usuario, yaConstruido )) {
				System.out.println( "  Encontrada contraseña " + yaConstruido + " de " + usuario );
				return ServicioLogin.tiempoDesdePrimerLoginIncorrecto();
			} else {
				return -1;
			}
		}
		for (int i=0; i<charset.length(); i++) {
			char car = charset.charAt( i );
			long ret = fuerzaBruta( usuario, charset, yaConstruido + car, longitud );
			if (ret!=-1) { // Truncado de combinatoria recursiva por resultado encontrado
				return ret;
			}
		}
		return -1;
	}

}
