package es.deusto.prog3.cap04.ejemploAproxSuc;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// TAREA 1

public class NaveTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMueveUnPoco() {
		// Chequear movimiento horizontal
		Nave nave = new Nave( 100, 100, 20, 118, 100, Color.blue );
		for (int i=0; i<100; i++) {
			double antX = nave.getX();
			nave.mueveUnPoco( null, 10, false );
			assertTrue( antX < nave.getX() );
			assertEquals( nave.getY(), 100.0, 0.000000001 );
		}
		// Chequear movimiento vertical
		nave = new Nave( 100, 100, 20, 100, 120, Color.blue );
		for (int i=0; i<100; i++) {
			double antY = nave.getY();
			nave.mueveUnPoco( null, 10, false );
			assertTrue( antY < nave.getY() );
			assertEquals( nave.getX(), 100.0, 0.000000001 );
		}
		// Chequear movimiento diagonal positivo
		double velX = 10.0; double velY = 20.0; int milis = 10;
		nave = new Nave( 100, 100, 20, 100 + velX, 100 + velY, Color.blue );
		for (int i=0; i<100; i++) {
			double antX = nave.getX();
			double antY = nave.getY();
			nave.mueveUnPoco( null, milis, false );
			assertEquals( nave.getX() - antX, velX/1000.0*milis, 0.000001 );
			assertEquals( nave.getY() - antY, velY/1000.0*milis, 0.000001 );
		}
	}

}
