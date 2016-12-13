package goserver.game.rules.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.KoRule;
import goserver.game.rules.KomiRule;
import goserver.game.test.EmptyGoPlayer;

public class KomiRuleTest {

	@Test
	public void testSingleton() {
		assertNotEquals(KomiRule.getInstance(), null);
	}
	
	@Test
	public void testKomiPoints() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 19;
		GoGame game = new DefaultGoGame(player1, player2, size, new GoRuleset().with(KomiRule.getInstance()));

		game.passTurn(player1);
		game.passTurn(player2);
		assertTrue(game.isGroupMarkingPhase());
		game.applyGroupTypeChanges(player1, new HashMap<Integer, GoGroupType>());
		assertTrue(game.isGameEnd());
		
		assertTrue(game.getPlayersScore(game.getPlayer2()) == game.getPlayersScore(game.getPlayer1()) + KomiRule.komiPoints);
	}
}
