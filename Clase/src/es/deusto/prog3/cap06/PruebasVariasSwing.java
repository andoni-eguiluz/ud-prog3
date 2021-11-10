package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

/** Pruebas varias de aspectos puntuales de Swing
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class PruebasVariasSwing {

	private static void pruebaTextoConTTF() {
		String nombreFont = "SpecialElite.ttf";  // u otro ejemplo "Hack-Regular.ttf"
		JFrame v= new JFrame("Texto desde TTF");
		v.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			Font f = Font.createFont( Font.TRUETYPE_FONT,
				PruebasVariasSwing.class.getResourceAsStream(nombreFont) );
			f = f.deriveFont( Font.PLAIN, 24 );
			JTextArea ta = new JTextArea( 10, 40 );
			ta.setFont( f );
			ta.setText( "Prueba de texto\ncon tipo de letra " + nombreFont + "\ncargado desde ttf");
			v.getContentPane().add( new JScrollPane(ta), BorderLayout.CENTER );
			v.pack();
			v.setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog( null, "Error en carga de ttf" );
		}
	}
	
		private static JEditorPane ep;
		private static String textoHtml;
	private static void pruebaTextoEnriquecidoConImagenes() {
		JFrame v= new JFrame("HelloWorldSwing");
		v.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// String imgsrc = PruebasVariasSwing.class.getClassLoader().getSystemResource("img/bicho.png").toString();
		String imgsrc = PruebasVariasSwing.class.getResource("img/bicho.png").toString();
		// String imgsrc = "img/bicho.png";
		textoHtml = "<html>Hola imagen <img src='" + imgsrc + "' width=100 height=100></img> bienvenida!";
		ep = new JEditorPane("text/html", textoHtml + "<html>" );
		ep.setPreferredSize( new Dimension( 800, 600 ) );
		v.getContentPane().add( new JScrollPane(ep), BorderLayout.CENTER );
		JButton b = new JButton( "Pulsa para añadir imagen" );
		v.getContentPane().add( b, BorderLayout.SOUTH );
		b.addActionListener( (e) -> {
			String img = PruebasVariasSwing.class.getResource("img/bicho.png").toString();
			textoHtml = textoHtml + "<br/><img src='" + img + "' width=200 height=200></img> y tú también!";
			ep.setText( textoHtml + "</html>" );
		});
		v.pack();
		v.setVisible(true);
	}
	
		private static Rectangle tamanyoPanel = null;
		private static HashMap<Object,Rectangle> tamComponentes = new HashMap<>();
	private static void pruebaReajusteLayoutNulo() {
		JFrame miJFrame = new JFrame();
		miJFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		miJFrame.setTitle ( "Ventana con reajuste por programa de tamaños en layout nulo" );
		miJFrame.setSize( 640, 480 );
		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout( null );
		// Añadimos un par de componentes de ejemplo para el tema del reajuste
			JLabelGrafico bicho = new JLabelGrafico( 200, 150, "img/bicho.png" );
			bicho.setLocation( 200, 100 );
			panelPrincipal.add( bicho );
			JLabel texto = new JLabel( "Redimensiona la ventana y observa los cambios" );
			texto.setBounds( 20, 0, 300, 40 );
			panelPrincipal.add( texto );
			JTextField tfEjemplo = new JTextField( "ejemplo JTextField" );
			tfEjemplo.setBounds( 50, 150, 100, 30 );
			panelPrincipal.add( tfEjemplo );
		// Eventos para gestionar el reescalado
		miJFrame.addWindowListener( new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {  // Al activarse la ventana almacenamos el tamaño del panel
				tamanyoPanel = panelPrincipal.getBounds();
				for (Component c : panelPrincipal.getComponents()) {
					tamComponentes.put( c, c.getBounds() );  // Guardamos el tamaño y posición inicial de cada componente para luego escalarlo con él
				}
			}
		});
		panelPrincipal.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {  // Al redimensionarse el panel, reajustamos sus componentes
				if (panelPrincipal!=null && tamanyoPanel!=null) {
					double escalaX = panelPrincipal.getWidth() / tamanyoPanel.getWidth();   // Nueva escala X
					double escalaY = panelPrincipal.getHeight() / tamanyoPanel.getHeight(); // Nueva escala Y
					for (Component c : panelPrincipal.getComponents()) {
						Rectangle tamanyoInicial = tamComponentes.get( c );
						if (c!=null) {
							c.setSize( new Dimension( (int) (tamanyoInicial.getWidth()*escalaX), (int)(tamanyoInicial.getHeight()*escalaY) ) );
							c.setLocation( (int) (tamanyoInicial.getX()*escalaX), (int)(tamanyoInicial.getY()*escalaY) );
						}
					}
				}
			}
		});
		miJFrame.getContentPane().add( panelPrincipal, BorderLayout.CENTER );  // El panel ocupa siempre toda la ventana y se reescala con ella
		miJFrame.setVisible( true );
	}
	
		@SuppressWarnings("serial")
		private static class JLabelGrafico extends JLabel {
			/** Construye y devuelve el JLabel del gráfico con el tamaño y nombre de fichero dado
			 * @param ancho	Ancho en pixels
			 * @param alto	Alto en pixels
			 * @param ficGrafico	Nombre de recurso partiendo de esta clase
			 */
			public JLabelGrafico( int ancho, int alto, String ficGrafico ) {
				try {
					setIcon( new ImageIcon( JLabelGrafico.class.getResource( ficGrafico) ) );
				} catch (Exception e) {
					System.err.println( "Error en carga de recurso: " + ficGrafico + " no encontrado" ); e.printStackTrace();
				}
				setBounds( 0, 0, ancho, alto );
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
		        g2.drawImage( img, 0, 0, getWidth(), getHeight(), null );  // Dibujado de la imagen
			}
		}

	private static void pruebaDecoraciones() {
		JFrame miJFrame = new JFrame();
		miJFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		miJFrame.setUndecorated( false );
		miJFrame.setTitle ( "Ventana decorada" );
		miJFrame.setSize( 320, 200 );
		miJFrame.setVisible( true );
		
		miJFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		miJFrame.setTitle ( "Ventana con internal frames" );
		miJFrame.setSize( 640, 400 );
		miJFrame.setVisible( true );
		JDesktopPane fondo = new JDesktopPane();
		miJFrame.setContentPane( fondo );
		
		JInternalFrame miFrameInterno1 = new JInternalFrame();
		miFrameInterno1.setTitle( "JFinterno 1 con iconos" );
		miFrameInterno1.setIconifiable( true );
		miFrameInterno1.setMaximizable( true );
		miFrameInterno1.setClosable( true );
		miFrameInterno1.setBounds( 10, 10, 280, 160);
		JInternalFrame miFrameInterno2 = new JInternalFrame();
		miFrameInterno2.setTitle( "JFinterno 2 por defecto" );
		// miFrameInterno2.setIconifiable( false );
		// miFrameInterno2.setMaximizable( false );
		// miFrameInterno2.setClosable( true );
		miFrameInterno2.setBounds( 300, 50, 280, 160);
		fondo.add( miFrameInterno1 );
		fondo.add( miFrameInterno2 );
		miFrameInterno1.setVisible( true );
		miFrameInterno2.setVisible( true );
		JOptionPane.showInternalConfirmDialog( fondo, "Hola" );  // JOptionPane internal
	}

		// Usa JPanelConFondo
	private static void pruebaVentanaConGraficoDeFondo() {
		JFrame prueba = new JFrame();
		prueba.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		prueba.setSize( 600, 500 );
		JPanel panelFondo = new JPanelConFondo( "bicho.png" );
		panelFondo.setLayout( new BorderLayout() );
		JButton botonPrueba = new JButton( "Botón de prueba encima de panel con imagen" );
		botonPrueba.setOpaque( false );
		panelFondo.add( botonPrueba, "North" );
		prueba.getContentPane().add( panelFondo, "Center" );
		prueba.setVisible( true );
	}

	private static void pruebaCentradoVertical() {
		// Con GridLayout
		JFrame miJFrame = new JFrame();
			JLabel etiq1 = new JLabel("Prueba etiqueta");
			miJFrame.getContentPane().setLayout( new GridLayout(1,3) );
			etiq1.setBorder( BorderFactory.createLineBorder( Color.RED ));
		miJFrame.getContentPane().add( etiq1 );
			JTextArea ta1 = new JTextArea("Prueba textArea");
			ta1.setBorder( BorderFactory.createLineBorder( Color.RED ));
			ta1.setAlignmentY( SwingConstants.CENTER );
		miJFrame.getContentPane().add( ta1 );
			JPanel panel = new JPanel();
			panel.setAlignmentY( SwingConstants.CENTER );
				JLabel etiq2 = new JLabel("Prueba etiqueta en panel");
				etiq2.setAlignmentY( SwingConstants.CENTER );
				etiq2.setBorder( BorderFactory.createLineBorder( Color.RED ));
			panel.add( etiq2 );
		miJFrame.getContentPane().add( panel );
		miJFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		miJFrame.setTitle ( "Alineación vertical en GridLayout" );
		miJFrame.setSize( 320, 200 );
		miJFrame.setVisible( true );
		// Con GridBagLayout
	    JFrame frame = new JFrame();
	    frame.setLayout(new GridBagLayout());
	    JPanel panelC = new JPanel();
	    	JLabel etiq3 = new JLabel("Etiqueta en panel en GridBagLayout");
	    	etiq3.setBorder( BorderFactory.createLineBorder( Color.RED ));
	    panelC.add( etiq3 );
	    panelC.setBorder(BorderFactory.createLineBorder( Color.black ));
	    frame.add(panelC, new GridBagConstraints());  // Lo pone por defecto - centrado en hor y vert.
	    frame.setSize(400, 400);
		frame.setTitle ( "Alineación vertical en GridBagLayout" );
	    frame.setLocationRelativeTo(null);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setVisible(true);

	    // Con BoxLayout
			    JPanel panelC2 = new JPanel();
		    	JLabel etiq4 = new JLabel("Etiqueta en panel en BoxLayout");
		    	etiq4.setBorder( BorderFactory.createLineBorder( Color.RED ));
		    panelC2.add( etiq4 );
		    panelC2.setBorder(BorderFactory.createLineBorder( Color.black ));
	    Box verticalBox = Box.createVerticalBox();  // Box
	    verticalBox.add(Box.createVerticalGlue());
	    verticalBox.add(panelC2);
	    verticalBox.add(Box.createVerticalGlue());
	    JFrame vent3 = new JFrame();
	    vent3.add( verticalBox );  // Lo pone por defecto - centrado en hor y vert.
	    vent3.setSize(400, 400);
		vent3.setTitle ( "Alineación vertical en BoxLayout" );
	    vent3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    vent3.setVisible(true);
	}

	private static JLabel mens = new JLabel();
	private static JLabel mens2 = new JLabel();
	private static long tiempo;
	public static void pruebaTiempoDeFont() {
		JFrame vent = new JFrame();
		vent.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		vent.setSize( 600, 500 );
		vent.setTitle( "Ventana sin font");
		vent.add( mens, BorderLayout.NORTH );
		vent.addWindowListener( new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				mens.setText( (System.currentTimeMillis()-tiempo) + " milisegundos. ");
			}
		});
		tiempo = System.currentTimeMillis();
		vent.setVisible( true );
		try { Thread.sleep(1000); } catch (Exception e) {}
		JFrame vent2 = new JFrame();
		vent2.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		vent2.setSize( 600, 500 );
		vent2.setTitle( "Ventana con font");
		mens2.setFont( new Font( "Courier", 50, 50 ) );
		vent2.add( mens2, BorderLayout.NORTH );
		vent2.addWindowListener( new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				mens2.setText( (System.currentTimeMillis()-tiempo) + " milisegundos. ");
			}
		});
		tiempo = System.currentTimeMillis();
		vent2.setVisible( true );
	}

	/** Devuelve el contenedor principal del componente indicado
	 * @param c	Cualquier componente visual
	 * @return	Devuelve el contenedor de primer nivel al final de su cadena de contenedores
	 */
	public static Container getContenedorPrincipal( Component c ) {
		if (c==null) return null;
		Container ret = c.getParent();
		while (ret != null) {
			c = ret;
			ret = ret.getParent();
		}
		return (Container) c;
	}
	
	private static void pruebaConsultorContenedorPrincipal() {
		JFrame f = new JFrame( "Prueba contenedor principal" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize(300,200);
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JLabel lTest = new JLabel( "Etiqueta test" );
		f.add( p1 );
		p1.add( p2 );
		p2.add( p3 );
		p3.add( lTest );
		f.setVisible( true );
		System.out.println( getContenedorPrincipal( lTest ) );
	}
	
	private static JTextField tfTexto;
	private static void pruebaLimitarJTextField() {
		JFrame f = new JFrame( "Prueba cuadro de texto limitado" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize(300,80);
		JLabel lTest = new JLabel( "Introduce texto limitado:" );
		f.add( lTest, BorderLayout.NORTH );
		tfTexto = new JTextField( "", 10 );
		f.add( tfTexto, BorderLayout.SOUTH );
		f.setVisible( true );
		tfTexto.addKeyListener( new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent ev) {
				if (tfTexto.getText().length() >= 3)
					// Aquí se controla que la longitud no sea mayor que 3
					// Se podría controlar cualquier otra cosa
					ev.consume(); // Si se consume el evento, no llega
						// al proceso estándar del cuadro de texto, y
						// por tanto no llega a introducirse en el cuadro
			}
		});
	}
	
	private static void pruebaJOptionPaneConComponentesPersonalizados() {
		Object[] varios = new Object[4];
		varios[0] = "Un string (label)";
		varios[1] = new JTextField( "Un textfield" );
		varios[2] = new JButton( "Un botón con evento" );
		((JButton)varios[2]).addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println( "Pulsado");
			}
		});
		varios[3] = new JScrollPane( new JTextArea( "Un textarea con scrollpane                     \n\n\n\n\n", 4, 10 ) );
		JOptionPane.showConfirmDialog( null, varios, "Mensajes variopintos", JOptionPane.YES_NO_CANCEL_OPTION );
		System.out.println( ((JTextField)varios[1]).getText() );
	}
	
	private static void pruebaJPassword() {
		JFrame f = new JFrame( "Prueba JPassword" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JPasswordField pass = new JPasswordField( 10 );
		f.add( pass, BorderLayout.NORTH );
		JTextArea taVal = new JTextArea( 10, 40 );
		f.add( taVal, BorderLayout.CENTER );
		pass.addActionListener( (e) -> {
			taVal.setText( 
				"getText() (deprecated) = " + pass.getText() +
				"\ngetPassword() = " + pass.getPassword() + " (array de chars)" +
				"\nnew String(getPassword()) = " + new String(pass.getPassword())
			);
		});
		f.pack();
		f.setLocationRelativeTo( null );
		f.setVisible( true );
	}
	
		
	public static void main( String[] s ) {
		JFrame vP = new JFrame();
		vP.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JPanel p = new JPanel();
		p.setLayout( new GridLayout(10,1) );
		vP.getContentPane().add( p, BorderLayout.CENTER );
		JButton b;
		p.add( b = new JButton( "texto con ttf") );
		// Prueba de texto enriquecido con imágenes
			b.addActionListener( (e) -> { pruebaTextoConTTF(); } ); 
		p.add( b = new JButton( "texto con imagen") );
			// Prueba de texto enriquecido con imágenes
			b.addActionListener( (e) -> { pruebaTextoEnriquecidoConImagenes(); } ); 
		p.add( b = new JButton( "Reajuste tamaño con layout nulo con eventos") );
			// Prueba de reajuste programático de layout nulo en cambios de escala
			b.addActionListener( (e) -> { pruebaReajusteLayoutNulo(); } );
		p.add( b = new JButton( "Decoración JFrame y JInternalFrame") );
			// Decoración JFrame y JInternalFrame
			b.addActionListener( (e) -> { pruebaDecoraciones(); } );
		p.add( b = new JButton( "Fondo de pantalla con gráfico") );
			// Fondo de pantalla con un gráfico
			b.addActionListener( (e) -> { pruebaVentanaConGraficoDeFondo(); } );  // ver clase JPanelConFondo
		p.add( b = new JButton( "Centrado vertical con GridLayout y con GridBagLayout") );
			// Centrado vertical con GridLayout y con GridBagLayout
			b.addActionListener( (e) -> { pruebaCentradoVertical(); } );
		p.add( b = new JButton( "tiempo que tarda una ventana con font") );
			// Prueba de tiempo que tarda una ventana y tiempo que tarda Font
			b.addActionListener( (e) -> { pruebaTiempoDeFont(); } );
		p.add( b = new JButton( "consultor de contenedor principal") );
			// Prueba de consultor de contenedor principal
			b.addActionListener( (e) -> { pruebaConsultorContenedorPrincipal(); } );
		p.add( b = new JButton( "textfield con entrada limitada") );
			// Prueba de text field con entrada limitada
			b.addActionListener( (e) -> { pruebaLimitarJTextField(); } );
		p.add( b = new JButton( "JOptionPane con componentes personalizados") );
			// Prueba de JOptionPane personalizado
			b.addActionListener( (e) -> { pruebaJOptionPaneConComponentesPersonalizados(); } );
		p.add( b = new JButton( "Valor de JPasswordField") );
			// Ver valor de JPasswordField
			b.addActionListener( (e) -> { pruebaJPassword(); } );
		vP.pack();
		vP.setVisible( true );
	}
}



@SuppressWarnings("serial")
class JPanelConFondo extends JPanel {

	private BufferedImage imagenOriginal;
	public JPanelConFondo( String nombreImagenFondo ) {
        URL imgURL = getClass().getResource("img/" + nombreImagenFondo);
        try {
        	imagenOriginal = ImageIO.read( imgURL );
        } catch (IOException e) {
        }
	}

	protected void paintComponent(Graphics g) {
		Rectangle espacio = g.getClipBounds();  // espacio de dibujado del panel
		// setBounds( espacio );
		//super.paintComponent(g);  en vez de esto...
		Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
		// Código para que el dibujado se reescale al área disponible
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
		// Dibujado
		g2.drawImage(imagenOriginal, 0, 0, (int)espacio.getWidth(), (int)espacio.getHeight(), null);
	}

}
