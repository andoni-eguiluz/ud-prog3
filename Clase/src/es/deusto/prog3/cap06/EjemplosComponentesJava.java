package es.deusto.prog3.cap06;

import java.awt.*;
import java.util.Date;

import javax.swing.*;

public class EjemplosComponentesJava {

	public static void main(String[] args) {
		ejemploSpinnerHora();
	}
	
	private static void ejemploSpinnerHora() {
		JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
		timeSpinner.setEditor(timeEditor);
		timeSpinner.setValue(new Date()); // will only show the current time
		JFrame f = new JFrame( "Ejemplo spinner para hora" );
		f.add( timeSpinner );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.pack();
		f.setVisible( true );
	}
	
}
