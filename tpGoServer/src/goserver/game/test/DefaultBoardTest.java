/**
 * 
 */
package goserver.game.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.DefaultGoBoard;

/**
 * @author Kacper
 *
 */
public class DefaultBoardTest {

	@Test
	public void testBoardSize() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		assertEquals(board.getSize(), size);
		assertEquals(board.getBoard().length, size);
		assertEquals(board.getBoard()[0].length, size);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalBoardSize() {
		int size = 0;
		DefaultGoBoard board = new DefaultGoBoard(size);
	}

	@Test
	public void testEmptyBoard() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				assertEquals(board.getBoard()[i][j], DefaultGoBoard.EMPTY);
				assertEquals(board.getPreviousBoard()[i][j], DefaultGoBoard.EMPTY);
			}
		}
	}

	@Test
	public void testPlaceStone() {
		int size = 9;
		int x = 2;
		int y = 3;
		int color = DefaultGoBoard.BLACK;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(color, x, y);
		assertEquals(board.getBoard()[x][y], color);
		assertEquals(board.getPreviousBoard()[x][y], DefaultGoBoard.EMPTY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlaceStoneInvalidLocationOverX() {
		int size = 9;
		int x = 9;
		int y = 3;
		int color = DefaultGoBoard.BLACK;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(color, x, y);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlaceStoneInvalidLocationUnderX() {
		int size = 9;
		int x = -1;
		int y = 0;
		int color = DefaultGoBoard.BLACK;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(color, x, y);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlaceStoneInvalidColor() {
		int size = 9;
		int x = 2;
		int y = 3;
		int color = DefaultGoBoard.EMPTY;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(color, x, y);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlaceStoneOnNonEmpty() {
		int size = 9;
		int x = 2;
		int y = 3;
		int color = DefaultGoBoard.BLACK;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(color, x, y);
		board.placeStone(color, x, y);
	}

	@Test
	public void testLiberties() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		int color = DefaultGoBoard.BLACK;
		board.placeStone(color, 1, 1);
		assertEquals(4, board.getLiberties(1, 1));
	}

	@Test
	public void testLibertiesCorner() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		int color = DefaultGoBoard.BLACK;
		board.placeStone(color, 0, 0);
		assertEquals(2, board.getLiberties(0, 0));
	}

	@Test
	public void testLibertiesEmpty() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		assertEquals(-1, board.getLiberties(0, 0));
	}
	
	@Test
	public void testLibertiesSurrounded() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(DefaultGoBoard.BLACK, 1, 1);
		board.placeStone(DefaultGoBoard.WHITE, 0, 1);
		board.placeStone(DefaultGoBoard.WHITE, 1, 0);
		board.placeStone(DefaultGoBoard.WHITE, 1, 2);
		assertEquals(1, board.getLiberties(1, 1));
	}

	@Test
	public void testOpposingColors() {
		assertEquals(DefaultGoBoard.getOpposingColor(DefaultGoBoard.BLACK), DefaultGoBoard.WHITE);
		assertEquals(DefaultGoBoard.getOpposingColor(DefaultGoBoard.WHITE), DefaultGoBoard.BLACK);
		assertEquals(DefaultGoBoard.getOpposingColor(DefaultGoBoard.EMPTY), DefaultGoBoard.EMPTY);
	}

	@Test
	public void testPreviousBoard() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(DefaultGoBoard.BLACK, 1, 2);
		board.placeStone(DefaultGoBoard.WHITE, 3, 2);
		board.placeStone(DefaultGoBoard.BLACK, 1, 4);
		board.placeStone(DefaultGoBoard.WHITE, 2, 2);
		int[][] boardCopy = copyMatrix(board.getBoard());
		board.placeStone(DefaultGoBoard.BLACK, 5, 4);
		assertTrue(compareMatrix(boardCopy, board.getPreviousBoard()));
	}
	
	@Test
	public void testCapture() {
		int size = 9;
		DefaultGoBoard board = new DefaultGoBoard(size);
		board.placeStone(DefaultGoBoard.BLACK, 0, 0);
		board.placeStone(DefaultGoBoard.BLACK, 1, 0);
		board.placeStone(DefaultGoBoard.BLACK, 1, 1);
		board.placeStone(DefaultGoBoard.BLACK, 2, 1);
		board.placeStone(DefaultGoBoard.WHITE, 1, 2);
		board.placeStone(DefaultGoBoard.WHITE, 2, 0);
		board.placeStone(DefaultGoBoard.WHITE, 2, 2);
		board.placeStone(DefaultGoBoard.WHITE, 3, 1);
		int captured = board.placeStone(DefaultGoBoard.WHITE, 0, 1);
		assertEquals(4, captured);
		assertEquals(DefaultGoBoard.EMPTY, board.getBoard()[0][0]);
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