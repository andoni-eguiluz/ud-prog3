package es.deusto.prog3.cap05;

import static es.deusto.prog3.cap05.AnalisisEjecucion.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import es.deusto.prog3.cap06.pr0506resuelta.ExploradorObjetos;
import es.deusto.prog3.utils.IUConsola;

public class PruebasListas {
		
	private static void pruebaEspacioListas( int numPrueba ) {
//		Long lll = null;
//		Integer iii = null;
		visuMem( "Al inicio", true );
		visuMem( "Tras inicialización", true );
//		lll = new Long(5);
//		visuMem( "Tras inicialización long", true );
//		System.out.println( lll );
//		iii = new Integer(5);
//		visuMem( "Tras inicialización integer", true );
//		System.out.println( iii );
		if (numPrueba==1 || numPrueba==3) {
			ArrayList<Object> l = new ArrayList<Object>();
			visuMem( "Tras crear ArrayList (tam = " + l.size() + ", cap = " + UtilidadArrayList.getArrayListCapacity(l) + ")", true, true );
			int num = 75;
			for (int i=0; i<num; i++) {
				int numAdds = 1;
				for (int j=0; j<numAdds; j++) l.add(null);
				visuMem( "Tras meter " + numAdds + " datos nulos en ArrayList (tam = " + l.size() + ", cap = " + UtilidadArrayList.getArrayListCapacity(l) + ")", false );
			}
		}
		if (numPrueba==2 || numPrueba==3) {
			LinkedList<Object> l2 = new LinkedList<Object>();
			visuMem( "Tras crear LinkedList (tam = " + l2.size() + ")", true, true );
			int num = 20;
			for (int i=0; i<num; i++) {
				int numAdds = 1;
				for (int j=0; j<numAdds; j++) l2.add(null);
				visuMem( "Tras meter " + numAdds + " datos nulos en LinkedList (tam = " + l2.size() + ")", false );
			}
		}
	}

		@SuppressWarnings("unused")
		private static void recorreLista( List<Object> l, int numVeces ) {
			System.out.print( "   Recorriendo lista..." );
			for (int i=0; i<numVeces; i++) {
				for (int j=0; j<l.size(); j++) {
					if (j%10000==0) System.out.print( " " + j );  // Feedback en procesos largos
					Object o = l.get(j);  // Nada, simplemente recorrer al derecho
				}
				for (int j=l.size()-1; j>=0; j--) {
					if (j%10000==0) System.out.print( " " + j );  // Feedback en procesos largos
					Object o = l.get(j);  // Nada, simplemente recorrer al revés
				}
			}
			System.out.println();
		}
		@SuppressWarnings("unused")
		private static void recorreListaConIterador( LinkedList<Object> l, int numVeces ) {
			System.out.print( "   Recorriendo lista..." );
			for (int i=0; i<numVeces; i++) {
				Iterator<Object> itL = l.iterator();
				int rec = 0;
				while (itL.hasNext()) {
					if (rec%10000==0) System.out.print( " " + rec );  // Feedback en procesos largos
					rec++;
					Object o = itL.next();  // Nada, simplemente recorrer al derecho
				}
				ListIterator<Object>itL2 = l.listIterator( l.size() );
				rec = l.size();
				while (itL2.hasPrevious()) {
					if (rec%10000==0) System.out.print( " " + rec );  // Feedback en procesos largos
					rec--;
					Object o = itL2.previous();  // Nada, simplemente recorrer al derecho
				}
			}
			System.out.println();
		}
		private static void cogePosicionesLista( List<Object> l, int numVeces ) {
			System.out.print( "   Cogiendo posiciones lista " + numVeces + " veces... " );
			for (int i=0; i<numVeces; i++) {
				if (i%1000==0) System.out.print( " " + i );
				l.get(0);
				l.get(l.size()/2);
				l.get(l.size()-1);
			}
			System.out.println();
		}
		
		private static void cargaLista( List<Object> l, int numEls ) {
			for (int i=0; i<numEls; i++) l.add( null );
		}
	
		private static void cargaListaInsMedio( List<Object> l, int numEls ) {
			for (int i=0; i<numEls; i++) l.add( i/2, null );
		}
	
		private static void cargaListaInsInicio( List<Object> l, int numEls ) {
			for (int i=0; i<numEls; i++) l.add( 0, null );
		}
	
	private static void pruebaTiempoListas( int numPrueba ) {
		ArrayList<Object> al = new ArrayList<>();
		LinkedList<Object> ll = new LinkedList<>();
		ArrayList<Object> al2 = new ArrayList<>();
		LinkedList<Object> ll2 = new LinkedList<>();
		int tamListas = 100000;
		visuMem( null, false );
		if (numPrueba==1 || numPrueba==3) {
			visuTiempo( null );
			cargaLista( al, tamListas );
			visuTiempo( "  Tras cargar ArrayList de " + tamListas );
			visuMem( "  Y la memoria usada", true );
		}
		if (numPrueba==2 || numPrueba==3) {
			visuTiempo( null );
			cargaLista( ll, tamListas );
			visuTiempo( "  Tras cargar LinkedList de " + tamListas );
			visuMem( "  Y la memoria usada", true );
		}
		System.out.println();
		visuMem( null, false );
		if (numPrueba==1 || numPrueba==3) {
			visuTiempo( null );
			cargaListaInsMedio( al2, tamListas );
			visuTiempo( "  Tras cargar ArrayList de " + tamListas + " insertando en medio" );
			visuMem( "  Y la memoria usada", true );
		}
		if (numPrueba==2 || numPrueba==3) {
			visuTiempo( null );
			cargaListaInsMedio( ll2, tamListas );
			visuTiempo( "  Tras cargar LinkedList de " + tamListas + " insertando en medio" );
			visuMem( "  Y la memoria usada", true );
		}
		System.out.println();
		visuMem( null, false );
		if (numPrueba==1 || numPrueba==3) {
			al2.clear();
			visuTiempo( null );
			cargaListaInsInicio( al2, tamListas );
			visuTiempo( "  Tras cargar ArrayList de " + tamListas + " insertando en el inicio" );
			visuMem( "  Y la memoria usada", true );
		}
		if (numPrueba==2 || numPrueba==3) {
			ll2.clear();
			visuTiempo( null );
			cargaListaInsInicio( ll2, tamListas );
			visuTiempo( "  Tras cargar LinkedList de " + tamListas + " insertando en el inicio" );
			visuMem( "  Y la memoria usada", true );
		}
		System.out.println();
		if (numPrueba==1 || numPrueba==3) {
			visuTiempo( null );
			recorreLista( al, 1 );
			visuTiempo( "  Tras recorrer arriba y abajo ArrayList de " + tamListas );
		}
		if (numPrueba==2 || numPrueba==3) {
			visuTiempo( null );
			recorreLista( ll, 1 );
			visuTiempo( "  Tras recorrer arriba y abajo LinkedList de " + tamListas );
		}
		if (numPrueba==2 || numPrueba==3) {
			recorreListaConIterador( ll, 1 );
			visuTiempo( "  Tras recorrer arriba y abajo con ITERADOR LinkedList de " + tamListas );
		}
		System.out.println();
		int numVeces = 10000;
		if (numPrueba==1 || numPrueba==3) {
			visuTiempo( null );
			cogePosicionesLista( al, numVeces );
			visuTiempo( "  Tras coger " + numVeces + " posiciones numéricas sueltas de ArrayList de " + tamListas );
		}
		if (numPrueba==2 || numPrueba==3) {
			visuTiempo( null );
			cogePosicionesLista( ll, numVeces );
			visuTiempo( "  Tras coger " + numVeces + " posiciones numéricas sueltas de LinkedList de " + tamListas );
		}
		System.out.println();
		System.out.println( String.format("Tamaño en memoria del AL: %,d bytes.", ExploradorObjetos.getTamanyoObjeto( al ) ) );
		System.out.println( String.format("Tamaño en memoria del LL: %,d bytes.", ExploradorObjetos.getTamanyoObjeto( ll ) ) );
	}
	
	public static void main(String[] args) {
		IUConsola.lanzarConsolaEnIU( null );  // Para ver la consola en ventana y lanzar las pruebas
		// int numPrueba = 1;  // Ver ArrayList
		// int numPrueba = 2;  // Ver LinkedList
		// int numPrueba = 3;  // Ver ArrayList y LinkedList
		// pruebaListas( numPrueba );
		IUConsola.addBoton( "ArrayList", () -> pruebaListas(1) );
		IUConsola.addBoton( "LinkedList", () -> pruebaListas(2) );
		IUConsola.addBoton( "Ambos", () -> pruebaListas(3) );
	}
	
	private static void pruebaListas( int numPrueba ) {
		Thread t = new Thread() {
			public void run() {
				System.out.println( "Test 1 - mejor ver el segundo que es más aproximado en uso de memoria");
				pruebaEspacioListas( numPrueba );
				System.out.println();
				pruebaTiempoListas( numPrueba );
				System.out.println();
				System.out.println( "Test 2 - VER ESTE:");
				pruebaEspacioListas( numPrueba );
				System.out.println();
				pruebaTiempoListas( numPrueba );
			}
		};
		t.start();
	}
	
}
