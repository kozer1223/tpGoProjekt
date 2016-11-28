/**
 * 
 */
package goserver.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import goserver.util.IntPair;

/**
 * @author Kacper
 *
 */
public class Board implements BoardInterface {

	// oznaczenia kamieni na planszy
	public static final int EMPTY = 0;
	public static final int BLACK = 1;
	public static final int WHITE = 2;

	private final int size;
	private int board[][];
	private int previousBoard[][]; // ko rule

	/**
	 * Tworzy plansze o rozmiarze (size x size).
	 * 
	 * @param size
	 */
	public Board(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException();
		}
		this.size = size;
		board = new int[size][size];
		previousBoard = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = EMPTY;
				previousBoard[i][j] = EMPTY;
			}
		}
	}

	/**
	 * Kładzie kamień na polu (x,y) i dusi kamienie przeciwnika, jeśli możliwe.
	 * Zwraca liczbę uduszonych kamieni przeciwnika.
	 * 
	 * @param color
	 * @param x
	 * @param y
	 * @return liczba zabranych jeńców
	 * @throws IllegalArgumentException
	 */
	public int placeStone(int color, int x, int y) throws IllegalArgumentException {
		if (x < 0 || x >= size || y < 0 || y >= size) {
			throw new IllegalArgumentException();
		}
		if (board[x][y] != EMPTY) {
			throw new IllegalArgumentException();
		}
		if (color != WHITE && color != BLACK) {
			throw new IllegalArgumentException();
		}

		// sprawdzanie poprawnosci ruchow przez Game.ruleset.validateMove() przy
		// wywolaniu Game.makeMove()

		// board -> previousBoard
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				previousBoard[i][j] = board[i][j];
			}
		}

		board[x][y] = color;

		// zabierz jencow
		int captured = 0;
		if (x + 1 < size && board[x + 1][y] == getOpposingColor(color))
			captured += captureStones(x + 1, y);
		if (y + 1 < size && board[x][y + 1] == getOpposingColor(color))
			captured += captureStones(x, y + 1);
		if (x - 1 >= 0 && board[x - 1][y] == getOpposingColor(color))
			captured += captureStones(x - 1, y);
		if (y - 1 >= 0 && board[x][y - 1] == getOpposingColor(color))
			captured += captureStones(x, y - 1);

		return captured;
	}

	/**
	 * Zabierz kamienie z grupy, do której należy kamień, jeśli jest udoszona.
	 * Zwraca liczbę jeńców.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int captureStones(int x, int y) {
		List<IntPair> stoneGroup = getConnectedStones(x, y);

		int liberties = 0;

		for (int i = 0; i < stoneGroup.size(); i++) {
			liberties += getLiberties(stoneGroup.get(i).x, stoneGroup.get(i).y);
		}
		if (liberties == 0) { // uduszone
			int captured = stoneGroup.size();
			for (int i = 0; i < stoneGroup.size(); i++) {
				board[stoneGroup.get(i).x][stoneGroup.get(i).y] = EMPTY;
			}
			return captured;
		} else {
			return 0;
		}
	}

	/**
	 * Zwraca liczbę oddechów kamienia na polu (x,y). (Oddech kamienia definiuje
	 * jako puste pola tuż obok kamienia).
	 * 
	 * Zwraca -1, jeśli pole (x,y) jest puste.
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws IllegalArgumentException
	 */
	public int getLiberties(int x, int y) throws IllegalArgumentException {
		if (x < 0 || x >= size || y < 0 || y >= size) {
			throw new IllegalArgumentException();
		}
		if (board[x][y] == EMPTY) {
			return -1;
		}
		int liberties = 0;
		if (x + 1 < size && board[x + 1][y] == EMPTY)
			liberties++;
		if (y + 1 < size && board[x][y + 1] == EMPTY)
			liberties++;
		if (x - 1 >= 0 && board[x - 1][y] == EMPTY)
			liberties++;
		if (y - 1 >= 0 && board[x][y - 1] == EMPTY)
			liberties++;
		return liberties;
	}

	public static int getOpposingColor(int color) {
		switch (color) {
		case BLACK:
			return WHITE;
		case WHITE:
			return BLACK;
		default:
			return EMPTY;
		}
	}

	public List<IntPair> getConnectedStones(int x, int y) {
		// algorytm floodfill
		final int width = getSize();
		final int height = getSize();

		Set<IntPair> result = new HashSet<IntPair>();
		List<IntPair> queue = new ArrayList<IntPair>();

		final int color = board[x][y];

		queue.add(new IntPair(x, y));

		int i, cur_x, cur_y;

		while (!queue.isEmpty()) {
			cur_x = queue.get(0).x;
			cur_y = queue.get(0).y;

			// obecne pole
			result.add(new IntPair(cur_x, cur_y));
			if (cur_y - 1 >= 0 && board[cur_x][cur_y - 1] == color) {
				if (result.add(new IntPair(cur_x, cur_y - 1)))
					queue.add(new IntPair(cur_x, cur_y - 1));
			}
			if (cur_y + 1 < height && board[cur_x][cur_y + 1] == color) {
				if (result.add(new IntPair(cur_x, cur_y + 1)))
					queue.add(new IntPair(cur_x, cur_y + 1));
			}
			// w lewo
			i = 1;
			while (cur_x - i >= 0) {
				if (board[cur_x - i][cur_y] == color) {
					result.add(new IntPair(cur_x - i, cur_y));
					if (cur_y - 1 >= 0 && board[cur_x - i][cur_y - 1] == color) {
						if (result.add(new IntPair(cur_x - i, cur_y - 1)))
							queue.add(new IntPair(cur_x - i, cur_y - 1));
					}
					if (cur_y + 1 < height && board[cur_x - i][cur_y + 1] == color) {
						if (result.add(new IntPair(cur_x - i, cur_y + 1)))
							queue.add(new IntPair(cur_x - i, cur_y + 1));
					}
					i++;
				} else {
					break;
				}
			}
			// w prawo
			i = 1;
			while (cur_x + i < width) {
				if (board[cur_x + i][cur_y] == color) {
					result.add(new IntPair(cur_x + i, cur_y));
					if (cur_y - 1 >= 0 && board[cur_x + i][cur_y - 1] == color) {
						if (result.add(new IntPair(cur_x + i, cur_y - 1)))
							queue.add(new IntPair(cur_x + i, cur_y - 1));
					}
					if (cur_y + 1 < height && board[cur_x + i][cur_y + 1] == color) {
						if (result.add(new IntPair(cur_x + i, cur_y + 1)))
							queue.add(new IntPair(cur_x + i, cur_y + 1));
					}
					i++;
				} else {
					break;
				}
			}
			queue.remove(0);
		}

		return new ArrayList<IntPair>(result);
	}

	public int getSize() {
		return size;
	}

	public int[][] getBoard() {
		return board;
	}

	public int[][] getPreviousBoard() {
		return previousBoard;
	}

}
