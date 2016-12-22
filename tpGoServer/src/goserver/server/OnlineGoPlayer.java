/**
 * 
 */
package goserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.util.IntPair;

/**
 * Implementation of GoPlayer which communicates with a client using sockets.
 * 
 * @author Maciek
 *
 */
public class OnlineGoPlayer extends Thread implements GoPlayer {
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private GoGame game;
	private ServerProtocolParser parser;
	private ServerRequestSender sender;
	private int color;

	private boolean isRematch = false;

	private static final int BLACK = 1;
	private static final int WHITE = 2;

	/**
	 * Create a new OnlineGoPlayer using a socket, input and an output object.
	 * 
	 * @param socket
	 *            Socket.
	 * @param input
	 *            Input (incoming messages).
	 * @param output
	 *            Output (outgoing messages).
	 */
	public OnlineGoPlayer(Socket socket, BufferedReader input, PrintWriter output) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		parser = ServerProtocolParser.getInstance();
		sender = ServerRequestSender.getInstance();
	}

	/**
	 * @return Opposing player.
	 */
	protected GoPlayer getOpposingPlayer() {
		return game.getOpposingPlayer(this);
	}

	/**
	 * Method which sends game status. Information sent depends on the current
	 * phase.
	 */
	private void sendTurnData() {
		if (game != null) {
			if (game.isStonePlacingPhase()) {
				sender.sendBoardData(game.getBoard().getBoardData(), output);
				sender.sendCapturedStones(game.getPlayersCapturedStones(game.getPlayer1()),
						game.getPlayersCapturedStones(game.getPlayer2()), output);
			} else {
				sender.sendLabeledBoardData(game.getBoard().getBoardWithLabeledGroups(), output);
				sender.sendGroupStateData(game.getLabelsMap(), output);
				sender.sendLockedGroups(game.getAllLockedGroups(), output);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		if (game != null) {
			if (game.getPlayer1() == this) {
				color = BLACK;
				sender.assignColorToClient(ServerRequestSender.BLACK, output);
			} else {
				color = WHITE;
				sender.assignColorToClient(ServerRequestSender.WHITE, output);
			}
			sender.sendGameBegin(output);
			sendTurnData();
			while (true) {
				try {
					String line = input.readLine();
					if (line != null) {
						System.out.println("[PLAYER]>" + line);

						IntPair move;
						Map<Integer, GoGroupType> changes;
						try {
							if (game.isGameEnd() && parser.parseRematchRequest(line)) {
								game.requestRematch(this);
							} else if ((move = parser.parseMove(line)) != null) {
								try {
									game.makeMove(this, move.x, move.y);
									sender.sendMoveAccepted(output);
								} catch (InvalidMoveException e) {
									sender.sendMessage(e.getMessage(), output);
								}
							} else if (parser.parsePassTurn(line)) {
								if (game.isStonePlacingPhase()) {
									game.passTurn(this);
								} else {
									// pas jako zgodzenie sie z propozycja grup
									game.applyGroupTypeChanges(this, new HashMap<Integer, GoGroupType>());
								}
								sender.sendMoveAccepted(output);
							} else if ((changes = parser.parseGroupStateChange(line)) != null) {
								game.applyGroupTypeChanges(this, changes);
								sender.sendMoveAccepted(output);
							}
						} catch (IllegalArgumentException e) {

						}

					}
				} catch (IOException e) {
					System.out.println("Lost connection with player");
					if (game.isGameEnd()) {
						game.denyRematch(this);
					} else {
						game.leaveGame(this);
						game.denyRematch(this);
					}
					System.out.println("Player left the game");
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#setGame(goserver.game.GoGame)
	 */
	public void setGame(GoGame game) {
		this.game = game;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutTurn(goserver.game.GoMoveType)
	 */
	public void notifyAboutTurn(GoMoveType opponentsMove) {
		int turnInfo;
		if (!(opponentsMove == GoMoveType.FIRST)) {
			if (opponentsMove == GoMoveType.MOVE || opponentsMove == GoMoveType.GROUP_CHANGED) {
				turnInfo = ServerRequestSender.MOVE;
			} else if (opponentsMove == GoMoveType.PASS || opponentsMove == GoMoveType.GROUP_NOCHANGE) {
				turnInfo = ServerRequestSender.PASS;
			} else {
				turnInfo = ServerRequestSender.PASS; // undefined
			}
			sender.sendLastTurnInfo(turnInfo, output);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#updateBoard()
	 */
	@Override
	public void updateBoard() {
		sendTurnData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutGamePhaseChange(int)
	 */
	@Override
	public void notifyAboutGamePhaseChange(int gamePhase) {
		sender.sendGamePhase(gamePhase, output);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutGameEnd(double, double)
	 */
	@Override
	public void notifyAboutGameEnd(double playerScore, double opponentScore) {
		if (color == BLACK) {
			sender.sendGameScore(playerScore, opponentScore, output);
		} else {
			sender.sendGameScore(opponentScore, playerScore, output);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#rematchAccepted()
	 */
	@Override
	public void rematchAccepted() {
		sender.sendRematchAccepted(output);
		isRematch = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#rematchDenied()
	 */
	@Override
	public void rematchDenied() {
		sender.sendRematchDenied(output);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutGameBegin()
	 */
	@Override
	public void notifyAboutGameBegin() {
		if (isRematch) {
			sender.sendGameBegin(output);
			sendTurnData();
		}
	}

	/**
	 * Send a message to a player and wait for a reply.
	 * 
	 * @return true if could reach the client and receive a message from it,
	 *         false if it failed.
	 */
	public boolean pingPlayer() {
		try {
			sender.sendPing(output);
			String line = input.readLine();
			if (line == null) {
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			return false;
		}
	}

}
