package es.deusto.prog3.cap01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class EjemploLambda2 {

	static ArrayList<Multimedia> listaMm;
	
	public static void main(String[] args) {
		listaMm = new ArrayList<>();
		listaMm.add( new Multimedia( "Aleluyah", "mp4", 12.5f, Multimedia.Tipo.VIDEO ) );
		listaMm.add( new Multimedia( "Happy", "flv", 7.3f, Multimedia.Tipo.VIDEO ) );
		listaMm.add( new Multimedia( "Happy", "mp3", 0.8f, Multimedia.Tipo.AUDIO ) );
		listaMm.add( new Multimedia( "Royals", "mp4", 9.4f, Multimedia.Tipo.VIDEO ) );
		//
		int prueba = 4;  // Ir cambiando para probar distintas cosas
		// 1. Prueba normal
		if (prueba==1) sacaLosVideos( listaMm );
		// ¿Y si queremos sacar los vídeos entre 5 y 10 Mb? 
		// ¿O los audios que empiecen por A?
		// ¿Cómo generalizar este test para "cualquier cosa"? 
		// 2. Objeto con método test (clase anónima implementando un interfaz)
		if (prueba==2) {
			TestMultimedia miTest = new TestMultimedia() {
				@Override
				public boolean test(Multimedia m) {
					return (m.tipo==Multimedia.Tipo.VIDEO
						 && m.megas>=5 && m.megas<=10);
				}
			};
			sacaLosQueCumplanTestMultimedia( listaMm, miTest );
		}
		// 3. Objeto anónimo creado en la misma llamada
		// (Casi lo mismo que el caso 2)
		if (prueba==3) {
			sacaLosQueCumplanTestMultimedia( listaMm, 
					new TestMultimedia() {
						@Override
						public boolean test(Multimedia m) {
							return (m.tipo==Multimedia.Tipo.VIDEO
								 && m.megas>=5 && m.megas<=10);
						}
					}
			);
		}
		// 4. Expresión lambda
		if (prueba==4) {
			sacaLosQueCumplanTestMultimedia( listaMm, 
					(Multimedia m) -> m.tipo==Multimedia.Tipo.VIDEO
								      && m.megas>=5 && m.megas<=10
			);
			// Se puede obviar el tipo
			// sacaLosQueCumplanTestMultimedia( listaMm, 
			//		m  -> m.tipo==Multimedia.Tipo.VIDEO
			//		      && m.megas>=5 && m.megas<=10
			// );
		}
		// Puede hacerse con lambda expressions en lugar de interfaces si
		// el interfaz sólo tiene UN método (Functional Interface)
		//
		// Como es tan habitual hay Interfaces funcionales implementados
		// ya en java. Por ejemplo:
		// Predicate<T>  ==>  boolean test(T t)
		// Consumer<T>   ==>  void accept(T t)
		//
		// 5. Usando interfaces funcionales predefinidos:
		if (prueba==5) {
			consumeLosQueCumplan( listaMm, 
				m -> m.tipo==Multimedia.Tipo.VIDEO && m.megas>=5 && m.megas<=10,
				m -> System.out.println( m )
			);
		}
		

		// Qué interfaces conocemos con un sólo método?  
		// Todos esos permiten notación lambda.
		// Por ejemplo ActionListener:
		JButton bot1 = new JButton();
			// Lo tradicional...
			bot1.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println( "Botón pulsado" );
					System.out.println( "Fuente: " + e.getSource() );
				}
			});
			// se puede hacer con expresión lambda:
			bot1.addActionListener( 
				e -> {
					System.out.println( "Botón pulsado" );
					System.out.println( "Fuente: " + e.getSource() );
				}
			);
			
		// Otro ejemplo: Runnable (sin parámetros)
		SwingUtilities.invokeLater( () -> { /* Cosas de ventanas */ } );
	}
	
	static void sacaLosVideos( ArrayList<Multimedia> lista ) {
		for (Multimedia m : lista) {
			if (m.tipo == Multimedia.Tipo.VIDEO)
				System.out.println( m );
		}
	}

	static void sacaLosQueCumplanTestMultimedia( ArrayList<Multimedia> lista, 
			TestMultimedia t ) {
		for (Multimedia m : lista) {
			if (t.test(m))
				System.out.println( m );
		}
	}

	/** Sobre una lista de objetos multimedia, comprueba cuáles 
	 * cumplen una condición dada y sobre esos realiza una acción
	 * @param lista	Lista de objetos multimedia
	 * @param test	Condición de filtro
	 * @param haz	Acción a ejecutar sobre los objetos que pasen el filtro
	 */
	static void consumeLosQueCumplan( ArrayList<Multimedia> lista, 
			Predicate<Multimedia> test, 
			Consumer<Multimedia> haz ) {
		for (Multimedia m : lista) {
			if (test.test(m))
				haz.accept( m );
		}
	}

}

class Multimedia {
    public enum Tipo { VIDEO, AUDIO }
    String nombre;
    String extension;
    float megas;
    Tipo tipo;
    public Multimedia( String nombre, String extension, float megas, Tipo tipo ) {
    	this.nombre = nombre;
    	this.extension = extension;
    	this.megas = megas;
    	this.tipo = tipo;
    }
    @Override
    public String toString() {
    	return nombre + "." + extension + " (" + megas + " Mb): " + tipo;
    }
}

interface TestMultimedia {
	/**  Método de testeo
 	 * @param m	Objeto multimedia a evaluar
	 * @return	true si pasa el test, false si no lo pasa
	 */
	boolean test( Multimedia m );
}

//
// Interfaces predefinidos clásicos para notación lambda:  
// (excepto Runnable, todos en java.util.function)
//
// Runnable --> run()
// Consumer<T> --> void accept(T)
// Predicate<T> --> boolean test(T)
// Supplier<T> --> T get()
// Function<T,R> --> R apply(T)
