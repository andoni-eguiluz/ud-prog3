package es.deusto.prog3.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.deusto.prog3.utils.tabla.*;
import es.deusto.prog3.utils.tabla.iu.*;

	
/** Prueba de carga de un CSV de datos de ATP en tabla visual
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ProcesoCSVTenis {

	public static void main( String[] s ) {
		atp();
	}
	
		private static VentanaDatos newVentanaTabla( VentanaGeneral vg, Tabla tabla, String codTabla, int posX, int posY ) {
			try {
				String tit = codTabla + " (" + tabla.size() + ")";
				VentanaDatos vd = new VentanaDatos( vg, codTabla, tit ); 
				vd.setSize( 1400, 800 );
				vd.setTabla( tabla ); 
				vg.addVentanaInterna( vd, codTabla );
				vd.setLocation( posX, posY );
				vd.addBoton( "-> clipboard", new Tabla.CopyToClipboard( tabla, vd ) );
				vd.setVisible( true ); 
				try { Thread.sleep( 100 ); } catch (InterruptedException e) {}
				return vd;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
	private static VentanaGeneral ventana;
	private static final SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );

	private static void atp() {
		try {
			String urlATP = "dataATP.csv";
			
			ventana = new VentanaGeneral();
			ventana.setSize( 1800, 1000 );
			ventana.setEnCierre( new Runnable() { public void run() {  } } );
			ventana.setTitle( "Revisión de datos de Inspira 2020" );
			ventana.setVisible( true );

			// Crea la tabla con todos los datos
			Tabla tablaATPBruto = Tabla.processCSV( ProcesoCSVTenis.class.getResource( urlATP ) );
			System.out.println( tablaATPBruto.getHeadersTabs( "" ) );
			
			// Filtra solo las columnas interesantes y muestra la ventana
			Tabla tablaATPFiltrada = tablaATPBruto.crearTablaConCols( "ATP", "Location", "Tournament", "Date", "Series", "Court", "Surface", "Round", "Best of", "Winner", "Loser", "WRank", "LRank", "W1", "L1", "W2", "L2", "W3", "L3", "W4", "L4", "W5", "L5", "Wsets", "Lsets" );
			newVentanaTabla( ventana, tablaATPFiltrada, "ATP Filtrada", 20, 20 );
			
			// Hace algo con los datos (cambiar esto si se quiere hacer otra cosa)
			for (int i=0; i<tablaATPFiltrada.size(); i++) {
				Date fecha = tablaATPFiltrada.getDate( i, "Date" );
				String torneo = tablaATPFiltrada.get( i, "Tournament" );
				String tipo = tablaATPFiltrada.get( i, "Series" );
				String ronda = tablaATPFiltrada.get( i, "Round" );
				String ganador = tablaATPFiltrada.get( i, "Winner" );
				String perdedor = tablaATPFiltrada.get( i, "Loser" );
				int setsGanados = tablaATPFiltrada.getInt( i, "Wsets" );
				int setsPerdidos = tablaATPFiltrada.getInt( i, "Lsets" );
				// Ejemplo: se visualizan solo las finales
				if (ronda.equals("The Final") && tipo.equals( "Grand Slam" )) {
					System.out.println( torneo + "\t" + sdf.format(fecha) + "\t" + ronda + "\t" + ganador + "\t" + perdedor + "\t" + (setsGanados + "-" + setsPerdidos) );
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

