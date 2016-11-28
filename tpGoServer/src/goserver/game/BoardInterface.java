package goserver.game;

public interface BoardInterface {
	
	public int placeStone(int color, int x, int y);
	public int getSize();
	public int[][] getBoard();
	public int[][] getPreviousBoard();

}
