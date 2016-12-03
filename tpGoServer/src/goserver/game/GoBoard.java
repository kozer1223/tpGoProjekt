package goserver.game;

import java.util.List;

import goserver.util.IntPair;

/**
 * @author Kacper
 *
 */
public interface GoBoard {
	
	public IntPair placeStone(int color, int x, int y);
	public List<IntPair> getConnectedStones(int x, int y);
	public int getSize();
	public int[][] getBoard();
	public int[][] getPreviousBoard();
	public int getBlackColor();
	public int getWhiteColor();
	public int getEmptyColor();

}
