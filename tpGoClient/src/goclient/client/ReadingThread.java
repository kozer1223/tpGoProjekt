/**
 * 
 */
package goclient.client;

/**
 * @author maciek
 *
 */
public class ReadingThread extends Thread {

	private int size;
	private BoardFrame frame;
	private ReaderWriter communication;

	public ReadingThread(int size, BoardFrame frame) {
		this.size = size;
		this.frame = frame;
	}
	
	public void setCommunication(ReaderWriter communication){
		this.communication = communication;
	}

	public void run() {
		ClientProtocolParser parser = ClientProtocolParser.getInstance();
		while (true) {
			String line = communication.read();
			if (line != null) {
				if (parser.parseBeginGame(line)){
					// begin game
					frame.beginGame();
				} else if (parser.parseColor(line) != ""){
					// get color
					frame.setColor(parser.parseColor(line));
					if (parser.parseColor(line).equals(ServerClientProtocol.getInstance().BLACK)){
						frame.enableInput();
					}
				} else if (parser.parseBoard(line, size) != null) {
					// update board
					frame.drawBoard(parser.parseBoard(line, size));
				} else if (parser.parsePhase(line)!=-1){
					// new phase
					frame.setPhase(parser.parsePhase(line));
				} else if (parser.parseCapturedStones(line)!=null){
					// captured stones information
					frame.setCapturedStones(parser.parseCapturedStones(line));
				} else if (parser.parseMessage(line) != null){
					// invalid move message
					frame.setMessage(parser.parseMessage(line));
					frame.enableInput();
				} else if (parser.parseMoveAccepted(line)){
					// move accepted
					frame.setMessage(""); //nothing to do here
				} else if (parser.parseLastTurnInform(line) != -1){
					// opponent's move
					frame.enableInput();
				} else if (parser.parseLabeledBoard(line, size) != null){
					// labeled board
					frame.setGroupLabels(parser.parseLabeledBoard(line, size));
				} else if (parser.parseGroupState(line) != null){
					// update group states
					frame.setGroupStates(parser.parseGroupState(line));
				} else if (parser.parseLockedGroups(line) != null){
					// update locked group
					frame.setLockedGroups(parser.parseLockedGroups(line));
				} else if (parser.parseScore(line) != null){
					// show score
					frame.showScore(parser.parseScore(line));
				} else if (parser.parseRematchDenied(line)){
					// opponent denied a rematch
					frame.rematchDenied();
				} else if (parser.parseRematchAccepted(line)){
					// opponent accepted a rematch
					frame.rematchAccepted();
				} else if (parser.parsePing(line)){
					// server sent a ping message, send ping back
					ClientRequestSender.getInstance().sendPing(communication);
				}
			}
		}
	}
}
