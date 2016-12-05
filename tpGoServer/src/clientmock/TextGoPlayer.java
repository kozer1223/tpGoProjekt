package clientmock;

import goserver.game.GoBoard;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;

public class TextGoPlayer implements GoPlayer {
	
	GoGame game;
	public int playerId;
	
	public TextGoPlayer(int playerId){
		this.playerId = playerId;
	}
	
	public void makeMove(int x, int y) throws InvalidMoveException{
		game.makeMove(this, x, y);
	}
	
	public void passTurn(){
		game.passTurn(this);
	}
	
	public void drawBoard(){
		GoBoard board = game.getBoard();
		int[][] boardData = board.getBoard();
		int[][] labeledBoard = board.getBoardWithLabeledGroups();
		for(int i=0; i<board.getSize(); i++){
			for(int j=0; j<board.getSize(); j++){
				System.out.print(boardData[j][i]+" ");
			}
			System.out.print("   ");
			for(int j=0; j<board.getSize(); j++){
				System.out.print(labeledBoard[j][i]+" ");
			}
			System.out.println();
		}
	}
	
	public int getCapturedStones(){
		return game.getPlayersCapturedStones(this);
	}

	@Override
	public void setGame(GoGame game) {
		this.game = game;
	}

	@Override
	public void notifyAboutTurn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoard() {
		// TODO Auto-generated method stub
		
	}

}
