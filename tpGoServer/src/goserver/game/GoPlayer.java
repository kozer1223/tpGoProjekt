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
	void notifyAboutTurn();
	void updateBoard();

}
