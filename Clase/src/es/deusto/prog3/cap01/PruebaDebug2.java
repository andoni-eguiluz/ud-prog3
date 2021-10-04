package es.deusto.prog3.cap01;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

/** Prueba de Debug
 * Utiliza el depurador (observa en los breakpoints la diferencia entre
 * parar la VM o parar solo el hilo)
 * para corregir este programa en tres cosas:
 * 1) Corregir el nullpointer (por qué boton da nullpointer si se crea el botón?)
 * 2) Que se redimensionen los componentes de la ventana según se cambien de tamaño
 *    (ahora no coge ninguno del mapa, aunque la lógica es buena... no?)
 * 2b) Por qué el botón y el cuadro de texto a veces no se ven al empezar?
 * 3) Que se redimensionen bien
 *    (no lo hacen, aunque la lógica es buena... no?)
 */
public class PruebaDebug2 {

	public static void main(String[] args) {
		reajusteLayoutNulo();
		// Al cabo de 10 segundos pulsa el botón para forzar el cierre de la ventana
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
		boton.doClick();
	}
	
	private static Rectangle tamanyoPanel = null;  // Variable para almacenar el tamaño actual de la ventana (y saberlo cuando cambie)
	private static HashMap<Object,Rectangle> tamComponentes = new HashMap<>();  // Mapa para almacenar el tamaño de los componentes
	private static JFrame miJFrame = null;  // Atributo para la ventana
	private static JButton boton = null;  // Atributo para el botón
	
	private static void reajusteLayoutNulo() {
		miJFrame = new JFrame(); miJFrame.setLocation( 0, 0 );
		JPanel panelPrincipal = new JPanel();
		miJFrame.getContentPane().add( panelPrincipal, BorderLayout.CENTER );  // El panel ocupa siempre toda la ventana y se reescala con ella
		miJFrame.setVisible( true );
		miJFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		miJFrame.setTitle ( "Ventana con reajuste por programa de tamaños en layout nulo" );
		miJFrame.setSize( 640, 480 );
		panelPrincipal.setLayout( null );
		// Añadimos un par de componentes de ejemplo para el tema del reajuste
			JLabel rectangulo = new JLabel( "" );
			rectangulo.setBorder( BorderFactory.createLineBorder( Color.red, 2 ));
			rectangulo.setSize( 100, 100 );
			rectangulo.setLocation( 200, 100 );
			panelPrincipal.add( rectangulo );
			JLabel texto = new JLabel( "Redimensiona la ventana y observa los cambios" );
			texto.setBounds( 20, 0, 300, 40 );
			panelPrincipal.add( texto );
			JTextField tfEjemplo = new JTextField( "ejemplo JTextField" );
			tfEjemplo.setBounds( 50, 150, 100, 30 );
			panelPrincipal.add( tfEjemplo );
			JButton boton = new JButton( "Cerrar!" );
			boton.setBounds( 200, 250, 100, 50 );
			panelPrincipal.add( boton );
		// Evento para el botón
		boton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				miJFrame.dispose();
			}
		});
		// Eventos para gestionar el reescalado
		// 1.- Guarda los tamaños al activar la ventana
		miJFrame.addWindowListener( new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {  // Al activarse la ventana almacenamos el tamaño del panel
				tamanyoPanel = panelPrincipal.getBounds();
				for (Component c : panelPrincipal.getComponents()) {
					tamComponentes.put( c, c.getBounds() );  // Guardamos el tamaño y posición inicial de cada componente para luego escalarlo con él
				}
			}
		});
		// 2.- Cambia los tamaños al redimensionar la ventana
		panelPrincipal.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {  // Al redimensionarse el panel, reajustamos sus componentes
				if (panelPrincipal!=null && tamanyoPanel!=null) {
					int escalaX = panelPrincipal.getWidth() / (int) tamanyoPanel.getWidth();   // Nueva escala X
					int escalaY = panelPrincipal.getHeight() / (int) tamanyoPanel.getHeight(); // Nueva escala Y
						// Calcula la escala del tamaño de panel anterior con respecto al actual
					for (Component c : panelPrincipal.getComponents()) { // Reescala cada componente:
						Rectangle tamanyoInicial = tamComponentes.get( c );  // Coge el tamaño que tiene ahora...
						if (tamanyoInicial!=null && c!=null) {
							c.setSize( new Dimension( (int) (tamanyoInicial.getWidth()*escalaX), (int)(tamanyoInicial.getHeight()*escalaY) ) );
								// Multiplica el tamaño por la escala
							c.setLocation( (int) (tamanyoInicial.getX()*escalaX), (int)(tamanyoInicial.getY()*escalaY) );
								// Y cambia la posición inicial utilizando la escala
						}
					}
				}
			}
		});
	}

	
}
