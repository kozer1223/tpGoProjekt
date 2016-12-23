package goclient.client;

public interface ReaderWriter {

	/**
	 * Send an outgoing message.
	 * 
	 * @param message
	 */
	void write(String message);

	/**
	 * Read an incoming message.
	 * 
	 * @return A single line.
	 */
	String read();

}