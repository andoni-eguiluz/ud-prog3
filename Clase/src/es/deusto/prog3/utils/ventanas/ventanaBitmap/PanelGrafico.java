package es.deusto.prog3.utils.ventanas.ventanaBitmap;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;

/** Clase panel con dibujado directo y memorización de bitmap
 */
@SuppressWarnings("serial")
public class PanelGrafico extends JPanel {
	
	// ====================================================
	//   Parte estática - pruebas de funcionamiento
	// ====================================================

	private static JFrame v;
	private static PanelGrafico pg;
	private static JLabel lMensaje;
	/** Método main de prueba de la clase
	 * @param args	No utilizado 
	 */
	public static void main(String[] args) {
		v = new JFrame( "Test panel gráfico" );
		v.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		v.setSize( 800, 600 );
		JPanel pSuperior = new JPanel();
		JButton bDibInm = new JButton( "Pon/quita dibujado inmediato" );
		pSuperior.add( bDibInm );
		v.add( pSuperior, BorderLayout.NORTH );
		lMensaje = new JLabel( " " );
		v.add( lMensaje, BorderLayout.SOUTH );
		pg = new PanelGrafico( 800, 600 );
		v.add( pg, BorderLayout.CENTER );
		bDibInm.addActionListener( new ActionListener() {  // Para ver cómo se ve con flickering si se dibujan cosas una a una
			@Override
			public void actionPerformed(ActionEvent e) {
				pg.setDibujadoInmediato( !pg.isDibujadoInmediato() );
				lMensaje.setText( "Dibujado inmediato está " + (pg.isDibujadoInmediato() ? "ACTIVADO" : "DESACTIVADO") );
			}
		});
		pg.setDibujadoInmediato( false );
		v.setVisible( true );
//		pg.borra();
//		pg.dibujaTexto( 100, 100, "texto móvil", new Font( "Arial", Font.PLAIN, 16 ), Color.black );
		Object opcion = JOptionPane.showInputDialog( v, "¿Qué quieres probar?",
				"Selección de test", JOptionPane.QUESTION_MESSAGE, null, 
				new String[] { "Movimiento", "Giros", "Tiro", "Zoom" }, "Movimiento" );
		if ( "Movimiento".equals( opcion ) ) {
			movimiento();
		} else if ( "Giros".equals( opcion ) ) {
			giros();
		} else if ( "Tiro".equals( opcion ) ) {
			tiro();
		} else if ( "Zoom".equals( opcion ) ) {
			zoom();
		}
	}

	// Prueba 1: escudo verde que se mueve y sube y baja
	private static void movimiento() {
		int altura = pg.getAltura();
		boolean subiendo = true;
		for (int i=0; i<=800; i++) {
			pg.borra();
			pg.dibujaTexto( i+100, 100+(i/4), "texto móvil", new Font( "Arial", Font.PLAIN, 16 ), Color.black );
			pg.dibujaImagen( "/tema3/ejemplos/runner/img/UD-green.png", i, altura, 1.0, 0.0, 1.0f );
			if (subiendo) {
				altura--;
				if (altura<=0) subiendo = false;
			} else {
				altura++;
				if (altura>=pg.getAltura()) subiendo = true;
			}
			pg.repaint();
			pg.espera( 10 );
		}
		pg.espera( 5000 );
		v.dispose();
	}
	
	// Prueba 2: escudos y formas girando y zoomeando
	private static void giros() {
		for (int i=0; i<=1000; i++) {
			pg.borra();
			pg.dibujaImagen( "/tema3/ejemplos/runner/img/UD-green.png", 100, 100, 0.5+i/200.0, Math.PI / 100 * i, 0.9f );
			pg.dibujaImagen( "/utils/ventanas/ventanaBitmap/img/UD-magenta.png", 500, 100, 100, 50, 1.2, Math.PI / 100 * i, 0.1f );
			pg.dibujaRect( 20, 20, 160, 160, 0.5f, Color.red );
			pg.dibujaRect( 0, 0, 100, 100, 1.5f, Color.blue );
			pg.dibujaCirculo( 500, 100, 50, 1.5f, Color.orange );
			pg.dibujaPoligono( 2.3f, Color.magenta, true, 
				new Point(200,250), new Point(300,320), new Point(400,220) );
			pg.repaint();
			pg.espera( 10 );
		}
		pg.espera( 5000 );
		v.dispose();
	}

	private static boolean disparando = false;
	// Prueba 3: tiro parabólico
	private static void tiro() {
		double xLanz = 20;
		double yLanz = pg.getAltura()-20;
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (disparando) return;  // No hace nada si está el tiro en curso
				// Se hace click: disparar!
				pg.borra();
				pg.dibujaCirculo( xLanz, yLanz, 10, 3.0f, Color.MAGENTA );
				disparar( xLanz, yLanz, e.getPoint().getX(), e.getPoint().getY() );
			}
			@Override
			public void mouseMoved(MouseEvent e) { // Se hace movimiento: dibujar flecha
				if (disparando) return;  // No hace nada si está el tiro en curso
				pg.borra();
				pg.dibujaCirculo( xLanz, yLanz, 10, 3.0f, Color.MAGENTA );
				pg.dibujaFlecha( xLanz, yLanz, e.getPoint().getX(), e.getPoint().getY(), 2.0f, Color.GREEN, 25 );
				pg.repaint();
			}
		};
		lMensaje.setText( "Click ratón para disparar (con fuerza y ángulo)");
		pg.addMouseListener( ma );
		pg.addMouseMotionListener( ma );
		pg.borra();
	}
		// Hace un disparo con la velocidad marcada por el vector con gravedad
		private static void disparar( double x1, double y1, double x2, double y2 ) {
			(new Thread() {
				@Override
				public void run() {
					disparando = true;
					double velX = x2-x1; double velY = y2-y1;
					double G = 9.8; // Aceleración de la gravedad
					dispararVA( x1, y1, velX, velY, G );
				}
			}).start();
		}
		// Hace un disparo con la velocidad y ángulo indicados
		private static void dispararVA( double x1, double y1, double velX, double velY, double acel ) {
			lMensaje.setText( "Calculando disparo con velocidad (" + velX + "," + velY + ")" );
			double x = x1; double y = y1;  // Punto de disparo
			int pausa = 10; // msg de pausa de animación
			double tempo = pausa / 1000.0; // tiempo entre frames de animación (en segundos)
			do {
				pg.dibujaCirculo( x, y, 1.0, 1.0f, Color.blue );  // Dibuja punto
				x = x + velX * tempo; // Mueve x (según la velocidad)
				y = y + velY * tempo;// Mueve y (según la velocidad)
				velY = velY + acel * 10 * tempo; // Cambia la velocidad de y (por efecto de la gravedad)
				pg.repaint();
				pg.espera( pausa ); // Pausa 20 msg (aprox 50 frames/sg)
			} while (y<y1);  // Cuando pasa hacia abajo la vertical se para
			pg.espera( 2000 ); // Pausa de 2 segundos
			lMensaje.setText( "Vuelve a disparar!" );
			disparando = false;
		}
	
	private static boolean ctrlPulsado = false;
	private static Point puntoInicioDrag = null;
	private static boolean clickEnOrigen = false;
	private static double zoom = 1.0;
	// Prueba 4: zoom y movimiento de referencia del panel
	private static void zoom() {
		boolean sigue = true;
		int offset = 0;
		boolean bajando = true;
		pg.setFocusable( true );
		pg.requestFocus();
		pg.addKeyListener( new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				ctrlPulsado = false;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				ctrlPulsado = true;
			}
		});
		
		// Primera animación con zoom
		while (sigue) {
			dibujoEjemplo( false );
			while (ctrlPulsado) pg.espera(10);  // Con Ctrl se pausa la animación
			pg.espera( 10 );
			if (offset<200) {  // Amplía el offset hasta 200,200
				offset += 4;
				pg.setOffsetDibujo( new Point( offset, offset ) );
			} else if (bajando) {
				zoom *= 0.99; // Va bajando el zoom
				pg.setEscalaDibujo( zoom );
				if (zoom < 0.5) {  // A partir de 0.5 empieza a subir
					bajando = false; 
				}
			}
			if (!bajando) {
				zoom *= 1.01; // Va subiendo el zoom
				pg.setEscalaDibujo( zoom );
				if (zoom > 3.0) {  // Al llegar a 3 se para el programa
					sigue = false;
				}
			}
			lMensaje.setText( "Offset " + offset + " - zoom " + zoom );
		}

		// Segunda animación con ratón - moviendo y escalando
		zoom = pg.getEscalaDibujo();
		MouseAdapter ma = new MouseAdapter() {
			Point antDrag = null;
			@Override
			public void mousePressed(MouseEvent e) {
				puntoInicioDrag = e.getPoint();
				antDrag = e.getPoint();
				clickEnOrigen = false;
				Point2D.Double puntoClick = pg.getPuntoEnEscala( e.getPoint() );
				if (Math.abs(puntoClick.x)*pg.getEscalaDibujo()<5 && Math.abs(puntoClick.y)*pg.getEscalaDibujo()<5) {
					clickEnOrigen = true;
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				String mens = String.format( "  (click en punto anterior (%4.2f,%4.2f)", pg.getPuntoEnEscala(e.getPoint()).x, pg.getPuntoEnEscala(e.getPoint()).y );
				int xOffset = pg.getOffsetDibujo().x;
				int yOffset = pg.getOffsetDibujo().y;
				pg.setEjeYInvertido( !pg.getEjeYInvertido() );
				pg.setOffsetDibujo( new Point( xOffset, pg.getAltura() - yOffset ) );
				lMensaje.setText( "Eje Y invertido = " + (pg.getEjeYInvertido() ? "SI" : "NO" ) + mens + ", nuevo " + String.format( "(%4.2f,%4.2f)", pg.getPuntoEnEscala(e.getPoint()).x, pg.getPuntoEnEscala(e.getPoint()).y ) + ")");
				dibujoEjemplo( true );
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if (clickEnOrigen) {
					int distX = e.getPoint().x-puntoInicioDrag.x; // Solo se considera el drag en la x (derecha crecer, izquierda decrecer)
					double incZoom;
					if (distX>=0) {
						incZoom = 1.0 + distX/100.0;  // 100 pixels a la derecha duplica
					} else {
						incZoom = 1.0 + distX/500.0;  // 500 pixels a la derecha minimiza al máximo
					}
					double nuevoZoom = zoom * incZoom;
					if (nuevoZoom < 0.01) nuevoZoom = 0.01;  // Mínimo zoom
					pg.setEscalaDibujo( nuevoZoom );
					dibujoEjemplo( true );
					lMensaje.setText( "Zoom = " + nuevoZoom );
					zoom = nuevoZoom;
				} else if (antDrag!=null) {
					pg.setOffsetDibujo( e.getPoint().x-antDrag.x, e.getPoint().y-antDrag.y );
					dibujoEjemplo( true );
					antDrag = e.getPoint();
				}
			}
		};
		lMensaje.setText( "Ratón para cambio de escala y posición");
		pg.addMouseListener( ma );
		pg.addMouseMotionListener( ma );
		dibujoEjemplo( true );
	}
		private static void dibujoEjemplo( boolean conMensaje ) {
			pg.borra();
			pg.dibujaEjes( 100, Color.BLACK, 1.0f );
			pg.dibujaLinea( -100, 50, 800, 50, 0.5f, Color.CYAN );
			pg.dibujaTexto( 100, 50, "texto dibujado", new Font( "Arial", Font.PLAIN, 16 ), Color.black );
			pg.dibujaRect( 100, 100, 200, 50, 0.5f, Color.ORANGE );
			pg.dibujaTextoCentrado( 100, 100, 200, 50, "texto centrado entre 100 y 300", new Font( "Arial", Font.PLAIN, 16 ), Color.green, true );
			pg.dibujaImagen( "img/UD-green.png", 400, 150, 1.0, Math.PI/6.0, 0.4f );
			pg.dibujaCirculo( 400, 150, 100.0, 1.0f, Color.BLACK );
			pg.dibujaFlecha( 100, 350, 300, 550, 2.0f, Color.MAGENTA, 10 );
			pg.dibujaCirculo( 400, 350, 50.0, 2.0f, Color.BLUE, Color.YELLOW );
			pg.dibujaPoligono( 3.0f, Color.GREEN, true, new Point( 100, 400 ), new Point( 400, 410 ), new Point( 380, 440 ), new Point( 110, 450 ) );
			pg.dibujaRect( 550, 350, 120,  60,  2.0f, Color.MAGENTA, Color.CYAN );
			if (conMensaje) pg.dibujaTexto( 10, -20, "Drag para desplazar, drag sobre origen para zoom, Click inversión eje Y", new Font( "Arial", Font.PLAIN, 16 ), Color.RED );
			pg.repaint();
		}

		
	
	// ====================================================
	//   Parte no estática - objeto PanelGrafico
	// ====================================================
	
	private BufferedImage buffer;   // Buffer gráfico del panel
	private Graphics2D graphics;    // Objeto gráfico sobre el que dibujar (del buffer)
	private boolean dibujadoInmediato = true; // Refresco de dibujado en cada orden de dibujado
	private Point offsetInicio = new Point( 0, 0 );  // Offset de inicio de coordenadas ((0,0) por defecto)
	private double escalaDibujo = 1.0;               // Escala de dibujado (1=1 píxeles por defecto)
	private boolean ejeYInvertido = true;            // Eje Y invertido con respecto a la representación matemática clásica (por defecto true -crece hacia abajo-)
	private Color colorFondo = Color.white;          // El color de fondo
	
	/** Construye un nuevo panel gráfico con fondo blanco
	 * @param anchura	Anchura en píxels (valor positivo) de la zona de pintado
	 * @param altura	Altura en píxels (valor positivo) de la zona de pintado
	 */
	public PanelGrafico( int anchura, int altura ) {
		this(anchura, altura, Color.WHITE );
	}
	
	/** Construye un nuevo panel gráfico
	 * @param anchura	Anchura en píxels (valor positivo) de la zona de pintado
	 * @param altura	Altura en píxels (valor positivo) de la zona de pintado
	 * @param colorDeFondo  El color de fondo del panel
	 */
	public PanelGrafico(int anchura, int altura, Color colorDeFondo) {
		this.colorFondo = colorDeFondo;
		buffer = new BufferedImage( anchura, altura, BufferedImage.TYPE_INT_ARGB );
		graphics = buffer.createGraphics();
		graphics.setPaint( colorFondo );
		graphics.fillRect( 0, 0, anchura, altura );
	}

	/** Espera un tiempo y sigue
	 * @param milis	Milisegundos a esperar
	 */
	public void espera( long milis ) {
		try {
			Thread.sleep( milis );
		} catch (InterruptedException e) {
		}
	}
	
	/** Devuelve la altura del panel gráfico
	 * @return	Altura del panel gráfico en píxels
	 */
	public int getAltura() {
		return getHeight();
	}
	
	/** Devuelve la anchura del panel gráfico
	 * @return	Anchura del panel gráfico en píxels
	 */
	public int getAnchura() {
		return getWidth();
	}
	
	/** Borra todo el panel (pinta de color de fondo)
	 */
	public void borra() {
		graphics.setColor( colorFondo );
		graphics.fillRect( 0, 0, getWidth()+2, getHeight()+2 );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un rectángulo en el panel
	 * @param rectangulo	Rectángulo a dibujar
	 * @param grosor	Grueso de la línea del rectángulo (en píxels)
	 * @param color  	Color del rectángulo
	 */
	public void dibujaRect( Rectangle rectangulo, float grosor, Color color ) {
		dibujaRect( rectangulo.getX(), rectangulo.getY(), rectangulo.getX()+rectangulo.getWidth(), rectangulo.getY()+rectangulo.getHeight(), grosor, color );
	}
	
	/** Dibuja un rectángulo en el panel
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 * @param color  	Color del rectángulo
	 */
	public void dibujaRect( double x, double y, double anchura, double altura, float grosor, Color color ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		if (ejeYInvertido)
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		else
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		if (dibujadoInmediato) repaint();
	}

		// Convierte x de coordenadas propuestas a coordenadas visuales (con zoom y desplazamiento)
		private double calcX( double x ) {
			return offsetInicio.x + x * escalaDibujo;
		}
		// Convierte y de coordenadas propuestas a coordenadas visuales (con zoom y desplazamiento)
		private double calcY( double y ) {
			return offsetInicio.y + (ejeYInvertido?1.0:-1.0) * y * escalaDibujo;
		}

	/** Dibuja un rectángulo relleno en el panel
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 * @param color  	Color de la línea del rectángulo
	 * @param colorRell	Color del relleno del rectángulo
	 */
	public void dibujaRect( double x, double y, double anchura, double altura, float grosor, Color color, Color colorRell ) {
		graphics.setColor( colorRell );
		graphics.setStroke( new BasicStroke( grosor ));
		if (ejeYInvertido)
			graphics.fillRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		else
			graphics.fillRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		graphics.setColor( color );
		if (ejeYInvertido)
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		else
			graphics.drawRect( (int)Math.round(calcX(x)), (int)Math.round(calcY(y))-(int)Math.round(altura*escalaDibujo), (int)Math.round(anchura*escalaDibujo), (int)Math.round(altura*escalaDibujo) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un rectángulo azul en el panel
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 */
	public void dibujaRect( double x, double y, double anchura, double altura, float grosor ) {
		dibujaRect( x, y, anchura, altura, grosor, Color.blue );
	}
	
	/** Borra un rectángulo en el panel
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 */
	public void borraRect( double x, double y, double anchura, double altura, float grosor ) {
		dibujaRect( x, y, anchura, altura, grosor, colorFondo );
	}
	
	/** Dibuja un círculo relleno en el panel
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 * @param color  	Color del círculo
	 * @param colorRelleno  	Color de relleno del círculo
	 */
	public void dibujaCirculo( double x, double y, double radio, float grosor, Color color, Color colorRelleno ) {
		graphics.setStroke( new BasicStroke( grosor ));
		graphics.setColor( colorRelleno );
		graphics.fillOval( (int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo) );
		graphics.setColor( color );
		graphics.drawOval( (int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un círculo en el panel
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 * @param color  	Color del círculo
	 */
	public void dibujaCirculo( double x, double y, double radio, float grosor, Color color ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		graphics.drawOval( (int)Math.round(calcX(x)-radio*escalaDibujo), (int)Math.round(calcY(y)-radio*escalaDibujo), (int)Math.round(radio*2*escalaDibujo), (int)Math.round(radio*2*escalaDibujo) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un círculo azul en el panel
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 */
	public void dibujaCirculo( double x, double y, double radio, float grosor ) {
		dibujaCirculo( x, y, radio, grosor, Color.blue );
	}
	
	/** Borra un círculo en el panel
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 */
	public void borraCirculo( double x, double y, double radio, float grosor ) {
		dibujaCirculo( x, y, radio, grosor, colorFondo );
	}
	
	/** Dibuja una línea en el panel
	 * @param linea	a dibujar
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaLinea( Line2D linea, float grosor, Color color ) {
		dibujaLinea( linea.getX1(), linea.getY1(), linea.getX2(), linea.getY2(), grosor, color );
	}
	
	/** Dibuja una línea en el panel
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea 
	 * @param y2	Coordenada y del segundo punto de la línea
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaLinea( double x, double y, double x2, double y2, float grosor, Color color ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		graphics.drawLine( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja una línea azul en el panel
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea 
	 * @param y2	Coordenada y del segundo punto de la línea
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void dibujaLinea( double x, double y, double x2, double y2, float grosor ) {
		dibujaLinea( x, y, x2, y2, grosor, Color.blue );
	}
	
	/** Borra una línea en el panel
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea 
	 * @param y2	Coordenada y del segundo punto de la línea
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void borraLinea( double x, double y, double x2, double y2, float grosor ) {
		dibujaLinea( x, y, x2, y2, grosor, colorFondo );
	}

	/** Dibuja una flecha en el panel
	 * @param linea	a dibujar (el segundo punto es la punta de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaFlecha( Line2D linea, float grosor, Color color ) {
		dibujaFlecha( linea.getX1(), linea.getY1(), linea.getX2(), linea.getY2(), grosor, color );
	}
	
	/** Dibuja una flecha en el panel
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaFlecha( double x, double y, double x2, double y2, float grosor, Color color ) {
		dibujaFlecha( x, y, x2, y2, grosor, color, 10 );
	}
	
	/** Dibuja una flecha en el panel
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 * @param largoFl	Pixels de largo de la flecha
	 */
	public void dibujaFlecha( double x, double y, double x2, double y2, float grosor, Color color, int largoFl ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		graphics.drawLine( (int)Math.round(calcX(x)), (int)Math.round(calcY(y)), (int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)) );
		double angulo = Math.atan2( y2-y, x2-x ) + Math.PI;
		if (!ejeYInvertido) angulo = Math.atan2( y-y2, x2-x ) + Math.PI;
		double angulo1 = angulo - Math.PI / 10;  // La flecha se forma rotando 1/10 de Pi hacia los dos lados
		double angulo2 = angulo + Math.PI / 10;
		graphics.drawLine( (int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)),
				(int)Math.round(calcX(x2)+largoFl*Math.cos(angulo1)), (int)Math.round(calcY(y2)+largoFl*Math.sin(angulo1)) );
		graphics.drawLine( (int)Math.round(calcX(x2)), (int)Math.round(calcY(y2)), 
				(int)Math.round(calcX(x2)+largoFl*Math.cos(angulo2)), (int)Math.round(calcY(y2)+largoFl*Math.sin(angulo2)) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja una línea azul en el panel
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void dibujaFlecha( double x, double y, double x2, double y2, float grosor ) {
		dibujaFlecha( x, y, x2, y2, grosor, Color.blue );
	}
	
	/** Borra una línea en el panel
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void borraFlecha( double x, double y, double x2, double y2, float grosor ) {
		dibujaFlecha( x, y, x2, y2, grosor, colorFondo );
	}
	
	/** Dibuja un polígono en el panel
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 * @param cerrado	true si el polígono se cierra (último punto con el primero), false en caso contrario
	 * @param punto		Puntos a dibujar (cada punto se enlaza con el siguiente)
	 */
	public void dibujaPoligono( float grosor, Color color, boolean cerrado, Point2D... punto ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		if (punto.length<2) return;
		Point2D puntoIni = punto[0];
		Point2D puntoAnt = punto[0];
		Point2D pto = null;
		int numPto = 1;
		do {
			pto = punto[numPto];
			graphics.drawLine( (int)Math.round(calcX(puntoAnt.getX())), (int)Math.round(calcY(puntoAnt.getY())), (int)Math.round(calcX(pto.getX())), (int)Math.round(calcY(pto.getY())) );
			puntoAnt = pto;
			numPto++;
		} while (numPto<punto.length);
		if (cerrado) {
			graphics.drawLine( (int)Math.round(calcX(pto.getX())), (int)Math.round(calcY(pto.getY())), (int)Math.round(calcX(puntoIni.getX())), (int)Math.round(calcY(puntoIni.getY())) );
		}
		if (dibujadoInmediato) repaint();
	}
	
	/** Borra un polígono en el panel
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param cerrado	true si el polígono se cierra (último punto con el primero), false en caso contrario
	 * @param punto		Puntos a borrar (cada punto se enlaza con el siguiente)
	 */
	public void borraPoligono( float grosor, boolean cerrado, Point2D... punto ) {
		dibujaPoligono( grosor, colorFondo, cerrado, punto );
	}

	/** Dibuja un texto en el panel
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param texto	Texto a dibujar 
	 * @param font	Tipo de letra con el que dibujar el texto
	 * @param color	Color del texto
	 */
	public void dibujaTexto( double x, double y, String texto, Font font, Color color ) {
		graphics.setColor( color );
		graphics.setFont( font );
		graphics.drawString( texto, (int)Math.round(calcX(x)), (int)Math.round(calcY(y)) );
		if (dibujadoInmediato) repaint();
	}
	
	/** Dibuja un texto en el panel, centrado en un rectángulo dado
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo
	 * @param altura	Altura del rectángulo
	 * @param texto	Texto a dibujar 
	 * @param font	Tipo de letra con el que dibujar el texto
	 * @param color	Color del texto
	 */
	public void dibujaTextoCentrado( double x, double y, double anchura, double altura, String texto, Font font, Color color ) {
		Rectangle2D rect = graphics.getFontMetrics(font).getStringBounds(texto, graphics);  // Dimensiones del texto que se va a pintar
		graphics.setColor( color );
		graphics.setFont( font );
		double xCalc = calcX(x) + anchura/2.0*escalaDibujo - rect.getWidth()/2.0;
		double yCalc = calcY(y) + altura*escalaDibujo - graphics.getFontMetrics(font).getDescent() - (altura*escalaDibujo - rect.getHeight())/2.0;
		graphics.drawString( texto, (float)calcX(xCalc), (float)calcY(yCalc) );
		if (dibujadoInmediato) repaint();
	}

	/** Dibuja un texto en el panel, centrado en un rectángulo dado
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo
	 * @param altura	Altura del rectángulo
	 * @param texto	Texto a dibujar 
	 * @param font	Tipo de letra con el que dibujar el texto
	 * @param color	Color del texto
	 * @param ajustaSiMayor	true si se disminuye el texto hasta que quepa (si no cabe), false se dibuja con el tamaño indicado.
	 */
	public void dibujaTextoCentrado( double x, double y, double anchura, double altura, String texto, Font font, Color color, boolean ajustaSiMayor ) {
		Rectangle2D rect = graphics.getFontMetrics(font).getStringBounds(texto, graphics);  // Dimensiones del texto que se va a pintar
		while (rect.getWidth() > anchura*escalaDibujo || rect.getHeight() > altura*escalaDibujo) {
			font = new Font( font.getName(), font.getStyle(), font.getSize() - 1 );
			rect = graphics.getFontMetrics(font).getStringBounds(texto, graphics);  // Dimensiones del texto que se va a pintar
		}
		graphics.setColor( color );
		graphics.setFont( font );
		double xCalc = calcX(x) + anchura/2.0*escalaDibujo - rect.getWidth()/2.0;
		double yCalc = calcY(y) + (ejeYInvertido?1.0:-1.0)*(altura*escalaDibujo - graphics.getFontMetrics(font).getDescent() - (altura*escalaDibujo - rect.getHeight())/2.0) + (ejeYInvertido?0.0:rect.getHeight()/2);
		// Vista del cálculo de y (invertida):
		//   Inicio texto y     
		//                |
		//                |
		//                |               -altura en blanco/2  ^
		//                |   -descent  ^                      |
		//       +altura  v             |
		graphics.drawString( texto, (float)xCalc, (float)yCalc );
		if (dibujadoInmediato) repaint();
	}
	
	/** Devuelve el objeto de gráfico sobre el que pintar, correspondiente al 
	 * panel. Después de actualizar graphics hay que llamar a {@link #repaint()}
	 * si se quiere que se visualice en pantalla
	 * @return	Objeto gráfico principal del panel
	 */
	public Graphics2D getGraphics() {
		return graphics;
	}
	
	/** Devuelve el buffer gráfico del panel
	 * @return	Buffer gráfico que se dibuja en el panel
	 */
	public BufferedImage getBuffer() {
		return buffer;
	}
	
	/** Devuelve el objeto gráfico en el que dibujar con persistencia
	 * @return	Objeto gráfico de dibujado
	 */
	public Graphics2D getGraphicsPers() {
		return graphics;
	}
	
	/** Devuelve el objeto gráfico en el que dibujar sin persistencia (no se mantiene al refrescar)
	 * @return	Objeto gráfico de dibujado
	 */
	public Graphics2D getGraphicsSinPers() {
		return (Graphics2D) super.getGraphics();
	}
	
	@Override
	public void repaint() {
		super.repaint();
		if (getParent()!=null) getParent().repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D)g).drawImage( buffer, null, 0, 0 );
	}
	
	/** Repinta el panel de forma inmediata. En caso de que el dibujado inmediato esté desactivado,
	 * es imprescindible llamar a este método para que el panel gráfico se refresque.
	 */
	public void pintadoInmediato() {
		getGraphics().drawImage( buffer, null, 0, 0 );
		paintImmediately( 0, 0, getWidth(), getHeight() );
	}

	/** Pone modo de dibujado (por defecto el modo es de dibujado inmediato = true)
	 * @param dibujadoInmediato	true si cada orden de dibujado inmediatamente pinta el panel. false si se
	 * van acumulando las órdenes y se pinta el panel solo al hacer un {@link #repaint()}.
	 */
	public void setDibujadoInmediato( boolean dibujadoInmediato ) {
		this.dibujadoInmediato = dibujadoInmediato;
	}

	/** Informa del modo de dibujado (por defecto el modo es de dibujado inmediato = true)
	 * @return true si cada orden de dibujado inmediatamente pinta el panel. false si se
	 * van acumulando las órdenes y se pinta el panel solo al hacer un {@link #repaint()}.
	 */
	public boolean isDibujadoInmediato() {
		return dibujadoInmediato;
	}

		// Variable local para guardar las imágenes y no recargarlas cada vez
		private static volatile HashMap<String,ImageIcon> recursosGraficos = new HashMap<>();
		
	/** Carga una imagen de un fichero gráfico y la dibuja en el panel. Si la imagen no puede cargarse, no se dibuja nada.
	 * El recurso gráfico se busca en el paquete de esta clase o en la clase llamadora.
	 * El recurso gráfico se carga en memoria, de modo que al volver a dibujar la misma imagen, no se vuelve a cargar ya de fichero
	 * @param recursoGrafico	Nombre del fichero (path absoluto desde la carpeta raíz de clases del proyecto o relativo desde este paquete)  (p. ej. "img/prueba.png")
	 * @param centroX	Coordenada x del panel donde colocar el centro de la imagen 
	 * @param centroY	Coordenada y del panel donde colocar el centro de la imagen
	 * @param anchuraDibujo	Pixels de anchura con los que dibujar la imagen (se escala de acuerdo a su tamaño)
	 * @param alturaDibujo	Pixels de altura con los que dibujar la imagen (se escala de acuerdo a su tamaño)
	 * @param zoom	Zoom a aplicar (mayor que 1 aumenta la imagen, menor que 1 y mayor que 0 la disminuye)
	 * @param radsRotacion	Rotación en radianes
	 * @param opacity	Opacidad (0.0f = totalmente transparente, 1.0f = totalmente opaca)
	 */
	public void dibujaImagen( String recursoGrafico, double centroX, double centroY, 
			int anchuraDibujo, int alturaDibujo, double zoom, double radsRotacion, float opacity ) {
		ImageIcon ii = getRecursoGrafico(recursoGrafico); if (ii==null) return;
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR); // Configuración para mejor calidad del gráfico escalado
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
		graphics.translate( calcX(centroX)-anchuraDibujo/2*escalaDibujo, calcY(centroY)-alturaDibujo/2*escalaDibujo );
		graphics.rotate( radsRotacion, anchuraDibujo/2*escalaDibujo, alturaDibujo/2*escalaDibujo );  // Incorporar al gráfico la rotación definida
		graphics.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) ); // Incorporar la transparencia definida
        int anchoDibujado = (int)Math.round(anchuraDibujo*zoom*escalaDibujo);  // Calcular las coordenadas de dibujado con el zoom, siempre centrado en el label
        int altoDibujado = (int)Math.round(alturaDibujo*zoom*escalaDibujo);
        int difAncho = (int) ((anchuraDibujo* escalaDibujo - anchoDibujado) / 2);  // Offset x para centrar
        int difAlto = (int) ((alturaDibujo* escalaDibujo - altoDibujado) / 2);     // Offset y para centrar
        // graphics.drawRect( difAncho, difAlto, anchoDibujado, altoDibujado );
        graphics.drawImage( ii.getImage(), difAncho, difAlto, anchoDibujado, altoDibujado, null);  // Dibujar la imagen con el tamaño calculado tras aplicar el zoom
		graphics.setTransform( new AffineTransform() );  // Restaurar graphics  (sin rotación ni traslación)
		graphics.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ));  // Restaurar graphics (pintado sin transparencia)
		if (dibujadoInmediato) repaint();
	}

	/** Carga una imagen de un fichero gráfico y la dibuja en el panel. Si la imagen no puede cargarse, no se dibuja nada.
	 * El recurso gráfico se busca en el paquete de esta clase o en la clase llamadora.
	 * El recurso gráfico se carga en memoria, de modo que al volver a dibujar la misma imagen, no se vuelve a cargar ya de fichero
	 * @param recursoGrafico	Nombre del fichero (path absoluto desde la carpeta raíz de clases del proyecto o relativo desde este paquete)  (p. ej. "img/prueba.png")
	 * @param centroX	Coordenada x del panel donde colocar el centro de la imagen 
	 * @param centroY	Coordenada y del panel donde colocar el centro de la imagen
	 * @param zoom	Zoom a aplicar (mayor que 1 aumenta la imagen, menor que 1 y mayor que 0 la disminuye)
	 * @param radsRotacion	Rotación en radianes
	 * @param opacity	Opacidad (0.0f = totalmente transparente, 1.0f = totalmente opaca)
	 */
	public void dibujaImagen( String recursoGrafico, double centroX, double centroY, 
			double zoom, double radsRotacion, float opacity ) {
		ImageIcon ii = getRecursoGrafico(recursoGrafico); if (ii==null) return;
		dibujaImagen( recursoGrafico, centroX, centroY, ii.getIconWidth(), ii.getIconHeight(), zoom, radsRotacion, opacity);
	}
		// Intenta cargar el recurso gráfico del mapa interno, de la clase actual, o de la clase llamadora. Devuelve null si no se ha podido hacer
		private ImageIcon getRecursoGrafico( String recursoGrafico ) {
			ImageIcon ii = recursosGraficos.get( recursoGrafico );
			if (ii==null) {
				try {
					ii = new ImageIcon( PanelGrafico.class.getResource( recursoGrafico ));
					recursosGraficos.put( recursoGrafico, ii );
				} catch (NullPointerException e) {  // Mirar si está en la clase llamadora en lugar de en el panel gráfico
					StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
					for (int i=1; i<stElements.length; i++) {
						StackTraceElement ste = stElements[i];
						if ( !ste.getClassName().endsWith("PanelGrafico") ) {  // Busca la clase llamadora a PanelGrafico y busca ahí el recurso
							try {
								Class<?> c = Class.forName( ste.getClassName() );
								URL url = c.getResource( recursoGrafico );
								if (url==null) return null;
								ii = new ImageIcon( url );
								recursosGraficos.put( recursoGrafico, ii );
								return ii;
							} catch (ClassNotFoundException e1) {
								return null;
							}
						}
					}
					return null;
				}			
			}
			return ii;
		}
	
	/** Muestra un cuadro de diálogo encima del panel y espera a que el usuario lo pulse
	 * @param titulo	Título del diálogo
	 * @param mensaje	Texto a mostrar
	 */
	public void sacaDialogo( String titulo, String mensaje ) {
		JOptionPane.showMessageDialog( this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE );
	}
	
	/** Muestra un cuadro de diálogo de selección de color encima del panel y permite al usuario seleccionar un color
	 * @param mens	Mensaje de título del diálogo
	 * @return	Color seleccionado, null si no se ha seleccionado ninguno
	 */
	public Color eligeColor( String mens ) {
		JColorChooser jcc = new JColorChooser();
		int resp = JOptionPane.showConfirmDialog( this, jcc, mens, JOptionPane.OK_CANCEL_OPTION );
		return (resp==JOptionPane.OK_OPTION) ? jcc.getColor() : null;
	}
	
	/** Modifica el offset de las coordenadas del panel (moviendo el punto de origen 0,0 al indicado)
	 * @param offset	Punto de offset nuevo de dibujado (los cambios no tendrán efecto hasta la siguiente operación de dibujado)
	 */
	public void setOffsetDibujo( Point offset ) {
		offsetInicio = offset;
	}
	
	/** Modifica el offset de las coordenadas del panel (moviendo el punto de origen 0,0 al indicado)
	 * @param incrX	Incremento de x de offset
	 * @param incrY	Incremento de y de offset
	 */
	public void setOffsetDibujo( int incrX, int incrY ) {
		offsetInicio.translate( incrX, incrY );
	}

	/** Consulta el offset de las coordenadas del panel
	 * @return	Punto actual de offset de dibujado
	 */
	public Point getOffsetDibujo() {
		return offsetInicio;
	}
	
	/** Modifica la escala de dibujado del panel
	 * @param escala	Nueva escala (1.0 escala 1=1). Por ejemplo con 0.5 el dibujo se escala a la mitad de tamaño, con 2.0 se escala al doble de tamaño
	 */
	public void setEscalaDibujo( double escala ) {
		this.escalaDibujo = escala;
	}
	
	/** Devuelve la escala de dibujado del panel
	 * @return Escala de dibujado (1.0 escala 1=1)
	 */
	public double getEscalaDibujo() {
		return escalaDibujo;
	}
	
	/** Modifica la inversión del eje Y. Actualiza el offset en Y siempre al borde de panel actual (superior o inferior).
	 * @param invertido	true para invertido con respecto a matemáticas clásicas (crece hacia abajo), false para habitual (crece hacia arriba)
	 */
	public void setEjeYInvertido( boolean invertido ) {
		if (ejeYInvertido) {  // Estaba creciente hacia abajo (lo habitual en pantalla) y hay que ponerlo hacia arriba
			offsetInicio.y = getAltura();
		} else {  // Estaba creciente hacia arriba (lo habitual en mates) y hay que ponerlo hacia abajo
			offsetInicio.y = 0;
		}
		ejeYInvertido = invertido;
	}
	
	/** Informa sobre la inversión del eje Y.
	 * @return	true indica eje invertido con respecto a matemáticas clásicas (crece hacia abajo), false para habitual (crece hacia arriba)
	 */
	public boolean getEjeYInvertido() {
		return ejeYInvertido;
	}
	
	/** Establece el color de fondo
	 * @param color El color de fondo a establecer
	 */
	public void setColorFondo(Color color) {
		colorFondo = color;
		graphics.setPaint(color);
	}
	
	/** Devuelve el color de fondo
	 * @return El color de fondo actual
	 */
	public Color getColorFondo() {
		return colorFondo;
	}
	
	/** Devuelve un punto del panel del panel convertido a coordenadas de offset y escala
	 * @param punto	Punto lógico en píxels visuales del panel
	 * @return	Ese punto convertido a coordenadas de acuerdo al zoom y offset actual
	 */
	public Point2D.Double getPuntoEnEscala( Point punto ) {
		double x = punto.x-offsetInicio.x;
		double y = punto.y-offsetInicio.y;
		if (!ejeYInvertido) {
			y = -y;
		}
		return new Point2D.Double( x/escalaDibujo, y/escalaDibujo );
	}
	
	/** Dibuja los ejes de coordenadas
	 * @param marcaUnidad	Número positivo en el que se marca la unidad del eje (se dibujará con una flecha)
	 * @param colorEjes	Color de los ejes
	 * @param grosor	Grosor de los ejes
	 */
	public void dibujaEjes( int marcaUnidad, Color colorEjes, float grosor ) {
		dibujaLinea( 0, -marcaUnidad*100, 0, marcaUnidad*100, grosor, colorEjes );
		dibujaLinea( -marcaUnidad*100, 0, marcaUnidad*100, 0, grosor, colorEjes );
		dibujaCirculo( 0, 0, 2/escalaDibujo, 1.0f, colorEjes, colorEjes);
		dibujaFlecha( 0, 0, 0, marcaUnidad, grosor, colorEjes, 7 );
		dibujaFlecha( 0, 0, marcaUnidad, 0, grosor, colorEjes, 7 );
	}
	
}
