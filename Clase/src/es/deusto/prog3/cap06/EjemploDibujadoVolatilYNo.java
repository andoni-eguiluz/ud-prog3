package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/** Prueba de dibujado interactivo en Swing para ver qué es volátil y qué no
 * El panel de la izquierda es un panel normal, el de la derecha es un objeto BufferedImage
 * Prueba a dibujar en ambos y luego redimensionar o mover fuera y dentro la ventana ¿qué pasa?
 * @author andoni.eguiluz at ingenieria.deusto.es
 */
public class EjemploDibujadoVolatilYNo {

	private static JPanel dibujoIzquierda = new JPanel();
	private static BufferedImage dibujoDerecha = new BufferedImage( 500, 800, BufferedImage.TYPE_INT_ARGB );  // RGB + alfa
	private static JPanelDibujo panelDerecha = new JPanelDibujo( dibujoDerecha );
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setTitle( "Dibuja en los dos paneles!" );
		// Panel de la izquierda
		dibujoIzquierda.setPreferredSize( new Dimension( 500, 800 ) );
		dibujoIzquierda.setBackground( Color.white );
		f.add( dibujoIzquierda, BorderLayout.WEST );
		// Panel de la derecha
		dibujoDerecha.getGraphics().setColor( Color.white );
		dibujoDerecha.getGraphics().fillRect( 0, 0, 500, 800 );
		panelDerecha.setPreferredSize( new Dimension( 500, 800 ) );
		f.add( panelDerecha, BorderLayout.EAST );
		// Separación entre paneles
		f.add( new JLabel( " " ), BorderLayout.CENTER );  
		// Eventos
		dibujoIzquierda.addMouseMotionListener( new Dibujador( dibujoIzquierda ) );
		panelDerecha.addMouseMotionListener( new Dibujador( dibujoDerecha ) );
		// Redimensión y visualización
		f.pack();
		f.setVisible( true );
	}
	
	private static class Dibujador extends MouseMotionAdapter {
		Object dondeDibujar;
		public Dibujador( Object dondeDibujar ) {
			this.dondeDibujar = dondeDibujar;
		}
		Point inicio;
		@Override
		public void mouseMoved(MouseEvent e) {
			inicio = e.getPoint();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (inicio!=null) {
				Graphics2D g2 = null;
				if (dondeDibujar instanceof JPanel) {
					JPanel p = (JPanel) dondeDibujar;
					g2 = (Graphics2D) p.getGraphics();
				} else if (dondeDibujar instanceof BufferedImage) {
					BufferedImage bi = (BufferedImage) dondeDibujar;
					g2 = (Graphics2D) bi.getGraphics();
				}
				if (g2!=null) {
					g2.setColor( Color.blue );
					g2.drawLine( inicio.x, inicio.y, e.getX(), e.getY() );
					if (dondeDibujar instanceof BufferedImage) {
						panelDerecha.repaint();
					}
				}
			}
			inicio = e.getPoint();
		}
	}
	
	@SuppressWarnings("serial")
	private static class JPanelDibujo extends JPanel {
		private BufferedImage bi;
		public JPanelDibujo( BufferedImage bi ) {
			this.bi = bi;
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage( bi, 0, 0, getWidth(), getHeight(), null );
		}
	}
	
}
