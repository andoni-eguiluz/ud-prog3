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
	public void testGetDuracionString() throws CancionException {
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
	
	@Test 
	public void testGetDuracionString2() throws CancionException {
		String[] duracionesF = { "00:00:40", "00:01:00", "00:01:05", "01:00:00", "00:59:59", "02:00:25", "02:30:05"   , "65:00:00", "00:00:00" };
		int[] duracionesInt  = {         40,         60,         65,       3600,       3599,  2*3600+25,2*3600+30*60+5,  65*3600  ,  0         };
		for (int i=0; i<duracionesF.length; i++) {
			pruebaGetDuracionString( duracionesInt[i], duracionesF[i] );
		}
	}
	
	private void pruebaGetDuracionString( int numSegundos, String duracionFormateada ) throws CancionException {
		cancion1.setDuracionEnSegundos( numSegundos );
		assertEquals( duracionFormateada, cancion1.getDuracion() );
	}

	// Ventanas o trabajos en tiempo real
	@Test
	public void testGetVentanaCancion() {
		JFrame v = cancion2.getVentanaCancion();
		v.setVisible( true );
		// Título
		assertEquals( "Ventana canción", v.getTitle() );
		// Componentes: nombre de canción, duración, botón, barra de progreso
		assertEquals( "Edge of Glory", cancion2.tfNombre.getText() );
		assertEquals( cancion2.getNombre(), cancion2.tfNombre.getText() );
		assertEquals( "Simular", cancion2.bSimular.getText() );
		assertEquals( cancion2.getDuracion(), cancion2.lDuracion.getText() );
		assertEquals( 0, cancion2.pbDuracion.getValue() );
		
		// Cambio interactivo de nombre de canción
		cancion2.tfNombre.setText( "Shallow" );
		try {
			cancion2.tfNombre.requestFocus();
			Robot robot = new Robot();
			robot.keyPress( KeyEvent.VK_ENTER );
			try { Thread.sleep( 50 ); } catch (InterruptedException e) { }
			robot.keyRelease( KeyEvent.VK_ENTER );
			try { Thread.sleep( 50 ); } catch (InterruptedException e) { }
			assertEquals( "Shallow", cancion2.getNombre() );
		} catch (AWTException e) {
			fail( "Robot no posible" );
		}
		
		// Barra de progreso
		try {
			cancion2.setDuracionEnSegundos( 10 );
			// Simulación de botón
			cancion2.bSimular.doClick();
			// Pausita hasta mitad de simulación
			try { Thread.sleep( 5100 ); } catch (InterruptedException e) {}  // 5,1 sgs (un pelín de margen)
			assertEquals( 5, cancion2.pbDuracion.getValue(), 1.0 ); // Más o menos a la mitad
			// Pausita al final de la simulación
			try { Thread.sleep( 5100 ); } catch (InterruptedException e) {}  // 5,1 sgs (un pelín de margen)
			assertEquals( 10, cancion2.pbDuracion.getValue() ); // Al final
			assertFalse( cancion2.pbDuracion.isEnabled() ); // Al final
			// TODO ¿Por qué no funciona este test de pbDuracion?  Hay que corregir el error en la clase  :-)
		} catch (CancionException e1) {
			fail( "Error en duración" );
		}
		
		// Pausita antes del cierre
		try { Thread.sleep( 100 ); } catch (InterruptedException e) {}
		// Cierre
		v.dispose();
		
		
	}
	
}
