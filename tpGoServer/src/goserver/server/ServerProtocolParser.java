/**
 * 
 */
package goserver.server;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import goserver.game.GoGroupType;
import goserver.util.IntPair;

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
			int result = scanner.nextInt();
			scanner.close();
			return result;
		} catch (InputMismatchException e){
			scanner.close();
			return -1;
		}
	}
	
	public IntPair parseMove(String line) {
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.SEND_MOVE);
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			scanner.close();
			return new IntPair(x, y);
		} catch (NoSuchElementException nsee) {
			scanner.close();
			return null;	
		}
	}
	
	public boolean parsePassTurn(String line) {
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.PASS_TURN);
			scanner.close();
			return true;
		} catch (InputMismatchException e){
			scanner.close();
			return false;
		}
	}
	
	public Map<Integer, GoGroupType> parseGroupStateChange(String line) {
		Scanner scanner = new Scanner(line);
		Map<Integer, GoGroupType> changes = new HashMap<Integer, GoGroupType>();
		
		try {
			scanner.next(protocol.CHANGE_GROUP_STATE);
			while (scanner.hasNext()){
				int label = scanner.nextInt();
				String token = scanner.next();
				if (token.equals(protocol.ALIVE)){
					changes.put(label, GoGroupType.ALIVE);
				} else if (token.equals(protocol.DEAD)){
					changes.put(label, GoGroupType.DEAD);
				} else {
					scanner.close();
					return null;
				}
			}
			scanner.close();
			return changes;
		} catch (InputMismatchException e){
			scanner.close();
			return null;
		}
	}

}
