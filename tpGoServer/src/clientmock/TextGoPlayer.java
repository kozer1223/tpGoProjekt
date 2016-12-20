package clientmock;

import java.util.Map;

import goserver.game.GoBoard;
import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;

public class TextGoPlayer implements GoPlayer {

	GoGame game;
	public int playerId;

	public TextGoPlayer(int playerId) {
		this.playerId = playerId;
	}

	public void makeMove(int x, int y) throws InvalidMoveException {
		game.makeMove(this, x, y);
	}

	public void passTurn() {
		game.passTurn(this);
	}
	
	public void applyGroupTypeChanges(Map<Integer, GoGroupType> groupTypeChanges) {
		game.applyGroupTypeChanges(this, groupTypeChanges);
	}
	
	public char encodeColors(int color) {
		if (color == game.getBoard().getBlackColor()){
			return '●';
		} else if (color == game.getBoard().getWhiteColor()){
			return '○';
		}
		return '◦';
	}

	public void drawBoard() {
		GoBoard board = game.getBoard();
		int[][] boardData = board.getBoard();
		int[][] labeledBoard = board.getBoardWithLabeledGroups();
		
		System.out.print("   ");
		for (int j = 0; j < board.getSize(); j++) {
			System.out.print(String.format("%1$-2s", j));
		}
		System.out.print("     ");
		for (int j = 0; j < board.getSize(); j++) {
			System.out.print(String.format("%1$-2s", j));
		}
		System.out.println();
		
		for (int i = 0; i < board.getSize(); i++) {
			System.out.print(String.format("%1$2s ", i));
			for (int j = 0; j < board.getSize(); j++) {
				System.out.print(encodeColors(boardData[j][i]) + " ");
			}
			
			System.out.print("  ");
			
			System.out.print(String.format("%1$2s ", i));
			for (int j = 0; j < board.getSize(); j++) {
				System.out.print(labeledBoard[j][i] + " ");
			}
			System.out.println();
		}
	}

	public int getCapturedStones() {
		return game.getPlayersCapturedStones(this);
	}
	
	public double getScore() {
		return game.getPlayersScore(this);
	}
	
	public int getGamePhase() {
		return game.getGamePhase();
	}

	@Override
	public void setGame(GoGame game) {
		this.game = game;
	}

	@Override
	public void notifyAboutTurn(GoMoveType opponentsMove) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateBoard() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyAboutGamePhaseChange(int gamePhase) {
		// TODO Auto-generated method stub
		//if (gamePhase == 1){
		//	TextGo.printMap(game.getLabelsMap());
		//}
	}

	@Override
	public void notifyAboutGameEnd(double playerScore, double opponentScore) {
		// TODO Auto-generated method stub
		System.out.println(playerId + " score: " + playerScore);
	}

	@Override
	public void rematchAccepted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rematchDenied() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAboutGameBegin() {
		// TODO Auto-generated method stub
		
	}

}
