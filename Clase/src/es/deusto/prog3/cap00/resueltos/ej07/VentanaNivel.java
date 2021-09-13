package es.deusto.prog3.cap00.resueltos.ej07;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class VentanaNivel extends VentanaUsuario{
	protected JSlider slNivel = new JSlider(0,10);
	public VentanaNivel() {
		super();
		pReservado.add( slNivel  );
		slNivel.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (usuario!=null && usuario instanceof UsuarioNivel) {
					((UsuarioNivel)usuario).setNivel( slNivel.getValue() );
				}
			}
		});
	}
	
	@Override
	public void muestraUsuario(Usuario u) {
		super.muestraUsuario(u);
		// Si es un usuario de nivel actualiza también el slider
		if (u instanceof UsuarioNivel) {
			slNivel.setValue( ((UsuarioNivel)u).getNivel() ); 
		}
	}
	
}
