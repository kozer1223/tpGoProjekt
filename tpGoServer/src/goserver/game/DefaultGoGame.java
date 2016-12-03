/**
 * 
 */
package goserver.game;

import goserver.util.IntPair;

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

	GoPlayer currentPlayer;

	public DefaultGoGame(GoPlayer player1, GoPlayer player2, int boardSize, GoRuleset ruleset) {
		players = new GoPlayer[2];
		players[0] = player1;
		player1.setGame(this);
		// player1.setOpposingPlayer(player2);
		players[1] = player2;
		player2.setGame(this);
		// player1.setOpposingPlayer(player1);

		currentPlayer = players[0];

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
	public void makeMove(GoPlayer player, int x, int y) throws InvalidMoveException {
		if (isPlayersTurn(player)) {
			int playerNo = getPlayersNo(player);

			// throws exception
			if (ruleset.validateMove(board, getPlayersColor(playerNo), x, y)) {
				IntPair placeResult = board.placeStone(getPlayersColor(playerNo), x, y);
				capturedStones[playerNo] += placeResult.x;
				capturedStones[1 - playerNo] += placeResult.y;

				players[0].updateBoard();
				players[1].updateBoard();
				currentPlayer = getOpposingPlayer(currentPlayer);
			} else {
				throw new InvalidMoveException("Invalid move");
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoGame#makeMove(goserver.game.GoPlayer)
	 */
	@Override
	public void passTurn(GoPlayer player) {
		if (isPlayersTurn(player)) {
			currentPlayer = getOpposingPlayer(currentPlayer);
		} else {
			throw new IllegalArgumentException();
		}
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

	protected GoPlayer getOpposingPlayer(GoPlayer player) {
		return players[1 - getPlayersNo(player)];
	}

	protected int getPlayersNo(GoPlayer player) {
		if (player.equals(players[0])) {
			return 0;
		} else if (player.equals(players[1])) {
			return 1;
		} else {
			throw new IllegalArgumentException();
		}
	}

	protected int getPlayersColor(int playerNo) {
		return (playerNo == 0) ? board.getBlackColor() : board.getWhiteColor();
	}

	protected int getPlayersColor(GoPlayer player) {
		return getPlayersColor(getPlayersNo(player));
	}

	@Override
	public int getPlayersCapturedStones(GoPlayer player) {
		return capturedStones[getPlayersNo(player)];
	}

	@Override
	public boolean isPlayersTurn(GoPlayer player) {
		return (currentPlayer == player);
	}

}
