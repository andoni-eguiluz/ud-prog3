package es.deusto.prog3.cap00.resueltos;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

/** Ejercicio de hilos  con ventanas. Esta clase carga el texto del Quijote en un área de texto,
 * y permite navegar por el área con la scrollbar y con botones de página arriba y página abajo.
 * 1. Modificarlo para que al pulsar los botones el scroll se haga con una animación 
 * a lo largo de un segundo, en lugar de en forma inmediata.
 * 2. Prueba a pulsar muy rápido varias páginas abajo. ¿Cómo lo arreglarías para que el scroll
 * en ese caso funcione bien y vaya bajando una página tras otra pero las baje *completas*?
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class VentanaQuijote extends JFrame {

	private JTextArea taTexto;
	private JScrollPane spTexto;
	
	public VentanaQuijote() {
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
	}
	
	private ArrayList<Thread> listaHilosScroll = new ArrayList<>();
	
	// Mueve el área de texto en vertical
	// pixelsVertical -> Número de pixels a mover, si es + hacia abajo y si es - hacia arriba
	private void muevePagina( int pixelsVertical ) {
		// TODO Cambiar este comportamiento de acuerdo a los comentarios de la cabecera de clase
		Thread hilo = new Thread() {
			@Override
			public void run() {
				Thread hiloAnterior = null;
				if (!listaHilosScroll.isEmpty()) {
					listaHilosScroll.add( this );
					hiloAnterior = listaHilosScroll.get( listaHilosScroll.size()-2 );
					// System.out.println( "Lista de hilos pendientes: " + listaHilosScroll );
					try {
						hiloAnterior.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//				} else {
//					listaHilosScroll.add( this );
				}
				JScrollBar bVertical = spTexto.getVerticalScrollBar();
				System.out.println( "Moviendo texto de " + bVertical.getValue() + " a " + (bVertical.getValue()+pixelsVertical) );
				// bVertical.setValue( bVertical.getValue() + pixelsVertical );
				int incremento = pixelsVertical / Math.abs(pixelsVertical);  // Calcular pixels a subir o bajar, -1 o +1
				for (int i=0; i<Math.abs(pixelsVertical); i++) {
					bVertical.setValue( bVertical.getValue() + incremento );
					try {
						Thread.sleep( 10 );
					} catch (InterruptedException e) {
						// e.printStackTrace();
						return; // Paro el scroll - me han interrumpido
					}
				}
				System.out.println( "Pixel final: " + bVertical.getValue() );
			}
		};
		hilo.start();
	}
	
	private void cargaQuijote() {
		try {
			Scanner scanner = new Scanner( VentanaQuijote.class.getResourceAsStream( "DonQuijote.txt" ), "UTF-8" );
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
		VentanaQuijote v = new VentanaQuijote();
		v.setVisible( true );
		v.cargaQuijote();
	}

}
