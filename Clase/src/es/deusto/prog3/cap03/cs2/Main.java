package es.deusto.prog3.cap03.cs2;

import java.io.File;

import javax.swing.JOptionPane;

/** Clase de prueba del ejemplo de persistencia de productos implementado con tres alternativas:
 *   1. Ficheros 
 *   2. Base de datos
 *   3. Base de datos y servidor de ficheros remoto con arquitectura cliente/servidor
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class Main {

	public static void main(String[] args) {
		// TODO cambiar el gestor de persistencia según lo que se quiera probar
		ServicioPersistenciaProductos servicio = null;
		
		String[] modos = {
			"Memoria con ficheros",
			"Base de datos local",
			"Base de datos y serv. ficheros remoto"
		};
		String resp = (String) JOptionPane.showInputDialog( null, "Selecciona modo de gestión de persistencia", "Modo de trabajo", JOptionPane.QUESTION_MESSAGE, null, modos, "Memoria con ficheros" );
		
		if (resp.equals(modos[0])) {
			// Caso 1 - gestor de persistencia en memoria con guardado en ficheros
			servicio = new ServicioPersistenciaFicheros();
			servicio.init( "prueba-productos.dat", "" );
			if (servicio.getNumeroProductos() == 0) {
				servicio.initDatosTest( "prueba-productos.dat", "" );
			}
		} else if (resp.equals(modos[1])) {
			// Caso 2 - gestor de persistencia en base de datos local
			servicio = new ServicioPersistenciaBD();
			File fileBD = new File( "prueba-productos.db" );
			if (fileBD.exists()) {
				servicio.init( "prueba-productos.db", "" );
			} else {
				servicio.initDatosTest( "prueba-productos.db", "" );
			}
		} else if (resp.equals(modos[2])) {
			JOptionPane.showMessageDialog( null, "Lanza un servidor local de BD antes de probar esta opción" );
			// Caso 3 - gestor de persistencia en base de datos remota
			servicio = new ServicioPersistenciaBDRemota();
			boolean ok = servicio.init( ConfigCS.HOST, ConfigCS.PUERTO + "" );
			if (ok && servicio.getNumeroProductos() == 0) {
				ok = servicio.initDatosTest( ConfigCS.HOST, ConfigCS.PUERTO + "" );
			}
			if (!ok) {
				JOptionPane.showMessageDialog( null, "No ha podido establecerse conexión con el servidor de productos", "Error en conexión", JOptionPane.ERROR_MESSAGE, null );
				return;
			}
		}
		
		VentanaConsultaProductos ventana = new VentanaConsultaProductos( servicio );
		ventana.setVisible( true );
	}

}
