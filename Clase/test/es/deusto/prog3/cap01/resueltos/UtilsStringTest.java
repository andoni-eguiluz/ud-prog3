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
		assertEquals("#", UtilsString.quitarTabsYSaltosLinea("\n"));
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
		} catch (Exception e) {
			fail( "Excepción incorrecta" );
		}
	}
	
	// Cómo comprobar excepciones - con parámetro de la anotación @Test
	@Test (expected = IndexOutOfBoundsException.class)
	public void wrapStringNegativo2() {
		UtilsString.wrapString("Andoni", -5);
	}
	
	@Test
	public void wrapStringCero() {
		assertEquals( "...", UtilsString.wrapString( "Andoni", 0) );
		assertEquals( "", UtilsString.wrapString( "", 0) );
	}

	@Test
	public void combinarQuitarTabsYWrapString() {
		String prueba = "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.";
		assertEquals( "Hola#Esto ...", 
				UtilsString.wrapString( UtilsString.quitarTabsYSaltosLinea(prueba), 10 ) );
	}	

	// Prueba de TDD de conversión a strings ordenables
	// Problema: ver clase ConceptoCompStrings
//	System.out.println( "panza".compareTo( "papa" ) );
//	System.out.println( "panza".compareTo( "Papa" ) );  Mal por mayúsculas
//	System.out.println( "pánza".compareTo( "papa" ) );  Mal por tildes
//	System.out.println( "panza".compareTo( "pañal" ) );
//	System.out.println( "pañal".compareTo( "papa" ) );  Mal por eñe
//	System.out.println( "panzzal".compareTo( "papa" ) );

	
	@Test
	public void convierteAOrdenableMays() {
		String conv1 = UtilsString.convierteAOrdenable( "panza" );
		String conv2 = UtilsString.convierteAOrdenable( "papa" );
		assertTrue( conv1.compareTo( conv2 ) < 0 );
		String conv3 = UtilsString.convierteAOrdenable( "Papa" );
		assertTrue( conv1.compareTo( conv3 ) < 0 );
	}
	
	@Test
	public void convierteAOrdenableTildes() {
		// Opción 1 - Según la lógica que queremos que tenga el método 
		String conv1 = UtilsString.convierteAOrdenable( "pánza" );
		String conv2 = UtilsString.convierteAOrdenable( "papa" );
		assertTrue( conv1.compareTo( conv2 ) < 0 );

		// Opción 2 - Se podría hacer así? 
		assertEquals( "PANZA", conv1 );
		assertEquals( "PAPA", conv2 );
		// Sí, de acuerdo a la manera en la que queremos que se implemente esa lógica
		// Dependiendo de cómo definamos el javadoc del método, se podrían hacer las pruebas
		// de las dos maneras, o solo de la segunda.
		// Desde el punto de vista del diseño de software, es mejor que el método tenga la definición
		//  más general posible y que oculte todo lo posible la implementación
	}
	
	@Test
	public void convierteAOrdenableEnyes() {
		String conv1 = UtilsString.convierteAOrdenable( "panza" );
		String conv2 = UtilsString.convierteAOrdenable( "pañal" );
		String conv3 = UtilsString.convierteAOrdenable( "papa" );
		assertTrue( conv1.compareTo( conv2 ) < 0 );
		assertTrue( conv2.compareTo( conv3 ) < 0 );
	}
	
}
