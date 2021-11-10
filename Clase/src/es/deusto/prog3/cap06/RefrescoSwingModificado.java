package es.deusto.prog3.cap06;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Clase para dibujar y calcular FPS cambiando la manera en la que hace el refresco Swing.
 */
public class RefrescoSwingModificado {

    private final Object bloqueoDibujo = new Object();
    private JPanel componente;
    private Image imageBuffer;
    // Variable de bucle principal de juego/animación
    private boolean sigue = true;
    // Variables de FPS
	private long milis = -1;
	private int numDibujados = 0;
	private final boolean MUESTRA_FPS = true;
	private long pausaEntreFrames = 0;

	/** Crea un objeto para hacer el refresco de Swing modificado para asegurar FPS
	 * @param fpsDeseados	Número de fps que se quiere conseguir, 0 si se buscan los máximos posibles, -1 si no se quiere modificar el tiempo de refresco estándar de Swing
	 */
	public RefrescoSwingModificado( int fpsDeseados ) {
		if (fpsDeseados > 0)
			pausaEntreFrames = 1000/fpsDeseados; // msg de espera entre dibujado y dibujado
		else
			pausaEntreFrames = fpsDeseados;
        componente = new PanelDeDibujo();
	}
	
	/** Devuelve el panel de dibujado que se refresca
	 * @return	Panel del objeto de refresco modificado
	 */
	public JPanel getPanelDibujado() {
		return componente;
	}
	
    /** Lanza la animación. Crea y lanza el hilo que recorre el bucle principal de animación/juego.
     * Debe llamarse al método #detener() para que el hilo se pare, si no seguirá ejecutándose aún después de detenerse el EDT.
     */
    public final void lanzar() {
        imageBuffer = componente.createImage(componente.getWidth(),componente.getHeight());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                bucleDeAnimacion();
            }
        });
        thread.start();
    }

    public void detener() {
        sigue = false;  // Para el bucle
        synchronized (bloqueoDibujo) {   
        	bloqueoDibujo.notifyAll();  // Desbloquea cualquier bloqueo de dibujado que pudiera estar pendiente
        }
    }

    /** Actualización del juego/animación de forma repetida
     * Llama a #dibujarFrame() en cada iteración, y espera un tiempo configurable hasta la siguiente iteración
     */
    public void bucleDeAnimacion() {
        // Actualizar la animación/juego de forma repetida
        while (sigue) {
            long msDeDibujo = dibujarFrame();
            if (pausaEntreFrames>0)
	            try { Thread.sleep(Math.max(0, pausaEntreFrames - msDeDibujo)); } catch (InterruptedException e) {}
        }
    }

    /** Dibujado de cada fotograma. Realiza cambios en el modelo #actualizarDatosModelo(), dibuja
     * el modelo a un buffer de imagen #dibujarModeloAImageBuffer(), y dibuja ese buffer en el panel de dibujado.
     * @return	Tiempo en milisegundos que ha llevado todo el proceso
     */
    public final long dibujarFrame() {
        long t = System.currentTimeMillis();
        // 1.- Realiza cambios en el modelo
        actualizarDatosModelo();
        // 2.- Dibuja el modelo de datos a un objeto BufferedImage
        dibujarModeloAImageBuffer();
        // 3.- Indica de forma asíncrona al EDT que haga un paint, que pintará la BufferedImage en el panel
        componente.repaint();
        // 4.- Espera con un bloqueo a que ocurra el dibujado por parte del EDT 
        // (Al hacer la espera, el EDT se lanza casi inmediatamente)
        // (Si no se hace la espera -pausa<0- el EDT hará TODOS los repaint(), pero probablemente hará varios de repente
        //  y no tiene por qué haber sincronía entre el cambio en pantalla y el redibujado)
        if (pausaEntreFrames>=0) esperaAlDibujado();
        // Devuelve tiempo empleado en dibujado del frame
        return System.currentTimeMillis() - t;
    }

    	// Datos de ejemplo
	    private double x = 50;
	    private double y = 50;
	    private double incX = +0.3;  // Pixels/msg
	    private double incY = +0.3;
	    private long antTiempo = System.currentTimeMillis();
    private void actualizarDatosModelo() {
        // TODO: Cambio de elementos de la animación/juego, basado en el tiempo (ejemplo: coords x e y que rebotan)
    	// Cambio de elementos de ejemplo:
        x += incX*(System.currentTimeMillis()-antTiempo); y += incY*(System.currentTimeMillis()-antTiempo);
        if (x>componente.getWidth()-30 || x<0) { x -= incX; incX = -incX; }
        if (y>componente.getHeight()-5 || y<0) { y -= incY; incY = -incY; }
        antTiempo = System.currentTimeMillis();
    }

    // Dibuja en pantalla (objeto Graphics) el modelo de objetos del juego/animación (ejemplo: un texto en las coordenadas)
    private void drawModel(Graphics g) {
        g.setColor( componente.getBackground() );
        g.fillRect( 0, 0, componente.getWidth(), componente.getHeight() );
        g.setColor( componente.getForeground() );
        // TODO: Dibujado de los elementos de la animación/juego
        // Dibujado de ejemplo:
        g.drawString("Voooyyy!", (int)x, (int)y);
    }

    private void dibujarModeloAImageBuffer() {
        drawModel(imageBuffer.getGraphics());
    }

    // Se bloquea hasta que ocurra el dibujado
    // Lo debe llamar el hilo de dibujado (bucle de juego o animación, por ejemplo)
    private void esperaAlDibujado() {
        try {
            synchronized (bloqueoDibujo) { bloqueoDibujo.wait(100); }
        } catch (InterruptedException e) {}
    }

    // Libera el bloqueo para seguir dibujando
    // Lo debe llamar el EDT (Event Dispatch Thread)
    private void resume() {
        if (pausaEntreFrames>=0)
	        synchronized (bloqueoDibujo) { bloqueoDibujo.notify(); }
    }

    private void dibuja(Graphics g) {
        // dibuja la imagen a pantalla (Objeto Graphics)
        g.drawImage(imageBuffer, 0, 0, componente);
        // Notifica que ya se ha pintado el dibujo
        if (pausaEntreFrames>=0) resume();
        if (MUESTRA_FPS) {  // Calcular FPS y sacarlo en pantalla
	    	if (milis<0) milis = System.currentTimeMillis();  // Tiempo de primer dibujado
	        g.drawString( String.format( "FPS: %1$,1.1f",
	        		1000.0*numDibujados/(System.currentTimeMillis()-milis) )
	        	, 0, componente.getHeight()-2 );
        }
    }

    /** Clase interna que redefine un panel que simplemente redibuja un componente con refresco modificado
     */
    @SuppressWarnings("serial")
	public class PanelDeDibujo extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            RefrescoSwingModificado.this.dibuja( g );
	        numDibujados++;  // Conteo de dibujos
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	RefrescoSwingModificado animacion = new RefrescoSwingModificado(50);  // Probar a cambiar este valor -1, 0, 25, 50
            		// El FPS mide el número de repaints que se ejecutan... pero no son regulares y afecta a la calidad de vista

                JFrame frame = new JFrame();
                frame.setTitle("Prueba FPS modificando dibujado Swing");
                frame.setSize(800, 600);
                frame.setLayout(new BorderLayout());
                frame.getContentPane().add(animacion.getPanelDibujado(), BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.addWindowListener( new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						animacion.detener();  // Para el hilo de animación
					}
				});
                frame.setVisible(true);

                animacion.lanzar();
            }
        });
    }

}
