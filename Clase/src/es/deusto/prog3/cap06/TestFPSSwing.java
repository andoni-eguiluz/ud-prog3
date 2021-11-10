package es.deusto.prog3.cap06;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TestFPSSwing {
	private static boolean sigue = true;
	private static JFrame f;
	public static PanelFPS p;
	private static void init() {
		f = new JFrame();
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize( 1024,  768 );
		f.add( (p = new PanelFPS()) );
		f.setVisible( true );
		f.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				sigue = false;
			}
		});
	}
	private static void test1() {
		p.restartFPS();
		while (sigue) {
			p.repaint();
		}
	}
	private static void test2() {
		p.restartFPS();
		while (sigue) {
			try {
				SwingUtilities.invokeAndWait( new Runnable() {
					@Override
					public void run() {
						p.repaint();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static void test3() {
		p.restartFPS();
		while (sigue) {
			// Ojo que esto no lo hace el hilo de Swing!
			p.paintImmediately( 0, 0, p.getWidth(), p.getHeight() );
		}
	}
	
	public static void main(String[] args) {
		init();
		String opt = (String) JOptionPane.showInputDialog( f, "Elige tipo de prueba", "Prueba de FPS", JOptionPane.QUESTION_MESSAGE, null,
				new String[] { "Repaint cuando Swing quiere", "Repaint forzado con invokeAndWait", "Repaint immediately" },
				null );
		if (opt==null || opt.isEmpty()) { f.dispose(); return; }
		f.setTitle( opt );
		if (opt.startsWith( "Repaint cuando Swing" )) test1();
		else if (opt.startsWith( "Repaint forzado con invokeAndWait" )) test2();
		else if (opt.startsWith( "Repaint immediately" )) test3();
	}
	
	private static Random random = new Random();
	
	public static class PanelFPS extends JPanel {
		private long fps = 0;
		private long time = System.currentTimeMillis();
		private int x1 = random.nextInt(50);
		private int y1 = random.nextInt(100);
		private int x2 = 600 + random.nextInt(100);
		private int y2 = 400 + random.nextInt(50);
		private void restartFPS() {
			fps = 0;
			time = System.currentTimeMillis();
		}
		@Override
		protected void paintComponent(Graphics g) {
			fps++;
			super.paintComponent(g);
			g.drawLine( x1, y1, x2, y2 );
			x1 += (random.nextInt(5) - 2); if (x1<0) x1 = 0; else if (x1>getHeight()) x1 = getHeight();
			x2 += (random.nextInt(5) - 2); if (x2<0) x2 = 0; else if (x2>getHeight()) x2 = getHeight();
			y1 += (random.nextInt(5) - 2); if (y1<0) y1 = 0; else if (y1>getHeight()) y1 = getHeight();
			y2 += (random.nextInt(5) - 2); if (y2<0) y2 = 0; else if (y2>getHeight()) y2 = getHeight();
			g.setColor( Color.white );
			g.fillRect( 490, 0, 200, 30 );
			g.setColor( Color.blue );
			g.drawString( "FPS = " + String.format( "%1$7.1f", (fps * 1000.0 / (System.currentTimeMillis()-time))) + "  " + Thread.currentThread().getName(), 500, 20);
		}
	}

	
}
