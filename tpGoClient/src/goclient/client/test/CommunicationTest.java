/**
 * 
 */
package goclient.client.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import goclient.client.ClientConfig;
import goclient.client.Communication;

/**
 * @author Kacper
 *
 */
public class CommunicationTest {

	ServerSocket serverSocket = null;
	Socket socket;
	BufferedReader input;
	PrintWriter output;
	Communication communication;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		serverSocket = new ServerSocket(ClientConfig.getInstance().getServerSocket());
		
		communication = new Communication();
		
		socket = serverSocket.accept();
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		socket.close();
		serverSocket.close();
	}

	@Test
	public void testWrite() throws IOException {
		String message = "message";
		communication.write(message);
		String line;
		line = input.readLine();
		assertTrue(line.startsWith(message));
	}
	
	@Test
	public void testRead() throws IOException {
		String message = "message";
		output.println(message);
		String line;
		line = communication.read();
		assertTrue(line.startsWith(message));
	}

}
