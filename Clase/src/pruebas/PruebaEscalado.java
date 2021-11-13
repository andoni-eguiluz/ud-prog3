package pruebas;

import javax.swing.*;

import es.deusto.prog3.utils.JLabelGrafico;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

// Distintos modos de escalar - ver diferencia de calidad
public class PruebaEscalado extends JFrame {

	public PruebaEscalado() {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 1300, 600 );
		setLocation( 0, 0 );
		this.setLayout( null );
		
		// Añadir imagen con JLabelGrafico (dibujado escalado en paintComponent)
		JLabelGrafico lCoche1 = new JLabelGrafico( "/es/deusto/prog3/cap00/coche.png", 500, 500 );
		lCoche1.setLocation( 20, 20 );
		this.add( lCoche1 );
		
		// Añadir imagen con escalado en memoria
		ImageIcon imageIcon = new ImageIcon( PruebaImagen.class.getResource("/es/deusto/prog3/cap00/coche.png") );
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance( 500, 500,  java.awt.Image.SCALE_SMOOTH ); // Probar y ver cómo cambia con SCALE_FAST
		imageIcon = new ImageIcon(newimg);  // transform it back
		JLabel lCoche2 = new JLabel( imageIcon );
		lCoche2.setBounds( 420, 20, 500, 500 );
		this.add( lCoche2 );
		
		// Añadir imagen con pintado en memoria en un bufferedimage
		ImageIcon imageIcon2 = new ImageIcon( PruebaImagen.class.getResource("/es/deusto/prog3/cap00/coche.png") );
		Image image2 = imageIcon2.getImage();
		BufferedImage resizedImg = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image2, 0, 0, 500, 500, null);
		g2.dispose();
		JLabel lCoche3 = new JLabel( new ImageIcon(resizedImg) );
		lCoche3.setBounds( 820, 20, 500, 500 );
		this.add( lCoche3 );
	}

	public static void main(String[] args) {
		PruebaEscalado ventana = new PruebaEscalado();
		ventana.setVisible( true );
	}

}
