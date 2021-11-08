package es.deusto.prog3.cap04.ejemploAproxSuc;
import java.awt.geom.Point2D;

public class PolarPoint {
	private double modulo;
	private double argumento;
	
	/** Constructor de punto polar
	 * @param modulo	Módulo del punto  (debe ser positivo)
	 * @param argumento	Argumento en radianes (ángulo sobre el eje OX)
	 */
	public PolarPoint( double modulo, double argumento ) {
		this.modulo = modulo;
		this.argumento = argumento;
	}
	
	
	public double getModulo() {
		return modulo;
	}

	public void setModulo(double modulo) {
		this.modulo = modulo;
	}

	public double getArgumento() {
		return argumento;
	}

	public void setArgumento(double argumento) {
		this.argumento = argumento;
	}

	
	/** Devuelve el módulo que tendría este punto polar si la X cartesiana tuviera el valor indicado, con ese mismo argumento
	 * @param x	Valor de x cartesiana
	 * @return	Módulo que tendría. Si el argumento es vertical (90 o 270º), el valor puede ser infinito.
	 */
	public double getModuloParaX( double x ) {
		return Math.abs(x/Math.cos( argumento ));
	}

	/** Devuelve el módulo que tendría este punto polar si la Y cartesiana tuviera el valor indicado, con ese mismo argumento
	 * @param y	Valor de y cartesiana
	 * @return	Módulo que tendría. Si el argumento es vertical (0 o 180º), el valor puede ser infinito.
	 */
	public double getModuloParaY( double y ) {
		return Math.abs(y/Math.sin( argumento ));
	}
	
	/** Devuelve la coordenada cartesiana X del punto polar
	 * @return	coordenada X
	 */
	public double getX() {
		return modulo*Math.cos(argumento);
	}

	/** Devuelve la coordenada cartesiana Y del punto polar
	 * @return	coordenada Y
	 */
	public double getY() {
		return modulo*Math.sin(argumento);
	}

	/** Convierte el punto polar en un punto cartesiano
	 * @return	Punto cartesiano (x,y) equivalente al polar origen
	 */
	public Point2D toPoint() {
		return new Point2D.Double( modulo*Math.cos(argumento) , modulo*Math.sin(argumento) );
	}
	
	/** Transforma el punto polar a un nuevo eje de referencia
	 * @param eje	Nuevo eje indicado por un vector representado por el punto desde el origen
	 */
	public void transformaANuevoEje( Point2D eje ) {
		double angulo = Math.atan2( eje.getY(), eje.getX() );
		this.argumento -= angulo;
	}
	
	/** Transforma el punto polar a un nuevo eje de referencia
	 * @param angulo	Ángulo que forma el nuevo eje con respecto al eje OX
	 */
	public void transformaANuevoEje( double angulo ) {
		this.argumento -= angulo;
	}
	
	@Override
	public String toString() {
		return String.format( "PolarPoint[m=%1$5.3f,ang=%2$5.3f]", modulo, argumento );
	}
	
	/** Convierte un punto cartesiano en una coordenada polar
	 * @param p1	Punto en coordenadas x,y
	 * @return	Punto polar en coordenadas módulo,argumento
	 */
	public static PolarPoint pointToPolar( Point2D p1 ) {
		return new PolarPoint( p1.distance(0,0), Math.atan2( p1.getY(), p1.getX() ) );
	}
	
	/** Método de prueba de la clase
	 * @param args	No utilizado
	 */
	public static void main(String[] args) {
		Point2D.Double p1 = new Point2D.Double( 4.0, 3.0 );
		System.out.println( p1 );
		System.out.println( p1.distance( 0, 0 ) );
		PolarPoint pp1 = pointToPolar( p1 );
		System.out.println( pp1 );
		Point2D.Double nuevoEje = new Point2D.Double( 5.0, 0.1 );
		System.out.println( "Nuevo eje: " + nuevoEje + " - ángulo " + Math.atan2( nuevoEje.getY(), nuevoEje.getX() ) );
		pp1.transformaANuevoEje( nuevoEje );
		System.out.println( "Sobre nuevo eje:" );
		System.out.println( "  " + pp1 );
		System.out.println( "  " + pp1.toPoint() );
	}
	
}
