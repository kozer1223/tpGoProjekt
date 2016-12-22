/**
 * 
 */
package goclient.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import goclient.client.GoGroupType;
import goclient.util.DoublePair;
import goclient.util.IntPair;

/**
 * Class parsing messages sent from server to client.
 * 
 * @author Kacper
 */
public class ClientProtocolParser {

	public static ClientProtocolParser instance;

	private ServerClientProtocol protocol;

	public static final int MOVE = 3;
	public static final int PASS = 4;

	private ClientProtocolParser() {
		protocol = ServerClientProtocol.getInstance();
	}

	public synchronized static ClientProtocolParser getInstance() {
		if (instance == null) {
			instance = new ClientProtocolParser();
		}
		return instance;
	}

	/**
	 * Parse information about the color assigned to the client.
	 * 
	 * @param line
	 * @return String representing the color, empty string if it's not a valid
	 *         request.
	 */
	public String parseColor(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.ASSIGN_COLOR);
			String color = scanner.next();
			scanner.close();
			return color;
		} catch (InputMismatchException e) {
			scanner.close();
			return "";
		}
	}

	/**
	 * Parse an information about the game having begun.
	 * 
	 * @param line
	 * @return True if line is a valid request.
	 */
	public boolean parseBeginGame(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.GAME_BEGIN);
			scanner.close();
			return true;
		} catch (InputMismatchException e) {
			return false;
		}
	}

	/**
	 * Parse information about player's move being accepted.
	 * 
	 * @param line
	 * @return True if line is a valid request.
	 */
	public boolean parseMoveAccepted(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.MOVE_ACCEPTED);
			scanner.close();
			return true;
		} catch (InputMismatchException e) {
			return false;
		}
	}

	/**
	 * Parse information about the current board state.
	 * 
	 * @param line
	 * @param size
	 *            Board size.
	 * @return Parsed board data, null if line is not a valid request.
	 */
	public int[][] parseBoard(String line, int size) {
		Scanner scanner = new Scanner(line);
		int[][] board = new int[size][size];

		try {
			scanner.next(protocol.SEND_BOARD);
			String boardData = scanner.next();
			int i = 0, j = 0;
			for (int t = 0; t < boardData.length(); t++) {
				board[i][j] = boardData.charAt(t) - '0';
				j++;
				if (j >= size) {
					j = 0;
					i++;
				}
				if (i >= size) {
					break;
				}
			}
			scanner.close();
			return board;
		} catch (InputMismatchException e) {
			return null;
		}
	}

	/**
	 * Parse information about the current phase.
	 * 
	 * @param line
	 * @return Current phase number, -1 if line is not a valid request.
	 */
	public int parsePhase(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.SEND_PHASE);
			int phase = scanner.nextInt();
			scanner.close();
			return phase;
		} catch (InputMismatchException e) {
			scanner.close();
			return -1;
		}
	}

	/**
	 * Parse information about the board labeling.
	 * 
	 * @param line
	 * @param size
	 *            Board size.
	 * @return Parsed labeled board data, null if line is not a valid request.
	 */
	public int[][] parseLabeledBoard(String line, int size) {
		Scanner scanner = new Scanner(line);
		int[][] board = new int[size][size];

		try {
			scanner.next(protocol.SEND_LABELED_BOARD);
			int i = 0, j = 0;
			while (scanner.hasNext()) {
				board[i][j] = scanner.nextInt();
				j++;
				if (j >= size) {
					j = 0;
					i++;
				}
				if (i >= size) {
					break;
				}
			}
			scanner.close();
			return board;
		} catch (InputMismatchException e) {
			return null;
		}
	}

	/**
	 * Parse information about the state of all the groups.
	 * 
	 * @param line
	 * @return Map containing group state for each group, null if line is not a
	 *         valid request.
	 */
	public Map<Integer, GoGroupType> parseGroupState(String line) {
		Scanner scanner = new Scanner(line);
		Map<Integer, GoGroupType> changes = new HashMap<Integer, GoGroupType>();

		try {
			scanner.next(protocol.SEND_GROUP_STATE);
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
		} catch (InputMismatchException e) {
			scanner.close();
			return null;
		}
	}

	/**
	 * Parse information about the locked groups.
	 * 
	 * @param line
	 * @return List of all locked groups, null if line is not a valid request.
	 */
	public List<Integer> parseLockedGroups(String line) {
		Scanner scanner = new Scanner(line);
		List<Integer> lockedGroups = new ArrayList<Integer>();

		try {
			scanner.next(protocol.SEND_LOCKED_GROUPS);
			while (scanner.hasNext()) {
				int label = scanner.nextInt();
				lockedGroups.add(label);
			}
			scanner.close();
			return lockedGroups;
		} catch (InputMismatchException e) {
			scanner.close();
			return null;
		}
	}

	/**
	 * Parse score sent at the end of the game.
	 * 
	 * @param line
	 * @return (Black player's score, White player's score), null if line is not
	 *         a valid request.
	 */
	public DoublePair parseScore(String line) {
		Scanner scanner = new Scanner(line);
		scanner.useLocale(Locale.US); // '.' as a separator

		try {
			scanner.next(protocol.SEND_SCORE);
			double blackScore, whiteScore;
			blackScore = scanner.nextDouble();
			whiteScore = scanner.nextDouble();
			scanner.close();
			return new DoublePair(blackScore, whiteScore);
		} catch (InputMismatchException e) {
			scanner.close();
			return null;
		}
	}

	/**
	 * Parse information about stones captured by each player.
	 * 
	 * @param line
	 * @return (Stones captured by Black player, stones captured by White
	 *         player), null if line is not a valid request.
	 */
	public IntPair parseCapturedStones(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.SEND_CAPTURED_STONES);
			int blackStones, whiteStones;
			blackStones = scanner.nextInt();
			whiteStones = scanner.nextInt();
			scanner.close();
			return new IntPair(blackStones, whiteStones);
		} catch (InputMismatchException e) {
			scanner.close();
			return null;
		}
	}

	/**
	 * Parse an invalid move message.
	 * 
	 * @param line
	 *            Invalid move message.
	 * @return Invalid move message, null if line is not a valid request.
	 */
	public String parseMessage(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.SEND_MESSAGE);
			String message = scanner.nextLine();
			message = message.trim();
			scanner.close();
			return message;
		} catch (InputMismatchException e) {
			scanner.close();
			return null;
		}
	}

	/**
	 * Parse information about the opponent's last turn.
	 * 
	 * @param line
	 * @return MOVE if opponent placed a stone, PASS if opponent passed, -1
	 *         otherwise or if the line is not a valid request.
	 */
	public int parseLastTurnInform(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.LAST_MOVE);
			String turn = scanner.next();
			scanner.close();
			if (turn.startsWith(protocol.MOVE)) {
				return MOVE;
			} else if (turn.startsWith(protocol.PASS)) {
				return PASS;
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			scanner.close();
			return -1;
		}
	}

	/**
	 * Parse information about a rematch request being accepted.
	 * 
	 * @param line
	 * @return true if the line is a valid request.
	 */
	public boolean parseRematchAccepted(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.REMATCH_ACCEPTED);
			scanner.close();
			return true;
		} catch (InputMismatchException e) {
			return false;
		}
	}

	/**
	 * Parse information about a rematch request being denied.
	 * 
	 * @param line
	 * @return true if the line is a valid request.
	 */
	public boolean parseRematchDenied(String line) {
		Scanner scanner = new Scanner(line);

		try {
			scanner.next(protocol.REMATCH_DENIED);
			scanner.close();
			return true;
		} catch (InputMismatchException e) {
			return false;
		}
	}

	/**
	 * Parse ping request from server.
	 * 
	 * @param line
	 * @return true if the line is a ping request, false otherwise.
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
