package es.deusto.prog3.cap06.pr0506resuelta;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.swing.tree.*;

import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;

/** Utilidad de exploración de objetos y sus atributos, usando reflectividad (accede a todos los atributos, aunque sean privados).
 * Suministra información de uso (teórico) de memoria, en una máquina virtual Java de 32 bits.
 * Para una representación visual en ventana, ver clase VentanaBancoDePruebas (ejemplo en el main)
 * @author Andoni Eguíluz Morán
 * Facultad de Ingeniería - Universidad de Deusto
 */
public class ExploradorObjetos {
	
	private static String SEPR = "  "; // "\t";
	private static String MARCA_OBJETO_YA_VISITADO = "<#> ";
	private static String MARCA_OBJETO_PARCIAL = "<...> ";
	private static String TEXTO_METADATOS = "<Metadatos: Ref.Clase + Flags + Locks>";
	private static int TAM_METADATOS = 12;
	private static int NUM_MAX_LLAMADAS = 20;  // Número de llamadas recursivas que se permiten - más se truncan
	private static int PUNTO_CONTROL = 17;  // Punto de control de las llamadas recursivas para ver iteratividad (menor que NUM_MAX_LLAMADAS para posibilitar que haya recursividades en estructuras con objetos más internos)
	private static int NUM_MAX_ITERATIVIDAD_VISUAL = 50;  // Número de recorrido iterativo máximo que se permite visualizar - más se computa pero no se visualiza
	private static int NUM_MAX_ITERATIVIDAD = 10000000;  // Número de recorrido iterativo máximo que se permite - más se truncan (ni se visualizan ni se computan)
	private static int NUM_MAX_SEPRS = 10; // Número de llamadas recursivas que se visualizan con separador - más se mantiene la separación y se indica "...(n) " al principio
	private static int NUM_REC_COINC = 8;  // Número de llamadas recursivas coincidentes en atributo y espacio para hacer iteratividad
	private static int NUM_MAX_DATO_ARRAY = 16;  // Número máximo de valores de array que se muestra en valor de atributo
	private static boolean ANYADIR_ATRIBUTOS_HEREDADOS = true;  // true para añadir atributos heredados al desglosar cada clase, false solo atributos propios
	private static int TAM_REF_EN_BYTES = 4;
	private static HashMap<String, Integer> TAM_EN_BYTES;
	static {
		TAM_EN_BYTES = new HashMap<>();
		TAM_EN_BYTES.put( "byte", 1 );
		TAM_EN_BYTES.put( "short", 2 );
		TAM_EN_BYTES.put( "int", 4 );
		TAM_EN_BYTES.put( "long", 8 );
		TAM_EN_BYTES.put( "float", 4 );
		TAM_EN_BYTES.put( "double", 8 );
		TAM_EN_BYTES.put( "char", 2 );
		TAM_EN_BYTES.put( "boolean", 1 );   // Es un bit pero la unidad mínima de asignación es un byte
	}

	private static int numLlamadas = 0;  // Para truncar la recursividad
	private static HashSet<Object> objetosYaRecorridos = new HashSet<>();
	private static int tamObjeto = 0;
	private static String camposAMostrar = "";
	private static String ret = "";
	private static String atsInstancia = "";
	private static String atsStatic = "";
	private static String atsErr = "";
	private static boolean hayErr = false;
	private static boolean soloCalcTamanyo = false;      // Utilizado para recorridos de devolver tamaño en lugar de crear String
	private static DefaultMutableTreeNode raiz = null;   // Utilizado para recorridos de crear árbol en lugar de crear String
	private static UltimasN<AtributoYEspacio> ultimosAtEsp = new UltimasN<>(NUM_REC_COINC);
	
	/** Calcula un string para visualización de atributos (y sus valores, y sus tamaños en memoria) del objeto indicado
	 * @param sep	Separador (de tabulación) que se usa en cada línea de atributo
	 * @param o	Objeto que se quiere analizar (diferente de null)
	 * @param tbStatic	true si se quieren visualizar también los atributos estáticos
	 * @param mostrarTam true si se quiere mostrar el tamaño ocupado por el objeto y sus atributos
	 * @return	String maquetado con toda la información
	 */
	public static String atributosYValoresToString( String sep, Object o, boolean tbStatic, boolean mostrarTam ) {
		ret = "";
		atsInstancia = !tbStatic ? "" : "Atributos de instancia:\n";
		atsStatic = "Atributos de clase (static):\n";
		atsErr = "Errores de acceso a atributos:\n";
		numLlamadas = 0;
		tamObjeto = 0;
		hayErr = false;
		// objetosYaRecorridos = new HashSet<>();  // Hecho ya una vez en inicialización
		// ultimosAtEsp = new UltimasN<>(5);  // Hecho ya en inicialización
		atributosYValoresToStringRec( sep, o, tbStatic, mostrarTam, raiz );
		objetosYaRecorridos.clear();
		ultimosAtEsp.clear();
		ret += atsInstancia;
		if (tbStatic) ret += atsStatic;
		if (hayErr) ret += atsErr;
		if (mostrarTam) ret += (tamDe(tamObjeto) + "\n");
		camposAMostrar = "";  // Prepara para la siguiente ejecución
		return ret;
	}
	
	/** Devuelve un despliegue de los atributos (y sus valores, y sus tamaños en memoria) de un objeto,
	 * explorando en profundidad todos los objetos contenidos en él.
	 * @param sep	Separador (de tabulación) que se usa en cada línea de atributo
	 * @param o	Objeto que se quiere analizar (diferente de null)
	 * @param tbStatic	true si se quieren visualizar también los atributos estáticos
	 * @param mostrarTam true si se quiere mostrar el tamaño ocupado por el objeto y sus atributos
	 * @param camposAMostrar String que incluye los nombres de campos que queremos mostrar (el resto no aparecen en el resultado).<p>
	 * Por ejemplo, si queremos los atributos root y left podríamos pasar "root#left" a este parámetro.
	 * @return	String maquetado con toda la información
	 */
	public static String atributosYValoresToString( String sep, Object o, boolean tbStatic, boolean mostrarTam, String camposAMostrar ) {
		ExploradorObjetos.camposAMostrar = camposAMostrar;
		return atributosYValoresToString( sep, o, tbStatic, mostrarTam );
	}
	
	/** Devuelve el tamaño de un objeto en memoria en bytes, explorando en profundidad todos los objetos contenidos en él.
	 * Devuelve el tamaño completo de todos los objetos referenciados.
	 * @param o	Objeto que se quiere analizar (diferente de null)
	 * @return	Tamaño en bytes del objeto
	 */
	public static int getTamanyoObjeto(Object o) {
		soloCalcTamanyo = true;
		atributosYValoresToString( "", o, false, true );
		soloCalcTamanyo = false;
		return tamObjeto;
	}
	
	/** Devuelve un despliegue de los atributos (y sus valores, y sus tamaños en memoria) de un objeto,
	 * introduciéndolos en un modelo de datos de árbol, visualizable en un JTree.
	 * @param o	Objeto que se quiere analizar (diferente de null)
	 * @param atributosAVer	Lista de atributos que se quieren visualizar, incluidos en un string
	 * @return	Tamaño en bytes del objeto
	 */
	public static DefaultTreeModel atributosYValoresToTree(Object o, String atributosAVer ) {
		raiz = new DefaultMutableTreeNode( o.toString() );
		DefaultTreeModel arbol = new DefaultTreeModel( raiz );
		atributosYValoresToString( "", o, false, false, atributosAVer );
		raiz = null;
		return arbol;
	}
	
	/** Devuelve un despliegue de los atributos (y sus valores, y sus tamaños en memoria) de un objeto,
	 * introduciéndolos en un modelo de datos de árbol, visualizable en un JTree.
	 * @param o	Objeto que se quiere analizar (diferente de null)
	 * @return	Tamaño en bytes del objeto
	 */
	public static DefaultTreeModel atributosYValoresToTree(Object o) {
		raiz = new DefaultMutableTreeNode( o.toString() );
		DefaultTreeModel arbol = new DefaultTreeModel( raiz );
		atributosYValoresToString( "", o, false, true );
		raiz = null;
		return arbol;
	}
		// Método de utilidad para los árboles
		private static DefaultMutableTreeNode anyadirHijoArbol( DefaultMutableTreeNode raiz, String nuevoHijo ) {
			DefaultMutableTreeNode nuevo = new DefaultMutableTreeNode( nuevoHijo );
			raiz.add( nuevo );
			return nuevo;
		}
	
		private static String tamDe(int tam) { return "{" + tam + "} "; }
	private static void atributosYValoresToStringRec( String sep, Object o, boolean tbStatic, boolean mostrarTam, DefaultMutableTreeNode raiz ) {
		DefaultMutableTreeNode nodoHijo = null;
		String miSep = sep+SEPR;
		String sepSgte = miSep;
		if (numLlamadas > NUM_MAX_SEPRS) {
			miSep = sep + "...(" + numLlamadas + ") ";
			sepSgte = sep;
		}
		Class<?> c = o.getClass();
		Field fs[] = c.getDeclaredFields();
		// Añadido de campos heredados
		if (ANYADIR_ATRIBUTOS_HEREDADOS) {
			Class<?> cParent = c.getSuperclass();
			while (cParent!=null && !cParent.getName().equals("Object")) {
				Field[] masAtributos = cParent.getDeclaredFields();
				Field[] nuevoArray = new Field[fs.length + masAtributos.length];
				System.arraycopy( fs, 0, nuevoArray, 0, fs.length);
				System.arraycopy( masAtributos, 0, nuevoArray, fs.length, masAtributos.length);
				fs = nuevoArray;
				cParent = cParent.getSuperclass();
			}
		}
		// Fin añadido de campos heredados
		tamObjeto += TAM_METADATOS;
		if (mostrarTam && !soloCalcTamanyo) {
			if (raiz==null)
				atsInstancia += (miSep + tamDe(TAM_METADATOS) + TEXTO_METADATOS + "\n" );
			else
				anyadirHijoArbol( raiz, tamDe(TAM_METADATOS) + TEXTO_METADATOS );
		}
		if (c.getName().startsWith("[")) {  // El objeto es un array. Ojo entonces no tiene atributos - es un objeto especial
			String tipoArr = tipoArray(o);
			int tamArr = tamArray(o);
			int tamanyo = 0;
			if (TAM_EN_BYTES.containsKey(tipoArr))
				tamanyo = tamArr * TAM_EN_BYTES.get( tipoArr );
			else
				tamanyo = tamArr * TAM_REF_EN_BYTES;
			String tam = (mostrarTam) ? tamDe(tamanyo) : "";
			tamObjeto += tamanyo;
			if (camposAMostrar.equals("") && !soloCalcTamanyo)
				if (raiz==null)
					atsInstancia += (miSep + tam + " <" + tamArr + ">*" + aString(o, o.getClass()) + "\n");
				else
					nodoHijo = anyadirHijoArbol( raiz, tam + " <" + tamArr + ">*" + aString(o, o.getClass()) );
			if (!TAM_EN_BYTES.containsKey(tipoArr)) {
				// for de proceso de elementos de array si son objetos (y no son nulls)
				numLlamadas++;
				for (int i=0; i<tamArr; i++) {
					DefaultMutableTreeNode nodoNieto = null;
					Object oArr = valorArray(o,i);
					if (oArr!=null) {
						String tamA = (mostrarTam) ? "{} " : "";
						if (objetosYaRecorridos.contains( oArr )) {  // Si ya está recorrido, no insistimos :-)
							if (camposAMostrar.equals("") || camposAMostrar.contains("[]"))
								if (!soloCalcTamanyo)
									if (raiz==null)
										atsInstancia += (miSep + SEPR + tamA + "[" + i + "] = " + MARCA_OBJETO_YA_VISITADO + aString(oArr, oArr.getClass()) + "\n" );
									else
										anyadirHijoArbol( nodoHijo, tamA + "[" + i + "] = " + MARCA_OBJETO_YA_VISITADO + aString(oArr, oArr.getClass()) );
						} else {
							if (camposAMostrar.equals("") || camposAMostrar.contains("[]"))
								if (!soloCalcTamanyo)
									if (raiz==null)
										atsInstancia += (miSep + SEPR + tamA + "[" + i + "] = " + aString(oArr, oArr.getClass()) + "\n" );
									else
										nodoNieto = anyadirHijoArbol( nodoHijo, tamA + "[" + i + "] = " + aString(oArr, oArr.getClass()) );
							objetosYaRecorridos.add( oArr );
							atributosYValoresToStringRec( sepSgte+SEPR, oArr, tbStatic, mostrarTam, nodoNieto );
						}
					}
				}
				numLlamadas--;
			}
		} else {  // Objeto normal - con atributos
			for (Field f : fs) {
				try {
					f.setAccessible(true);
					if (Modifier.isStatic(f.getModifiers())) {
						if (tbStatic)
							if (camposAMostrar.equals("") || camposAMostrar.contains(f.getName()))
								if (raiz==null)
									atsStatic += (miSep + f.getName() + " = " + aString(f.get( null ), f.getType()) + "\n" );
								else
									anyadirHijoArbol( raiz, "STATIC " + f.getName() + " = " + aString(f.get( null ), f.getType()) );
					} else {
						// atsInstancia += ( "#" + f.getType().toString().substring(6) );
						// atsInstancia += ( "# - #" + c.getName().toString() + "#\n" );
						String tam = "";
						if (mostrarTam) {  // Cálculo tamaño
							int tamanyo = 0;
							if (f.getType().toString().startsWith("class [")) { //&& f.get(o)!=null) {
								tamanyo = TAM_REF_EN_BYTES;  // Un array es una referencia
							} else if (f.getType().toString().startsWith("class ")) {
								tamanyo = TAM_REF_EN_BYTES;
							} else if (f.getType().toString().startsWith("interface [")) { //&& f.get(o)!=null) {
								tamanyo = TAM_REF_EN_BYTES;  // Un array es una referencia
							} else if (f.getType().toString().startsWith("interface "))
								tamanyo = TAM_REF_EN_BYTES;
							else if (TAM_EN_BYTES.containsKey(f.getType().toString()))
								tamanyo = TAM_EN_BYTES.get( f.getType().toString() );
							else {
								if (f.get(o)!=null) {
									System.err.println( "TAMAÑO DESCONOCIDO!!!");
									System.err.println( "Tipo -> " + f.getType().toString() );
									System.err.println( "Objeto -> " + f.get(o) );
								}
								tamanyo = 4;
							}
							tam = tamDe(tamanyo);
							tamObjeto += tamanyo;
						}
						numLlamadas++;
						if (f.getType().toString().startsWith("class ") && f.get(o)!=null ||
						    f.getType().toString().startsWith("interface ") && f.get(o)!=null) {
							//	Si quisiéramos diferenciar el objeto con definición recursiva directa
							//	if (f.getType().toString().substring(6).equals( c.getName().toString() )) {
							if (objetosYaRecorridos.contains( f.get(o) )) {  // Si ya está recorrido, no insistimos :-)
								if (camposAMostrar.equals("") || camposAMostrar.contains(f.getName()))
									if (!soloCalcTamanyo)
										if (raiz==null)
											atsInstancia += (miSep + tam + f.getName() + " = " + MARCA_OBJETO_YA_VISITADO + aString(f.get( o ), f.getType()) + "\n" );
										else
											anyadirHijoArbol( raiz, tam + f.getName() + " = " + MARCA_OBJETO_YA_VISITADO + aString(f.get( o ), f.getType()) );
							} else {
								if (camposAMostrar.equals("") || camposAMostrar.contains(f.getName()))
									if (!soloCalcTamanyo)
										if (raiz==null) {
											atsInstancia += (miSep + tam + f.getName() + " = " + aString(f.get( o ), f.getType()) + "\n" );
										} else {
											nodoHijo = anyadirHijoArbol( raiz,  tam + f.getName() + " = " + aString( f.get( o ), f.getType()) );
										}
								objetosYaRecorridos.add( f.get(o) );
								int difEspacio = -1;  // Variable que sólo es != -1 si hay iteratividad en vez de recursividad (ver código subsiguiente)
								if (numLlamadas == PUNTO_CONTROL) {
									// System.out.println( "##" + ultimosAtEsp );
									if (ultimosAtEsp.getEquals()!=null && ultimosAtEsp.getEquals().atributo.equals(f.getName()) && f.get(o)!=null ) {
										// La recursividad n-1 es del mismo campo que tenemos ahora
										// 1.- Comprobamos que el espacio se haya ido aumentando de forma coherente
										int espacioIni = ultimosAtEsp.get(1).espacio;
										difEspacio = ultimosAtEsp.get(0).espacio - espacioIni;
										for (int i=2; i<ultimosAtEsp.size(); i++) {
											if (difEspacio != espacioIni - ultimosAtEsp.get(i).espacio) {
												difEspacio = -1; break;
											} else {
												espacioIni = ultimosAtEsp.get(i).espacio;
											}
										}
										// 2.- si es así, iniciamos proceso iterativo
										if (difEspacio!=-1) {
											// Proceso iterativo en vez de recursivo!!
											miSep = sep + SEPR;
											Object ref = f.get(o);  // Referencia inicial
											if (!soloCalcTamanyo)
												if (raiz==null)
													atsInstancia += (miSep + "MOSTRADO ITERATIVAMENTE POR EXCESO DE RECURSIVIDAD (Información parcial, cálculo de espacio correcto)\n");
												else
													anyadirHijoArbol( raiz, "MOSTRADO ITERATIVAMENTE POR EXCESO DE RECURSIVIDAD (Información parcial, cálculo de espacio correcto)" );
											String sTam = mostrarTam ? tamDe(difEspacio) : "";
											boolean errNoRecursivo = true;
											int numIt = NUM_MAX_LLAMADAS;
											while (ref != null) {
												Class<?> c2 = ref.getClass();
												Field fs2[] = c2.getDeclaredFields();
												errNoRecursivo = true;
												for (Field f2 : fs2) {
													f2.setAccessible(true);
													if (f2.getName().equals(f.getName())) {
														errNoRecursivo = false;
														ref = f2.get(ref);
														objetosYaRecorridos.add( ref );
														if (numIt<NUM_MAX_ITERATIVIDAD_VISUAL && !soloCalcTamanyo) {
															if (raiz==null)
																atsInstancia += (miSep + "...(" + numIt + ") " + sTam + f2.getName() + " = " + MARCA_OBJETO_PARCIAL + aString(ref, f2.getType()) + "\n" );
															else
																anyadirHijoArbol( raiz, "...(" + numIt + ") " + sTam + f2.getName() + " = " + MARCA_OBJETO_PARCIAL + aString(ref, f2.getType()) );
														}
														tamObjeto += difEspacio;
													}
												}
												if (errNoRecursivo) ref = null;
												if (numIt>=NUM_MAX_ITERATIVIDAD) {
													ref = null;
													break;
												}
												numIt++;
											}
											if (!soloCalcTamanyo) {
												if (raiz==null) {
													if (numIt>=NUM_MAX_ITERATIVIDAD_VISUAL)
														atsInstancia += (miSep + "(...) REFERENCIAS ADICIONALES NO MOSTRADAS POR EXCESO DE TAMAÑO (cálculo de espacio correcto)\n");
													if (numIt>=NUM_MAX_ITERATIVIDAD)
														atsInstancia += (miSep + "ERROR: ALCANZADO TAMAÑO MAXIMO = " + numIt + " (Información parcial, cálculo total de espacio INCORRECTO)\n");
													if (errNoRecursivo)
														atsInstancia += (miSep + "ERROR EN RECURSIVIDAD (Información incorrecta: enlace de objetos no coherente en algún punto)\n");
												} else {
													if (numIt>=NUM_MAX_ITERATIVIDAD_VISUAL)
														anyadirHijoArbol( raiz, "(...) REFERENCIAS ADICIONALES NO MOSTRADAS POR EXCESO DE TAMAÑO (cálculo de espacio correcto)\n");
													if (numIt>=NUM_MAX_ITERATIVIDAD)
														anyadirHijoArbol( raiz, "ERROR: ALCANZADO TAMAÑO MAXIMO = " + numIt + " (Información parcial, cálculo total de espacio INCORRECTO)\n");
													if (errNoRecursivo)
														anyadirHijoArbol( raiz, "ERROR EN RECURSIVIDAD (Información incorrecta: enlace de objetos no coherente en algún punto)\n");
												}
											}
										}
									}
								}
								if (numLlamadas > NUM_MAX_LLAMADAS) {
									if (!soloCalcTamanyo)
										if (raiz==null)
											atsInstancia += (miSep + "TRUNCADO POR EXCESO DE RECURSIVIDAD\n");
										else
											anyadirHijoArbol( raiz, "TRUNCADO POR EXCESO DE RECURSIVIDAD" );
								} else if (difEspacio==-1) {
									ultimosAtEsp.push( new AtributoYEspacio( f.getName(), tamObjeto ));
									atributosYValoresToStringRec( sepSgte, f.get(o), tbStatic, mostrarTam, (nodoHijo==null?raiz:nodoHijo) );
									if (ultimosAtEsp.size()>0) ultimosAtEsp.pop();
								}
							}
						} else {
							if (camposAMostrar.equals("") || camposAMostrar.contains(f.getName()))
								if (!soloCalcTamanyo)
									if (raiz==null)
										atsInstancia += (miSep + tam + f.getName() + " = " + aString(f.get( o ), f.getType()) + "\n" );
									else
										anyadirHijoArbol( raiz, tam + f.getName() + " = " + aString(f.get( o ), f.getType()) );
						}
						numLlamadas--;
					}
	//				Otra manera de hacerlo:
	//				try {
	//					atsStatic += ("  " + f.getName() + " = " + aString(f.get( null )) + "\n" );
	//				} catch (NullPointerException e2) {
	//					atsInstancia += ("  " + f.getName() + " = " + aString(f.get( o )) + "\n" );
	//				}
				} catch (Exception e) {
					if (camposAMostrar.equals("") || camposAMostrar.contains(f.getName()))
						if (!soloCalcTamanyo)
							if (raiz==null)
								atsErr += ( miSep + f.getName() + " (Error " + e.getClass().getName() + " / " + e.getMessage() + ")\n");
							else
								anyadirHijoArbol( raiz, "[ERROR!] " + f.getName() + " (Error " + e.getClass().getName() + " / " + e.getMessage() + ")" );
					hayErr = true;
					e.printStackTrace();
				}
			}
		}
	}

		// Devuelve el tipo base del array o
		private static String tipoArray( Object o ) {
			// o.getClass().getSimpleName() es "tipo[]"
			return o.getClass().getSimpleName().substring(0, o.getClass().getSimpleName().length()-2 );
		}
		// Devuelve el número de elementos del array o
		private static int tamArray( Object o ) {
			if (o instanceof char[]) {
				char[] arr = (char[]) o;
				return arr.length;
			} else if (o instanceof boolean[]) {
				boolean[] arr = (boolean[]) o;
				return arr.length;
			} else if (o instanceof int[]) {
				int[] arr = (int[]) o;
				return arr.length;
			} else if (o instanceof long[]) {
				long[] arr = (long[]) o;
				return arr.length;
			} else if (o instanceof float[]) {
				float[] arr = (float[]) o;
				return arr.length;
			} else if (o instanceof double[]) {
				double[] arr = (double[]) o;
				return arr.length;
			} else if (o instanceof byte[]) {
				byte[] arr = (byte[]) o;
				return arr.length;
			} else if (o instanceof short[]) {
				short[] arr = (short[]) o;
				return arr.length;
			} else if (o instanceof Object[]) {
				Object[] arr = (Object[]) o;
				return arr.length;
			} 
			return 0;
		}
		// Devuelve el valor del elemento i del array o
		private static Object valorArray( Object o, int i ) {
			Object[] arr = (Object[]) o;
			return arr[i];
		}
	
		// Devuelve la representación (toString()) del objeto. Si es un array,
		// en su lugar lo devuelve maquetado [ valor1 valor2 valor3 ... ]
		private static String aString( Object o, Class<?> tipo ) {
			String ret;
			if (o==null) {
				ret = "(" + tipo.getName().toString() + ") null";
			} else if (!tipo.toString().startsWith("class")) {
				ret = "(" + tipo.getName().toString() + ") " + o.toString();
			} else if (o instanceof Object[]) {
				Object[] arr = (Object[]) o;
				ret = "(" + o.getClass().getCanonicalName() + ") [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof char[]) {
				char[] arr = (char[]) o;
				ret = "(char[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof boolean[]) {
				boolean[] arr = (boolean[]) o;
				ret = "(boolean[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof int[]) {
				int[] arr = (int[]) o;
				ret = "(int[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof long[]) {
				long[] arr = (long[]) o;
				ret = "(long[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof float[]) {
				float[] arr = (float[]) o;
				ret = "(float[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof double[]) {
				double[] arr = (double[]) o;
				ret = "(double[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof byte[]) {
				byte[] arr = (byte[]) o;
				ret = "(byte[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else if (o instanceof short[]) {
				short[] arr = (short[]) o;
				ret = "(byte[]) [";
				for (int i=0; i<arr.length; i++) {
					ret += arr[i];
					if (i+1<arr.length) ret += " ";
					if (i==NUM_MAX_DATO_ARRAY) { ret += "..."; break; }
				}
				ret += "]";
			} else {
				ret = "(" + o.getClass().getCanonicalName() + ") " + o.toString();
			}
			return ret;
		}

	/** Prueba de la clase de acceso a atributos
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println( "Prueba de ejecución de clase ExploradorObjetos." );
		System.out.println();
		
		Integer o = new Integer(7);
		System.out.println( "INTEGER 7\n" + atributosYValoresToString( "", o, true, false ) );
		
		String tit = "ARRAY DE ENTEROS CON 7\n";
		int[] ar = new int[10];
		ar[0] = 7;
		System.out.println( tit + atributosYValoresToString( "", ar, false, true ) );

		tit = "ARRAYLIST DE ENTEROS CON 7,2,3\n";
		ArrayList<Integer> al = new ArrayList<>();
		al.add( new Integer(7) );
		al.add( new Integer(2) );
		al.add( new Integer(3) );
		System.out.println( tit + atributosYValoresToString( "", al, false, true ) );
		
		tit = "LINKEDLIST DE ENTEROS CON 7,2,3\n";
		LinkedList<Integer> ll = new LinkedList<>();
		ll.add( new Integer(7) );
		ll.add( new Integer(2) );
		ll.add( new Integer(3) );
		System.out.println( tit + atributosYValoresToString( "", ll, false, true ) );
		
		tit = "LINKEDLIST DE ENTEROS CON 150 VALORES\n";
		ll = new LinkedList<>();
		for (int i=1; i<=150; i++) ll.add( new Integer(i) );
		System.out.println( tit + atributosYValoresToString( "", ll, false, true ) );
		System.out.println( "Tamaño de linkedlist: " + getTamanyoObjeto(ll) + " bytes.");
		System.out.println();

		tit = "ARRAY DE STRINGS\n";
		String[] sts = { "Hola", "Adiós", "Vale" };
		System.out.println( tit + atributosYValoresToString( "", sts, false, true ) );
		
		tit = "HASHMAP DE STRINGS-ENTEROS CON 2 VALORES\n";
		HashMap<String,Integer> hm = new HashMap<>();
		hm.put( "Clave1", 1 );
		hm.put( "Clave2", 2 );
		System.out.println( tit + atributosYValoresToString( "", hm, false, true ) );

		tit = "HASHSET DE STRINGS (vacío, cap. inicial 2, con 1, 2, 3 y 4 elementos)\n";
		HashSet<String> hs = new HashSet<>(2);
		System.out.println( tit + atributosYValoresToString( "", hs, false, true ) );
		hs.add( "Andoni" );
		System.out.println( tit + atributosYValoresToString( "", hs, false, true ) );
		hs.add( "Elena" );
		System.out.println( tit + atributosYValoresToString( "", hs, false, true ) );
		hs.add( "Rosa" );
		System.out.println( tit + atributosYValoresToString( "", hs, false, true ) );
		hs.add( "Asier" );
		System.out.println( tit + atributosYValoresToString( "", hs, false, true ) );

		tit = "HASHSET DE STRINGS SOLO CON LA ESTRUCTURA\n";
		System.out.println( tit + atributosYValoresToString( "", hs, false, false, 
				"map#table#hash#key#value#next#size#loadFactor#[]" ) );
		// Ad hoc para este ejemplo...
		System.out.println( "2553165 % 8 = " + (2553165%8) + "   -    63559309 % 8 = " + (63559309%8) );
		System.out.println();
		
		tit = "TREESET DE STRINGS\n";
		TreeSet<String> ts = new TreeSet<>();
		ts.add( "Andoni" );
		ts.add( "Elena" );
		ts.add( "Asier" );
		ts.add( "Rosa" );
		System.out.println( tit + atributosYValoresToString( "", ts, false, true ) );

		tit = "TREESET DE STRINGS SOLO CON LA ESTRUCTURA\n";
		System.out.println( tit + atributosYValoresToString( "", ts, false, false, 
				"root#key#left#right" ) );
		
		VentanaBancoDePruebas vEj = new VentanaBancoDePruebas( ts, "Ejemplo de treeset en ventana" );
		vEj.setVisible( true );
		vEj.setAtributosAVer( "m#root#size#key#value#left#right#parent#color" );
		vEj.setObjeto( ts );

		Object oo = new Object();
		Integer i = 5;
		System.out.println( "Objeto entero: " + i.toString() );
		System.out.println( "  Su dirección de memoria:" + Integer.toHexString( System.identityHashCode( i ) ) );  // Método dependiente de implementación de la Java VM
		System.out.println( "  Cantidad de memoria utilizada: " + getTamanyoObjeto( i ) + " bytes." );
		System.out.println( "Objeto Object: " + oo.toString() );
		System.out.println( "  Su dirección de memoria:" + Integer.toHexString( System.identityHashCode( oo ) ) );
		System.out.println( "  Cantidad de memoria utilizada: " + getTamanyoObjeto( oo ) + " bytes." );
		
	}

	private static class AtributoYEspacio {
		public String atributo;
		public int espacio;
		public AtributoYEspacio( String atributo, int espacio ) {
			this.atributo = atributo;
			this.espacio = espacio;
		}
		@Override
		public boolean equals( Object o ) {
			if (!(o instanceof AtributoYEspacio)) return false;
			AtributoYEspacio ae = (AtributoYEspacio) o;
			return (atributo.equals(ae.atributo));
		}
		@Override
		public String toString() {
			return atributo + ":{" + espacio + "}";
		}
	}
	
	private static class UltimasN<T> {
		int n;
		LinkedList<T> l;
		public UltimasN(int n) {
			this.n = n;
			this.l = new LinkedList<>();
		}
		public void push(T t) {
			l.push(t);
			if (l.size()>n) l.removeLast();
		}
		public T pop() {
			return l.pop();
		}
		public T get( int i ) {
			return l.get(i);
		}
		public int size() {
			return l.size();
		}
		@SuppressWarnings("unused")
		public boolean areAllEqual() {
			if (l.size()<1) return false;
			T first = l.get(0);
			for (int i=1; i<l.size(); i++) {
				if (!first.equals(l.get(i))) return false;
			}
			return true;
		}
		public T getEquals() {
			if (l.size()<1) return null;
			T first = l.get(0);
			for (int i=1; i<l.size(); i++) {
				if (!first.equals(l.get(i))) return null;
			}
			return first;
		}
		public void clear() {
			l.clear();
		}
		@Override
		public String toString() {
			return l.toString();
		}
	}
	
}
