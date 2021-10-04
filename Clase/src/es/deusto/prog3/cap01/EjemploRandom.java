package es.deusto.prog3.cap01;

import java.util.Random;

public class EjemploRandom {

	public static void main(String[] args) {
		Random r = new Random();
		System.out.println( "Tres enteros aleatorios de 0 a 99:" );
		System.out.println( r.nextInt(100) + ", " + r.nextInt(100) + ", " + r.nextInt(100) );
		System.out.println( "Tres reales aleatorios de 0 a 1:" );
		System.out.println( r.nextDouble() + ", " + r.nextDouble() + ", " + r.nextDouble() );
		r = new Random(15);
		System.out.println( "Tres enteros aleatorios con semilla 15, de 0 a 99:" );
		System.out.println( r.nextInt(100) + ", " + r.nextInt(100) + ", " + r.nextInt(100) );
		r = new Random(15);
		System.out.println( "Tres enteros aleatorios con nueva semilla 15, de 0 a 99:" );
		System.out.println( r.nextInt(100) + ", " + r.nextInt(100) + ", " + r.nextInt(100) );
		// Ahhhmmm... por eso son pseudoaleatorios  :-)
	}

}
