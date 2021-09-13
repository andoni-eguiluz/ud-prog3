package es.deusto.prog3.cap00.resueltos.ej07;

@SuppressWarnings("serial")
public class VentanaFoto extends VentanaUsuario{
	private JLabelGrafico foto;
	public VentanaFoto() {
		super();
		foto = new JLabelGrafico( "", 200, 200 );
		pReservado.add( foto );
	}
	
	@Override
	public void muestraUsuario(Usuario u) {
		super.muestraUsuario(u);
		// Si es un usuario de foto actualiza también la foto
		if (u instanceof UsuarioFoto) {
			foto.setImagen( ((UsuarioFoto) u).getFoto() );
		}
	}
}
