package goserver.game.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.GoRuleset;
import goserver.game.InvalidMoveException;
import goserver.game.SuicideRule;
import goserver.game.test.DefaultGameTest.EmptyGoPlayer;

public class SuicideRuleTest {
	
	class EmptyGoPlayer implements GoPlayer {

		@Override
		public void setGame(GoGame game) {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateBoard() {
			// TODO Auto-generated method stub
			
		}

	}

	@Test
	public void testSingleton() {
		assertNotEquals(SuicideRule.getInstance(), null);
	}
	
	@Test
	public void testSuicideMove() throws InvalidMoveException{
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
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

		try{
			game.makeMove(player2, 0, 0);
			assertFalse(true);
		} catch(InvalidMoveException e){
			assertEquals(e.getMessage(), SuicideRule.invalidMoveMessage);
		}
	}

}
