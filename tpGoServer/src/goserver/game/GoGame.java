package goserver.game;

/**
 * @author Kacper
 *
 */
public interface GoGame {
	
	public void setRuleset(GoRuleset ruleset);
	public void makeMove(GoPlayer player, int x, int y);
	public GoBoard getBoard();
	
}
