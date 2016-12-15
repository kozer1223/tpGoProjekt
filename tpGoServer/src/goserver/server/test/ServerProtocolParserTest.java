package goserver.server.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import goserver.server.GoGameFactory;
import goserver.server.ServerClientProtocol;
import goserver.server.ServerProtocolParser;

public class ServerProtocolParserTest {
	
	ServerProtocolParser instance;
	ServerClientProtocol protocol;
	
	@Before
	public void setUp() throws Exception {
		instance = ServerProtocolParser.getInstance();
		protocol = ServerClientProtocol.getInstance();
	}

	@Test
	public void testSingleton() {
		assertNotNull(instance);
	}
	
	@Test
	public void testParseGameWithPlayerRequest() {
		int size = 4;
		assertEquals(size, instance.parseGameWithPlayerRequest(protocol.REQUEST_GAME + " " + protocol.PLAYER + " " + size));
	}
	
	@Test
	public void testParseGameWithPlayerRequestWrong() {
		int size = 4;
		assertEquals(-1, instance.parseGameWithPlayerRequest(protocol.REQUEST_GAME + " " + protocol.BOT + " " + size));
	}
	
	@Test
	public void testParseGameWithBotRequest() {
		int size = 4;
		assertEquals(size, instance.parseGameWithBotRequest(protocol.REQUEST_GAME + " " + protocol.BOT + " " + size));
	}
	
	@Test
	public void testParseGameWithBotRequestWrong() {
		int size = 4;
		assertEquals(-1, instance.parseGameWithBotRequest(protocol.REQUEST_GAME + " " + protocol.PLAYER + " " + size));
	}

}
