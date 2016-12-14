package goserver.server;

import java.io.PrintWriter;

public class ServerRequestSender {

	private static ServerRequestSender instance;
	private ServerClientProtocol protocol;

	private ServerRequestSender() {
		protocol = ServerClientProtocol.getInstance();
	}
	
	public synchronized static ServerRequestSender getInstance() {
		if (instance == null) {
			instance = new ServerRequestSender();
		}
		return instance;
	}
	
	public void assignColorToClient(int color, PrintWriter output){
		// 1 - black, 2 - white
		if (color == 1 || color == 2){
			StringBuilder command = new StringBuilder(protocol.ASSIGN_COLOR + " ");
			command.append( ( color == 1 ? protocol.BLACK : protocol.WHITE ) );
			output.println(command.toString());
		} else {
			throw new IllegalArgumentException();
		}
	}

}
