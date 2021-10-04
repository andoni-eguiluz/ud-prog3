package es.deusto.prog3.cap01;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class EjemploLocale {

	// Muestra la información básica del Locale
	// y la fecha de hoy maquetada en formato largo de esa info local
	private static void muestraLocale( Locale l ) {
		System.out.println( "   " + l.getLanguage() + ", " + l.getCountry()
				+ ", " + l.getVariant() + ", " + l.getDisplayName() );
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, l);
		String fechaLocal = df.format( new Date() );
		System.out.println( "      Fecha en este locale: " + fechaLocal );
	}
	
	public static void main(String[] a) {
		// Coger el locale por defecto
		Locale l = Locale.getDefault();
		System.out.println( "   Idioma, País, Variante, Nombre" );
		System.out.println();
		System.out.println("Locale por defecto: ");
		muestraLocale( l );
		System.out.println();
		// Coger un local predefinido ad hoc
		l = Locale.ITALIAN;
		System.out.println("Locale italiano:"); 
		muestraLocale( l );
		System.out.println();
		// Definir un locale nuevo: inglés en china
		l = new Locale("en", "CN");
		System.out.println("Locale creado inglés - China:"); 
		muestraLocale( l );
		System.out.println();
		// get the supported locales
		Locale[] s = Locale.getAvailableLocales();
		System.out.println("Locales soportados: ");
		for (int i=0; i<s.length; i++) {
			muestraLocale( s[i] );
		}
	}
}
