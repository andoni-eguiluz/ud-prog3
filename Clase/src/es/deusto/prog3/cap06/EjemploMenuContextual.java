package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

/** Clase de prueba de menú contextual con botón derecho (clase JPopupMenu)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploMenuContextual {

	private static JPanel pPrincipal;
	public static void main(String[] args) {
		JFrame ventana = new JFrame( "Test de menú contextual lanzado con botón derecho" );
		ventana.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		ventana.setSize( 1000, 800 );
		pPrincipal = new JPanel();
		ventana.getContentPane().add( pPrincipal, BorderLayout.CENTER );
		JLabel lInst = new JLabel( "Pulsa botón derecho en el panel para ver menú", JLabel.CENTER );
		ventana.getContentPane().add( lInst, BorderLayout.NORTH );
		pPrincipal.addMouseListener( escuchadorBotonDerecho );
		ventana.setVisible( true );
	}

	// Clase que hereda de JPopupMenu para crear un menú personalizado
	private static class MiPopup extends JPopupMenu {
		public MiPopup() {
			JMenuItem item = new JMenuItem( "Cambiar color de fondo" );
			add( item );
			item.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Random r = new Random();
					pPrincipal.setBackground( new Color( r.nextInt(256), r.nextInt(256), r.nextInt(256) ) );
				}
			});
			item = new JMenuItem( "Pintar círculo" );
			add( item );
			item.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					pPrincipal.getGraphics().drawOval( coordPulsada.x-10, coordPulsada.y-10, 20, 20 );
					// Obsérvese cómo el pintado directo no puede pintar "debajo" del menú y queda tapado un trozo
				}
			});
			item = new JMenuItem( "Pintar punto" );
			add( item );
			item.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					pPrincipal.getGraphics().fillOval( coordPulsada.x-6, coordPulsada.y-6, 12, 12 );
					// Obsérvese cómo el pintado directo no puede pintar "debajo" del menú y queda tapado un trozo
				}
			});
		}
	}

	// Escuchador que añadir a todo componente en el que se quiera este menú de contexto
	private static class RightClickListener extends MouseAdapter {
		private MiPopup menu = new MiPopup();
	    public void mouseReleased(MouseEvent e) {
	        if (e.isPopupTrigger()) {
		        coordPulsada = e.getPoint();
		        menu.show(e.getComponent(), e.getX(), e.getY());
	        }
	    }
	    private void doPop(MouseEvent e) {
	    }
	}

	// Variables para el escuchador
	private static Point coordPulsada;
	private static RightClickListener escuchadorBotonDerecho = new RightClickListener();
	
}
