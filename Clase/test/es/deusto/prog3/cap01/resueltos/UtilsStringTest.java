package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

// import org.junit.After;
// import org.junit.Before;
import org.junit.Test;

public class UtilsStringTest {

// No hace falta @Before en este ejercicio
//	@Before
//	public void setUp() throws Exception {
//		System.out.println( "b");
//	}

// No hace falta @After en este ejercicio
//	@After
//	public void tearDown() throws Exception {
//		System.out.println( "a" );
//	}

	@Test
	public void quitarTabsYSaltosLinea() {
		// System.out.println( "t");
		String prueba = "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.";
		String prueba2 = "Hola#Esto es un string con tres líneas#y|varios|tabuladores.";
		assertEquals( prueba2, UtilsString.quitarTabsYSaltosLinea(prueba) );
	}

	@Test
	public void quitarTabsYSaltosLinea2() {
		assertEquals("", UtilsString.quitarTabsYSaltosLinea(""));
		assertEquals("hola", UtilsString.quitarTabsYSaltosLinea("hola"));
		assertEquals("|", UtilsString.quitarTabsYSaltosLinea("\t"));
		assertEquals("||", UtilsString.quitarTabsYSaltosLinea("\t\t"));
		assertEquals(" | | | ", UtilsString.quitarTabsYSaltosLinea(" \t \t \t "));
		assertEquals("##", UtilsString.quitarTabsYSaltosLinea("\n\n"));
	}

	@Test
	public void quitarTabsYSaltosLinea3() {
		assertEquals(null, UtilsString.quitarTabsYSaltosLinea(null));
		assertNull(UtilsString.quitarTabsYSaltosLinea(null));
	}

	@Test
	public void wrapString() {
		assertEquals("Andoni", UtilsString.wrapString("Andoni", 10));
		assertEquals("Ando...", UtilsString.wrapString("Andoni", 4));
		assertEquals("", UtilsString.wrapString("", 4));
		assertEquals("Andoni\nMar...", UtilsString.wrapString("Andoni\nMaría", 10));
	}
	
	@Test
	public void wrapString2() {
		assertEquals(null, UtilsString.wrapString(null, 10));
	}

	// Cómo comprobar excepciones - con try/catch
	@Test
	public void wrapStringNegativo() {
		try {
			UtilsString.wrapString("Andoni", -5);
			fail( "No excepción" );
			// 1
		} catch (IndexOutOfBoundsException e) {
			// 2
			// assertTrue( true );
		}
	}
	
	// Cómo comprobar excepciones - con parámetro de la anotación @Test
	@Test (expected = IndexOutOfBoundsException.class)
	public void wrapStringNegativo2() {
		UtilsString.wrapString("Andoni", -5);
	}

	@Test
	public void combinarQuitarTabsYWrapString() {
		String prueba = "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.";
		assertEquals( "Hola#Esto ...", 
				UtilsString.wrapString( UtilsString.quitarTabsYSaltosLinea(prueba), 10 ) );
	}	
	
}
