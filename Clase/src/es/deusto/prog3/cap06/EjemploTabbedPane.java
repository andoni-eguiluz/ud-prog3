package es.deusto.prog3.cap06;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class EjemploTabbedPane extends JFrame {

   public static void main( String args[] )
   {
      EjemploTabbedPane v = new EjemploTabbedPane();
      v.setVisible( true );
   }

   public EjemploTabbedPane() {
      super( "Ejemplo de JTabbedPane" );
      setSize( 800, 600 );
      setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
      
      // Crear objeto contenedor JTabbedPane 
      JTabbedPane panelConFichas = new JTabbedPane();
      panelConFichas.setFont( new Font( "Times New Roman", Font.PLAIN, 20 ) );
      
      // establecer panel1 y agregarlo al objeto JTabbedPane 
      String nombre = "DATOS PERSONALES";
      JLabel etiqueta1 = new JLabel( nombre, SwingConstants.CENTER );
      JPanel panel1 = new JPanel();
      panel1.setBackground( Color.CYAN );
      panel1.add( etiqueta1 ); 
      panelConFichas.addTab( "Datos Personales", null, panel1, nombre ); 
      
      // establecer panel2 y agregarlo al objeto JTabbedPane
      nombre = "DATOS DOMICILIARIOS";
      JLabel etiqueta2 = new JLabel( nombre, SwingConstants.CENTER );
      JPanel panel2 = new JPanel();
      panel2.setBackground( Color.YELLOW );
      panel2.add(etiqueta2 );
      panelConFichas.addTab( "Domicilio", null, panel2, nombre ); 

      // establecer panel3 y agregarlo al objeto JTabbedPane
      nombre = "DATOS BANCARIOS";
      JLabel etiqueta3 = new JLabel( nombre, SwingConstants.CENTER );
      JPanel panel3 = new JPanel();
      panel3.setBackground( Color.GREEN );
      panel3.add(etiqueta3 );
      panelConFichas.addTab( "Bancos", null, panel3, nombre );
      
      // establecer panel4 y agregarlo al objeto JTabbedPane
      nombre = "IDENTIDAD DIGITAL";
      JLabel etiqueta4 = new JLabel( nombre, SwingConstants.CENTER );
      JPanel panel4 = new JPanel();
      panel4.setBackground( Color.ORANGE );
      panel4.add( etiqueta4 );
      panelConFichas.addTab( "Panel tarifas del cliente", null, panel4, nombre );

      // agregar JTabbedPane al contenedor
      getContentPane().add( panelConFichas );

     }

} 