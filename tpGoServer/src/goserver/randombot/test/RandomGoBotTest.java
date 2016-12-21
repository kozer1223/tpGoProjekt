package goserver.randombot.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
import goserver.game.GoGroupType;
import goserver.game.GoPlayer;
import goserver.game.InvalidMoveException;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.KomiRule;
import goserver.game.test.EmptyGoPlayer;
import goserver.randombot.RandomGoBot;

public class RandomGoBotTest {

	@Test
	public void testRandomBotMakeSingleMove() throws InvalidMoveException {
		GoPlayer player = new EmptyGoPlayer();
		GoPlayer bot = new RandomGoBot();
		int size = 19;
		GoGame game = new DefaultGoGame(player, bot, size, DefaultGoRuleset.getDefaultRuleset());

		game.makeMove(player, 0, 0);
		// bot wykonał ruch
		int[][] board = game.getBoard().getBoardData();
		boolean foundBotsStone = false;
		for (int i=0; i < game.getBoard().getSize(); i++){
			for (int j=0; j < game.getBoard().getSize(); j++){
				if (board[i][j] == game.getBoard().getWhiteColor()){
					foundBotsStone = true;
				}
			}
		}
		assertTrue(foundBotsStone);
	}
	
	@Test
	public void testUntilBotPasses() throws InvalidMoveException {
		GoPlayer player = new EmptyGoPlayer();
		GoPlayer bot = new RandomGoBot();
		int size = 19;
		GoGame game = new DefaultGoGame(player, bot, size, DefaultGoRuleset.getDefaultRuleset());

		int turns = 0;
		while(!game.isGroupMarkingPhase()){
			if (turns > size * size){
				fail();
			}
			
			game.passTurn(player);
			turns++;
		}
		assertTrue(true);
	}
	
	@Test
	public void testBotAgreesWithAllLabelChanges() throws InvalidMoveException {
		GoPlayer player = new EmptyGoPlayer();
		GoPlayer bot = new RandomGoBot();
		int size = 19;
		GoGame game = new DefaultGoGame(player, bot, size, DefaultGoRuleset.getDefaultRuleset());

		game.makeMove(player, 0, 0);
		int turns = 1;
		while(!game.isGroupMarkingPhase()){
			if (turns > size * size){
				fail();
			}
			
			game.passTurn(player);
			turns++;
		}
		
		Map<Integer, GoGroupType> labels = game.getLabelsMap();
		
		game.applyGroupTypeChanges(player, new HashMap<Integer, GoGroupType>());
		assertEquals(labels, game.getLabelsMap());
	}
	
}
