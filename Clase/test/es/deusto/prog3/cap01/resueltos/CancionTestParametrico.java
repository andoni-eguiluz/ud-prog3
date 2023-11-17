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

// 1.- RunWith para test paramétrico
@RunWith(Parameterized.class)
public class CancionTestParametrico {

	// 2.- Declarar una anotación @Parameters
	// que son los valores sobre los que repetir
	// Lista de arrays de Object (tantos como valores y resultados necesitemos para repetir) - 1 valor + 1 resultado
	@Parameters
	public static List<Object[]> datos() {
		return Arrays.asList(
			new Object[][] {
				{ 40, "00:00:40" },
				{ 60, "00:01:00" },
				{ 65, "00:01:05" }  // Falta poner todos los datos
			}
		);
	}
	// String[] duracionesF = { "00:00:40", "00:01:00", "00:01:05", "01:00:00", "00:59:59", "02:00:25", "02:30:05"   , "65:00:00", "00:00:00" };
	// int[] duracionesInt  = {         40,         60,         65,       3600,       3599,  2*3600+25,2*3600+30*60+5,  65*3600  ,  0         };

	// 3.- Dar nombre a cada uno de los datos con @Parameter(n)
	@Parameter(0)
	public int datoSegundos;
	
	@Parameter(1)
	public String esperado;
	
	// 4.- Hacer el test con las variables que acabamos de declarar
	
	@Test
	public void test() throws CancionException {
		Cancion cancion = new Cancion( "test" );
		cancion.setDuracionEnSegundos( datoSegundos );
		assertEquals( esperado, cancion.getDuracion() );
	}

}
