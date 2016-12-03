/**
 * 
 */
package goserver.game;

/**
 * @author Kacper
 *
 */
public interface GoRule {
	
	public boolean validateMove(GoBoard board, int color, int x, int y) throws InvalidMoveException;

}
