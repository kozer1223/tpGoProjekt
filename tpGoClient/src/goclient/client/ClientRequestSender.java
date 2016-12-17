package goclient.client;

/**
 * @author Kacper
 */
public class ClientRequestSender {

	private static ClientRequestSender instance;
	private ServerClientProtocol protocol;
	private ReaderWriter communication;

	private ClientRequestSender() {
		protocol = ServerClientProtocol.getInstance();
		communication = GUI.getCommunication();
	}
	
	public synchronized static ClientRequestSender getInstance() {
		if (instance == null) {
			instance = new ClientRequestSender();
		}
		return instance;
	}
	
	public void requestGameWithPlayer(int size){
		communication.write(protocol.REQUEST_GAME + " " + protocol.PLAYER + " " + size);
	}
	
	public void requestGameWithBot(int size){
		communication.write(protocol.REQUEST_GAME + " " + protocol.BOT + " " + + size);
	}
	
	public void sendMove(int size){
		communication.write(protocol.REQUEST_GAME + " " + protocol.BOT + " " + + size);
	}

}
