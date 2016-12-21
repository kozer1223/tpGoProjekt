package goserver.game;

import java.util.List;
import java.util.Map;

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
	public GoBoard getBoard();
	public GoPlayer getOpposingPlayer(GoPlayer player);
	
	public void makeMove(GoPlayer player, int x, int y) throws InvalidMoveException;
	public void passTurn(GoPlayer player);
	
	public int getPlayersCapturedStones(GoPlayer player);
	public boolean isPlayersTurn(GoPlayer player);
	public int getGamePhase();
	public boolean isStonePlacingPhase();
	public boolean isGroupMarkingPhase();
	public boolean isGameEnd();
	
	public double getPlayersScore(GoPlayer player);
	public void setPlayersScore(GoPlayer player, double score);
	
	public Map<Integer, GoGroupType> getLabelsMap();
	public void applyGroupTypeChanges(GoPlayer player, Map<Integer, GoGroupType> groupTypeChanges);
	public List<Integer> getAllLockedGroups();
	
	public void leaveGame(GoPlayer player);
	public void requestRematch(GoPlayer player);
	public void denyRematch(GoPlayer player);
	
}
