/**
 * 
 */
package goserver.server.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import goserver.server.ServerClientProtocol;
import goserver.server.ServerProtocolParser;
import goserver.server.ServerRequestSender;

/**
 * @author Kacper
 *
 */
public class ServerRequestSenderTest {

	ServerRequestSender instance;
	ServerClientProtocol protocol;
	
	@Before
	public void setUp() throws Exception {
		instance = ServerRequestSender.getInstance();
		protocol = ServerClientProtocol.getInstance();
	}

	@Test
	public void testSendAssignBlackColor() {
		StringWriter writer = new StringWriter();
		instance.assignColorToClient(ServerRequestSender.BLACK, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.ASSIGN_COLOR + " " + protocol.BLACK));
	}
	
	@Test
	public void testSendAssignWhiteColor() {
		StringWriter writer = new StringWriter();
		instance.assignColorToClient(ServerRequestSender.WHITE, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.ASSIGN_COLOR + " " + protocol.WHITE));
	}


}
