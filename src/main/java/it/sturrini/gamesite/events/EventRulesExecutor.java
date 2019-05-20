package it.sturrini.gamesite.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.gamesite.controllers.player.ManageScore;

public class EventRulesExecutor {

	private static Logger log;

	private List<EventRule> rules;

	private static EventRulesExecutor instance;

	public EventRulesExecutor() {
		super();

	}

	public static EventRulesExecutor getInstance() {
		if (instance == null) {
			synchronized (EventRulesExecutor.class) {
				if (instance == null) {
					instance = new EventRulesExecutor();
					log = LogManager.getLogger(instance.getClass());
					instance.init();
				}
			}
		}
		return instance;
	}

	private void init() {
		rules = new ArrayList<>();
		rules.add(new ManageScore());

	}

	public List<String> executeRules(Event e, Object source) {
		List<String> errors = new ArrayList<>();
		rules.stream().filter(r -> r.isExecutable(e, source)).forEach(r -> {
			errors.addAll(r.execute(e, source));
		});
		return errors;
	}

}
