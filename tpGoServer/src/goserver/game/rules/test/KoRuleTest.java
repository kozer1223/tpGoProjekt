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

public class KoRuleTest {
	
	class EmptyGoPlayer implements GoPlayer {

		@Override
		public void setGame(GoGame game) {
			// TODO Auto-generated method stub

		}

		@Override
		public void notifyAboutTurn(GoMoveType opponentsMove) {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateBoard() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyAboutGamePhaseChange(int gamePhase) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void notifyAboutGameEnd(double playerScore, double opponentScore) {
			// TODO Auto-generated method stub
			
		}

	}

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
	
}
