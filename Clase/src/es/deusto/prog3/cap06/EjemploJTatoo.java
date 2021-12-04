package es.deusto.prog3.cap06;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


// Ejemplo basado en http://www.jtattoo.net/HowTo_MinFrame.html
// Descargar y enlazar JTattoo-1.6.11.jar de http://www.jtattoo.net/Download.html

@SuppressWarnings("serial")
public class EjemploJTatoo extends JFrame {

    public EjemploJTatoo() {
        super("");
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        // setup menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic( 'F' );
        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic( 'x' );
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        
        // setup widgets
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        JScrollPane westPanel = new JScrollPane(new JTree());
        JEditorPane editor = new JEditorPane("text/plain", "Hello World");
        JScrollPane eastPanel = new JScrollPane(editor);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, westPanel,eastPanel);
        splitPane.setDividerLocation(148);
        contentPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(contentPanel);
        setSize(400, 300);
        
        // add listeners
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
    } // end CTor MinFrame
    

    static int posLAF = 1;
    static JFrame ventana;
	static String[] lookAndFeels = { "com.jtattoo.plaf.aero.AeroLookAndFeel", "com.jtattoo.plaf.acryl.AcrylLookAndFeel", "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel",
			"com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", "com.jtattoo.plaf.fast.FastLookAndFeel", "com.jtattoo.plaf.graphite.GraphiteLookAndFeel",
			"com.jtattoo.plaf.hifi.HiFiLookAndFeel", "com.jtattoo.plaf.luna.LunaLookAndFeel", "com.jtattoo.plaf.mcwin.McWinLookAndFeel",
			"com.jtattoo.plaf.mint.MintLookAndFeel", "com.jtattoo.plaf.noire.NoireLookAndFeel", "com.jtattoo.plaf.smart.SmartLookAndFeel",
			"com.jtattoo.plaf.texture.TextureLookAndFeel" };
    public static void main(String[] args) {
    	int POS_X_VENTANA = 1400;
    	// select Look and Feel (elige uno)
    	// start application
    	ventana = new EjemploJTatoo();
    	try {
    		UIManager.setLookAndFeel( lookAndFeels[ 0 ] );
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	ventana.setTitle( lookAndFeels[0] );
        ventana.setLocation( POS_X_VENTANA, 32);
        ventana.setVisible( true );
    	posLAF = 1;
    	while (true) {
    		try { Thread.sleep( 2000 ); } catch (Exception e) {}
    		SwingUtilities.invokeLater(new Runnable() {
    			public void run() {
    				try {
    					UIManager.setLookAndFeel( lookAndFeels[ posLAF ] );
    					ventana.dispose();
    					ventana = new EjemploJTatoo();
    					ventana.setTitle( lookAndFeels[posLAF] );
    			        ventana.setLocation( POS_X_VENTANA, 32);
    			        ventana.setVisible( true );
    				} catch (Exception ex) {
    					ex.printStackTrace();
    				}
    			}
    		});
    		posLAF++;  if (posLAF==lookAndFeels.length) posLAF=0;
    	}
    } // end main
    
} // end class MinFrame