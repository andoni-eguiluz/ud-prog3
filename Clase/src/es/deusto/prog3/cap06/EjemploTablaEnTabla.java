package es.deusto.prog3.cap06;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Prueba de concepto de tabla dentro de tabla
 * solo con renderer - o sea, solo se puede visualizar. Para editar habría que cambiar el tableeditor
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class EjemploTablaEnTabla {
	private static JFrame vent;
	private static JTable tabla;
	private static DefaultTableModel modelo;
	
	public static void main(String[] args) {
		// Ventana rápida
		vent = new JFrame();
		vent.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		vent.setSize( 400, 300 );

		// Para localizarla dependiendo del tamaño de pantalla:
		Dimension tamPantalla = Toolkit.getDefaultToolkit().getScreenSize();
		vent.setLocation(tamPantalla.width - 600, 0);  // La pone a 600 pixels de la esquina derecha de escritorio
		
		// Tabla en ventana
		modelo = new DefaultTableModel( new Object[] { "Nombre", "Cantidad", "Tabla datos" }, 0 );
		tabla = new JTable( modelo );
		vent.getContentPane().add( new JScrollPane( tabla ), BorderLayout.CENTER );
		
		// Añadir datos
		JTable tablaDatos1 = new JTable( new Object[][] { { "HP Laptop 15s", "595"  }, { "Lenovo IdeaPad 3", "749" }, { "Apple MacBook Air", "959" } }, new Object[] { "Prod", "Precio" } );
		JTable tablaDatos2 = new JTable( new Object[][] { { "HUAWEI Display 23.8", "109" }, { "Samsung LF24T350", "139" } }, new Object[] { "Nombre", "Precio "} );
		modelo.addRow( new Object[] { "Portátil", 3, tablaDatos1 } );
		modelo.addRow( new Object[] { "Monitor", 2, tablaDatos2 } );

		// Cambios de anchura
		tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
		tabla.getColumnModel().getColumn(1).setMinWidth(40);
		tabla.getColumnModel().getColumn(1).setMinWidth(40);
		tabla.getColumnModel().getColumn(2).setMinWidth(250);
		
		tabla.getTableHeader().setReorderingAllowed(false);  // Prohibe el movimiento de columnas del usuario
		
		tabla.setDefaultRenderer( Object.class, new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component ret = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (value instanceof JTable) {  // Lo que hay en la celda es una tabla: cambiamos el render
					// La propia subtabla funcionará como render
					JTable tablaInterna = (JTable) value;
					// Cambio de altura de la fila correspondiente de la tabla principal
					tabla.setRowHeight( row, tablaInterna.getModel().getRowCount() * 16 );  // Ajuste a ojo de altura de cada fila de subtabla
					return tablaInterna;
				}
				return ret;
			}
		});
		
		vent.setVisible( true );
	}
}
