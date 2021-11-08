package es.deusto.prog3.cap04;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.deusto.prog3.utils.VisualizaProceso;

/** Ejemplo de backtracking utilizando el Sudoku (se usa backtracking tanto para generar un Sudoku válido, como para resolverlo).
 * Utiliza la clase de utilidad VisualizaProceso para visualizar el backtracking según va ocurriendo
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class SudokuResueltoEnVentana {

	private static VisualizaProceso visual;
	private static Sudoku sudoku;
	public static void main(String[] args) throws Exception {
		int opcion = 1;  // 1 = Aleatorio   2 = Manual
		sudoku = new Sudoku();
		visualiza();
		visual.ponMensaje( "Creando sudoku aleatorio...");
		if (opcion==1) sudoku.creaRandom();
		else if (opcion==2)
			sudoku.creaSudokuManual( new int[][] {
				{ 0, 5, 0  , 3, 9, 0,   0, 8, 0},
				{ 0, 4, 7  , 0, 0, 0,   3, 0, 0},
				{ 0, 0, 0  , 0, 0, 7,   0, 0, 5},
				
				{ 0, 0, 1  , 0, 0, 0,   0, 0, 0},
				{ 0, 0, 4  , 7, 0, 5,   6, 0, 0},
				{ 0, 0, 0  , 0, 0, 0,   8, 0, 0},
				
				{ 7, 0, 0  , 8, 0, 0,   0, 0, 0},
				{ 0, 0, 6  , 0, 0, 0,   5, 2, 0},
				{ 0, 8, 0  , 0, 2, 9,   0, 1, 0}
			});
		visual.setRunningMode( 0 );
		visual.ponMensaje( "Sudoku creado.");
		visual.hazPausa();
		visual.ponMensaje( "Ocultando casillas...");
		if (opcion==1) sudoku.ocultaRandom( 27 );
		visual.setRunningMode( 0 );
		visual.ponMensaje( "Casillas ocultadas.");
		visual.hazPausa();
		visual.ponMensaje( "Solucionando sudoku por backtracking...");
		sudoku.intentaSolucionar();
		visual.setRunningMode( 0 );
		visual.repintar();
		visual.ponMensaje( "Final!");
		visual.hazPausa();
		visual.kill();
	}

	private static JPanel panelSudoku;
	private static JLabel[][] lSudoku;
	private static void visualiza() {
		visual = new VisualizaProceso();
		panelSudoku = new JPanel();
		panelSudoku.setLayout( new GridLayout( 9, 9 ));
		lSudoku = new JLabel[9][9];
		Font font = new Font( "Arial", Font.BOLD, 18 );
		for (int fila=0; fila<9; fila++) {
			for (int col=0; col<9; col++) {
				JLabel l = new JLabel( " ", JLabel.CENTER );
				l.setOpaque( true );
				l.setFont( font );
				l.setBorder( BorderFactory.createLineBorder( Color.black, 2 ));
				lSudoku[fila][col] = l;
				panelSudoku.add( l );
			}
		}
		visual.setProcesoRepintado( () -> {
			for (int fila=0; fila<9; fila++) {
				for (int col=0; col<9; col++) {
					int cuadrante = (fila/3 + col/3) % 2;
					if (sudoku.marca[fila][col]==0) {
						lSudoku[fila][col].setBackground( cuadrante==0 ? Color.white : new Color( 215, 215, 215 ) );
						lSudoku[fila][col].setText( sudoku.valor[fila][col]==0 ? " " : ""+sudoku.valor[fila][col] );
					} else if (sudoku.marca[fila][col]>0) {
						lSudoku[fila][col].setBackground( cuadrante==0 ? new Color( 0, 235, 235 ) : new Color( 0, 195, 195 ) );
						lSudoku[fila][col].setText( sudoku.marca[fila][col]+"" );
					} else {  // -1 casilla oculta
						lSudoku[fila][col].setBackground( cuadrante==0 ? new Color( 135, 135, 135 ) : new Color( 95, 95, 95 ) );
						lSudoku[fila][col].setText( " " );
					}
				}
			}
		});
		visual.getPanelPrincipal().setLayout( new BorderLayout() );
		visual.getPanelPrincipal().add( panelSudoku, BorderLayout.CENTER );
	}

	
	private static class Sudoku {
		protected int[][] valor; // Sudoku solucionado  (0 = no solucionado)
		protected int[][] marca; // 0 = valor mostrado | -1 = oculto | 1 a 9 = puesto por el usuario o por el solucionador
		/** Crea un Sudoku vacío
		 */
		public Sudoku() {
			valor = new int[9][9];
			marca = new int[9][9];
		}
		
		/** Crea un Sudoku introduciendo los valores a mano
		 * @param valores	Array de 9x9 valores de 1 a 9 (0 si el valor es oculto)
		 */
		public void creaSudokuManual( int[][] valores ) {
			for (int col=0; col<9; col++) { for (int fila=0; fila<9; fila++) {
				valor[fila][col] = 0;   // Valor sin solucionar
				marca[fila][col] = -1;  // Ocultar todas
			} }
			for (int fila=0; fila<valores.length; fila++) {
				for (int col=0; col<valores[0].length; col++) {
					if (valores[fila][col] == 0) {
						valor[fila][col] = 0;
						marca[fila][col] = -1;
					} else if (valores[fila][col] >= 1 && valores[fila][col] <= 9) {
						valor[fila][col] = valores[fila][col];
						marca[fila][col] = 0;
					}
				}
			}
		}
		/** Crea un Sudoku aleatorio visualizando n números aleatorios
		 * (ojo: el sudoku es correcto, pero puede ser que sea imposible de solucionar si se visualizan muy pocos números)
		 */
		public void creaRandom() {
			rellenaSudokuBackTracking( 0, 0 );
			visual.hazPausa();
		}
		/** Oculta casillas de un sudoku
		 * @param numCasillasVisibles	Número de casillas a dejar visibles
		 */
		public void ocultaRandom( int numCasillasVisibles ) {
			for (int col=0; col<9; col++) { for (int fila=0; fila<9; fila++) {
				marca[fila][col] = -1;  // Oculto todas
			} }
			while (numCasillasVisibles>0) {
				int random = r.nextInt( 81 );
				int fila = random / 9;
				int col = random % 9;
				if (marca[fila][col]==-1) {
					marca[fila][col] = 0;
					numCasillasVisibles--;
				}
			}
		}
		/** Intenta solucionar el sudoku por backtracking
		 */
		public void intentaSolucionar() {
			solucionaSudokuBackTracking( 0, 0 );
		}
		// Soluciona el Sudoku con backtracking
		private boolean solucionaSudokuBackTracking( int fila, int col ) {
			if (col==9) { col=0; fila++; }  // El avance es por columnas y luego por filas
			if (fila==9) return true;  // Ok - caso base
			if (marca[fila][col]==-1) {  // Solo si el valor está oculto se prueban las opciones
				for (int i=1; i<10; i++) {  // Ir probando de 1 a 9 todas las opciones
					visual.hazPausa();
					marca[fila][col] = i;
					if (isSudokuCorrect()) {
						if (solucionaSudokuBackTracking( fila, col+1 )) return true;
					}
				}
				marca[fila][col] = -1; // No se ha encontrado
				return false;
			} else {
				return solucionaSudokuBackTracking( fila, col+1 );
			}
		}
		// Rellena el Sudoku con backtracking
		private boolean rellenaSudokuBackTracking( int fila, int col ) {
			if (col==9) { col=0; fila++; }  // El avance es por columnas y luego por filas
			if (fila==9) return true;  // Ok - caso base
			int[] random = crea9Random();
			for (int i=0; i<9; i++) {
				visual.hazPausa();
				valor[fila][col] = random[i];
				if (isSudokuCorrect()) {
					if (rellenaSudokuBackTracking( fila, col+1 )) return true;
				}
			}
			valor[fila][col] = 0;
			return false;
		}
		// Comprueba que el sudoku planteado hasta el momento es correcto (0s significan casillas sin rellenar). true si lo es
		private boolean isSudokuCorrect() {
			boolean[] numerosQueHay;
			// Comprobación de filas
			for (int fila=0; fila<9; fila++) {
				numerosQueHay = new boolean[10];  // Inicializa a 10 falses (el 0 no se usa)
				for (int col=0; col<9; col++) {
					int val = valor[fila][col];
					if (marca[fila][col]==-1) val = 0;
					else if (marca[fila][col]>0) val = marca[fila][col];
					if (val>0) {
						if (numerosQueHay[val]) return false;
						numerosQueHay[val] = true;
					}
				}
			}
			// Comprobación de columnas
			for (int col=0; col<9; col++) {
				numerosQueHay = new boolean[10];  // Inicializa a 10 falses (el 0 no se usa)
				for (int fila=0; fila<9; fila++) {
					int val = valor[fila][col];
					if (marca[fila][col]==-1) val = 0;
					else if (marca[fila][col]>0) val = marca[fila][col];
					if (val>0) {
						if (numerosQueHay[val]) return false;
						numerosQueHay[val] = true;
					}
				}
			}
			// Comprobación de las 9 matrices parciales
			for (int fI=0; fI<9; fI+=3) {
				for (int cI=0; cI<9; cI+=3) {
					numerosQueHay = new boolean[10];  // Inicializa a 10 falses (el 0 no se usa)
					for (int col=0; col<3; col++) {
						for (int fila=0; fila<3; fila++) {
							int val = valor[fI+fila][cI+col];
							if (marca[fI+fila][cI+col]==-1) val = 0;
							else if (marca[fI+fila][cI+col]>0) val = marca[fI+fila][cI+col];
							if (val>0) {
								if (numerosQueHay[val]) return false;
								numerosQueHay[val] = true;
							}
						}
					}
				}
			}
			return true;
		}
			private Random r = new Random();
		// Crea un array de 9 números random
		private int[] crea9Random() {
			int[] ret = new int[9];
			ArrayList<Integer> datos = new ArrayList<>();
			for (int i = 1; i<10; i++) {
				datos.add( i );
			}
			for (int i = 0; i<9; i++) {
				int aleat = r.nextInt( datos.size() );
				ret[i] = datos.get(aleat);
				datos.remove(aleat);
			}
			return ret;
		}
	}
	
}
