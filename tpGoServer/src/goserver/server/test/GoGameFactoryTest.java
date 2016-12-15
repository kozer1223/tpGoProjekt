/**
 * 
 */
package goserver.server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.test.EmptyGoPlayer;
import goserver.server.GoGameFactory;

/**
 * @author Kacper
 *
 */
public class GoGameFactoryTest {

	@Test
	public void testSingleton() {
		GoGameFactory instance = GoGameFactory.getInstance();
		assertNotNull(instance);
	}
	
	@Test
	public void testCreateGameWithTwoPlayers() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = GoGameFactory.getInstance().createDefaultGoGameWithTwoPlayers(player1, player2, size);
		assertNotNull(game);
	}
	
	@Test
	public void testCreateGameWithBot() {
		GoPlayer player = new EmptyGoPlayer();
		int size = 9;
		GoGame game = GoGameFactory.getInstance().createDefaultGoGameWithBot(player, size);
		assertNotNull(game);
	}

}
