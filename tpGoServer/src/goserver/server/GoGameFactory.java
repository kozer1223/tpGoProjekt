package goserver.server;

import goserver.game.DefaultGoGame;
import goserver.game.DefaultGoRuleset;
import goserver.game.GoGame;
import goserver.game.GoPlayer;
import goserver.game.rules.GoRuleset;
import goserver.game.rules.SuicideRule;
import goserver.randombot.RandomGoBot;

/**
 * Factory which creates Go game objects using the DefaultGoGame implementation
 * and the RandomGoBot bot.
 * 
 * @author Kacper
 *
 */
public class GoGameFactory {

	private static GoGameFactory instance;

	private GoGameFactory() {
	}

	public synchronized static GoGameFactory getInstance() {
		if (instance == null) {
			instance = new GoGameFactory();
		}
		return instance;
	}

	/**
	 * Create a default Go Game with two online players.
	 * 
	 * @param player1
	 *            Black player.
	 * @param player2
	 *            White player.
	 * @param size
	 *            Board size.
	 * @return GoGame object.
	 */
	public GoGame createDefaultGoGameWithTwoPlayers(GoPlayer player1, GoPlayer player2, int size) {
		return new DefaultGoGame(player1, player2, size, DefaultGoRuleset.getDefaultRuleset());
	}

	/**
	 * Create a default Go Game with a player and a RandomGoBot.
	 * 
	 * @param player
	 *            Online player.
	 * @param size
	 *            Board size.
	 * @return GoGame object.
	 */
	public GoGame createDefaultGoGameWithBot(GoPlayer player, int size) {
		return new DefaultGoGame(player, new RandomGoBot(), size, DefaultGoRuleset.getDefaultRuleset());
	}

}
