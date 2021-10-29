package es.deusto.prog3.cap03.ejemploPartidas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

// dtd de logs  copiado en  http://docs.oracle.com/javase/7/docs/technotes/guides/logging/overview.html
// El formato del logger deja un único registro log que contiene n registros "record". Cada uno de ellos 
// tiene los siguientes campos (los datos de cada log):
// "date", "millis", "sequence", "logger", "level", "class", "method", "thread", "message"
// Los más importantes son:
// - millis    - Fecha-hora exacta del log en el formato millis de Java (msgs. transcurridos desde 1/1/1970)
// - level     - Importancia del error de acuerdo a la clase Level: FINEST, FINER, FINE, CONFIG, INFO, WARNING, SEVERE
// - class     - Clase que causa el log
// - method    - Método que causa el log
// - message   - Mensaje de log
// - exception - (solo a veces) Excepción volcada al log. Es un registro que a su vez genera los siguientes campos:
//   - message   - Cada mensaje de excepción
//   - frame     - un registro por cada llamada de la pila de llamadas en esa excepción
//     - class     - Clase que provoca la llamada
//     - method    - Método que provoca la llamada
//     - line      - Línea del fichero fuente dentro de ese método donde está la llamada

// Usada librería SAX que permite leer XML de una forma sencilla
// Si se quisieran también generar se puede usar en su lugar StAX. Ver referencia en:
// https://docs.oracle.com/javase/tutorial/jaxp/stax/why.html

public class EjemploProcesoLog {

	// TODO ATENCIÓN: Copiar el logger.dtd que está en este paquete en la ruta del fichero que se quiere analizar (ver main)
	private static String FICHERO_A_ANALIZAR = "bd.log.xml";
	
		private static boolean enExcepcion = false;
	public static void parseXMLSAX( String xmlFile ) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				String campoEnCurso = null;
				public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
					// System.out.println("Inicio de elemento:" + qName);
					campoEnCurso = qName;
					if (qName.equals("exception")) enExcepcion = true;
				}
				public void characters(char ch[], int start, int length) throws SAXException {
					String valor = new String(ch, start, length);
					// System.out.println("Valor de elemento:" + valor);
					// Visualizar
					System.out.print( campoEnCurso+"="+valor + "\t" );
					if (enExcepcion)
						procesoExcepcion( campoEnCurso, valor );
					else
						proceso( campoEnCurso, valor );
				}
				public void endElement(String uri, String localName, String qName) throws SAXException {
					// System.out.println("Fin de elemento:" + qName);
					if (qName.equals("record")) System.out.println();
					if (qName.equals("exception")) enExcepcion = false;
				}
			};
			MultipleXMLFileInputStream miXmlFile = new MultipleXMLFileInputStream(xmlFile);
			do {
				try {
					saxParser.parse( miXmlFile, handler );
				} catch (SAXParseException e) {
					System.out.println( "   Error de parsing XML en línea " + e.getLineNumber() + " - " + e.getMessage() );
				}
			} while (miXmlFile.tieneMasLogs());
			miXmlFile.fullClose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Proceso particular de log
		private static int numLogs = 0;
		private static int numSevere = 0;
		private static ArrayList<String> excepciones = new ArrayList<String>();
		private static ArrayList<ArrayList<String>> llamadasExcepciones = new ArrayList<>();
	private static void proceso( String campo, String valor ) {
		numLogs++;
		if (campo.equals("level") && valor.equals("SEVERE")) numSevere++;
	}
	
	// Proceso particular de excepción
		private static String llamadaEnProceso = "";
	private static void procesoExcepcion( String campo, String valor ) {
		switch (campo) {
			case "message" : {
				excepciones.add( valor );
				llamadasExcepciones.add( new ArrayList<String>() );
				break;
			}
			case "class" : {
				llamadaEnProceso = valor;
				break;
			}
			case "method" : {
				llamadaEnProceso += ("." + valor);
				break;
			}
			case "line" : {
				llamadasExcepciones.get( llamadasExcepciones.size()-1 ).add( llamadaEnProceso + " (" + valor + ")" );
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		// parseXMLSAX( "bdtest.log.xml" );
		parseXMLSAX( FICHERO_A_ANALIZAR );
		System.out.println( "Logs: " + numLogs + " --> severos: " + numSevere );
		System.out.println( "Excepciones:");
		for (int i=0; i<excepciones.size(); i++) {
			System.out.println( "  " + excepciones.get(i) );
			for (String s : llamadasExcepciones.get(i)) {
				System.out.println( "    " + s );
			}
		}
	}

	
	// Clase privada para gestionar un fichero que puede tener varios XML concatenados en el mismo fichero
	// Se considera el string "<?xml version="" al principio de la línea como iniciador de un nuevo fichero secundario
	// El método close() no cierra el fichero, solo lo preparar para leer el siguiente fichero secundario
	// El método tieneMasLogs() determina si hay o no más ficheros secundarios posteriormente
	// El método fullClose() cierra definitivamente el fichero
	private static class MultipleXMLFileInputStream extends FileInputStream {
		private int TAM_BUFFER = 8192;
		private byte[] buffer = new byte[TAM_BUFFER];
		private int offsetBuffer = 0;
		private int tamBuffer = -1;
		private int offsetMarca = -1;
		private static String marcaInicio = "<?xml version=\"";
		private static int[] marcaInt;
		private boolean reinicio = true;
		static {
			marcaInt = new int[ marcaInicio.length() ];
			for (int i=0; i<marcaInicio.length(); i++) marcaInt[i] = marcaInicio.charAt(i);
		}
		public MultipleXMLFileInputStream( String s ) throws FileNotFoundException { super(s); }
		@Override
		public int read() throws IOException {
			if (tamBuffer==-1 || offsetBuffer>=tamBuffer) leeSiguiente();
			else if (offsetMarca!=-1 && !reinicio && offsetBuffer==offsetMarca) leeSiguiente();
			if (tamBuffer==-1 || offsetBuffer>=tamBuffer) return -1;  // EOF
			if (vieneMarcaXML()) return -1;  // Marca XML nuevo
			offsetBuffer++;
			// System.out.println( "read() - " + buffer[offsetBuffer-1] + "   " + ((char)buffer[offsetBuffer-1]));
			reinicioMarca();
			return (int) buffer[offsetBuffer-1];
		}
		@Override
		public int read(byte[] b) throws IOException {
			if (tamBuffer==-1 || offsetBuffer>=tamBuffer) leeSiguiente();
			else if (offsetMarca!=-1 && !reinicio && offsetBuffer==offsetMarca) leeSiguiente();
			if (tamBuffer==-1 || offsetBuffer>=tamBuffer) return -1;  // EOF
			if (vieneMarcaXML()) return -1;  // Marca XML nuevo
			int bytes = tamBuffer-offsetBuffer;
			if (offsetMarca > offsetBuffer) {  // Y si viene marca en medio
				bytes = offsetMarca - offsetBuffer;
			}
			if (bytes <= b.length) {  
				for (int i=offsetBuffer; i<bytes; i++) b[i-offsetBuffer] = buffer[i];
				offsetBuffer += bytes;
			} else {
				for (int i=0; i<b.length; i++) b[i] = buffer[i+offsetBuffer];
				offsetBuffer += b.length;
				bytes = b.length;
			}
			// System.out.println( "read(byte[]) " + bytes );
			// for (int i=0;i<bytes;i++) System.out.print( (char) (b[i]) );
			// System.out.println();
			reinicioMarca();
			return bytes;
		}
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if (tamBuffer==-1 || offsetBuffer>=tamBuffer) leeSiguiente();
			else if (offsetMarca!=-1 && !reinicio && offsetBuffer==offsetMarca) leeSiguiente();
			if (tamBuffer==-1 || offsetBuffer>=tamBuffer) return -1;  // EOF
			if (vieneMarcaXML()) return -1;  // Marca XML nuevo
			int bytes = tamBuffer-offsetBuffer;
			if (offsetMarca > offsetBuffer) {  // Y si viene marca en medio
				bytes = offsetMarca - offsetBuffer;
			}
			if (bytes <= len) {  
				for (int i=0; i<bytes; i++) b[off+i] = buffer[i+offsetBuffer];
				offsetBuffer += bytes;
			} else {
				for (int i=0; i<len; i++) b[off+i] = buffer[i+offsetBuffer];
				offsetBuffer += len;
				bytes = len;
			}
			// System.out.println( "read(byte[],off,len) " + len + "," + off + "," + len + " -> " + bytes + "   [Int " + offsetBuffer + " m" + offsetMarca + " de " + tamBuffer + "]" );
			// for (int i=0;i<bytes;i++) if ((char)(b[off+i])=='\n') System.out.print("#"); else System.out.print( (char) (b[off+i]) );
			// System.out.println();
			reinicioMarca();
			return bytes;
		}
		@Override
		public void close() throws IOException {
			reinicio = true;
			// System.out.println( "close()" );
			// super.close();
		}
		@Override
		public int available() throws IOException {
			return super.available();
		}
		private boolean vieneMarcaXML() {
			return (!reinicio && offsetMarca!=-1 && offsetMarca==offsetBuffer && offsetMarca+marcaInt.length<=tamBuffer);
		}
		private void leeSiguiente() throws IOException {
			if (offsetMarca==-1) {
				// System.out.println( "Lectura sin marca");
				tamBuffer = super.read( buffer, 0, TAM_BUFFER );
				offsetBuffer = 0;
			} else {
				int parteMarca = tamBuffer - offsetMarca;
				// System.out.println( "Lectura con marca en " + offsetMarca + " dejando " + parteMarca + " y leyendo " + (TAM_BUFFER - parteMarca) + " bytes");
				for (int i=0; i<parteMarca; i++) buffer[i] = buffer[i+offsetMarca];
				tamBuffer = super.read( buffer, parteMarca, TAM_BUFFER - parteMarca ) + parteMarca;
				offsetBuffer = 0;
			}
			buscaMarca();
			// System.out.print( "  BUFFER:" );
			// for (int i=0;i<tamBuffer;i++) if ((char)(buffer[i])=='\n') System.out.print("#"); else System.out.print( (char) (buffer[i]) );
			// System.out.println();
		}
		private void buscaMarca() {
			offsetMarca = -1;
			int offsetEnMarca = 0;
			for (int i=offsetBuffer; i<tamBuffer; i++) {
				if (buffer[i]==marcaInt[offsetEnMarca]) {  // Estamos encontrando la marca
					if (offsetEnMarca==0) offsetMarca = i;
					offsetEnMarca++;
				} else {
					offsetMarca = -1;
					offsetEnMarca = 0;
				}
				if (offsetEnMarca==marcaInt.length) break;  // Encontrada marca!!
			}
			// if (offsetMarca!=-1) System.out.println( "Encontrada marca! " + offsetMarca );
		}
		private void reinicioMarca() {
			if (reinicio) buscaMarca();
			reinicio = false;
		}
		public boolean tieneMasLogs() {
			return offsetBuffer<tamBuffer;
		}
		public void fullClose() throws IOException {
			super.close();
			tamBuffer=0;
			offsetBuffer=0;
			offsetMarca=-1;
		}
	}

	
}
