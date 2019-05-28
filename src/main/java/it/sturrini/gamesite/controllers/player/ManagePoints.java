package it.sturrini.gamesite.controllers.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.events.EventBaseRule;
import it.sturrini.gamesite.events.EventRule;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.research.Research;

public class ManagePoints extends EventBaseRule implements EventRule {

	private static Logger log = LogManager.getLogger(ManagePoints.class);

	public ManagePoints() {
		super();
	}

	@Override
	public boolean isExecutable(String caller, Event e, Object source, Object... args) {
		return caller.equals(PlayerController.class.getSimpleName()) && (e.equals(Event.research)) && source instanceof Research && args.length == 1;
	}

	@Override
	public List<String> execute(String caller, Event e, Object source, Object... args) {
		List<String> out = new ArrayList<>();
		try {
			Research me = (Research) source;
			long points = (long) args[0];
			Player player = (Player) MongoDao.getInstance(Player.class).findById(me.getPlayerId());
			if (e.equals(Event.research)) {
				player.setPoints(player.getPoints() - points);
			}
			boolean result = MongoDao.getInstance(Player.class).saveOrUpdate(player);
			if (!result) {
				out.add("Cannot save Player " + player.getNickname());
			}
		} catch (Exception ex) {
			log.debug("Something gone wrong", e);
			out.add("Something gone wrong : See logs");
		}
		return out;
	}

}
