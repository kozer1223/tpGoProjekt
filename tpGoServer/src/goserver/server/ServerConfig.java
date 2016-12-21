package goserver.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

	private final String CONFIG_FILE = "config/config.properties";
	private Properties prop;
	
	private static ServerConfig instance;

	private ServerConfig() throws IOException {
		prop = new Properties();
		InputStream inputStream = new FileInputStream(CONFIG_FILE);
		
		prop.load(inputStream);
	}
	
	public synchronized static ServerConfig getInstance() {
		if (instance == null) {
			try {
				instance = new ServerConfig();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return instance;
	}
	
	public int getServerSocket(){
		return Integer.parseInt(prop.getProperty("socket"));
	}

}
