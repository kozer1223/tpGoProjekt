/**
 * 
 */
package goserver.game.rules;

import goserver.game.GoBoard;
import goserver.game.GoGame;
import goserver.game.InvalidMoveException;

/**
 * @author Kacper
 *
 */
public interface GoRule {
	
	public void onGameStart(GoGame game);
	public boolean validateMove(GoBoard board, int color, int x, int y) throws InvalidMoveException;

}
