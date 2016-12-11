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
	//void setOpposingPlayer(GoPlayer player);
	void notifyAboutTurn(GoMoveType opponentsMove);
	void updateBoard();
	void notifyAboutGamePhaseChange(int gamePhase);
	void notifyAboutGameEnd(double playerScore, double opponentScore);

}
