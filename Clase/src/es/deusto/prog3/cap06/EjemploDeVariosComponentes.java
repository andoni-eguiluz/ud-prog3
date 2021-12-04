package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** Clase ejemplo de varios componentes de Swing:
 * Border
 * Checkbox
 * ButtonGroup y RadioButton
 * JSpinner
 * JSlider
 * JProgressBar
 * setPreferredSize
 * Menús
 * @author andoni.eguiluz at deusto.es
 */
@SuppressWarnings("serial")
public class EjemploDeVariosComponentes extends JFrame {
	/** Método principal
	 * @param args	Ignorado
	 */
	public static void main(String[] args) {
		EjemploDeVariosComponentes f = new EjemploDeVariosComponentes();
		f.setVisible( true );
		// Espera 3 segundos y progresa el progressbar
		try {
			Thread.sleep( 3000 );
		} catch (InterruptedException e) {}
		for (int i=0; i<100; i++) {
			f.pbProgreso.setValue( i );
			try {
				Thread.sleep( 20 );
			} catch (InterruptedException e2) {}
		}
	}

	private ButtonGroup bgRadios;  // Grupo de los botones de radio
	private JRadioButton rbAthletic;
	private JRadioButton rbVirgen;
	private JRadioButton rbPNV;
	private JRadioButton rbNinguno;
	private JCheckBox cbReal;
	private JCheckBox cbPaso;
	private JTextArea taEscribeAqui;
	private JScrollPane spCentral;
	private JSpinner spinValor;
	private JTextField tfValor;
	private JSlider slValor;
	private JProgressBar pbProgreso;
	private JButton bProgresa;
	
	public EjemploDeVariosComponentes() {
		// 1.- Inicialización de ventana
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		setLocationRelativeTo( null );  // Centra la posición en el escritorio
		setTitle( "Ejemplo de varios componentes de Swing" );
		
		// 2.- Inicialización de componentes y contenedores
		// Paneles y layouts
		JPanel pBotonera = new JPanel();
		pBotonera.setLayout( new BoxLayout( pBotonera, BoxLayout.Y_AXIS ) );
		JPanel pInferior = new JPanel();  // FlowLayout por defecto
		// Botones de radio y buttongroup
		rbAthletic = new JRadioButton( "Athletic" );
		rbVirgen = new JRadioButton( "Virgen de Begoña" );
		rbPNV = new JRadioButton( "PNV" );
		rbNinguno = new JRadioButton( "Ninguno de los anteriores" );
		bgRadios = new ButtonGroup();  // Observa que si quitas el buttongroup los 4 radios funcionan independientes y se pueden pulsar los 4 (como si fueran checkboxes)
		bgRadios.add( rbAthletic ); bgRadios.add( rbVirgen ); bgRadios.add( rbPNV ); bgRadios.add( rbNinguno );
		// Checkboxes
		cbReal = new JCheckBox( "Soy más bien de la Real" );
		cbPaso = new JCheckBox( "Paso de política" );
		// Resto componentes
		taEscribeAqui = new JTextArea( 15, 40 ); // Filas y columnas aunque si el layout manda da igual el tamaño
		spCentral = new JScrollPane( taEscribeAqui );  // El scrollpane "contiene" al textarea para darle barras de scroll
		slValor = new JSlider( 0, 100 );  // Valores mínimo y máximo del slider
		tfValor = new JTextField( "0", 5 );
		spinValor = new JSpinner();
		bProgresa = new JButton( "Progreso" );
		pbProgreso = new JProgressBar( 0, 100 );  // Valores mínimo y máximo de la progressbar
 		
		// 3.- Formato y configuración
		// Ejemplo de border
		pBotonera.setBorder( BorderFactory.createDashedBorder( Color.red, 10.0f, 5.0f ) );
		spinValor.setPreferredSize( new Dimension( 60, 20 ) );  // Forzar la dimensión preferida del spinner (vale con cualquier componente)
		taEscribeAqui.append( "JTextArea - texto multilínea\n" );
		taEscribeAqui.append( "Tiene scroll que se ve cuando la línea es más ancha que lo que la ventana permite\n" );
		taEscribeAqui.append( "O las líneas más altas\n" );
		taEscribeAqui.append( "\nObserva que la progressbar desde el botón no funciona ¿por qué?\n" );
		
		// 4.- Asociación de componentes a contenedores
		pBotonera.add( new JLabel( "Tu corazón está con..." ) );
		pBotonera.add( rbAthletic );
		pBotonera.add( rbVirgen );
		pBotonera.add( rbPNV );
		pBotonera.add( rbNinguno );
		pBotonera.add( cbReal );
		pBotonera.add( cbPaso );
		pInferior.add( spinValor );
		pInferior.add( slValor );
		pInferior.add( tfValor );
		pInferior.add( new JLabel( "Progreso:" ) );
		pInferior.add( bProgresa );
		pInferior.add( pbProgreso );
		// El buttongroup no hay que añadirlo, no es visual, solo funciona su lógica (no se pueden activar 2 botones del mismo buttongroup)
		getContentPane().add( pBotonera, BorderLayout.WEST );
		getContentPane().add( spCentral, BorderLayout.CENTER );
		getContentPane().add( pInferior, BorderLayout.SOUTH );
		
		// 5.- Menús 
		JMenuBar barraMenu = new JMenuBar(); // Barra de menú
		JMenu menu1 = new JMenu( "Acciones" ); // Primer menú desplegable
		JMenuItem menuItem1 = new JMenuItem( "Reset radios" );   // Opciones
		JMenuItem menuItem2 = new JMenuItem( "Reset progreso" ); 
		menu1.add( menuItem1 ); menu1.add( menuItem2 );
		barraMenu.add( menu1 ); // Añadir a barra
		JMenu menu2 = new JMenu( "Otras" ); // Segundo menú desplegable
		JMenuItem menuItem3 = new JMenuItem( "Salir" );   // Opciones
		menu2.add( menuItem3 );
		barraMenu.add( menu2 ); // Añadir a barra
		setJMenuBar( barraMenu );
		
		// 6.- Eventos
		spinValor.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) { 
				// Al cambiar el spinner actualizamos el cuadro de texto
				tfValor.setText( spinValor.getValue() + "" );
				taEscribeAqui.append( "Actuación sobre el spinner\n" );
				taEscribeAqui.setSelectionStart( taEscribeAqui.getText().length() );  // Al seleccionar el final del texto, se ve este punto en pantalla aunque el scroll se salga 
			}
		});
		slValor.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Al cambiar el slider actualizamos el cuadro de texto
				tfValor.setText( slValor.getValue() + "" );
				taEscribeAqui.append( "Actuación sobre el slider\n" );
				taEscribeAqui.setSelectionStart( taEscribeAqui.getText().length() );  // Al seleccionar el final del texto, se ve este punto en pantalla aunque el scroll se salga 
			}
		});
		bProgresa.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				taEscribeAqui.append( "Actualizando barra de progreso...\n" );
				taEscribeAqui.setSelectionStart( taEscribeAqui.getText().length() );  // Al seleccionar el final del texto, se ve este punto en pantalla aunque el scroll se salga 
				// TODO ¿Por qué esto no funciona? Desde el main sí que funciona
				for (int i=0; i<100; i++) {
					pbProgreso.setValue( i );
					try {
						Thread.sleep( 20 );
					} catch (InterruptedException e2) {}
				}
			}
		});
		menuItem1.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bgRadios.clearSelection();
			}
		});
		menuItem2.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pbProgreso.setValue( 0 );
			}
		});
		menuItem3.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				// Es lo mismo que EjemploDeVariosComponentes.this.dispose();
			}
		});
	}
}
