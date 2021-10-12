package es.deusto.prog3.utils.comunicacion;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/** Se puede crear un servidor Http con Java, aunque es mucho trabajo hacerlo bien y normalmente
 * se utilizan frameworks ya preparados. Ejemplo de un miniservidor
 * (Ejecutarlo y teclead en el navegador:  localhost:8000/prueba)
 */
public class ServidorHttp {

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/prueba", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = " <!DOCTYPE html><html lang=\"es\"><body>Estoy sirviendo este <b>minicontenido</b> html</body></html>";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

}
