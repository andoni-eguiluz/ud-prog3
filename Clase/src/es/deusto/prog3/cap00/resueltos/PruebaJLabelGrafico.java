package es.deusto.prog3.cap00.resueltos;

import javax.swing.*;
import java.awt.*;

/** Clase que muestra cómo crear JLabels personalizados, con una imagen de coche que puede rotarse
 * y se escala a un tamaño diferente a la imagen original
 */
public class PruebaJLabelGrafico extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Dimension TAMANYO_VENTANA = new Dimension( 600, 400 );

	/** Método principal - crea una ventana de prueba y muestra un coche con el JLabel creado 
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		PruebaJLabelGrafico vent = new PruebaJLabelGrafico();
		vent.setVisible( true );
		vent.mover();
	}
	
	private JPanel pJuego;
	private MiCoche lCoche;
	
	public PruebaJLabelGrafico() {
		// Configuración de la ventana
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( TAMANYO_VENTANA );
		
		// Creamos los contenedores
		pJuego = new JPanel();
		pJuego.setLayout( null );
		
		// Creamos los componentes. Se puede hacer con ficheros:
		// lCoche = new JLabel( new ImageIcon( "src/es/deusto/prog3/cap00/resueltos/coche.png" ) );
		// O con recursos:
		// lCoche = new JLabel( new ImageIcon( PruebaJLabelGrafico.class.getResource("coche.png") ) );
		// Lo vamos a hacer con una clase personalizada que dibuja mejor lo que queremos que JLabel:
		lCoche = new MiCoche( new ImageIcon( PruebaJLabelGrafico.class.getResource("coche.png") ) );
		
		// Asociamos componentes a contenedores
		pJuego.add( lCoche );
		getContentPane().add( pJuego, BorderLayout.CENTER );
		// Gestión de eventos (si procede)
	}
	
	private void mover() {
		// Hacer algo con el coche
		// lCoche.setSize( 100, 100 );  // Tamaño
		// lCoche.setLocation( 100, 0 );  // Posición
		// lCoche.setBounds( 100, 0, 100, 100);  // Tamaño y posición
		double direccionPrueba = Math.PI/8;  // Prueba de movimiento en dirección 22,5º (en radianes)
		double x = lCoche.getX();  // Para movimiento fino mejor números reales que enteros
		double y = lCoche.getY();
		double pixelsPorPaso = 5;
		lCoche.setDireccion( direccionPrueba );
		// Animación de movimiento
		for (int i=0; i<100; i++) {
			x += pixelsPorPaso*Math.cos(direccionPrueba);
			y += pixelsPorPaso*Math.sin(direccionPrueba);
			lCoche.setLocation( (int)x, (int)y );
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// e.printStackTrace();  // Ojo con ocultar el tratamiento de excepción si no se tiene claro
			}
		}
		// Animación de rotación
		// Pregunta: ¿por qué no se esta animación de rotación, y sí se ve la de movimiento?
		// Pista: redimensiona la ventana mientras está ocurriendo esta animación
		// ¿Cómo se soluciona?
		for (int i=0; i<200; i++) {
			direccionPrueba += 0.05;
			lCoche.setDireccion( direccionPrueba );
			// Respuesta: hace falta esta línea - Swing no se entera de que hay cambio, hay que informarle
			lCoche.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}
	}

	/** Clase que permite crear JLabels adaptadas a un gráfico de coche que puede girar y se escala a 100x100 píxeles
	 * @author andoni.eguiluz @ ingenieria.deusto.es
	 */
	private class MiCoche extends JLabel {
		private static final long serialVersionUID = 1L;
		private static final int TAMANYO_COCHE = 200;
		private double direccion; // Ángulo de giro del coche

		public MiCoche( ImageIcon i ) {
			super( i );
			setSize( TAMANYO_COCHE, TAMANYO_COCHE );
		}
		
		/** Cambia la dirección del coche
		 * @param direccion	Nuevo ángulo (en radianes)
		 */
		public void setDireccion(double direccion) {
			this.direccion = direccion + Math.PI/2; 
			// Reflexión ¿Por qué se suman 90º?
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// super.paintComponent(g);  // No vamos a dibujar igual que un JLabel
			Graphics2D g2 = (Graphics2D) g;  // Componente gráfico en el que dibujar (Graphics2D tiene más funcionalidad que Graphics)
			Image img = ((ImageIcon)getIcon()).getImage();  // Imagen del label - la vamos a dibujar diferente
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );  // suaviza la rotación
			g2.rotate( direccion, TAMANYO_COCHE/2, TAMANYO_COCHE/2 );  // Rota la imagen
			g2.drawImage( img, 0, 0, TAMANYO_COCHE, TAMANYO_COCHE, null  );  // Dibuja la imagen escalando a 100x100
		}
	}
	
}
