package es.deusto.prog3.cap01;

import java.util.List;

import javax.script.*;

public class EjemploScripting {
	public static void main(String[] args) throws Exception {
		// Crea un script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// Crea un intérprete JavaScript
		ScriptEngine engine = factory.getEngineByName( "JavaScript" );
		try {
			// Evalúa código JavaScript
			engine.eval( "print('Hello, World')" );
			// Evalúa código JavaScript incorrecto
			engine.eval( "print(Hello, World')" );
		} catch (ScriptException e) {
			System.out.println( "Error de ejecución de script: " + e.getMessage() );
		}
		// Lista lenguajes disponibles:
		for (ScriptEngineFactory fact : factory.getEngineFactories()) {
		    System.out.println("ScriptEngineFactory Info");
		    String engName = fact.getEngineName();
		    String engVersion = fact.getEngineVersion();
		    String langName = fact.getLanguageName();
		    String langVersion = fact.getLanguageVersion();
		    System.out.printf("\tScript Engine: %s (%s)\n", 
		        engName, engVersion);
		    List<String> engNames = fact.getNames();
		    for(String name: engNames) {
		      System.out.printf("\t\tEngine Alias: %s\n", name);
		    }
		    System.out.printf("\tLanguage: %s (%s)\n", 
		        langName, langVersion);
		  }  		
	}
}
