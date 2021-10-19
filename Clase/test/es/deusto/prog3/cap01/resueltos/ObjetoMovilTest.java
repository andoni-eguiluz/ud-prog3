package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.deusto.prog3.cap01.ejercicios.ObjetoMovil;

public class ObjetoMovilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// Test solo del hilo sin ventana
	@Test
	public void testVelocidadInicialYFinal() {
		ObjetoMovil.movil = new ObjetoMovil();
		ObjetoMovil.hiloMovimiento( 400, 1000, 0, 980.0 );
		try {
			ObjetoMovil.hilo.join();  // Espera a que acabe el hilo
		} catch (InterruptedException e) {}
		assertEquals( 1000.0, ObjetoMovil.movil.getVelY(), 10.0 );
	}

	// Test del hilo con la ventana
	@Test
	public void testVelocidadInicialYFinal2() {
		ObjetoMovil.movil = new ObjetoMovil();
		ObjetoMovil.hiloMovimiento( 400, 1000, 0, 980.0 );
		try {
			ObjetoMovil.hilo.join();  // Espera a que acabe el hilo
		} catch (InterruptedException e) {}
		assertEquals( 1000.0, ObjetoMovil.movil.getVelY(), 10.0 );
	}

}
