package es.deusto.prog3.cap06;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class EjemploInvoke extends JFrame implements ActionListener
{
	private final JPanel contentPane, panelDerecho;
	private final JButton btnSalir;

	public EjemploInvoke()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Ejemplo ventana");
		setBounds(0, 0, 600, 450);
		setMinimumSize(new Dimension(720, 400));
		setLocationRelativeTo(null);
		// No debería hacerse el setVisible hasta que la ventana esté construida
		setVisible(true);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{220, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		JPanel panelIzquierdo = new JPanel();
		GridBagConstraints gbc_panelIzquierdo = new GridBagConstraints();
		gbc_panelIzquierdo.insets = new Insets(0, 0, 0, 5);
		gbc_panelIzquierdo.fill = GridBagConstraints.BOTH;
		gbc_panelIzquierdo.gridx = 0;
		gbc_panelIzquierdo.gridy = 0;
		contentPane.add(panelIzquierdo, gbc_panelIzquierdo);
		GridBagLayout gbl_panelIzquierdo = new GridBagLayout();
		gbl_panelIzquierdo.columnWidths = new int[]{0, 0};
		gbl_panelIzquierdo.rowHeights = new int[]{50, 25, 0, 20, 0};
		gbl_panelIzquierdo.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelIzquierdo.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panelIzquierdo.setLayout(gbl_panelIzquierdo);

		JLabel lblNombreJugador = new JLabel("Prueba");
		lblNombreJugador.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNombreJugador.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombreJugador.setOpaque(true);
		lblNombreJugador.setBackground(new Color(30, 144, 255));
		lblNombreJugador.setForeground(Color.BLACK);
		lblNombreJugador.setFont(new Font("Serif", Font.BOLD, 25));
		GridBagConstraints gbc_lblNombreJugador = new GridBagConstraints();
		gbc_lblNombreJugador.insets = new Insets(0, 0, 5, 0);
		gbc_lblNombreJugador.fill = GridBagConstraints.BOTH;
		gbc_lblNombreJugador.gridx = 0;
		gbc_lblNombreJugador.gridy = 0;
		panelIzquierdo.add(lblNombreJugador, gbc_lblNombreJugador);

		JLabel lblNiveles = new JLabel("NIVELES");
		lblNiveles.setOpaque(true);
		lblNiveles.setBackground(Color.LIGHT_GRAY);
		lblNiveles.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNiveles.setHorizontalAlignment(SwingConstants.CENTER);
		lblNiveles.setForeground(Color.BLACK);
		lblNiveles.setFont(new Font("Serif", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNiveles = new GridBagConstraints();
		gbc_lblNiveles.insets = new Insets(0, 0, 5, 0);
		gbc_lblNiveles.fill = GridBagConstraints.BOTH;
		gbc_lblNiveles.gridx = 0;
		gbc_lblNiveles.gridy = 1;
		panelIzquierdo.add(lblNiveles, gbc_lblNiveles);

		JScrollPane scrollPaneListaNiveles = new JScrollPane();
		GridBagConstraints gbc_scrollPaneListaNiveles = new GridBagConstraints();
		gbc_scrollPaneListaNiveles.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneListaNiveles.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneListaNiveles.gridx = 0;
		gbc_scrollPaneListaNiveles.gridy = 2;
		panelIzquierdo.add(scrollPaneListaNiveles, gbc_scrollPaneListaNiveles);

		String [] niveles = new String [] {"  Nivel 1", "  Nivel 2", "  Nivel 3", "  Nivel 4", "  Nivel 5", "  Nivel 6", "  Nivel 7", "  Nivel 8"};
		JList<String> listaNiveles = new JList<String>(niveles);
		listaNiveles.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		listaNiveles.setForeground(Color.BLACK);
		listaNiveles.setFont(new Font("Serif", Font.PLAIN, 20));
		scrollPaneListaNiveles.setViewportView(listaNiveles);

		btnSalir = new JButton("SALIR");
		btnSalir.setBackground(SystemColor.inactiveCaption);
		btnSalir.setFont(new Font("Serif", Font.PLAIN, 20));
		btnSalir.setForeground(Color.BLACK);
		btnSalir.addActionListener(this);
		GridBagConstraints gbc_btnSalir = new GridBagConstraints();
		gbc_btnSalir.fill = GridBagConstraints.BOTH;
		gbc_btnSalir.gridx = 0;
		gbc_btnSalir.gridy = 3;
		panelIzquierdo.add(btnSalir, gbc_btnSalir);

		JScrollPane scrollPaneDerecho = new JScrollPane();
		GridBagConstraints gbc_scrollPaneDerecho = new GridBagConstraints();
		gbc_scrollPaneDerecho.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneDerecho.gridx = 1;
		gbc_scrollPaneDerecho.gridy = 0;
		contentPane.add(scrollPaneDerecho, gbc_scrollPaneDerecho);

		panelDerecho = new JPanel();
		panelDerecho.setPreferredSize(new Dimension( 400, 4000 ));
		scrollPaneDerecho.setViewportView(panelDerecho);
		panelDerecho.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		añadirComponentesPanelDerecho();
	}

	private void añadirComponentesPanelDerecho()
	{
		for(int i=0; i<500; i++)
		{
			panelDerecho.add(new JButton("Botón "+(i+1)));
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource() == btnSalir)
		{
			this.dispose();
		}
	}
	
	private static boolean testNoMeDatiempo = true;
	public static void main(String[] args)
	{
		if (testNoMeDatiempo) {
			// Se crea y se muestra la ventana desde el hilo principal, no el de swing
			// El setVisible() activa la ventana y al hilo de swing para que la pinte...
			// ...con lo que swing a veces muestra la ventana a medias 
			// (mientras el main todavía la está construyendo)
			new EjemploInvoke();
		} else {
			// En cambio así aunque el setVisible se haga muy pronto, 
			// así construye toda la ventana antes de pintarla
			// (setVisible marca que la ventana se pinte, pero hasta que no acaba el 
			// trabajo de este código COMPLETO Swing no puede ponerse a pintar)
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					new EjemploInvoke();
				}
			} );
		}
		Utils.muestraThreadsActivos();	
	}
}
