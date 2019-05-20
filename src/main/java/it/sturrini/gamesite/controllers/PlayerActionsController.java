package it.sturrini.gamesite.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.actionrules.ActionRulesExecutor;
import it.sturrini.gamesite.model.actions.Action;

public class PlayerActionsController extends ControllerWithEvents {

	static Logger log;

	private static PlayerActionsController instance = null;

	protected PlayerActionsController() {
		super();
	}

	private void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");

	}

	public static PlayerActionsController getInstance() {
		if (instance == null) {
			synchronized (PlayerActionsController.class) {
				if (instance == null) {
					instance = new PlayerActionsController();
					log = LogManager.getLogger(instance.getClass());
					instance.init();
				}
			}
		}
		return instance;
	}

	public List<String> executeAction(Action a) throws GamesiteException {
		try {
			List<String> errors;
			ActionRulesExecutor executor = new ActionRulesExecutor(a);
			errors = executor.executeRules();
			return errors;
		} catch (Exception e) {
			if (e instanceof GamesiteException) {
				throw e;
			} else {
				log.error(e);
				throw new GamesiteException(e);
			}
		}

	}
}
