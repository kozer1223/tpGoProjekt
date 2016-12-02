package goclient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Comunication {

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	public Comunication () {
		try {
			socket =  new Socket("localhost",8888);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
		}
		catch(Exception e) {}
	}
	public void write(String message) {
		output.println(message);
	}
	public String read() {
		String line=null;
		try {
			line = input.readLine();
		} catch (IOException e) {
		}
		return line;
	}
}
