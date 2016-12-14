/**
 * 
 */
package goserver.server;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Kacper
 */
public class ServerProtocolParser {

	public static ServerProtocolParser instance;

	private ServerClientProtocol protocol;

	private ServerProtocolParser() {
		protocol = ServerClientProtocol.getInstance();
	}

	public synchronized static ServerProtocolParser getInstance() {
		if (instance == null) {
			instance = new ServerProtocolParser();
		}
		return instance;
	}

	/**
	 * 
	 * @param line
	 * @return Żądany rozmiar planszy, -1 jeśli podana linia nie pasuje do
	 *         protokołu żądania o gre z innym graczem.
	 */
	public int parseGameWithPlayerRequest(String line) {
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.REQUEST_GAME);
			scanner.next(protocol.PLAYER);
			return scanner.nextInt();
		} catch (InputMismatchException e){
			return -1;
		}
	}
	
	/**
	 * 
	 * @param line
	 * @return Żądany rozmiar planszy, -1 jeśli podana linia nie pasuje do
	 *         protokołu żądania o gre z botem.
	 */
	public int parseGameWithBotRequest(String line) {
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.REQUEST_GAME);
			scanner.next(protocol.BOT);
			return scanner.nextInt();
		} catch (InputMismatchException e){
			return -1;
		}
	}

}
