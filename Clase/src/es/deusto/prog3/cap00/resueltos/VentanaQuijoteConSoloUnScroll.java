package es.deusto.prog3.cap00.resueltos;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

import javax.swing.*;

/** Ejercicio del quijote (0.2) con scroll recortado (cada vez que se pide un scroll se para el anterior)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class VentanaQuijoteConSoloUnScroll extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextArea taTexto;
	private JScrollPane spTexto;
	private boolean acabaScroll;  // Variable para determinar si se quiere parar el scroll
	private Thread hiloEnCurso = null; // Variable para saber qué hilo está en curso
	
	public VentanaQuijoteConSoloUnScroll() {
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
	
	private void muevePagina( int pixelsVertical ) {
		Thread hilo = new Thread() {
			public void run() {
				acabaScroll = true;  // Informamos al hilo anterior para que acabe (pacíficamente)
				// ESPERAR!!!! A que el hilo anterior acabe
 				while (hiloEnCurso!=null) {
					System.out.println( "Esperando!");
					// Esperar
					try {
						Thread.sleep( 10 );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
 				// Y ahora nuestro hilo ya está solo. Marcamos que puede funcionar
				acabaScroll = false;
				hiloEnCurso = this;  // Y guardamos el nuevo hilo
				JScrollBar bVertical = spTexto.getVerticalScrollBar();
				System.out.println( "Moviendo texto de " + bVertical.getValue() + " a " + (bVertical.getValue()+pixelsVertical) );
				// Sentencia original:
				// bVertical.setValue( bVertical.getValue() + pixelsVertical );
				// Convertida ahora en un bucle:
				int incremento = (pixelsVertical<0) ? -1 : +1;  // El incremento es -1 (subir) o +1 (bajar)
				for (int i=0; i<Math.abs(pixelsVertical); i++) {
					if (acabaScroll) {
						break;
					}
					bVertical.setValue( bVertical.getValue() + incremento );
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				hiloEnCurso = null;
			}
		};
		hilo.start();
	}
	
	private void cargaQuijote() {
		try {
			Scanner scanner = new Scanner( VentanaQuijoteConSoloUnScroll.class.getResourceAsStream( "DonQuijote.txt" ), "UTF-8" );
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
		VentanaQuijoteConSoloUnScroll v = new VentanaQuijoteConSoloUnScroll();
		v.setVisible( true );
		v.cargaQuijote();
	}

}
