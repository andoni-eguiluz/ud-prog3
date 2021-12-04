package es.deusto.prog3.cap05;

import java.lang.reflect.Field;
import java.util.ArrayList;

/** Clase de utilidad - permite consultar la capacidad de un ArrayList<p>
 * Implementación dependiente de la JVM utilizada. Supone que hay un atributo
 * elementData[] que contiene los elementos del arraylist. Devuelve la longitud 
 * de array de ese atributo.
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class UtilidadArrayList {

    static final Field atributoElementData;
    static {
        try {
            atributoElementData = ArrayList.class.getDeclaredField("elementData");
            atributoElementData.setAccessible(true);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <E> int getArrayListCapacity(ArrayList<E> arrayList) {
        try {
            final E[] elementData = (E[]) atributoElementData.get(arrayList);
            return elementData.length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
