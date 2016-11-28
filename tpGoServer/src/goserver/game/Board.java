/**
 * 
 */
package goserver.game;

/**
 * @author Kacper
 *
 */
public class Board {

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
	 * K³adzie kamieñ na polu (x,y) i dusi kamienie przeciwnika, jeœli mo¿liwe.
	 * Zwraca liczbê uduszonych kamieni przeciwnika.
	 * 
	 * @param color
	 * @param x
	 * @param y
	 * @return liczba zabranych jeñców
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

		// TODO
		// sprawdz ile zabrano jencow

		return 0;
	}

	/**
	 * Zwraca liczbê oddechów kamienia na polu (x,y). (Oddech kamienia definiuje
	 * jako puste pola tu¿ obok kamienia).
	 * 
	 * Zwraca -1, jeœli pole (x,y) jest puste.
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
