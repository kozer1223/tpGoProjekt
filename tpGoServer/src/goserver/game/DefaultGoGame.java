/**
 * 
 */
package goserver.game;

/**
 * @author Kacper
 *
 */
public class DefaultGoGame implements GoGame {

	private GoBoard board;
	private GoPlayer[] players;
	private GoRuleset ruleset;
	private int boardSize;
	private int[] capturedStones;

	public DefaultGoGame(GoPlayer player1, GoPlayer player2, int boardSize, GoRuleset ruleset) {
		players = new GoPlayer[2];
		players[0] = player1;
		player1.setGame(this);
		player1.setOpposingPlayer(player2);
		players[1] = player2;
		player2.setGame(this);
		player1.setOpposingPlayer(player1);
		
		capturedStones = new int[2];
		capturedStones[0] = 0;
		capturedStones[1] = 0;

		this.boardSize = boardSize;
		board = new DefaultGoBoard(boardSize);

		setRuleset(ruleset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoGame#makeMove(goserver.game.GoPlayer, int, int)
	 */
	@Override
	public void makeMove(GoPlayer player, int x, int y) {
		int playerNo = getPlayersNo(player);
		
		// throws exception?
		ruleset.validateMove(board, getPlayersColor(playerNo), x, y);
		
		capturedStones[playerNo] += board.placeStone(getPlayersColor(playerNo), x, playerNo);
		// player.updateBoard() ???
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoGame#setRuleset(goserver.game.GoRuleset)
	 */
	@Override
	public void setRuleset(GoRuleset ruleset) {
		this.ruleset = ruleset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoGame#getBoard()
	 */
	@Override
	public GoBoard getBoard() {
		return board;
	}

	@Override
	public GoPlayer getPlayer1() {
		return players[0];
	}

	@Override
	public void setPlayer1(GoPlayer player) {
		players[0] = player;
	}

	@Override
	public GoPlayer getPlayer2() {
		return players[1];
	}

	@Override
	public void setPlayer2(GoPlayer player) {
		players[1] = player;
	}

	@Override
	public GoRuleset getRuleset() {
		return ruleset;
	}
	
	protected int getPlayersNo(GoPlayer player){
		if (player.equals(players[0])) {
			return 0;
		} else if (player.equals(players[1])) {
			return 1;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected int getPlayersColor(int playerNo) {
		return (playerNo == 0) ? board.getWhiteColor() : board.getBlackColor();
	}

	protected int getPlayersColor(GoPlayer player) {
		return (getPlayersNo(player) == 0) ? board.getWhiteColor() : board.getBlackColor();
	}

	@Override
	public int getPlayersCapturedStones(GoPlayer player) {
		return (getPlayersNo(player) == 0) ? capturedStones[0] : capturedStones[1];
	}

}
