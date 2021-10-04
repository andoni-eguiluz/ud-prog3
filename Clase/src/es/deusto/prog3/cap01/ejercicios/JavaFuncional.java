package es.deusto.prog3.cap01.ejercicios;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
	
	/** Crea ventana de ejemplo con un cuadro de texto y un botón
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		// Creación y configuración ventana
		JFrame f = new JFrame( "Ejemplo de lambda en Java 8" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// Componentes
		JPanel pEntrada = new JPanel();
		JButton bProcesar = new JButton( "Procesar" );
		pEntrada.add( new JLabel( "lista de enteros entre comas:" ) );
		pEntrada.add( tfEntrada );
		pEntrada.add( bProcesar );
		f.add( pEntrada, BorderLayout.NORTH );
		f.add( lSalida, BorderLayout.SOUTH );
		// Visualizar
		f.pack();
		f.setLocationRelativeTo( null );
		f.setVisible( true );
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
