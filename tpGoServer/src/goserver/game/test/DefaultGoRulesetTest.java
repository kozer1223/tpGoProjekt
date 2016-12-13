package goserver.game.test;

import static org.junit.Assert.*;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.rules.GoRuleset;

public class DefaultGoRulesetTest {

	@Test
	public void testIncludeDefaultRuleset() {
		GoPlayer player1 = new EmptyGoPlayer();
		GoPlayer player2 = new EmptyGoPlayer();
		int size = 9;
		GoGame game = new DefaultGoGame(player1, player2, size, DefaultGoRuleset.getDefaultRuleset());
	}

}
