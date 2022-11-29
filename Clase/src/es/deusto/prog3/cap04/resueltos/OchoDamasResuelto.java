package es.deusto.prog3.cap04.resueltos;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import es.deusto.prog3.cap04.VisualDeRecursividad;

import java.awt.*;

public class OchoDamasResuelto extends JFrame {
	JLabel[][] tablero = new JLabel[8][8];
	JLabel lMensaje = new JLabel( " " );
	public OchoDamasResuelto() {
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
		OchoDamasResuelto v = new OchoDamasResuelto();
		v.setVisible( true );
		// Versión de visual normal de backtracking
		// System.out.println( "Resuelto? " + v.resolverTableroDesdeFila( v.tablero, 0 ) );
		// Versión de visual de backtracking con seguimiento de llamadas
		VisualDeRecursividad arbol = new VisualDeRecursividad( "Test Hanoi", true );
		System.out.println( "Resuelto? " + v.resolverTableroDesdeFila( arbol, null, v.tablero, 0 ) );
	}

	/* Algoritmo base:
	 	si fila = 8
			caso base - true
		si no
			probar todas las columnas de 0 a 7
			en cada columna poner la dama
			¿es posición correcta de tablero?
			sí -
				probar recursivo dama + 1
				si prueba ok - caso base true
				si no - seguir probando
			no -
				quitar esa dama de esa columna
			caso base: no hay solución - false
	 */
	public boolean resolverTableroDesdeFila( JLabel[][] tablero, int fila ) {
		try { Thread.sleep(200); } catch (Exception e) {}
		if (fila==8) {
			lMensaje.setText( "Resuelto!" );
			return true;
		} else {
			for (int col=0; col<8; col++) {
				tablero[fila][col].setText("D");
				if (esPosicionCorrecta(tablero)) {
					boolean res = resolverTableroDesdeFila( tablero, fila+1 );
					if (res) return true;
					tablero[fila][col].setText(" ");
				} else {
					tablero[fila][col].setText(" ");
				}
			}
			return false;
		}
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
				// System.out.print( "Dama en " + fila + "," + col );
				// System.out.println( " amenaza a otra dama en " + f + "," + col );
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

	
	// Versión de visualización de llamadas
	public boolean resolverTableroDesdeFila( VisualDeRecursividad vr, DefaultMutableTreeNode padre, JLabel[][] tablero, int fila ) {
		DefaultMutableTreeNode nuevaLlamada = vr.anyadeNodoHijo( "damas-fila("+fila+")", padre );
		while (vr.isPaused()) { try { Thread.sleep(100); } catch (Exception e) {} }  // Pausa con botón
		try { Thread.sleep(200); } catch (Exception e) {}
		if (fila==8) {
			vr.cambiaValorNodo( "damas-fila("+fila+") -> RESUELTO!", nuevaLlamada );
			lMensaje.setText( "Resuelto!" );
			return true;
		} else {
			for (int col=0; col<8; col++) {
				vr.cambiaValorNodo( "damas-fila("+fila+") -> probando columna "+col, nuevaLlamada );
				while (vr.isPaused()) { try { Thread.sleep(100); } catch (Exception e) {} }  // Pausa con botón
				try { Thread.sleep(100); } catch (Exception e) {}
				tablero[fila][col].setText("D");
				if (esPosicionCorrecta(tablero)) {
					boolean res = resolverTableroDesdeFila( vr, nuevaLlamada, tablero, fila+1 );
					if (res) return true;
					tablero[fila][col].setText(" ");
				} else {
					tablero[fila][col].setText(" ");
				}
			}
			vr.cambiaValorNodo( "damas-fila("+fila+") -> no hay solución por aquí ", nuevaLlamada );
			return false;
		}
	}
	
	
}
