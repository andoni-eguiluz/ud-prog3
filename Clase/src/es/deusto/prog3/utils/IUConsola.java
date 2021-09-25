package es.deusto.prog3.utils;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.event.*;

/** Utilidad para sacar la consola al IU en una ventana Swing con doble cuadro de texto, para la consola de salida (izquierda) y la de error (derecha)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class IUConsola {
	
	private static int LIM_LINEAS_TEXTAREAS = 10000;  // Líneas límite a partir de las que se truncan ambas textareas
	private static int TAMANYO_TIPO_INICIAL = 14; // Puntos de tipo de letra inicial
	private static String NOMBRE_TIPO = "Hack-Regular.ttf";  // Tipo de letra (fichero debe estar en el mismo paquete)
	private static MiJTextArea taOut = new MiJTextArea( LIM_LINEAS_TEXTAREAS );  // Textarea para el out
	private static MiJTextArea taErr = new MiJTextArea( LIM_LINEAS_TEXTAREAS );  // Textarea para el err
	private static JPanel pBotonera = new JPanel();  // Panel para los botones (opcionales)
	private static VentanaIUConsola ventana;  // Ventana para las textareas

		private static Runnable rLanzarIU = new Runnable() {
			@Override
			public void run() {
				ventana = new VentanaIUConsola( true );
				ventana.setVisible( true );
			}
		};

		private static Runnable rLanzarIUOut = new Runnable() {
			@Override
			public void run() {
				ventana = new VentanaIUConsola( false );
				ventana.setVisible( true );
			}
		};

	/** Atrapa la salida a consolas estándar del sistema (out y err) y lanza una ventana con doble panel para visualizarlas en la IU
	 * (duplica la salida, es decir, se mantiene también la salida a ambas consolas estándar)
	 * @param salidaOut	Stream de salida adicional al que redirigir out, por ejemplo un fichero (null si no se quiere utilizar)
	 * @param salidaErr	Stream de salida adicional al que redirigir err, por ejemplo un fichero (null si no se quiere utilizar)
	 * @param numLineas	(Opcional) número de líneas máximo de visualización en las áreas de texto (debe ser mayor que 100)
	 * @return	true si el proceso se hace correctamente, false en caso contrario. 
	 */
	public static boolean lanzarConsolaEnIU( OutputStream salidaOut, OutputStream salidaErr, int... numLineas ) {
		if (numLineas!=null && numLineas.length>0 && numLineas[0]>100) {
			taOut.setLimLineas( numLineas[0] );
			taErr.setLimLineas( numLineas[0] );
		}
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				rLanzarIU.run();
			} else {
				SwingUtilities.invokeAndWait( rLanzarIU );
			}
		} catch (Exception e) {  // No se ha podido crear la ventana
			return false;
		}
		try {
			System.setOut( new PrintStream( new MiOutputStream( taOut, System.out, salidaOut ) ) );
			System.setErr( new PrintStream( new MiOutputStream( taErr, System.err, salidaErr ) ) );
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			taErr.append( e.toString() );
			return false;
		}
	}
	
	/** Atrapa la salida a consola estándar de salida del sistema (out) y lanza una ventana para visualizarla en la IU
	 * (duplica la salida, es decir, se mantiene también la salida a out)
	 * @param salidaOut	Stream de salida adicional al que redirigir out, por ejemplo un fichero (null si no se quiere utilizar)
	 * @param numLineas	(Opcional) número de líneas máximo de visualización en las áreas de texto (debe ser mayor que 100)
	 * @return	true si el proceso se hace correctamente, false en caso contrario. 
	 */
	public static boolean lanzarConsolaEnIU( OutputStream salidaOut, int... numLineas ) {
		if (numLineas!=null && numLineas.length>0 && numLineas[0]>100) {
			taOut.setLimLineas( numLineas[0] );
		}
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				rLanzarIUOut.run();
			} else {
				SwingUtilities.invokeAndWait( rLanzarIUOut );
			}
		} catch (Exception e) {  // No se ha podido crear la ventana
			return false;
		}
		try {
			System.setOut( new PrintStream( new MiOutputStream( taOut, System.out, salidaOut ) ) );
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** Añade un botón a la línea de botones (llamar antes a {@link #lanzarConsolaEnIU})
	 * @param texto	Texto del botón
	 * @param run	Código del botón
	 */
	public static void addBoton( String texto, Runnable run ) {
		JButton b = new JButton( texto );
		pBotonera.add( b );
		b.addActionListener( (e) -> run.run() );
	}
	
	/** Cierra la ventana de salida de consola
	 */
	public static void cerrarConsolaEnIU() {
		if (ventana!=null) ventana.dispose();
	}

	/** Main de prueba
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int numPrueba = 2;  // 1.- prueba con ficheros / 2.- prueba con salida out+err / 3.- prueba con salida out
			if (numPrueba==1) {
				lanzarConsolaEnIU( new FileOutputStream( "test_out.txt" ), new FileOutputStream( "test_err.txt" ) );
				System.out.println( "Prueba consola salida" );
				for (int i=0; i<1000; i++)
					System.out.println( "Prueba números consola salida " + i );
				System.err.println( "Prueba consola error" );
				System.err.println( "Prueba consola error 2" );
			} else if (numPrueba==2) {
				lanzarConsolaEnIU( null, null, null );
				System.out.println( "Prueba tilde: áéíóúñÑÁÉÍÓÚ" );
				System.err.println( "Prueba consola error" );
			} else if (numPrueba==3) {
				lanzarConsolaEnIU( null, null );
				System.out.println( "Prueba tilde: áéíóúñÑÁÉÍÓÚ" );
				System.err.println( "Prueba consola error" );
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("serial")
	private static class VentanaIUConsola extends JFrame {
		// ambasConsolas = true saca out y err, false saca solo out
		private VentanaIUConsola( boolean ambasConsolas ) {
			setTitle( "Consola de Java" );
			initFont();
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			add( pBotonera, BorderLayout.NORTH );
			if (ambasConsolas) {
				JSplitPane p = new JSplitPane();
				add( p, BorderLayout.CENTER );
				p.setLeftComponent( new JScrollPane( taOut ) );
				p.setRightComponent( new JScrollPane( taErr ) );
				p.setDividerLocation( 0.5 );
				taErr.setForeground( Color.red );
			} else {
				add( new JScrollPane( taOut ), BorderLayout.CENTER );
				taErr = null;
			}
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					if (ventana!=null) ventana = null;
				}
			});
			KeyListener kl = new KeyAdapter() {
				boolean ctrlPulsado = false;
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
						ctrlPulsado = true;
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
						ctrlPulsado = false;
					} else if (e.getKeyCode()==KeyEvent.VK_PLUS && ctrlPulsado) {
						cambiarTipo( +2 );
					} else if (e.getKeyCode()==KeyEvent.VK_MINUS && ctrlPulsado) {
						cambiarTipo( -2 );
					}
				}
			};
			taOut.addKeyListener( kl );
			if (taErr!=null) taErr.addKeyListener( kl );
			pack();
		}
		private int tamanyo = TAMANYO_TIPO_INICIAL;
		private void cambiarTipo( int incTipo ) {
			tamanyo += incTipo;
			Font f = this.f.deriveFont( Font.PLAIN, tamanyo );
			taOut.setFont( f );
			if (taErr!=null) taErr.setFont( f );
		}
		private Font f; 
		private void initFont() {
			try {
				String nombreFont = NOMBRE_TIPO;
				f = Font.createFont( Font.TRUETYPE_FONT,
					IUConsola.class.getResourceAsStream(nombreFont) );
			} catch (Exception e) {
				f = new Font( "Arial", Font.PLAIN, tamanyo );
				JOptionPane.showMessageDialog( null, "Error en carga de ttf" );
			}
			cambiarTipo( 0 );
		}
	}
	
	@SuppressWarnings("serial")
	private static class MiJTextArea extends JTextArea {
		private int limLineas;
		/** Crea una JTextArea con límite de líneas
		 * @param limiteLineas	Número de líneas límite (cada vez que se alcance ese umbral, se corta la primera mitad de las líneas)
		 */
		public MiJTextArea( int limiteLineas ) {
			super( 20, 80 );
			limLineas = limiteLineas;
		}
		public void setLimLineas( int limiteLineas ) {
			limLineas = limiteLineas;
		}
		@Override
		public void append(String str) {
			try {
				boolean ponerAlFinal = getLineCount()<=3 || getSelectionStart()>getLineEndOffset( getLineCount()-3 );
				super.append(str);
				if (getLineCount() > limLineas) {
					setSelectionStart(0);
					setSelectionEnd( getLineEndOffset( limLineas/2 ) );
					replaceSelection( "" );
					setSelectionStart( getText().length() );
					setSelectionEnd( getText().length() );
				}
				if (ponerAlFinal) { // Posiciona el caret al final del cuadro de texto, excepto si el usuario lo mueve explícitamente antes (en ese caso se mantiene)
					setSelectionStart( getText().length() );  
					setSelectionEnd( getText().length() );
				}
			} catch (BadLocationException e) {}
		}
	}
	
	/** Nuevo stream de salida que saca a varios streams (indicados) y a la textArea indicada
	 */
	private static class MiOutputStream extends OutputStream
	{
		private OutputStream[] outputStreams;
		private MiJTextArea textArea; 
		
		public MiOutputStream(MiJTextArea textArea, OutputStream... outputStreams) {
			this.outputStreams= outputStreams; 
			this.textArea = textArea;
		}
		
		@Override
		public void write(int b) throws IOException {
			for (OutputStream out: outputStreams)
				if (out!=null) out.write(b);
			if (ventana!=null) textArea.append( b+"" );
		}
		
		@Override
		public void write(byte[] b) throws IOException {
			for (OutputStream out: outputStreams)
				if (out!=null) out.write(b);
			if (ventana!=null) textArea.append( new String( b ) );
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			for (OutputStream out: outputStreams)
				if (out!=null) out.write(b, off, len);
			if (ventana!=null) textArea.append( new String( b, off, len ) );
		}

		@Override
		public void flush() throws IOException {
			for (OutputStream out: outputStreams)
				if (out!=null) out.flush();
		}

		@Override
		public void close() throws IOException {
			for (OutputStream out: outputStreams)
				if (out!=null) out.close();
			if (ventana!=null) { ventana.dispose(); ventana = null; }
		}
	}

}
