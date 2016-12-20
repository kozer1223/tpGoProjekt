package goserver.game.test;

import goserver.game.GoGame;
import goserver.game.GoMoveType;
import goserver.game.GoPlayer;

public class EmptyGoPlayer implements GoPlayer {

	@Override
	public void setGame(GoGame game) {
		// TODO Auto-generated method stub

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
		
	}

	@Override
	public void notifyAboutGameEnd(double playerScore, double opponentScore) {
		// TODO Auto-generated method stub
		
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