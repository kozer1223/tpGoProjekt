/**
 * 
 */
package goserver.server.test;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import goserver.game.GoGroupType;
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

	@Test
	public void testSendGameBegin() {
		StringWriter writer = new StringWriter();
		instance.sendGameBegin(new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.GAME_BEGIN));
	}
	
	@Test
	public void testSendMoveAccepted() {
		StringWriter writer = new StringWriter();
		instance.sendMoveAccepted(new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.MOVE_ACCEPTED));
	}
	
	@Test
	public void testSendBoardData() {
		StringWriter writer = new StringWriter();
		int[][] boardData = { {0, 0, 0, 0},
				              {1, 1, 0, 2},
				              {2, 1, 1, 2},
				              {0, 2, 0, 1}};
		String stringBoardData = "0000110221120201";
		instance.sendBoardData(boardData, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.SEND_BOARD + " " + stringBoardData));
	}
	
	@Test
	public void testSendGamePhase() {
		StringWriter writer = new StringWriter();
		int phase = 1;
		instance.sendGamePhase(1, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.SEND_PHASE + " " + phase));
	}
	
	@Test
	public void testSendLabeledBoardData() {
		StringWriter writer = new StringWriter();
		int[][] boardData = { {0, 0, 0, 0},
				              {1, 1, 0, 2},
				              {2, 1, 1, 2},
				              {0, 2, 0, 1}};
		String stringBoardData = "0 0 0 0 1 1 0 2 2 1 1 2 0 2 0 1";
		instance.sendLabeledBoardData(boardData, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.SEND_LABELED_BOARD + " " + stringBoardData));
	}
	
	@Test
	public void testSendGroupStateData() {
		StringWriter writer = new StringWriter();
		Map<Integer, GoGroupType> labels = new HashMap<Integer, GoGroupType>();
		labels.put(1, GoGroupType.ALIVE);
		labels.put(3, GoGroupType.DEAD);
		String labelData = "1 A 3 D";
		instance.sendGroupStateData(labels, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.SEND_GROUP_STATE + " " + labelData));
	}
	
	@Test
	public void testSendGameScore() {
		StringWriter writer = new StringWriter();
		double blackScore = 1;
		double whiteScore = 6.5;
		instance.sendGameScore(blackScore, whiteScore, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.SEND_SCORE + " " + blackScore + " " + whiteScore));
	}
	
	@Test
	public void testSendCapturedStones() {
		StringWriter writer = new StringWriter();
		int blackStones = 5;
		int whiteStones = 3;
		instance.sendCapturedStones(blackStones, whiteStones, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.SEND_CAPTURED_STONES + " " + blackStones + " " + whiteStones));
	}
	
	@Test
	public void testSendMessage() {
		StringWriter writer = new StringWriter();
		String message = "test";
		instance.sendMessage(message, new PrintWriter(writer));
		assertTrue(writer.toString().startsWith(protocol.SEND_MESSAGE + " " + message));
	}
	
}
