package es.deusto.prog3.cap01.resueltos;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import javax.swing.*;

/** Ejemplo/ejercicio para mostrar la sintaxis funcional de Java 8
 * Ejercicio: haz que el botón procese la lista de strings que mete el usuario en el cuadro de texto
 * y que muestre los enteros uno a uno (2 segundos cada uno) en el label de mensaje
 * UTILIZANDO EN LO POSIBLE JAVA FUNCIONAL
 * ¿Cómo harías además que se pudiera elegir la acción entre una lista de acciones
 * (por ejemplo [a] sacar los enteros cada 2 segundos y también [b] calcular la media y [c] solo visualizar)
 * también utilizando java funcional?
 */
public class JavaFuncional {

	private static JLabel lSalida = new JLabel( " " );
	private static JTextField tfEntrada = new JTextField( 20 );
	private static JButton bProcesar;

	// Parte de código con Java funcional para las 3 acciones
	// Podríamos hacer un interface particular...
	// public static interface RecibeListaInts {
	// 	void procesaLE( ArrayList<Integer> l );
	// }
	// ... pero para eso están los interfaces estándares de Java 8 (para no tener que hacer nuevos):
	// Runnable --> run()
	// Consumer<T> --> void accept(T)
	// Predicate<T> --> boolean test(T)
	// Supplier<T> --> T get()
	// Function<T,R> --> R apply(T)
	//
	// así que usamos Consumer<ArrayList<Integer>>
	
	private static ArrayList<String> nombreOpcion = new ArrayList<>();
	private static ArrayList<Consumer<ArrayList<Integer>>> nombreCodigo = new ArrayList<>();
	private static void init() {
		nombreOpcion.add( "Visualizar cada 2 sgs." );
		nombreOpcion.add( "Calcular la media" );
		nombreOpcion.add( "Visualizar lista" );
		nombreCodigo.add( JavaFuncional::cada2Segs );  // Código de la primera opción
		nombreCodigo.add( JavaFuncional::calculaMedia );  // Código de la 2
		nombreCodigo.add( (l) -> { lSalida.setText( l.toString() ); } );  // Código de la 3
		for (String s : nombreOpcion) {
			cbOpciones.addItem( s );
		}
	}
	private static JComboBox<String> cbOpciones = new JComboBox<>();
	static {
		// Código que se ejecuta al cargar la clase y solo una vez
		init();
	}
	
	/** Crea ventana de ejemplo con un cuadro de texto y un botón
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		// init(); (está en el static inicializado)
		// Creación y configuración ventana
		JFrame f = new JFrame( "Ejemplo de lambda en Java 8" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// Componentes
		JPanel pEntrada = new JPanel();
		bProcesar = new JButton( "Procesar" );
		pEntrada.add( new JLabel( "lista de enteros entre comas:" ) );
		pEntrada.add( tfEntrada );
		pEntrada.add( bProcesar );
		f.add( pEntrada, BorderLayout.NORTH );
		f.add( cbOpciones, BorderLayout.CENTER );
		f.add( lSalida, BorderLayout.SOUTH );
		// Programar eventos
		// Java 7 no funcional
//		ActionListener obj = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				procesoBoton();
//			}
//		};
//		bProcesar.addActionListener( obj );
		bProcesar.addActionListener( (jj) -> { procesoBoton(); } );
		// ¿Qué pasa?
		// addActionListener necesita un ActionListener
		// ¿Cuántos métodos tiene? Uno
		// La signatura del código (params y ret) tiene que ser igual a actionPerformed
		f.addWindowListener( new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
			}
			@Override
			public void windowClosed(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		// No se podría sin embargo (son varios métodos):
		// f.addWindowListener( (e) -> {}  );
		
		
		
		// Visualizar
		f.pack();
		f.setLocationRelativeTo( null );
		f.setVisible( true );
	}
	
	private static void procesoBoton() {
		ArrayList<String> lStrings = listaDeStrings( tfEntrada.getText() );
		ArrayList<Integer> lEnteros = listaDeInts( lStrings );
		if (lEnteros==null || lEnteros.isEmpty()) { return; }
		if (cbOpciones.getSelectedIndex()!=-1) {
			nombreCodigo.get( cbOpciones.getSelectedIndex() ).accept( lEnteros );
		}
	}
	
	private static void calculaMedia(ArrayList<Integer> l) {
		int suma = 0;
		for (int i : l) suma += i;
		lSalida.setText( "" + 1.0 * suma / l.size() );
	}
	
	private static void cada2Segs(ArrayList<Integer> lEnteros) {
		//		Thread t = new Thread( new Runnable() {
		//		@Override
		//		public void run() {
		//		}
		//	});
		Thread t = new Thread( 
				() -> {
					bProcesar.setEnabled( false );
					for (int i : lEnteros) {
						lSalida.setText( i + "" );
						try { Thread.sleep(2000); } catch (InterruptedException e) {}
					}
					lSalida.setText( "" );
					bProcesar.setEnabled( true );
				}
				);
		t.start();
	}
	
	/** Devuelve un arraylist de strings partiendo de un string con comas
	 * @param lista	Lista de substrings separados por comas
	 * @return	Devuelve una lista de strings separando los substrings que estén con comas (quitando los espacios)
	 */
	private static ArrayList<String> listaDeStrings( String lista ) {
		ArrayList<String> ret = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer( lista, "," );
		while (st.hasMoreTokens()) {
			ret.add( st.nextToken().trim() );  // Mete el siguiente substring quitando espacios
		}
		return ret;
	}
	
	/** Devuelve una lista de enteros partiendo de una lista de strings
	 * @param lista	Lista de strings que representan a enteros
	 * @return	Lista de los enteros en la lista de strings (si algún string no es un entero válido, se ignora)
	 */
	private static ArrayList<Integer> listaDeInts( ArrayList<String> lista ) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (String string : lista) {
			try {
				ret.add( Integer.parseInt( string ) );
			} catch (NumberFormatException e) {
				// Se ignora el string que no es un entero válido
			}
		}
		return ret;
	}

}
