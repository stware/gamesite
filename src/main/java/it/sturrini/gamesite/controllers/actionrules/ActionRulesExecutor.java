package it.sturrini.gamesite.controllers.actionrules;

import java.util.ArrayList;
import java.util.List;

import it.sturrini.gamesite.model.actions.Action;

public class ActionRulesExecutor {

	private List<ActionRule> rules;

	public ActionRulesExecutor(Action a) {
		super();
		init(a);
	}

	private void init(Action a) {
		rules = new ArrayList<>();
		rules.add(new MapElementActionRule(a));
		rules.add(new ResearchActionRule(a));

	}

	public List<String> executeRules() {
		List<String> errors = new ArrayList<>();
		rules.stream().filter(r -> r.isExecutable()).forEach(r -> {
			errors.addAll(r.execute());
		});
		return errors;
	}

}
