package goserver.game.rules;

import goserver.game.GoBoard;
import goserver.game.GoGame;
import goserver.game.InvalidMoveException;
import goserver.util.MatrixUtil;

public class KoRule implements GoRule {
	
	private static KoRule instance;
	public static final String invalidMoveMessage = "Ko rule.";

	private KoRule() {
	};

	public synchronized static KoRule getInstance() {
		if (instance == null) {
			instance = new KoRule();
		}
		return instance;
	}

	@Override
	public void onGameStart(GoGame game) {
	}

	@Override
	public boolean validateMove(GoBoard board, int color, int x, int y) throws InvalidMoveException {
		if (board.getPreviousBoard()[x][y] == color) {
			// mozliwe ko
			MockGoBoard mockBoard = new MockGoBoard(board.getSize());
			mockBoard.setBoard(MatrixUtil.copyMatrix(board.getBoard()));

			mockBoard.placeStone(color, x, y);
			if (MatrixUtil.compareMatrix(board.getPreviousBoard(), mockBoard.getBoard())) {
				// powtorzenie planszy
				throw new InvalidMoveException(invalidMoveMessage);
			}
		}
		return true;
	}

	@Override
	public void onGameEnd(GoGame game) {
		// TODO Auto-generated method stub
		
	}

}
