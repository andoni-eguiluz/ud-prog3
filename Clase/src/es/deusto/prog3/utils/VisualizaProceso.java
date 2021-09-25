package es.deusto.prog3.utils;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

/** Clase de utilidad para visualizar procesos de programación
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class VisualizaProceso {

	private Ventana miVentana = null;
	private Runnable repintar = null;
	private Runnable parada = null;
	private long tiempoPausa = 100;
	private int running = 0;  // 0 = pausa  1 = simulación  2 = paso a paso
	private boolean sigoViva = true;

	@SuppressWarnings("serial")
	private class Ventana extends JFrame {
		JTextField tfTiempoPausas;
		JButton bSimular, bStop, bPlay;
		JPanel pPrincipal;
		JLabel lMensaje;
		JPanel pSupIzq;
		public Ventana() {
			// Inicialización contenedores
			pSupIzq = new JPanel();
			JPanel pSupPlay = new JPanel();
			JPanel pSupCent = new JPanel(); pSupCent.setLayout( new BoxLayout( pSupCent, BoxLayout.Y_AXIS ) );
			JPanel pSupDer = new JPanel(); pSupDer.setLayout( new BoxLayout( pSupDer, BoxLayout.Y_AXIS ) );
			JPanel pSup = new JPanel(); pSup.setLayout( new BorderLayout() );
			JPanel pLineaCent = new JPanel( new FlowLayout(FlowLayout.CENTER) );
			JPanel pLineaDer = new JPanel( new FlowLayout(FlowLayout.LEFT) );
			// Inicialización componentes
				pLineaCent.add( new JLabel( "T.pausa" ) );
				tfTiempoPausas = new JTextField( "100" ); tfTiempoPausas.setColumns( 4 ); 
				pLineaCent.add( tfTiempoPausas );
				pLineaCent.add( new JLabel( "(msegs.)" ) );
			pSupCent.add( pLineaCent );
				bSimular = new JButton(); bSimular.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Play.png" ) ) );
			pSupCent.add( bSimular );
				JLabel l2 = new JLabel( "Paso a paso", JLabel.CENTER );
				JPanel p2 = new JPanel();
				p2.setBorder( BorderFactory.createLineBorder( Color.blue, 1 ));
				p2.add(l2);
			pSupDer.add( p2 );
				bStop = new JButton(); bStop.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Stop.png" ) ) );
				pLineaDer.add( bStop );
				bPlay = new JButton(); bPlay.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Play Pause.png" ) ) );
				pLineaDer.add( bPlay );
			pSupDer.add( pLineaDer );
			pSup.add( pSupIzq, BorderLayout.CENTER );
			pSupPlay.add( pSupCent );
			pSupPlay.add( pSupDer );
			pSup.add( pSupPlay, BorderLayout.EAST );
			getContentPane().add( pSup, BorderLayout.NORTH );
				pPrincipal = new JPanel(); pPrincipal.setLayout( null ); pPrincipal.setPreferredSize( new Dimension( 800, 400 ) );
			getContentPane().add( pPrincipal, BorderLayout.CENTER );
				lMensaje = new JLabel( " " ); lMensaje.setFont( new Font( "Arial", Font.BOLD, 24 )); lMensaje.setHorizontalAlignment( SwingConstants.CENTER );
			getContentPane().add( lMensaje, BorderLayout.SOUTH );
			// Formato general
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			pack();
			// Eventos
			tfTiempoPausas.addFocusListener( new FocusListener() {
				String valAnt;
				@Override
				public void focusGained(FocusEvent e) {
					valAnt = tfTiempoPausas.getText();
				}
				@Override
				public void focusLost(FocusEvent e) {
					if (!valAnt.equals( tfTiempoPausas.getText() )) {   // Si ha habido cambio validar valor
						try {
							long l = Long.parseLong( tfTiempoPausas.getText() );
							if (l<0) throw new NumberFormatException();
							tiempoPausa = l;
						} catch (NumberFormatException ex) {
							JOptionPane.showMessageDialog( Ventana.this, "El valor debe ser un entero válido y positivo", "Error en valor", JOptionPane.ERROR_MESSAGE );
							tfTiempoPausas.requestFocus();
						}
					}
				}
			} );
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowActivated(WindowEvent e) {
					if (repintar!=null) repintar.run();
				}
				@Override
				public void windowClosing(WindowEvent e) {
					miVentana = null;
					sigoViva = false;
				}
			});
			pPrincipal.addComponentListener( new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					if (repintar!=null) repintar.run();
				}
			});
			bSimular.addActionListener( (e) -> {
				if (running==0) {
					running = 1;
					bSimular.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Pause.png" ) ) );
				} else {
					running = 0;
					bSimular.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Play.png" ) ) );
				}
			} );
			bStop.addActionListener( (e) -> {
				if (parada!=null) parada.run();
				if (running==1) // Si estaba ejecutándose se pone el icono de play de nuevo
					bSimular.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Play.png" ) ) );
				running = 0;
				if (repintar!=null) repintar.run();
			} );
			bPlay.addActionListener( (e) -> {
				running = 2;
			} );
		}
	}
	
	public VisualizaProceso() {
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					miVentana = new Ventana();
					miVentana.setVisible( true );
				}
			} );
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Hace la pausa que corresponda si está ejecutándose la simulación, indefinida si no se está ejecutando nada (hasta que se de a un botón de play)
	 * Antes de la pausa pone el mensaje indicado
	 */
	public void hazPausa( String mensaje ) {
		ponMensaje( mensaje );
		hazPausa();
	}
	
	/** Hace la pausa que corresponda si está ejecutándose la simulación, indefinida si no se está ejecutando nada (hasta que se de a un botón de play)
	 */
	public void hazPausa() {
		if (sigoViva) {
			if (repintar!=null) repintar.run();
			if (running==1) {  // Modo simulación
				if (tiempoPausa>0) {
					try { Thread.sleep( tiempoPausa ); } catch (InterruptedException e) {}
				}
			} else if (running==2) {  // Modo paso a paso
				running = 0;
			}
			while (running==0 && sigoViva) {
				try { Thread.sleep( 10 ); } catch (InterruptedException e) {}
			}
		}
	}
	
	/** Pone un mensaje en la línea de mensajes
	 * @param mensaje
	 */
	public void ponMensaje( String mensaje ) {
		if (mensaje==null || mensaje.isEmpty()) mensaje = " ";
		if (miVentana!=null) miVentana.lMensaje.setText( mensaje );
	}
	
	/** Devuelve el panel de configuración. Por defecto tiene FlowLayout
	 * @return	panel de configuración de la ventana de proceso (arriba a la izquierda)
	 */
	public JPanel getPanelConfig() {
		return miVentana.pSupIzq;
	}
	
	/** Devuelve la ventana
	 * @return
	 */
	public JFrame getVentana() {
		return miVentana;
	}
	
	/** Devuelve el panel principal. Por defecto tiene layout nulo
	 * @return	panel principal de la ventana de proceso (abajo central)
	 */
	public JPanel getPanelPrincipal() {
		return miVentana.pPrincipal;
	}
	
	/** Define el código a ejecutar cuando se hace una espera con el método {@link #hazPausa()}. 
	 * También se utiliza cuando se repinta la ventana por manipulación directa del usuario (cambio de tamaño o reactivación de la ventana por ejemplo)
	 * @param r	Objeto cuyo código run() debe ejecutarse
	 */
	public void setProcesoRepintado( Runnable r ) {
		repintar = r;
	}
	
	/** Define el código a ejecutar cuando se pulsa el stop en pantalla 
	 * @param r	Objeto cuyo código run() debe ejecutarse
	 */
	public void setProcesoParada( Runnable r ) {
		parada = r;
	}
	
	/** Informa si la ventana ha sido eliminada, por el programador o por el usuario
	 * @return	true si la ventana sigue activa, false si ha sido cerrada
	 */
	public boolean isAlive() {
		return sigoViva;
	}
	
	/** Repinta explícitamente
	 */
	public void repintar() {
		if (repintar!=null) repintar.run();
	}
	
	/** Mata la ventana
	 */
	public void kill() {
		sigoViva = false;
		if (miVentana!=null) miVentana.dispose();
	}
	
	/** Indica si se ha pulsado el botón de play
	 * @return	true si se ha pulsado
	 */
	public boolean isRunning() {
		return (running==1);
	}
	
	/** Cambia el modo de visualización
	 * @param mode	0 para parada, 1 para ejecución continua, 2 para paso a paso
	 */
	public void setRunningMode( int mode ) {
		if (running==1) miVentana.bSimular.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Play.png" ) ) );
		running = 0;
	}
	
	/** Hace un stop programático del proceso
	 */
	public void stop() {
		if (running==1) miVentana.bSimular.setIcon( new ImageIcon( VisualizaProceso.class.getResource( "img/Button Play.png" ) ) );
		running = 0;
	}
	
	/** Indica si se ha pulsado el botón de paso a paso
	 * @return	true si se ha pulsado
	 */
	public boolean isStepByStep() {
		return (running==2);
	}
	
		// Variables de ejemplo para el main
		private static JLabel lDato = new JLabel( " " );
		private static int i;
		
	/** Ejemplo de utilización de la clase. Va visualizando valores de un bucle
	 * @param args
	 */
	public static void main(String[] args) {
		VisualizaProceso vp = new VisualizaProceso();
		vp.setProcesoRepintado( new Runnable() {
			@Override
			public void run() {
				lDato.setText( "Valor = " + i );
			}
		});
		vp.setProcesoParada( new Runnable() {
			@Override
			public void run() {
				i = 0;
			}
		});
		vp.getPanelPrincipal().setLayout( new FlowLayout() );
		vp.getPanelPrincipal().add( lDato );
		for (i = 0; i<100; i++) {
			vp.hazPausa();
		}
	}

}
