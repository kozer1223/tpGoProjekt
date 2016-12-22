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
 * Class parsing messages sent from client to server.
 * 
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
	 * Parse a request for a game with another player.
	 * 
	 * @param line
	 * @return Requested board size, -1 if the line is not a valid request.
	 */
	public int parseGameWithPlayerRequest(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.REQUEST_GAME);
			scanner.next(protocol.PLAYER);
			return scanner.nextInt();
		} catch (NoSuchElementException e) {
			return -1;
		}
	}

	/**
	 * Parse a request for a game with a bot.
	 * 
	 * @param line
	 * @return Requested board size, -1 if the line is not a valid request.
	 */
	public int parseGameWithBotRequest(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.REQUEST_GAME);
			scanner.next(protocol.BOT);
			int result = scanner.nextInt();
			scanner.close();
			return result;
		} catch (NoSuchElementException e) {
			scanner.close();
			return -1;
		}
	}

	/**
	 * Parse a request to place a stone on the board.
	 * 
	 * @param line
	 * @return (x, y) location on the board, null if line is not a valid
	 *         request.
	 */
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

	/**
	 * Parse a request to pass a turn.
	 * 
	 * @param line
	 * @return true if line is a valid pass turn request, false otherwise.
	 */
	public boolean parsePassTurn(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.PASS_TURN);
			scanner.close();
			return true;
		} catch (NoSuchElementException e) {
			scanner.close();
			return false;
		}
	}

	/**
	 * Parse a request to apply given group state changes.
	 * 
	 * @param line
	 * @return Map representing group state changes, null if line is not a valid
	 *         request.
	 */
	public Map<Integer, GoGroupType> parseGroupStateChange(String line) {
		Scanner scanner = new Scanner(line);
		Map<Integer, GoGroupType> changes = new HashMap<Integer, GoGroupType>();

		try {
			scanner.next(protocol.CHANGE_GROUP_STATE);
			while (scanner.hasNext()) {
				int label = scanner.nextInt();
				String token = scanner.next();
				if (token.equals(protocol.ALIVE)) {
					changes.put(label, GoGroupType.ALIVE);
				} else if (token.equals(protocol.DEAD)) {
					changes.put(label, GoGroupType.DEAD);
				} else {
					scanner.close();
					return null;
				}
			}
			scanner.close();
			return changes;
		} catch (NoSuchElementException e) {
			scanner.close();
			return null;
		}
	}

	/**
	 * Parse a rematch request.
	 * 
	 * @param line
	 * @return True if line is a valid rematch request.
	 */
	public boolean parseRematchRequest(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.REQUEST_REMATCH);
			scanner.close();
			return true;
		} catch (NoSuchElementException e) {
			scanner.close();
			return false;
		}
	}

	/**
	 * Parse a reply ping signal.
	 * 
	 * @param line
	 * @return True if line is a reply ping signal.
	 */
	public boolean parsePing(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.PING);
			scanner.close();
			return true;
		} catch (NoSuchElementException e) {
			scanner.close();
			return false;
		}
	}

}
