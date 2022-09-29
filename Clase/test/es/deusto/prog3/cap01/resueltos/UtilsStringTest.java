package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilsStringTest {

//	@Before
//	public void setUp() throws Exception {
//		System.out.println( "b");
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		System.out.println( "a" );
//	}

	@Test
	public void test() {
		// System.out.println( "t");
		String prueba = "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.";
		String prueba2 = "Hola#Esto es un string con tres líneas#y|varios|tabuladores.";
		assertEquals( prueba2, UtilsString.quitarTabsYSaltosLinea(prueba) );
		// fail("Not yet implemented");
	}

	@Test
	public void test2() {
		System.out.println( "t2" );
	}
	
}
