package it.sturrini.gamesite.controllers.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.events.EventBaseRule;
import it.sturrini.gamesite.events.EventRule;
import it.sturrini.gamesite.model.Player;

public class ManagePlayers extends EventBaseRule implements EventRule {

	private static Logger log = LogManager.getLogger(ManagePlayers.class);

	public ManagePlayers() {
		super();
	}

	@Override
	public boolean isExecutable(String caller, Event e, Object source) {
		return caller.equals(GameContext.class.getSimpleName()) && (e.equals(Event.login)) && source instanceof Player;
	}

	@Override
	public List<String> execute(String caller, Event e, Object source) {
		List<String> out = new ArrayList<>();
		try {
			Player p = (Player) source;

			if (e.equals(Event.login)) {
				GameContext.getInstance().addPlayer(p);
			}
		} catch (Exception ex) {
			log.debug("Something gone wrong", e);
			out.add("Something gone wrong : See logs");
		}
		return out;
	}

}
