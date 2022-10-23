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
		ObjetoMovil.movil.setPosY( 1000 );
		long tiempoInicio = System.currentTimeMillis();
		ObjetoMovil.hiloMovimiento( 400, -1000, 0, 980.0 );
		while (ObjetoMovil.movil.getVelY()<=0) { // Mientras está subiendo... esperar
			// System.out.println( ObjetoMovil.movil.getPosX() + "," + ObjetoMovil.movil.getPosY() + " --> " + ObjetoMovil.movil.getVelY() );
			try { Thread.sleep(10);} catch (InterruptedException e) {}
		}
		long tiempoSubida = System.currentTimeMillis();
		try {
			ObjetoMovil.hilo.join();  // Espera a que acabe el hilo
		} catch (InterruptedException e) {}
		long tiempoFinal = System.currentTimeMillis();
		double subida = tiempoSubida-tiempoInicio;
		double bajada = tiempoFinal-tiempoSubida;
		assertEquals( (double) subida, (double) bajada, 100.0 );   // Error de 1 décima de segundo
		assertEquals( 1000.0, ObjetoMovil.movil.getVelY(), 20.0 ); // Error de 20 píxels por segundo
	}

}
