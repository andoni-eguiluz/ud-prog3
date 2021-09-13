package es.deusto.prog3.cap00.resueltos;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class Ej06VentanaAleatoria extends JFrame {
	public JComboBox<String> cbOpciones;  // Combo de opciones
	private JTable tDatos;      // JTable con datos
	private TableModel mDatos;  // Modelo de los datos de la JTable
	private JPanel panelTabla;  // Panel central para la tabla
	private JPanel panelCombo;  // Panel inferior para el combo y el botón
	private JButton aleatorio;
	private JLabel lMensaje;
	private Random r = new Random();  // Generador de aleatorios
	private String opciones[] = {"2x2","3x3","4x4","5x5"};  // Opciones del combo

	public static void main(String[] args) {
		Ej06VentanaAleatoria v = new Ej06VentanaAleatoria();
		v.setVisible(true);
	}

	public Ej06VentanaAleatoria() {
		super( "Ventana aleatoria - ej. 0.6" );
		setSize(400, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		panelTabla = new JPanel();
		panelTabla.setLayout( new BorderLayout() ); 
		add( panelTabla, BorderLayout.CENTER ); // Añade el panel de la tabla al centro
		panelCombo = new JPanel();
		add( panelCombo, BorderLayout.SOUTH);  // Añade el panel del combo al sur
		
		lMensaje = new JLabel( "Selecciona una opción de tabla:" );
		panelCombo.add(lMensaje);
		cbOpciones = new JComboBox<String>(opciones);
		panelCombo.add(cbOpciones);
		aleatorio = new JButton("“¡Aleatorio!”");
		panelCombo.add(aleatorio);

		aleatorio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numerosAleatorios();
			}

		});
		cbOpciones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				creaTabla();
			}
		});

		creaTabla();
	}

	// Crea la JTable con tamaño partiendo del valor del combo
	private void creaTabla() {
		int[] tamanyos = { 2, 3, 4, 5 };  // Tamaños en función de los valores del combo
		int tamanyo = tamanyos[ cbOpciones.getSelectedIndex() ];
		panelTabla.removeAll();  // Vacía el panel para volver a crear componente nuevo
		mDatos = new DefaultTableModel( tamanyo, tamanyo );
		if (tDatos==null) {  // Si no existía la tabla (primera vez), se crea
			tDatos = new JTable( mDatos );  // se crea
		} else {
			tDatos.setModel( mDatos );  // Si existía, se cambia el modelo (tamaño de datos)
		}
		panelTabla.add( tDatos, BorderLayout.SOUTH );
		panelCombo.revalidate();  // Reconstruye el panel que hemos cambiado
		panelTabla.repaint();  // Redibuja el panel general para refrescarlo (en blanco)
	}

	// Lanza los números aleatorios
	private void numerosAleatorios() {
		(new Thread() {
			public void run() {
				long tiempoInicio = System.currentTimeMillis(); 
				while (System.currentTimeMillis()-tiempoInicio < 3000) {  // Durante 3 segundos
					for (int i = 0; i<mDatos.getRowCount(); i++) {
						for (int j = 0; j<mDatos.getColumnCount(); j++) {
							int numero = (int) (r.nextInt(1000)) + 1;
							mDatos.setValueAt( numero+"", i, j );
						}
					}
					try {sleep(15);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		}).start();
	}

}
