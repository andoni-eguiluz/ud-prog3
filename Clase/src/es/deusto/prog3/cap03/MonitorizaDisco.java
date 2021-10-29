package es.deusto.prog3.cap03;

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.io.*;
import java.util.*;
 
public class MonitorizaDisco {
 
    private WatchService monitorizador;
    private Map<WatchKey,Path> carpetasMonitorizadas;
    private boolean recursivo;
    private boolean sacaInfoDepuracion = false;  // a true para sacar información de qué carpetas se están registrando
 
    /**
     * Registra el directorio dado con el servicio estándar de monitorización
     * de java.nio - WatchService
     */
    private void registra(Path dir) throws IOException {
    	// Registrar este directorio para eventos de creación, borrado o modificación:
        WatchKey key = dir.register(monitorizador, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (sacaInfoDepuracion) {
            Path yaEstaba = carpetasMonitorizadas.get( key );
            if (yaEstaba == null) {
                System.out.format("registro de: %s\n", dir);
            } else if (!dir.equals(yaEstaba)) {
            	System.out.format("actualización de: %s -> %s\n", yaEstaba, dir);
            }
        }
        carpetasMonitorizadas.put( key, dir );
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registraArbol(final Path start) throws IOException {
        // Registra carpeta y subcarpetas con el método de recorrido:
        Files.walkFileTree( start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registra(dir);
                return FileVisitResult.CONTINUE;  // Continúa el recorrido
            }
        });
    }
 
    /**
     * Construye un servicio de monitorización sobre el directorio indicado
     * @param dir	Carpeta a monitorizar
     * @param recursivo	Indicación de si también se observan las subcarpetas (true) o no (false)
     * @throws IOException
     */
    MonitorizaDisco(Path dir, boolean recursivo) throws IOException {
    	// Crea servicio (java.nio.FileSystems -> Java 7)
        this.monitorizador = FileSystems.getDefault().newWatchService();
        this.carpetasMonitorizadas = new HashMap<WatchKey,Path>();
        this.recursivo = recursivo;
        if (recursivo) {
            System.out.format( "Recorriendo %s ...\n", dir );
            registraArbol(dir);
            System.out.println( "Fin." );
        } else {
            registra(dir);
        }
 
        // enable trace after initial registration
        this.sacaInfoDepuracion = true;
    }
 
    /**
     * Procesa todos los eventos asociados con el monitorizador:
     * El hilo que llama a este método se queda ejecutando de forma continua
     * permanentemente observando los cambios que ocurren en las carpetas monitorizadas
     */
    void procesaEventos() {
        while (true) {
            // Espera al próximo evento de monitorización:
            WatchKey key;
            try {
                key = monitorizador.take();  // Toma evento (y espera si no lo hay hasta que lo haya)
            } catch (InterruptedException x) {
                return;
            }
            Path dir = carpetasMonitorizadas.get( key );
            if (dir == null) {
                System.err.println("Cambio no reconocido");
                continue;
            }
            // Procesar los eventos monitorizados
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                // El contexto para el evento de directorio es el nombre de fichero de la entrada
                	@SuppressWarnings("unchecked")  // No sacar el warning del cast siguiente
				WatchEvent<Path> evento = (WatchEvent<Path>)event;
                Path nombreFicConCambio = evento.context();
                Path pathConCambio = dir.resolve(nombreFicConCambio);
                // Saca el evento por consola
                System.out.format("%s: %s\n", event.kind().name(), pathConCambio);
                // Si se crea una carpeta, y se ha marcado la monitorización como recursiva,
                // registrar esa carpeta y sus subcarpetas
                // (puede haberse creado por una copia y venir ya con contenido)
                if (recursivo && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(pathConCambio, NOFOLLOW_LINKS)) {
                            registraArbol(pathConCambio);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }
 
            // reinicia la clave del evento y la quita si el directorio ya no es accesible
            boolean valid = key.reset();
            if (!valid) {
                carpetasMonitorizadas.remove(key);
                if (carpetasMonitorizadas.isEmpty()) {
                    // no hay ninguna carpeta accesible (se han borrado todas)
                    break;
                }
            }
        }
    }
 
    static void muestraUso() {
        System.err.println("Uso: java MonitorizaDisco [-r] dir");
        System.exit(-1);
    }
 
    public static void main(String[] args) {
        // Poner directorio a mano (comentar para cogerlo de la línea de comandos)
    	// args = new String[] { "-r", "d:\\\\t" };
    	args = new String[] { "D:/t/temp" };
        if (args.length == 0 || args.length > 2) muestraUso();
        boolean recursivo = false;
        int paramCarpeta = 0;
        if (args[0].equals("-r")) {
            if (args.length < 2)
                muestraUso();
            recursivo = true;
            paramCarpeta++;
        }
        // registra la carpeta y procesa sus eventos
        Path dir = Paths.get(args[paramCarpeta]);
        try {
	        MonitorizaDisco md = new MonitorizaDisco(dir, recursivo);
	        md.procesaEventos();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
}
