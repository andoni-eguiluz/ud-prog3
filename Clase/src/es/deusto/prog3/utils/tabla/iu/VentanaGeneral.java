package es.deusto.prog3.utils.tabla.iu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class VentanaGeneral extends JFrame {
	
	public static Color COLOR_GRIS_CLARITO = new Color( 220, 220, 220 );
	
	private JDesktopPane desktop;
	private JLabel lMensaje = new JLabel( " " );
	private JMenu menuVentanas;
	private JMenu menuAcciones;
	private ArrayList<JInternalFrame> misSubventanas;
	private Runnable runEnCierre;
	
	public VentanaGeneral() {
		misSubventanas = new ArrayList<>();
		// Configuración general
		setTitle( "Ventana General" );
		setSize( 1200, 800 ); // Tamaño por defecto
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// Creación de componentes y contenedores
		desktop = new JDesktopPane();
		add( desktop, BorderLayout.CENTER );
		// setContentPane( desktop );
		lMensaje.setOpaque( true );
		add( lMensaje, BorderLayout.SOUTH );
		// Eventos
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (runEnCierre!=null) runEnCierre.run();
			}
		});
		// Menú y eventos
		JMenuBar menuBar = new JMenuBar();
		menuVentanas = new JMenu( "Ventanas" ); menuVentanas.setMnemonic( KeyEvent.VK_V );
		menuBar.add( menuVentanas );
		menuAcciones = new JMenu( "Acciones" ); menuAcciones.setMnemonic( KeyEvent.VK_A );
		menuBar.add( menuAcciones );
		setJMenuBar( menuBar );
	}
	
	public void setEnCierre( Runnable runnable ) {
		runEnCierre = runnable;
	}
	
		private ActionListener alMenu = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String vent = e.getActionCommand();
				for (JInternalFrame vd : misSubventanas) if (vd.getName().equals( vent )) { vd.setVisible( true ); vd.moveToFront(); return; }
			}
		};
	public void addVentanaInterna( JInternalFrame f, String codVentana ) {
		desktop.add( f );
		JMenuItem menuItem = new JMenuItem( codVentana ); 
		menuItem.setActionCommand( codVentana );
		menuItem.addActionListener( alMenu );
		menuVentanas.add( menuItem );	
		misSubventanas.add( f );
		f.setName( codVentana );
	}
	
	public void setMensaje( String mens, Color... colorFondo ) {
		Color fondo = (colorFondo.length>0) ? colorFondo[0] : COLOR_GRIS_CLARITO; 
		if (mens==null || mens.isEmpty()) mens = " ";
		lMensaje.setText( mens );
		lMensaje.setBackground( fondo );
	}
	
	public void setMensajeSinCambioColor( String mens ) {
		if (mens==null || mens.isEmpty()) mens = " ";
		lMensaje.setText( mens );
	}
	
	public void addMenuAccion( String textoMenu, ActionListener accion ) {
		JMenuItem menuItem = new JMenuItem( textoMenu );
		menuItem.setActionCommand( textoMenu );
		menuItem.addActionListener( accion );
		menuAcciones.add( menuItem );
	}
	
	public ArrayList<JInternalFrame> getSubventanas() {
		return misSubventanas;
	}
	
}
