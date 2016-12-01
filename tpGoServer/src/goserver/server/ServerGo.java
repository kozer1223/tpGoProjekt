/**
 * 
 */
package goserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import goserver.game.OnlineGoPlayer;
/**
 * @author Maciek
 *
 */
public class ServerGo {

	private static OnlineGoPlayer waitingPlayer19 = null;
	private static OnlineGoPlayer waitingPlayer13 = null;
	private static OnlineGoPlayer waitingPlayer9 = null;
	//random comment
	public static void main(String[] args) {
		try{
		ServerSocket serverSocket = new ServerSocket(8888);
		while(true) {
			Socket socket = serverSocket.accept();
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream());
			String line=input.readLine();
			if(line=="bot") {
				OnlineGoPlayer player = new OnlineGoPlayer(socket);
				player.run();
			}
			else if(line=="gracz") {
				line=input.readLine();
				if(line=="19"){
					if(waitingPlayer19==null) {
						waitingPlayer19=new OnlineGoPlayer(socket);
					}
					else {
						OnlineGoPlayer player = new OnlineGoPlayer(socket);
						player.run();
						waitingPlayer19.run();
						waitingPlayer19 = null;						
					}
				}
				else if(line=="13") {
					if(waitingPlayer13==null) {
						waitingPlayer13=new OnlineGoPlayer(socket);
					}
					else {
						OnlineGoPlayer player = new OnlineGoPlayer(socket);
						player.run();
						waitingPlayer13.run();
						waitingPlayer13 = null;						
					}
				}
				else if(line=="9") {
					if(waitingPlayer9==null) {
						waitingPlayer9=new OnlineGoPlayer(socket);
					}
					else {
						OnlineGoPlayer player = new OnlineGoPlayer(socket);
						player.run();
						waitingPlayer9.run();
						waitingPlayer9 = null;						
					}
				}
			}
			
		}
		} catch (Exception e) {
		}
	}

}
