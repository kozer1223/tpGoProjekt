package goserver.server.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import goserver.game.GoGroupType;
import goserver.server.GoGameFactory;
import goserver.server.ServerClientProtocol;
import goserver.server.ServerProtocolParser;
import goserver.server.ServerRequestSender;
import goserver.util.IntPair;

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
	
	@Test
	public void testParseMove() {
		int x = 2;
		int y = 3;
		assertEquals(new IntPair(x, y), instance.parseMove(protocol.SEND_MOVE + " " + x + " " + y));
	}
	
	@Test
	public void testParseMoveWrongInput() {
		assertNull(instance.parseMove(protocol.SEND_MOVE + " 1"));
	}
	
	@Test
	public void testParseMoveWrongInputComand() {
		assertNull(instance.parseMove("PASS 1"));
	}
	
	@Test
	public void testParsePass() {
		assertTrue(instance.parsePassTurn(protocol.PASS_TURN));
	}
	
	@Test
	public void testParsePassWrongInput() {
		assertFalse(instance.parsePassTurn(protocol.SEND_MOVE));
	}
	
	@Test
	public void testParseGroupChange() {
		StringWriter writer = new StringWriter();
		Map<Integer, GoGroupType> labels = new HashMap<Integer, GoGroupType>();
		labels.put(1, GoGroupType.ALIVE);
		labels.put(3, GoGroupType.DEAD);
		ServerRequestSender.getInstance().sendGroupStateData(labels, new PrintWriter(writer));
		assertEquals(labels, instance.parseGroupStateChange(writer.toString().replace(protocol.SEND_GROUP_STATE, protocol.CHANGE_GROUP_STATE)));
	}

}
