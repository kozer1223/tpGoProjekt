/**
 * 
 */
package goclient.client;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import goclient.client.GoGroupType;
import goclient.util.DoublePair;
import goclient.util.IntPair;

/**
 * @author Kacper
 */
public class ClientProtocolParser {

	public static ClientProtocolParser instance;

	private ServerClientProtocol protocol;
	
	public static final int MOVE = 3;
	public static final int PASS = 4;

	private ClientProtocolParser() {
		protocol = ServerClientProtocol.getInstance();
	}

	public synchronized static ClientProtocolParser getInstance() {
		if (instance == null) {
			instance = new ClientProtocolParser();
		}
		return instance;
	}

	public String parseColor(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.ASSIGN_COLOR);
			String color = scanner.next();
			scanner.close();
			return color;
		} catch (InputMismatchException e){
			scanner.close();
			return "";
		}
	}
	
	public boolean parseBeginGame(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.GAME_BEGIN);
			scanner.close();
			return true;
		} catch (InputMismatchException e){
			return false;
		}
	}
	
	public int[][] parseBoard(String line, int size){
		Scanner scanner = new Scanner(line);
		int[][] board = new int[size][size];
		
		try {
			scanner.next(protocol.SEND_BOARD);
			String boardData = scanner.next();
			int i = 0, j = 0;
			for(int t=0; t<boardData.length(); t++){
				board[i][j] = boardData.charAt(t) - '0';
				j++;
				if(j >= size){
					j = 0;
					i++;
				}
				if (i >= size){
					break;
				}
			}
			scanner.close();
			return board;
		} catch (InputMismatchException e){
			return null;
		}
	}
	
	public int parsePhase(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.SEND_PHASE);
			int phase = scanner.nextInt();
			scanner.close();
			return phase;
		} catch (InputMismatchException e){
			scanner.close();
			return -1;
		}
	}
	
	public int[][] parseLabeledBoard(String line, int size){
		Scanner scanner = new Scanner(line);
		int[][] board = new int[size][size];
		
		try {
			scanner.next(protocol.SEND_LABELED_BOARD);
			int i = 0, j = 0;
			while(scanner.hasNext()){
				board[i][j] = scanner.nextInt();
				j++;
				if(j >= size){
					j = 0;
					i++;
				}
				if (i >= size){
					break;
				}
			}
			scanner.close();
			return board;
		} catch (InputMismatchException e){
			return null;
		}
	}
	
	public Map<Integer, GoGroupType> parseGroupState(String line) {
		Scanner scanner = new Scanner(line);
		Map<Integer, GoGroupType> changes = new HashMap<Integer, GoGroupType>();
		
		try {
			scanner.next(protocol.SEND_GROUP_STATE);
			while (scanner.hasNext()){
				int label = scanner.nextInt();
				String token = scanner.next();
				if (token.equals(protocol.ALIVE)){
					changes.put(label, GoGroupType.ALIVE);
				} else if (token.equals(protocol.DEAD)){
					changes.put(label, GoGroupType.DEAD);
				} else {
					scanner.close();
					return null;
				}
			}
			scanner.close();
			return changes;
		} catch (InputMismatchException e){
			scanner.close();
			return null;
		}
	}
	
	public DoublePair parseScore(String line){
		Scanner scanner = new Scanner(line);
		scanner.useLocale(Locale.US); //kropka jako separator
		
		try {
			scanner.next(protocol.SEND_SCORE);
			double blackScore, whiteScore;
			blackScore = scanner.nextDouble();
			whiteScore = scanner.nextDouble();
			scanner.close();
			return new DoublePair(blackScore, whiteScore);
		} catch (InputMismatchException e){
			scanner.close();
			return null;
		}
	}
	
	public IntPair parseCapturedStones(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.SEND_CAPTURED_STONES);
			int blackStones, whiteStones;
			blackStones = scanner.nextInt();
			whiteStones = scanner.nextInt();
			scanner.close();
			return new IntPair(blackStones, whiteStones);
		} catch (InputMismatchException e){
			scanner.close();
			return null;
		}
	}
	
	public String parseMessage(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.SEND_MESSAGE);
			String message = scanner.next(); 
			scanner.close();
			return message;
		} catch (InputMismatchException e){
			scanner.close();
			return null;
		}
	}
	
	public int parseLastTurnInform(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.LAST_MOVE);
			String turn = scanner.next();
			scanner.close();
			if(turn.startsWith(protocol.MOVE)){
				return MOVE;
			} else if (turn.startsWith(protocol.PASS)){
				return PASS;
			} else {
				return -1;
			}
		} catch (InputMismatchException e){
			scanner.close();
			return -1;
		}
	}
	
	public boolean parseRematchAccepted(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.REMATCH_ACCEPTED);
			scanner.close();
			return true;
		} catch (InputMismatchException e){
			return false;
		}
	}
	
	public boolean parseRematchDenied(String line){
		Scanner scanner = new Scanner(line);
		
		try {
			scanner.next(protocol.REMATCH_DENIED);
			scanner.close();
			return true;
		} catch (InputMismatchException e){
			return false;
		}
	}
	
	
}
