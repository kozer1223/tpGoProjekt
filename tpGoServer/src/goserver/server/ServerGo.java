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

import goserver.game.OnlineGoPlayer;
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
		ServerSocket serverSocket = new ServerSocket(8888,0,InetAddress.getByName(null));
		while(true) {
			Socket socket = serverSocket.accept();
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
			String line=input.readLine();
			if(line.compareTo("bot")==0) {
				OnlineGoPlayer player = new OnlineGoPlayer(socket);
				player.run();
			}
			else if(line.compareTo("player")==0) {
				line=input.readLine();
				if(line.compareTo("19")==0){
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
				else if(line.compareTo("13")==0) {
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
				else if(line.compareTo("9")==0) {
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
