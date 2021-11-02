package es.deusto.prog3.cap03.ejemploPartidas;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.deusto.prog3.cap03.ejemploPartidas.BD;
import es.deusto.prog3.cap03.ejemploPartidas.Partida;
import es.deusto.prog3.cap03.ejemploPartidas.TipoUsuario;
import es.deusto.prog3.cap03.ejemploPartidas.Usuario;

// Documentación particular de foreign keys en sqlite en
// https://www.sqlite.org/foreignkeys.html
// Si se quiere hacer sin foreign keys, quitar las líneas marcadas con (1) y sustituirlas por las (2) en BD.java

public class BDTest {

	private Usuario u1, u2, u3;      // Usuarios de prueba
	private Usuario[] usuarios;
	private List<Usuario> lUsuarios;
	private Partida p1, p2, p3, p4;  // Partidas de prueba
	private Partida[] partidas;
	
	@Before
	public void setUp() throws Exception {
		u1 = new Usuario( "nick1", "pass1", "nom1", "ape1", 1, TipoUsuario.Admin, new ArrayList<>() );
		u2 = new Usuario( "nick2", "pass2", "nom2", "ape2", 2, TipoUsuario.Cliente, new ArrayList<>() );
		u3 = new Usuario( "nick3", "pass3", "nom3", "ape3", 3, TipoUsuario.Invitado, new ArrayList<>() );
		usuarios = new Usuario[] { u1, u2, u3 };
		lUsuarios = Arrays.asList( usuarios );
		p1 = new Partida( u1, System.currentTimeMillis(), 100 );
		p2 = new Partida( u1, System.currentTimeMillis()-100000, 250 );
		p3 = new Partida( u1, System.currentTimeMillis()-200000, 50 );
		p4 = new Partida( u3, System.currentTimeMillis(), 72 );
		partidas = new Partida[] { p1, p2, p3, p4 };
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void initCrearCerrarTest() {
		Connection con = BD.initBD( "bd-test" );
		assertNotNull( con );
		Statement stat =  BD.usarCrearTablasBD( con );
		assertNotNull( stat );
		BD.cerrarBD( con, stat );
	}

	@Test
	public void reiniciarBDTest() {
		Connection con = BD.initBD( "bd-test" );
		Statement stat =  BD.usarCrearTablasBD( con );
		BD.usuarioInsert( stat, u1 );
		stat =  BD.reiniciarBD( con );
		ArrayList<Usuario> lUsuarios = BD.usuarioSelect( stat, null );
		ArrayList<Partida> lPartidas = BD.partidaSelect( stat, null, null, null );
		assertEquals( 0, lUsuarios.size() );  // Comprueba el número de datos (vacío)
		assertEquals( 0, lPartidas.size() );  // Comprueba el número de datos (vacío)
		BD.cerrarBD( con, stat );
	}

	@Test
	public void usuarioInsertTest() {
		Connection con = BD.initBD( "bd-test" );
		// Statement stat =  BD.usarCrearTablasBD( con );
		Statement stat =  BD.reiniciarBD( con );
		ArrayList<Usuario> lUsuarios = BD.usuarioSelect( stat, null );
		int tamBD = lUsuarios.size();
		assertTrue( BD.usuarioInsert( stat, u1 ) );
		assertTrue( BD.usuarioInsert( stat, u2 ) );
		lUsuarios = BD.usuarioSelect( stat, null );
		assertEquals( tamBD+2, lUsuarios.size() );  // Comprueba el número de datos
		BD.cerrarBD( con, stat );
	}

	@Test
	public void usuarioSelectTest() {
		Connection con = BD.initBD( "bd-test" );
		Statement stat =  BD.reiniciarBD( con );
		BD.usuarioInsert( stat, u1 );
		BD.usuarioInsert( stat, u2 );
		BD.usuarioInsert( stat, u3 );
		ArrayList<Usuario> lUsuarios = BD.usuarioSelect( stat, null );
		lUsuarios.sort( new Comparator<Usuario>() {
			@Override
			public int compare(Usuario o1, Usuario o2) {
				return o1.getNick().compareTo( o2.getNick() );
			}
		});
		for (int i=0; i<lUsuarios.size(); i++) {
			assertEquals( lUsuarios.get(i).getNick(), usuarios[i].getNick() );
			assertEquals( lUsuarios.get(i).getPassword(), usuarios[i].getPassword() );
			assertEquals( lUsuarios.get(i).getNombre(), usuarios[i].getNombre() );
			assertEquals( lUsuarios.get(i).getApellidos(), usuarios[i].getApellidos() );
			assertEquals( lUsuarios.get(i).getFechaUltimoLogin(), usuarios[i].getFechaUltimoLogin() );
			assertEquals( lUsuarios.get(i).getTelefono(), usuarios[i].getTelefono() );
			assertEquals( lUsuarios.get(i).getTipo(), usuarios[i].getTipo() );
			assertEquals( lUsuarios.get(i).getEmails(), usuarios[i].getEmails() );
			assertEquals( lUsuarios.get(i).getListaEmails(), usuarios[i].getListaEmails() );
		}
		BD.cerrarBD( con, stat );
	}

	@Test
	public void usuarioUpdateTest() {
		Connection con = BD.initBD( "bd-test" );
		Statement stat =  BD.reiniciarBD( con );
		assertTrue( BD.usuarioInsert( stat, u1 ) );
		assertTrue( BD.usuarioInsert( stat, u2 ) );
		u1.setTelefono(7); u1.setApellidos( "cambio" );
		assertTrue( BD.usuarioUpdate( stat, u1 ) );  // Comprueba que el update es correcto
		lUsuarios = BD.usuarioSelect( stat, "nick='" + u1.getNick() + "'" );
		assertEquals( 1, lUsuarios.size() );  // Comprueba el número de datos
		assertEquals( u1.getTelefono(), lUsuarios.get(0).getTelefono() );  // Comprueba el cambio de datos
		assertEquals( "cambio", lUsuarios.get(0).getApellidos() );  // Comprueba el cambio de datos
		BD.cerrarBD( con, stat );
	}

	@Test
	public void partidaInsertTest() {
		Connection con = BD.initBD( "bd-test" );
		Statement stat =  BD.reiniciarBD( con );
		for (Usuario u : usuarios)
			BD.usuarioInsert( stat, u );
		for (Partida p : partidas)
			assertTrue( BD.partidaInsert( stat, p ) );
		ArrayList<Partida> lPartidas = BD.partidaSelect( stat, u1, null, null );
		assertEquals( 3, lPartidas.size() );  // Comprueba el número de datos: u1 tiene 3 partidas
		lPartidas = BD.partidaSelect( stat, u2, null, null );
		assertEquals( 0, lPartidas.size() );  // Comprueba el número de datos: u2 tiene 0 partidas
		lPartidas = BD.partidaSelect( stat, u3, null, null );
		assertEquals( 1, lPartidas.size() );  // Comprueba el número de datos: u3 tiene 1 partida
		lPartidas = BD.partidaSelect( stat, null, null, lUsuarios );
		assertEquals( 4, lPartidas.size() );  // Comprueba el número de datos: hay 4 partidas en total
		lPartidas = BD.partidaSelect( stat, null, "puntuacion>200", lUsuarios );
		assertEquals( 1, lPartidas.size() );  // Comprueba el where: hay una partida con más de 200 puntos
		lPartidas = BD.partidaSelect( stat, u1, "puntuacion>70", lUsuarios );
		assertEquals( 2, lPartidas.size() );  // Comprueba el where: hay dos partidas de u1 con más de 70 puntos
		BD.cerrarBD( con, stat );
	}

	// prueba del on delete cascade del usuario
	// (solo con las foreign keys activadas)
	// Ver https://www.sqlite.org/foreignkeys.html
	@Test
	public void usuarioDeleteCascadeTest() {
		Connection con = BD.initBD( "bd-test2" );
		Statement stat =  BD.reiniciarBD( con );
		for (Usuario u : usuarios) BD.usuarioInsert( stat, u );
		for (Partida p : partidas) BD.partidaInsert( stat, p );
		long fechaHoy = System.currentTimeMillis();
		Partida pErronea = new Partida( new Usuario( "nickfalso", "", "", "", 4, TipoUsuario.Invitado, new ArrayList<>() ), fechaHoy, 100 );
		assertFalse( BD.partidaInsert( stat, pErronea ) );  // Comprueba que una partida sin usuario no se puede insertar
		BD.usuarioDelete( stat, u1 );
		ArrayList<Usuario> lUsuario = BD.usuarioSelect( stat, "nick = '" + u1.getNick() + "'" );
		assertTrue( lUsuario.size() == 0 );  // Comprueba que al borrar el usuario se borra correctamente ese usuario...
		ArrayList<Partida> lPartidas = BD.partidaSelect( stat, u1, null, null );
		assertEquals( 0, lPartidas.size() );  // ... y que se han borrado en cascada sus partidas
		BD.cerrarBD( con, stat );
	}
	
	@Test
	public void usuarioInsertCaracteresEspecialesTest() {
	   	Connection con = BD.initBD( "bd-test" );
	   	Statement stat =  BD.reiniciarBD( con );
	   	Usuario u = new Usuario( "tomo", "pass", "Tom", "O'Reilly", 27, TipoUsuario.Empleado, new ArrayList<>() );
	   	assertTrue( BD.usuarioInsert( stat, u ) );
	   	ArrayList<Usuario> lUsuarios = BD.usuarioSelect( stat, null );     	
	   	assertEquals( lUsuarios.size(), 1 );  // Comprueba el número de datos
	   	BD.cerrarBD( con, stat );
	}

	
}
