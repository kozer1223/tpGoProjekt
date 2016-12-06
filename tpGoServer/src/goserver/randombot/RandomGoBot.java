package goserver.randombot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.util.IntPair;

public class RandomGoBot implements GoPlayer {

	GoGame game;
	Random rng;

	public RandomGoBot() {
		// TODO Auto-generated constructor stub
		rng = new Random();
	}

	@Override
	public void setGame(GoGame game) {
		this.game = game;
	}

	@Override
	public void notifyAboutTurn() {
		// wykonaj ruch
		if (game.isPlayersTurn(this)) {
			int[][] boardData = game.getBoard().getBoard();
			List<IntPair> validMoves = new ArrayList<IntPair>();

			for (int i = 0; i < boardData.length; i++) {
				for (int j = 0; j < boardData[i].length; j++) {
					if(boardData[i][j] == game.getBoard().getEmptyColor()){
						validMoves.add(new IntPair(i,j));
					}
				}
			}
			
			
			int index;
			while (!validMoves.isEmpty()){
				index = rng.nextInt(validMoves.size());
				if(makeMove(validMoves.get(index).x, validMoves.get(index).y)){
					return;
				} else {
					validMoves.remove(index);
				}
			}
			
			passTurn();
			return;
			
		} else {
			// ???
		}
	}

	private boolean makeMove(int x, int y) {
		try {
			game.makeMove(this, x, y);
			return true;
		} catch (InvalidMoveException e) {
			return false;
		}
	}

	private void passTurn() {
		game.passTurn(this);
	}

	private int getBoardSize() {
		return game.getBoard().getSize();
	}

	@Override
	public void updateBoard() {
		// bot ma dostep do planszy poprzez serwer wiec nie musi byc
		// powiadamiany na bieżąco
	}

}
