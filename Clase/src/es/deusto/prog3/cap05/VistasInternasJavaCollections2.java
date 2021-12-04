package es.deusto.prog3.cap05;

import java.util.*;
import es.deusto.prog3.cap06.pr0506resuelta.gui.VentanaBancoDePruebas;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.*;

/** Permite una vista interna de las estructuras lineales básicas
 * cambiable al ir añadiendo elementos
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
public class VistasInternasJavaCollections2 {

	public static void main(String[] args) {
		JFrame f = new JFrame( "Vista de estructuras" );
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		f.setSize( 320, 200 );
		String[] datos = { "ArrayList", "LinkedList", "HashSet", "TreeSet", "HashMap", "TreeMap" };
		JList<String> lista = new JList<>( datos );
		f.add( lista, BorderLayout.WEST );
		JButton bAnyadir = new JButton( "Añadir dato" );
		JPanel pBots = new JPanel();
		pBots.add( bAnyadir );
		f.add( pBots, BorderLayout.SOUTH );
		lista.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					iniciarEstructura( lista.getSelectedValue() );
				}
			}
		});
		bAnyadir.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				anyadirAEstructura();
			}
		});
		f.setVisible( true );
	}

	private static Object estructura = null;
	private static void iniciarEstructura( String est ) {
		if (est!=null) {
			switch (est) {
				case "ArrayList": {
					estructura = new ArrayList<String>();
					break;
				}
				case "LinkedList": {
					estructura = new LinkedList<String>();
					break;
				}
				case "HashSet": {
					estructura = new HashSet<String>();
					break;
				}
				case "TreeSet": {
					estructura = new TreeSet<String>();
					break;
				}
				case "HashMap": {
					estructura = new HashMap<String,String>();
					break;
				}
				case "TreeMap": {
					estructura = new TreeMap<String,String>();
					break;
				}
			}
			vistaEstructura( estructura, est + " vacío" );
		}
	}

	private static void anyadirAEstructura() {
		if (estructura!=null) {
			if (estructura instanceof ArrayList) {
				ArrayList<String> l = (ArrayList<String>) estructura;
				l.add( new String( "Dato" + l.size() ) );
			} else if (estructura instanceof LinkedList) {
				LinkedList<String> l = (LinkedList<String>) estructura;
				l.add( new String( "Dato" + l.size() ) );
			} else if (estructura instanceof HashSet) {
				HashSet<String> l = (HashSet<String>) estructura;
				l.add( new String( "Dato" + l.size() ) );
			} else if (estructura instanceof TreeSet) {
				TreeSet<String> l = (TreeSet<String>) estructura;
				l.add( new String( "Dato" + l.size() ) );
			} else if (estructura instanceof HashMap) {
				HashMap<String,String> l = (HashMap<String,String>) estructura;
				l.put( new String( "Clave" + l.size() ), new String( "Valor" + l.size() ) );
			} else if (estructura instanceof TreeMap) {
				TreeMap<String,String> l = (TreeMap<String,String>) estructura;
				l.put( new String( "Clave" + l.size() ), new String( "Valor" + l.size() ) );
			}
			vistaEstructura( estructura, "Añadido un elemento" );
		}
	}
	
	private static VentanaBancoDePruebas vista = null;
	private static void vistaEstructura( Object o, String titulo ) {
		if (vista==null) {
			vista = new VentanaBancoDePruebas( o, titulo );
			vista.setVisible( true );
		} else {
			vista.setTitle( titulo );
			vista.setObjeto( o );
		}
	}

}
