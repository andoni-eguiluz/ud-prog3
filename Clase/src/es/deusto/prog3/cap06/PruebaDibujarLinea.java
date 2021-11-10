package es.deusto.prog3.cap06;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;

@SuppressWarnings("serial")
public class PruebaDibujarLinea extends JPanel {
	private static int anchura = 0;
	private static int altura = 0;
	public PruebaDibujarLinea() {
		setBackground( Color.white );
		addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				anchura = getWidth();
				altura = getHeight();
			}
		} );
	}
	
	static PruebaDibujarLinea p = new PruebaDibujarLinea();
	private static JFrame f;
	public static void main(String[] args) {
		f = new JFrame();
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize( 800, 600 );
		f.setLocation( new Point(2000,0) ); // Cambia la posición de la ventana
		f.add( p, BorderLayout.CENTER );
		f.setVisible( true );
		/* 
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {  
					// Esto se ejecutará cuando toda la actividad de dibujado de swing 
					// haya acabado
				}
			});
		} catch (Exception e1) {}
		*/
		Graphics2D g = (Graphics2D) p.getGraphics();
		dibujarLinea( g, Color.black ); // Por qué esta no la dibuja?...
		try { Thread.sleep(100); } catch (InterruptedException e) {}  // Y si la pausa es menor?
		dibujarLinea( g, Color.blue ); // Y esta sí... pero relativamente...?
	}
	
	private static Random r = new Random();
	
	private static void dibujarLinea(Graphics2D g, Color c) {
		g.setColor( c );
		g.drawLine( r.nextInt(100), r.nextInt(100), anchura - r.nextInt(100), altura - r.nextInt(100));
	}
		int mePintan = 0;
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println( "Me pintan " + ++mePintan );
		dibujarLinea( (Graphics2D) g, Color.red ); // ...Sin embargo, esta no admite dudas!!
	}
	
}
