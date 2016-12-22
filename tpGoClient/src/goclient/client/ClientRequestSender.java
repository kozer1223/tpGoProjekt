package goclient.client;

import java.io.PrintWriter;
import java.util.Map;

import goclient.client.GoGroupType;

/**
 * Class sending requests to server.
 * 
 * @author Kacper
 */
public class ClientRequestSender {

	private static ClientRequestSender instance;
	private ServerClientProtocol protocol;

	private ClientRequestSender() {
		protocol = ServerClientProtocol.getInstance();
	}
	
	public synchronized static ClientRequestSender getInstance() {
		if (instance == null) {
			instance = new ClientRequestSender();
		}
		return instance;
	}
	
	/**
	 * Send a request for a game with another player.
	 * @param size Requested board size.
	 * @param communication Communication object.
	 */
	public void requestGameWithPlayer(int size, ReaderWriter communication){
		communication.write(protocol.REQUEST_GAME + " " + protocol.PLAYER + " " + size);
	}
	
	/**
	 * Send a request for a game with a bot.
	 * @param size Requested board size.
	 * @param communication Communication object.
	 */
	public void requestGameWithBot(int size, ReaderWriter communication){
		communication.write(protocol.REQUEST_GAME + " " + protocol.BOT + " " + + size);
	}
	
	/**
	 * Send a request to place a stone in the specified location on the board.
	 * @param x X position on the board.
	 * @param y Y position on the board.
	 * @param communication Communication object.
	 */
	public void sendMove(int x, int y, ReaderWriter communication){
		communication.write(protocol.SEND_MOVE + " " + x + " " + y);
	}
	
	/**
	 * Send a request to pass the turn.
	 * @param communication Communication object.
	 */
	public void passTurn(ReaderWriter communication){
		communication.write(protocol.PASS_TURN);
	}
	
	/**
	 * Send a request to propose group state changes.
	 * @param labels Map of group state changes.
	 * @param communication Communication object.
	 */
	public void sendGroupChanges(Map<Integer, GoGroupType> labels, ReaderWriter communication){
		StringBuilder command = new StringBuilder(protocol.CHANGE_GROUP_STATE + " ");
		for(int label : labels.keySet()){
			command.append(label + " ");
			command.append( (labels.get(label) == GoGroupType.ALIVE ? protocol.ALIVE : protocol.DEAD) + " ");
		}
		communication.write(command.toString());
	}
	
	/**
	 * Send a request for a rematch.
	 * @param communication Communication object.
	 */
	public void requestRematch(ReaderWriter communication){
		communication.write(protocol.REQUEST_REMATCH);
	}

	/**
	 * Send a ping reply to the server.
	 * @param communication Communication object.
	 */
	public void sendPing(ReaderWriter communication){
		communication.write(protocol.PING);
	}
	
}
