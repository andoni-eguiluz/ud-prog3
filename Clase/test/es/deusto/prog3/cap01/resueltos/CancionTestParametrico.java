package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

// Características específicas de un test paramétrico:
// 0: Esta anotación informa a JUnit de que este test tiene parte paramétrica

@RunWith(Parameterized.class)  
public class CancionTestParametrico {
	
	// 1: Hay que declarar una anotación @Parameters
	//  Declarar los valores sobre los que hay que repetir
	//  Lista de arrays de Object
	// { 45, 60, 62, 123, 3615, 3600*102 };
	// { "00:00:45", "00:01:00", "00:01:02", "00:02:03", "01:00:15", "102:00:00" };
	@Parameters
	public static List<Object[]> datos() {
		return Arrays.asList(
				new Object[][] {
					{ 45, "00:00:45" },
					{ 60, "00:01:00" },
					{ 62, "00:01:02" }  // Añadir el resto
				}
		);
	}
	
	// 2: Nombres de los datos con @Parameter(n)  (0 a n-1)
	@Parameter(0)
	public int inicial;
	
	@Parameter(1)
	public String esperado;

	// 3: Hacer el test con esos nombres de datos
	
	@Test
	public void test() throws CancionException {
		Cancion cancion = new Cancion( "test" );
		cancion.setDuracionEnSegundos( inicial );
		assertEquals( esperado, cancion.getDuracion() );
	}

}
