package es.deusto.prog3.cap01;
import javax.swing.SwingUtilities;

/** Concepto de first-order y de lambda
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ConceptoLambda {

	private static void datoDePrimerOrdenEntero() {
		// Un dato de primer orden por ejemplo el entero 5...
		// 1.- Puede ser el valor de una variable
		int i = 5;
		// 2.- Puede ser parámetro actual de un método
		System.out.println( 5 );
		// 3.- Puede ser devuelto por un método
		int j = Math.abs( -5 );
		// 4.- Puede ser parte de un dato agregado (array, arraylist...)
		int[] enteros = { 5, 1, 2, 5 };
		// 5.- Puede existir sin un nombre (de variable) == o sea, un literal
		// 5
	}
	
	private static void datoDePrimerOrdenObjetoString() {
		// Un dato de primer orden por ejemplo el string "a"...
		// 1.- Puede ser el valor de una variable
		String s1 = "a";
		// 2.- Puede ser parámetro actual de un método
		System.out.println( "a" );
		// 3.- Puede ser devuelto por un método
		String s2 = "ab".substring(0, 1);
		// 4.- Puede ser parte de un dato agregado (array, arraylist...)
		String[] datos = { "5", "a", "hola", "a" };
		// 5.- Puede existir sin un nombre (de variable)
		// "a"
		// new ObjetoX( ... )
	}

		private static Runnable devuelveCodigo() {
			return () -> System.out.println("cod");
		}
		
	private static void datoDePrimerOrdenCodigo() {
		// El código no es dato de primer orden en Java 7
		// pero sí en Java 8, por ejemplo el código sin entrada System.out.println("cod");
		// 1.- Puede ser el valor de una variable
		Runnable r = () -> System.out.println("cod");
		// 2.- Puede ser parámetro actual de un método
		SwingUtilities.invokeLater( () -> System.out.println( "cod" ) );
		// 3.- Puede ser devuelto por un método
		Runnable r2 = devuelveCodigo();
		// 4.- Puede ser parte de un dato agregado (array, arraylist...)
		Runnable[] codigos = { ConceptoLambda::devuelveCodigo,
							   () -> System.out.println("a") };
		// 5.- Puede existir sin un nombre (de variable) == o sea, un literal
		// () -> System.out.println("cod")
		
		// El TRUCO que se utiliza es que el código se mete dentro de un
		// OBJETO que implementa un INTERFAZ.
		// Ese interfaz solo puede tener un método. 
		// Se infieren del contexto sus parámetros y su retorno. Por ejemplo:
		// SwingUtilities.invokeLater( ... ) 
		//        ahí debe ir un Runnable o sea un código sin parámetros que devuelve void
		//        (pero sin nombre - no tiene que llamarse run())
		// boton.addActionListener( ... )
		//	      ahí debe ir un ActionListener o sea un código con un parámetro ActionEvent que devuelve void
		//        (pero sin nombre - no tiene que llamarse actionPerformed(ActionEvent e)
		
		// Los lenguajes donde el código es un dato de primer orden a todos los efectos
		// se llaman lenguajes FUNCIONALES.
		// Java 7 era funcional con truco (se pasaban objetos con código en vez de código)
		// Java 8 es funcional con "menos truco" (pasa lo mismo pero la sintaxis no explicita el objeto)
	}
	
		private static int dondeA( String string ) {
			return string.indexOf( "a" );
		}
	private static void connotacionesImportantesDeLenguajesFuncionales() {
		// Este cambio no es trivial y tiene varias connotaciones. La más importante
		// tiene que ver con el tiempo de ejecución. 
		// Un lenguaje no funcional (no se puede pasar código) es siempre SINCRONO.
		// Por ejemplo:
		System.out.println( "Hola".substring( 0, dondeA("Hola") ) );
		System.out.println( "Adiós" );
		// En qué orden se ejecuta?  Es fijo y hasta que no se ejecuta todo no se sigue
		// Sin embargo:
		SwingUtilities.invokeLater( () -> System.out.println( "Otro hola" ) );
		System.out.println( "Otro adiós" );
		// ¿En qué orden se ejecuta?
		
		// Otra connotación tb importante tiene que ver con el ámbito de ejecución
		int i = 0;
		i++;
		// SwingUtilities.invokeLater( () -> System.out.println( i ) );
		// ¿Por qué esta línea de arriba genera un error de compilación?
	}

	
	public static void main(String[] args) {
		datoDePrimerOrdenEntero();
		datoDePrimerOrdenObjetoString();
		datoDePrimerOrdenCodigo();
		connotacionesImportantesDeLenguajesFuncionales();
	}

}
