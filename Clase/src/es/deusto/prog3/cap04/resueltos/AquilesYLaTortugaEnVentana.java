package es.deusto.prog3.cap04.resueltos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.sun.org.apache.xml.internal.security.Init;

import es.deusto.prog3.utils.VisualizaProceso;

public class AquilesYLaTortugaEnVentana {			
	
	private static VisualizaProceso ventana;
	private static PanelConfig panelConf;
	
	public static double VEL_AQUILES = 10;   // metros / sg
	public static double VEL_TORTUGA = 0.5;  // m/sg (0.05m/sg = 1 metro cada 20 segs) 
	public static double INICIO_AQUILES = 0;    // Aquiles empieza en el metro 0
	public static double INICIO_TORTUGA = 1000; // La tortuga tiene 1 km de ventaja
	
	public static final double GRAF_AQUILES_ESCALA = 1.0;
	public static final double GRAF_AQUILES_ANCHO = 346;
	public static final double GRAF_AQUILES_ALTO = 254;
	public static final double GRAF_AQUILES_REFX = 33;
	public static final double GRAF_AQUILES_REFY = 248;
	public static final double GRAF_TORTUGA_ESCALA = 2.0;
	public static final double GRAF_TORTUGA_ANCHO = 71 * GRAF_TORTUGA_ESCALA;
	public static final double GRAF_TORTUGA_ALTO = 41 * GRAF_TORTUGA_ESCALA;
	public static final double GRAF_TORTUGA_REFX = 34 * GRAF_TORTUGA_ESCALA;
	public static final double GRAF_TORTUGA_REFY = 32 * GRAF_TORTUGA_ESCALA;

	private static double tiempo = 0;
	private static double posAquiles = 0;
	private static double posTortuga = 0;
	private static double vistaPantalla = 1800;
	private static double tiempoInicial = 0;
	private static double tiempoFinal = 150;

	@SuppressWarnings("serial")
	public static class PanelConfig extends JPanel {
		JTextField tfIniAquiles, tfIniTortuga, tfVelAquiles, tfVelTortuga, tfVistaPantalla;
		JTextField tfTiempoFinal;
		public PanelConfig() {
			// Inicialización contenedores
			this.setLayout( new GridLayout( 6, 1 ) );
			JPanel pSup = new JPanel(); pSup.setLayout( new BorderLayout() );
			JPanel pLinea1izq = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			JPanel pLinea2izq = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			JPanel pLinea3izq = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			JPanel pLinea4izq = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			JPanel pLinea5izq = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			JPanel pLinea3der = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			// Inicialización componentes
				JLabel l1 = new JLabel( "Configuración inicial", JLabel.CENTER );
				l1.setBorder( BorderFactory.createLineBorder( Color.blue, 1 ));
			this.add( l1 );
				pLinea1izq.add( new JLabel( "Inicio (m) - Aquiles" ) );
				tfIniAquiles = new JTextField( "" + INICIO_AQUILES ); tfIniAquiles.setColumns( 5 ); 
				posAquiles = INICIO_AQUILES;
				pLinea1izq.add( tfIniAquiles );
				pLinea1izq.add( new JLabel( " Tortuga" ) );
				tfIniTortuga = new JTextField( "" + INICIO_TORTUGA ); tfIniTortuga.setColumns( 5 ); 
				posTortuga = INICIO_TORTUGA;
				pLinea1izq.add( tfIniTortuga );
			this.add( pLinea1izq );
				pLinea2izq.add( new JLabel( "Velocidad (m/sg) - Aquiles" ) );
				tfVelAquiles = new JTextField( "" + VEL_AQUILES ); tfVelAquiles.setColumns( 5 ); 
				pLinea2izq.add( tfVelAquiles );
				pLinea2izq.add( new JLabel( " Tortuga" ) );
				tfVelTortuga = new JTextField( "" + VEL_TORTUGA ); tfVelTortuga.setColumns( 5 ); 
				pLinea2izq.add( tfVelTortuga );
			this.add( pLinea2izq );
				pLinea3izq.add( new JLabel( "Vista en pantalla (m)" ) );
				tfVistaPantalla = new JTextField( "" + vistaPantalla ); tfVistaPantalla.setColumns( 6 ); 
				pLinea3izq.add( tfVistaPantalla );
			this.add( pLinea3izq );
				pLinea4izq.add( new JLabel( "Tiempo inicio:" ) );
				pLinea4izq.add( new JLabel( "  0  " ) );
			this.add( pLinea4izq );
				pLinea5izq.add( new JLabel( "Tiempo final:" ) );
				tfTiempoFinal = new JTextField( "" + tiempoFinal ); tfTiempoFinal.setColumns( 6 ); 
				pLinea5izq.add( tfTiempoFinal );
			this.add( pLinea5izq );
		}
		private class EscFocoVal implements FocusListener {
			private JTextField miCampo;
			public EscFocoVal( JTextField tf ) {
				miCampo = tf;
			}
			private String valAnt = "";
			@Override
			public void focusGained(FocusEvent e) {
				valAnt = miCampo.getText();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (!valAnt.equals( miCampo.getText() )) {   // Si ha habido cambio validar valor
					try {
						double d = Double.parseDouble( miCampo.getText() );
						if (d<0) throw new NumberFormatException();
						if (miCampo==tfVistaPantalla) {
							vistaPantalla = d;
							lMetrosFin.setText( tfVistaPantalla.getText() );
							recolocaCarrera();
						} else if (miCampo==tfIniAquiles) {
							INICIO_AQUILES = d;
							ponAquiles( dondeEstaAquiles( d ) );
							recolocaCarrera();
						} else if (miCampo==tfIniTortuga) {
							INICIO_TORTUGA = d;
							ponTortuga( dondeEstaLaTortuga( d ) );
							recolocaCarrera();
						} else if (miCampo==tfVelAquiles) {
							VEL_AQUILES = d;
						} else if (miCampo==tfVelTortuga) {
							VEL_TORTUGA = d;
						} else if (miCampo==tfTiempoFinal) {
							tiempoFinal = d;
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog( ventana.getVentana(), "El valor debe ser un real válido y positivo", "Error en valor", JOptionPane.ERROR_MESSAGE );
						miCampo.requestFocus();
					}
				}
			}
		}
	}

	// Dibujado de panel
	private static JLabel lAquiles, lTortuga, lMetrosIni, lMetrosFin;
	private static JLabelGrafico lAquilesIni, lAquilesFin;
	private static JLabelGrafico lTortugaIni, lTortugaFin;
	private static JPanel pCarrera;
	public static void init() {
		panelConf = new PanelConfig();
		// Inicialización contenedores
		JPanel pSup = new JPanel(); pSup.setLayout( new BorderLayout() );
		// Inicialización componentes
		pCarrera = ventana.getPanelPrincipal();
		pCarrera.setBackground( Color.white );
		lAquiles = new JLabelGrafico( (int)GRAF_AQUILES_ANCHO, (int)GRAF_AQUILES_ALTO, "../../utils/img/Aquiles.png", GRAF_AQUILES_REFX, GRAF_AQUILES_REFY );
		lAquilesIni = new JLabelGrafico( (int)GRAF_AQUILES_ANCHO, (int)GRAF_AQUILES_ALTO, "../../utils/img/Aquiles.png", GRAF_AQUILES_REFX, GRAF_AQUILES_REFY );
		lAquilesFin = new JLabelGrafico( (int)GRAF_AQUILES_ANCHO, (int)GRAF_AQUILES_ALTO, "../../utils/img/Aquiles.png", GRAF_AQUILES_REFX, GRAF_AQUILES_REFY );
		lAquilesIni.setTransparencia( 0.6f ); lAquilesIni.setVisible( false );
		lAquilesFin.setTransparencia( 0.6f ); lAquilesFin.setVisible( false );
		lTortuga = new JLabelGrafico( (int)GRAF_TORTUGA_ANCHO, (int)GRAF_TORTUGA_ALTO, "../../utils/img/tortuga.png", GRAF_TORTUGA_REFX, GRAF_TORTUGA_REFY );
		lTortugaIni = new JLabelGrafico( (int)GRAF_TORTUGA_ANCHO, (int)GRAF_TORTUGA_ALTO, "../../utils/img/tortuga.png", GRAF_TORTUGA_REFX, GRAF_TORTUGA_REFY );
		lTortugaFin = new JLabelGrafico( (int)GRAF_TORTUGA_ANCHO, (int)GRAF_TORTUGA_ALTO, "../../utils/img/tortuga.png", GRAF_TORTUGA_REFX, GRAF_TORTUGA_REFY );
		lTortugaIni.setTransparencia( 0.6f ); lTortugaIni.setVisible( false );
		lTortugaFin.setTransparencia( 0.6f ); lTortugaFin.setVisible( false );
		pCarrera.add( lTortuga ); pCarrera.add( lTortugaIni ); pCarrera.add( lTortugaFin );
		pCarrera.add( lAquiles ); pCarrera.add( lAquilesIni ); pCarrera.add( lAquilesFin );
		lMetrosIni = new JLabel( "0" );
		lMetrosFin = new JLabel( "" + vistaPantalla ); lMetrosFin.setHorizontalAlignment( SwingConstants.RIGHT );
		lMetrosIni.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
		lMetrosFin.setFont( new Font( "Arial", Font.PLAIN, 18 ) );
		pCarrera.add( lMetrosIni );
		pCarrera.add( lMetrosFin );
		ventana.setProcesoParada( AquilesYLaTortugaEnVentana::parada );
		ventana.setProcesoRepintado( AquilesYLaTortugaEnVentana::recolocaCarrera );
		ventana.getPanelConfig().setLayout( new BorderLayout() );
		ventana.getPanelConfig().add( panelConf, BorderLayout.CENTER );
		ventana.getPanelConfig().revalidate();
		ventana.getPanelConfig().repaint();
	}
	public static void activaEdicion( boolean activa ) {
		JComponent[] comps = { panelConf.tfIniAquiles, panelConf.tfIniTortuga, panelConf.tfVelAquiles, panelConf.tfVelTortuga, panelConf.tfVistaPantalla, panelConf.tfTiempoFinal };
		for (JComponent comp : comps) comp.setEnabled( activa );
	}
	private static void parada() {
		lAquilesIni.setVisible( false ); lAquilesFin.setVisible( false );
		lTortugaIni.setVisible( false ); lTortugaFin.setVisible( false );
		tiempo = 0;
		tiempoInicial = 0;
		try {
			tiempoFinal = Double.parseDouble( panelConf.tfTiempoFinal.getText() );
		} catch (NumberFormatException ex) {}
		posAquiles = dondeEstaAquiles( tiempo );
		posTortuga = dondeEstaLaTortuga( tiempo );
		ponAquiles( posAquiles );
		ponTortuga( posTortuga );
		recolocaCarrera();
	}
	private static void recolocaAproxSuc( boolean mostrar ) {
		if (mostrar) {
			double tiempoMedio = (tiempoInicial + tiempoFinal) / 2;
			lAquilesIni.setVisible( true ); lAquilesFin.setVisible( true );
			lTortugaIni.setVisible( true ); lTortugaFin.setVisible( true );
			muestraAquiles( lAquilesIni, tiempoInicial );
			muestraAquiles( lAquilesFin, tiempoFinal );
			muestraLaTortuga( lTortugaIni, tiempoInicial );
			muestraLaTortuga( lTortugaFin, tiempoFinal );
			ponAquiles( dondeEstaAquiles( tiempoMedio ) );
			ponTortuga( dondeEstaLaTortuga( tiempoMedio ) );
			if (posAquiles<posTortuga) {
				tiempoInicial = tiempoMedio;
			} else {
				tiempoFinal = tiempoMedio;
			}
			ventana.ponMensaje( String.format( "Tiempo medio %1$,5.3f . Aquiles en %2$,5.3f - tortuga en %3$,5.3f", tiempoMedio, posAquiles, posTortuga ) );
		} else {
			lAquilesIni.setVisible( false ); lAquilesFin.setVisible( false );
			lTortugaIni.setVisible( false ); lTortugaFin.setVisible( false );
		}
	}
	
	private static void recolocaCarrera() {
		lMetrosIni.setBounds( 0, pCarrera.getHeight() - 40, 100, 40 );
		lMetrosFin.setBounds( pCarrera.getWidth() - 100, pCarrera.getHeight() - 40, 100, 40 );
		ponAquiles( posAquiles );
		ponTortuga( posTortuga );
	}
	public static void ponAquiles( double posAquiles ) {
		AquilesYLaTortugaEnVentana.posAquiles = posAquiles;
		lAquiles.setLocation( (int) (posAquiles / vistaPantalla * pCarrera.getWidth() - GRAF_AQUILES_REFX),
				              (int) (pCarrera.getHeight() - 50 - GRAF_AQUILES_REFY) );
	}
	public static void muestraAquiles( JLabel labelAquiles, double tAquiles ) {
		double posAquiles = dondeEstaAquiles( tAquiles );
		labelAquiles.setLocation( (int) (posAquiles / vistaPantalla * pCarrera.getWidth() - GRAF_AQUILES_REFX),
				                  (int) (pCarrera.getHeight() - 50 - GRAF_AQUILES_REFY) );
	}
	public static void ponTortuga( double posTortuga ) {
		AquilesYLaTortugaEnVentana.posTortuga = posTortuga;
		lTortuga.setLocation( (int) (posTortuga / vistaPantalla * pCarrera.getWidth() - GRAF_TORTUGA_REFX),
	                          (int) (pCarrera.getHeight() - 50 - GRAF_TORTUGA_REFY) );
	}
	public static void muestraLaTortuga( JLabel labelTortuga, double tTortuga ) {
		double posTortuga = dondeEstaLaTortuga( tTortuga );
		labelTortuga.setLocation( (int) (posTortuga / vistaPantalla * pCarrera.getWidth() - GRAF_TORTUGA_REFX),
				                  (int) (pCarrera.getHeight() - 50 - GRAF_TORTUGA_REFY) );
	}
	
	
	@SuppressWarnings("serial")
	public static class JLabelGrafico extends JLabel {
		double refx = -1, refy = -1;   // Punto de referencia  (negativo si no existe)
		int imagenAncho = -1, imagenAlto = -1;  // Tamaño original imagen
		float opacidad = -1.0f;  // Opacidad (negativo = no se considera. En caso contrario en rango 0-1)
		
		/** Construye y devuelve el JLabel del gráfico con el tamaño y nombre de fichero dado
		 * @param ancho	Ancho en pixels
		 * @param alto	Alto en pixels
		 * @param ficGrafico	Nombre de recurso partiendo de esta clase
		 */
		public JLabelGrafico( int ancho, int alto, String ficGrafico ) {
			try {
				setIcon( new ImageIcon( es.deusto.prog3.cap04.ejercicios.AquilesYLaTortuga.class.getResource( ficGrafico) ) );
			} catch (Exception e) {
				System.err.println( "Error en carga de recurso: " + ficGrafico + " no encontrado" );
				e.printStackTrace();
			}
			setBounds( 0, 0, ancho, alto );
		}
		
		/** Construye y devuelve el JLabel del gráfico con el tamaño y nombre de fichero dado
		 * y con punto de referencia que se marcará en el dibujado
		 * @param ancho	Ancho en pixels
		 * @param alto	Alto en pixels
		 * @param ficGrafico	Nombre de recurso partiendo de esta clase
		 */
		public JLabelGrafico( int ancho, int alto, String ficGrafico, double refx, double refy ) {
			try {
				setIcon( new ImageIcon( es.deusto.prog3.cap04.ejercicios.AquilesYLaTortuga.class.getResource( ficGrafico) ) );
				imagenAncho = ancho; imagenAlto = alto;
				this.refx = refx; this.refy = refy;
			} catch (Exception e) {
				System.err.println( "Error en carga de recurso: " + ficGrafico + " no encontrado" );
				e.printStackTrace();
			}
			setBounds( 0, 0, ancho, alto );
		}
		
		// giro
		private double miGiro = 0; 
		/** Cambia el giro del gráfico de JLabel
		 * @param gradosGiro	Grados a los que tiene que "apuntar" el coche,
		 * 						considerados con el 0 en el eje OX positivo,
		 * 						positivo en sentido antihorario, negativo horario.
		 */
		public void setGiro( double gradosGiro ) {
			miGiro = gradosGiro/180*Math.PI;   // De grados a radianes...
			miGiro = -miGiro;  // Cambio el sentido del giro (el giro en la pantalla es en sentido horario (inverso))
			miGiro = miGiro + Math.PI/2; // Sumo 90º para que corresponda al origen OX (el gráfico del coche apunta hacia arriba (en lugar de derecha OX))
		}

		/** Cambia la opacidad de la imagen
		 * @param opacity	Valor de opacidad en el rango: 0.0f = transparente, 1.0f = opaco
		 */
		public void setTransparencia( float opacidad ) {
			this.opacidad = opacidad;
		}
		
		// Redefinición del paintComponent para que se escale y se rote el gráfico
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
			Image img = ((ImageIcon)getIcon()).getImage();
			// Escalado más fino con estos 3 parámetros:
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
	        g2.rotate( miGiro, getWidth()/2, getHeight()/2 );  // Prepara rotación
	        Composite old = g2.getComposite();
	        if (opacidad>=0.0f) {
		        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidad));
	        }
	        g2.drawImage( img, 0, 0, getWidth(), getHeight(), null );  // Dibujado de la imagen
	        g2.setComposite(old);	        
	        if (refx>=0 && refy>=0) {  // Si hay punto de referencia lo dibuja
	        	double centrox = refx/imagenAncho*getWidth();
	        	double centroy = refy/imagenAlto*getHeight();
	        	g2.setColor( Color.green );
	        	g2.setStroke( new BasicStroke( 1.5f ) );
	        	g2.fillOval( (int)(centrox-4), (int)(centroy-4), 8, 8 );
	        	g2.drawOval( (int)(centrox-6), (int)(centroy-6), 12, 12 );
	        }
		}
	}

	
	/** Devuelve la posición de Aquiles en la carrera, dado el tiempo transcurrido
	 * @param t	Tiempo transcurrido de carrera (en sgs)
	 * @return	Posición de Aquiles (en m)
	 */
	public static double dondeEstaAquiles( double t ) {
		return INICIO_AQUILES + VEL_AQUILES * t;
	}

	/** Devuelve la posición de la tortuga en la carrera, dado el tiempo transcurrido
	 * @param t	Tiempo transcurrido de carrera (en sgs)
	 * @return	Posición de la tortuga (en m)
	 */
	public static double dondeEstaLaTortuga( double t ) {
		return INICIO_TORTUGA + VEL_TORTUGA * t;
	}
	

	// Algoritmo recursivo resuelto
	
	private static int numLlams = 0;
	// Algoritmo recursivo resuelto
	// Pre: en tIni Aquiles no ha alcanzado a la tortuga
	// Pre: en tFin Aquiles ha pasado a la tortuga
	public static double cuandoSeEncuentran( double tIni, double tFin ) {
		numLlams++; // Auxiliar para contar el número de llamadas
		if (Math.abs(tIni-tFin) < 0.0001) {
			return tIni;
		} else {
			double tMedio = (tIni+tFin)/2;
			posAquiles = dondeEstaAquiles( tMedio );
			posTortuga = dondeEstaLaTortuga( tMedio );
			ventana.ponMensaje( String.format( "Tiempo %1$,5.3f . Aquiles en %2$,5.3f - tortuga en %3$,5.3f - Llamadas: %4$1d", tMedio, posAquiles, posTortuga, numLlams ) );
			recolocaAproxSuc( true );
			ventana.hazPausa();
			if (posAquiles <= posTortuga) {
				return cuandoSeEncuentran( tMedio , tFin );
			} else {
				return cuandoSeEncuentran( tIni, tMedio );
			}
		}
	}
	
	public static void solucionRec() {
		cuandoSeEncuentran( 0, tiempoFinal );
	}
	
	public static void solucionIte() {
		activaEdicion( false );
		tiempo = 0;
		posAquiles = 0;
		while (ventana.isAlive() && posAquiles<posTortuga && posAquiles<vistaPantalla && posTortuga<vistaPantalla) {
			posAquiles = dondeEstaAquiles( tiempo );
			posTortuga = dondeEstaLaTortuga( tiempo );
			ventana.ponMensaje( String.format( "Tiempo %1$,5.3f . Aquiles en %2$,5.3f - tortuga en %3$,5.3f", tiempo, posAquiles, posTortuga ) );
			ventana.hazPausa();
			tiempo = tiempo + 0.01;
		}
		activaEdicion( true );
		ventana.stop();
	}
	
	
	public static void main(String[] args) {
		ventana = new VisualizaProceso();
		init();
		recolocaCarrera();
		while (ventana.isAlive()) {
			if (ventana.isRunning()) {
				solucionIte();
			} else if (ventana.isStepByStep()) {
				recolocaAproxSuc( true );
				ventana.hazPausa();
				solucionRec();
				recolocaAproxSuc( false );
			} else {
				try { Thread.sleep(1); } catch (InterruptedException e) { }
			}
		}
	}
	
	
}
