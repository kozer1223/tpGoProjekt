package goserver.game;

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
	public void makeMove(GoPlayer player, int x, int y);
	public GoBoard getBoard();
	
	public int getPlayersCapturedStones(GoPlayer player);
	
}
