package es.deusto.prog3.cap00.resueltos;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

//Versión que incorpora cierre de hilos cuando se cierra la ventana (se corta el trabajo de scroll pendiente)

/** Ejercicio de hilos  con ventanas. Esta clase carga el texto del Quijote en un área de texto,
 * y permite navegar por el área con la scrollbar y con botones de página arriba y página abajo.
 * 1. Modificarlo para que al pulsar los botones el scroll se haga con una animación 
 * a lo largo de un segundo, en lugar de en forma inmediata.
 * 2. Prueba a pulsar muy rápido varias páginas abajo. ¿Cómo lo arreglarías para que el scroll
 * en ese caso funcione bien y vaya bajando una página tras otra pero las baje *completas*?
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
@SuppressWarnings("serial")
public class VentanaQuijoteConColaDeHilosEInterrupcion extends JFrame {

	private JTextArea taTexto;
	private JScrollPane spTexto;
	
	public VentanaQuijoteConColaDeHilosEInterrupcion() {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setTitle( "Don Quijote de la Mancha" );
		setSize( 800, 600 );
		setLocationRelativeTo( null );  // Pone la ventana relativa a la pantalla
		taTexto = new JTextArea();
		spTexto = new JScrollPane( taTexto );
		add( spTexto, BorderLayout.CENTER );
		JPanel pBotonera = new JPanel();
		JButton bPagArriba = new JButton( "^" );
		JButton bPagAbajo = new JButton( "v" );
		pBotonera.add( bPagArriba );
		pBotonera.add( bPagAbajo );
		add( pBotonera, BorderLayout.SOUTH );
		bPagArriba.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				muevePagina( -(spTexto.getHeight()-20) );
			}
		});
		bPagAbajo.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				muevePagina( (spTexto.getHeight()-20) );
			}
		});
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				for (Thread t : hilosActivos) {
					t.interrupt();
				}
			}
		});
	}
	
	private ArrayList<Thread> hilosActivos = new ArrayList<>();
	
	// private Thread hiloActual;  // No hace falta definirlo como atributo...
	
	private void muevePagina( int pixelsVertical ) {
		Thread hiloActual = new Thread() {  // ...porque solo lo usamos aquí
			public void run() {
				// Thread yo = hiloActual;  // En vez de guardar el hilo para cogerlo en la variable
				// Thread yo = this;  // ... lo podemos tomar directamente del this
				hilosActivos.add( this );
				while (hilosActivos.get(0) != this) {
					if (interrupted()) return; // Posible interrupción
					// System.out.println( "Soy " + getName() + " y la cola es " + hilosActivos );
					try {
						Thread.sleep( 10 );
					} catch (InterruptedException e) {
						// e.printStackTrace();
						return; // Posible interrupción
					}
				}
				JScrollBar bVertical = spTexto.getVerticalScrollBar();
				System.out.println( "Moviendo texto de " + bVertical.getValue() + " a " + (bVertical.getValue()+pixelsVertical) );
				// bVertical.setValue( bVertical.getValue() + pixelsVertical );
				if (pixelsVertical>0) {
					for (int i=0; i<pixelsVertical; i++) {
						if (interrupted()) return; // Posible interrupción
						bVertical.setValue( bVertical.getValue() + 1 );
						try {
							Thread.sleep(10); 
						} catch (InterruptedException e) {
							return; // Posible interrupción
						}
					}
				} else {
					// for (int i=0; i>pixelsVertical; i--) {
					for (int i=0; i<Math.abs(pixelsVertical); i++) {
						if (interrupted()) return; // Posible interrupción
						bVertical.setValue( bVertical.getValue() - 1 );
						try {
							Thread.sleep(10); 
						} catch (InterruptedException e) {
							return; // Posible interrupción
						}
					}
				}
				hilosActivos.remove(0);
			}
		};
		hiloActual.start();
	}
	
	private void cargaQuijote() {
		try {
			Scanner scanner = new Scanner( VentanaQuijoteConColaDeHilosEInterrupcion.class.getResourceAsStream( "DonQuijote.txt" ), "UTF-8" );
			while (scanner.hasNextLine()) {
				String linea = scanner.nextLine();
				taTexto.append( linea + "\n" );
			}
			scanner.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog( this, "No se ha podido cargar el texto" );
		}
	}

	public static void main(String[] args) {
		VentanaQuijoteConColaDeHilosEInterrupcion v = new VentanaQuijoteConColaDeHilosEInterrupcion();
		v.setVisible( true );
		v.cargaQuijote();
	}

}
