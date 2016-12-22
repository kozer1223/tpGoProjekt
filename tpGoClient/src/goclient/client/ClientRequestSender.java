package goclient.client;

import java.io.PrintWriter;
import java.util.Map;

import goclient.client.GoGroupType;

/**
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
	
	public void requestGameWithPlayer(int size, ReaderWriter communication){
		communication.write(protocol.REQUEST_GAME + " " + protocol.PLAYER + " " + size);
	}
	
	public void requestGameWithBot(int size, ReaderWriter communication){
		communication.write(protocol.REQUEST_GAME + " " + protocol.BOT + " " + + size);
	}
	
	public void sendMove(int x, int y, ReaderWriter communication){
		communication.write(protocol.SEND_MOVE + " " + x + " " + y);
	}
	
	public void passTurn(ReaderWriter communication){
		communication.write(protocol.PASS_TURN);
	}
	
	public void sendGroupChanges(Map<Integer, GoGroupType> labels, ReaderWriter communication){
		StringBuilder command = new StringBuilder(protocol.CHANGE_GROUP_STATE + " ");
		for(int label : labels.keySet()){
			command.append(label + " ");
			command.append( (labels.get(label) == GoGroupType.ALIVE ? protocol.ALIVE : protocol.DEAD) + " ");
		}
		communication.write(command.toString());
	}
	
	public void requestRematch(ReaderWriter communication){
		communication.write(protocol.REQUEST_REMATCH);
	}

	public void sendPing(ReaderWriter communication){
		communication.write(protocol.PING);
	}
	
}
