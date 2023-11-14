package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CancionTest {

	private Cancion cancion1;
	private Cancion cancion2;
	@Before
	public void setUp() throws Exception {
		cancion1 = new Cancion( "Bad Romance" );
		cancion2 = new Cancion( "Edge of Glory", 320 );
	}

	@Test
	public void testConstructorNombre() {
		assertEquals( "Bad Romance", cancion1.nombre );
		assertEquals( 0, cancion1.duracionEnSegundos );
		Cancion cancion3 = new Cancion( "" );
		assertEquals( "", cancion3.nombre );
		assertEquals( 0, cancion3.duracionEnSegundos );
		Cancion cancion4 = new Cancion( null );
		assertEquals( "", cancion4.nombre );
		assertEquals( 0, cancion4.duracionEnSegundos );
	}

	@Test
	public void testConstructorNombreYDuracion() {
		assertEquals( "Edge of Glory", cancion2.nombre );
		assertEquals( 320, cancion2.duracionEnSegundos );
		Cancion cancion3 = null;
		try {
			cancion3 = new Cancion( "Poker Face", -5 );
			fail( "Excepción en constructor" );
		} catch (CancionException e) {
			// No tiene sentido assertEquals( "Poker Face", cancion3.nombre );
			// Ok
		}
		try {
			Cancion cancion4 = new Cancion( "Poker Face", 0 );
			assertEquals( "Poker Face", cancion4.nombre );
			assertEquals( 0, cancion4.duracionEnSegundos );
			// Ok
		} catch (CancionException e) {
			fail( "Excepción no debería lanzarse" );
		}
		try {
			Cancion cancion5 = new Cancion( "Poker Face", 999999 );
			assertEquals( "Poker Face", cancion5.nombre );
			assertEquals( 999999, cancion5.duracionEnSegundos );
			// Ok
		} catch (CancionException e) {
			fail( "Excepción no debería lanzarse" );
		}
	}
	
	@Test
	public void testSetNombre() {
		cancion1.setNombre( "Poker Face" );
		assertEquals( "Poker Face", cancion1.nombre );
		cancion1.setNombre( null );
		assertEquals( "Poker Face", cancion1.nombre );
		cancion1.setNombre( "" );
		assertEquals( "", cancion1.nombre );
	}
	
	@Test
	public void testGetNombre() {
		assertEquals( "Bad Romance", cancion1.getNombre() );
		assertEquals( cancion1.nombre, cancion1.getNombre() );
	}
	
	@Test
	public void getDuracionString() throws CancionException {
		cancion1.setDuracionEnSegundos( 40 );
		assertEquals( "00:00:40", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 60 );
		assertEquals( "00:01:00", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 65 );
		assertEquals( "00:01:05", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 3600 );
		assertEquals( "01:00:00", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 3599 );
		assertEquals( "00:59:59", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 2*3600 + 25 );
		assertEquals( "02:00:25", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 2*3600 + 30*60 + 5 );
		assertEquals( "02:30:05", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 65*3600 );
		assertEquals( "65:00:00", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 0 );
		assertEquals( "00:00:00", cancion1.getDuracion() );
	}
	
	
}
