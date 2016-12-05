package goserver.game.rules;

import goserver.game.DefaultGoBoard;
import goserver.game.GoBoard;

public class MockGoBoard extends DefaultGoBoard implements GoBoard {

	public MockGoBoard(int size) {
		super(size);
	}
	
	public void setBoard(int[][] board){
		this.board = board;
	}

}
