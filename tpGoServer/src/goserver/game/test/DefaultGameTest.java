package goserver.game.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.GoRuleset;
import goserver.game.InvalidMoveException;

public class DefaultGameTest {

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
	public void testCreateEmptyBoard() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		assertTrue(compareMatrix(game.getBoard().getBoard(), new int[size][size]));
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
	
	@Test(expected=IllegalArgumentException.class)
	public void testConflictingMoves() throws InvalidMoveException {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset());
		game.makeMove(player1, 0, 0);
		game.makeMove(player2, 0, 0);
	}

	public int[][] copyMatrix(int[][] matrix) {
		int[][] newMatrix = new int[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			newMatrix[i] = matrix[i].clone();
		}
		return newMatrix;
	}

	public boolean compareMatrix(int[][] matrix1, int[][] matrix2) {
		if (matrix1.length != matrix2.length) {
			return false;
		}
		for (int i = 0; i < matrix1.length; i++) {
			if (matrix1[i].length != matrix2[i].length) {
				return false;
			}
			for (int j = 0; j < matrix1.length; j++) {
				if (matrix1[i][j] != matrix2[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

}
