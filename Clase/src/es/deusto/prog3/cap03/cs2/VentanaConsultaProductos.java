package es.deusto.prog3.cap03.cs2;

import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** Programa cliente (ejecutar tantos como se quieran después de ejecutar el servidor)
 * Ver EjemploServidor.java en este mismo paquete
 * @author andoni.eguiluz @ ingenieria.deusto.es
 */
@SuppressWarnings("serial")
public class VentanaConsultaProductos extends JFrame {

	private DefaultListModel<Producto> mProductos = new DefaultListModel<>();
	private JList<Producto> lProductos = new JList<>( mProductos );
	private JTextField tfFiltro = new JTextField( "Introduce filtro de productos y pulsa <Enter>" );
	private JTextField tfCodigo = new JTextField( "", 5 );
	private JTextField tfNombre = new JTextField( "", 20 );
	private JTextField tfPrecio = new JTextField( "", 10 );
	private JLabelAjustado lFoto = new JLabelAjustado( null );
	private JButton bNuevoProd = new JButton( "Añadir producto" );
	private JButton bConfirmarCambios = new JButton( "Confirmar cambios" );
	private JButton bCambiarFoto = new JButton( "Cambiar foto" );
	
	private ServicioPersistenciaProductos servicioPersistencia;
	
	private boolean insertando;
	private Producto productoEnInsercion;
	private Producto productoEnSeleccion;
	
	/** Construye una nueva ventana de consulta de productos
	 * @param servicio	Servicio de persistencia a utilizar. Debe estar correctamente inicializado
	 */
	public VentanaConsultaProductos( ServicioPersistenciaProductos servicio ) {
		this.servicioPersistencia = servicio;
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 500, 300 );
		setTitle( "Ventana de consulta de productos" );
		JPanel pNorte = new JPanel(); // Panel norte
			pNorte.add( new JLabel( "Filtro:" ) );
			pNorte.add( tfFiltro );
			getContentPane().add( pNorte, BorderLayout.NORTH );
		getContentPane().add( new JScrollPane(lProductos), BorderLayout.WEST ); // Lista oeste
		JPanel pPrincipal = new JPanel( new BorderLayout() ); // Panel central
			JPanel pDatos = new JPanel();
			pDatos.setLayout( new BoxLayout( pDatos, BoxLayout.Y_AXIS ) );
			JPanel pDatos1 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
				pDatos1.add( new JLabel( "Código:" ) );
				pDatos1.add( tfCodigo );
				pDatos.add( pDatos1 );
			JPanel pDatos2 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
				pDatos2.add( new JLabel( "Nombre:" ) );
				pDatos2.add( tfNombre );
				pDatos.add( pDatos2 );
			JPanel pDatos3 = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
				pDatos3.add( new JLabel( "Precio:" ) );
				pDatos3.add( tfPrecio );
				pDatos.add( pDatos3 );
			pPrincipal.add( pDatos, BorderLayout.NORTH );
			pPrincipal.add( lFoto, BorderLayout.CENTER );
			getContentPane().add( pPrincipal, BorderLayout.CENTER );
			tfCodigo.setEnabled( false );
		JPanel pBotonera = new JPanel(); // Panel inferior (botonera)
			pBotonera.add( bNuevoProd );
			pBotonera.add( bConfirmarCambios );
			pBotonera.add( bCambiarFoto );
			getContentPane().add( pBotonera, BorderLayout.SOUTH );

		// Primera lista - todos los productos
		List<Producto> l = servicioPersistencia.buscarTodos();
		for (Producto prod : l) {
			mProductos.addElement( prod );
		}
		bNuevoProd.setEnabled( true );
		bConfirmarCambios.setEnabled( false );
		bCambiarFoto.setEnabled( false );
		
		// Eventos
		tfFiltro.addFocusListener( new FocusAdapter() { // Selecciona todo el texto del cuadro en cuanto se le da el foco del teclado
			@Override
			public void focusGained(FocusEvent e) {
				tfFiltro.selectAll();
			}
		});
		tfFiltro.addActionListener( new ActionListener() { // Evento de <enter> de textfield de filtro
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Producto> l = servicioPersistencia.buscarParteNombre( tfFiltro.getText() );
				mProductos.clear();
				for (Producto prod : l) {
					mProductos.addElement( prod );
				}
			}
		});
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				servicioPersistencia.close();
			}
		});
		lProductos.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					refrescaPanelProducto();
				}
			}
		});
		bNuevoProd.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int nuevoCod = servicioPersistencia.getNumeroProductos() + 1;
				lProductos.setSelectedIndices( new int[0] );  // Quita selección
				tfCodigo.setText( nuevoCod + "" );
				tfNombre.requestFocus();
				tfFiltro.setEnabled( false );
				bNuevoProd.setEnabled( false );
				bConfirmarCambios.setEnabled( true );
				bCambiarFoto.setEnabled( true );
				insertando = true;
				productoEnInsercion = new Producto( nuevoCod, "", 0, "" );
			}
		});
		bConfirmarCambios.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (insertando) {  // Nuevo producto
					if (lFoto.getImagen() == null) {
						JOptionPane.showMessageDialog( VentanaConsultaProductos.this, "No se puede insertar producto: foto no definida ", "Error en inserción", JOptionPane.ERROR_MESSAGE, null );
						return;
					}
					try {
						String precioTx = tfPrecio.getText().replaceAll( ",", "." );  // Pone punto decimal en lugar de coma
						double precio = Double.parseDouble( precioTx );
						productoEnInsercion.setPrecio( precio );
						productoEnInsercion.setNombre( tfNombre.getText() );
						servicioPersistencia.insertar( productoEnInsercion, lFoto.getImagen() );
						insertando = false;
						mProductos.addElement( productoEnInsercion );
						lProductos.addSelectionInterval( mProductos.size()-1, mProductos.size()-1 );
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog( VentanaConsultaProductos.this, "No se puede insertar producto: código incorrecto " + tfCodigo.getText(), "Error en inserción", JOptionPane.ERROR_MESSAGE, null );
					}
				} else if (productoEnSeleccion!=null) {  // Modificación de producto existente
					try {
						String precioTx = tfPrecio.getText().replaceAll( ",", "." );  // Pone punto decimal en lugar de coma
						double precio = Double.parseDouble( precioTx );
						productoEnSeleccion.setPrecio( precio );
						productoEnSeleccion.setNombre( tfNombre.getText() );
						servicioPersistencia.actualizar( productoEnSeleccion, lFoto.getImagen() );
						bConfirmarCambios.setEnabled( false );
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog( VentanaConsultaProductos.this, "No se puede actualizar producto " + productoEnSeleccion, "Error en modificación", JOptionPane.ERROR_MESSAGE, null );
					}
				}
			}
		});
		bCambiarFoto.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Producto pActual = productoEnSeleccion;
				if (insertando) {
					pActual = productoEnInsercion;
				}
				if (pActual!=null) {
					JFileChooser fc = new JFileChooser( new File( "." ) );
					fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
					fc.setDialogTitle( "Selecciona fichero de imagen de producto" );
					fc.showOpenDialog( VentanaConsultaProductos.this );
					pActual.setRutaFoto( fc.getSelectedFile().getAbsolutePath() );
					ImageIcon imagen = getImageIcon( pActual.getRutaFoto() );
					lFoto.setImagen( imagen );
					lFoto.repaint();
					bConfirmarCambios.setEnabled( true );
				}
			}
		});
		tfNombre.addFocusListener( new FocusListener() {
			String valAnterior;
			@Override
			public void focusLost(FocusEvent e) {
				if (!tfNombre.getText().equals( valAnterior )) {
					bConfirmarCambios.setEnabled( true );
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				valAnterior = tfNombre.getText();
			}
		});
		tfPrecio.addFocusListener( new FocusListener() {
			String valAnterior;
			@Override
			public void focusLost(FocusEvent e) {
				if (!tfPrecio.getText().equals( valAnterior )) {
					bConfirmarCambios.setEnabled( true );
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				valAnterior = tfPrecio.getText();
			}
		});
	}
	
	private void refrescaPanelProducto() {
		insertando = false;
		Producto p = lProductos.getSelectedValue();
		if (p==null) {
			tfCodigo.setText( "" );
			tfNombre.setText( "" );
			tfPrecio.setText( "" );
			lFoto.setImagen( null );
			lFoto.repaint();
			tfFiltro.setEnabled( true );
			bNuevoProd.setEnabled( true );
			bConfirmarCambios.setEnabled( false );
			bCambiarFoto.setEnabled( false );
			productoEnSeleccion = null;
		} else {
			tfCodigo.setText( p.getCodigo() + "" );
			tfNombre.setText( p.getNombre() );
			tfPrecio.setText( p.getPrecio() + "" );
			ImageIcon imagen = servicioPersistencia.cargaImagen( p );
			lFoto.setImagen( imagen );
			lFoto.repaint();
			tfFiltro.setEnabled( true );
			bNuevoProd.setEnabled( true );
			bConfirmarCambios.setEnabled( false );
			bCambiarFoto.setEnabled( true );
			productoEnSeleccion = p;
		}
	}
	
	private static ImageIcon getImageIcon( String pathORecurso ) {
		File fic = new File( pathORecurso );
		if (fic.exists()) {
			return new ImageIcon( fic.getAbsolutePath() );
		} else {
			ImageIcon icon = new ImageIcon( VentanaConsultaProductos.class.getResource( pathORecurso ) );
			return icon;
		}
	}

	private static class JLabelAjustado extends JLabel {
		private ImageIcon imagen; 
		private int tamX;
		private int tamY;
		/** Crea un jlabel que ajusta una imagen cualquiera con fondo blanco a su tamaño (a la que ajuste más de las dos escalas, horizontal o vertical)
		 * @param imagen	Imagen a visualizar en el label
		 */
		public JLabelAjustado( ImageIcon imagen ) {
			setImagen( imagen );
		}
		public ImageIcon getImagen() {
			return imagen;
		}
		/** Modifica la imagen
		 * @param imagen	Nueva imagen a visualizar en el label
		 */
		public void setImagen( ImageIcon imagen ) {
			this.imagen = imagen;
			if (imagen==null) {
				tamX = 0;
				tamY = 0;
			} else {
				this.tamX = imagen.getIconWidth();
				this.tamY = imagen.getIconHeight();
			}
		}
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
			g2.setColor( Color.WHITE );
			g2.fillRect( 0, 0, getWidth(), getHeight() );
			if (imagen!=null && tamX>0 && tamY>0) {
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
				double escalaX = 1.0 * getWidth() / tamX;
				double escalaY = 1.0 * getHeight() / tamY;
				double escala = escalaX;
				int x = 0;
				int y = 0;
				if (escalaY < escala) {
					escala = escalaY;
					x = (int) ((getWidth() - (tamX * escala)) / 2);
				} else {
					y = (int) ((getHeight() - (tamY * escala)) / 2);
				}
		        g2.drawImage( imagen.getImage(), x, y, (int) (tamX*escala), (int) (tamY*escala), null );
			}
		}
	}
}
