package org.util.nanolog.internals;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.util.nanolog.Level;

public final class ConfigLoader {

	private static final Properties properties = new Properties();

	static {
		try {

			final File file = new File("ini/logging.properties");
			if(file.exists()) {
				try(FileReader in = new FileReader(file)) {
					properties.load(in);
					if(!properties.containsKey("LEVEL")) properties.put("LEVEL", "TRACE");
					if(!properties.containsKey("LOG_CONSOLE")) properties.put("LOG_CONSOLE", "Y");
					System.out.println("Properties : " + properties);
				} catch (Exception e) {e.printStackTrace();}
			}
			else {
				System.err.println("Loading default logging properties\r\n" + "LEVEL=TRACE\r\n" + "LOG_CONSOLE=Y");
				properties.put("LEVEL", "TRACE");
				properties.put("LOG_CONSOLE", "Y");
			}
		
		} catch (Exception e) {e.printStackTrace();}
	}

	public static final Level getLevel() {
		if ("ERROR".equalsIgnoreCase(properties.getProperty("LEVEL"))) return Level.ERROR;
		else if ("WARN".equalsIgnoreCase(properties.getProperty("LEVEL"))) return Level.WARN;
		else if ("INFO".equalsIgnoreCase(properties.getProperty("LEVEL"))) return Level.INFO;
		else if ("DEBUG".equalsIgnoreCase(properties.getProperty("LEVEL"))) return Level.DEBUG;
		else if ("TRACE".equalsIgnoreCase(properties.getProperty("LEVEL"))) return Level.TRACE;
		else return Level.TRACE;
	}

	public static final boolean logConsole() {
		if (properties.getProperty("LOG_CONSOLE") == null) return true;
		return "Y".equalsIgnoreCase(properties.getProperty("LOG_CONSOLE"));
	}
	
}
