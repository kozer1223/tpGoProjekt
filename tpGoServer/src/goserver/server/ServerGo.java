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
import java.util.HashMap;
import java.util.Map;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;

/**
 * @author Maciek
 *
 */
public class ServerGo {

	private static Map<Integer, OnlineGoPlayer> waitingPlayers; // size - player
	private static ServerProtocolParser parser;


	public static void main(String[] args) {
		waitingPlayers = new HashMap<Integer, OnlineGoPlayer>();
		parser = ServerProtocolParser.getInstance();
		try {
			ServerSocket serverSocket = new ServerSocket(ServerConfig.getInstance().getServerSocket());
			while (true) {
				Socket socket = serverSocket.accept();
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
				String line = input.readLine();
				int size;
				System.out.println(line);

				if ((size = parser.parseGameWithBotRequest(line)) > 0) {
					OnlineGoPlayer player = new OnlineGoPlayer(socket, input, output);
					//ServerRequestSender.getInstance().assignColorToClient(ServerRequestSender.BLACK, output);
					GoGame game = GoGameFactory.getInstance().createDefaultGoGameWithBot(player, size);
					System.out.println("Player playing with bot on size " + size);
					player.start();
				}
				if ((size = parser.parseGameWithPlayerRequest(line)) > 0) {
					OnlineGoPlayer player = new OnlineGoPlayer(socket, input, output);
					if (waitingPlayers.containsKey(size) && waitingPlayers.get(size) != null && waitingPlayers.get(size).pingPlayer()) {
						//ServerRequestSender.getInstance().assignColorToClient(ServerRequestSender.WHITE, output);
						GoGame game = GoGameFactory.getInstance()
								.createDefaultGoGameWithTwoPlayers(waitingPlayers.get(size), player, size);
						System.out.println("Paired 2 players for size " + size);
						waitingPlayers.get(size).start();
						player.start();
						waitingPlayers.remove(size);
					} else {
						waitingPlayers.put(size, player);
						//ServerRequestSender.getInstance().assignColorToClient(ServerRequestSender.BLACK, output);
						System.out.println("Player waiting for size " + size);
					}
				}

				System.out.println(size);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
