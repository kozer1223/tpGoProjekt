package goclient.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientConfig {

	private final String CONFIG_FILE = "config/config.properties";
	private Properties prop;
	
	private static ClientConfig instance;

	private ClientConfig() throws IOException {
		prop = new Properties();
		InputStream inputStream = new FileInputStream(CONFIG_FILE);
		
		prop.load(inputStream);
	}
	
	public synchronized static ClientConfig getInstance() {
		if (instance == null) {
			try {
				instance = new ClientConfig();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return instance;
	}
	
	/**
	 * @return Server address string.
	 */
	public String getServerAddress(){
		return prop.getProperty("server_address");
	}
	
	/**
	 * @return Server socket.
	 */
	public int getServerSocket(){
		return Integer.parseInt(prop.getProperty("server_socket"));
	}
}
