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

	public ReadingThread(int size, BoardFrame frame) {
		this.size = size;
		this.frame = frame;
	}

	public void run() {
		ReaderWriter communication = GUI.getCommunication();
		ClientProtocolParser parser = ClientProtocolParser.getInstance();
		while (true) {
			String line = communication.read();
			if (line != null) {
				System.out.println("[SERVER]> " + line);
				if (parser.parseBeginGame(line)){
					frame.beginGame();
				} else if (parser.parseColor(line) != ""){
					frame.setColor(parser.parseColor(line));
					if (parser.parseColor(line).equals(ServerClientProtocol.getInstance().BLACK)){
						frame.enableInput();
					}
				} else if (parser.parseBoard(line, size) != null) {
					frame.drawBoard(parser.parseBoard(line, size));
				} else if (parser.parsePhase(line)!=-1){
					frame.setPhase(parser.parsePhase(line));
				} else if (parser.parseCapturedStones(line)!=null){
					frame.setCapturedStones(parser.parseCapturedStones(line));
				} else if (parser.parseMessage(line) != null){
					frame.setMessage(parser.parseMessage(line));
					frame.enableInput();
				} else if (parser.parseMoveAccepted(line)){
					frame.setMessage(""); //nothing to do here
				} else if (parser.parseLastTurnInform(line) != -1){
					frame.enableInput();
				} else if (parser.parseLabeledBoard(line, size) != null){
					frame.setGroupLabels(parser.parseLabeledBoard(line, size));
				} else if (parser.parseGroupState(line) != null){
					frame.setGroupStates(parser.parseGroupState(line));
				} else if (parser.parseLockedGroups(line) != null){
					frame.setLockedGroups(parser.parseLockedGroups(line));
				} else if (parser.parseScore(line) != null){
					frame.showScore(parser.parseScore(line));
				} else if (parser.parseRematchDenied(line)){
					frame.rematchDenied();
				} else if (parser.parseRematchAccepted(line)){
					frame.rematchAccepted();
				} else if (parser.parsePing(line)){
					ClientRequestSender.getInstance().sendPing(communication);
				}
			}
		}
	}
}
