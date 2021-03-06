package goclient.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class used to communicate with the server.
 * 
 * @author Maciek
 *
 */
public class Communication implements ReaderWriter {

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	public Communication() {
		try {
			socket = new Socket(ClientConfig.getInstance().getServerAddress(), ClientConfig.getInstance().getServerSocket());
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			System.err.println("Cannot connect to the server");
			System.exit(1);
		}
	}

	/* (non-Javadoc)
	 * @see goclient.ReaderWriter#write(java.lang.String)
	 */
	@Override
	public void write(String message) {
		output.println(message);
	}

	/* (non-Javadoc)
	 * @see goclient.ReaderWriter#read()
	 */
	@Override
	public String read() {
		String line = null;
		try {
			if(input.ready()){
				line = input.readLine();
			}

		} catch (IOException e) {
			System.err.println("Lost connection");
			System.exit(1);
		}
		return line;
	}
	
}
