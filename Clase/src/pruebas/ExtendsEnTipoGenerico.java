package pruebas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Pequeña prueba de tipos genéricos heredados con cláusula ? extends tipo
 * @author andoni.eguiluz @ ingenieria.deusto.es
 * 
 * En Java Tutorials: https://docs.oracle.com/javase/tutorial/extra/generics/morefun.html
 */
public class ExtendsEnTipoGenerico {

	public static void main(String[] args) {
		// Los parámetros son polimórficos. A un método
		// recibe( Object o )
		// le podemos pasar cualquier clase hija
		recibe( "String" );

		// Pero la definición de tipos genéricos no es polimórfica, es *invariante*. A un método
		// recibeLista( List<Object> l )
		// no le podemos pasar otra lista, por ejemplo
		ArrayList<String> lis = new ArrayList<>( Arrays.asList( "A", "B", "C" ) ); 
		// recibeLista( lis );  // ERROR!  (probar a descomentar)
		
		// Si queremos un tipo generics que permita polimorfismo, puede usarse la sintaxis
		// ? extends Tipo
		// Por ejemplo un método
		//	recibeListaHer( List<? extends Object> l )
		// sí podrá recibir cualquier lista de tipo heredado:
		ArrayList<Integer> lis2 = new ArrayList<>( Arrays.asList( 1, 2, 3 ) ); 
		recibeListaHer( lis );
		recibeListaHer( lis2 );
		
		// Dicho de otro modo, String es instanceof Object, 
		// pero List<String> no es instanceof List<Object>
		// O en general si B hereda de A, cualquier B instanceof A
		// pero List<B> no es instanceof de List<A>

		// Sin embargo, los arrays son *covariantes*
		// String[] si es instanceof de Object[], o B[] instanceof A[]. Así que
		// recibeArray( Object[] )
		// sí podrá recibir cualquier array:
		String[] arrayS = { "A", "B", "C" };
		recibeArray( arrayS );
	}
	
	public static void recibe( Object o ) {
		System.out.println( o );
	}
	
	public static void recibeLista( List<Object> l ) {
		System.out.println( l );
	}

	public static void recibeListaHer( List<? extends Object> l ) {
		System.out.println( l );
	}
	
	public static void recibeArray( Object[] array ) {
		System.out.println( Arrays.toString( array ) );
	}
	
}
