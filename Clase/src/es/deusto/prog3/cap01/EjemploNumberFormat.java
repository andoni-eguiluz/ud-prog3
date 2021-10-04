package es.deusto.prog3.cap01;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class EjemploNumberFormat {

	public static void main(String[] args) {
		// Formatear 1.234.567,89
		double d = 1_234_567.89;  // Los _ se pueden usar para leer mejor los números largos
		DecimalFormat dfLocale = new DecimalFormat();
		NumberFormat nfUS = DecimalFormat.getNumberInstance( Locale.US );
		DecimalFormat dfManual = new DecimalFormat("0");
		DecimalFormat dfManualConDecimales = new DecimalFormat("0.000");
		System.out.println( "Formato local: " + dfLocale.format( d ));
		System.out.println( "Formato US: " + nfUS.format( d ));
		System.out.println( "Formato adhoc entero: " + dfManual.format( d ));
		System.out.println( "Formato adhoc real: " + dfManualConDecimales.format( d ));
		// Ver documentación de java.util.Formatter
		System.out.println( "Formato a través de " +
				String.format( "String.format(): #%1$5d# vs. #%2$,12.1f#", 123, d ) );
	}

}
