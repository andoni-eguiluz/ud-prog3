package es.deusto.prog3.cap01.resueltos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class JavaFuncionalEjemplo {
	int x;
	int y;
	// int-int funcion = funcion;
	
	public static void main(String[] args) {
		
		// Java 7
		JButton boton = new JButton( "hola" );
		boton.addActionListener( new ActionListener() {  // objeto de clase anónima que implementa ActionListener
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println( "hola" );
			}
		});
		boton.addActionListener( new MiEsc() );  // objeto de clase no anónima que implementa ActionListener
		
		// Java 8
		boton.addActionListener( 
				(e) -> { System.out.println( "hola" ); }
				// ActionListener
		);
		
		
		boton.addMouseListener( new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		// No se puede si son varios métodos
//		boton.addMouseListener( 
//			(MouseEvent e) -> { }
//		);
		
		ActionListener objetoEsc1 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println( "hola 2");
			}
		};
		ActionListener objetoEsc2 = (e) -> { System.out.println( "hola 2"); };
		boton.addActionListener( devolverFuncion() );
		
		
		Thread hilo = new Thread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		
		Thread hilo2 = new Thread(
			() -> {}
		);
		
	}
	
	public static ActionListener devolverFuncion() {
		return (e) -> { System.out.println( "hola 3" ); };
	}
	
	public static int funcion( int x ) {
		return x;
	}
}

class MiEsc implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println( "hola" );
	}
}
