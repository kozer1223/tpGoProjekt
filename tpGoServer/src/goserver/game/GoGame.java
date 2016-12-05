package goserver.game;

import goserver.game.rules.GoRuleset;

/**
 * @author Kacper
 *
 */
public interface GoGame {
	
	public GoPlayer getPlayer1();
	public void setPlayer1(GoPlayer player);
	public GoPlayer getPlayer2();
	public void setPlayer2(GoPlayer player);
	public GoRuleset getRuleset();
	public void setRuleset(GoRuleset ruleset);
	public void makeMove(GoPlayer player, int x, int y) throws InvalidMoveException;
	public void passTurn(GoPlayer player);
	public GoBoard getBoard();
	
	public int getPlayersCapturedStones(GoPlayer player);
	public boolean isPlayersTurn(GoPlayer player);
	
}
