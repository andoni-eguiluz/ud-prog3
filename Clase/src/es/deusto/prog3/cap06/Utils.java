package es.deusto.prog3.cap06;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import javax.swing.JFrame;

public class Utils {
	public static void muestraThreadsActivos() {
		Set<Thread> s = Thread.getAllStackTraces().keySet();
		ArrayList<Thread> l = new ArrayList<Thread>( s );
		l.sort( new Comparator<Thread>() {
			@Override
			public int compare(Thread o1, Thread o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		System.out.print( "Threads activos (" + s.size() + "): " );
		for (Thread t : l) {
			if (t.isAlive())
				System.out.print( "\"" + t.getName() + (t.isDaemon()?" [d]":"") + "\" " );
		}
		System.out.println();
	}
	public static boolean isSwingActivo() {
		Set<Thread> s = Thread.getAllStackTraces().keySet();
		for (Thread t : s) {
			if (t.getName().startsWith( "AWT-EventQueue")) return true;  // El Thread principal de Swing empieza por AWT-EventQueue
		}
		return false;
	}
	public static void esperaTiempo( long millis ) {
		try {
			Thread.sleep( millis );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** Prueba de muestra de threads activos
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println( "Inicio");
		muestraThreadsActivos();
		JFrame f = new JFrame();
		f.setVisible( true );
		System.out.println( "Con ventana");
		muestraThreadsActivos();
		f.dispose();
		System.out.println( "Tras dispose");
		muestraThreadsActivos();
		try { Thread.sleep(3000); } catch (InterruptedException e) {}
		System.out.println( "Tras un ratín");
		muestraThreadsActivos();
	}
}
