package goserver.game.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.game.rules.GoRuleset;
import goserver.util.MatrixUtil;

public class DefaultGameTest {

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

	}

	@Test
	public void testCreateEmptyBoard() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		assertTrue(MatrixUtil.compareMatrix(game.getBoard().getBoard(), new int[size][size]));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongBoardSize() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = -1;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
	}

	@Test
	public void testRightPlayers() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		assertEquals(player1, game.getPlayer1());
		assertEquals(player2, game.getPlayer2());
	}

	@Test
	public void testMakeMoves() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		assertEquals(game.getBoard().getBlackColor(), game.getBoard().getBoard()[0][0]);
		game.makeMove(player2, 0, 1);
		assertEquals(game.getBoard().getWhiteColor(), game.getBoard().getBoard()[0][1]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConflictingMoves() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		game.makeMove(player2, 0, 0);
	}
	
	@Test
	public void testPassTurn() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		assertTrue(game.isPlayersTurn(player1));
		game.passTurn(player1);
		assertTrue(game.isPlayersTurn(player2));
	}

}
