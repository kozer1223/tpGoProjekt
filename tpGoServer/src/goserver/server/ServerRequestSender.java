package goserver.server;

import java.io.PrintWriter;

public class ServerRequestSender {

	private static ServerRequestSender instance;
	private ServerClientProtocol protocol;
	
	public static final int BLACK = 1;
	public static final int WHITE = 2;

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
		if (color == BLACK || color == WHITE){
			StringBuilder command = new StringBuilder(protocol.ASSIGN_COLOR + " ");
			command.append( ( color == BLACK ? protocol.BLACK : protocol.WHITE ) );
			output.println(command.toString());
		} else {
			throw new IllegalArgumentException();
		}
	}

}
