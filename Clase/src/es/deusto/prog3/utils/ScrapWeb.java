package es.deusto.prog3.utils;

import java.awt.Color;
//Imports relacionados con el proceso
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

//Imports de librería externa  -  https://sourceforge.net/projects/htmlparser/
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.TextNode;

import es.deusto.prog3.utils.ventanas.ventanaConsola.VentanaColorConsola;


/** 
* Clase de scrapping de una página web para procesar su contenido
* Programación II y III
* 
* Utiliza la librería externa htmlparser 1.6 
* Descargada en feb de 2016 de  https://sourceforge.net/projects/htmlparser/files/htmlparser/1.6/htmlparser1_6_20060610.zip
* Web del proyecto: https://sourceforge.net/projects/htmlparser/
* Descomprimir, guardar y enlazar fichero: htmlparser.jar
* 
* Programada como ejemplo para procesar estadísticas de jugadores desde la web de laliga.es
* 
*/
public class ScrapWeb {
	
	private static boolean MOSTRAR_TODOS_LOS_TAGS = false;
	public static void main(String[] args) {
		// La url que quieras analizar
		String urlAAnalizar = "https://www.tutiempo.net/clima/ws-80250.html";
		revisaWeb( urlAAnalizar );
		// proceso();
	}
	
	private static void proceso() {
		// Pasos a dar para procesar una web de forma automática.
		// 1.- Analizar sus contenidos. Poner aquí la URL con la que trabajar:
		// String urlAAnalizar = "http://www.marca.com/tag/f/0e/fernando_pacheco/estadisticas/primera/2016_17/";
		// revisaWeb( urlAAnalizar );
		// String urlAAnalizar2 = "http://www.laliga.es/jugador/busquets/partidos";
		// revisaWeb( urlAAnalizar2 );
		
		// 2.- Observar la forma que tiene la web en la ventana que aparece. Si se quieren
		// buscar textos, escribirlos abajo y hacer Ctrl+F.
		// Una vez que se sepa que se quiere hacer, programar este método de la manera adecuada:
//		procesaWeb( urlAAnalizar, new ProcesadoWeb() {
//			// TODO Atributos internos para el proceso
//			// Por ejemplo:
//			private String ultimoDato = "";
//			private String[] tagsBuscados = { "TD", "TR" };
//			private ArrayList<String> lTextos = new ArrayList<String>( Arrays.asList( new String[] { "Paradas", "Minutos jugados", "Tarjetas amarillas", "Tarjetas rojas", "Faltas cometidas", "Faltas recibidas" } ) );
//			@Override
//			public void procesaTexto(TextNode texto, LinkedList<Tag> pilaTags) {
//				// TODO programación del método (si procede)
//				// Por ejemplo:
//				if (pilaContieneTags( pilaTags, tagsBuscados )) {
//					if (lTextos.contains( texto.getText() )) {
//						ultimoDato = texto.getText();
//					} else {
//						if (!ultimoDato.isEmpty()) {
//							System.out.println( ultimoDato + " = " + texto.getText() );
//						}
//						ultimoDato = "";
//					}
//				}
//			}
//			@Override
//			public void procesaTag(Tag tag, LinkedList<Tag> pilaTags) {
//				// TODO programación del método (si procede)
//			}
//			@Override
//			public void procesaTagCierre( Tag tag, LinkedList<Tag> pilaTags, boolean enHtml ) {
//				// TODO programación del método (si procede)
//			}
//		});
		ProcesadoLaLiga pLaLiga = new ProcesadoLaLiga();
		
		// 3.- Una vez conseguida la programación, se puede quitar el punto 1 y dejar solo el punto 2 
		// con su recorrido de los datos.
		//
		//
		String urlLaLiga = "http://www.laliga.es/jugador/busquets/partidos";
		procesaWeb( urlLaLiga, pLaLiga );
		System.out.println( pLaLiga.getDescs() );
		System.out.println( pLaLiga.getCabs() );
		System.out.println( pLaLiga.getTabla() );
		urlLaLiga = "http://www.laliga.es/jugador/busquets/partidos/defensivas";
		procesaWeb( urlLaLiga, pLaLiga );
		System.out.println( pLaLiga.getDescs() );
		System.out.println( pLaLiga.getCabs() );
		System.out.println( pLaLiga.getTabla() );
		urlLaLiga = "http://www.laliga.es/jugador/busquets/partidos/disciplina";
		procesaWeb( urlLaLiga, pLaLiga );
		System.out.println( pLaLiga.getDescs() );
		System.out.println( pLaLiga.getCabs() );
		System.out.println( pLaLiga.getTabla() );
		urlLaLiga = "http://www.laliga.es/jugador/busquets/partidos/ofensivas";
		procesaWeb( urlLaLiga, pLaLiga );
		System.out.println( pLaLiga.getDescs() );
		System.out.println( pLaLiga.getCabs() );
		System.out.println( pLaLiga.getTabla() );
		urlLaLiga = "http://www.laliga.es/jugador/busquets/partidos/eficiencia";
		procesaWeb( urlLaLiga, pLaLiga );
		System.out.println( pLaLiga.getDescs() );
		System.out.println( pLaLiga.getCabs() );
		System.out.println( pLaLiga.getTabla() );
		
		
		// Otro ejemplo:
//		urlAAnalizar = "http://www.marca.com/estadisticas/futbol/primera/2016_17/alaves/";
//		urls = new ArrayList<>();
//		// revisaWeb( urlAAnalizar );
//		procesaWeb( urlAAnalizar, new ProcesadoWeb() {
//			boolean enZonaJugadores = false;
//			private String[] tagsBuscados1 = { "H4", "DIV" };
//			private String[] tagsBuscados2 = { "LI", "UL", "DIV" };
//			@Override
//			public void procesaTexto(TextNode texto, LinkedList<Tag> pilaTags) {
//				if (pilaContieneTags( pilaTags, tagsBuscados1 )) {
//					if ("Jugadores".equals(texto.getText())) {
//						enZonaJugadores = true;
//					} else if (texto.getText().startsWith("Últimos partidos disputados")) {
//						enZonaJugadores = false;
//					}
//				}
//			}
//			@Override
//			public void procesaTag(Tag tag, LinkedList<Tag> pilaTags) {
//				if (enZonaJugadores && pilaContieneTags( pilaTags, tagsBuscados2 )) {
//					if (tag.getTagName().equals("A")) {
//						System.out.println( tag.getAttribute( "href" ) );
//						urls.add( tag.getAttribute("href") );
//					}
//				}
//			}
//			@Override
//			public void procesaTagCierre( Tag tag, LinkedList<Tag> pilaTags, boolean enHtml ) {
//			}
//		});
//		procesaJugadores();
	}
	
		// Ejemplo: procesado de laliga.es
		private static class ProcesadoLaLiga implements ProcesadoWeb {
			private String[] tagsBuscados1 = { "TH", "TR", "THEAD", "TABLE" };
			private String[] tagsBuscados2 = { "TD", "TR", "TBODY", "TABLE" };
			private ArrayList<String> descs = new ArrayList<>();
			private ArrayList<String> cabs = new ArrayList<>();
			private ArrayList<String> vals = new ArrayList<>();
			private HashMap<String,ArrayList<String>> tabla = new HashMap<>();
			private boolean enTabla = false;
			private boolean haHabidoValores = false;
			private boolean sacadasCabeceras = false;
			private String lastTH = "";
			public ArrayList<String> getDescs() { return descs; }
			public ArrayList<String> getCabs() { return cabs; }
			public HashMap<String,ArrayList<String>> getTabla() { return tabla; }
			@Override
			public void procesaTexto(TextNode texto, LinkedList<Tag> pilaTags) {
				// Por ejemplo:
				if (pilaContieneTags( pilaTags, tagsBuscados1 )) {
					cabs.add( texto.getText() );  // Añade cabecera
					descs.add( lastTH );  // Añade descripción (th anterior)
					// System.out.println( texto.getText() );
				} else if (pilaContieneTags( pilaTags, tagsBuscados2 )) {
					vals.add( texto.getText() );
					// System.out.println( texto.getText() );
				}
			}
			@Override
			public void procesaTag(Tag tag, LinkedList<Tag> pilaTags) {
				// TODO programación del método (si procede)
				if (tag.getTagName().equals("TABLE") && tag.getText().contains("class='datatable'")) {
					// Empieza la tabla de datos
					enTabla = true;
				} else if (enTabla && tag.getTagName().equals("TR")) {
					// Marca líneas entre cabeceras y datos y entre líneas completas de datos
					// System.out.println( "Separación (TR)" );
					if (!sacadasCabeceras) {  // Cabecera - primera vez
						System.out.println( descs );
						System.out.println( cabs );
						sacadasCabeceras = true;
					} else if (!vals.isEmpty()) { // Datos - resto de veces
						System.out.println( vals );
						tabla.put( vals.get(1), vals );
						vals = new ArrayList<>();
						haHabidoValores = true;
					}
				} else if (tag.getTagName().equals("SECTION") && enTabla && haHabidoValores) {
					// Acaba la tabla de datos
					enTabla = false;
				} else if (tag.getTagName().equals("TH")) {
					// Guarda el title del último TH
					// Se usa para las descripciones (completas) de las cabeceras (resumidas)
					lastTH = tag.getAttribute( "title" );
				}
			}
			@Override
			public void procesaTagCierre( Tag tag, LinkedList<Tag> pilaTags, boolean enHtml ) {
			}
		}
	
	
		// Método de ejemplo
		private static ArrayList<String> urls;
		private static void procesaJugadores() {
			System.out.println();
			for (String url : urls) {
				System.out.println( url );
				procesaWeb( "http://www.marca.com" + url, new ProcesadoWeb() {
					private String ultimoDato = "";
					private String[] tagsBuscados = { "TD", "TR" };
					private ArrayList<String> lTextos = new ArrayList<String>( Arrays.asList( new String[] { 
							"Paradas", "Minutos jugados", "Tarjetas amarillas", "Tarjetas rojas", "Faltas cometidas", "Faltas recibidas",
							"Goles", "Tiros entre los tres palos", "Tiros a puerta", "Balones perdidos", "Balones recuperados", "Asistencias"
							} ) );
					@Override
					public void procesaTexto(TextNode texto, LinkedList<Tag> pilaTags) {
						if (pilaContieneTags( pilaTags, tagsBuscados )) {
							if (lTextos.contains( texto.getText() )) {
								ultimoDato = texto.getText();
							} else {
								if (!ultimoDato.isEmpty()) {
									System.out.println( "  " + ultimoDato + " = " + texto.getText() );
								}
								ultimoDato = "";
							}
						}
					}
					@Override
					public void procesaTag(Tag tag, LinkedList<Tag> pilaTags) {
					}
					@Override
					public void procesaTagCierre( Tag tag, LinkedList<Tag> pilaTags, boolean enHtml ) {
					}
				});
			}
		}

	//
	// Métodos de utilidad generales
	//

		private static LinkedList<Tag> pilaTags;
	/** Procesa una web y muestra en una ventana de consola coloreada sus contenidos etiquetados
	 * @param dirWeb
	 */
	public static void revisaWeb( String dirWeb ) {
		URL url;
		pilaTags = new LinkedList<>();
		try {
			ConnectionManager manager = Page.getConnectionManager();
			manager.getRequestProperties().put( "User-Agent", "Mozilla/4.0" );  // Hace pensar a la web que somos un navegador
			URLConnection connection = manager.openConnection( dirWeb );
			Lexer mLexer =  new Lexer( connection );
			Node n = mLexer.nextNode();
			while (n!=null) {
				if (n instanceof Tag) {
					Tag t = (Tag) n;
					if (t.isEndTag()) {
						if (pilaTags.get(0).getTagName().equals(t.getTagName())) {  // Tag de cierre
							pilaTags.pop();
							if (MOSTRAR_TODOS_LOS_TAGS) {
								VentanaColorConsola.println( String.format( "%" + (pilaTags.size()*2+1) + "s", "" ) +
										"</" + t.getTagName() + "> -> " + quitaCR( t.getText() ), Color.ORANGE );
							}
						} else {  // El tag que se cierra no es el último que se abrió: error html pero se procesa
							boolean estaEnPila = false;
							for (Tag tag : pilaTags) if (tag.getTagName().equals(t.getTagName())) estaEnPila = true;
							if (estaEnPila) {  // Ese tag está en la pila: quitar todos los niveles hasta él
								while (!pilaTags.get(0).getTagName().equals(t.getTagName())) pilaTags.pop();
								pilaTags.pop();
								if (MOSTRAR_TODOS_LOS_TAGS) {
									VentanaColorConsola.println( String.format( "%" + (pilaTags.size()*2+1) + "s", "" ) +
											"**PÉRDIDA DE TAGS ANIDADOS", Color.RED );
									VentanaColorConsola.println( String.format( "%" + (pilaTags.size()*2+1) + "s", "" ) +
											"</" + t.getTagName() + "> -> " + quitaCR( t.getText() ), Color.ORANGE );
								}
							} else { // El tag que se cierra no está en la pila
									VentanaColorConsola.println( 
											"**ERROR EN CIERRE DE TAG", Color.RED );
									VentanaColorConsola.println( String.format( "%" + (pilaTags.size()*2+1) + "s", "" ) +
											"</" + t.getTagName() + "> -> " + quitaCR( t.getText() ), Color.ORANGE );
							}
						}
					} else if (t.getText().endsWith("/")){  // Tag de cierre y apertura
						VentanaColorConsola.println( String.format( "%" + (pilaTags.size()*2+1) + "s", "" ) +
						"<" + t.getTagName() + "/> -> " + quitaCR( t.getText() ), Color.GREEN );
					} else {
						VentanaColorConsola.println( String.format( "%" + (pilaTags.size()*2+1) + "s", "" ) +
								"<" + t.getTagName() + "> -> " + quitaCR( t.getText() ), Color.BLUE );
						pilaTags.push( t );
					}
				} else {
					if (!quitaCR(n.getText()).trim().isEmpty()) {
						VentanaColorConsola.println( String.format( "%" + (pilaTags.size()*2+1) + "s", "" ) + 
							quitaCR( n.getText() ), Color.BLACK );
					}
				}
				n = mLexer.nextNode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pilaTags.clear();
	}
	
		private static String quitaCR( String s ) {
			return s.replaceAll( "\n", " " );
		}

	
	/** Procesa una web y lanza el método observador con cada uno de sus elementos
	 * @param dirWeb	Web que se procesa
	 * @param proc	Objeto observador que es llamado con cada elemento de la web
	 */
	public static void procesaWeb( String dirWeb, ProcesadoWeb proc ) {
		URL url;
		pilaTags = new LinkedList<>();
		try {
			ConnectionManager manager = Page.getConnectionManager();
			manager.getRequestProperties().put( "User-Agent", "Mozilla/4.0" );  // Hace pensar a la web que somos un navegador
			URLConnection connection = manager.openConnection( dirWeb );
			Lexer mLexer =  new Lexer( connection );
			Node n = mLexer.nextNode();
			while (n!=null) {
				if (n instanceof Tag) {
					Tag t = (Tag) n;
					if (t.isEndTag()) {
						if (pilaTags.get(0).getTagName().equals(t.getTagName())) {  // Tag de cierre
							pilaTags.pop();
							proc.procesaTagCierre( t, pilaTags, true );
						} else {  // El tag que se cierra no es el último que se abrió: error html pero se procesa
							boolean estaEnPila = false;
							for (Tag tag : pilaTags) if (tag.getTagName().equals(t.getTagName())) estaEnPila = true;
							if (estaEnPila) {  // Ese tag está en la pila: quitar todos los niveles hasta él
								while (!pilaTags.get(0).getTagName().equals(t.getTagName())) {
									Tag tag = pilaTags.pop();
									proc.procesaTagCierre( tag, pilaTags, false );
								}
								pilaTags.pop();
								proc.procesaTagCierre( t, pilaTags, true );
							} else { // El tag que se cierra no está en la pila
							}
						}
					} else if (t.getText().endsWith("/")){  // Tag de apertura y cierre
						proc.procesaTag( t, pilaTags );
						proc.procesaTagCierre( t, pilaTags, true );
					} else { // Tag de inicio
						proc.procesaTag( t, pilaTags );
						pilaTags.push( t );
					}
				} else {
					if (n instanceof TextNode) {
						proc.procesaTexto( (TextNode)n, pilaTags );
					} else {
						// Otros nodos como org.htmlparser.nodes.RemarkNode no se procesan
						// System.out.println( n.getClass().getName() );
						// System.out.println( n.getText() );
					}
				}
				n = mLexer.nextNode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		pilaTags.clear();
	}

	
	/** Interfaz de observador de procesado de web
	 * @author andoni.eguiluz @ ingenieria.deusto.es
	 */
	public static interface ProcesadoWeb {
		/** Método llamado cuando se procesa un tag de apertura html
		 * @param tag	Tag de apertura con toda la información incluida
		 * @param pilaTags	Pila actual de tags (previa a ese tag)
		 */
		void procesaTag( Tag tag, LinkedList<Tag> pilaTags );
		/** Método llamado cuando se procesa un tag de cierre
		 * @param tag	Tag de cierre
		 * @param pilaTags	Pila actual de tags (posterior a cerrar ese tag)
		 * @param enHtml	true si el tag de cierre es explícito HTML, false si es implícito en el fichero pero no está indicado
		 */
		void procesaTagCierre( Tag tag, LinkedList<Tag> pilaTags, boolean enHtml );
		/** Método llamado cuando se procesa un texto html
		 * @param texto	Texto html
		 * @param pilaTags	Pila actual de tags donde aparece ese texto
		 */
		void procesaTexto( TextNode texto, LinkedList<Tag> pilaTags );
	}
	
	/** Chequea si la pila de tags contiene los tags indicados en el mismo orden
	 * @param pilaTags	Pila de tags anidados (el primero es el más reciente)
	 * @param tags	Array de tags (solo nombres) buscados en la pila (el primero es el más interior que se busca)	
	 * @return	true si en la pila están los tags indicados en el mismo orden de anidamiento, false en caso contrario
	 */
	public static boolean pilaContieneTags( LinkedList<Tag> pilaTags, String... tags ) {
		LinkedList<String> pilaBuscada = new LinkedList<String>( Arrays.asList( tags ) );
		if (pilaBuscada.size()==0) return true;
		for (Tag tag : pilaTags) {
			if (tag.getTagName().equals( pilaBuscada.get(0) )) {
				pilaBuscada.pop();
				if (pilaBuscada.size()==0) return true;
			}
		}
		return false;
	}
	
	/** Convierte a string visualizable la pila de tags sacando en una línea solo los tags separados por barras
	 * @param pilaTags	Pila de tags anidados (el primero es el más reciente)
	 * @return	String de tags de la pila en el mismo orden
	 */
	public static String tagsDePila( LinkedList<Tag> pilaTags ) {
		String ret = "";
		for (Tag tag : pilaTags) ret += (tag.getTagName() + "|");
		return ret;
	}

}
