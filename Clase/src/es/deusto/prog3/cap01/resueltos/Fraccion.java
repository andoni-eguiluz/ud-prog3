package es.deusto.prog3.cap01.resueltos;

/** Ejercicio 1.8.a  resuelto (clase a probar)
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class Fraccion {
	
	/** Método de prueba de la clase fracción - implementado mejor en la clase de test TestFraccion
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		Fraccion f = new Fraccion( 2, 6 ); // 2/6 = 1/3
		System.out.println( f );
		Fraccion f2 = new Fraccion( 1, -5 ); // 1/-5 = -1/5
		System.out.println( f2 );
		System.out.println( suma(f,f2) ); // 1/3 + (-1/5) = 2/15
		System.out.println( resta(f,f2) ); // 1/3 - (-1/5) = 8/15
		System.out.println( multiplica(f,f2) ); // 1/3 * (-1/5) = -1/15
		System.out.println( divide(f,f2) ); // 1/3 / (-1/5) = -5/3
		Fraccion f3 = new Fraccion( 0, 6 ); // 0/6 = 0/1  (cualquier 0 da igual el denominador)
		System.out.println( f3 );
		System.out.println( suma(f,f3) ); // 1/3 + 0/1 = 1/3
		System.out.println( resta(f,f3) ); // 1/3 - 0/1 = 1/3
		System.out.println( multiplica(f,f3) ); // 1/3 * 0/1 = 0/11
		System.out.println( divide(f,f3) ); // Error! división por 0
		Fraccion f4 = new Fraccion( 6, 0 ); // Error! Fracción irracional
		System.out.println( f4 );
		Fraccion f5 = new Fraccion( 0, 0 ); // Error! Fracción más irracional
		System.out.println( f5 );
	}
	
	private int num; // Numerador
	private int den; // Denominador
	
	/** Crea y simplifica (si procede) una fracción
	 * @param num	Numerador
	 * @param den	Denominador
	 * @throws ArithmeticException	Fracción incorrecta si denominador es cero
	 */
	public Fraccion( int num, int den ) throws ArithmeticException {
		if (den==0) throw new ArithmeticException( "Número no racional: fracción con denominador 0" );
		this.num = num;
		this.den = den;
		simplifica();
	}
	
	/** Devuelve el numerador de una fracción
	 * @return	Valor del numerador (si hay signo lo tiene el numerador)
	 */
	public int getNum() { return num; }
	
	/** Devuelve el denominador de una fracción
	 * @return	Valor del denominador (positivo)
	 */
	public int getDen() { return den; }
	
	/** Devuelve el valor real de la fracción
	 * @return	Numerador entre denominador (con precisión double)
	 */
	public double getVal() {
		return 1.0 * num / den;
	}
	
	/** Suma dos fracciones
	 * @param f1	Fracción 1
	 * @param f2	Fracción 2
	 * @return	Fracción resultado de la suma de las fracciones 1 y 2
	 */
	public static Fraccion suma( Fraccion f1, Fraccion f2 ) {
		return new Fraccion( f1.getNum()*f2.getDen() + f2.getNum()*f1.getDen(), f1.getDen()*f2.getDen() );
	}
	
	/** Resta dos fracciones
	 * @param f1	Fracción 1
	 * @param f2	Fracción 2
	 * @return	Fracción resultado de la resta f1 - f2
	 */
	public static Fraccion resta( Fraccion f1, Fraccion f2 ) {
		return new Fraccion( f1.getNum()*f2.getDen() - f2.getNum()*f1.getDen(), f1.getDen()*f2.getDen() );
	}
	
	/** Multiplica dos fracciones
	 * @param f1	Fracción 1
	 * @param f2	Fracción 2
	 * @return	Fracción resultado de la resta f1 - f2
	 */
	public static Fraccion multiplica( Fraccion f1, Fraccion f2 ) {
		return new Fraccion( f1.getNum()*f2.getNum(), f1.getDen()*f2.getDen() );
	}
	
	/** Divide dos fracciones
	 * @param f1	Fracción 1
	 * @param f2	Fracción 2
	 * @return	Fracción resultado de la división f1 / f2
	 * @throws ArithmeticException	Si f2 es cero, división incorrecta
	 */
	public static Fraccion divide( Fraccion f1, Fraccion f2 ) throws ArithmeticException {
		if (f2.getNum()==0) throw new ArithmeticException( "Número no racional: fracción con denominador 0" );
		return new Fraccion( f1.getNum()*f2.getDen(), f1.getDen()*f2.getNum() );
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Fraccion) {
			return num==((Fraccion)obj).num && den==((Fraccion)obj).den;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return num+"/"+den;
	}
	
	private void simplifica() {
		int mcd = mcd( num, den );
		num /= mcd;
		den /= mcd;
		if (den<0) {  // Pone el signo siempre en el numerador (5/-3 pasa a ser -5/3 por ejemplo)
			den *= -1;
			num *= -1;
		}
		if (num==0) {  // La fracción con numerador 0 es siempre 0, el denominador da igual, se pone a 1
			den = 1;
		}
	}
	
	// Utilidad: Devuelve el máximo común divisor de 2 números positivos
	// private quitado para que se pueda usar desde el test - no hace falta probarla, pero se puede usar para probar otras cosas
	static int mcd( int num1, int num2 ) {
		int divisor = num1<num2 ? num1 : num2;  // El número mayor que hay que probar para calcular el MCD es el más pequeño. De ahí hacia abajo
		int mcd = 1;
		while (divisor > 1) {
			if (num1%divisor == 0 && num2%divisor==0) {  // Es divisor común: se añade y se reducen los números
				mcd *= divisor;
				num1 /= divisor;
				num2 /= divisor;
			}
			divisor--;
		}
		return mcd;
	}
	
}
