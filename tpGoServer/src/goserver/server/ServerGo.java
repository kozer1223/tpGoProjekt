/**
 * 
 */
package goserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
/**
 * @author Maciek
 *
 */
public class ServerGo {

	private static OnlineGoPlayer waitingPlayer19 = null;
	private static OnlineGoPlayer waitingPlayer13 = null;
	private static OnlineGoPlayer waitingPlayer9 = null;

	public static void main(String[] args) {
		try{
		ServerSocket serverSocket = new ServerSocket(8888);
		while(true) {
			Socket socket = serverSocket.accept();
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
			String line=input.readLine();
			if(line.compareTo("bot")==0) {
				OnlineGoPlayer player = new OnlineGoPlayer(socket,input,output);
				player.run();
			}
			else if(line.compareTo("player")==0) {
				line=input.readLine();
				if(line.compareTo("19")==0){
					if(waitingPlayer19==null) {
						output.println("black");
						waitingPlayer19=new OnlineGoPlayer(socket,input,output);
					}
					else {
						output.println("white");
						OnlineGoPlayer player = new OnlineGoPlayer(socket,input,output);
						GoGame game = GoGameFactory.getInstance().createDefaultGoGameWithTwoPlayers(waitingPlayer19, player, 19);
						player.run();
						waitingPlayer19.run();
						waitingPlayer19 = null;
					}
				}
				else if(line.compareTo("13")==0) {
					if(waitingPlayer13==null) {
						output.println("black");
						waitingPlayer13=new OnlineGoPlayer(socket,input,output);
					}
					else {
						output.println("white");
						OnlineGoPlayer player = new OnlineGoPlayer(socket,input,output);
						GoGame game = GoGameFactory.getInstance().createDefaultGoGameWithTwoPlayers(waitingPlayer19, player, 13);
						player.run();
						waitingPlayer13.run();
						waitingPlayer13 = null;						
					}
				}
				else if(line.compareTo("9")==0) {
					if(waitingPlayer9==null) {
						output.println("black");
						waitingPlayer9=new OnlineGoPlayer(socket,input,output);
					}
					else {
						output.println("white");
						OnlineGoPlayer player = new OnlineGoPlayer(socket,input,output);
						GoGame game = GoGameFactory.getInstance().createDefaultGoGameWithTwoPlayers(waitingPlayer19, player, 9);
						player.run();
						waitingPlayer9.run();
						waitingPlayer9 = null;						
					}
				}
			}
			
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
