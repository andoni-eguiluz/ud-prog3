package es.deusto.prog3.cap06;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

/** Clase para probar y entender el funcionamiento de la JTable
 * con su modelo de datos, y sus renderers y editors
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ProbandoJTable {
	private static JFrame vent;
	private static JTable tabla;
	private static DefaultTableModel modelo;
	
	public static void main(String[] args) {
		// Ventana rápida
		vent = new JFrame();
		vent.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		vent.setSize( 400, 300 );
		vent.setLocation(2000, 0);
		
		// Tabla en ventana
		modelo = new DefaultTableModel( new Object[] { "Nom", "Cod" }, 0 );
		tabla = new JTable( modelo );
		vent.getContentPane().add( new JScrollPane( tabla ), BorderLayout.CENTER );
		
		// Añadir datos
		modelo.addRow( new Object[] { "Itziar", 70 } );
		Vector<Object> v = new Vector<>();
		v.add( "Andoni" );
		v.add( 120 );
		modelo.addRow( v );
		modelo.addRow( new Object[] { "Xabi", 220 } );
		
		// Cambios de anchura
		tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
		tabla.getColumnModel().getColumn(1).setMaxWidth(100);
		tabla.getColumnModel().getColumn(1).setMinWidth(80);
		
		// Investiguemos el modelo
		modelo = new DefaultTableModel( new Object[] { "Nom", "Cod" }, 0 ) { // Herencia

			@Override
			public boolean isCellEditable(int row, int column) {
				boolean val = super.isCellEditable(row, column);
				System.out.println( "IsCellEditable " + row + "," + column + " -> " + val);
				if (column==0) return false;
				else return super.isCellEditable(row, column);
			}

			@Override
			public Object getValueAt(int row, int column) {
				Object val = super.getValueAt(row, column);
				System.out.println( "getValueAt " + row + "," + column + " <- " + val );
				return val;
			}

			@Override
			public void setValueAt(Object aValue, int row, int column) {
				System.out.println( "setValueAt " + row + "," + column + " -> " + aValue );
				try {
					int valor = Integer.parseInt( aValue + "" );
					if (valor<=255) {
						super.setValueAt(aValue, row, column);
					}
				} catch (NumberFormatException e) {
					// No cambiar nada
				}
			} 
			
		};
		modelo.addRow( new Object[] { "Itziar", 70 } );
		modelo.addRow( new Object[] { "Andoni", 120 } );
		modelo.addRow( new Object[] { "Xabi", 220 } );
		tabla.setModel( modelo );
		
		// TODO next week
		// tabla.setDefaulRenderer
		
		vent.setVisible( true );
	}
}
