package goclient.client;

public interface ReaderWriter {

	void write(String message);
	String read();

}