package goserver.server;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.SuicideRule;
import goserver.randombot.RandomGoBot;

public class GoGameFactory {
	
	private static GoGameFactory instance;

	private GoGameFactory() {}
	
	public synchronized static GoGameFactory getInstance() {
		if (instance == null) {
			instance = new GoGameFactory();
		}
		return instance;
	}
	
	public GoGame createDefaultGoGameWithTwoPlayers(GoPlayer player1, GoPlayer player2, int size){
		return new DefaultGoGame (player1, player2, 19, DefaultGoRuleset.getDefaultRuleset());
	}
	
	public GoGame createDefaultGoGameWithBot(GoPlayer player, int size){
		return new DefaultGoGame (player, new RandomGoBot(), 19, DefaultGoRuleset.getDefaultRuleset());
	}

}
