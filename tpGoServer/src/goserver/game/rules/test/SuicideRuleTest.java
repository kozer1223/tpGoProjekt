package goserver.game.rules.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.SuicideRule;
import goserver.game.test.EmptyGoPlayer;

public class SuicideRuleTest {

	@Test
	public void testSingleton() {
		assertNotEquals(SuicideRule.getInstance(), null);
	}

	@Test
	public void testSuicideMove() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 19;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset().with(SuicideRule.getInstance()));

		// \ 0 1 2
		// 0 O O X
		// 1 X X
		// 2 O

		game.makeMove(player1, 0, 1);
		game.makeMove(player2, 1, 0);
		game.makeMove(player1, 2, 0);
		game.makeMove(player2, 0, 2);
		game.makeMove(player1, 1, 1);

		try {
			game.makeMove(player2, 0, 0);
			fail();
		} catch (InvalidMoveException e) {
			assertEquals(e.getMessage(), SuicideRule.invalidMoveMessage);
		}
	}

	@Test
	public void testAcceptableSuicideMove() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset().with(SuicideRule.getInstance()));

		// \ 0 1 2
		// 0 . 1 2
		// 1 1 2
		// 2 2

		game.makeMove(player1, 0, 1);
		game.makeMove(player2, 1, 1);
		game.makeMove(player1, 1, 0);
		game.makeMove(player2, 0, 2);
		game.makeMove(player1, 4, 0); // ruchy spoza tworzonej konfiguracji
										// kamieni
		game.makeMove(player2, 2, 0);
		game.makeMove(player1, 0, 4); // ruchy spoza tworzonej konfiguracji
										// kamieni
		game.makeMove(player2, 0, 0);
		assertEquals(2, game.getPlayersCapturedStones(player2));

	}

}
