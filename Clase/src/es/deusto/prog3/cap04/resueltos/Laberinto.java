package es.deusto.prog3.cap04.resueltos;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** Solución recursiva a un laberinto
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class Laberinto {
	
	// ====================================================
	// Parte static
	// ====================================================
	
	public static void main(String[] args) {
		Laberinto l = new Laberinto();    sacaVentana( l );  // Visualización
		l.entra();    mostrarLab( l, "Inicio", true );  // Visualización
		boolean fin = resuelveLaberinto( l );
		sacaMens( "Fin de laberinto " + (fin ? "CON" : "SIN") + " solución" );
	}
	
	// Intenta resolver el laberinto de forma recursiva. Devuelve true si se ha conseguido
	private static boolean resuelveLaberinto( Laberinto l ) {
		if (l.acabado()) return true;
		Direccion dir;
		while ((dir = l.posibleMovimiento()) !=null) {
			                        if (ventCerrada) return false; // Corte: Si se cierra la ventana, se acaba
			l.mueve( dir );         mostrarLab( l, "Mueve " + dir, true ); // Visualización
			boolean fin = resuelveLaberinto( l );
			if (fin) sacaMens( "Encontrada una salida" );
			if (fin) return true;  // Truncamos el proceso cuando se acaba (observa que si se comenta esta línea, no se trunca la recursión y se hacen todos los caminos posibles)
			l.mueve( dir.dirOpuesta() );       mostrarLab( l, "Backtrack " + dir.dirOpuesta(), true ); // Visualización
		}
		return l.acabado();
	}

	// ====================================================
	// Parte visual
	// ====================================================
	
	private static JFrame ventana;              // Ventana de visualización del laberinto
	private static JLabel[][] lLab;             // Etiquetas de texto para mostrar el laberinto
	private static JTextArea taMens;            // Área de texto para mensajes
	private static long msegsPausa = 1000;      // Msg de pausa entre movimientos  (de 0 a 1000)
	private static boolean enPausa = true;      // Información de pausa
	private static boolean ventCerrada = false; // Información de cierre de ventana
	private static JSlider slTempo;             // Slider de velocidad
	
	// Saca la ventana con el laberinto
	private static void sacaVentana( Laberinto l ) {
		ventana = new JFrame( "Laberinto" );
		ventana.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JPanel p = new JPanel( new GridLayout( l.getAltura(), l.getAnchura() ) );
		lLab = new JLabel[l.getAltura()][l.getAnchura()];
		for (int fila=0; fila<l.getAltura(); fila++) {
			for (int col=0; col<l.getAnchura(); col++) {
				lLab[fila][col] = new JLabel( "   " );
				lLab[fila][col].setFont( new Font( "Courier", Font.PLAIN, 22 ) );
				lLab[fila][col].setOpaque( true );
				p.add( lLab[fila][col] );
				mostrarCasillaLab( l, fila, col );
			}
		}
		ventana.add( p, BorderLayout.CENTER );
		taMens = new JTextArea( 6, 10 ); // Lo importante van a ser las filas porque lo metemos en el sur
		taMens.setFont( new Font( "Courier", Font.PLAIN, 18 ) );
		ventana.add( new JScrollPane(taMens), BorderLayout.SOUTH );
		slTempo = new JSlider( 0, 1000 );
		slTempo.setValue( 0 );
		JPanel pSuperior = new JPanel();
		JButton bPausa = new JButton( enPausa ? "Play" : "Pausa" );
		pSuperior.add( new JLabel("Vel:")); pSuperior.add( slTempo ); pSuperior.add( bPausa );
		ventana.add( pSuperior, BorderLayout.NORTH );
		mostrarLab( l, null, false ); // Carga el laberinto en la ventana por primera vez
		ventana.pack();
		ventana.setLocationRelativeTo( null );
		// Eventos
		slTempo.addChangeListener( new ChangeListener() {
			private double factor = 1000.0 / Math.log(1001);
			@Override
			public void stateChanged(ChangeEvent e) {
				// msegsPausa = 1000 - slTempo.getValue(); // Cambio lineal (0-1000 --> cambio msgs 1000 a 0)
				msegsPausa = (int) Math.round( factor * (Math.log(1001) - Math.log(1+slTempo.getValue())) );  // Cambio logarítmico - más natural para tener rango de aceleración en el slider
			}
		});
		bPausa.addActionListener( (e) -> {
			if (enPausa) {
				bPausa.setText( "Pausa" );
			} else {
				bPausa.setText( "Play" );
			}
			enPausa = !enPausa;
		});
		ventana.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				ventCerrada = true;
			}
		});
		// Visualización de ventana
		ventana.setVisible( true );
	}
	
	private static void mostrarCasillaLab( Laberinto l, int fila, int col ) {
		char casilla = l.lab[fila][col];
		JLabel lCasilla = lLab[fila][col];
		if (casilla == 'X' ) {
			lCasilla.setBackground( Color.DARK_GRAY );
		} else if (casilla == 'E' ) {
			lCasilla.setBackground( Color.CYAN );
			lCasilla.setText( " E " );
		} else if (casilla == 'S' ) {
			lCasilla.setBackground( Color.CYAN );
			lCasilla.setText( " S " );
		} else if (casilla == ' ' ) {
			lCasilla.setBackground( Color.WHITE );
			lCasilla.setText( "   " );
		}
		if (l.marca[fila][col]) {
			lCasilla.setBackground( Color.LIGHT_GRAY );
			if (casilla==' ') lCasilla.setText( " . " );  // Si es pasillo andado pone un punto
		}
		if (fila==l.filaJugador && col==l.colJugador) {
			lCasilla.setBackground( Color.MAGENTA );
		}
	}
	
	// Muestra el laberinto en pantalla con posible pausa después
	private static void mostrarLab( Laberinto l, String mens, boolean pausa ) {
		sacaMens( mens );
		for (int fila=0; fila<l.getAltura(); fila++) {
			for (int col=0; col<l.getAnchura(); col++) {
				mostrarCasillaLab( l, fila, col );
			}
		}
		if (pausa) pausa();
	}
	
	private static void sacaMens( String mens ) {
		if (mens!=null && !mens.isEmpty()) { 
			taMens.append( "\n" + mens );
			taMens.setSelectionStart( taMens.getText().length() );
		}
	}
	
	// Hace una pausa
	private static void pausa() {
		try { Thread.sleep( msegsPausa ); } catch (InterruptedException e) {}
		while (enPausa && !ventCerrada) {
			try { Thread.sleep( msegsPausa ); } catch (InterruptedException e) {}
		}
	}
	
	// ====================================================
	// Subclase utilitaria de dirección
	// ====================================================
	
	/** Dirección de avance en el laberinto
	 * @author andoni.eguiluz @ ingenieria.deusto.es
	 */
	public static enum Direccion { IZQUIERDA, ARRIBA, DERECHA, ABAJO;
		public Direccion dirOpuesta() {
			if (this==Direccion.IZQUIERDA) return Direccion.DERECHA;
			else if (this==Direccion.DERECHA) return Direccion.IZQUIERDA;
			else if (this==Direccion.ARRIBA) return Direccion.ABAJO;
			else return Direccion.ARRIBA;
		}
	}
	
	// ====================================================
	// Parte no static - clase Laberinto
	// ====================================================
	
	private static String[] LAB_POR_DEFECTO = { // Laberinto expresado como un array de strings
		"XXXXXXXXXXSXXXXXXSXX",   // X son las paredes
		"X   X X     X      X",   // S es la salida (o las salidas)
		"X X X X XXXXXXXXXX X",   // espacios son los caminos viables
		"X X   X   X   X  X X",
		"X XXXXXX XX X X XX X",
		"X      X    X X  X X",
		"XXXXXX XXXXXX XX X X",
		"X      X   X     X X",
		"X XXXXXX XXXX XXXX X",
		"X      X X         X",
		"XXXXXX X X XXXXXXXXX",
		"X    X X X      X  X",
		"XXXX X X XXXXXX X XX",
		"X  X X X      X X  X",
		"X XX X X XXXX X XX X",
		"X    X X    X X    X",
		"X XXXX XXXX X XXXX X",
		"X           X      X",   // E es la entrada
		"XXXXXXEXXXXXXXXXXXXX"
	};
	
	private char[][] lab;      // Laberinto ('X' para pared, 'E' para entrada, 'S' para salida, ' ' para camino)
	private boolean[][] marca; // Indicación de si se ha pasado o no por la casilla indicada
	private int filaJugador;   // Posición de fila donde está el jugador del laberinto (-1 si no ha entrado)
	private int colJugador;    // Posición de columna donde está el jugador del laberinto (-1 si no ha entrado)
	
	/** Inicializa un laberinto partiendo de un modelo dato con un array de strings
	 * @param lab	Laberinto donde cada fila es un string y cada carácter simboliza el objeto del
	 *              laberinto: "X" para pared, "E" para entrada, "S" para salida, " " para camino.<br/>
	 *              Las filas tienen que tener la misma longitud (anchura constante de laberinto)
	 */
	public Laberinto( String[] lab ) {
		int numCols = lab[0].length();
		this.lab = new char[lab.length][numCols];
		this.marca = new boolean[lab.length][numCols];
		int numFila = 0;
		for (String fila : lab) {
			for (int col=0; col<fila.length(); col++) {
				this.lab[numFila][col] = fila.charAt( col );
			}
			numFila++;
		}
		filaJugador = -1;
		colJugador = -1;
	}
	
	/** Inicializa un laberinto con un valor por defecto de 20 columnas y 19 filas
	 */
	public Laberinto() {
		this( LAB_POR_DEFECTO );
	}
	
	/** Reinicia el laberinto (borra todas las marcas y pone al jugador fuera aún del laberinto)
	 */
	public void reset() {
		for (int fila=0; fila<lab.length; fila++) {
			for (int col=0;col<lab[0].length; col++) {
				marca[fila][col] = false;
			}
		}
		filaJugador = -1;
		colJugador = -1;
	}
	
	/** Devuelve la altura del laberinto
	 * @return	Número de filas
	 */
	public int getAltura() {
		return lab.length;
	}
	
	/** Devuelve la anchura del laberinto
	 * @return	Número de columnas
	 */
	public int getAnchura() {
		return lab[0].length;
	}
	
	/** Mete al jugador en el laberinto (en la casilla de entrada) y marca esa casilla como pasada
	 */
	public void entra() {
		for (int fila=0; fila<lab.length; fila++) {
			for (int col=0;col<lab[0].length; col++) {
				if (lab[fila][col]=='E') {
					filaJugador = fila;
					colJugador = col;
					marca[filaJugador][colJugador] = true;
					return;
				}
			}
		}
	}
	
	/** Intenta mover al jugador en la dirección indicada dentro del laberinto y marca la nueva posición (si procede)
	 * @param avance	Dirección de movimiento del jugador
	 * @return	true si se puede mover, false si hay una pared en esa dirección (y entonces no hay movimiento)
	 */
	public boolean mueve( Direccion avance ) {
		int filaDestino = calculaFilaDestino(avance);
		int colDestino = calculaColDestino(avance);
		if (filaDestino>=getAltura() || filaDestino<0 || colDestino>=getAnchura() || colDestino<0)
			return false;
		else if (lab[filaDestino][colDestino]=='X')  // Pared
			return false;
		else {  // Movimiento válido
			filaJugador = filaDestino;
			colJugador = colDestino;
			marca[filaDestino][colDestino] = true;  
			return true;
		}
	}
	
		// Calcula fila destino según avance
		private int calculaFilaDestino( Direccion avance ) {
			int filaDestino = filaJugador;
			if (avance==Direccion.ARRIBA) filaDestino--;
			else if (avance==Direccion.ABAJO) filaDestino++;
			return filaDestino;
		}
		
		// Calcula columna destino según avance
		private int calculaColDestino( Direccion avance ) {
			int colDestino = colJugador;
			if (avance==Direccion.IZQUIERDA) colDestino--;
			else if (avance==Direccion.DERECHA) colDestino++;
			return colDestino;
		}
	
	
	/** Comprueba si hay pasillo para avanzar en una dirección dada
	 * @param avance	Dirección de comprobación
	 * @return	true si hay pasillo en esa dirección (esté marcado o no), false en caso contrario
	 */
	public boolean hayPasilloEn( Direccion avance ) {
		int filaDestino = calculaFilaDestino(avance);
		int colDestino = calculaColDestino(avance);
		if (filaDestino>=getAltura() || filaDestino<0 || colDestino>=getAnchura() || colDestino<0)
			return false;
		else if (" ES".contains( ""+lab[filaDestino][colDestino]))
			return true;
		else return false;
	}
	
	/** Comprueba si hay marca para avanzar en una dirección dada
	 * @param avance	Dirección de comprobación
	 * @return	true si hay marca en esa dirección, false en caso contrario
	 */
	public boolean hayMarcaEn( Direccion avance ) {
		int filaDestino = calculaFilaDestino(avance);
		int colDestino = calculaColDestino(avance);
		if (filaDestino>=getAltura() || filaDestino<0 || colDestino>=getAnchura() || colDestino<0)
			return false;
		else return marca[filaDestino][colDestino];
	}
	
	/** Comprueba si hay algún movimiento posible a casilla sin marca y devuelve la primer dirección disponible.
	 * @return	Dirección de movimiento posible o null si no hay ninguna
	 */
	public Direccion posibleMovimiento() {
		for (Direccion d : Direccion.values()) {
			if (hayPasilloEn(d) && !hayMarcaEn(d)) {
				return d;
			}
		}
		return null;
	}
	
	/** Informa si se ha llegado al final del laberinto
	 * @return	true si el jugador ha llegado a la casilla de salida, false en caso contrario
	 */
	public boolean acabado() {
		return (lab[filaJugador][colJugador] == 'S');
	}
	
	/* Devuelve el laberinto en formato de string multilínea
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String ret = "";
		for (int fila=0; fila<lab.length; fila++) {
			for (int col=0;col<lab[0].length; col++) {
				char c = lab[fila][col];
				if (fila==filaJugador && col==colJugador) c = 'O';
				if (marca[fila][col] && c=='.') c = '.';
				ret += c;
			}
			ret += "\n";
		}
		return ret;
	}

}
