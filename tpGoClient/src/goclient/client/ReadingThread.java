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
	public ReadingThread(int size,BoardFrame frame) {
		this.size = size;
		this.frame=frame;
	}
	public void run() {
		ReaderWriter communication = GUI.getCommunication();
		ClientProtocolParser parser = ClientProtocolParser.getInstance();
		while(true){
			if(communication.isReadReady()) {
				String line=communication.read();
				if(parser.parseBoard(line, size)!=null) {
					frame.drawBoard(parser.parseBoard(line, size));
				}
			}
		}
	}
}
