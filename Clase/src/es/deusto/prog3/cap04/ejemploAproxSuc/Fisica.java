package es.deusto.prog3.cap04.ejemploAproxSuc;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.*;
import static java.lang.Double.*;

/** Clase con fórmulas básicas de física para simular movimiento
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class Fisica {
	
	public static double GRAVEDAD = 0.0;  // Píxels por segundo cuadrado
	
	/**	Calcula la velocidad provocada por una energía aplicada sobre un objeto
	 * @param energía aplicada, en "julixels" (kgs.*pixel^2/seg^2)<br/>
	 *  (un newtixel = trabajo realizado por una fuerza constante de un newtixel durante un pixel en la dirección de la fuerza)
	 * @param masa	Masa del objeto en "kgs."
	 * @return	Aceleración provocada, en pixels/segundo^2
	 */
	public static double calcVelocidad( double energia, double masa ) {
		// Ec = 1/2 * m * v^2  (Energía cinética)
		//   O sea v = sqrt( 2 * Ec / m )
		return Math.sqrt( 2.0 * energia / masa );
	}
	
	/**	Calcula la aceleración provocada por una fuerza sobre un objeto con una masa
	 * @param fuerza aplicada, en "newtixels" (kgs.*pixel/seg^2)<br/>
	 *  (un newtixel = fuerza que aplicada durante un segundo a una masa de 1 kg incrementa su velocidad en 1 píxel/seg)
	 * @param masa	Masa del objeto en "kgs."
	 * @return	Aceleración provocada, en pixels/segundo^2
	 */
	public static double calcAceleracion( double fuerza, double masa ) {
		return fuerza / masa;  // 2a Ley de Newton   F = m * a     (a = F/m)
	}
	
	/** Calcula el cambio de espacio, considerando un movimiento uniformemente acelerado  s(fin) = 1/2*a*t^2 + v(ini)*t + s(ini)
	 * @param espacioIni	Espacio inicial (pixels)
	 * @param tiempoMsgs	Tiempo transcurrido desde el espacio inicial (milisegundos)
	 * @param vIni	Velocidad inicial (píxels/seg)
	 * @param aceleracion	Aceleración aplicada (píxels/seg^2)
	 * @return	Nuevo espacio
	 */
	public static double calcEspacio( double espacioIni, double tiempoMsgs, double vIni, double aceleracion ) {
		return aceleracion * tiempoMsgs * tiempoMsgs * 0.0000005 + vIni * tiempoMsgs * 0.001 + espacioIni;
	}
	
	/** Calcula el cambio de espacio, considerando un movimiento uniforme (sin aceleración)   s(fin) = s(ini) + v * t
	 * @param espacioIni	Espacio inicial (pixels)
	 * @param tiempo	Tiempo transcurrido desde el espacio inicial (milisegundos)
	 * @param vIni	Velocidad inicial (píxels/seg)
	 * @param aceleracion	Aceleración aplicada (píxels/seg^2)
	 * @return	Nuevo espacio
	 */
	public static double calcEspacio( double espacioIni, double tiempo, double vIni ) {
		return vIni * tiempo * 0.001 + espacioIni;
	}
	
	/** Calcula el tiempo que falta para que un objeto llegue a un espacio determinado, con movimiento uniformemente acelerado
	 * @param vIni	Velocidad inicial (píxels/seg)
	 * @param espIni	Espacio inicial (píxels)
	 * @param aceleracion	Aceleración aplicada (píxels/seg^2)
	 * @param donde	Espacio al que llegar (píxels)
	 * @return	Tiempo que falta (segundos). Si no es posible que el objeto llegue, valor negativo.
	 */
	public static double calcTiempoHastaEspacio( double vIni, double espIni, double aceleracion, double donde ) {
		// s[objetivo] = 1/2*a*t[objetivo]^2 + v[ini]*t[objetivo] + s[ini]  |
		//     0 = 1/2*a*t[objetivo]^2 + v[ini]*t[objetivo] + (s[ini] - s[objetivo]) |  
		//        (*) Ec. segundo grado: ax2+bx+c=0  |  x = (-b +/- sqrt(b^2 - 4*a*c)) / (2*a)
		//     t[objetivo] = (-v[ini] + sqrt( v[ini]^2 - 4*1/2*a*(s[ini]-s[objetivo]) ) ) / (2*1/2*a)
		if (igualACero(aceleracion)) return calcTiempoHastaEspacio(vIni, espIni, donde);  // Si la aceleración es cero el movimiento es uniforme
		double tiempo = (-vIni + Math.sqrt(vIni*vIni - 2.0*aceleracion*(espIni-donde))) / aceleracion;
		if (Double.isNaN(tiempo)) return -1.0;  // Si no puede llegar, tiempo negativo para indicar que nunca llega
		return tiempo;
	}
	
	/** Calcula el cambio de velocidad, considerando un movimiento uniformemente acelerado    v(fin) = v(ini) + a * t
	 * @param vIni	Velocidad inicial (píxels/seg)
	 * @param tiempoMsgs	Tiempo transcurrido desde la velocidad inicial (milisegundos)
	 * @param aceleracion	Aceleración aplicada (píxels/seg^2)
	 * @return	Nueva velocidad
	 */
	public static double calcVelocidad( double vIni, double tiempoMsgs, double aceleracion ) {
		return vIni + aceleracion * 0.001 * tiempoMsgs;
	}

	/** Calcula el tiempo que falta para que un objeto llegue a un espacio determinado, con movimiento uniforme
	 * @param vIni	Velocidad inicial (píxels/seg)
	 * @param espIni	Espacio inicial (píxels)
	 * @param donde	Espacio al que llegar (píxels)
	 * @return	Tiempo que falta (segundos). Si no es posible que el objeto llegue, valor negativo.
	 */
	public static double calcTiempoHastaEspacio( double vIni, double espIni, double donde ) {
		// s[objetivo] = v[ini]*t[objetivo] + s[ini]  |
		//     t[objetivo] = (s[objetivo] - s[ini]) / v[ini]
		if (igualACero( vIni )) return -1.0;
		double tiempo = (donde - espIni) / vIni;
		if (Double.isInfinite(tiempo)) return -1.0;  // Si error aritmético, tiempo negativo para indicar que nunca llega
		return tiempo;
	}
	
	
	/** Calcula un choque elástico entre dos cuerpos
	 * @param masa1	Masa del cuerpo 1 (Kg)
	 * @param vel1	Velocidad del cuerpo 1 lineal hacia el otro cuerpo en el sentido del cuerpo 1 al cuerpo 2
	 * @param masa2	Masa del cuerpo 2 (Kg)
	 * @param vel2	Velocidad del cuerpo 2 lineal hacia el otro cuerpo en el sentido del cuerpo 1 al cuerpo 2
	 * @return
	 */
	public static double[] calcChoque( double masa1, double vel1, double masa2, double vel2 ) {
		// Fórmula de choque perfectamente elástico. Ver por ejemplo  http://www.fis.puc.cl/~rbenguri/cap4(Dinamica).pdf
		double[] velocFinal = new double[2];
		velocFinal[0] = ((masa1-masa2)*vel1 + 2*masa2*vel2)/(masa1+masa2);
		velocFinal[1] = ((masa2-masa1)*vel2 + 2*masa1*vel1)/(masa1+masa2);
		return velocFinal;
	}

	/** Calcula el choque entre dos objetos
	 * @param ventana	Ventana en la que ocurre el choque
	 * @param objeto	Objeto 1 que choca
	 * @param objeto2	Objeto 2 que choca
	 * @param milis	Milisegundos que pasan en el paso de movimiento
	 * @param visualizarChoque	true para visualizar la info del choque en la ventana y en consola
	 */
	public static void calcChoqueEntreObjetos( VentanaGrafica ventana, ObjetoEspacial objeto, ObjetoEspacial objeto2, double milis, boolean visualizarChoque ) {
		if (objeto instanceof Nave && objeto2 instanceof Nave) {
			Nave nave = (Nave) objeto;
			Nave nave2 = (Nave) objeto2;
			Point2D choque = nave.chocaConObjeto( nave2 );
			if (choque==null) return;
			if (visualizarChoque)
				System.out.println( "Choque entre " + nave + " y " + nave2 + " con vector " + choque );
			Point2D choqueLinea = new Point2D.Double( nave2.getX()-nave.getX(), nave2.getY()-nave.getY() );
			PolarPoint tangente = PolarPoint.pointToPolar( choqueLinea );
			tangente.transformaANuevoEje( Math.PI/2.0 );  // La tangente es la del choque girada 90 grados
			Point2D tangenteXY = tangente.toPoint();
			Point2D.Double velNaveXY = new Point.Double( nave.getVelocidadX(), nave.getVelocidadY() );
			Point2D.Double velNave2XY = new Point.Double( nave2.getVelocidadX(), nave2.getVelocidadY() );
			PolarPoint velNave = PolarPoint.pointToPolar( velNaveXY );
			PolarPoint velNave2 = PolarPoint.pointToPolar( velNave2XY );
			velNave.transformaANuevoEje( tangenteXY );
			velNave2.transformaANuevoEje( tangenteXY );
			Point2D nuevaVelNave = velNave.toPoint();
			Point2D nuevaVelNave2 = velNave2.toPoint();
			double[] velChoque = Fisica.calcChoque( nave.getVolumen(), nuevaVelNave.getY(), nave2.getVolumen(), nuevaVelNave2.getY() );
			nuevaVelNave.setLocation( nuevaVelNave.getX(), velChoque[0] );
			nuevaVelNave2.setLocation( nuevaVelNave2.getX(), velChoque[1] );
			if (visualizarChoque) {
				// Naves antes del choque
				nave.dibuja( ventana );
				nave2.dibuja( ventana );
				// Velocidades antes del choque
				ventana.dibujaFlecha( nave.getX(), nave.getY(), nave.getX()+velNaveXY.getX()/10, nave.getY()+velNaveXY.getY()/10, 4.0f, Color.green );
				ventana.dibujaFlecha( nave2.getX(), nave2.getY(), nave2.getX()+velNave2XY.getX()/10, nave2.getY()+velNave2XY.getY()/10, 4.0f, Color.green );
				// Eje de choque (magenta) y tangente (negro)
				ventana.dibujaLinea( 500, 200, 500+choqueLinea.getX(), 200+choqueLinea.getY(), 2.0f, Color.magenta );
				ventana.dibujaLinea( 500, 200, 500+tangenteXY.getX(), 200+tangenteXY.getY(), 2.0f, Color.black );
				// Vista de datos en consola
				System.out.println( "Cambio en choque:");
				System.out.println( "  Nave 1: " + velNaveXY + " es " + velNave + " o sea " + nuevaVelNave );
				System.out.println( "  Nave 2: " + velNave2XY + " es " + velNave2 + " o sea " + nuevaVelNave2 );
				System.out.println( "  Nueva vel nave 1: " + nuevaVelNave );
				System.out.println( "  Nueva vel nave 2: " + nuevaVelNave2 );
			}
			velNave = PolarPoint.pointToPolar(nuevaVelNave);
			velNave2 = PolarPoint.pointToPolar(nuevaVelNave2);
			velNave.transformaANuevoEje( -Math.atan2( tangenteXY.getY(), tangenteXY.getX() ) );
			velNave2.transformaANuevoEje( -Math.atan2( tangenteXY.getY(), tangenteXY.getX() ) );
			Point2D velNaveFin = velNave.toPoint();
			Point2D velNave2Fin = velNave2.toPoint();
			if (visualizarChoque) {
				// Velocidades después del choque
				ventana.dibujaFlecha( nave.getX(), nave.getY(), nave.getX()+velNaveFin.getX()/10, nave.getY()+velNaveFin.getY()/10, 4.0f, Color.red );
				ventana.dibujaFlecha( nave2.getX(), nave2.getY(), nave2.getX()+velNave2Fin.getX()/10, nave2.getY()+velNave2Fin.getY()/10, 4.0f, Color.red );
				System.out.println( "  Vel fin nave 1: " + velNaveFin );
				System.out.println( "  Vel fin nave 2: " + velNave2Fin );
			}
			nave.setVelocidad( velNaveFin );
			nave2.setVelocidad( velNave2Fin );
			if (visualizarChoque) {  // Naves tras el choque sin corrección
				// ventana.dibujaCirculo( nave.getX(), nave.getY(), nave.getTamanyo(), 2.5f, nave.getColor() );
				// ventana.dibujaCirculo( nave2.getX(), nave2.getY(), nave2.getTamanyo(), 2.5f, nave2.getColor() );
				boolean antDV = ObjetoEspacial.DIBUJAR_VELOCIDAD;
				ObjetoEspacial.DIBUJAR_VELOCIDAD = false;
				ObjetoEspacial.DIBUJAR_VELOCIDAD = antDV;
				System.out.println( "Montado exacto: " + choque );
			}
			// Corrige posición para que no se monten (en función de los avances previos)
			if (Fisica.igualACero(choque.getX()) && Fisica.igualACero(choque.getY())) { // Caso de choque estático en suelo
				double diferencia = 0.01;
				if (nave.getX() < nave2.getX()) diferencia = -diferencia;
				if (visualizarChoque) {  // Corrección x
					System.out.println( "  nave 1 - x: " + nave.getX() + " - corrección directa " + diferencia );
					System.out.println( "  nave 2 - x: " + nave2.getX() + " - corrección directa " + -diferencia );
				}
				nave.setX( nave.getX()+diferencia );  // Corrige y aleja un poquito para que no choquen
				nave2.setX( nave2.getX()-diferencia );
			}
			if (!Fisica.igualACero(choque.getX())) {
				double diferencia = 0.0;
				if (!Fisica.igualACero(nave.getAvanceX())) diferencia = Math.abs(nave.getAvanceX()) / (Math.abs(nave.getAvanceX()) + Math.abs(nave2.getAvanceX()));
				double diferencia2 = 1 - diferencia;
				if (visualizarChoque) {  // Corrección x
					System.out.println( "  nave 1 - x: " + nave.getX() + " - corrección " + diferencia );
					System.out.println( "  nave 2 - x: " + nave2.getX() + " - corrección " + diferencia2 );
				}
				nave.setX( nave.getX()-choque.getX()*diferencia*1.1 );  // Corrige y aleja un poquito para que no choquen
				nave2.setX( nave2.getX()+choque.getX()*diferencia2*1.1 );
			}
			if (!Fisica.igualACero(choque.getY())) {
				double diferencia = 0.0;
				if (!Fisica.igualACero(nave.getAvanceY())) diferencia = Math.abs(nave.getAvanceY()) / (Math.abs(nave.getAvanceY()) + Math.abs(nave2.getAvanceY()));
				double diferencia2 = 1 - diferencia;
				if (visualizarChoque) {  // Corrección y
					System.out.println( "  nave 1 - y: " + nave.getY() + " - corrección " + diferencia );
					System.out.println( "  nave 2 - y: " + nave2.getY() + " - corrección " + diferencia2 );
				}
				nave.setY( nave.getY()-choque.getY()*diferencia*1.1 );  // Corrige y aleja un poquito para que no choquen
				nave2.setY( nave2.getY()+choque.getY()*diferencia2*1.1 );
			}
			if (visualizarChoque) {  // Naves tras el choque con corrección
				// ventana.dibujaCirculo( nave.getX(), nave.getY(), nave.getTamanyo(), 3f, nave.getColor() );
				// ventana.dibujaCirculo( nave2.getX(), nave2.getY(), nave2.getTamanyo(), 3f, nave2.getColor() );
			}
		// } else if (objeto instanceof Nave && objeto2 instanceof ...) {
		//	calcChoqueEntreObjetos( ventana, objeto2, objeto, milis, visualizarChoque );
		// } else if (objeto instanceof ... && objeto2 instanceof Nave) {
		// ...
		} else {
			// TODO calcular choques entre otros objetos que no sean dos naves
		}
	}
	
	/** Comprueba la igualdad a cero de un valor double
	 * @param num	Valor a comprobar
	 * @return	true si está muy cerca de cero (10^-12), false en caso contrario
	 */
	public static boolean igualACero( double num ) {
		return Math.abs(num)<=1E-12;  // 1 * 10^-12
	}
	
	
	// Funciones de área de polígonos
	
    /** Devuelve el valor aproximado de área de un objeto área poligonal (no curvo) 
     * @param area	Área a calcular
     * @return	Valor aproximado del área
     */
    public static double approxAreaSinCurvas(Area area) {
    	if (area.isEmpty()) return 0.0;
        PathIterator i = area.getPathIterator(identity);
        return approxArea(i);
    }

	    private static double approxArea(PathIterator i) {
	        double a = 0.0;
	        double[] coords = new double[6];
	        double startX = NaN, startY = NaN;
	        Line2D segment = new Line2D.Double(NaN, NaN, NaN, NaN);
	        while (! i.isDone()) {
	            int segType = i.currentSegment(coords);
	            double x = coords[0], y = coords[1];
	            switch (segType) {
	            case PathIterator.SEG_CLOSE:
	                segment.setLine(segment.getX2(), segment.getY2(), startX, startY);
	                a += hexArea(segment);
	                startX = startY = NaN;
	                segment.setLine(NaN, NaN, NaN, NaN);
	                break;
	            case PathIterator.SEG_LINETO:
	                segment.setLine(segment.getX2(), segment.getY2(), x, y);
	                a += hexArea(segment);
	                break;
	            case PathIterator.SEG_MOVETO:
	                startX = x;
	                startY = y;
	                segment.setLine(NaN, NaN, x, y);
	                break;
	            default:
	                throw new IllegalArgumentException("PathIterator contains curved segments");
	            }
	            i.next();
	        }
	        if (isNaN(a)) {
	            throw new IllegalArgumentException("PathIterator contains an open path");
	        } else {
	            return 0.5 * Math.abs(a);
	        }
	    }
	
	    private static double hexArea(Line2D seg) {
	        return seg.getX1() * seg.getY2() - seg.getX2() * seg.getY1();
	    }
	
	    private static final AffineTransform identity = AffineTransform.getQuadrantRotateInstance(0);
	
	
}
