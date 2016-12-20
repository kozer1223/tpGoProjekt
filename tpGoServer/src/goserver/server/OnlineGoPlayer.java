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

	private static final int BLACK = 1;
	private static final int WHITE = 2;

	public OnlineGoPlayer(Socket socket, BufferedReader input, PrintWriter output) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		parser = ServerProtocolParser.getInstance();
		sender = ServerRequestSender.getInstance();
	}

	protected GoPlayer getOpposingPlayer() {
		return game.getOpposingPlayer(this);
	}

	private void sendTurnData() {
		if (game != null) {
			if (game.isStonePlacingPhase()) {
				sender.sendBoardData(game.getBoard().getBoard(), output);
				sender.sendCapturedStones(game.getPlayersCapturedStones(game.getPlayer1()),
						game.getPlayersCapturedStones(game.getPlayer2()), output);
			} else {
				sender.sendLabeledBoardData(game.getBoard().getBoardWithLabeledGroups(), output);
				sender.sendGroupStateData(game.getLabelsMap(), output);
			}
		}
	}

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
					//if (game.isPlayersTurn(this)) {
						String line = input.readLine();
						if (line != null) {
							System.out.println("[PLAYER]>" + line);

							IntPair move;
							Map<Integer, GoGroupType> changes;
							try {
								if ((move = parser.parseMove(line)) != null) {
									try {
										game.makeMove(this, move.x, move.y);
										sender.sendMoveAccepted(output);
									} catch (InvalidMoveException e) {
										sender.sendMessage(e.getMessage(), output);
									}
								} else if (parser.parsePassTurn(line)) {
									if (game.isStonePlacingPhase()){
										game.passTurn(this);
									} else {
										//pas jako zgodzenie sie z propozycja grup
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
					//}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Lost connection with player");
					game.leaveGame(this);
					System.out.println("Player left the game");
					break;
				}
			}
		}
	}

	// @Override
	public void setGame(GoGame game) {
		this.game = game;
	}

	// @Override
	public void notifyAboutTurn(GoMoveType opponentsMove) {
		// TODO Auto-generated method stub
		int turnInfo;
		if (!(opponentsMove == GoMoveType.FIRST)) {
			if (opponentsMove == GoMoveType.MOVE || opponentsMove == GoMoveType.GROUP_CHANGED) {
				turnInfo = ServerRequestSender.MOVE;
			} else if (opponentsMove == GoMoveType.PASS || opponentsMove == GoMoveType.GROUP_NOCHANGE) {
				turnInfo = ServerRequestSender.PASS;
			} else {
				turnInfo = ServerRequestSender.PASS; // nieokreslony przypadek
			}
			sender.sendLastTurnInfo(turnInfo, output);
		}

	}

	// @Override
	public void updateBoard() {
		// TODO Auto-generated method stub
		sendTurnData();
	}

	@Override
	public void notifyAboutGamePhaseChange(int gamePhase) {
		// TODO Auto-generated method stub
		sender.sendGamePhase(gamePhase, output);
	}

	@Override
	public void notifyAboutGameEnd(double playerScore, double opponentScore) {
		// TODO Auto-generated method stub
		if (color == BLACK) {
			sender.sendGameScore(playerScore, opponentScore, output);
		} else {
			sender.sendGameScore(opponentScore, playerScore, output);
		}

	}

}
