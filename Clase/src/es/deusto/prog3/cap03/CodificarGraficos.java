package es.deusto.prog3.cap03;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;

/** Clase que muestra cómo codificar gráficos en texto.<br/>
 * Una manera alternativa de guardar elementos binarios
 * como gráficos en modo textual. Hay maneras más eficientes 
 * de hacerlo, se implementa simplemente usando hexadecimal
 * (2 bytes por cada byte binario).<br/>
 * La codificación se puede transmitir, guardar en base de datos...
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class CodificarGraficos {

	public static void main(String[] args) {
		/* test con bytearray de 0 a 255
		byte[] b = new byte [256];
		for (int i=0; i<256; i++) b[i] = (byte) i;
		String s = byteArrayAHexString( b );
		System.out.println( s );
		b = hexStringAByteArray( s );
		for (byte b1 : b) System.out.print( b1 + " " );
		System.out.println();
		System.exit(0);
		*/
		try {
			String s1 = ficheroBinarioAStringHex( CodificarGraficos.class.getResource( "surfingmouse.png") );
			String s2 = graficoDeFicheroAStringHex( CodificarGraficos.class.getResourceAsStream( "surfingmouse.png") );
			int ancho = getAnchuraUltimoGrafico();
			int alto = getAlturaUltimoGrafico();
			boolean conAlfa = getTieneTransparencia();
			System.out.println( "Tamaño: " + ancho + "," + alto );
			File file = stringHexAFichero( s1, "temporal.png" );
			BufferedImage bi = stringHexAImagen( s2, ancho, alto, conAlfa );
			JFrame f = new JFrame( "Ejemplo de gráficos codificados en texto" );
			f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			JLabel l1 = new JLabel();
			JLabel l2 = new JLabel();
			JLabel lInfo = new JLabel( " " );
			f.add( l1, BorderLayout.WEST );
			f.add( l2, BorderLayout.EAST );
			f.add( lInfo, BorderLayout.SOUTH );
			lInfo.setText( "Izquierda: png (" + s1.length() + " cars). Derecha: bitmap (" + s2.length() + " cars).");
			l1.setIcon( new ImageIcon( file.toURI().toURL() ) );
			l2.setIcon( new ImageIcon( bi ) );
			f.pack();
			f.setLocationRelativeTo( null );
			f.setVisible( true );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Convierte un fichero binario en un string codificado en hexadecimal
	 * @param is	URL de fichero correcto con permiso de la lectura
	 * @return	String correspondiente a esos datos en hexadecimal (null si hay error en la lectura)
	 */
	public static String ficheroBinarioAStringHex( URL url ) {
		try {
			InputStream is = url.openStream();
			int longi = is.available();
			byte[] buffer = new byte[ longi ];
			is.read( buffer );
			return byteArrayAHexString( buffer );
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Crea un fichero binario partiendo de un string codificado en hexadecimal
	 * @param hex	String en hexadecimal (dos caracteres por cada byte)
	 * @param fileName	Nombre de fichero a crear
	 * @return	fichero creado, null si hay algún error
	 */
	public static File stringHexAFichero( String hex, String fileName ) {
		try {
			File f = new File( fileName );
			FileOutputStream fos = new FileOutputStream( f );
			fos.write( stringHexAByteArray( hex ) );
			fos.close();
			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
		private static int ultimaAnchura = -1;
		private static int ultimaAltura = -1;
		private static boolean ultimoAlfa = false;
	/** Convierte un gráfico en un string codificado en hexadecimal
	 * @param is	Stream de entrada de fichero gráfico correcto y ya configurado para la lectura
	 * @return	String correspondiente a esos datos en hexadecimal (null si hay error en la lectura)
	 */
	public static String graficoDeFicheroAStringHex( InputStream is ) {
		ultimaAnchura = -1;
		ultimaAltura = -1;
		try {
			BufferedImage bufferedImage = ImageIO.read( is );
			ultimaAnchura = bufferedImage.getWidth();
			ultimaAltura = bufferedImage.getHeight();
			ultimoAlfa = bufferedImage.getColorModel().hasAlpha();
			WritableRaster raster = bufferedImage.getRaster();
			DataBufferByte data  = (DataBufferByte) raster.getDataBuffer();
			return byteArrayAHexString( data.getData() );
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Devuelve la anchura del gráfico convertido con la última llamada a {@link #graficoDeFicheroAStringHex(InputStream)}
	 * @return anchura en píxels de ese gráfico, -1 si ha habido error
	 */
	public static int getAnchuraUltimoGrafico() {
		return ultimaAnchura;
	}
	/** Devuelve la altura del gráfico convertido con la última llamada a {@link #graficoDeFicheroAStringHex(InputStream)}
	 * @return altura en píxels de ese gráfico, -1 si ha habido error
	 */
	public static int getAlturaUltimoGrafico() {
		return ultimaAltura;
	}
	/** Devuelve la información de si hay canal alfa (transparencia) en el gráfico convertido con la última llamada a {@link #graficoDeFicheroAStringHex(InputStream)}
	 * @return true si esa imagen tiene transparencia, false si no
	 */
	public static boolean getTieneTransparencia() {
		return ultimoAlfa;
	}

	
	/** Crea una imagen partiendo de un string codificado en hexadecimal
	 * @param hex	String en hexadecimal (dos caracteres por cada byte)
	 * @param ancho	Anchura en píxels de la imagen codificada
	 * @param alto	Altura en píxels de la imagen codificada
	 * @param transp	true si la imagen tiene transparencia (canal alfa), false en caso contrario
	 * @return	imagen creada, null si hay algún error
	 */
	public static BufferedImage stringHexAImagen( String hex, int ancho, int alto, boolean transp ) {
		try {
			byte[] buffer = stringHexAByteArray( hex );
			BufferedImage img = new BufferedImage( ancho, alto, transp ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR );
			img.setData( Raster.createRaster( img.getSampleModel(), new DataBufferByte(buffer, buffer.length), new Point() ) );
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Convierte un array de bytes en un string hexadecimal
	 * @param bytes	Array de bytes codificados en binario
	 * @return	String de ese array en formato hexadecimal (dos caracteres por cada byte)
	 */
	public static String byteArrayAHexString(byte[] bytes) {
		StringBuffer text = new StringBuffer( bytes.length*2 );
		for (byte b : bytes) {
			text.append( String.format( "%02X", b ) );
		}
	    return text.toString();
	}	
	
	/** Convierte un string hexadecimal en un array de bytes
	 * @param hex	String de valores binarios en formato hexadecimal contiguos (dos caracteres por cada byte)
	 * @return	Array de bytes correspondiente al string hex
	 */
	public static byte[] stringHexAByteArray(String hex) {
		byte[] ret = new byte[ hex.length() / 2 ];
		for (int i=0; i<hex.length(); i+=2) {
			ret[i/2] = (byte) Integer.parseInt( hex.substring( i, i+2 ), 16 );
		}
		return ret;
	}
}
