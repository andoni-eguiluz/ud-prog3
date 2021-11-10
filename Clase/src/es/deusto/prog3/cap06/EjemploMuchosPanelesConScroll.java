package es.deusto.prog3.cap06;

import java.awt.*;
import java.util.Random;

import javax.swing.*;

import es.deusto.prog3.utils.JLabelGrafico;

/** Ejemplo de muchos paneles personalizados en filas - columnas (con BoxLayout)
 * @author andoni.eguiluz at ingenieria.deusto.es
 */
@SuppressWarnings("serial")
public class EjemploMuchosPanelesConScroll extends JFrame {

	public static void main(String[] args) {
		EjemploMuchosPanelesConScroll ventana = new EjemploMuchosPanelesConScroll();
		ventana.setVisible( true );
		ventana.empezarPanel();
		for (int i=0; i<100; i++) {
			Dato datoPrueba = new Dato( "Dato " + i, "Texto " + i + "\nBlablabla\nBlablabla\nBlablabla\nBlablabla\nBlablabla\nBlablabla\nBlablabla" );
			ventana.anyadirDato( datoPrueba );
		}
		ventana.acabarPanel();
	}

	private JPanel pPrincipal;
	public EjemploMuchosPanelesConScroll() {
		setTitle( "Ventana de ejemplo de paneles con scroll" );
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		setLocationRelativeTo( null );
		pPrincipal = new JPanel();
		pPrincipal.setLayout(new BoxLayout(pPrincipal,BoxLayout.Y_AXIS));
		add( new JScrollPane( pPrincipal ) , BorderLayout.CENTER );  // El panel principal ocupa toda la ventana (dentro de un panel de scroll)
	}
	
	// Suponemos que tiene filas con BoxLayout y columnas también con BoxLayout. La marca de dónde vamos añadiendo la llevan estas variables:
	private int numCol;  // En qué columna vamos añadiendo
	private final int NUM_COLS = 5;  // 5 columnas en cada fila
	private JPanel pEnCurso = null;  // Panel que se está añadiendo 
	public void empezarPanel() {
		numCol = 4; // Para que al empezar se empiece en fila nueva
		pPrincipal.removeAll();  // Quita todo para empezar "limpio"
	}
	public void anyadirDato( Dato dato ) {
		numCol++;  // Incremento la columna en la que se añade
		if (numCol==NUM_COLS) {  // Si está llena la fila (última columna) se empieza fila nueva
			numCol = 0;
			pEnCurso = new JPanel();
			pEnCurso.setLayout(new BoxLayout(pEnCurso,BoxLayout.X_AXIS));
			pPrincipal.add( pEnCurso );
		}
		pEnCurso.add( new MiPanel( dato.titulo, dato.texto ) );  // Añadimos panel
	}
	public void acabarPanel() {
		pPrincipal.revalidate();  // Rehace la visual de todo el panel con el montaje
	}

	// Gráficos de ejemplo
	private static String[] grafsEjemplo = new String[] { "img/bicho.png", "img/nave.png", "img/star.png", "img/asteroid.png", "img/UD-green.png" };

	// Ejemplo de clase de panel particular para ver cómo se visualizan muchos paneles como este
	private static class MiPanel extends JPanel {
		public MiPanel( String titulo, String texto ) {
			setLayout( new BorderLayout() );
			JLabel lTitulo = new JLabel( titulo, JLabel.CENTER );
			add( lTitulo, BorderLayout.NORTH );
			JTextArea taTexto = new JTextArea( texto, 5, 10 );  // Atención - este marca el tamaño visual que va a "pedir" la textarea, 5 filas y 10 columnas
			add( new JScrollPane( taTexto ), BorderLayout.CENTER );
			String grafEjemplo = grafsEjemplo[(new Random()).nextInt(grafsEjemplo.length)];  // Aleatorio para el ejemplo
			JLabelGrafico graficoEjemplo = new JLabelGrafico( grafEjemplo, 50, 80 );
			add( graficoEjemplo, BorderLayout.EAST );
		}
	}
	
	// Ejemplo de clase de dato a visualizar en cada panel
	private static class Dato {
		String titulo;
		String texto;
		public Dato( String titulo, String texto ) {
			this.titulo = titulo;
			this.texto = texto;
		}
	}
}
