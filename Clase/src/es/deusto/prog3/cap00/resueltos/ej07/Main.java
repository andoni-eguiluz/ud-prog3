package es.deusto.prog3.cap00.resueltos.ej07;

// Prueba de ejercicio 0.7
public class Main {
	public static void main(String[] args) {
		Usuario u1 = new Usuario( "Andoni", "EGUILUZ" );
		VentanaUsuario v = new VentanaUsuario();
		v.setVisible( true );
		v.muestraUsuario( u1 );
		try { Thread.sleep( 2000 ); } catch (InterruptedException e) {}  // Espera 2 segundos
		UsuarioNivel u2 = new UsuarioNivel( "Maite", "LOPEZ", 8 );
		VentanaNivel v2 = new VentanaNivel();
		v2.setLocation( 100, 100 );
		v2.setVisible( true );
		v2.muestraUsuario( u2 );
		try { Thread.sleep( 2000 ); } catch (InterruptedException e) {}  // Espera 2 segundos
		UsuarioFoto u3 = new UsuarioFoto( "Jordi", "SANZ", "persona.png" );
		VentanaFoto v3 = new VentanaFoto();
		v3.setLocation( 200, 200 );
		v3.setVisible( true );
		v3.muestraUsuario( u3 );
	}

}
