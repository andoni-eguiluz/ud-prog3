package es.deusto.prog3.cap00.resueltos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class PruebaJLabelGrafico extends JFrame {
	public static void main(String[] args) {
		PruebaJLabelGrafico vent = new PruebaJLabelGrafico();
		vent.setVisible( true );
		vent.mover();
	}
	
	// No static
	
	private JPanel pJuego;
	private JLabel lCoche;
	
	public PruebaJLabelGrafico() {
		// Configuración de la ventana
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 600, 400 );
		// setLocation( 2000, 0 );
		
		// Creamos los contenedores
		pJuego = new JPanel();
		pJuego.setLayout( null );
		
		// Creamos los componentes
		// lCoche = new JLabel( new ImageIcon( "src/es/deusto/prog3/cap00/resueltos/coche.png" ) );
		//try {
			lCoche = new MiCoche( new ImageIcon( PruebaJLabelGrafico.class.getResource("coche.png") ) );
		//} catch (MalformedURLException | URISyntaxException e) {
		//	e.printStackTrace();
		//}
		
		// Asociamos componentes a contenedores
		pJuego.add( lCoche );
		getContentPane().add( pJuego, BorderLayout.CENTER );
		// Gestión de eventos
		
	}
	
	private void mover() {
		// Hacer algo con el coche
//		lCoche.setSize( 100, 100 );
//		lCoche.setLocation( 100, 0 );
		lCoche.setBounds( 100, 0, 100, 100);
		
		for (int i=0; i<100; i++) {
			lCoche.setLocation( lCoche.getX()+1, lCoche.getY()+1 );
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}

	private class MiCoche extends JLabel {
		public MiCoche( ImageIcon i ) {
			super( i );
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// super.paintComponent(g);
			// g.drawLine( 0,  0,  100,  100 );
			Image img = ((ImageIcon)getIcon()).getImage();
			Graphics2D g2 = (Graphics2D) g;
			g2.rotate( 3 * Math.PI / 4, 50, 50 );
			g2.drawImage( img, 0, 0, 100, 100, null  );
		}
	}
	
}
