/**
 * 
 */
package goclient.client.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import goclient.client.ClientProtocolParser;
import goclient.client.ServerClientProtocol;

/**
 * @author Kacper
 *
 */
public class ClientProtocolParserTest {

	ClientProtocolParser instance;
	ServerClientProtocol protocol;
	
	@Before
	public void setUp() throws Exception {
		instance = ClientProtocolParser.getInstance();
		protocol = ServerClientProtocol.getInstance();
	}

	@Test
	public void testSingleton() {
		assertNotNull(instance);
	}
	
	@Test
	public void testParseColor() {
		String color = "BLACK";
		assertEquals(color, instance.parseColor(protocol.ASSIGN_COLOR + " " + color));
	}
	
	@Test
	public void testParseBeginGame() {
		assertTrue(instance.parseBeginGame(protocol.GAME_BEGIN));
	}

}
