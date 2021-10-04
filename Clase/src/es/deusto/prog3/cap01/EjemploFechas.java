package es.deusto.prog3.cap01;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class EjemploFechas  {

	/** Programa de prueba de fechas
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println( System.currentTimeMillis() );
		
		Date d1 = new Date();
		System.out.println( "Hoy = " + d1 );
		Date d2 = new Date( Long.MAX_VALUE );
		System.out.println( "Max día = " + d2 );
		Date d3 = new Date( Long.MIN_VALUE );   // En el año 0 vuelve a "rebotar"
		System.out.println( "Min día = " + d3 );
		// Formatear:
		SimpleDateFormat f1 = new SimpleDateFormat( "d/M" );
		System.out.println( "Día de hoy formateado 1: " + f1.format(d1) );
		SimpleDateFormat f2 = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
		System.out.println( "Día de hoy formateado 2: " + f2.format(d1) );
		String ej = "28/12/2015 3:2:1";
		Date d4 = f2.parse(ej);
		System.out.println( "Fecha creada partiendo de string " + ej + " ==> " + f2.format(d4) );

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d1);
		System.out.println( "Día de hoy (emp. en 1): " + gc.get( GregorianCalendar.DAY_OF_MONTH ) );
		System.out.println( "Mes de hoy (emp. en 0): " + gc.get( GregorianCalendar.MONTH ) );
		
		// System.out.println( d1.getDay() );   // deprecated
		// System.out.println( d1.getMonth() ); // deprecated
	}

}
