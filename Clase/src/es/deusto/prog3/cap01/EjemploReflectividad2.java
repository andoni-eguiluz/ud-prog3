package es.deusto.prog3.cap01;

import java.lang.reflect.Method;

/** Prueba de acceso a método privado de clase ajena  (!!!)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploReflectividad2 {

	public static void main(String[] args) {
		Class<?> c = EjemploReflectividad.class;
		Method[] mets = c.getDeclaredMethods();
		for (Method m : mets)
			if (m.getName().equals("metodo1")) {
				try {
					m.setAccessible( true ); // Hala!!!  Incluso si es privado!!!
					 // Si no se hace el setAccessible la llamada genera un error de ámbito IllegalAccessException
					m.invoke( new EjemploReflectividad(), 5 );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}
	
}
