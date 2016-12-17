/**
 * 
 */
package goclient.client;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Kacper
 */
public class ClientProtocolParser {

	public static ClientProtocolParser instance;

	private ServerClientProtocol protocol;

	private ClientProtocolParser() {
		protocol = ServerClientProtocol.getInstance();
	}

	public synchronized static ClientProtocolParser getInstance() {
		if (instance == null) {
			instance = new ClientProtocolParser();
		}
		return instance;
	}

	public String parseColor(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			return scanner.next(protocol.ASSIGN_COLOR);
		} catch (InputMismatchException e){
			return "";
		}
	}

}
