package goserver.server;

import java.io.PrintWriter;
import java.util.Map;

import goserver.game.GoGroupType;

public class ServerRequestSender {

	private static ServerRequestSender instance;
	private ServerClientProtocol protocol;
	
	public static final int BLACK = 1;
	public static final int WHITE = 2;
	
	public static final int MOVE = 3;
	public static final int PASS = 4;

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
			System.out.println(command.toString());
			output.println(command.toString());
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void sendGameBegin(PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.GAME_BEGIN);
		output.println(command.toString());
	}
	
	public void sendBoardData(int[][] board, PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.SEND_BOARD + " ");
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				command.append(board[i][j]);
			}
		}
		output.println(command.toString());
	}
	
	public void sendMoveAccepted(PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.MOVE_ACCEPTED);
		output.println(command.toString());
	}
	
	public void sendGamePhase(int phase, PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.SEND_PHASE + " ");
		command.append(phase);
		output.println(command.toString());
	}
	
	public void sendLabeledBoardData(int[][] board, PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.SEND_LABELED_BOARD + " ");
		for(int i=0; i<board.length; i++){
			for(int j=0; j<board[0].length; j++){
				command.append(board[i][j] + " ");
			}
		}
		output.println(command.toString());
	}
	
	public void sendGroupStateData(Map<Integer, GoGroupType> labels, PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.SEND_GROUP_STATE + " ");
		for(int label : labels.keySet()){
			command.append(label + " ");
			command.append( (labels.get(label) == GoGroupType.ALIVE ? protocol.ALIVE : protocol.DEAD) + " ");
		}
		output.println(command.toString());
	}
	
	public void sendGameScore(double blackScore, double whiteScore, PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.SEND_SCORE + " ");
		command.append(blackScore + " ");
		command.append(whiteScore);
		output.println(command.toString());
	}
	
	public void sendCapturedStones(int blackStones, int whiteStones, PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.SEND_CAPTURED_STONES + " ");
		command.append(blackStones + " ");
		command.append(whiteStones);
		output.println(command.toString());
	}
	
	public void sendMessage(String message, PrintWriter output){
		StringBuilder command = new StringBuilder(protocol.SEND_MESSAGE + " ");
		command.append(message);
		output.println(command.toString());
	}
	
	public void sendLastTurnInfo(int turnInfo, PrintWriter output){
		if (turnInfo == MOVE || turnInfo == PASS){
			StringBuilder command = new StringBuilder(protocol.LAST_MOVE + " ");
			command.append( ( turnInfo == MOVE ? protocol.MOVE : protocol.PASS ) );
			output.println(command.toString());
		} else {
			throw new IllegalArgumentException();
		}
	}
}
