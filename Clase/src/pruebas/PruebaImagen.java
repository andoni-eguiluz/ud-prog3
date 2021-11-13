package pruebas;

import javax.swing.*;

import es.deusto.prog3.utils.JLabelGrafico;

import java.awt.*;
import java.awt.event.*;

// Diferencia entre añadir una imagen en JLabel y en JLabelGrafico (o alguna variante)
public class PruebaImagen extends JFrame {

	private CocheGrafico lCoche2;
	
	public PruebaImagen() {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 600, 300 );
		setLocation( 0, 0 );
		this.setLayout( null );
		// Añadir una imagen con JLabel
		Icon imagen = new ImageIcon( PruebaImagen.class.getResource( "/es/deusto/prog3/cap00/coche.png" ) );
		JLabel lCoche = new JLabel( imagen );
		lCoche.setLocation( 100, 100 );
		lCoche.setSize( 50, 50 );
		// lCoche.setBounds( 100, 100, 50, 50 );
		lCoche.setOpaque( true );
		lCoche.setBackground( Color.CYAN );
		this.add( lCoche );
		// Añadirla con JLabelGrafico
		lCoche2 = new CocheGrafico( "/es/deusto/prog3/cap00/coche.png", 50, 50 );
		lCoche2.setLocation( 400, 100 );
		this.add( lCoche2 );
	}

	public static void main(String[] args) {
		PruebaImagen ventana = new PruebaImagen();
		ventana.setVisible( true );
		ventana.lCoche2.setRotacion( Math.PI / 2 );
		ventana.lCoche2.setOpacidad( 0.2f );
		for (int x=400; x<=500; x++) {
			ventana.lCoche2.setLocation(x, 100);
			try {
				Thread.sleep( 10 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class CocheGrafico extends JLabelGrafico {
	private double vx;
	private double vy;
	private double posX;
	private double posY;
	public CocheGrafico( String nom, int anch, int alt ) {
		super( nom, anch, alt );
	}
	public void setX( double x ) {
		posX = x;
		setLocation( (int) Math.round(x) , getLocation().y );
	}
}

