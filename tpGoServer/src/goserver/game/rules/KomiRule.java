package goserver.game.rules;

import goserver.game.GoBoard;
import goserver.game.GoGame;
import goserver.game.InvalidMoveException;
import goserver.util.MatrixUtil;

public class KomiRule implements GoRule {
	
	private static KomiRule instance;
	public static final double komiPoints = 6.5;

	private KomiRule() {
	};

	public synchronized static KomiRule getInstance() {
		if (instance == null) {
			instance = new KomiRule();
		}
		return instance;
	}

	@Override
	public void onGameStart(GoGame game) {
	}

	@Override
	public boolean validateMove(GoBoard board, int color, int x, int y) throws InvalidMoveException {
		return true;
	}

	@Override
	public void onGameEnd(GoGame game) {
		double player2Score = game.getPlayersScore(game.getPlayer2());
		player2Score += komiPoints;
		game.setPlayersScore(game.getPlayer2(), player2Score);
	}

}
