package es.deusto.prog3.cap04.ejemploAproxSuc;
import java.awt.*;

/** Clase que permite crear y gestionar rectángulos
 * Realizada sin herencia (copiar-y-pegar desde Pelota)
 */
public class Asteroide extends ObjetoEspacial {
	private double radio;
	
	/** Constructor de bloque con datos
	 * @param x	Coordenada x de la esquina superior izquierda del bloque
	 * @param y	Coordenada y de la esquina superior izquierda del bloque
	 * @param radio Radio del asteroide
	 * @param color	Color del bloque
	 */
	public Asteroide(double x, double y, double radio, Color color ) {
		super( x, y, color );
		this.radio = radio;
	}
	
	/** Constructor de bloque con datos mínimos. Crea un bloque azul
	 * @param x	Coordenada x de la esquina sup izqda
	 * @param y	Coordenada y de la esquina sup izqda
	 * @param radio Radio del asteroide
	 */
	public Asteroide(double x, double y, double radio) {
		this( x, y, radio, Color.blue );  // Reutilizar el constructor más específico desde otro
	}
	
	public double getRadio() {
		return radio;
	}

	public void setRadio(double radio) {
		this.radio = radio;
	}

	/** Calcula el volumen del bloque suponiendo que es esférico
	 * @return	Volumen del bloque
	 */
	@Override
	public double getVolumen() {
		return 4.0 / 3.0 * Math.PI * radio * radio * radio;
	}
	
	/** Calcula el área del bloque partiendo de su información
	 * @return	Area del bloque suponiendo un círculo perfecto
	 */
	@Override
	public double getArea() {
		return Math.PI * radio * radio;
	}
	
	/** Dibuja el bloque en una ventana, en el color correspondiente (por defecto, negro)
	 * @param v	Ventana en la que dibujar el bloque
	 */
	@Override
	public void dibuja( VentanaGrafica v ) {
		v.dibujaCirculo( x, y, radio, 1.5f, color );
		super.dibuja( v );  // Posible dibujo de velocidad
	}
	
	/** Borra el bloque en una ventana
	 * @param v	Ventana en la que borrar el bloque
	 */
	@Override
	public void borra( VentanaGrafica v ) {
		v.borraCirculo( x, y, radio, 1.5f );
	}

	@Override
	public void corrigeChoqueVertical( VentanaGrafica v, boolean dibujar ) {
		if (y<radio) {  // Toca por abajo
			if (dibujar) borra( v );
			setY( radio );
			if (dibujar) dibuja( v );
		} else {
			double dondeToca = v.getAltura() - radio;  // Coordenada y en la que se toca el suelo, a la que hay que ajustar
			if (dondeToca >= y) return;  // Si no está pasando el suelo, no se hace nada 
			if (dibujar) borra( v );
			setY( dondeToca );
			if (dibujar) dibuja( v );
		}
	}
	
	@Override
	public void corrigeChoqueLateral( VentanaGrafica v, boolean dibujar ) {
		if (x<radio) {  // Choque izquierda
			if (dibujar) borra( v );
			setX( radio );
			if (dibujar) dibuja( v );
		} else if (x>v.getAnchura()-radio) {  // Choque derecha
			if (dibujar) borra( v );
			setX( v.getAnchura() - radio );
			if (dibujar) dibuja( v );
		}
	}
	
	/** Detecta el choque del bloque con los bordes de la ventana
	 * @param v	Ventana con la que probar el choque
	 * @return	Devuelve un número formado por la suma de: 0 si no choca, 1 si choca con la izquierda, 2 con la derecha, 4 arriba, 8 abajo.
	 */
	@Override
	public int chocaConBorde( VentanaGrafica v ) {
		int ret = 0;
		if (x-radio<=0) ret += 1;
		if (x+radio>=v.getAnchura()) ret += 2;
		if (y-radio<=0) ret += 4;
		if (y+radio>=v.getAltura()) ret += 8;
		return ret;
	}
	
	/** Detecta el choque del bloque con otra
	 * @param bloque2	Bloque con el que probar el choque
	 * @return	Devuelve null si no chocan, un vector con forma de punto indicando el ángulo y amplitud del choque sobre el bloque en curso, 
	 * 			(0,0) si el bloque 2 incluye al bloque 1
	 */
	@Override
	public Point chocaConObjeto( ObjetoEspacial objeto2 ) {
		if (objeto2 instanceof Asteroide) {
			Asteroide pelota2 = (Asteroide) objeto2;
			Point p = new Point();
			p.setLocation( pelota2.x - x, pelota2.y - y );
			double dist = p.distance(0,0);
			double moduloChoque = radio + pelota2.radio - dist;
			if (moduloChoque < 0) return null;
			p.setLocation( p.getX() * moduloChoque / dist, p.getY() * moduloChoque / dist );
			return p;
		// TODO
		// } else if (objeto2 instanceof ...) {
		} else {
			return null;
		}
	}
	
	/** Comprueba si el bloque incluye a un punto dado
	 * @param punto	Punto a chequear
	 * @return	true si el punto está dentro del bloque, false en caso contrario
	 */
	@Override
	public boolean contieneA( Point punto ) {
		double dist = punto.distance( x, y );
		return dist <= radio;
	}

	@Override
	public String toString() {
		return String.format( "Asteroide %1s (%2$7.2f,%3$7.2f) R=%4$5.1f Vel.=(%5$6.3f,%6$6.3f)", nombre, x, y, radio, velocidadX, velocidadY );
	}
	
}
