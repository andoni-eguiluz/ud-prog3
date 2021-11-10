package es.deusto.prog3.cap06;

import java.awt.*;
import javax.swing.*;

/** Ejemplo de renderer en un JList - la lista de la derecha se visualiza con un renderer diferente
 * que añade una progressbar al texto
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploJListRenderer {

	public static void main(String[] args) {
		JFrame f = new JFrame( "Test JList" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize( 800, 600 );
		DefaultListModel<Estadistica> m1 = new DefaultListModel<>();
		m1.addElement( new Estadistica( "Andoni", 0.5 ) );
		m1.addElement( new Estadistica( "Loreto", 0.7 ) );
		m1.addElement( new Estadistica( "Arantza", 0.1 ) );
		m1.addElement( new Estadistica( "Juanma", 0.9 ) );
		m1.addElement( new Estadistica( "Isabel", 0.3 ) );
		m1.addElement( new Estadistica( "Jaime", 0.7 ) );
		JList<Estadistica> l1 = new JList<Estadistica>( m1 );
		l1.setFont( new Font( "Arial", Font.PLAIN, 20 ));
		f.add( new JScrollPane(l1), BorderLayout.WEST );
		JList<Estadistica> l2 = new JList<Estadistica>( m1 );
		l2.setCellRenderer( new MiRenderer() );
		l2.setFont( new Font( "Arial", Font.PLAIN, 20 ));
		f.add( new JScrollPane(l2), BorderLayout.EAST );
		f.setVisible( true );
	}
	
	@SuppressWarnings("serial")
	private static class MiRenderer extends DefaultListCellRenderer {
		private JPanel miPanel;
		private JProgressBar miPB;
		private JLabel miLabel;
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (miPanel==null) {
				miPanel = new JPanel();
				miPanel.setLayout( new BorderLayout() );
				miLabel = new JLabel( "" );
				miLabel.setBackground( Color.white );
				miLabel.setOpaque( true );
				miLabel.setFont( new Font( "Arial", Font.PLAIN, 20 ));
				miPB = new JProgressBar( 0, 1000 );
				miPanel.add( miLabel, BorderLayout.CENTER );
				miPanel.add( miPB, BorderLayout.EAST );
			}
			if (isSelected) {  // Para diferenciar las filas seleccionadas del JLabel
				miLabel.setBackground( Color.LIGHT_GRAY );
			} else {
				miLabel.setBackground( Color.WHITE );
			}
			Estadistica e = (Estadistica)value;
			miLabel.setText( e.nombre );
			miPB.setValue( (int) Math.round(e.porcentaje*1000) );
			return miPanel;
		}
		
	}
	
	private static class Estadistica {
		String nombre;     // Nombre de usuario
		double porcentaje; // de 0.0 a 1.0
		public Estadistica(String nombre, double porcentaje) {
			this.nombre = nombre;
			this.porcentaje = porcentaje;
		}
		@Override
		public String toString() {
			return nombre;
		}
	}

}
