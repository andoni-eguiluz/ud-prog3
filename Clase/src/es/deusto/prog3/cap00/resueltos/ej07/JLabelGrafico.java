package es.deusto.prog3.cap00.resueltos.ej07;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

/** Clase mejorada de JLabel para gestionar imágenes ajustadas al JLabel, con escala, rotación y transparencia
 * (la escala se relaciona con el tamaño del label para que no se recorte al cambiar de tamaño. Sí se puede recortar con la rotación)
 */
public class JLabelGrafico extends JLabel {


	// No static
	
	// la posición X,Y se hereda de JLabel
	protected double anchuraObjeto;   // Anchura definida del objeto en pixels
	protected double alturaObjeto;    // Altura definida del objeto en pixels
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
	public JLabelGrafico( String nombreImagenObjeto, double anchura, double altura ) {
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
	public JLabelGrafico( String nombreImagenObjeto, double anchura, double altura, double rotacion, float opacidad ) {
		setName( nombreImagenObjeto );
		setImagen( nombreImagenObjeto ); // Cargamos el icono
		setSize( anchura, altura );
		setRotacion(rotacion);
		setOpacidad(opacidad);
	}
	
	public void setSize(double anchura, double altura) {
        if (anchura <= 0 && imagenObjeto!=null) anchura = imagenObjeto.getWidth();
        if (altura <= 0 && imagenObjeto!=null) altura = imagenObjeto.getHeight();
		anchuraObjeto = anchura;
		alturaObjeto = altura;
    	super.setSize( (int) anchura, (int) altura );
    	setPreferredSize( new Dimension( (int) anchura, (int)altura ));
	}
	
	/** Cambia la imagen del objeto
	 * @param nomImagenObjeto	Nombre fichero donde está la imagen del objeto. Puede ser también un nombre de recurso desde el paquete de esta clase.
	 */
	public void setImagen( String nomImagenObjeto ) {
		File f = new File(nomImagenObjeto);
        URL imgURL = null;
        try {
        	imgURL = f.toURI().toURL();
    		if (!f.exists()) {
    			imgURL = JLabelGrafico.class.getResource( nomImagenObjeto ).toURI().toURL();
    		}
        } catch (Exception e) {}  // Cualquier error de carga, la imagen se queda nula
        if (imgURL == null) {
        	imagenObjeto = null;
        } else {
        	try {  // guarda la imagen para dibujarla de forma escalada después
    			imagenObjeto = ImageIO.read(imgURL);
    		} catch (IOException e) {}  // Error al leer la imagen
        }
        if (imagenObjeto==null) {
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
	public double getAnchuraObjeto() {
		return anchuraObjeto;
	}
	
	/** Devuelve la altura del rectángulo gráfico del objeto
	 * @return	Altura
	 */
	public double getAlturaObjeto() {
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
