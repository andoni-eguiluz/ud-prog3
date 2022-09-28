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
@SuppressWarnings("serial")
public class VentanaQuijoteConColaDeHilos extends JFrame {

	private JTextArea taTexto;
	JScrollPane spTexto;  // atributo de paquete para poder hacer test de JUnit
	JButton bPagAbajo;    // atributo de paquete para poder hacer test de JUnit
	
	public VentanaQuijoteConColaDeHilos() {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setTitle( "Don Quijote de la Mancha" );
		setSize( 800, 600 );
		setLocationRelativeTo( null );  // Pone la ventana relativa a la pantalla
		taTexto = new JTextArea();
		spTexto = new JScrollPane( taTexto );
		add( spTexto, BorderLayout.CENTER );
		JPanel pBotonera = new JPanel();
		JButton bPagArriba = new JButton( "^" );
		bPagAbajo = new JButton( "v" );
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
	
	private ArrayList<Thread> hilosActivos = new ArrayList<>(); // Podría haber algún problema de acceso concurrente (sería mala suerte, pero... esas cosas pasan. Mejor Vector)
	
	// private Thread hiloActual;  // No hace falta definirlo como atributo...
	
	private void muevePagina( int pixelsVertical ) {
		Thread hiloActual = new Thread() {  // ...porque solo lo usamos aquí
			public void run() {
				// Thread yo = hiloActual;  // En vez de guardar el hilo para cogerlo en la variable
				Thread yo = this;  // ... lo podemos tomar directamente del this
				hilosActivos.add( yo );
				while (hilosActivos.get(0) != yo) {
					System.out.println( "Soy " + yo.getName() + " y la cola es " + hilosActivos );
					try {
						Thread.sleep( 10 );
					} catch (InterruptedException e) {}
				}
				JScrollBar bVertical = spTexto.getVerticalScrollBar();
				System.out.println( "Moviendo texto de " + bVertical.getValue() + " a " + (bVertical.getValue()+pixelsVertical) );
				// bVertical.setValue( bVertical.getValue() + pixelsVertical );
				if (pixelsVertical>0) {
					for (int i=0; i<pixelsVertical; i++) {
						bVertical.setValue( bVertical.getValue() + 1 );
						try {Thread.sleep(10); } catch (InterruptedException e) {}
					}
				} else {
					// for (int i=0; i>pixelsVertical; i--) {
					for (int i=0; i<Math.abs(pixelsVertical); i++) {
						bVertical.setValue( bVertical.getValue() - 1 );
						try {Thread.sleep(10); } catch (InterruptedException e) {}
					}
				}
				hilosActivos.remove(0);
			}
		};
		hiloActual.start();
	}
	
	protected void cargaQuijote() {  // protected para poder hacer test de JUnit
		try {
			Scanner scanner = new Scanner( VentanaQuijoteConColaDeHilos.class.getResourceAsStream( "DonQuijote.txt" ), "UTF-8" );
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
		VentanaQuijoteConColaDeHilos v = new VentanaQuijoteConColaDeHilos();
		v.setVisible( true );
		v.cargaQuijote();
	}

}
