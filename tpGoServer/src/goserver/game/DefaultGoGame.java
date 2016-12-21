/**
 * 
 */
package goserver.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import goserver.game.rules.GoRuleset;
import goserver.util.IntPair;

/**
 * @author Kacper
 *
 */
public class DefaultGoGame implements GoGame {
	
	public final static int MAX_GROUP_MARKING_PHASE_LENGTH = 6; // maksymalna liczba tur na ustalenie grup

	private GoBoard board;
	private GoPlayer[] players;
	private GoRuleset ruleset;
	private int boardSize;
	private int[] capturedStones;
	private double[] score;
	private boolean[] rematchRequested;
	private int consecutivePasses;
	private int gamePhase; // 0 - stawianie kamieni, 1 - oznaczanie grup, 2 - koniec gry
	private int groupMarkingPhaseLength;

	GoPlayer currentPlayer;

	public DefaultGoGame(GoPlayer player1, GoPlayer player2, int boardSize, GoRuleset ruleset) {
		players = new GoPlayer[2];
		players[0] = player1;
		player1.setGame(this);

		players[1] = player2;
		player2.setGame(this);
		
		setRuleset(ruleset);
		this.boardSize = boardSize;

		restartGame();
	}
	
	protected void restartGame() {
		capturedStones = new int[2];
		capturedStones[0] = 0;
		capturedStones[1] = 0;
		score = new double[2];
		rematchRequested = new boolean[2];
		rematchRequested[0] = false;
		rematchRequested[1] = false;

		board = new DefaultGoBoard(boardSize);

		ruleset.onGameStart(this);
		
		gamePhase = 0;
		consecutivePasses = 0;
		currentPlayer = players[0];
		
		players[0].notifyAboutGameBegin();
		players[1].notifyAboutGameBegin();
		
		currentPlayer.notifyAboutTurn(GoMoveType.FIRST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoGame#makeMove(goserver.game.GoPlayer, int, int)
	 */
	@Override
	public void makeMove(GoPlayer player, int x, int y) throws InvalidMoveException {
		if (isPlayersTurn(player) && isStonePlacingPhase()) {
			int playerNo = getPlayersNo(player);

			// throws exception
			if (x < 0 || x >= board.getSize() || y < 0 || y >= board.getSize() ){
				throw new InvalidMoveException("Invalid move");
			}
			
			if (ruleset.validateMove(board, getPlayersColor(playerNo), x, y)) {
				try {
					IntPair placeResult = board.placeStone(getPlayersColor(playerNo), x, y);
					capturedStones[playerNo] += placeResult.x;
					capturedStones[1 - playerNo] += placeResult.y;

					players[0].updateBoard();
					players[1].updateBoard();
					
					consecutivePasses = 0;
					currentPlayer = getOpposingPlayer(currentPlayer);
					currentPlayer.notifyAboutTurn(GoMoveType.MOVE);
				} catch (IllegalArgumentException e){
					throw new InvalidMoveException("Invalid move");
				}
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
	 * @see goserver.game.GoGame#passTurn(goserver.game.GoPlayer)
	 */
	@Override
	public void passTurn(GoPlayer player) {
		if (isPlayersTurn(player) && isStonePlacingPhase()) {
			consecutivePasses++;
			currentPlayer = getOpposingPlayer(currentPlayer);
			//sprawdz czy consecutivePasses >= 2
			if (consecutivePasses >= 2){
				setGamePhase(1);
				board.getAllGroupLabels();
				
				players[0].notifyAboutGamePhaseChange(getGamePhase());
				players[1].notifyAboutGamePhaseChange(getGamePhase());
				
				players[0].updateBoard();
				players[1].updateBoard();
				
				currentPlayer.notifyAboutTurn(GoMoveType.PASS);
			} else {
				currentPlayer.notifyAboutTurn(GoMoveType.PASS);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @param groupTypeChanges
	 * @see goserver.game.GoBoard#applyGroupTypeChanges(java.util.Map)
	 */
	public void applyGroupTypeChanges(GoPlayer player, Map<Integer, GoGroupType> groupTypeChanges) {
		if (isPlayersTurn(player) && isGroupMarkingPhase()){
			groupMarkingPhaseLength++;
			currentPlayer = getOpposingPlayer(currentPlayer);
			if(board.applyGroupTypeChanges(groupTypeChanges)){
				if (groupMarkingPhaseLength < MAX_GROUP_MARKING_PHASE_LENGTH){
					players[0].updateBoard();
					players[1].updateBoard();
					
					if (areAllGroupsLocked()){
						// oblicz wynik, powiadom o wygranej/przegranej/remisie
						endGame();
					} else {
						currentPlayer.notifyAboutTurn(GoMoveType.GROUP_CHANGED);
					}
				} else {
					setGamePhase(0);
					board.resetGroupLabels();
					currentPlayer.notifyAboutGamePhaseChange(getGamePhase());
					players[0].updateBoard();
					players[1].updateBoard();
				}
			} else {
				if (areAllGroupsLocked()){
					// oblicz wynik, powiadom o wygranej/przegranej/remisie
					endGame();
				} else {
					currentPlayer.notifyAboutTurn(GoMoveType.GROUP_NOCHANGE);
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected void endGame() {
		if (!isGameEnd()) {
			board.removeDeadGroups();
			IntPair scores = board.calculateTerritoryScore();
			score[0] = (double) (scores.x - capturedStones[1]);
			score[1] = (double) (scores.y - capturedStones[0]);
			ruleset.onGameEnd(this);

			setGamePhase(2);

			players[0].notifyAboutGameEnd(score[0], score[1]);
			players[1].notifyAboutGameEnd(score[1], score[0]);

			currentPlayer = null;
		}
	}

	protected boolean areAllGroupsLocked() {
		for(int label : board.getAllGroupLabels()){
			if (!board.checkIfGroupIsLocked(label)){
				return false;
			}
		}
		return true;
	}
	
	public List<Integer> getAllLockedGroups(){
		List<Integer> lockedGroups = new ArrayList<Integer>();
		
		for(int label : board.getAllGroupLabels()){
			if (board.checkIfGroupIsLocked(label)){
				lockedGroups.add(label);
			}
		}
		
		return lockedGroups;
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

	public GoPlayer getOpposingPlayer(GoPlayer player) {
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
	
	protected void setGamePhase(int gamePhase) {
		if (gamePhase != 0 && gamePhase != 1 && gamePhase != 2) {
			throw new IllegalArgumentException();
		}
		consecutivePasses = 0;
		groupMarkingPhaseLength = 0;
		this.gamePhase = gamePhase;
	}

	@Override
	public int getGamePhase() {
		return gamePhase;
	}

	@Override
	public boolean isStonePlacingPhase() {
		return getGamePhase() == 0;
	}

	@Override
	public boolean isGroupMarkingPhase() {
		return getGamePhase() == 1;
	}
	
	@Override
	public boolean isGameEnd() {
		return getGamePhase() == 2;
	}
	
	@Override
	public Map<Integer, GoGroupType> getLabelsMap() {
		Map<Integer, GoGroupType> labelMap = new HashMap<Integer, GoGroupType>();
		
		for(int label : board.getAllGroupLabels()){
			labelMap.put(label, board.getGroupType(label));
		}
		
		return labelMap;
	}

	@Override
	public double getPlayersScore(GoPlayer player) {
		return score[getPlayersNo(player)];
	}

	@Override
	public void setPlayersScore(GoPlayer player, double score) {
		this.score[getPlayersNo(player)] = score;
	}

	@Override
	public void leaveGame(GoPlayer player) {
		if (!isGameEnd()) {
			int playerNo = getPlayersNo(player);
			score[1 - playerNo] = board.getSize() * board.getSize();
			score[playerNo] = 0;

			setGamePhase(2);

			System.out.println("Notifying player " + (1 - playerNo));
			players[1 - playerNo].notifyAboutGameEnd(score[1 - playerNo], score[playerNo]);

			currentPlayer = null;
		}
	}

	@Override
	public void requestRematch(GoPlayer player) {
		if (isGameEnd()) {
			int playerNo = getPlayersNo(player);
			rematchRequested[playerNo] = true;
			if (rematchRequested[0] == true && rematchRequested[1] == true){
				players[0].rematchAccepted();
				players[1].rematchAccepted();
				restartGame();
			}
		}
	}

	@Override
	public void denyRematch(GoPlayer player) {
		int playerNo = getPlayersNo(player);
		players[1 - playerNo].rematchDenied();
	}

}
