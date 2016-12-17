package goserver.server;

/**
 * @author Kacper
 */
public class ServerClientProtocol {

	private static ServerClientProtocol instance;

	private ServerClientProtocol() {}
	
	public synchronized static ServerClientProtocol getInstance() {
		if (instance == null) {
			instance = new ServerClientProtocol();
		}
		return instance;
	}
	
	// -- Klient -> Serwer
	
	// prosba klienta o gre:
	// REQUEST [PLAYER|BOT] [size]
	public final String REQUEST_GAME = "REQUEST";
	
	// wyslanie ruchu
	// MOVE [x] [y]
	public final String SEND_MOVE = "MOVE";
	
	// wyslanie informacji o spasowaniu
	// PASS
	public final String PASS_TURN = "PASS";
	
	// wyslanie oznaczenia grupy
	// CHANGE_STATE [x1] [A|D] [x2] [A|D] ...
	public final String CHANGE_GROUP_STATE = "CHANGE_STATE";
	
	// -- Serwer -> Klient
	
	// informacja o kolorze gracza
	// COLOR [BLACK|WHITE]
	public final String ASSIGN_COLOR = "COLOR";
	
	// informacja o rozpoczeciu gry
	// GAME_BEGIN
	public final String GAME_BEGIN = "BEGIN";
	
	// wyslanie stanu planszy
	// BOARD 0001012020120210012...
	public final String SEND_BOARD = "BOARD";
	
	// wyslanie informacji o zaakcpetowanym ruchu
	// ACCEPTED
	public final String MOVE_ACCEPTED = "ACCEPTED";
	
	// informacja o zmianie fazy gry
	// GAME_PHASE [0|1]
	public final String SEND_PHASE = "GAME_PHASE";
	
	// wyslanie planszy z oznaczeniami grup
	// LABEL_BOARD 0010030402022...
	public final String SEND_LABELED_BOARD = "LABEL_BOARD";
	
	// wyslanie stanu grup
	// GROUP [x1] [A|D] [x2] [A|D] (np. GROUP 1 D 2 A 3 D 4 D...)
	public final String SEND_GROUP_STATE = "GROUP_STATE";
	
	// wyslanie wyniku gry
	// SCORE [BLACK's score] [WHITE's score]
	public final String SEND_SCORE = "SCORE";
	
	// wyslanie ilosci jencow
	// CAPTURED [BLACK's captured stones] [WHITE's captured stones]
	public final String SEND_CAPTURED_STONES = "CAPTURED";
	
	// wyslanie wiadomosci (w formie tekstu)
	// MESSAGE [message (string)]
	public final String SEND_MESSAGE = "MESSAGE";
	
	// misc.
	
	public final String PLAYER = "PLAYER";
	public final String BOT = "BOT";
	
	public final String BLACK = "BLACK";
	public final String WHITE = "WHITE";
	
	public final String ALIVE = "A";
	public final String DEAD = "D";

}
