package es.deusto.prog3.cap04.ejemploAproxSuc;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/** Clase ventana sencilla para dibujado
 */
public class VentanaGrafica {
	private JFrame ventana;       // Ventana que se visualiza
	private boolean cerrada;      // Lógica de cierre (false al inicio)
	private JPanel panel;         // Panel principal
	private JLabel lMens;         // Etiqueta de texto de mensajes en la parte inferior
	private BufferedImage buffer; // Buffer gráfico de la ventana
	private Graphics2D graphics;  // Objeto gráfico sobre el que dibujar (del buffer)
	private Point pointPressed;   // Coordenada pulsada de ratón (si existe)
	private boolean dibujadoInmediato = true; // Refresco de dibujado en cada orden de dibujado

		private Object lock = new Object();  // Tema de sincronización de hilos para el acceso como si no los hubiera
	
	/** Construye una nueva ventana gráfica con fondo blanco y la visualiza en el centro de la pantalla
	 * @param anchura	Anchura en píxels (valor positivo)
	 * @param altura	Altura en píxels (valor positivo)
	 * @param titulo	Título de la ventana
	 */
	@SuppressWarnings("serial")
	public VentanaGrafica( int anchura, int altura, String titulo ) {
		cerrada = false;
		ventana = new JFrame( titulo );
		ventana.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		ventana.setSize( anchura, altura );
		ventana.setLocationRelativeTo( null );
		buffer = new BufferedImage( 2000, 1500, BufferedImage.TYPE_INT_ARGB );
		graphics = buffer.createGraphics();
		graphics.setPaint ( Color.white );
		graphics.fillRect ( 0, 0, 2000, 1500 );
		panel = new JPanel() {
			{
				setLayout( new BorderLayout() );
			}
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D)g).drawImage( buffer, null, 0, 0 );
			}
		};
		lMens = new JLabel( " " );
		ventana.getContentPane().add( panel, BorderLayout.CENTER );
		ventana.getContentPane().add( lMens, BorderLayout.SOUTH );
		ventana.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrada = true;
			}
		});
		panel.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				synchronized (lock) {
					pointPressed = null;
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				synchronized (lock) {
					pointPressed = e.getPoint();
				}
			}
		});
		panel.addMouseMotionListener( new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				synchronized (lock) {
					pointPressed = e.getPoint();
				}
			}
		});
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					ventana.setVisible( true );
				}
			});
		} catch (InvocationTargetException | InterruptedException e1) {
			e1.printStackTrace();
		}
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
	/** Cierra la ventana (también ocurre cuando se pulsa el icono de cierre)
	 */
	public void acaba() {
		ventana.dispose();
		cerrada = true;
	}
	
	/** Consultor de estado de visibilidad de la ventana
	 * @return	false si sigue activa, true si ya se ha cerrado
	 */
	public boolean estaCerrada() {
		return cerrada;
	}
	
	/** Devuelve el punto donde está el ratón pulsado en este momento
	 * @return	Punto relativo a la ventana, null si el ratón no está siendo pulsado
	 */
	public Point getRatonPulsado() {
		synchronized (lock) {
			return pointPressed;
		}
	}
	
	/** Cambia el mensaje de la ventana (línea inferior de mensajes)
	 * @param mensaje	Texto de mensaje
	 */
	public void setMensaje( String mensaje ) {
		if (mensaje==null || mensaje.isEmpty())
			lMens.setText( " " );
		else
			lMens.setText( mensaje );
	}
	
	/** Devuelve la altura del panel de dibujo de la ventana
	 * @return	Altura del panel principal (última coordenada y) en píxels
	 */
	public int getAltura() {
		return panel.getHeight()-1;
	}
	
	/** Devuelve la anchura del panel de dibujo de la ventana
	 * @return	Anchura del panel principal (última coordenada x) en píxels
	 */
	public int getAnchura() {
		return panel.getWidth()-1;
	}
	
	/** Borra toda la ventana (pinta de color blanco)
	 */
	public void borra() {
		graphics.setColor( Color.white );
		graphics.fillRect( 0, 0, panel.getWidth()+2, panel.getHeight()+2 );
		if (dibujadoInmediato) panel.repaint();
	}
	
	/** Dibuja un rectángulo en la ventana
	 * @param rectángulo a dibujar
	 * @param grosor	Grueso de la línea del rectángulo (en píxels)
	 * @param color  	Color del rectángulo
	 */
	public void dibujaRect( Rectangle rectangulo, float grosor, Color color ) {
		dibujaRect( rectangulo.getX(), rectangulo.getY(), rectangulo.getX()+rectangulo.getWidth(), rectangulo.getY()+rectangulo.getHeight(), grosor, color );
	}
	
	/** Dibuja un rectángulo en la ventana
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
		graphics.drawRect( (int)Math.round(x), (int)Math.round(y), (int)Math.round(anchura), (int)Math.round(altura) );
		if (dibujadoInmediato) panel.repaint();
	}
	
	/** Dibuja un rectángulo azul en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 */
	public void dibujaRect( double x, double y, double anchura, double altura, float grosor ) {
		dibujaRect( x, y, anchura, altura, grosor, Color.blue );
	}
	
	/** Borra un rectángulo en la ventana
	 * @param x	Coordenada x de la esquina superior izquierda del rectángulo
	 * @param y	Coordenada y de la esquina superior izquierda del rectángulo
	 * @param anchura	Anchura del rectángulo (en píxels) 
	 * @param altura	Altura del rectángulo (en píxels)
	 * @param grosor	Grueso del rectángulo (en píxels)
	 */
	public void borraRect( double x, double y, double anchura, double altura, float grosor ) {
		dibujaRect( x, y, anchura, altura, grosor, Color.white );
	}
	
	/** Dibuja un círculo en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 * @param color  	Color del círculo
	 */
	public void dibujaCirculo( double x, double y, double radio, float grosor, Color color ) {
		graphics.setColor( color );
		graphics.setStroke( new BasicStroke( grosor ));
		graphics.drawOval( (int)Math.round(x-radio), (int)Math.round(y-radio), (int)Math.round(radio*2), (int)Math.round(radio*2) );
		if (dibujadoInmediato) panel.repaint();
	}
	
	/** Dibuja un círculo azul en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 */
	public void dibujaCirculo( double x, double y, double radio, float grosor ) {
		dibujaCirculo( x, y, radio, grosor, Color.blue );
	}
	
	/** Borra un círculo en la ventana
	 * @param x	Coordenada x del centro del círculo
	 * @param y	Coordenada y del centro del círculo
	 * @param radio	Radio del círculo (en píxels) 
	 * @param grosor	Grueso del círculo (en píxels)
	 */
	public void borraCirculo( double x, double y, double radio, float grosor ) {
		dibujaCirculo( x, y, radio, grosor, Color.white );
	}
	
	/** Dibuja una línea en la ventana
	 * @param linea	a dibujar
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaLinea( Line2D linea, float grosor, Color color ) {
		dibujaLinea( linea.getX1(), linea.getY1(), linea.getX2(), linea.getY2(), grosor, color );
	}
	
	/** Dibuja una línea en la ventana
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
		graphics.drawLine( (int)Math.round(x), (int)Math.round(y), (int)Math.round(x2), (int)Math.round(y2) );
		if (dibujadoInmediato) panel.repaint();
	}
	
	/** Dibuja una línea azul en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea 
	 * @param y2	Coordenada y del segundo punto de la línea
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void dibujaLinea( double x, double y, double x2, double y2, float grosor ) {
		dibujaLinea( x, y, x2, y2, grosor, Color.blue );
	}
	
	/** Borra una línea en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea 
	 * @param y2	Coordenada y del segundo punto de la línea
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void borraLinea( double x, double y, double x2, double y2, float grosor ) {
		dibujaLinea( x, y, x2, y2, grosor, Color.white );
	}

	/** Dibuja una flecha en la ventana
	 * @param linea	a dibujar (el segundo punto es la punta de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 */
	public void dibujaFlecha( Line2D linea, float grosor, Color color ) {
		dibujaFlecha( linea.getX1(), linea.getY1(), linea.getX2(), linea.getY2(), grosor, color );
	}
	
	/** Dibuja una flecha en la ventana
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
	
	/** Dibuja una flecha en la ventana
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
		graphics.drawLine( (int)Math.round(x), (int)Math.round(y), (int)Math.round(x2), (int)Math.round(y2) );
		double angulo = Math.atan2( y2-y, x2-x ) + Math.PI;
		double angulo1 = angulo - Math.PI / 10;  // La flecha se forma rotando 1/10 de Pi hacia los dos lados
		double angulo2 = angulo + Math.PI / 10;
		graphics.drawLine( (int)Math.round(x2), (int)Math.round(y2), 
				(int)Math.round(x2+largoFl*Math.cos(angulo1)), (int)Math.round(y2+largoFl*Math.sin(angulo1)) );
		graphics.drawLine( (int)Math.round(x2), (int)Math.round(y2), 
				(int)Math.round(x2+largoFl*Math.cos(angulo2)), (int)Math.round(y2+largoFl*Math.sin(angulo2)) );
		if (dibujadoInmediato) panel.repaint();
	}
	
	/** Dibuja una línea azul en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void dibujaFlecha( double x, double y, double x2, double y2, float grosor ) {
		dibujaFlecha( x, y, x2, y2, grosor, Color.blue );
	}
	
	/** Borra una línea en la ventana
	 * @param x	Coordenada x de un punto de la línea 
	 * @param y	Coordenada y de un punto de la línea
	 * @param x2	Coordenada x del segundo punto de la línea (el de la flecha)
	 * @param y2	Coordenada y del segundo punto de la línea (el de la flecha)
	 * @param grosor	Grueso de la línea (en píxels)
	 */
	public void borraFlecha( double x, double y, double x2, double y2, float grosor ) {
		dibujaFlecha( x, y, x2, y2, grosor, Color.white );
	}
	
	
	
	
	
	
	
	/** Dibuja un polígono en la ventana
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
			graphics.drawLine( (int)Math.round(puntoAnt.getX()), (int)Math.round(puntoAnt.getY()), (int)Math.round(pto.getX()), (int)Math.round(pto.getY()) );
			puntoAnt = pto;
			numPto++;
		} while (numPto<punto.length);
		if (cerrado) {
			graphics.drawLine( (int)Math.round(pto.getX()), (int)Math.round(pto.getY()), (int)Math.round(puntoIni.getX()), (int)Math.round(puntoIni.getY()) );
		}
		if (dibujadoInmediato) panel.repaint();
	}
	
	/** Borra un polígono en la ventana
	 * @param grosor	Grueso de la línea (en píxels)
	 * @param color  	Color de la línea
	 * @param cerrado	true si el polígono se cierra (último punto con el primero), false en caso contrario
	 * @param punto		Puntos a borrar (cada punto se enlaza con el siguiente)
	 */
	public void borraPoligono( float grosor, boolean cerrado, Point2D... punto ) {
		dibujaPoligono( grosor, Color.white, cerrado, punto );
	}


	
	
	
	
	
	
	
	
	
	
	
	

	/** Devuelve el objeto de gráfico sobre el que pintar, correspondiente al 
	 * panel principal de la ventana. Después de actualizar graphics hay que llamar a {@link #repaint()}
	 * si se quiere que se visualice en pantalla
	 * @return	Objeto gráfico principal de la ventana
	 */
	public Graphics2D getGraphics() {
		return graphics;
	}
	
	/** Repinta la ventana. En caso de que el dibujado inmediato esté desactivado,
	 * es imprescindible llamar a este método para que la ventana gráfica se refresque.
	 */
	public void repaint() {
		panel.paintImmediately( 0, 0, panel.getWidth(), panel.getHeight() );
	}
	

	// Métodos estáticos
		private static int codTeclaTecleada = 0;
		private static int codTeclaActualmentePulsada = 0;
		private static boolean controlActivo = false;
	// Inicializa el control de teclado
	private static void init() {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher( new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					codTeclaActualmentePulsada = e.getKeyCode();
					if (e.getKeyCode() == KeyEvent.VK_CONTROL) controlActivo = true; 
				} else if (e.getID() == KeyEvent.KEY_RELEASED) {
					if (e.getKeyCode() == KeyEvent.VK_CONTROL) controlActivo = false; 
					codTeclaTecleada = e.getKeyCode();
					codTeclaActualmentePulsada = 0;
				} else if (e.getID() == KeyEvent.KEY_TYPED) {
				}
				return false;   // false = enviar el evento al comp
			} } );
	}
	static {  // Inicializar en la carta de la clase
		init();
	}

	/** Indica si la tecla Ctrl está siendo pulsada en este momento
	 * @return	true si está pulsada, false en caso contrario
	 */
	public boolean isControlPulsado() {
		return controlActivo;
	}
	
	/** Devuelve el código de la tecla pulsada actualmente
	 * @return	código de tecla pulsada, 0 si no hay ninguna. Si hay varias, se devuelve la última que se pulsó.<br/>
	 * 			Si se pulsan varias a la vez, tras soltar la primera este método devuelve 0.
	 */
	public int getCodTeclaQueEstaPulsada() {
		return codTeclaActualmentePulsada;
	}
	
	/** Devuelve el código de la última tecla tecleada (pulsada y soltada). Tras eso, borra la tecla (solo se devuelve una vez)
	 * @return	Código de la última tecla tecleada. Si no ha sido tecleada ninguna o ya se ha consultado, se devuelve 0.
	 */
	public int getCodUltimaTeclaTecleada() {
		int ret = codTeclaTecleada;
		codTeclaTecleada = 0;
		return ret;
	}

	/** Pone modo de dibujado (por defecto el modo es de dibujado inmediato = true)
	 * @param dibujadoInmediato	true si cada orden de dibujado inmediatamente pinta la ventana. false si se
	 * van acumulando las órdenes y se pinta la ventana solo al hacer un {@link #repaint()}.
	 */
	public void setDibujadoInmediato( boolean dibujadoInmediato ) {
		this.dibujadoInmediato = dibujadoInmediato;
	}

		// Variable local para guardar las imágenes y no recargarlas cada vez
		private static volatile HashMap<String,ImageIcon> recursosGraficos = new HashMap<>();
		
	/** Carga una imagen de un fichero gráfico y la dibuja en la ventana. Si la imagen no puede cargarse, no se dibuja nada.
	 * @param recursoGrafico	Nombre del fichero (path absoluto desde la carpeta raíz de clases del proyecto)  (p. ej. "/img/prueba.png")
	 * @param centroX	Coordenada x de la ventana donde colocar el centro de la imagen 
	 * @param centroY	Coordenada y de la ventana donde colocar el centro de la imagen
	 * @param anchuraDibujo	Pixels de anchura con los que dibujar la imagen (se escala de acuerdo a su tamaño)
	 * @param alturaDibujo	Pixels de altura con los que dibujar la imagen (se escala de acuerdo a su tamaño)
	 * @param zoom	Zoom a aplicar (mayor que 1 aumenta la imagen, menor que 1 y mayor que 0 la disminuye)
	 * @param radsRotacion	Rotación en radianes
	 * @param opacity	Opacidad (0.0f = totalmente transparente, 1.0f = totalmente opaca)
	 */
	public void dibujaImagen( String recursoGrafico, double centroX, double centroY, 
			int anchuraDibujo, int alturaDibujo, double zoom, double radsRotacion, float opacity ) {
		ImageIcon ii = recursosGraficos.get( recursoGrafico );
		if (ii==null) {
			try {
				ii = new ImageIcon( VentanaGrafica.class.getResource( recursoGrafico ));
			} catch (NullPointerException e) {
				return;
			}
			recursosGraficos.put( recursoGrafico, ii );
		}
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR); // Configuración para mejor calidad del gráfico escalado
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
		graphics.translate( centroX-anchuraDibujo/2, centroY-alturaDibujo/2 );
		graphics.rotate( radsRotacion, anchuraDibujo/2, alturaDibujo/2 );  // Incorporar al gráfico la rotación definida
		graphics.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) ); // Incorporar la transparencia definida
        int anchoDibujado = (int)Math.round(anchuraDibujo*zoom);  // Calcular las coordenadas de dibujado con el zoom, siempre centrado en el label
        int altoDibujado = (int)Math.round(alturaDibujo*zoom);
        int difAncho = (anchuraDibujo - anchoDibujado) / 2;  // Offset x para centrar
        int difAlto = (alturaDibujo - altoDibujado) / 2;     // Offset y para centrar
        graphics.drawImage( ii.getImage(), difAncho, difAlto, anchoDibujado, altoDibujado, null);  // Dibujar la imagen con el tamaño calculado tras aplicar el zoom
		graphics.setTransform( new AffineTransform() );  // Restaurar graphics  (sin rotación ni traslación)
		graphics.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ));  // Restaurar graphics (pintado sin transparencia)
		if (dibujadoInmediato) panel.repaint();
	}

	/** Carga una imagen de un fichero gráfico y la dibuja en la ventana. Si la imagen no puede cargarse, no se dibuja nada.
	 * @param recursoGrafico	Nombre del fichero (path absoluto desde la carpeta raíz de clases del proyecto)  (p. ej. "/img/prueba.png")
	 * @param centroX	Coordenada x de la ventana donde colocar el centro de la imagen 
	 * @param centroY	Coordenada y de la ventana donde colocar el centro de la imagen
	 * @param zoom	Zoom a aplicar (mayor que 1 aumenta la imagen, menor que 1 y mayor que 0 la disminuye)
	 * @param radsRotacion	Rotación en radianes
	 * @param opacity	Opacidad (0.0f = totalmente transparente, 1.0f = totalmente opaca)
	 */
	public void dibujaImagen( String recursoGrafico, double centroX, double centroY, 
			double zoom, double radsRotacion, float opacity ) {
		ImageIcon ii = recursosGraficos.get( recursoGrafico );
		if (ii==null) {
			try {
				ii = new ImageIcon( VentanaGrafica.class.getResource( recursoGrafico ));
			} catch (NullPointerException e) {
				return;
			}
			recursosGraficos.put( recursoGrafico, ii );
		}
		dibujaImagen( recursoGrafico, centroX, centroY, ii.getIconWidth(), ii.getIconHeight(), zoom, radsRotacion, opacity);
	}
	
		private transient JPanel pBotonera = null;
	/** Añade un botón de acción a la botonera superior
	 * @param texto	Texto del botón
	 * @param evento	Evento a lanzar en la pulsación del botón
	 */
	public void anyadeBoton( String texto, ActionListener evento ) {
		JButton b = new JButton( texto );
		if (pBotonera==null) {
			pBotonera = new JPanel();
			pBotonera.add( b );
			ventana.getContentPane().add( pBotonera, BorderLayout.NORTH );
			ventana.revalidate();
		} else {
			pBotonera.add( b );
			pBotonera.revalidate();
		}
		b.addActionListener( evento );
	}
	
		private static transient VentanaGrafica v = null;
	/** Método main de prueba de la clase
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		v = new VentanaGrafica( 600, 480, "Test Ventana Gráfica" );
		v.anyadeBoton( "Pon dibujado inmediato", new ActionListener() {  // Para ver cómo se ve con flickering si se dibujan cosas una a una
			@Override
			public void actionPerformed(ActionEvent e) {
				v.setDibujadoInmediato( true );
			}
		});
		v.setDibujadoInmediato( false );
		for (int i=0; i<=200; i++) {
			v.borra();
			v.dibujaImagen( "/utils/ventanas/ventanaJuego/img/" + "UD-green.png",
				100, 100, 0.5+i/200.0, Math.PI / 100 * i, 0.9f );
			v.dibujaImagen( "/utils/ventanas/ventanaJuego/img/" + "UD-magenta.png",
					500, 100, 100, 50, 1.2, Math.PI / 100 * i, 0.1f );
			v.dibujaRect( 20, 20, 160, 160, 0.5f, Color.red );
			v.dibujaRect( 0, 0, 100, 100, 1.5f, Color.blue );
			v.dibujaCirculo( 500, 100, 50, 1.5f, Color.orange );
			v.dibujaPoligono( 2.3f, Color.magenta, true, 
				new Point(200,250), new Point(300,320), new Point(400,220) );
			v.repaint();
			v.espera( 10 );
		}
		v.espera( 5000 );
		v.acaba();
	}
	
}
