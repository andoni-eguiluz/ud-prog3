package es.deusto.prog3.cap01.resueltos;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFuncionParaTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFactorialNegativo() {
		try {
			FuncionParaTest.factorial(-1); 
			fail( "Excepción no lanzada" );
		} catch (ArithmeticException e) {
			// nada
		}
		try {
			FuncionParaTest.factorial(-2);
			fail( "Excepción no lanzada" );
		} catch (ArithmeticException e) {}
		try {
			FuncionParaTest.factorial(Integer.MIN_VALUE);
			fail( "Excepción no lanzada" );
		} catch (ArithmeticException e) {}
	}

	// O de otra manera...
	@Test (expected = ArithmeticException.class)
	public void testFactorialNegativo2() {
		FuncionParaTest.factorial(-1); 
	}
	
	@Test (expected = ArithmeticException.class)
	public void testFactorialNoCabeEnInt() {
		System.out.println( FuncionParaTest.factorial(13) );
	}

	@Test
	public void testFactorialCorrecto() {
		int[] y = {
				1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600
		};
		for (int x=0; x<=12; x++) {
			System.out.println( x );
			assertEquals("Valor incorrecto de factorial", y[x], FuncionParaTest.factorial(x) );
		}
		/*
1! = 1
2! = 2
3! = 6
4! = 24
5! = 120
6! = 720
7! = 5040
8! = 40320
9! = 362880
10! = 3628800
11! = 39916800
12! = 479001600
13! = 6227020800
14! = 87178291200
15! = 1307674368000
16! = 20922789888000
17! = 355687428096000
18! = 6402373705728000
19! = 121645100408832000
20! = 2432902008176640000
		 */
	}

}
