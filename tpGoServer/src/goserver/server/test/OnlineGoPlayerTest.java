/**
 * 
 */
package goserver.server.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.game.test.EmptyGoPlayer;
import goserver.server.OnlineGoPlayer;
import goserver.server.ServerClientProtocol;

/**
 * @author Kacper
 *
 */
public class OnlineGoPlayerTest {

	private OnlineGoPlayer onlinePlayer;
	private BufferedReader outputReader;
	private PrintWriter inputWriter;

	private BufferedReader input;
	private PrintWriter output;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		PipedWriter outputPipeWriter = new PipedWriter();
		output = new PrintWriter(outputPipeWriter);
		outputReader = new BufferedReader(new PipedReader(outputPipeWriter));

		PipedWriter inputPipeWriter = new PipedWriter();
		inputWriter = new PrintWriter(inputPipeWriter);
		input = new BufferedReader(new PipedReader(inputPipeWriter));
		onlinePlayer = new OnlineGoPlayer(null, input, output);
	}

	@Test
	public void testInputWriter() {
		String line = "test";
		inputWriter.println(line);
		try {
			assertEquals(line, input.readLine());
		} catch (IOException e) {
			fail();
		}
	}

	@Test(timeout = 3000)
	public void testHookUpToAGame() {
		GoPlayer opponent = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(onlinePlayer, opponent, size, DefaultGoRuleset.getDefaultRuleset());
		onlinePlayer.start();
		boolean receivedAllMessages = false;

		while (!receivedAllMessages) {
			String out;
			try {
				if (outputReader.ready()) {
					out = outputReader.readLine();
					if (out != null) {
						assertTrue(out.contains(ServerClientProtocol.getInstance().ASSIGN_COLOR));
						return;
					}
				}

			} catch (IOException e) {
				fail();
			}

		}
	}

	@Test(timeout = 10000)
	public void testPlayAGame() throws InvalidMoveException, IOException {
		GoPlayer opponent = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(opponent, onlinePlayer, size, DefaultGoRuleset.getDefaultRuleset());
		onlinePlayer.start();
		
		ServerClientProtocol protocol = ServerClientProtocol.getInstance();

		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.GAME_BEGIN)) {
					break;
				}
			}

		}

		game.makeMove(opponent, 0, 0);
		
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.LAST_MOVE + " " + protocol.MOVE)) {
					break;
				}
			}
		}
		
		inputWriter.println(protocol.SEND_MOVE + " " + 2 + " " + 3);
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.MOVE_ACCEPTED)) {
					break;
				}
			}
		}
		
		assertTrue(game.isPlayersTurn(opponent));
		assertTrue(game.getBoard().getBoard()[2][3] == game.getBoard().getWhiteColor());
	}

	@Test(timeout = 10000)
	public void testPlayAGamePassAndRematch() throws InvalidMoveException, IOException {
		GoPlayer opponent = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(opponent, onlinePlayer, size, DefaultGoRuleset.getDefaultRuleset());
		onlinePlayer.start();
		
		ServerClientProtocol protocol = ServerClientProtocol.getInstance();

		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.GAME_BEGIN)) {
					break;
				}
			}

		}

		game.passTurn(opponent);
		
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.LAST_MOVE + " " + protocol.PASS)) {
					break;
				}
			}
		}
		
		inputWriter.println(protocol.PASS_TURN);
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.MOVE_ACCEPTED)) {
					break;
				}
			}
		}
		
		assertTrue(game.isGroupMarkingPhase());
		
		// accept changes
		game.applyGroupTypeChanges(opponent, new HashMap<Integer, GoGroupType>());
		inputWriter.println(protocol.PASS_TURN);
		
		// game ends
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.SEND_SCORE)) {
					break;
				}
			}
		}	
		
		inputWriter.println(protocol.REQUEST_REMATCH);
		game.requestRematch(opponent);
		
		// rematch begins
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.GAME_BEGIN)) {
					break;
				}
			}
		}	
	}
	
	@Test(timeout = 20000)
	public void testPlayAGameDenyRematch() throws InvalidMoveException, IOException {
		GoPlayer opponent = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(onlinePlayer, opponent, size, DefaultGoRuleset.getDefaultRuleset());
		onlinePlayer.start();
		
		ServerClientProtocol protocol = ServerClientProtocol.getInstance();

		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.GAME_BEGIN)) {
					break;
				}
			}

		}

		inputWriter.println(protocol.SEND_MOVE + " " + 2 + " " + 3);
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.MOVE_ACCEPTED)) {
					break;
				}
			}
		}
		
		game.passTurn(opponent);
		inputWriter.println(protocol.PASS_TURN);
		
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.SEND_PHASE + " " + 1)) {
					break;
				}
			}
		}
		
		assertTrue(game.isGroupMarkingPhase());
		
		//accept changes
		game.applyGroupTypeChanges(opponent, new HashMap<Integer, GoGroupType>());
		
		int label = (int) game.getLabelsMap().keySet().toArray()[0];
		System.out.println(label);
		
		inputWriter.println(protocol.CHANGE_GROUP_STATE + " " + label + " " + protocol.ALIVE);
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.SEND_SCORE)) {
					break;
				}
			}
		}
		
		assertTrue(game.isGameEnd());
		
		// deny rematch
		game.denyRematch(opponent);
		
		while (true) {
			if (outputReader.ready()) {
				String out = outputReader.readLine();
				if (out.contains(protocol.REMATCH_DENIED)) {
					break;
				}
			}
		}
	}
	
}
