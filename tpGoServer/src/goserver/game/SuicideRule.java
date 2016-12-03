/**
 * 
 */
package goserver.game;

import java.util.ArrayList;
import java.util.List;

import goserver.util.IntPair;

/**
 * @author Kacper
 *
 */
public class SuicideRule implements GoRule {

	private static SuicideRule instance;
	public static final String invalidMoveMessage = "Suicide rule.";

	private SuicideRule() {
	};

	public synchronized static SuicideRule getInstance() {
		if (instance == null) {
			instance = new SuicideRule();
		}
		return instance;
	}

	private int getLiberties(GoBoard board, int x, int y, int tempx, int tempy) throws IllegalArgumentException {
		if (x < 0 || x >= board.getSize() || y < 0 || y >= board.getSize()) {
			throw new IllegalArgumentException();
		}

		int liberties = 0;
		if (x + 1 < board.getSize() && board.getBoard()[x + 1][y] == board.getEmptyColor() && x + 1 != tempx)
			liberties++;
		if (y + 1 < board.getSize() && board.getBoard()[x][y + 1] == board.getEmptyColor() && y + 1 != tempy)
			liberties++;
		if (x - 1 >= 0 && board.getBoard()[x - 1][y] == board.getEmptyColor() && x - 1 != tempx)
			liberties++;
		if (y - 1 >= 0 && board.getBoard()[x][y - 1] == board.getEmptyColor() && y - 1 != tempy)
			liberties++;
		return liberties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoRule#validateMove(goserver.game.GoBoard, int, int,
	 * int)
	 */
	@Override
	public boolean validateMove(GoBoard board, int color, int x, int y) throws InvalidMoveException {
		List<IntPair> stoneGroup = new ArrayList<IntPair>();
		stoneGroup.add(new IntPair(x, y));

		if (x + 1 < board.getSize() && board.getBoard()[x + 1][y] == color)
			stoneGroup.addAll(board.getConnectedStones(x + 1, y));
		if (y + 1 < board.getSize() && board.getBoard()[x][y + 1] == color)
			stoneGroup.addAll(board.getConnectedStones(x, y + 1));
		if (x - 1 >= 0 && board.getBoard()[x - 1][y] == color)
			stoneGroup.addAll(board.getConnectedStones(x - 1, y));
		if (y - 1 >= 0 && board.getBoard()[x][y - 1] == color)
			stoneGroup.addAll(board.getConnectedStones(x, y - 1));

		int liberties = 0;

		for (int i = 0; i < stoneGroup.size(); i++) {
			liberties += getLiberties(board, stoneGroup.get(i).x, stoneGroup.get(i).y, x, y);
		}
		if (liberties == 0) { // uduszone
			throw new InvalidMoveException(invalidMoveMessage);
		}
		return true;
	}

}
