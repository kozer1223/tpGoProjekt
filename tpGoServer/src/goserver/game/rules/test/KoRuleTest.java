package goserver.game.rules.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.KoRule;
import goserver.game.rules.SuicideRule;
import goserver.game.test.EmptyGoPlayer;

public class KoRuleTest {
	
	@Test
	public void testSingleton() {
		assertNotEquals(KoRule.getInstance(), null);
	}
	
	@Test
	public void testKoMove() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 19;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset().with(KoRule.getInstance()));

		// \ 0 1 2 3
		// 0   O X
		// 1 O X O X
		// 2   O X

		game.makeMove(player1, 1, 0);
		game.makeMove(player2, 2, 0);
		game.makeMove(player1, 0, 1);
		game.makeMove(player2, 3, 1);
		game.makeMove(player1, 1, 2);
		game.makeMove(player2, 2, 2);
		game.makeMove(player1, 2, 1);
		game.makeMove(player2, 1, 1);

		try {
			game.makeMove(player1, 2, 1);
			fail();
		} catch (InvalidMoveException e) {
			assertEquals(e.getMessage(), KoRule.invalidMoveMessage);
		}
	}
	
	@Test
	public void testSnapback() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 19;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset().with(KoRule.getInstance()));

		// \ 0 1 2 3 4 5 6
		// 0 X X O O
		// 1 X   X O
		// 2 X O O X
		// 3   X X
		// 4 
		// 5 O O
		// 6 

		game.makeMove(player1, 0, 0);
		game.makeMove(player2, 2, 0);
		game.makeMove(player1, 1, 0);
		game.makeMove(player2, 3, 0);
		game.makeMove(player1, 0, 1);
		game.makeMove(player2, 3, 1);
		game.makeMove(player1, 0, 2);
		game.makeMove(player2, 1, 2);
		game.makeMove(player1, 2, 1);
		game.makeMove(player2, 2, 2);
		game.makeMove(player1, 3, 2);
		game.makeMove(player2, 0, 5);
		game.makeMove(player1, 1, 3);
		game.makeMove(player2, 1, 5);
		game.makeMove(player1, 2, 3);
		
		// \ 0 1 2 3 4 5 6
		// 0 X X O O
		// 1 X O   O
		// 2 X O O X
		// 3   X X
		// 4 
		// 5 O O
		// 6 
		
		game.makeMove(player2, 1, 1);
		
		// snapback (legal "ko" move)
		
		game.makeMove(player1, 2, 1);
		
		// \ 0 1 2 3 4 5 6
		// 0 X X O O
		// 1 X   X O
		// 2 X     X
		// 3   X X
		// 4 
		// 5 O O
		// 6 
		
		assertEquals(game.getBoard().getBoardData()[1][1], game.getBoard().getEmptyColor());
	}
	
}
