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
 * Main server class.
 * 
 * @author Maciek
 *
 */
public class ServerGo {

	private static Map<Integer, OnlineGoPlayer> waitingPlayers; // size - player
	private static ServerProtocolParser parser;

	public static void main(String[] args) {
		waitingPlayers = new HashMap<Integer, OnlineGoPlayer>();
		parser = ServerProtocolParser.getInstance();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(ServerConfig.getInstance().getServerSocket());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Welcome to the Go Server.\nWaiting for players.");
		while (true) {
			try {
				// accept new player
				Socket socket;
				socket = serverSocket.accept();
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
				String line = input.readLine();
				int size;

				// with bot
				if ((size = parser.parseGameWithBotRequest(line)) > 0) {
					OnlineGoPlayer player = new OnlineGoPlayer(socket, input, output);
					GoGame game = GoGameFactory.getInstance().createDefaultGoGameWithBot(player, size);
					System.out.println("Player playing with bot on size " + size);
					player.start();
				}

				// with player
				if ((size = parser.parseGameWithPlayerRequest(line)) > 0) {
					OnlineGoPlayer player = new OnlineGoPlayer(socket, input, output);
					if (waitingPlayers.containsKey(size) && waitingPlayers.get(size) != null
							&& waitingPlayers.get(size).pingPlayer()) {
						// there is a player waiting
						GoGame game = GoGameFactory.getInstance()
								.createDefaultGoGameWithTwoPlayers(waitingPlayers.get(size), player, size);
						System.out.println("Paired 2 players for size " + size);
						waitingPlayers.get(size).start();
						player.start();
						waitingPlayers.remove(size);
					} else {
						// no waiting players
						waitingPlayers.put(size, player);
						System.out.println("Player waiting for size " + size);
					}
				}

				System.out.println(size);
			} catch (IOException e) {
				System.out.println("Lost connection.");
			}
		}

	}

}
