package goserver.game;

import java.util.List;

import goserver.util.IntPair;

/**
 * @author Kacper
 *
 */
public interface GoBoard {
	
	public IntPair placeStone(int color, int x, int y) throws InvalidMoveException;
	public List<IntPair> getConnectedStones(int x, int y);
	public int getSize();
	public int[][] getBoard();
	public int[][] getPreviousBoard();
	
	public int getBlackColor();
	public int getWhiteColor();
	public int getEmptyColor();
	public int getOpposingColor(int color);
	
	public void setSuicideCheckEnabled(boolean check);
	
	public int[][] getBoardWithLabeledGroups();
	public int[] getAllGroupLabels();
	public GoGroupType getGroupType(int label);
	public void setGroupType(int label, GoGroupType type);

}
