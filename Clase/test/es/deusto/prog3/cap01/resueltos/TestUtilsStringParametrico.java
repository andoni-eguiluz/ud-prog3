package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import es.deusto.prog3.cap01.resueltos.UtilsString;

/** Prueba de JUnit paramétrico (con listas de valores a testear)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 * (Ver comentarios al final del fichero)
 */
@RunWith(Parameterized.class)  // Esta anotación informa a JUnit de que este test es paramétrico
public class TestUtilsStringParametrico {

	@Before
	public void setUp() throws Exception {
		// No hay que hacer inicialización en este caso 
	}

	@After
	public void tearDown() throws Exception {
		// No hay que hacer cierre en este caso
	}

	@Parameters  // Esta anotación prepara un método que devuelve todos los valores a recorrer
	             // Tiene que estar en forma de una lista de arrays de Object
	public static List<Object[]> data() {
		return Arrays.asList(     
			new Object[] { "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.", "Hola#Esto es un string con tres líneas#y|varios|tabuladores." },
			new Object[] { "Esto solo tiene\ttabs", "Esto solo tiene|tabs" }, 
			new Object[] { "Y esto no tiene líneas ni tabuladores", "Y esto no tiene líneas ni tabuladores" }, 
			new Object[] { "", "" }, 
			new Object[] { null, null }  
		);
    }
	
	@Parameter(0) // Este es el primer elemento de cada array de Objects
	public String inicial;
	
	@Parameter(1) // Este es el segundo elemento de cada array de Objects (en este caso solo hay dos, podría haber n)
	public String esperado;
	
	@Test  // En el JUnit paramétrico se pueden utilizar los atributos públicos asignados a los elementos del array.
	       // Se ejecutará varias veces, tantas como elementos devuelva el método indicado con @Parameters
	public void testQuitarTabsYSaltosLinea() {
		assertEquals( esperado, UtilsString.quitarTabsYSaltosLinea( inicial ));
	}

}

// Obviamente, se puede hacer toda esta repetición "manualmente" con una repetitiva
// dentro de un método de test (ver abajo).
// La ventaja es que si utilizamos el mecanismo paramétrico de JUnit,
// habrá tantas llamadas al método de test como elementos y si hay varios errores
// se nos informará de cada uno de ellos 
// (no solo del primero como en cualquier otro test, que se corta en cuanto hay un fallo)

// Ejemplo de paso de test con repetitiva en lugar de paramétrica:
/*
	@Test
	public void testQuitarTabsYSaltosLineaConSecuenciaDePruebas() {
		String[][] tests = {
			{ "Hola\nEsto es un string con tres líneas\ny\tvarios\ttabuladores.", "Hola#Esto es un string con tres líneas#y|varios|tabuladores." }
			, { "Esto solo tiene\ttabs", "Esto solo tiene|tabs" }
			, { "Y esto no tiene líneas ni tabuladores", "Y esto no tiene líneas ni tabuladores" }
			, { "", "" } 
			, { null, null }  
		};
		for (int test=0; test<tests.length; test++) {
			assertEquals( tests[test][1], quitarTabsYSaltosLinea( tests[test][0] ));
		}
	}
*/
