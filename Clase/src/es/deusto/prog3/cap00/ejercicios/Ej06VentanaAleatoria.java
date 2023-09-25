package es.deusto.prog3.cap00.ejercicios;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Código de base para una ventana de Swing, de cara a completar el ejercicio 0.6
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
@SuppressWarnings("serial")
public class Ej06VentanaAleatoria extends JFrame {
	private static Dimension TAMANYO_INICIAL_VENTANA = new Dimension( 400, 300 );

	public static void main(String[] args) {
		Ej06VentanaAleatoria v = new Ej06VentanaAleatoria();
		v.setVisible(true);
	}

	public Ej06VentanaAleatoria() {
		super( "Ventana aleatoria - ej. 0.6" );
		setSize( TAMANYO_INICIAL_VENTANA );
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

}
