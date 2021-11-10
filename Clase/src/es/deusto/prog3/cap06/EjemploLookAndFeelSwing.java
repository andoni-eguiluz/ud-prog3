package es.deusto.prog3.cap06;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.metal.*;

import java.awt.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/** Ejemplo de ventanas en Swing que incluye:
 * - Cambio de look & feel
 * - Mensaje y selección de datos por JOptionPane
 * @author andoni
 */
public class EjemploLookAndFeelSwing {
	// Opciones de look & feel
	static String[] lookAndFeels = { "Metal", "Metal (Ocean)", "Windows", "System", "Motif", "Nimbus" };
	static String[] clasesLookAndFeels = { "javax.swing.plaf.metal.MetalLookAndFeel", "javax.swing.plaf.metal.MetalLookAndFeel", 
		"com.sun.java.swing.plaf.windows.WindowsLookAndFeel", UIManager.getSystemLookAndFeelClassName(), 
		"com.sun.java.swing.plaf.motif.MotifLookAndFeel", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" };

	public static void main(String[] args) {
        System.out.println( System.getProperty("java.version"));
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				crearYMostrarGUI();
			} 
		} );
    }
	
	
	/** Cambia todos los fonts por defecto del interfaz de usuario<p>
	 * Ejemplo: cambiarFontUI( new javax.swing.plaf.FontUIResource
	 *                         ("Arial", Font.BOLD, 16 ));
	 * @param font	Font a poner por defecto
	 */
	public static void cambiarFontUI(javax.swing.plaf.FontUIResource font){
		java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put( key, font );
			}
			System.out.println( key + "\t" + ((value==null)?"NULO":value.getClass().toString()) );
		}
		System.out.println( );
		System.out.println( "L&F");
		System.out.println( );
		keys = UIManager.getLookAndFeelDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put (key, font);
			}
			System.out.println( key + "\t" + ((value==null)?"NULO":value.getClass().toString()) );
		}
	}    
	
	/** Cambia el fonts por defecto del interfaz de usuario de 
	 * un componente en particular<p>
	 * Ejemplo: cambiarFontUI( "Label.font",
	 * 		new javax.swing.plaf.FontUIResource("Arial", Font.BOLD, 16 ));
	 * @param componentFontKey	Nombre de la clave del font de componente en el UI
	 * @param font	Font a poner por defecto
	 */
	public static void cambiarFontUI( String componentFontKey, javax.swing.plaf.FontUIResource font ) {
		UIManager.put(componentFontKey,font);
	}


	// Pone el look & feel al inicio del programa
	private static void ponerLookAndFeelAlInicio( String lookAndFeel ) {
		int posLaF = Arrays.asList(lookAndFeels).indexOf( lookAndFeel );
		if (posLaF >= 0) {  // Si el look and feel es una opción de las correctas
	        try {
	        	// L & F general
				UIManager.setLookAndFeel( clasesLookAndFeels[posLaF] );
                // Tema (si es Metal)
                if (lookAndFeel.startsWith( "Metal" )) {
                  if (lookAndFeel.equals("Metal"))
                     MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                  else if (lookAndFeel.equals("Metal (Ocean)"))
                     MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                  UIManager.setLookAndFeel(new MetalLookAndFeel()); 
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// Pone el look & feel a una ventana dada
	private static void ponerLookAndFeelALaVentana( String lookAndFeel, JFrame ventana ) {
		ponerLookAndFeelAlInicio( lookAndFeel );
		SwingUtilities.updateComponentTreeUI( ventana );
		// ventana.pack();
	}
	
    static JFrame miVentana;        
    private static void crearYMostrarGUI() {
    	// Cambiar fonts
    	cambiarFontUI( new javax.swing.plaf.FontUIResource("Arial",Font.BOLD,20) );
    	cambiarFontUI( "Label.font", new javax.swing.plaf.FontUIResource("Arial",Font.BOLD,30) );
    	// Poner L&F
    	ponerLookAndFeelAlInicio("Windows");
    	// Crear ventana inicial
        miVentana = new JFrame("Prueba de varias cosas de Swing");        
        // Tres JOptionPanes
        Object[] opciones = {"Sí", "No", "Cancelar" };
        int respuesta = 2;
        while (respuesta == 2) {
        	respuesta = JOptionPane.showOptionDialog( miVentana, "¿Quieres ejecutar este programa?", "Inicio", 
        		JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, "AA");
        }
        if (respuesta == 1) { miVentana.dispose(); return; }  // Si respuesta = "No", salir
        String nombreV = JOptionPane.showInputDialog( miVentana, "Introduce nombre de ventana", "Nombre", JOptionPane.QUESTION_MESSAGE );
        miVentana.setTitle( nombreV );
        String s = (String)JOptionPane.showInputDialog( miVentana, "Elige el look & feel:", "Selección", JOptionPane.PLAIN_MESSAGE, 
        		null, lookAndFeels, "Windows" );
        if ((s != null) && (s.length() > 0))
        	ponerLookAndFeelALaVentana( s, miVentana );
        // Acabar de crear y hacer visible ventana
        miVentana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        
        JPanel p1 = new JPanel();
			p1.setBorder( BorderFactory.createMatteBorder( 2, 5, 2, 5, Color.green ) );
        JPanel p2 = new JPanel();
    		Border b2 = BorderFactory.createBevelBorder( BevelBorder.RAISED );
    		p2.setBorder( b2 );
        JPanel p3 = new JPanel();
    		Border b3 = BorderFactory.createEtchedBorder();
    		p3.setBorder( b3 );
		JPanel p4 = new JPanel();
    		p4.setBorder( BorderFactory.createLineBorder( Color.red ) );
		JPanel p5 = new JPanel();
    		p5.setBorder( BorderFactory.createLoweredBevelBorder() );
		JPanel p6 = new JPanel();
    		p6.setBorder( BorderFactory.createRaisedBevelBorder() );
		JPanel p7 = new JPanel();
    		Border b7 = BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Nombre de panel"),
                    BorderFactory.createEmptyBorder(5,5,5,5));
        		p7.setBorder( b7 );
			p1.add( new JLabel("Ejemplo de panel con borde matte (verde)") );
			p2.add( new JLabel("Ejemplo de panel con borde Bevel") );
			p3.add( new JLabel("Ejemplo de panel con borde etched") );
			p4.add( new JLabel("Ejemplo de panel con borde line (rojo)") );
			p5.add( new JLabel("Ejemplo de panel con borde lowered") );
			p6.add( new JLabel("Ejemplo de panel con borde raised") );
			p7.add( new JLabel("Ejemplo de panel con borde compuesto") );
		JPanel pG = new JPanel();
			pG.setLayout( new BoxLayout( pG, BoxLayout.Y_AXIS ) );
			pG.add( p1 );
			pG.add( p2 );
			pG.add( p3 );
			pG.add( p4 );
			pG.add( p5 );
			pG.add( p6 );
			pG.add( p7 );
			
        miVentana.add( pG, "Center" );
        JButton b = new JButton( "Cambiar L&F");
        miVentana.add( b, "South" );
        b.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
		        String s = (String)JOptionPane.showInputDialog( miVentana, "Elige el look & feel:", "Selección", JOptionPane.PLAIN_MESSAGE, 
		        		null, lookAndFeels, "Windows" );
				ponerLookAndFeelALaVentana( s, miVentana );
			}
        } );
        miVentana.pack();
        miVentana.setLocationRelativeTo(null);  // Centrar en pantalla
        miVentana.setVisible(true);
    }
    
}
