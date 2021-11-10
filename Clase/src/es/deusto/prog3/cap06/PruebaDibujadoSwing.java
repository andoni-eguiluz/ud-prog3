package es.deusto.prog3.cap06;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class PruebaDibujadoSwing {
	static MiPanel mp = new MiPanel();
	public static void main(String[] args) {
        crearYMostrarGUI(); 
        while (true) {
        	mp.repaint();   // Redibuja cada 100 milisegundos (10 / segundo aprox)
        	try { Thread.sleep(30); } catch (InterruptedException e) {}
        }
    }
    private static void crearYMostrarGUI() {
        try {
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
		} catch (Exception e) {
			e.printStackTrace();
		}
        JFrame f = new JFrame("Prueba de dibujado de Swing");
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(mp);
        f.setSize(350,200);
        f.setVisible(true);
    } 
}

class MiPanel extends JPanel {
	private static final long serialVersionUID = -3478139411193386930L;
	CirculoVerde circuloVerde = new CirculoVerde();
    public MiPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                mueveCirculo(e.getX(),e.getY());
            }
        });
        addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e){
                mueveCirculo(e.getX(),e.getY());
            }
        });
    }

    private void mueveCirculo(int x, int y){
        // Current square state, stored as final variables 
        // to avoid repeat invocations of the same methods.
        final int CURR_X = circuloVerde.getX();
        final int CURR_Y = circuloVerde.getY();
        final int CURR_W = circuloVerde.getAnchura();
        final int CURR_H = circuloVerde.getAltura();
        final int OFFSET = 1;
        if ((CURR_X!=x) || (CURR_Y!=y)) {
            // Actualizar coordenadas
            circuloVerde.setX(x);
            circuloVerde.setY(y);
            // Hay dos sitios que repintar: el antiguo en el que estaba el círculo
            // (con el fondo)
            repaint( CURR_X, CURR_Y, CURR_W+OFFSET, CURR_H+OFFSET );
            // Y el nuevo al que va (con el círculo)
            repaint( circuloVerde.getX(), circuloVerde.getY(), 
                     circuloVerde.getAnchura()+OFFSET, circuloVerde.getAltura()+OFFSET);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(250,200);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Dibujado del padre (hacer siempre esto)
        // Dibujar un mensaje que se mueve cada décima de segundo un pixel
        circuloVerde.dibujaCirculo(g); // Dibujar círculo
        g.drawString("Soy un panel dibujado por programa!",10,20 + (int)((System.currentTimeMillis()/10)%100) );
    }  
}

class CirculoVerde {
    private int xPos = 50;
    private int yPos = 50;
    private int width = 20;
    private int height = 20;
    public void setX(int xPos){ 
        this.xPos = xPos;
    }
    public int getX(){
        return xPos;
    }
    public void setY(int yPos){
        this.yPos = yPos;
    }
    public int getY(){
        return yPos;
    }
    public int getAnchura(){
        return width;
    } 
    public int getAltura(){
        return height;
    }
    public void dibujaCirculo(Graphics g){
        g.setColor( Color.GREEN );
        g.fillOval( xPos,yPos,width,height );
        g.setColor( Color.BLACK );
        g.drawOval( xPos,yPos,width,height );  
    }

}
