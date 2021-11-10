package es.deusto.prog3.cap06;

import java.util.ArrayList;

import javax.swing.JFrame;

/** Clase para gestionar visibilización de ventanas de forma global.
 * Las ventanas se deben crear sin ser visibles, y añadirse al gestor
 *   con el método add( JFrame )
 * Se deben crear sin dispose automático al cierre 
 *   (para poderse visualizar varias veces).
 * Este método se ejecuta una única vez al inicio de la clase.
 * @author andoni
 */
public class GestorVentanas {

	/* Lista de ventanas. Gestión interna de todas las ventanas utilizadas */
	private static ArrayList<JFrame> listaVentanas = init();

	/** Inicializador de Ventanas.
	 * Podría crear e inicializar todas las ventanas del gestor.
	 * (o se van añadiendo con el método add)
	 * @return	ArrayList de todas las ventanas creadas
	 */
	private static ArrayList<JFrame> init() {
		ArrayList<JFrame> lista = new ArrayList<JFrame>();
		// Si queremos ventanas por defecto creadas se podrían poner aquí
		// lista.add( new Ventana... );
		return lista;
	}

	/** Añade una ventana al gestor
	 * @param vent
	 */
	public static void add( JFrame vent ) {
		listaVentanas.add( vent );
	}
	
	/** Libera y cierra todas las ventanas del gestor
	 */
	public static void closeAndDispose() {
		for (JFrame vent : listaVentanas) {
			vent.dispose();
		}
		listaVentanas.clear();
	}
	
	/** Hace visible la ventana indicada.
	 * Si hay algún error en los parámetros, no hace nada.
	 * @param ventanaAVisibilizar	Clase de la ventana a hacer visible
	 * @param ocultarElResto	Si true, oculta el resto. Si no, las deja como estuvieran
	 * @param numDeVentana	Si hay más de una ventana de la misma clase, índice de la ventana a visibilizar
	 */
	public static void hacerVisible(Class<?> ventanaAVisibilizar, boolean ocultarElResto, int numDeVentana ) {
		for (JFrame vent : listaVentanas) {
			if (vent.getClass().isAssignableFrom( ventanaAVisibilizar )) {
				// Si la clase de la ventana es igual o descendiente de la indicada
				if (numDeVentana > 0) {
					// Si no es la primera, esperar la siguiente
					numDeVentana--;
					if (ocultarElResto) vent.setVisible( false );
				} else if (numDeVentana == 0) {  // Si lo es, visibilizarla
					vent.setVisible( true );
					numDeVentana--;
				} else {
					vent.setVisible( false );
				}
			} else {
				if (ocultarElResto) vent.setVisible( false );
			}
		}
	}

	/** Oculta la ventana indicada.
	 * Si hay algún error en los parámetros, no hace nada.
	 * @param ventanaAOcultar	Clase de la ventana a hacer visible
	 * @param numDeVentana	Si hay más de una ventana de la misma clase, índice de la ventana a ocultar
	 */
	public static void ocultar(Class<?> ventanaAVisibilizar, int numDeVentana ) {
		for (JFrame vent : listaVentanas) {
			if (vent.getClass().isAssignableFrom( ventanaAVisibilizar )) {
				// Si la clase de la ventana es igual o descendiente de la indicada
				if (numDeVentana > 0) {
					// Si no es la primera, esperar la siguiente
					numDeVentana--;
				} else {  // Si lo es, visibilizarla
					vent.setVisible( false );
					break;
				}
			}
		}
	}

	
	/* Método de prueba */
	public static void main (String s[]) {
		add( new VentanaPrincipal() );
		add( new VentanaCreditos() );
		add( new VentanaOpciones() );
		add( new VentanaSeleccion() );
		add( new VentanaSeleccion() );
		try {
			hacerVisible( VentanaPrincipal.class, true, 0 );
			Thread.sleep( 3000 );
			hacerVisible( VentanaCreditos.class, true, 0 );
			Thread.sleep( 3000 );
			hacerVisible( VentanaOpciones.class, true, 0 );
			Thread.sleep( 3000 );
			hacerVisible( VentanaSeleccion.class, false, 0 );
			Thread.sleep( 3000 );
			hacerVisible( VentanaSeleccion.class, false, 1 );
			Thread.sleep( 3000 );
			ocultar( VentanaSeleccion.class, 0 );
			Thread.sleep( 3000 );
			closeAndDispose();
		} catch (InterruptedException e) {
		}
	}
}


/* Ejemplos de ventanas para la prueba */

@SuppressWarnings("serial")
class VentanaPrincipal extends JFrame {
	public VentanaPrincipal() {
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		setTitle( "Ventana Principal" );
		setSize( 600, 400 );
		setLocationRelativeTo(null);
	}
}
@SuppressWarnings("serial")
class VentanaCreditos  extends JFrame {
	public VentanaCreditos() {
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		setTitle( "Ventana de créditos" );
		setSize( 400, 300 );
	}
}
@SuppressWarnings("serial")
class VentanaOpciones  extends JFrame {
	public VentanaOpciones() {
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		setTitle( "Ventana de opciones" );
		setSize( 400, 300 );
		setLocationRelativeTo(null);
	}
}
@SuppressWarnings("serial")
class VentanaSeleccion extends JFrame {
	static int x = 100;
	static int y = 100;
	public VentanaSeleccion() {
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		setTitle( "Ventana de selección" );
		setSize( 300, 150 );
		setLocation(x, y);
		x += 100;
		y += 100;
	}
}
