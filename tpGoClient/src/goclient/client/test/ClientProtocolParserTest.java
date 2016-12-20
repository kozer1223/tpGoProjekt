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
import goclient.client.GoGroupType;
import goclient.client.ServerClientProtocol;
import goclient.util.DoublePair;
import goclient.util.IntPair;
import goclient.util.MatrixUtil;

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
	
	@Test
	public void testParseBeginGameWrongInput() {
		assertFalse(instance.parseBeginGame("aaaa"));
	}
	
	@Test
	public void testParseBoard() {
		int board[][] = {{0, 1, 0, 1},
				         {1, 1, 1, 1},
				         {0, 0, 1, 1},
				         {1 ,1 ,1, 0}};
		int size = board.length;
		String boardString = "0101111100111110";
		assertTrue(MatrixUtil.compareMatrix(board, instance.parseBoard(protocol.SEND_BOARD + " " + boardString, size)));
	}
	
	@Test
	public void testParsePhase() {
		int phase = 2;
		assertEquals(phase, instance.parsePhase(protocol.SEND_PHASE + " " + phase));
	}
	
	@Test
	public void testParseLabeledBoard() {
		int[][] board = { {0, 0, 0, 0},
						  {1, 1, 0, 2},
						  {2, 1, 1, 2},
						  {0, 2, 0, 1}};
		int size = board.length;
		String boardString = "0 0 0 0 1 1 0 2 2 1 1 2 0 2 0 1";
		assertTrue(MatrixUtil.compareMatrix(board, instance.parseLabeledBoard(protocol.SEND_LABELED_BOARD + " " + boardString, size)));
	}
	
	@Test
	public void testParseGroupState() {
		Map<Integer, GoGroupType> labels = new HashMap<Integer, GoGroupType>();
		labels.put(1, GoGroupType.ALIVE);
		labels.put(3, GoGroupType.DEAD);
		String labelStringData = "1 A 3 D";
		assertEquals(labels, instance.parseGroupState(protocol.SEND_GROUP_STATE + " " + labelStringData));
	}
	
	@Test
	public void testParseScore() {
		double x = 2;
		double y = 3.5;
		DoublePair scores = new DoublePair(x, y);
		assertEquals(scores, instance.parseScore(protocol.SEND_SCORE + " " + x + " " + y));
	}
	
	@Test
	public void testParseCapturedStones() {
		int x = 5;
		int y = 10;
		IntPair scores = new IntPair(x, y);
		assertEquals(scores, instance.parseCapturedStones(protocol.SEND_CAPTURED_STONES + " " + x + " " + y));
	}
	
	@Test
	public void testParseMessage() {
		String message = "Message";
		assertEquals(message, instance.parseMessage(protocol.SEND_MESSAGE + " " + message));
	}
	
	@Test
	public void testParseLastTurnInformMove() {
		assertEquals(instance.MOVE, instance.parseLastTurnInform(protocol.LAST_MOVE + " " + protocol.MOVE));
	}
	
	@Test
	public void testParseLastTurnInformPass() {
		assertEquals(instance.PASS, instance.parseLastTurnInform(protocol.LAST_MOVE + " " + protocol.PASS));
	}
	
	@Test
	public void testParseLastTurnInformWrong() {
		assertEquals(-1, instance.parseLastTurnInform(protocol.LAST_MOVE + " aaa"));
	}

	@Test
	public void testParseRematchAccepted() {
		assertTrue(instance.parseRematchAccepted(protocol.REMATCH_ACCEPTED));
	}
	
	@Test
	public void testParseRematchAcceptedWrongInput() {
		assertFalse(instance.parseRematchAccepted("aaaa"));
	}
	
	@Test
	public void testParseRematchDenied() {
		assertTrue(instance.parseRematchDenied(protocol.REMATCH_DENIED));
	}
	
	@Test
	public void testParseRematchDeniedWrongInput() {
		assertFalse(instance.parseRematchDenied("aaaa"));
	}
	
}
