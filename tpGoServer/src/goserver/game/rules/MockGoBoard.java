package goserver.game.rules;

import goserver.game.DefaultGoBoard;
import goserver.game.GoBoard;

/**
 * DefaultGoBoard with the capability to completely change the board.
 * 
 * @author Kacper
 *
 */
public class MockGoBoard extends DefaultGoBoard implements GoBoard {

	public MockGoBoard(int size) {
		super(size);
	}

	/**
	 * Change the board data to the selected one.
	 * 
	 * @param board
	 *            New bord data.
	 */
	public void setBoard(int[][] board) {
		this.board = board;
	}

}
