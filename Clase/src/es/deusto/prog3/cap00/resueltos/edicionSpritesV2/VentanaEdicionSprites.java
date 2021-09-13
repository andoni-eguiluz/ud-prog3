package es.deusto.prog3.cap00.resueltos.edicionSpritesV2;

// Iconos descargados de https://www.freepik.com/free-icons
// (en alud - "Gráficos de ejemplo para ejercicio de edición de sprites")

// Imports clásicos en casi cualquier programa de ventanas con Swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Import para ficheros
import java.io.*;
//Import para Java Collections y otras utilidades
import java.util.*;


/** Ejercicio de creación de ventanas complejas (versión 1 - resuelto solo lo visual)
 * En este mismo paquete verás un fichero 
 *   <a href="file:./ej-sprites-1.jpg">ej-sprites-1.jpg</a>
 * con una propuesta de ventana. Diseña con las variantes que veas oportuna esa ventana (en principio, sin programarla).<br/>
 * Hay otro fichero con marcas:
 *   <a href="file:./ej-sprites-2.jpg">ej-sprites-2.jpg</a>
 * sobre el que puedes observar los comentarios de cada parte de la ventana. Lo que se pretende es:<br/>
 *  - Objetivo de la ventana: editar y visualizar animaciones de sprites partiendo de gráficos ya existentes<br/>
 *  - Funcionamiento: al pulsar el botón (2) aparecerá una selección de ficheros (JFileChooser) que permitirá
 *    seleccionar una CARPETA cualquiera del disco <br/>
 *  - Los ficheros png/jpg/gif [recomendado pngs transparentes] que estén en esa carpeta aparecerán en (1) <br/>
 *  - (1) es un JList con desplazamiento (dentro de un JScrollPane) donde aparecerán los nombres de ficheros existentes en la carpeta <br/>
 *  - Entre el botón (2) y la lista (1) aparece el nombre de la carpeta seleccionada (JLabel)<br/>
 *  - Al cambiar la carpeta se crea una selección nueva vacía (lista (5)) <br/>
 *  - Al hacer doble click en un fichero de (1) se añade al final de la lista (5) <br/>
 *  - Al seleccionar un fichero en (5) se ve en el panel de preview (3), centrado con respecto al panel <br/>
 *  - Los componentes que hay a la derecha de la lista (5) tienen los siguientes significados: <br/>
 *  &nbsp;&nbsp;- Un deslizador (JSlider) de nivel de zoom, desde 10% hasta 200% <br/>
 *  &nbsp;&nbsp;- Un deslizador (JSlider) de rotación, desde 0º hasta 360º <br/>
 *  &nbsp;&nbsp;- Un JTextField de desplazamiento a derecha de pixels (0 por defecto) <br/>
 *  &nbsp;&nbsp;- Un JTextField de desplazamiento abajo de pixels (0 por defecto) <br/>
 *  &nbsp;&nbsp;- Una indicación de milisegundos de cada imagen dentro de la secuencia del sprite (100 por defecto) <br/>
 *  &nbsp;&nbsp;Estos valores indicados configuran cada imagen dentro de la secuencia (y por tanto deben almacenarse y restaurarse por cada imagen de la secuencia). Lo demás es para toda la secuencia:<br/>
 *  &nbsp;&nbsp;- Checkbox de ciclo (al visualizar la secuencia, tras la última imagen vuelve a empezar la primera) <br/>
 *  &nbsp;&nbsp;- Botones de arriba/abajo que suben o bajan una posición la imagen seleccionada con respecto al resto de la lista (5) <br/>
 *  &nbsp;&nbsp;- Debajo de (5) dos JTextField de ancho y alto en píxels de toda la secuencia <br/>
 *  &nbsp;&nbsp;- (6) Botones de nuevo/save/load para reiniciar, guardar o cargar una secuencia configurada (en save y load aparecerá un cuadro de diálogo que pide el nombre y localización) <br/>
 *  - (7) es un panel donde se define la animación de movimiento del sprite en la "arena" (4). Su contenido es: <br/>
 *  &nbsp;&nbsp;- Píxels de origen x e y de la animación dentro de la arena<br/>
 *  &nbsp;&nbsp;- Velocidad inicial de la animación en píxels por segundo, con un cuadro de texto y un JSlider entre 0 y 200<br/>
 *  &nbsp;&nbsp;- Ángulo inicial de la animación en grados, con un cuadro de texto y un JSlider entre 0 y 90<br/>
 *  &nbsp;&nbsp;- Gravedad de la animación, con un JSlider entre 0.0 y 10.0  (9.8 por defecto)<br/>
 *  - (8) es un panel donde se complementa la animación de movimiento del sprite. Contenido: <br/>
 *  &nbsp;&nbsp;- Rotación de la animación en grados por segundo, con un cuadro de texto y un JSlider entre 0 y 360 (0 por defecto)<br/>
 *  &nbsp;&nbsp;- Zoom de la animación en % por segundo, con un cuadro de texto y un JSlider entre 50 y 200 (100 por defecto)<br/>
 *  &nbsp;&nbsp;- Checkbox de si la animación se hace cíclica (infinita) <br/>
 *  &nbsp;&nbsp;- Checkbox de si la animación se hace con retorno (va y vuelve) <br/>
 *  - (9) son los tres botones de animación:<br/>
 *  &nbsp;&nbsp;- Anima solo la secuencia sin mover el sprite en la arena<br/>
 *  &nbsp;&nbsp;- Hace el movimiento sin animar la secuencia (toma el sprite seleccionado actualmente)<br/>
 *  &nbsp;&nbsp;- Realiza a la vez las dos animaciones, la de secuencia y la de movimiento<br/>
 *  Otras anotaciones:<br/>
 *  - Valora los layouts más adecuados para cada panel. Intenta utilizar los más sencillos.<br/>
 *  - Los cuadros de texto que van con sliders asociados deben alimentarse mutuamente (si se cambia el slider cambia el texto y viceversa). Intenta hacerlo de una forma metódica en lugar de repitiendo código.<br/>
 *  - Haz una ventana interna (JInternalFrame) en lugar de un JFrame normal y así podría integrarse con otras ventanas de la misma aplicación<br/>
 *  - La Arena debería ocupar el máximo espacio posible de la ventana. El resto de los paneles el mínimo necesario.<br/>
 *  - En la zona (9) se podría añadir una JProgressBar que vaya mostrando la progresión de la animación en curso<br/>
 *  - Añade tooltips a las partes que consideres interesante<br/>
 *  - Si quieres probar los SplitPane puedes hacer uno entre (3) y (4)<br/>
 *  
 * <br/>
 * Programación posterior:<br/>
 *  Hay que definir modelos de datos para las listas (¿de qué tipo cada JList y cada modelo?)<br/>
 *  Hilos para los plays (¿cuántos? ¿cuándo?)<br/>
 *  Hay que usar alguna estructura (ArrayList por ejemplo) para guardar toda la configuración de la secuencia (¿varios arraylists o un arraylist de una clase nueva?)<br/>
 */
@SuppressWarnings("serial")  // Anotación para evitar el warning del serial (relacionado con Serializable -tema 3-)
public class VentanaEdicionSprites extends JFrame {  // Vamos a definir una clase que permite instanciar ventanas y por ello hereda de ventana
	// Atributos de componentes -todos los componentes a los que vamos a acceder o modificar los definimos como atributos para que sean fácilmente accesibles
	// Normalmente se hacen private pero aquí los hacemos de paquete para que pueda utilizarlos directamente el controlador
	JList<File> lSprites;  // Lista de sprites (1)
	JLabel lCarpetaSel;  // Texto de la carpeta seleccionada
	JButton bBuscar;  // Botón de búsqueda de carpeta
	JPanel pPreview;  // Panel de preview (3)
	JPanel pArena;  // Panel de "arena" (4)
	JList<File> lSecuencia;  // Lista de secuencia (5)
	JTextField tfAncho;  // cuadro de texto píxels de ancho
	JTextField tfAlto;  // cuadro de texto píxels de alto
	JSlider slZoom;  // Slider de zoom
	JSlider slRotacion;  // Slider de rotación
	JTextField tfoffsetX;  // cuadro de texto desplazamiento horizontal
	JTextField tfoffsetY;  // cuadro de texto desplazamiento vertical
	JTextField tfDuracion;  // cuadro de texto duración en milisegundos
	JCheckBox cbCiclo;  // Checkbox de ciclo
	JButton bArriba;  // Botón de desplazamiento arriba en secuencia
	JButton bAbajo;  // Botón de desplazamiento abajo en secuencia
	JButton bNueva;  // Botón de nueva secuencia
	JButton bGuardar;  // Botón de guardar secuencia
	JButton bCargar;  // Botón de cargar secuencia
	JTextField tfOrigenX;  // cuadro de texto desplazamiento horizontal origen
	JTextField tfOrigenY;  // cuadro de texto desplazamiento vertical origen
	JTextField tfVelocidad;  // cuadro de texto velocidad origen
	JSlider slVelocidad;  // Slider de velocidad origen
	JTextField tfAngulo;  // cuadro de texto ángulo origen
	JSlider slAngulo;  // Slider de ángulo origen
	JTextField tfGravedad;  // cuadro de texto gravedad
	JSlider slGravedad;  // Slider de gravedad
	JTextField tfRotacionAnim;  // cuadro de texto rotación animación
	JSlider slRotacionAnim;  // Slider de rotación animación
	JTextField tfZoomAnim;  // cuadro de texto zoom animación
	JSlider slZoomAnim;  // Slider de zoom animación
	JCheckBox cbCicloAnim;  // Checkbox de ciclo animación
	JCheckBox cbRetornoAnim;  // Checkbox de retorno animación
	JButton bSecuencia;  // Botón de play de secuencia
	JButton bAnim;  // Botón de play de animación
	JButton bSecuenciaAnim;  // Botón de play de secuencia+animación
	// Atributos de modelos de datos asociados a los componentes
	DefaultListModel<File> mSprites;
	DefaultListModel<File> mSecuencia;
	
	// Controlador de la ventana
	private ControladorVentanaSprites miControlador;
	
	/** Constructor de ventana de edición de sprites<br/>
	 * Inicializa y asocia un controlador ControladorVentanaSprites
	 */
	public VentanaEdicionSprites() {
		// Inicializa el controlador
		miControlador = new ControladorVentanaSprites( this );
		
		// Inicialización de elementos generales de la ventana
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE ); // Al cerrar la ventana se hace un dispose()
		setTitle( "Edición de sprites - Prog. III" );

		// Creación de contenedores con sus layouts
		pPreview = new JPanelConCentro( null );
		pArena = new JPanel( null );
		JPanel pi1 = new JPanel( new BorderLayout() );
		JPanel pi2 = new JPanel( new GridLayout(2,1) );
		JPanel pi3 = new JPanel();
		JPanel pC = new JPanel( new BorderLayout() );
		JSplitPane pCSup = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		JPanel pCSup1 = new JPanel( new BorderLayout() );
		JPanel pCSup2 = new JPanel( new BorderLayout() );
		JPanel pCInf = new JPanel( new BorderLayout() );
		JPanel pSec = new JPanel( new BorderLayout() );
		JPanel pSec1 = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
			JPanel pSec1a = new JPanel( new GridLayout( 2, 1 ) );
			JPanel pSec1b = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
			JPanel pSec1c = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		JPanel pSec2 = new JPanel( new BorderLayout() );
			JPanel pSec2a = new JPanel( new GridLayout( 8, 1 ) );  // Cambiado con respecto al diseño para mejor aspecto
			JPanel pSec2a1 = new JPanel( new FlowLayout( FlowLayout.LEFT, 1, 0 ) ); // Offset horizontal entre elementos 1 px, vertical 0 px
			JPanel pSec2a2 = new JPanel( new FlowLayout( FlowLayout.LEFT, 1, 0 ) );
			JPanel pSec2a3 = new JPanel( new FlowLayout( FlowLayout.LEFT, 1, 0 ) );
			JPanel pSec2a4 = new JPanel( new FlowLayout( FlowLayout.LEFT, 1, 0 ) );
			JPanel pSec2a5 = new JPanel( new FlowLayout( FlowLayout.CENTER, 1, 0 ) );
			JPanel pSec2a6 = new JPanel( new FlowLayout( FlowLayout.CENTER, 1, 0 ) );
		JPanel pSec3 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		JPanel pAnim = new JPanel( new BorderLayout() );
		JPanel pAnim1 = new JPanel( new BorderLayout() );
		JPanel pAnim2 = new JPanel( new BorderLayout() );
			JPanel pAnim2a = new JPanel( new GridLayout( 4, 1 ) );
			JPanel pAnim2b = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			JPanel pAnim2c = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			JPanel pAnim2d = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			JPanel pAnim2e = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		JPanel pAnim3 = new JPanel( new BorderLayout() );
			JPanel pAnim3a = new JPanel( new GridLayout( 4, 1 ) );
			JPanel pAnim3b = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			JPanel pAnim3c = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			JPanel pAnim3d = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
			JPanel pAnim3e = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		JPanel pAnim4 = new JPanel();
		
		// Creación de modelos de datos para los componentes
		mSprites = new DefaultListModel<>();
		mSecuencia = new DefaultListModel<>();
		
		// Creación de componentes
		lSprites = new JList<>( mSprites );
		JScrollPane spSprites = new JScrollPane( lSprites );
		lCarpetaSel = new JLabel( " " ); // Si tiene algún espacio ocupa el tamaño necesario de alto (luego se cambiará)
		bBuscar = new JButton( "Buscar" );
		lSecuencia = new JList<>( mSecuencia );
		JScrollPane spSecuencia = new JScrollPane( lSecuencia );
		tfAncho = new JTextField( "200", 3 );
		tfAlto = new JTextField( "200", 3 );
		slZoom = new JSlider( JSlider.HORIZONTAL, 10, 200, 100 );
		slRotacion = new JSlider( JSlider.HORIZONTAL, 0, 360, 0 );
		tfoffsetX = new JTextField( "0", 4 );
		tfoffsetY = new JTextField( "0", 4 );
		tfDuracion = new JTextField( "100", 4 );
		cbCiclo = new JCheckBox( "ciclo" );
		bArriba = new JButton( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/arrow-up24.png" )));
		bAbajo = new JButton( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/arrow-down24.png" )));
		bNueva = new JButton( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/new24.png" )));
		bGuardar = new JButton( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/save24.png" )));
		bCargar = new JButton( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/load24.png" )));
		tfOrigenX = new JTextField( "0", 3 );
		tfOrigenY = new JTextField( "100", 3 );
		tfVelocidad = new JTextField( "0", 3 );
		slVelocidad = new JSlider( JSlider.HORIZONTAL, 0, 200, 0 );
		tfAngulo = new JTextField( "0", 2 );
		slAngulo = new JSlider( JSlider.HORIZONTAL, 0, 90, 0 );
		tfGravedad = new JTextField( "9.8", 3 );
		slGravedad = new JSlider( JSlider.HORIZONTAL, 0, 100, 98 );
		tfRotacionAnim = new JTextField( "0", 3 );
		slRotacionAnim = new JSlider( JSlider.HORIZONTAL, 0, 360, 0 );
		tfZoomAnim = new JTextField( "100", 3 );
		slZoomAnim = new JSlider( JSlider.HORIZONTAL, 50, 200, 100 );
		cbCicloAnim = new JCheckBox( "Ciclo" );
		cbRetornoAnim = new JCheckBox( "Retorno" );
		bSecuencia = new JButton( "Sec", new ImageIcon( VentanaEdicionSprites.class.getResource( "img/play24.png" )));
		bAnim = new JButton( "Anim", new ImageIcon( VentanaEdicionSprites.class.getResource( "img/play24.png" )));
		bSecuenciaAnim = new JButton( "Sec+Anim", new ImageIcon( VentanaEdicionSprites.class.getResource( "img/play24.png" )));

		// Asignación de componentes a contenedores
		add( pi1, BorderLayout.WEST );
			pi1.add( new JLabel( "Sprites" ) );
			pi1.add( spSprites, BorderLayout.CENTER );
			pi1.add( pi2, BorderLayout.SOUTH );
				pi2.add( lCarpetaSel, BorderLayout.NORTH );
				pi2.add( pi3, BorderLayout.SOUTH );
					pi3.add( bBuscar );
		add( pC, BorderLayout.CENTER );
			pC.add( pCSup, BorderLayout.CENTER );
				pCSup.setLeftComponent( pCSup1 );
					pCSup1.add( pPreview, BorderLayout.CENTER );
				pCSup.setRightComponent( pCSup2 );
					pCSup2.add( pArena, BorderLayout.CENTER );
			pC.add( pCInf, BorderLayout.SOUTH );
				pCInf.add( pSec, BorderLayout.WEST );
					pSec.add( pSec1, BorderLayout.NORTH );
						pSec1.add( pSec1a );
					pSec.add( pSec2, BorderLayout.CENTER );
						pSec2.add( spSecuencia, BorderLayout.CENTER );
						pSec2.add( pSec2a, BorderLayout.EAST );
							pSec2a.add( pSec1b );  // Cambiado de panel para mejor aspecto (pSec1a a pSec2a)
								pSec1b.add( slZoom );
								pSec1b.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/zoom24.png" )) ) );
							pSec2a.add( pSec1c );  // Cambiado de panel para mejor aspecto (pSec1a a pSec2a)
								pSec1c.add( slRotacion );
								pSec1c.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/rotate24.png" )) ) );
							pSec2a.add( pSec2a1 );
								pSec2a1.add( tfoffsetX );
								pSec2a1.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/angle-arrow-right24.png" )) ));
							pSec2a.add( pSec2a2 );
								pSec2a2.add( tfoffsetY);
								pSec2a2.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/angle-arrow-down24.png" )) ));
							pSec2a.add( pSec2a3 );
								pSec2a3.add( tfDuracion );
								pSec2a3.add( new JLabel( "msg" ) );
							pSec2a.add( pSec2a4 );
								pSec2a4.add( cbCiclo );
							pSec2a.add( pSec2a5 );
								pSec2a5.add( bArriba );
								pSec2a5.add( bAbajo );
							pSec2a.add( pSec2a6 );
								pSec2a6.add( bNueva );
								pSec2a6.add( bGuardar );
								pSec2a6.add( bCargar );
					pSec.add( pSec3, BorderLayout.SOUTH );
						pSec3.add( tfAncho );
						pSec3.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/doublearrow-horizontal24.png" )) ));
						pSec3.add( tfAlto );
						pSec3.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/doublearrow-vertical24.png" )) ));
				pCInf.add( pAnim, BorderLayout.CENTER );
					pAnim.add( pAnim1, BorderLayout.CENTER );
						pAnim1.add( pAnim2, BorderLayout.WEST );
							pAnim2.add( pAnim2a, BorderLayout.NORTH );
								pAnim2a.add( pAnim2b );
									pAnim2b.add( new JLabel( "Origen" ) );
									pAnim2b.add( tfOrigenX );
									pAnim2b.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/arrow-right24.png" )) ) );
									pAnim2b.add( tfOrigenY );
									pAnim2b.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/arrow-down24.png" )) ) );
								pAnim2a.add( pAnim2c );
									pAnim2c.add( new JLabel( "V.ini" ) );
									pAnim2c.add( tfVelocidad );
									pAnim2c.add( new JLabel( "px/sg" ) );
									pAnim2c.add( slVelocidad );
								pAnim2a.add( pAnim2d );
									pAnim2d.add( new JLabel( "      " ) );
									pAnim2d.add( tfAngulo );
									pAnim2d.add( new JLabel( "º" ) );
									pAnim2d.add( new JLabel( new ImageIcon( VentanaEdicionSprites.class.getResource( "img/angle24.png" )) ) );
									pAnim2d.add( slAngulo );
								pAnim2a.add( pAnim2e );
									pAnim2e.add( new JLabel( "Gravedad" ) );
									pAnim2e.add( tfGravedad );
									pAnim2e.add( new JLabel( "px/sg2" ) );
									pAnim2e.add( slGravedad );
						pAnim1.add( pAnim3, BorderLayout.CENTER );
							pAnim3.add( pAnim3a, BorderLayout.NORTH );
								pAnim3a.add( pAnim3b );
									pAnim3b.add( new JLabel( "Rotación" ) );
									pAnim3b.add( tfRotacionAnim );
									pAnim3b.add( new JLabel( "º/sg" ) );
									pAnim3b.add( slRotacionAnim );
								pAnim3a.add( pAnim3c );
									pAnim3c.add( new JLabel( "Zoom" ) );
									pAnim3c.add( tfZoomAnim );
									pAnim3c.add( new JLabel( "%" ) );
									pAnim3c.add( slZoomAnim );
								pAnim3a.add( pAnim3d );
									pAnim3d.add( cbCicloAnim );
								pAnim3a.add( pAnim3e );
									pAnim3e.add( cbRetornoAnim );
					pAnim.add( pAnim4, BorderLayout.SOUTH );
						pAnim4.add( bSecuencia );
						pAnim4.add( bAnim );
						pAnim4.add( bSecuenciaAnim );

		// Formato de contenedores
		pi1.setBorder( BorderFactory.createTitledBorder( "Sprites") );
		pCSup1.setBorder( BorderFactory.createTitledBorder( "Preview 100%") );
		pCSup2.setBorder( BorderFactory.createTitledBorder( "Arena") );
		pSec.setBorder( BorderFactory.createTitledBorder( "Secuencia") );
		pAnim.setBorder( BorderFactory.createTitledBorder( "Animación") );
		pAnim2.setBorder( BorderFactory.createRaisedBevelBorder() );
		pAnim3.setBorder( BorderFactory.createRaisedBevelBorder() );
		pPreview.setPreferredSize( new Dimension( 200, 200 ) );
		pPreview.setBackground( Color.WHITE );
		pArena.setBackground( Color.WHITE );
		// Formato de componentes
		slZoom.setPreferredSize( new Dimension( 100, 25 ) );
		slRotacion.setPreferredSize( new Dimension( 100, 25 ) );
		slVelocidad.setPreferredSize( new Dimension( 100, 25 ) );
		slAngulo.setPreferredSize( new Dimension( 100, 25 ) );
		slGravedad.setPreferredSize( new Dimension( 100, 25 ) );
		slZoomAnim.setPreferredSize( new Dimension( 100, 25 ) );
		slRotacionAnim.setPreferredSize( new Dimension( 100, 25 ) );
		spSprites.setPreferredSize( new Dimension( 150, 400 ));
		spSecuencia.setPreferredSize( new Dimension( 150, 200 ));

		// Inicializa el controlador por si este quiere hacer algo en la ventana antes de empezar
		miControlador.init();
		
		// Últimos elementos de la ventana
		pack(); // Pone a la ventana el tamaño mínimo que necesite el layout
		// setSize( 1200, 800 ); // Tamaño inicial de la ventana - sería una alternativa. El pack() del final pone el tamaño mínimo que necesite el layout
		setLocationRelativeTo( null ); // Posición relativa al escritorio (centrada en el escritorio)
		
		// Inicialización de los gestores de eventos (usando el controlador)
		// Botón de búsqueda
		bBuscar.addActionListener( new ActionListener( ) {
			@Override
			public void actionPerformed(ActionEvent e) {
				miControlador.clickBBuscar();
			}
		});

		// Doble click en lista (1)
		lSprites.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()>1) {  // Doble click (o más)
					miControlador.dobleClickListaSprites();
				}
			}
		});
		
		// Click en botones de subir/bajar en lista
		bArriba.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				miControlador.clickBSubir();
			}
		} );
		bAbajo.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				miControlador.clickBBajar();
			}
		} );
		
		// Slider y textfield de rotación
		slRotacionAnim.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				miControlador.sliderStateChanged( slRotacionAnim, tfRotacionAnim );
			}
		});
		tfRotacionAnim.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				miControlador.textFieldFocusLost( slRotacionAnim, tfRotacionAnim, slRotacionAnim.getMinimum(), slRotacionAnim.getMaximum() );
			}
		});
		// Resto de sliders y textfields asociados (mismo criterio, mismos métodos)
		slVelocidad.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				miControlador.sliderStateChanged( slVelocidad, tfVelocidad );
			}
		});
		tfVelocidad.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				miControlador.textFieldFocusLost( slVelocidad, tfVelocidad, slVelocidad.getMinimum(), slVelocidad.getMaximum() );
			}
		});
		slAngulo.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				miControlador.sliderStateChanged( slAngulo, tfAngulo );
			}
		});
		tfAngulo.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				miControlador.textFieldFocusLost( slAngulo, tfAngulo, slAngulo.getMinimum(), slAngulo.getMaximum() );
			}
		});
		slGravedad.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				miControlador.sliderStateChanged( slGravedad, tfGravedad, 0.1 );  // Con multiplicador (caso especial de double)
			}
		});
		tfGravedad.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				miControlador.textFieldFocusLost( slGravedad, tfGravedad, slGravedad.getMinimum(), slGravedad.getMaximum(), 0.1 );  // Con multiplicador
			}
		});
		slZoomAnim.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				miControlador.sliderStateChanged( slZoomAnim, tfZoomAnim );
			}
		});
		tfZoomAnim.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				miControlador.textFieldFocusLost( slZoomAnim, tfZoomAnim, slZoomAnim.getMinimum(), slZoomAnim.getMaximum() );
			}
		});
		
		// Selección de gráfico en la lista de secuencia
		lSecuencia.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {  // isAdjusting es un valor intermedio en eventos consecutivos - esperamos a que ya esté ajustada la selección
					miControlador.lSecuenciaSelectionChanged();
				}
			}
		});
		
		// Cambio de tamaño de panel de preview
		pPreview.addComponentListener( new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				miControlador.pPreviewResized();
			}
		});
		
		// Salida de foco de cuadros de texto
		tfAncho.addFocusListener( new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				miControlador.tfTamanyoFocusLost();
			}
		});
		tfAlto.addFocusListener( new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				miControlador.tfTamanyoFocusLost();
			}
		});
		
		// Sliders de zoom y rotación de secuencia
		slZoom.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				miControlador.slZoomStateChanged();
			}
		});
		slRotacion.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				miControlador.slRotacionStateChanged();
			}
		});
		
		// Cuadros de texto asociados a secuencia
		tfoffsetX.addFocusListener( new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				miControlador.tfOffsetXFocusLost();
			}
		});
		tfoffsetY.addFocusListener( new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				miControlador.tfOffsetYFocusLost();
			}
		});
		tfDuracion.addFocusListener( new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				miControlador.tfDuracionFocusLost();
			}
		});
		
		// Botones de animación
		bSecuencia.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				miControlador.clickBPlaySecuencia();
			}
		});
		bAnim.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				miControlador.clickBPlayAnim();
			}
		});
		bSecuenciaAnim.addActionListener( new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				miControlador.clickBPlayAnimSecuencia();
			}
		});
		
		// TODO Gestión de ficheros (botones de nuevo / carga / guardado)
		// TODO Ventana interna JIInternalFrame
		// TODO Añadir una JProgressBar en la zona de botones que muestre la evolución de la animación
		// TODO Añadir tooltips 
		
	}
	
		private static Stroke stroke1 = new BasicStroke( 1f );  // Atributo creado para no repetir cientos de veces la creación de un objeto que podemos crear una sola vez (ver el método paintComponent a continuación)
	/** Clase JPanel modificada para que se pinte siempre el centro del panel con una marca
	 */
	private static class JPanelConCentro extends JPanel {
		public JPanelConCentro( LayoutManager layout ) {
			super( layout );
		}
		
		// Redefinimos el método de dibujo de children (componentes dentro del contenedor)
		// para que siempre pinte una marca encima de todos sus componentes
		@Override
		protected void paintChildren(Graphics g) {
			super.paintChildren(g);
			// Y pinta la marca:
			Graphics2D g2 = (Graphics2D) g;  // Objeto graphics mejorado
			int xMed = getWidth() / 2;
			int yMed = getHeight() / 2;
			g2.setColor( Color.blue );
			g2.setStroke( stroke1 );
			g2.drawLine( xMed-12, yMed, xMed+12, yMed );  // Rayita horizontal
			g2.drawLine( xMed, yMed-12, xMed, yMed+12 );  // Rayita vertical
			g2.drawOval( xMed-8, yMed-8, 16, 16 );  // Círculo en medio
		}

		// Esto es lo que haríamos si quisiéramos pintar diferente el propio panel
		// @Override
		// protected void paintComponent(Graphics g) {
		// 	super.paintComponent(g);  // Pinta normal...
		// 	// Y luego lo que queramos...
		// }
	}
	
	/** Devuelve el controlador de la ventana
	 * @return
	 */
	public ControladorVentanaSprites getController() {
		return miControlador;
	}
}
