package es.deusto.prog3.cap06;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


/** Clase de JLabel gráfico con capacidad de escalado, rotación y transparencia
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
@SuppressWarnings("serial")
public class JLabelGrafico extends JLabel {
	// la posición X,Y se hereda de JLabel
	protected String nombreImagenObjeto; // Nombre del fichero de imagen del objeto
	protected int anchuraObjeto;  // Anchura del objeto en pixels (depende de la imagen)
	protected int alturaObjeto;  // Altura del objeto en pixels (depende de la imagen)
	protected double radsRotacion = 0;  // 0 = no rotación. PI = media vuelta
	protected double zoom = 1.0;  // 1.0 = 100% zoom
	protected float opacity = 1.0f;  // 1.0 = 100% opaque / 0.0 = 0% opaque
	protected ImageIcon icono;  // icono del objeto
	protected BufferedImage imagenObjeto;  // imagen para el escalado
	
	/** Crea un nuevo objeto gráfico enriquecido.<br>
	 * Si no existe el fichero de imagen, se crea un rectángulo blanco con borde rojo
	 * @param nombreImagenObjeto	Nombre fichero donde está la imagen del objeto con respecto a este path de clase
	 * @param visible	true si se quiere ver, false si se quiere tener oculto
	 * @param anchura	Anchura del objeto en píxels
	 * @param altura	Altura del objeto en píxels
	 * @param rotacion	Rotación inicial en radianes (0=sin rotación, 2PI=vuelta completa. Sentido horario)
	 */
	public JLabelGrafico( String nombreImagenObjeto, boolean visible, int anchura, int altura, double rotacion ) {
		setName( nombreImagenObjeto );
		anchuraObjeto = anchura;
		alturaObjeto = altura;
		// Cargamos el icono (como un recurso - vale tb del .jar)
		this.nombreImagenObjeto = nombreImagenObjeto;
        URL imgURL = JLabelGrafico.class.getResource( nombreImagenObjeto );
        if (imgURL == null) {
        	icono = null;
    		setOpaque( true );
    		setBackground( Color.red );
    		setForeground( Color.blue );
        	setBorder( BorderFactory.createLineBorder( Color.blue ));
        	setText( nombreImagenObjeto );
        } else {
        	icono = new ImageIcon(imgURL);
    		setIcon( icono );
        	try {
				imagenObjeto = ImageIO.read(imgURL);
			} catch (IOException e) { e.printStackTrace(); }
        }
    	setSize( anchura, altura );
		setVisible( visible );
		radsRotacion = rotacion;
		calcStartZoom();
	}
	
	// Calcula el zoom inicial que es el que permite ver todo el gráfico cargado en el tamaño definido
	private void calcStartZoom() {
		if (icono==null) {
			zoom = 1.0;
		} else {
			double ratioAnchura = anchuraObjeto * 1.0 / icono.getIconWidth();
			double ratioAltura = alturaObjeto * 1.0 / icono.getIconHeight();
			zoom = Math.min(ratioAnchura, ratioAltura);
		}
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
	
	/** Cambia la opacidad por la indicada
	 * @param opacity	Valor nuevo de opacidad, entre 0.0 (transparente) y 1.0 (opaco)
	 */
	public void setOpacity( float opacity ) {
		if (opacity>=0.0 && opacity<=1.0) {
			this.opacity = opacity;
			repaint();
		}
	}
	
	/** Devuelve la opacidad actual
	 * @return	Opacidad actual
	 */
	public float getOpacity() {
		return opacity;
	}
	
	/** Cambia la rotación a los radianes indicados
	 * @param rotacion	Rotación en radianes (0=sin rotación, 2PI=vuelta completa. Sentido horario)
	 */
	public void setRotacion( double rotacion ) {
		radsRotacion = rotacion;
		repaint();
	}
	
	/** Cambia la rotación con el incremento en radianes indicado
	 * @param rotacion	Incremento de rotación en radianes (0=sin incremento, 2PI=vuelta completa. Sentido horario)
	 */
	public void incRotacion( double rotacion ) {
		radsRotacion += rotacion;
		if (radsRotacion > 2*Math.PI) radsRotacion -= (2 * Math.PI);
		repaint();
	}
	
	/** Devuelve la rotación actual
	 * @return	Rotación actual en radianes (0=sin rotación, 2PI=vuelta completa. Sentido horario)
	 */
	public double getRotacion() {
		return radsRotacion;
	}
	
	/** Cambia la rotación a los grados indicados
	 * @param rotacion	Rotación en grados (0=sin rotación, 360=vuelta completa. Sentido horario)
	 */
	public void setRotacionGrados( double rotacion ) {
		radsRotacion = rotacion / 180 * Math.PI;
		repaint();
	}
	
	/** Devuelve la rotación actual
	 * @return	Rotación actual en grados (0=sin rotación, 360=vuelta completa. Sentido horario)
	 */
	public double getRotacionGrados() {
		return radsRotacion / Math.PI * 180;
	}
	
	// Dibuja este componente de una forma no habitual (si es proporcional)
	@Override
	protected void paintComponent(Graphics g) {
		if (imagenObjeto==null || icono==null) {
			super.paintComponent(g);
		} else {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR); // Configuración para mejor calidad del gráfico escalado
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
	        g2.rotate( radsRotacion, anchuraObjeto/2, alturaObjeto/2 );  // Incorporar al gráfico la rotación definida
	        g2.setComposite(AlphaComposite.getInstance( AlphaComposite.SRC_OVER, opacity ) ); // Incorporar la transparencia definida
	        int anchoDibujado = (int)Math.round(icono.getIconWidth()*zoom);  // Calcular las coordenadas de dibujado con el zoom, siempre centrado en el label
	        int altoDibujado = (int)Math.round(icono.getIconHeight()*zoom);
	        int difAncho = (anchuraObjeto - anchoDibujado) / 2;  // Offset x para centrar
	        int difAlto = (alturaObjeto - altoDibujado) / 2;     // Offset y para centrar
	        g2.drawImage(imagenObjeto, difAncho, difAlto, anchoDibujado, altoDibujado, null);  // Dibujar la imagen con el tamaño calculado tras aplicar el zoom
		}
	}
	
	// Permite cambiar el icono mientras el objeto está en pantalla
	/* (non-Javadoc)
	 * @see javax.swing.JLabel#setIcon(javax.swing.Icon)
	 */
	@Override
	public void setIcon(Icon icon) {
        if (icono == null) {
    		super.setIcon(icon);
        } else {
        	try {  // pone la imagen para el escalado
            	icono = (ImageIcon) icon;
        		super.setIcon( icono );
        		Image source = icono.getImage();
        		int w = source.getWidth(null);
        		int h = source.getHeight(null);
        		imagenObjeto = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        		Graphics2D g2d = (Graphics2D)imagenObjeto.getGraphics();
        		g2d.drawImage(source, 0, 0, null);
        		g2d.dispose();
    		} catch (Exception e) {
            	icono = null;
        		setOpaque( true );
        		setBackground( Color.red );
        		setForeground( Color.blue );
            	setBorder( BorderFactory.createLineBorder( Color.blue ));
            	setText( nombreImagenObjeto );
    		}
        }
	}


		private static boolean acaba = false;
	/** Método main de prueba que saca un escudo UD y lo va rotando y cambiando zoom de manera continua.
	 * Si se hace click en él, se hace un ciclo de desaparición y aparición cambiando su opacidad.
	 * Si se hace drag, se mueve en el panel
	 * @param args
	 */
	public static void main(String[] args) {
		JLabelGrafico o = new JLabelGrafico( "img/bicho.png", true, 400, 400, 0.0 );
		javax.swing.JFrame v = new javax.swing.JFrame();
		v.getContentPane().setLayout( null );
		v.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		v.getContentPane().add( o );
		v.setSize( 800, 600 );
		try { javax.swing.SwingUtilities.invokeAndWait( new Runnable() { @Override public void run() {
			v.setVisible( true );
		} }); } catch (Exception e) {}
		boolean zoomCreciendo = false;
		v.addWindowListener( new java.awt.event.WindowAdapter() { @Override
			public void windowClosing(java.awt.event.WindowEvent e) { acaba = true; } });
		o.addMouseListener( new java.awt.event.MouseAdapter() {   // Ciclo de transparencia
			@Override public void mouseClicked(java.awt.event.MouseEvent e) {
				(new Thread() { @Override public void run() {
					float f = 1.0f;
					while (f>=0.0f) {
						o.setOpacity( f );
						f -= 0.01f;
						try { Thread.sleep(15); } catch (Exception e) {}
					}
					try { Thread.sleep(1000); } catch (Exception e) {}
					while (f<=1.0f) {
						o.setOpacity( f );
						f += 0.01f;
						try { Thread.sleep(15); } catch (Exception e) {}
					}
				} }).start();
			} 
			private Point pPressed = null;
			@Override public void mousePressed(java.awt.event.MouseEvent e) {
				pPressed = e.getPoint();
			}
			@Override public void mouseReleased(java.awt.event.MouseEvent e) {
				if (!e.getPoint().equals(pPressed))
					o.setLocation( o.getX() - pPressed.x + e.getX(), o.getY() - pPressed.y + e.getY() );
			}
		} );
		while (!acaba) {
			if (o.getZoom() < 0.3) zoomCreciendo = true; else if (o.getZoom() > 3.0) zoomCreciendo = false;
			if (zoomCreciendo) o.setZoom( o.getZoom()*1.02 );
			else o.setZoom( o.getZoom()*0.98 );
			try { Thread.sleep(15); } catch (Exception e) {}
			o.incRotacion( 0.01 );
		}
	}
}
