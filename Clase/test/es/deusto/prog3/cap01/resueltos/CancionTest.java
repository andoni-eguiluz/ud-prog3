package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CancionTest {

	Cancion cancion1;
	Cancion cancion2;
	
	@Before
	public void setUp() throws Exception {
		cancion1 = new Cancion( "Bad Romance" );
		cancion2 = new Cancion( "Edge of Glory", 320 );
	}

//	@After
//	public void tearDown() throws Exception {
//	}

	@Test
	public void testConstructorNombre() {
		assertEquals( "Bad Romance", cancion1.nombre );
		Cancion cancion3 = new Cancion( "" );
		assertEquals( "", cancion3.nombre );
		Cancion cancion4 = new Cancion( null );
		assertEquals( "", cancion4.nombre );
	}
	
	@Test
	public void testConstructorNombreYDuracion() {
		assertEquals( "Edge of Glory", cancion2.nombre );
		assertEquals( 320, cancion2.getDuracionEnSegundos() );
		try {
			Cancion cancion3 = new Cancion( "Hola", 0 );
			assertEquals( 0, cancion3.getDuracionEnSegundos() );
		} catch (CancionException e) {
			fail( "Duración incorrecta pero es cero" );
		}
		try {
			Cancion cancion3 = new Cancion( "Hola", -5 );
			fail( "No salta excepción" );
		} catch (CancionException e) {
			// Pasa el test porque genera la excepción
		}
	}
	
	@Test
	public void testSetNombre() {
		cancion1.setNombre( "Poker Face" );
		assertEquals( "Poker Face", cancion1.nombre );
		// TODO null, ""
	}
	
	// TODO getters igual
	
	// TODO setDuracionEnSegundos igual
	
	@Test
	public void testGetDuracion() throws CancionException {
		cancion1.setDuracionEnSegundos( 45 );
		assertEquals( "00:00:45", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 60 );
		assertEquals( "00:01:00", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 62 );
		assertEquals( "00:01:02", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 123 );
		assertEquals( "00:02:03", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 3615 );
		assertEquals( "01:00:15", cancion1.getDuracion() );
		cancion1.setDuracionEnSegundos( 3600*102 );
		assertEquals( "102:00:00", cancion1.getDuracion() );
	}
	
	// Versión 2 del mismo test
	@Test
	public void testGetDuracion2() throws CancionException {
		int[] duracionSgs = { 45, 60, 62, 123, 3615, 3600*102 };
		String[] duracionHMS = { "00:00:45", "00:01:00", "00:01:02", "00:02:03", "01:00:15", "102:00:00" };
		for (int i=0; i<duracionSgs.length; i++) {
			cancion1.setDuracionEnSegundos( duracionSgs[i] );
			assertEquals( duracionHMS[i], cancion1.getDuracion() );
		}
	}
	
	@Test
	public void testGetVentanaCancion() {
		JFrame v = cancion2.getVentanaCancion();
		v.setVisible( true );
		// Título
		assertEquals( "Ventana canción", v.getTitle() );
		// Nombre de la canción
		assertEquals( cancion2.getNombre(), cancion2.tfNombre.getText() );
		// Cambio de nombre de la canción
		cancion2.tfNombre.setText( "Nombre nuevo" );
		try {
			cancion2.tfNombre.requestFocus();
			Robot robot = new Robot();
			robot.keyPress( KeyEvent.VK_ENTER );
			try { Thread.sleep( 50 ); } catch (InterruptedException e) {}
			robot.keyRelease( KeyEvent.VK_ENTER );
		} catch (AWTException e) {
			e.printStackTrace();
		} 
		assertEquals( "Nombre nuevo", cancion2.getNombre() );
		
		// Cambio de nombre en ventana
		cancion2.tfNombre.setText( "Nuevo nombre" );
		cancion2.tfNombre.requestFocus();
		try {
			Robot robot = new Robot();
			robot.keyPress( KeyEvent.VK_ENTER );
			robot.keyRelease( KeyEvent.VK_ENTER );
		} catch (AWTException e) {
			fail( "Error en simulación de teclado" );
		}
		try {  // Pausita
			Thread.sleep( 500 );
		} catch (InterruptedException e) {}
		assertEquals( "Nuevo nombre", cancion2.getNombre() );

		// Barra de progreso
		try {
			cancion2.setDuracionEnSegundos( 10 );
			// Simulación de botón
			cancion2.bSimular.doClick();
			// Pausita hasta mitad de simulación
			try { Thread.sleep( 5001 ); } catch (InterruptedException e) {}
			assertEquals( 5, cancion2.pbDuracion.getValue(), 1.0 ); // Más o menos a la mitad
			// Pausita al final de la simulación
			try { Thread.sleep( 5001 ); } catch (InterruptedException e) {}
			assertEquals( 10, cancion2.pbDuracion.getValue() ); // Al final
			// TODO ¿Por qué no funciona este test?  Hay que corregir el error en la clase  :-)
		} catch (CancionException e1) {
			fail( "Error en duración" );
		}
		
		// Pausita antes del cierre
		try { Thread.sleep( 1000 ); } catch (InterruptedException e) {}
		// Cierre
		v.dispose();
		
	}
	

}
