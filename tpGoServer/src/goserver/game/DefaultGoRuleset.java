package goserver.game;

import goserver.game.rules.GoRuleset;
import goserver.game.rules.KoRule;
import goserver.game.rules.SuicideRule;

public class DefaultGoRuleset {

	private static GoRuleset defaultRulesetInstance;

	private DefaultGoRuleset() {
	};

	public synchronized static GoRuleset getDefaultRuleset() {
		if (defaultRulesetInstance == null) {
			defaultRulesetInstance = new GoRuleset();
			defaultRulesetInstance.addRule(SuicideRule.getInstance());
			defaultRulesetInstance.addRule(KoRule.getInstance());
		}
		return defaultRulesetInstance;
	}
	
}