package es.deusto.prog3.cap04.ejercicios;

import javax.swing.*;

import java.awt.*;

public class OchoDamas extends JFrame {
	JLabel[][] tablero = new JLabel[8][8];
	JLabel lMensaje = new JLabel();
	public OchoDamas() {
		setSize( 800,600 );
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		JPanel pCentro = new JPanel();
		pCentro.setLayout( new GridLayout(8,8) );
		for (int fila = 0; fila<8; fila++)
			for (int col = 0; col<8; col++) {
				tablero[fila][col] = new JLabel( " " );
				tablero[fila][col].setBorder( BorderFactory.createLineBorder( Color.black ));
				tablero[fila][col].setFont( new Font( "Arial", Font.BOLD, 24 ) );
				tablero[fila][col].setHorizontalAlignment( JLabel.CENTER );
				pCentro.add( tablero[fila][col] );
			}
		lMensaje.setFont( new Font( "Arial", Font.BOLD, 24 ) );
		lMensaje.setHorizontalAlignment( JLabel.CENTER );
		getContentPane().add( pCentro, BorderLayout.CENTER );
		getContentPane().add( lMensaje, BorderLayout.SOUTH );
	}
	
	public static void main(String[] args) {
		OchoDamas v = new OchoDamas();
		v.setVisible( true );
		System.out.println( "Resuelto? " + v.resolverTableroDesdeFila( v.tablero, 0 ) );
	}

	public boolean resolverTableroDesdeFila( JLabel[][] tablero, int fila ) {
		try { Thread.sleep(100); } catch (Exception e) {}
		// TODO Aquí cambiar y hacer:
		tablero[0][0].setText( "D" );
		tablero[1][0].setText( "D" );
		lMensaje.setText( "Es correcto? " + esPosicionCorrecta(tablero) );
		return esPosicionCorrecta(tablero);
	}
	
	public boolean esPosicionCorrecta( JLabel[][] tablero ) {
		for (int fila = 0; fila<8; fila++)
			for (int col = 0; col<8; col++)
				if (tablero[fila][col].getText().equals("D")) {
					boolean hayOtraDama = hayOtraDamaEn( tablero, fila, col );
					if (hayOtraDama) return false;
				}
		return true;
	}
	
	public boolean hayOtraDamaEn( JLabel[][] tablero, int fila, int col ) {
		for (int f = 0; f<8; f++) // Comprueba toda la columna
			if (fila!=f && tablero[f][col].getText().equals("D")) {
				System.out.print( "Dama en " + fila + "," + col );
				System.out.println( " amenaza a otra dama en " + f + "," + col );
				return true;
			}
		for (int c = 0; c<8; c++) // Comprueba toda la fila
			if ((col!=c) && tablero[fila][c].getText().equals("D")) {
				System.out.print( "Dama en " + fila + "," + col );
				System.out.println( " amenaza a otra dama en " + fila + "," + c );
				return true;
			}
		for (int inc = -7; inc<8; inc++) { // Comprueba toda la diagonal 1
			int filaD = fila-inc;  // Fila/columna en diagonal
			int colD = col-inc;
			if ((fila!=filaD) && (filaD>=0 && filaD<8 && colD>=0 && colD<8)
					&& tablero[filaD][colD].getText().equals("D")) {
				System.out.print( "Dama en " + fila + "," + col );
				System.out.println( " amenaza a otra dama en " + filaD + "," + colD );
				return true;
			}
		}
		for (int inc = -7; inc<8; inc++) { // Comprueba toda la diagonal 1
			int filaD = fila-inc;  // Fila/columna en diagonal 2
			int colD = col+inc;
			if ((fila!=filaD) && (filaD>=0 && filaD<8 && colD>=0 && colD<8)
					&& tablero[filaD][colD].getText().equals("D")) {
				System.out.print( "Dama en " + fila + "," + col );
				System.out.println( " amenaza a otra dama en " + filaD + "," + colD );
				return true;
			}
		}
		return false;
	}
	
}
