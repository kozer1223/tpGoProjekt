package goserver.server;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import goserver.game.GoGroupType;

public class ServerRequestSender {

	private static ServerRequestSender instance;
	private ServerClientProtocol protocol;

	public static final int BLACK = 1;
	public static final int WHITE = 2;

	public static final int MOVE = 3;
	public static final int PASS = 4;

	private ServerRequestSender() {
		protocol = ServerClientProtocol.getInstance();
	}

	public synchronized static ServerRequestSender getInstance() {
		if (instance == null) {
			instance = new ServerRequestSender();
		}
		return instance;
	}

	/**
	 * Send an information about the player's color to the client.
	 * 
	 * @param color
	 *            Color.
	 * @param output
	 *            Output writer.
	 */
	public void assignColorToClient(int color, PrintWriter output) {
		if (color == BLACK || color == WHITE) {
			StringBuilder command = new StringBuilder(protocol.ASSIGN_COLOR + " ");
			command.append((color == BLACK ? protocol.BLACK : protocol.WHITE));
			output.println(command.toString());
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Send an information that the game begun.
	 * 
	 * @param output
	 *            Output writer.
	 */
	public void sendGameBegin(PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.GAME_BEGIN);
		output.println(command.toString());
	}

	/**
	 * Send current board data to the client.
	 * 
	 * @param board
	 *            Board data.
	 * @param output
	 *            Output writer.
	 */
	public void sendBoardData(int[][] board, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_BOARD + " ");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				command.append(board[i][j]);
			}
		}
		output.println(command.toString());
	}

	/**
	 * Send information about player's move being accepted to the client.
	 * 
	 * @param output
	 *            Output writer.
	 */
	public void sendMoveAccepted(PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.MOVE_ACCEPTED);
		output.println(command.toString());
	}

	/**
	 * Send information about the current game phase.
	 * 
	 * @param phase
	 *            Current phase.
	 * @param output
	 *            Output writer.
	 */
	public void sendGamePhase(int phase, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_PHASE + " ");
		command.append(phase);
		output.println(command.toString());
	}

	/**
	 * Send labeled board data to the client.
	 * 
	 * @param board
	 *            Labeled board data.
	 * @param output
	 *            Output writer.
	 */
	public void sendLabeledBoardData(int[][] board, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_LABELED_BOARD + " ");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				command.append(board[i][j] + " ");
			}
		}
		output.println(command.toString());
	}

	/**
	 * Send group state data to the client.
	 * 
	 * @param labels
	 *            Map representing group state assigned to each labeled group.
	 * @param output
	 *            Output writer.
	 */
	public void sendGroupStateData(Map<Integer, GoGroupType> labels, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_GROUP_STATE + " ");
		for (int label : labels.keySet()) {
			command.append(label + " ");
			command.append((labels.get(label) == GoGroupType.ALIVE ? protocol.ALIVE : protocol.DEAD) + " ");
		}
		output.println(command.toString());
	}

	/**
	 * Send information about which groups are locked to the client.
	 * 
	 * @param lockedGroups
	 *            List of locked groups.
	 * @param output
	 *            Output writer.
	 */
	public void sendLockedGroups(List<Integer> lockedGroups, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_LOCKED_GROUPS + " ");
		for (int label : lockedGroups) {
			command.append(label + " ");
		}
		output.println(command.toString());
	}

	/**
	 * Send information about the end game score.
	 * 
	 * @param blackScore
	 *            Black player's score.
	 * @param whiteScore
	 *            White player's score.
	 * @param output
	 *            Output writer.
	 */
	public void sendGameScore(double blackScore, double whiteScore, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_SCORE + " ");
		command.append(blackScore + " ");
		command.append(whiteScore);
		output.println(command.toString());
	}

	/**
	 * Send information about the number of each players' captured stones.
	 * 
	 * @param blackStones
	 *            Stones captured by Black player.
	 * @param whiteStones
	 *            Stones captured by White player.
	 * @param output
	 *            Output writer.
	 */
	public void sendCapturedStones(int blackStones, int whiteStones, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_CAPTURED_STONES + " ");
		command.append(blackStones + " ");
		command.append(whiteStones);
		output.println(command.toString());
	}

	/**
	 * Send invalid move message to the client.
	 * 
	 * @param message
	 *            Message.
	 * @param output
	 *            Output writer.
	 */
	public void sendMessage(String message, PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.SEND_MESSAGE + " ");
		command.append(message);
		output.println(command.toString());
	}

	/**
	 * Send information about the opponent's finished turn.
	 * 
	 * @param turnInfo
	 *            Opponent's last move.
	 * @param output
	 *            Output writer.
	 */
	public void sendLastTurnInfo(int turnInfo, PrintWriter output) {
		if (turnInfo == MOVE || turnInfo == PASS) {
			StringBuilder command = new StringBuilder(protocol.LAST_MOVE + " ");
			command.append((turnInfo == MOVE ? protocol.MOVE : protocol.PASS));
			output.println(command.toString());
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Send information about the rematch being accepted.
	 * 
	 * @param output
	 *            Output writer.
	 */
	public void sendRematchAccepted(PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.REMATCH_ACCEPTED);
		output.println(command.toString());
	}

	/**
	 * Send information about the rematch being denied.
	 * 
	 * @param output
	 *            Output writer.
	 */
	public void sendRematchDenied(PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.REMATCH_DENIED);
		output.println(command.toString());
	}

	/**
	 * Send ping signal to the client.
	 * 
	 * @param output
	 *            Output writer.
	 */
	public void sendPing(PrintWriter output) {
		StringBuilder command = new StringBuilder(protocol.PING);
		output.println(command.toString());
	}

}
