package es.deusto.prog3.cap01.ejercicios;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;


/** Clase que simula objetos móviles para prueba de fórmulas sencillas de movimiento en 2D
 * EJERCICIO:
 * Desarrolla una prueba de unidad para comprobar que en un disparo vertical 
 * la velocidad de subida inicial coincide con la inversa de la de llegada,
 * y el tiempo de subida coincide con el de bajada
 * (no hace falta sacar ninguna ventana)
 */
public class ObjetoMovil {
	private double posX;
	private double posY;
	private double velX;
	private double velY;
	
	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	/** Mueve el objeto
	 * @param tiempo	Milisegundos de tiempo de movimiento
	 * @param acelX	Aceleración X que sufre el objeto
	 * @param acelY	Aceleración Y que sufre el objeto
	 */
	public void mueve( long tiempo, double acelX, double acelY ) {
		posX += velX * tiempo / 1000.0;
		velX += acelX * tiempo / 1000.0;
		posY += velY * tiempo / 1000.0;
		velY += acelY * tiempo / 1000.0;
	}
	
	/** Dibuja el objeto como una circunferencia azul de 20 píxels de diámetro
	 * @param g	Gráfico en el que se dibuja el objeto
	 */
	public void dibuja( Graphics2D g ) {
		int x = (int) Math.round( posX ) - 10;
		int y = (int) Math.round( posY ) - 10;
		if (g!=null) g.setColor( Color.BLUE );
		if (g!=null) g.drawOval( x, y, 20, 20 );
	}

	// Atributos para la prueba de ventana del objeto móvil
	public static ObjetoMovil movil = new ObjetoMovil();
	public static Thread hilo;
	
	private static BufferedImage dibujo;
	private static Graphics2D gDibujo;
	private static JPanel pDibujo;
	private static JTextField tfVelX = new JTextField( "400.0", 4 );
	private static JTextField tfVelY = new JTextField( "-1000.0", 4 );
	private static JTextField tfAcelX = new JTextField( "0.0", 4 );
	private static JTextField tfAcelY = new JTextField( "980.0", 4 );
	/** Método de prueba del objeto móvil que visualiza una ventana con un objeto móvil
	 * donde se "dispara"
	 * @param args	No utilizado
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		// Objeto de dibujo
		dibujo = new BufferedImage( 3000, 3000, BufferedImage.TYPE_INT_RGB );
		gDibujo = dibujo.createGraphics();
		gDibujo.setColor( Color.white );
		gDibujo.fillRect( 0, 0, 3000, 3000 );  // Fondo blanco
		// Ventana
		final JFrame f = new JFrame( "Ventana de prueba de objetos móviles" );  // Ventana a visualizar
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );  // Se libera al cerrar
		f.setSize( 1000, 800 );  // Con ese tamaño
		f.setLocationRelativeTo( null );   // Se posiciona con respecto al escritorio (centrada)
		JPanel pInferior = new JPanel();
		JButton bDisparo = new JButton( "Lanzar" );
		pInferior.add( new JLabel("Vel.X,Y:") ); pInferior.add( tfVelX ); pInferior.add( tfVelY );
		pInferior.add( new JLabel("px/sg. Ac.X,Y:") ); pInferior.add( tfAcelX ); pInferior.add( tfAcelY );
		pInferior.add( new JLabel("px/sg2") );
		pInferior.add( bDisparo );
		pDibujo = new JPanel() {  // Panel principal que pinta el objeto de dibujo
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(dibujo, 0, 0, null);
			}
		};
		f.add( pDibujo, BorderLayout.CENTER );  // Panel de dibujo al centro
		f.add( pInferior, BorderLayout.SOUTH ); // Panel de datos al sur
		bDisparo.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					double vx = Double.parseDouble( tfVelX.getText() );
					double vy = Double.parseDouble( tfVelY.getText() );
					double acx = Double.parseDouble( tfAcelX.getText() );
					double acy = Double.parseDouble( tfAcelY.getText() );
					init();
					hiloMovimiento( vx, vy, acx, acy );
				} catch (Exception ex) {
					JOptionPane.showMessageDialog( null, "Valores de velocidad/aceleración incorrectos", "Error", JOptionPane.ERROR_MESSAGE );
				}
			}
		});
		// La hacemos visible
		f.setVisible( true );
		init();
	}

	// Inicializa el objeto móvil de prueba en la esquina inferior izquierda de la ventana
	private static void init() {
		movil.setPosX( 20 );
		movil.setPosY( pDibujo.getHeight() - 20 );
	}
	
	// Borra el panel para poder dibujar el siguiente fotograma (lo pinta entero de blanco)
	private static void borraDibujo() {
		if (gDibujo!=null) gDibujo.setColor( Color.white );
		if (gDibujo!=null) gDibujo.fillRect( 0, 0, 3000, 3000 );  // Fondo blanco
	}
	
	// Hilo de movimiento (lanzado desde el botón)
	public static void hiloMovimiento( double vx, double vy, double acx, double acy ) {
		hilo = new Thread() {
			public void run() {
				movil.setVelX( vx );
				movil.setVelY( vy );
				movil.dibuja( gDibujo ); // Dibuja el móvil en la posición inicial
				// Bucle de animación: Mientras el móvil no se salga, lo mueve y dibuja cada 20 milisegundos
				int anchMax = (pDibujo==null) ? 10000 : pDibujo.getWidth()+20;
				double altInferior = (pDibujo==null) ? movil.getPosY() : pDibujo.getHeight()+20;
				while (movil.getPosX()>=0 && movil.getPosY()>=0 && movil.getPosY()<=altInferior && movil.getPosX()<=anchMax) {
					try { Thread.sleep( 10 ); } catch (InterruptedException e) {}
					movil.mueve( 10, acx, acy );
					borraDibujo(); // Borra el panel
					movil.dibuja( gDibujo ); // Dibuja el móvil en su nueva posición
					if (pDibujo!=null) pDibujo.repaint(); // Repinta el panel completo
				}
			}
		};
		hilo.start();
	}

}
