package es.deusto.prog3.cap00.resueltos;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VentanaQuijoteTest {

	private VentanaQuijote v;
	@Before
	public void setUp() throws Exception {
		v = new VentanaQuijote();
		v.setLocation(2000, 0);
		v.setVisible( true );
		v.cargaQuijote();
	}

	@After
	public void tearDown() throws Exception {
		v.dispose();
	}

	@Test
	public void test() {
		v.bPagAbajo.doClick();
		// System.out.println( v );
		// System.out.println( v.spTexto.getVerticalScrollBar().getValue() );
		int vertical = v.spTexto.getVerticalScrollBar().getValue();
		for (int i=0; i<10; i++) {
			try {
				Thread.sleep( 500 );
			} catch (InterruptedException e) {
			}
			int verticalActual = v.spTexto.getVerticalScrollBar().getValue();
			assertTrue( verticalActual > vertical );
			vertical = verticalActual;
			// System.out.println( v.spTexto.getVerticalScrollBar().getValue() );
		}
		// System.out.println( v.spTexto.getVerticalScrollBar().getValue() );
	}

}
