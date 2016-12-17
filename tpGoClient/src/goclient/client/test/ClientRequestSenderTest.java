/**
 * 
 */
package goclient.client.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import goclient.client.ClientProtocolParser;
import goclient.client.ClientRequestSender;
import goclient.client.GoGroupType;
import goclient.client.ServerClientProtocol;

/**
 * @author Kacper
 *
 */
public class ClientRequestSenderTest {

	SpyReaderWriter writer;
	ClientRequestSender instance;
	ServerClientProtocol protocol;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		writer = new SpyReaderWriter();
		instance = ClientRequestSender.getInstance();
		protocol = ServerClientProtocol.getInstance();
	}
	
	@Test
	public void testSingleton() {
		assertNotNull(instance);
	}

	@Test
	public void testRequestGameWithPlayer() {
		int size = 5;
		instance.requestGameWithPlayer(size, writer);
		assertTrue(writer.getString().startsWith(protocol.REQUEST_GAME + " " + protocol.PLAYER + " " + size));
	}
	
	@Test
	public void testRequestGameWithBot() {
		int size = 5;
		instance.requestGameWithBot(size, writer);
		assertTrue(writer.getString().startsWith(protocol.REQUEST_GAME + " " + protocol.BOT + " " + size));
	}
	
	@Test
	public void testSendMove() {
		int x = 2;
		int y = 3;
		instance.sendMove(x, y, writer);
		assertTrue(writer.getString().startsWith(protocol.SEND_MOVE + " " + x + " " + y));
	}
	
	@Test
	public void testPassTurn() {
		instance.passTurn(writer);
		assertTrue(writer.getString().startsWith(protocol.PASS_TURN));
	}
	
	@Test
	public void testSendGroupChanges() {
		Map<Integer, GoGroupType> labels = new HashMap<Integer, GoGroupType>();
		labels.put(1, GoGroupType.ALIVE);
		labels.put(3, GoGroupType.DEAD);
		String labelStringData = "1 A 3 D";
		instance.sendGroupChanges(labels, writer);
		assertTrue(writer.getString().startsWith(protocol.CHANGE_GROUP_STATE + " " + labelStringData));
	}

}
