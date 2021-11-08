package es.deusto.prog3.cap06.pr0506resuelta.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelDibujoBancoDePruebas extends JPanel {

	// *******************************************************************
	// Datos y métodos generales para hacer un panel con dibujado gráfico permanente
	// *******************************************************************
	
	private BufferedImage imagen;  // Imagen a dibujar en el panel

	/** Crea un panel de dibujado
	 * @param tamHor	Tamaño máximo horizontal de dibujo (en píxels)
	 * @param tamVer	Tamaño máximo vertical de dibujo (en píxels)
	 * @param transp	true para dibujo transparente
	 */
	public PanelDibujoBancoDePruebas( int tamHor, int tamVer, boolean transp ) {
		if (transp) {
			setOpaque(false);
			imagen = new BufferedImage( tamHor, tamVer, BufferedImage.TYPE_INT_ARGB );
		} else {
			imagen = new BufferedImage( tamHor, tamVer, BufferedImage.TYPE_INT_RGB );
			Graphics2D gr2 = imagen.createGraphics();
			gr2.setColor( Color.white );
			gr2.fillRect( 0, 0, tamHor, tamVer );  // Rellenar el fondo
			setOpaque(true);
			setBackground( Color.white );
		}
		g2 = imagen.createGraphics();
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void pintaFondo( Color cF ) {
		g2.setColor( cF );
		g2.fillRect( 0, 0, getWidth(), getHeight() );  // Rellenar el fondo
		// g2.fillRect( 0, 0, imagen.getWidth(), imagen.getHeight() );  // Hacerlo así si se quiere ver sólo parte de la imagen pero inicializar toda
	}

	public Graphics2D getGraphics() {
		return (Graphics2D) imagen.getGraphics();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);
		if (imagen!=null) {
			g.drawImage(imagen, 0, 0, null);
		}
	}

//@Override
//protected void paintComponent(Graphics g) {
//	super.paintComponent(g);
//	if (imagen!=null) {
//		g.drawImage(imagen, 0, 0, null);
//	}
//}
	
	// *******************************************************************
	// Datos y métodos de dibujado especializado para el banco de pruebas
	// *******************************************************************
	
	// Atributos propios para guardar los datos que se van dibujando del banco de pruebas
	String[] nombresPruebas;  // Array de nombres de las pruebas
	ArrayList<Long> tiempos[];   // Array de procesos de arraylists de todos los tiempos
	ArrayList<Integer> espacios[];  // Array de procesos de arraylists de todos los espacios
	ArrayList<Integer> tamanyos;    // Arraylist de los tamaños
	ArrayList<Color> colores;  // Arraylist de colores
	long maxEspacio;  // Valor máximo a dibujar de espacio (en bytes)
	long maxTiempo;   // Valor máximo a dibujar de tiempo (en msgs)
	int maxTamanyo;   // Valor máximo a dibujar de tamaño
	
	Graphics2D g2;  // Objeto gráfico en el que dibujar
	int origenX;  // Origen X
	int origenYEspacios;  // Origen Y (espacio)
	int origenYTiempos;  // Origen Y (tiempo)
	int ESPACIO_IZQDA_X = 40; // Píxels libres por la izquierda
	int ESPACIO_DCHA_X = 40; // Píxels libres por la derecha
	int ESPACIO_ABAJO_Y = 20; // Píxels libres por abajo
	int RADIO_CIRCULO_PUNTO = 3;  // Píxels de radio de circulito
	int alto;   // Altura de dibujo de cada gráfico (en pixels)
	int ancho;  // Anchura de dibujo de cada gráfico (en píxels)
	int[] filasMarcadas;  // Array de filas marcadas en el dibujo
	static Stroke stroke1 = new BasicStroke(1);
	static Stroke stroke3 = new BasicStroke(3);
	static Stroke stroke5 = new BasicStroke(5);
	
	@SuppressWarnings("unchecked")
	public void iniciarDibujo( int tamMax, String[] nombresPruebas ) {
		this.nombresPruebas = nombresPruebas;
		int numProcs = nombresPruebas.length;
		tiempos = new ArrayList[numProcs]; for (int i=0; i<numProcs; i++) tiempos[i] = new ArrayList<Long>();
		espacios = new ArrayList[numProcs]; for (int i=0; i<numProcs; i++) espacios[i] = new ArrayList<Integer>();
		tamanyos = new ArrayList<Integer>();
		colores = new ArrayList<Color>();
		iniciarColores();
		maxEspacio = 10;  // Empezamos en 10 bytes (luego se irá redimensionando)
		maxTiempo = 10;  // Empezamos en 10 msgs (luego se irá redimensionando)
		maxTamanyo = tamMax;
		origenX = ESPACIO_IZQDA_X;
		g2.setFont( new Font("Arial", Font.PLAIN, 10) );
		reiniciarDibujo();
	}
	
	/** Reinicia el dibujo con distinto tamaño de lienzo (llamar cuando cambie el tamaño del panel),
	 * pero el resto de datos siguen como estuvieran.
	 */
	public void reiniciarDibujo() {
		if (tiempos!=null) {  // Si ya está inicializado el dibujo
			alto = (getHeight() - ESPACIO_ABAJO_Y*2) / 2;
			origenYTiempos = getHeight() - ESPACIO_ABAJO_Y*2 - alto;
			origenYEspacios = getHeight() - ESPACIO_ABAJO_Y;
			ancho = getWidth() - origenX - ESPACIO_DCHA_X;
			redibujaTodo();
			repaint();
		}
	}
	
	
	// Se dibuja en orden por tamanyos
	public void anyadirYDibujarTiempo( int numProc, int numTam, int tamanyo, long valor ) {
		if (tamanyos.size() == numTam) tamanyos.add( tamanyo );
		tiempos[numProc].add( valor );
		if (valor > maxTiempo) {
			maxTiempo = (int) (valor * 1.1);
			redibujaTodo();
		} else {
			dibujarTiempo( numProc, numTam );
		}
	}
	
	// Se dibuja en orden por tamanyos
	public void anyadirYDibujarEspacio( int numProc, int numTam, int tamanyo, int valor ) {
		if (tamanyos.size() == numTam) tamanyos.add( tamanyo );
		espacios[numProc].add( valor );
		if (valor > maxEspacio) {
			maxEspacio = (int) (valor * 1.1);
			redibujaTodo();
		} else {
			dibujarEspacio( numProc, numTam );
		}
	}
	
	// Dibuja un punto y, si procede, un segmento
	public void dibujarTiempo( int numProc, int numTam ) {
		g2.setColor( colores.get(numProc) );
		int x = calcX( tamanyos.get(numTam) );
		int y = calcYTiempo( tiempos[numProc].get(numTam) );
		if (marcadaFila(numProc))
			g2.fillOval( x-RADIO_CIRCULO_PUNTO, y-RADIO_CIRCULO_PUNTO, RADIO_CIRCULO_PUNTO*2, RADIO_CIRCULO_PUNTO*2 );
		else
			g2.drawOval( x-RADIO_CIRCULO_PUNTO, y-RADIO_CIRCULO_PUNTO, RADIO_CIRCULO_PUNTO*2, RADIO_CIRCULO_PUNTO*2 );
		if (numTam>0) {  // Dibujar línea
			int xAnt = calcX( tamanyos.get(numTam-1) );
			int yAnt = calcYTiempo( tiempos[numProc].get(numTam-1) );
			if (marcadaFila(numProc)) {
				g2.setStroke( stroke5 );
				g2.drawLine( xAnt, yAnt, x, y );
				g2.setStroke( stroke1 );
			} else {
				g2.drawLine( xAnt, yAnt, x, y );
			}
		}
	}
	
	// Dibuja un punto y, si procede, un segmento
	public void dibujarEspacio( int numProc, int numTam ) {
		g2.setColor( colores.get(numProc) );
		int x = calcX( tamanyos.get(numTam) );
		int y = calcYEspacio( espacios[numProc].get(numTam) );
		if (marcadaFila(numProc+nombresPruebas.length))
			g2.fillOval( x-RADIO_CIRCULO_PUNTO, y-RADIO_CIRCULO_PUNTO, RADIO_CIRCULO_PUNTO*2, RADIO_CIRCULO_PUNTO*2 );
		else
			g2.drawOval( x-RADIO_CIRCULO_PUNTO, y-RADIO_CIRCULO_PUNTO, RADIO_CIRCULO_PUNTO*2, RADIO_CIRCULO_PUNTO*2 );
		if (numTam>0) {  // Dibujar línea
			int xAnt = calcX( tamanyos.get(numTam-1) );
			int yAnt = calcYEspacio( espacios[numProc].get(numTam-1) );
			if (marcadaFila(numProc+nombresPruebas.length)) {
				g2.setStroke( stroke5 );
				g2.drawLine( xAnt, yAnt, x, y );
				g2.setStroke( stroke1 );
			} else {
				g2.drawLine( xAnt, yAnt, x, y );
			}
		}
	}

	// Dibuja todo el gráfico de nuevo
	public void redibujaTodo() {
		g2.setStroke( stroke1 );
		pintaFondo( new Color( 1,1,1,1f ) );  // Blanco con opacidad del 100%
		dibujaEjesEspacio();
		// Dibuja primero los tamaños
		for (int numProc=0; numProc<tiempos.length; numProc++) {
			for (int numTam=0; numTam<tamanyos.size(); numTam++) {
				if (espacios[numProc].size() > numTam) {   // Dibuja espacio
					 dibujarEspacio( numProc, numTam );
				}
			}
		}
		// Borra lo que se haya podido pasar hacia arriba (en el espacio de los tiempos)
		g2.setColor( Color.white );
		g2.fillRect( 0, 0, getWidth(), getHeight()/2 );
		// Y luego los tiempos
		dibujaEjesTiempo();
		for (int numProc=0; numProc<tiempos.length; numProc++) {
			for (int numTam=0; numTam<tamanyos.size(); numTam++) {
				if (tiempos[numProc].size() > numTam) {   // Dibuja tiempo
					 dibujarTiempo( numProc, numTam );
				}
			}
		}
		repaint();
	}

	// true si acercar, false si alejar
	public void cambiarZoom( boolean acercar ) {
		if (acercar) {
			if (maxTiempo>=2 && maxEspacio>=2) {
				maxEspacio /= 2;
				maxTiempo /= 2;
			}
		} else {
			maxEspacio *= 2;
			maxTiempo *= 2;
		}
		redibujaTodo();
	}
	
	public Color getColor( int numProc ) {
		return colores.get(numProc);
	}
	
	public void marcaLineas( int[] filasMarcadas ) {  // filas = 0 a n-1 de tiempo, n a 2n-1 de espacio
		this.filasMarcadas = filasMarcadas;
		redibujaTodo();
	}
	
	private boolean marcadaFila( int numFila ) {
		if (filasMarcadas==null) return false;
		for (int i=0; i<filasMarcadas.length; i++)
			if (filasMarcadas[i]==numFila) return true;
		return false;
	}
	
	// Métodos utilitarios
		private static Random r = new Random();
	private void iniciarColores() {
		colores.add( Color.blue );
		colores.add( Color.green );
		colores.add( Color.magenta );
		while (colores.size() < tiempos.length ) {  // Si hay más de 3 colores se añaden aleatoriamente
			colores.add( new Color( r.nextInt(240), r.nextInt(240), r.nextInt(240) ) );  // Se llega a 240 para no usar por azar el blanco (255,255,255) que se confundiría con el fondo
		}
	}

	private int calcX( long x ) {
		return origenX + (int) Math.round( ancho*x/maxTamanyo );
	}
	
	private int calcYEspacio( long y ) {
		return origenYEspacios - (int) Math.round( alto*y/maxEspacio );
	}
	
	private int calcYTiempo( long y ) {
		return origenYTiempos - (int) Math.round( alto*y/maxTiempo );
	}
	
	private void dibujaEjesEspacio() {
		g2.setColor( Color.black );
		g2.setStroke( stroke3 );
		g2.drawLine( calcX(0), calcYEspacio(0), calcX(maxTamanyo), calcYEspacio(0) );
		g2.drawLine( calcX(0), calcYEspacio(0), calcX(0), calcYEspacio(maxEspacio) );
		g2.setStroke( stroke1 );
		dibujaMarcasEjes( true, 10, 8 );
	}
	
	private void dibujaEjesTiempo() {
		g2.setColor( Color.black );
		g2.setStroke( stroke3 );
		g2.drawLine( calcX(0), calcYTiempo(0), calcX(maxTamanyo), calcYTiempo(0) );
		g2.drawLine( calcX(0), calcYTiempo(0), calcX(0), calcYTiempo(maxTiempo) );
		g2.setStroke( stroke1 );
		dibujaMarcasEjes( false, 10, 8 );
	}
	
	private void dibujaMarcasEjes( boolean esEspacio, int marcasX, int marcasY ) {
		// Marcas horizontales
		long espEntreMarcas = maxTamanyo / marcasX;
		for (int i=0; i<marcasX; i++) {
			long x = espEntreMarcas * (i+1);
			g2.drawLine( calcX(x), origenYEspacios-4, calcX(x), origenYEspacios+4 );
			g2.drawLine( calcX(x), origenYTiempos-4, calcX(x), origenYTiempos+4 );
			g2.drawString( ""+(x), calcX(x)-10, origenYEspacios+10 );
			g2.drawString( ""+(x), calcX(x)-10, origenYTiempos+10 );
		}
		// Marcas verticales
		if (esEspacio) {
			espEntreMarcas = maxEspacio / marcasY;
			for (int i=0; i<marcasY; i++) {
				long y = espEntreMarcas * (i+1);
				g2.drawLine( origenX-4, calcYEspacio(y), origenX+4, calcYEspacio(y) );
				g2.drawLine( origenX-4, calcYEspacio(y), origenX+4, calcYEspacio(y) );
				String texto = (y/1024)+"k";
				int anchoTexto = g2.getFontMetrics().stringWidth( texto );
				g2.drawString( texto, calcX(0)-anchoTexto-2, calcYEspacio(y)+5 );
			}
		} else {
			espEntreMarcas = maxTiempo / marcasY;
			for (int i=0; i<marcasY; i++) {
				long y = espEntreMarcas * (i+1);
				g2.drawLine( origenX-4, calcYTiempo(y), origenX+4, calcYTiempo(y) );
				g2.drawLine( origenX-4, calcYTiempo(y), origenX+4, calcYTiempo(y) );
				String texto = ""+y;
				int anchoTexto = g2.getFontMetrics().stringWidth( texto );
				g2.drawString( texto, calcX(0)-anchoTexto-2, calcYTiempo(y)+5 );
			}
		}
	}

	
}
