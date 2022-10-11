package es.deusto.prog3.cap01.resueltos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/** Clase canción de ejemplo, para hacer pruebas unitarias con JUnit
 */
public class Cancion {
	
	/** Método de prueba de ventana canción
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		try {
			Cancion c = new Cancion( "Shallow", 217 );
			JFrame v = c.getVentanaCancion();
			v.setVisible( true );
		} catch (CancionException e) {
			e.printStackTrace();
		}
	}
	
	
	protected String nombre = "";
	private int duracionEnSegundos;

	/** Crea una canción nueva
	 * @param nombre	Nombre de la canción (si es null se pone con string vacío)
	 */
	public Cancion( String nombre ) {
		setNombre( nombre );
		duracionEnSegundos = 0;
	}
	/** Crea una canción nueva
	 * @param nombre	Nombre de la canción (si es null se pone con string vacío)
	 * @param duracionEnSegundos	Duración de la canción en segundos
	 * @throws CancionException	Si la duración es negativa
	 */
	public Cancion( String nombre, int duracionEnSegundos ) throws CancionException {
		setNombre( nombre );
		setDuracionEnSegundos( duracionEnSegundos );
	}
	/** Actualiza el nombre de la canción
	 * @param nombre	Nuevo nombre. Si es null, no se modifica el nombre anterior
	 */
	public void setNombre(String nombre) {
		if (nombre==null) {
			return;
		}
		this.nombre = nombre;
	}
	/** Devuelve el nombre de la canción
	 * @return	Nombre de la canción
	 */
	public String getNombre() {
		return nombre;
	}
	/** cambia la duración de la canción
	 * @param duracionEnSegundos	Nueva duración, en segundos
	 * @throws CancionException	Si la duración es negativa
	 */
	public void setDuracionEnSegundos(int duracionEnSegundos) throws CancionException {
		if (duracionEnSegundos<0) {
			throw new CancionException( "Duración de canción incorrecta: " + duracionEnSegundos + " segundos." );
		}
		this.duracionEnSegundos = duracionEnSegundos;
	}
	/** Devuelve la duración de la canción
	 * @return	Duración en segundos
	 */
	public int getDuracionEnSegundos() {
		return duracionEnSegundos;
	}
	/** Devuelve la duración de la canción formateada como un string
	 * @return	Duración en formato hh:mm:ss rellenando a ceros
	 * Si hiciera falta se amplían los dígitos de las horas (>= 100 horas)
	 */
	public String getDuracion() {
		int horas = duracionEnSegundos / 3600;
		int minutos = (duracionEnSegundos % 3600) / 60;
		int segundos = duracionEnSegundos % 60;
		return String.format( "%02d:%02d:%02d", horas, minutos, segundos );
	}

	// Ojo si los atributos fueran privados sería mucho más difícil de probar
	protected JTextField tfNombre = new JTextField( "" );
	protected JLabel lDuracion = new JLabel( "" );
	protected JProgressBar pbDuracion = new JProgressBar();
	protected JButton bSimular = new JButton( "Simular" );
	/** Devuelve una ventana de edición de canción
	 * con un botón de simulación de duración de la canción
	 * @return	Ventana enlazada con el objeto canción, lista para visualizar
	 */
	public JFrame getVentanaCancion() {
		JFrame v = new JFrame( "Ventana canción" );
		v.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		v.setSize( 400, 120 );
		tfNombre.setText( nombre );
		lDuracion.setText( getDuracion() );
		pbDuracion.setStringPainted( true );
		pbDuracion.setMaximum( duracionEnSegundos );
		JPanel pCentral = new JPanel();
		pCentral.add( lDuracion );
		pCentral.add( bSimular );
		bSimular.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread h = (new Thread() {
					public void run() {
						pbDuracion.setMaximum( duracionEnSegundos );
						for (int i=0; i<=duracionEnSegundos; i++) {
							pbDuracion.setValue( i );
							try {
								Thread.sleep( 1000 );
							} catch (InterruptedException e) {}
						}
					}
				});
				h.setDaemon( true );
				h.start();
			}
		});
		v.add( tfNombre, BorderLayout.NORTH );
		v.add( pCentral, BorderLayout.CENTER );
		v.add( pbDuracion, BorderLayout.SOUTH );
		tfNombre.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setNombre( tfNombre.getText() );
			}
		});
		return v;
	}
}

/** Error en canción
 */
@SuppressWarnings("serial")
class CancionException extends Exception {
	public CancionException( String mensaje ) {
		super( mensaje );
	}
}