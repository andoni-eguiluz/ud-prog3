package es.deusto.prog3.utils.ventanas.ventanaConsola;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/** Clase de utilidad para poder realizar entrada de texto de teclado
 * y salida de texto a pantalla, utilizando una ventana en lugar de
 * hacerlo sobre consola.
 * Permite utilizar texto coloreado en la consola con los métodos 
 * específicos de color
 * Haciendo Ctrl+F se busca el texto que esté en la línea de entrada
 * @author andoni.eguiluz.moran
 */
@SuppressWarnings("serial")
public class VentanaColorConsola extends JFrame {
	private static VentanaColorConsola miVentana = null;
	private JTextField tfLineaEntrada = new JTextField();
	private JTextPane tpSalida = new JTextPane();
	private JScrollPane spSalida = null;
	private StyledDocument doc = tpSalida.getStyledDocument();
	private boolean lineaLeida = false;
	private VentanaColorConsola() {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 800, 600 );
		setLocationRelativeTo( null );
		setTitle( "Ventana de consola" );
		tpSalida.setEditable( true );
			JPanel noWrapPanel = new JPanel( new BorderLayout() ); // Truco para hacer el textpane sin wrap de línea
			noWrapPanel.add( tpSalida );
		spSalida = new JScrollPane( noWrapPanel );
		getContentPane().add( spSalida, BorderLayout.CENTER );
		getContentPane().add( tfLineaEntrada, BorderLayout.SOUTH );
		// Formato
		tpSalida.setFont( new Font( "Arial", Font.PLAIN, 16 ));
		tfLineaEntrada.setFont( new Font( "Arial", Font.PLAIN, 16 ));
		// Política de visualización de caret -cursor en textpane-
		DefaultCaret caret = (DefaultCaret) tpSalida.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);       
		// Escuchadores
		tfLineaEntrada.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lineaLeida = true;
			}
		});
		tfLineaEntrada.addKeyListener( new KeyAdapter() {
			boolean ctrlPulsado = false;
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) ctrlPulsado = false;
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_CONTROL) ctrlPulsado = true;
				else if (e.getKeyCode()==KeyEvent.VK_F && ctrlPulsado) {  // Ctrl+F
					buscarString( tfLineaEntrada.getText() );
					tpSalida.requestFocus();
				}
			}
		});
		tpSalida.addKeyListener( new KeyListener() {
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
		if (miVentana.tpSalida.getSelectionEnd() >= miVentana.doc.getLength()) {
			miVentana.tpSalida.setSelectionStart(0);
			miVentana.tpSalida.setSelectionEnd(0);
		}
		int posActual = miVentana.tpSalida.getSelectionEnd();
		int busqueda = miVentana.tpSalida.getText().indexOf( aBuscar, posActual );
		if (busqueda>=0) {
			// Diferente para TextArea - más complicado con JTextPane
			Element el = VentanaColorConsola.miVentana.doc.getDefaultRootElement();
			buscarStringRec( aBuscar, el );
		}
	}
		private static boolean buscarStringRec( String aBuscar, Element el ) {
			if (el instanceof AbstractDocument.LeafElement) {  // Nodo final de texto
				try {
					String text = el.getDocument().getText( el.getStartOffset(), el.getEndOffset()-el.getStartOffset() );
					int busqueda = text.indexOf( aBuscar );
					if (busqueda>=0) {
						miVentana.tpSalida.setSelectionStart( el.getStartOffset()+busqueda );
						miVentana.tpSalida.setSelectionEnd( el.getStartOffset()+busqueda + aBuscar.length() );
						return true;
					}
				} catch (BadLocationException e) {}
				return false;
			} else {
				// System.out.println( "#" + el + " - " + el.getClass().getName() );
				for (int i=0; i<el.getElementCount(); i++) {
					if (el.getElement(i).getStartOffset() > miVentana.tpSalida.getSelectionEnd()) {
						boolean esta = buscarStringRec( aBuscar, el.getElement(i) );
						if (esta) return true;
					}
				}
				return false;
			}
		}

	/** Inicializa la ventana de consola y la muestra en el centro de la pantalla
	 */
	static public void init() {
		if (miVentana == null) miVentana = new VentanaColorConsola();
		miVentana.setVisible( true );
		miVentana.tfLineaEntrada.requestFocus();
	}
	
	/** Cierra la ventana de consola. Llamar siempre a este método al final. 
	 * ATENCION: Si no se llama a este método Java sigue activo aunque el main que lo ejecuta se acabe
	 * (Si sabes de hilos... este hilo cierra la ventana para que Swing pueda acabar)
	 */
	static public void finish() {
		if (miVentana!=null) miVentana.dispose();
		miVentana = null;
		estilosUsados.clear();
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
	
	/** Borra el texto de la ventana
	 */
	static public void clear() {
		if (miVentana == null) init();
		miVentana.tpSalida.setText( "" );
	}
	
	/** Escribe en la ventana una línea
	 * @param s	String a visualizar en la ventana
	 */
	static public void println( String s ) {
		print( s + "\n" );
	}
	
	/** Escribe en la ventana una línea con color
	 * @param s	String a visualizar en la ventana
	 * @param color	Color con el que visualizarlo
	 */
	static public void println( String s, Color color ) {
		print( s + "\n", color );
	}
	
	/** Escribe en la ventana un string (no salta línea)
	 * @param s	String a visualizar en la ventana
	 */
	static public void print( String s ) {
		if (miVentana == null) init();
        try { miVentana.doc.insertString( miVentana.doc.getLength(), s, null ); }
        catch (BadLocationException e) {}
	}
	
		// Mapa interno de estilos de color, para reutilizarlos según se crean
		private static volatile HashMap<Integer,Style> estilosUsados = new HashMap<>();
	/** Escribe en la ventana un string (no salta línea) con color
	 * @param s	String a visualizar en la ventana
	 * @param color	Color con el que visualizarlo
	 */
	static public void print( String s, Color color ) {
		if (miVentana == null) init();
		Style style = estilosUsados.get( color.getRGB() );
		if (style==null) {
	        style = miVentana.tpSalida.addStyle( ""+color.getRGB(), null );
	        StyleConstants.setForeground( style, color );
	        estilosUsados.put( color.getRGB(), style );
		}
        try { miVentana.doc.insertString( miVentana.doc.getLength(), s, style ); }
        catch (BadLocationException e) {}
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
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		VentanaColorConsola.init();
		VentanaColorConsola.println( "Hola. Dame un número entero:" );  // Sin color
		int dato = VentanaColorConsola.leeInt();
		if (dato == Integer.MAX_VALUE)
			VentanaColorConsola.println( "Tu número introducido es INCORRECTO.", Color.red );
		else 
			VentanaColorConsola.println( "Tu número introducido es " + dato, Color.green );
		VentanaColorConsola.println( "Dame un número real:", Color.blue );
		double datoDouble = VentanaColorConsola.leeDouble();
		if (datoDouble == Double.MAX_VALUE)
			VentanaColorConsola.println( "Tu número introducido es INCORRECTO.", Color.red );
		else 
			VentanaColorConsola.println( "Tu número introducido es " + datoDouble, Color.green );
		VentanaColorConsola.println( "Dame un string:", Color.blue );
		String linea = VentanaColorConsola.leeString();
		VentanaColorConsola.clear();
		VentanaColorConsola.println( "Tu string introducido es \"" + linea + "\"", Color.blue );
		VentanaColorConsola.println( "Estoy ejecutándome 5 segundos más... y me paro", Color.blue );
		VentanaColorConsola.esperaUnRato( 5000 );
		VentanaColorConsola.finish();
	}

}
