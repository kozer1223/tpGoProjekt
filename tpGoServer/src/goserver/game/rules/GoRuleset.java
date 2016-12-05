/**
 * 
 */
package goserver.game.rules;

import java.util.ArrayList;
import java.util.List;

import goserver.game.GoBoard;
import goserver.game.GoGame;
import goserver.game.InvalidMoveException;

/**
 * @author Kacper
 *
 */
public class GoRuleset implements GoRule {
	
	List<GoRule> rules;

	public GoRuleset() {
		rules = new ArrayList<GoRule>();
	}
	
	public void onGameStart(GoGame game) {
		for(GoRule rule : rules){
			rule.onGameStart(game);
		}
	}

	public boolean validateMove(GoBoard board, int color, int x, int y) throws InvalidMoveException {
		boolean isValid = true;
		for(GoRule rule : rules){
			isValid = isValid && rule.validateMove(board, color, x, y);
		}
		return isValid;
	}
	
	public void addRule(GoRule rule){
		rules.add(rule);
	}
	
	public GoRuleset with(GoRule rule){
		addRule(rule);
		return this;
	}
	
	// TODO
	// onGameStart()
	// onGameEnd() / onScoreCount()

}
