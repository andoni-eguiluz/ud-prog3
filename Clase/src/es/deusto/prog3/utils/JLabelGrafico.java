package es.deusto.prog3.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

/** Clase mejorada de JLabel para gestionar imágenes ajustadas al JLabel, con escala, rotación y transparencia
 * (la escala se relaciona con el tamaño del label para que no se recorte al cambiar de tamaño. Sí se puede recortar con la rotación)
 */
public class JLabelGrafico extends JLabel {
	
	/** Método de prueba de label gráfico
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame( "Prueba JLabelGrafico" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// f.setLocation( 2000, 100 );   // Si se quiere cambiar la posición
		f.getContentPane().setLayout( null );
		JLabel label = new JLabel( new ImageIcon( "src/es/deusto/prog3/cap00/coche.png" ) );  // JLabel normal - como fichero
		label.setBounds( 0, 0, 150, 150 );
		JLabelGrafico labelGrafico = new JLabelGrafico( "/es/deusto/prog3/cap00/coche.png", 100, 100 );  // JLabelGrafico - como recurso
		labelGrafico.setLocation( 400, 100 );
			// TODO probar este 300, 300 con diferentes tamaños. Si x<=0 ajusta el ancho y si es y<=0 ajusta el alto
		f.setSize( 600, 400 );
		f.add( label );  // f.add es una simplificación de f.getContentPane().add  (es lo mismo)
		f.add( labelGrafico );
		f.setVisible( true );
		try { Thread.sleep( 5000 ); } catch (Exception e) {}  // Espera 5 segundos
		for (int rot=0; rot<=200; rot++ ) {
			labelGrafico.setRotacion( rot*Math.PI/100 );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos décimas entre rotación y rotación
		}
		for (int op=-100; op<=100; op++ ) {
			labelGrafico.setOpacidad( Math.abs(op*0.01f) );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos décimas entre cambio de transparencias
		}
		for (int op=0; op<99; op++ ) {
			labelGrafico.setSize( labelGrafico.getAnchuraObjeto()-1, labelGrafico.getAlturaObjeto()-1 );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos décimas entre cambio de tamaño
		}
		for (int op=0; op<200; op++ ) {
			labelGrafico.setSize( labelGrafico.getAnchuraObjeto()+1, labelGrafico.getAlturaObjeto()+1 );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos décimas entre cambio de tamaño
		}
		for (int op=0; op<100; op++ ) {
			labelGrafico.setSize( labelGrafico.getAnchuraObjeto()-1, labelGrafico.getAlturaObjeto() );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos décimas entre aplastamiento de tamaño
		}
		for (int rot=0; rot<=200; rot++ ) {
			labelGrafico.setRotacion( rot*Math.PI/100 );
			try { Thread.sleep( 20 ); } catch (Exception e) {}  // Espera dos décimas entre rotación y rotación
		}
	}

	// No static
	
	// la posición X,Y se hereda de JLabel
	protected int anchuraObjeto;   // Anchura definida del objeto en pixels
	protected int alturaObjeto;    // Altura definida del objeto en pixels
	protected double radsRotacion; // Rotación del objeto en radianes
	protected float opacidad;      // Opacidad del objeto (0.0f a 0.1f)
	protected double zoom = 1.0;   // 1.0 = 100% zoom  (0.1 = 10%, 10.0 = 1000% ...)
	protected BufferedImage imagenObjeto;  // imagen para el escalado
	private static final long serialVersionUID = 1L;  // para serializar

	/** Crea un nuevo JLabel gráfico.<br>
	 * Si no existe el fichero de imagen, se crea un rectángulo blanco con borde rojo
	 * @param nombreImagenObjeto	Nombre fichero donde está la imagen del objeto. Puede ser también un nombre de recurso desde el paquete de esta clase.
	 * @param anchura	Anchura del gráfico en píxels (si es <= 0 ocupa todo el ancho de la imagen original)
	 * @param altura	Altura del gráfico en píxels (si es <= 0 ocupa todo el alto de la imagen original)
	 */
	public JLabelGrafico( String nombreImagenObjeto, int anchura, int altura ) {
		this( nombreImagenObjeto, anchura, altura, 0.0, 1.0f );
	}
	
	/** Crea un nuevo JLabel gráfico.<br>
	 * Si no existe el fichero de imagen, se crea un rectángulo blanco con borde rojo
	 * @param nombreImagenObjeto	Nombre fichero donde está la imagen del objeto. Puede ser también un nombre de recurso desde el paquete de esta clase.
	 * @param anchura	Anchura del gráfico en píxels (si es <= 0 ocupa todo el ancho de la imagen original)
	 * @param altura	Altura del gráfico en píxels (si es <= 0 ocupa todo el alto de la imagen original)
	 * @param rotacion	Rotación del objeto (en radianes)
	 * @param opacidad	Opacidad del objeto (0.0f transparente a 1.0f opaco)
	 */
	public JLabelGrafico( String nombreImagenObjeto, int anchura, int altura, double rotacion, float opacidad ) {
		setName( nombreImagenObjeto );
		setImagen( nombreImagenObjeto ); // Cargamos el icono
		setSize( anchura, altura );
		setRotacion(rotacion);
		setOpacidad(opacidad);
	}
	
	@Override
	public void setSize(int anchura, int altura) {
        if (anchura <= 0 && imagenObjeto!=null) anchura = imagenObjeto.getWidth();
        if (altura <= 0 && imagenObjeto!=null) altura = imagenObjeto.getHeight();
		anchuraObjeto = anchura;
		alturaObjeto = altura;
    	super.setSize( anchura, altura );
		setMinimumSize( new Dimension( anchura, altura ) );
    	setPreferredSize( new Dimension( anchura, altura ) );
		setMaximumSize( new Dimension( anchura, altura ) );
	}
	
	/** Cambia la imagen del objeto
	 * @param nomImagenObjeto	Nombre fichero donde está la imagen del objeto. Puede ser también un nombre de recurso desde el paquete de esta clase.
	 */
	public void setImagen( String nomImagenObjeto ) {
		File f = new File(nomImagenObjeto);
        URL imgURL = null;
        try {
        	// Intento 1: desde fichero
        	imgURL = f.toURI().toURL();
			imagenObjeto = ImageIO.read(imgURL);
        } catch (Exception e) {
        	// Intento 2: desde paquete (recurso)
			try {
	    		imgURL = JLabelGrafico.class.getResource( nomImagenObjeto ).toURI().toURL();
				imagenObjeto = ImageIO.read(imgURL);
			} catch (Exception e2) {  
				// Intento 3: Mirar si está en el paquete de la clase llamadora
				StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
				for (int i=1; i<stElements.length; i++) {
					StackTraceElement ste = stElements[i];
					if ( !ste.getClassName().endsWith("JLabelGrafico") ) {  // Busca la clase llamadora a JLabelGrafico y busca ahí el recurso
						try {
							Class<?> c = Class.forName( ste.getClassName() );
				    		imgURL = c.getResource( nomImagenObjeto ).toURI().toURL();
							imagenObjeto = ImageIO.read(imgURL);
							break;
						} catch (Exception e3) {
							// Sigue intentando
						}
					}
				}
			}
        }  
        if (imgURL == null || imagenObjeto == null) {  // Con cualquier error de carga, la imagen queda nula
        	imgURL = null;
        	imagenObjeto = null;
        }
        if (imagenObjeto==null) { // Si es nula, se pone un texto de error con el nombre sobre fondo rojo
			setOpaque( true );
			setBackground( Color.red );
			setForeground( Color.blue );
	    	setBorder( BorderFactory.createLineBorder( Color.blue ));
	    	setText( nomImagenObjeto );
        }
        repaint();
	}
	
	/** Devuelve la anchura del rectángulo gráfico del objeto
	 * @return	Anchura
	 */
	public int getAnchuraObjeto() {
		return anchuraObjeto;
	}
	
	/** Devuelve la altura del rectángulo gráfico del objeto
	 * @return	Altura
	 */
	public int getAlturaObjeto() {
		return alturaObjeto;
	}
	
	/** Devuelve la rotación del objeto
	 * @return	Rotación actual del objeto en radianes
	 */
	public double getRotacion() {
		return radsRotacion;
	}

	/** Modifica la rotación del objeto
	 * @param rotacion	Nueva rotación del objeto (en radianes)
	 */
	public void setRotacion( double rotacion ) {
		radsRotacion = rotacion;
		repaint(); // Si no repintamos aquí Swing no sabe que ha cambiado el dibujo
	}
	
	/** Devuelve la opacidad del objeto
	 * @return	Opacidad del objeto (0.0f transparente a 1.0f opaco)
	 */
	public float getOpacidad() {
		return opacidad;
	}

	/** Modifica la opacidad del objeto
	 * @param opacidad	Opacidad del objeto (0.0f transparente a 1.0f opaco)
	 */
	public void setOpacidad(float opacidad) {
		if (opacidad<0.0f || opacidad>1.0f) return; // No se cambia si el valor es inválido
		this.opacidad = opacidad;
		repaint(); // Si no repintamos aquí Swing no sabe que ha cambiado el dibujo
	}
	
	/** Cambia el zoom por el zoom indicado
	 * @param zoom	Valor nuevo de zoom, positivo (0.1 = 10%, 1.0 = 100%, 2.0 = 200%...)
	 */
	public void setZoom( double zoom ) {
		if (zoom>0.0) {
			this.zoom = zoom;
			repaint();
		}
	}
	
	/** Devuelve el zoom actual
	 * @return	Zoom actual
	 */
	public double getZoom() {
		return zoom;
	}
	
	/** Actualiza la posición del objeto
	 * @param x	Coordenada x (doble) - se redondea al píxel más cercano
	 * @param y	Coordenada y (doble) - se redondea al píxel más cercano
	 */
	public void setLocation( double x, double y ) {
		setLocation( (int)Math.round(x), (int)Math.round(y) );
	}
	
	// Dibuja este componente de una forma no habitual
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (imagenObjeto!=null) {
			Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
			// Zoom
	        int anchoDibujado = (int)Math.round(anchuraObjeto*zoom);  // Calcular las coordenadas de dibujado con el zoom, siempre centrado en el label
	        int altoDibujado = (int)Math.round(alturaObjeto*zoom);
	        int difAncho = (getWidth() - anchoDibujado) / 2;  // Offset x para centrar
	        int difAlto = (getHeight() - altoDibujado) / 2;     // Offset y para centrar
			// Rotación
			g2.rotate( radsRotacion, getWidth()/2, getHeight()/2 );  // Incorporar al gráfico la rotación definida
			// Transparencia
			g2.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacidad ) ); // Incorporar la transparencia definida
	        g2.drawImage(imagenObjeto, difAncho, difAlto, anchoDibujado, altoDibujado, null);  // Dibujar la imagen con el tamaño calculado tras aplicar el zoom
	        // Deshacer rotación
	        g2.rotate( Math.PI*2-radsRotacion, getWidth()/2, getHeight()/2 );
		}
	}

}
