/**
 * 
 */
package goserver.game.rules;

import java.util.ArrayList;
import java.util.List;

import goserver.game.GoBoard;
import goserver.game.GoGame;
import goserver.game.InvalidMoveException;
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

	@Override
	public void onGameStart(GoGame game) {
		game.getBoard().setSuicideCheckEnabled(true);
	}

	@Override
	public boolean validateMove(GoBoard board, int color, int x, int y) throws InvalidMoveException {
		return true;
	}

}
