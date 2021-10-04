package es.deusto.prog3.utils.ventanas.ventanaConsola;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/** Clase de utilidad para poder realizar entrada de texto de teclado
 * y salida de texto a pantalla, utilizando una ventana en lugar de
 * hacerlo sobre consola.
 * Haciendo Ctrl+F se busca el texto que esté en la línea de entrada
 * @author andoni.eguiluz.moran
 */
@SuppressWarnings("serial")
public class VentanaConsola extends JFrame {
	private static VentanaConsola miVentana = null;
	private JTextField tfLineaEntrada = new JTextField();
	private JTextArea taSalida = new JTextArea();
	private JScrollPane spSalida = new JScrollPane( taSalida );
	private boolean lineaLeida = false;
	private VentanaConsola() {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 800, 600 );
		setLocationRelativeTo( null );
		setTitle( "Ventana de consola" );
		getContentPane().add( spSalida, BorderLayout.CENTER );
		getContentPane().add( tfLineaEntrada, BorderLayout.SOUTH );
		taSalida.setEditable( true );
		// Formato
		taSalida.setFont( new Font( "Arial", Font.PLAIN, 16 ));
		tfLineaEntrada.setFont( new Font( "Arial", Font.PLAIN, 16 ));
		// Política de visualización de caret -cursor en textarea-
		DefaultCaret caret = (DefaultCaret) taSalida.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);       
		// Escuchadores
		tfLineaEntrada.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lineaLeida = true;
			}
		});
		taSalida.addKeyListener( new KeyListener() {
			boolean ctrlPulsado = false;
			@Override
			public void keyTyped(KeyEvent e) {
				e.consume();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) ctrlPulsado = false;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) ctrlPulsado = true;
				else if (e.getKeyCode()==KeyEvent.VK_F && ctrlPulsado) {  // Ctrl+F
					buscarString( tfLineaEntrada.getText() );
				}
			}
		});
	}
	
	/** Busca un string en el área de texto. Si lo encuentra, lo selecciona
	 * @param aBuscar	String a buscar
	 */
	public static void buscarString( String aBuscar ) {
		if (aBuscar==null || aBuscar.isEmpty() || miVentana==null) return;
		int posActual = miVentana.taSalida.getSelectionEnd();
		int busqueda = miVentana.taSalida.getText().indexOf( aBuscar, posActual );
		if (busqueda>=0) {
			miVentana.taSalida.setSelectionStart( busqueda );
			miVentana.taSalida.setSelectionEnd( busqueda + aBuscar.length() );
		}
	}

	/** Inicializa la ventana de consola y la muestra en el centro de la pantalla
	 */
	static public void init() {
		if (miVentana == null) miVentana = new VentanaConsola();
		miVentana.setVisible( true );
		miVentana.tfLineaEntrada.requestFocus();
	}
	
	/** Cierra la ventana de consola. Llamar siempre a este método al final. 
	 * ATENCION: Si no se llama a este método Java sigue activo aunque el main que lo ejecuta se acabe
	 * (Si sabes de hilos... este hilo cierra la ventana para que Swing pueda acabar)
	 */
	static public void finish() {
		miVentana.dispose();
		miVentana = null;
	}
	
	/** Lee una línea desde la línea de entrada de la ventana
	 * @return	Línea leída como un string
	 */
	static public String leeString() {
		if (miVentana == null) init();
		miVentana.tfLineaEntrada.requestFocus();
		while (!miVentana.lineaLeida) {
			// Espera hasta que se lea algo
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { }
		}
		miVentana.lineaLeida = false;
		String ret  = miVentana.tfLineaEntrada.getText();
		miVentana.tfLineaEntrada.setText( "" );
		return ret;
	}
	
	/** Lee una línea desde la línea de entrada de la ventana y la interpreta como un entero
	 * @return	entero leído. Si es erróneo devuelve Integer.MAX_VALUE
	 */
	static public int leeInt() {
		if (miVentana == null) init();
		miVentana.tfLineaEntrada.requestFocus();
		while (!miVentana.lineaLeida) {
			// Espera hasta que se lea algo
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { }
		}
		miVentana.lineaLeida = false;
		String ret  = miVentana.tfLineaEntrada.getText();
		miVentana.tfLineaEntrada.setText( "" );
		try {
			int i = Integer.parseInt( ret );
			return i;
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}
	
	/** Lee una línea desde la línea de entrada de la ventana y la interpreta como un double
	 * @return	double leído. Si es erróneo devuelve Double.MAX_VALUE
	 */
	static public double leeDouble() {
		if (miVentana == null) init();
		miVentana.tfLineaEntrada.requestFocus();
		while (!miVentana.lineaLeida) {
			// Espera hasta que se lea algo
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) { }
		}
		miVentana.lineaLeida = false;
		String ret  = miVentana.tfLineaEntrada.getText();
		miVentana.tfLineaEntrada.setText( "" );
		try {
			double d = Double.parseDouble( ret );
			return d;
		} catch (NumberFormatException e) {
			return Double.MAX_VALUE;
		}
	}
	
	/** Escribe en la ventana una línea
	 * @param s	String a visualizar en la ventana
	 */
	static public void println( String s ) {
		if (miVentana == null) init();
		miVentana.taSalida.append( s + "\n" );
	}
	
	/** Escribe en la ventana un string (no salta línea)
	 * @param s	String a visualizar en la ventana
	 */
	static public void print( String s ) {
		if (miVentana == null) init();
		miVentana.taSalida.append( s );
	}

	/** Espera sin hacer nada durante el tiempo indicado en milisegundos
	 * @param msg	Tiempo a esperar
	 */
	static public void esperaUnRato( int msg ) {
		try {
			Thread.sleep( msg );
		} catch (InterruptedException e) {
		}
	}
	
	/** Método de prueba de la clase.
	 * @param args
	 */
	public static void main(String[] args) {
		VentanaConsola.init();
		VentanaConsola.println( "Hola. Dame un número entero:");
		int dato = VentanaConsola.leeInt();
		if (dato == Integer.MAX_VALUE)
			VentanaConsola.println( "Tu número introducido es INCORRECTO." );
		else 
			VentanaConsola.println( "Tu número introducido es " + dato );
		VentanaConsola.println( "Dame un número real:");
		double datoDouble = VentanaConsola.leeDouble();
		if (datoDouble == Double.MAX_VALUE)
			VentanaConsola.println( "Tu número introducido es INCORRECTO." );
		else 
			VentanaConsola.println( "Tu número introducido es " + datoDouble );
		VentanaConsola.println( "Dame un string:");
		String linea = VentanaConsola.leeString();
		VentanaConsola.println( "Tu string introducido es \"" + linea + "\"" );
		VentanaConsola.println( "Estoy ejecutándome 5 segundos más... y me paro");
		VentanaConsola.esperaUnRato( 5000 );
		VentanaConsola.finish();
	}

}
