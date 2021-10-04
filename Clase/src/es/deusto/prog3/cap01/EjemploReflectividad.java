package es.deusto.prog3.cap01;

import java.lang.reflect.Method;

/** Prueba de acceso por reflectividad a métodos
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploReflectividad {

	@SuppressWarnings("unused")  // Para que no salga el warning de que metodo1 no se usa
	private void metodo1( int i ) {
		System.out.println( "Método1 = " + i );
	}
	
	public void metodo2( double d ) {
		System.out.println( d );
	}
	
	public static void main(String[] args) {
		Class<?> c = EjemploReflectividad.class;
		System.out.println( "Métodos públicos:");
		Method[] mets = c.getMethods();
		for (Method m : mets)
			System.out.println( m );
		System.out.println();
		System.out.println( "Métodos declarados (en esta clase, no por herencia):");
		mets = c.getDeclaredMethods();
		for (Method m : mets)
			System.out.println( m );
		System.out.println();
		System.out.println( "Llamada por reflectividad a un método: " );
		for (Method m : mets)
			if (m.getName().equals("metodo1")) {
				try {
					m.setAccessible( true ); // Hala!!!  Incluso si es privado!!!
					m.invoke( new EjemploReflectividad(), 5 );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
}
