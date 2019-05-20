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
import it.sturrini.gamesite.model.map.MapElement;

public class ManageScore extends EventBaseRule implements EventRule {

	private static Logger log = LogManager.getLogger(ManageScore.class);

	public ManageScore() {
		super();
	}

	@Override
	public boolean isExecutable(Event e, Object source) {
		return (e.equals(Event.create) || e.equals(Event.delete)) && source instanceof MapElement;
	}

	@Override
	public List<String> execute(Event e, Object source) {
		List<String> out = new ArrayList<>();
		try {
			MapElement me = (MapElement) source;

			Player player = (Player) MongoDao.getInstance(Player.class).findById(me.getPlayerId());
			if (e.equals(Event.create)) {
				player.addPoints(me.getType().getPoints());
			} else {
				player.removePoints(me.getType().getPoints());
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
