/**
 * 
 */
package goserver.game;

/**
 * @author Kacper
 *
 */
public interface GoPlayer {

	void setGame(GoGame game);

	void notifyAboutGameBegin();
	void notifyAboutTurn(GoMoveType opponentsMove);
	void updateBoard();
	void notifyAboutGamePhaseChange(int gamePhase);
	void notifyAboutGameEnd(double playerScore, double opponentScore);
	void rematchAccepted();
	void rematchDenied();

}
