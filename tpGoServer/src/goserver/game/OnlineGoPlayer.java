/**
 * 
 */
package goserver.game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Maciek
 *
 */
public class OnlineGoPlayer extends Thread implements GoPlayer {
	Socket socket;
	public OnlineGoPlayer(Socket socket) {
		this.socket=socket;
	}
	public void run() {
		//TODO
	}
	@Override
	public void setGame(GoGame game) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyAboutTurn() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateBoard() {
		// TODO Auto-generated method stub
		
	}
	
}
