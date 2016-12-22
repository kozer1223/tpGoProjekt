package goserver.randombot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.util.IntPair;

/**
 * Implementation of a Go bot which makes random moves.
 * 
 * @author Kacper
 *
 */
public class RandomGoBot implements GoPlayer {

	GoGame game;
	Random rng;
	final double passChance = 0.5; // chance to pass after opponent passes turn

	/**
	 * Create a RandomGoBot.
	 */
	public RandomGoBot() {
		rng = new Random();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#setGame(goserver.game.GoGame)
	 */
	@Override
	public void setGame(GoGame game) {
		this.game = game;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutTurn(goserver.game.GoMoveType)
	 */
	@Override
	public void notifyAboutTurn(GoMoveType opponentsMove) {
		// make a move
		if (game.isPlayersTurn(this)) {
			if (game.isStonePlacingPhase()) {
				if (opponentsMove == GoMoveType.PASS) {
					double passRnd = rng.nextDouble();
					if (passRnd >= passChance) {
						passTurn();
						return;
					}
				}

				int[][] boardData = game.getBoard().getBoardData();
				List<IntPair> validMoves = new ArrayList<IntPair>();

				for (int i = 0; i < boardData.length; i++) {
					for (int j = 0; j < boardData[i].length; j++) {
						if (boardData[i][j] == game.getBoard().getEmptyColor()) {
							validMoves.add(new IntPair(i, j));
						}
					}
				}

				int index;
				while (!validMoves.isEmpty()) {
					index = rng.nextInt(validMoves.size());
					if (makeMove(validMoves.get(index).x, validMoves.get(index).y)) {
						return;
					} else {
						validMoves.remove(index);
					}
				}

				passTurn();
				return;
			} else if (game.isGroupMarkingPhase()) {
				// accept all group type changes
				game.applyGroupTypeChanges(this, new HashMap<Integer, GoGroupType>());
			}

		}
	}

	/**
	 * Attempt to place stone on a given spot.
	 * 
	 * @param x
	 *            X position on the board.
	 * @param y
	 *            Y position on the board.
	 * @return true if move was valid.
	 */
	private boolean makeMove(int x, int y) {
		try {
			game.makeMove(this, x, y);
			return true;
		} catch (InvalidMoveException e) {
			return false;
		}
	}

	/**
	 * Pass turn.
	 */
	private void passTurn() {
		game.passTurn(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#updateBoard()
	 */
	@Override
	public void updateBoard() {
		// bot already has access to the board
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutGamePhaseChange(int)
	 */
	@Override
	public void notifyAboutGamePhaseChange(int gamePhase) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutGameEnd(double, double)
	 */
	@Override
	public void notifyAboutGameEnd(double playerScore, double opponentScore) {
		// always accept a rematch
		game.requestRematch(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#rematchAccepted()
	 */
	@Override
	public void rematchAccepted() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#rematchDenied()
	 */
	@Override
	public void rematchDenied() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see goserver.game.GoPlayer#notifyAboutGameBegin()
	 */
	@Override
	public void notifyAboutGameBegin() {
	}

}
