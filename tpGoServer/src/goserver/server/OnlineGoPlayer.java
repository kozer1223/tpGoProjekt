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

import goserver.game.GoGame;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;

/**
 * @author Maciek
 *
 */
public class OnlineGoPlayer extends Thread implements GoPlayer {
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private GoGame game;

	public OnlineGoPlayer(Socket socket, BufferedReader input, PrintWriter output) {
		this.socket = socket;
		this.input = input;
		this.output = output;
	}

	public void run() {

	}

	// @Override
	public void setGame(GoGame game) {
		this.game = game;
	}

	// @Override
	public void notifyAboutTurn(GoMoveType opponentsMove) {
		// TODO Auto-generated method stub

	}

	// @Override
	public void updateBoard() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyAboutGamePhaseChange(int gamePhase) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyAboutGameEnd(double playerScore, double opponentScore) {
		// TODO Auto-generated method stub

	}

}
