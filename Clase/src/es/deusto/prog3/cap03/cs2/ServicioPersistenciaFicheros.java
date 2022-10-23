package es.deusto.prog3.cap03.cs2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

/** Gestor de persistencia basado en memoria (mapa de productos) y guardado en fichero binario
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class ServicioPersistenciaFicheros implements ServicioPersistenciaProductos {
	
	private Logger logger;
	private TreeMap<Integer,Producto> mapaProductos;
	private String nombreFichero;

	/** Crea un servicio de persistencia basado en memoria y guardado en fichero local
	 */
	public ServicioPersistenciaFicheros() {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(String nombrePersistencia, String configPersistencia) {
		mapaProductos = new TreeMap<Integer,Producto>();
		this.nombreFichero = nombrePersistencia;
		File fic = new File( nombreFichero );
		if (!fic.exists()) {
			return true;  // Se crea el sistema de persistencia pero no hay datos previos - se inicia correctamente aunque vacío
		}
		try {
			ObjectInputStream ois = new ObjectInputStream( new FileInputStream( fic ) );
			mapaProductos = (TreeMap<Integer,Producto>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			log( Level.SEVERE, "No se ha podido cargar el fichero de persistencia " + nombrePersistencia, e );
			return false;
		}
		return true;
	}

	@Override
	public boolean initDatosTest(String nombrePersistencia, String configPersistencia) {
		this.nombreFichero = nombrePersistencia;
		mapaProductos = new TreeMap<Integer,Producto>();
		mapaProductos.put( 1, new Producto( 1, "HTC Vive", 304.12, "fotosInit/htc-vive.jpg" ) );
		mapaProductos.put( 2, new Producto( 2, "Meta Quest 2", 449.99, "fotosInit/meta-quest-2.jpg" ) );
		mapaProductos.put( 3, new Producto( 3, "Meta Quest Pro", 1799.99, "fotosInit/meta-quest-pro.jpg" ) );
		return true;
	}

	@Override
	public void close() {
		try {
			File fic = new File( nombreFichero );
			ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( fic ) );
			oos.writeObject( mapaProductos );
			oos.close();
		} catch (Exception e) {
			log( Level.SEVERE, "No se ha podido guardar el fichero de persistencia " + nombreFichero, e );
		}
	}

	public int getNumeroProductos() {
		return mapaProductos.size();
	}
	
	@Override
	public Producto buscarCodigo(int codigo) {
		return mapaProductos.get( codigo );
	}

	@Override
	public Producto buscarNombre(String nombre) {
		for (Producto p : mapaProductos.values()) {
			if (nombre.equals(p.getNombre())) {
				return p;
			}
		}
		return null;
	}

	@Override
	public List<Producto> buscarTodos() {
		List<Producto> ret = new ArrayList<Producto>();
		for (Producto p : mapaProductos.values()) {
			ret.add( p );
		}
		return ret;
	}

	@Override
	public List<Producto> buscarParteNombre(String parteNombre) {
		if (parteNombre.isEmpty()) {
			return buscarTodos();
		}
		parteNombre = parteNombre.toUpperCase();
		List<Producto> ret = new ArrayList<Producto>();
		for (Producto p : mapaProductos.values()) {
			if (p.getNombre().toUpperCase().contains(parteNombre)) {
				ret.add( p );
			}
		}
		return ret;
	}

	// No se considera la imagen en almacenamiento local
	@Override
	public boolean insertar(Producto producto, ImageIcon imagen ) {
		if (mapaProductos.containsKey(producto.getCodigo())) {
			log( Level.WARNING, "Inserción de producto incorrecta: " + producto, null );
			return false;
		}
		mapaProductos.put( producto.getCodigo(), producto );
		actualizaFotoSiProcede( producto );
		log( Level.INFO, "Inserción de producto correcta: " + producto, null );
		return true;
	}
	
		private void actualizaFotoSiProcede( Producto producto ) {
			if (!producto.getRutaFoto().startsWith("fotosProductos/")) {
				// Si la foto está en otra ruta, se copia a la ruta de todas las fotos
				File fOrigen = new File( producto.getRutaFoto() );
				if (!fOrigen.exists()) {
					log( Level.SEVERE, "Fichero " + producto.getRutaFoto() + " no encontrado al intentar guardar producto " + producto, null );
					return;
				}
				File carpeta = new File( "fotosProductos" );
				try {
					Files.createDirectory( carpeta.toPath() );
					String nuevoPath = "fotosProductos/" + fOrigen.getName();
					Files.copy( fOrigen.toPath(), (new File( nuevoPath )).toPath() );
					producto.setRutaFoto( nuevoPath );
				} catch (IOException e) {
					log( Level.SEVERE, "Fichero " + producto.getRutaFoto() + " no se ha podido respaldar en el servicio de persistencia para el producto " + producto, e );
				}
			}
		}

	// No se considera la imagen en almacenamiento local
	@Override
	public boolean actualizar(Producto producto, ImageIcon imagen ) {
		if (!mapaProductos.containsKey(producto.getCodigo())) {
			log( Level.WARNING, "Actualización de producto incorrecta: " + producto, null );
			return false;
		}
		mapaProductos.put( producto.getCodigo(), producto );
		actualizaFotoSiProcede( producto );
		log( Level.INFO, "Actualización de producto correcta: " + producto, null );
		return true;
	}

	@Override
	public ImageIcon cargaImagen(Producto producto) {
		File fic = new File( producto.getRutaFoto() );
		if (!fic.exists()) {  // Si no se encuentra como fichero se busca como recurso
			URL url = ServicioPersistenciaFicheros.class.getResource( producto.getRutaFoto() );
			if (url==null) {
				log( Level.WARNING, "Error en carga de imagen " + producto + ": " + producto.getRutaFoto(), null );
				return null;
			} else {
				return new ImageIcon( url );
			}
		} else {
			return new ImageIcon( fic.getAbsolutePath() );
		}
	}

	@Override
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	// Método local para loggear (si no se asigna un logger externo, se asigna uno local)
	private void log( Level level, String msg, Throwable excepcion ) {
		if (logger==null) {  // Logger por defecto local:
			logger = Logger.getLogger( "persistencia-ficheros" );  // Nombre del logger
			logger.setLevel( Level.ALL );  // Loguea todos los niveles
			try {
				logger.addHandler( new FileHandler( "persistencia-ficheros-log.xml", true ) );  // Y saca el log a fichero xml
			} catch (Exception e) {
				logger.log( Level.SEVERE, "No se pudo crear fichero de log", e );
			}
		}
		if (excepcion==null)
			logger.log( level, msg );
		else
			logger.log( level, msg, excepcion );
	}
	

}
