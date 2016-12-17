package goclient.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Communication implements ReaderWriter {

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	public Communication() {
		try {
			socket = new Socket(ClientConfig.HOST_SERVER, ClientConfig.SERVER_SOCKET);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
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
			line = input.readLine();
		} catch (IOException e) {
		}
		return line;
	}
}
