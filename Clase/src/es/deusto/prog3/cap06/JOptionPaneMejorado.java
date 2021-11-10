package es.deusto.prog3.cap06;

import javax.swing.*;

/** Ejemplos de JOptionPane con posibilidades añadidas de interacción
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class JOptionPaneMejorado {

	public static void main(String[] args) {
		dialogoConCheckbox();
		dialogoConLista();
		dialogoConSpinner();
	}
	
	private static void dialogoConCheckbox() {
		JCheckBox checkbox = new JCheckBox("No lo tengo claro");
		String mensaje = "¿Estás seguro/a?";
		Object[] params = { mensaje, checkbox };  
		// El mensaje normalmente es un string, pero si se da un array de strings se añaden todos al cuadro de diálogo en vertical
		int n = JOptionPane.showConfirmDialog(null, params, "Pregunta con checkbox", JOptionPane.YES_NO_OPTION);
		System.out.println( "Opción elegida: " + n );
		System.out.println( "Seleccionado el checkbox? " + checkbox.isSelected() );
	}

	private static void dialogoConLista() {
		JList<String> lista = new JList<>( new String[] { "Azul", "Verde", "Rojo" } );
		lista.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );  // Podría ser SINGLE_SELECTION si solo se quiere seleccionar uno
		String mensaje = "¿Quieres dibujar?";
		String mensaje2 = "Selecciona color o colores con <Ctrl>";
		Object[] params = { mensaje, mensaje2, lista };  
		int n = JOptionPane.showConfirmDialog(null, params, "Pregunta con lista", JOptionPane.YES_NO_OPTION);
		System.out.println( "Opción elegida: " + n );
		System.out.println( "Seleccionado de la lista? " + lista.getSelectedValuesList() );
	}

	private static void dialogoConSpinner() {
		JSpinner spinner1 = new JSpinner();
		spinner1.setValue( 10 );
		JSpinner spinner2 = new JSpinner();
		spinner2.setValue( 50 );
		String mensaje = "Indica ancho y alto deseados";
		Object[] params = { mensaje, spinner1, spinner2 };  
		int n = JOptionPane.showConfirmDialog(null, params, "Pregunta con spinner", JOptionPane.OK_CANCEL_OPTION );
		System.out.println( "Opción elegida: " + n );
		System.out.println( "Valores spinners: " + spinner1.getValue() + " , " + spinner2.getValue() );
	}

}
