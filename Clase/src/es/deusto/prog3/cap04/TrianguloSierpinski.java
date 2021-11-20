package es.deusto.prog3.cap04;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

@SuppressWarnings("serial")
public class TrianguloSierpinski extends JPanel {

	private static JSlider slider;
	private static TrianguloSierpinski panel;
    public static void main(String[] args) {
		JFrame f = new JFrame( "Triángulo de Sierpinski" );
		panel = new TrianguloSierpinski(8);
		f.getContentPane().add( panel, BorderLayout.CENTER );
		slider = new JSlider( 1, 10, 8 );
		slider.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				panel.setDepth( slider.getValue() );
				f.getContentPane().repaint();
			}
		});
		f.getContentPane().add( slider, BorderLayout.SOUTH );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize( 640, 520 );
		f.setVisible( true );
	}
    
	/* Offset de dibujado desde los laterales inferior y superior en pixels */
    public static int OFFSET = 25;
    
	private int depth;  // Profundidad recursiva
	
	/** Construye un panel que dibuja un triángulo de Sierpinski de profundidad recursiva 5
	 */
	public TrianguloSierpinski() {
		setBackground( Color.white );
	}
	
	/** Construye un panel que dibuja un triángulo de Sierpinski con la profundidad recursiva indicada
	 * @param depth	Profundidad de llamadas al dibujar el triángulo
	 */
	public TrianguloSierpinski( int depth ) {
		this();
		this.depth = depth;
	}
	
	public void setDepth( int depth ) {
		this.depth = depth;
	}

    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        // Inicializar p1, p2, p3 basados en el tamaño del frame
    	// En un equilátero h = sqrt(3) * base / 2
    	// o sea base = h * 2 / sqrt(3)
    	double h = getHeight() - OFFSET*2;
    	double base = Math.round( h * 2 / Math.sqrt(3.0) );
        Point p1 = new Point(); p1.setLocation( getWidth()/2, OFFSET );
        Point p2 = new Point(); p2.setLocation( getWidth()/2 - base/2, getHeight() - OFFSET);
        Point p3 = new Point(); p3.setLocation( getWidth()/2 + base/2, getHeight() - OFFSET);            
        // dibujar triángulo de Sierpinski
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setColor( Color.blue );
    	g2.setStroke( new BasicStroke( 1f ) );
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        drawTriangles(g2, depth, p1, p2, p3);
    }
    
    // dibuja una línea entre p1 y p2 en g
    private static void drawLine(Graphics2D g, Point p1, Point p2) {
        g.drawLine( (int)Math.round(p1.getX()), (int)Math.round(p1.getY()), (int)Math.round(p2.getX()), (int)Math.round(p2.getY()));
    }
    
    // devuelve el punto medio entre p1 y p2
    private static Point puntoMedio(Point p1, Point p2) {
        Point p = new Point(); p.setLocation( (p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
        return p;
    }

    // dibuja un triángulo de Sierpinski con los puntos p1,p2,p3, de profundidad depth
    private static void drawTriangles(Graphics2D g, int depth, Point p1, Point p2, Point p3) {
        if (depth == 0) {  // Caso base
            drawLine(g, p1, p2);
            drawLine(g, p1, p3);
            drawLine(g, p2, p3);
        } else {  // Caso recursivo  
            drawTriangles(g, depth - 1, p1, puntoMedio(p1,p2), puntoMedio(p3,p1));
            drawTriangles(g, depth - 1, puntoMedio(p1,p2), p2, puntoMedio(p2,p3));
            drawTriangles(g, depth - 1, puntoMedio(p3,p1), puntoMedio(p2,p3), p3);
        }
    }
    
}
