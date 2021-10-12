package es.deusto.prog3.cap01.ejercicios;

import static org.junit.Assert.*;
import org.junit.Test;

public class UtilsStringTest {

	@Test
	public void quitarTabsYSaltosLinea() {
		String prueba = "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.";
		String prueba2 = "Hola#Esto es un string con tres líneas#y|varios|tabuladores.";
		assertEquals(UtilsString.quitarTabsYSaltosLinea(prueba), prueba2);
//		if (prueba2.equals(quitarTabsYSaltosLinea(prueba))) {
//			System.out.println( "OK" );
//		} else {
//			System.out.println( "FAIL" );
//		}
	}
	
	@Test
	public void wrapString() {
		
	}

}
