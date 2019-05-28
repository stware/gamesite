package it.sturrini.gamesite.controllers.researches;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.ControllerWithEvents;
import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.dao.SearchFilter;
import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.events.EventRulesExecutor;
import it.sturrini.gamesite.model.BaseEntity;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.research.Research;
import it.sturrini.gamesite.model.research.Researches;

public class ResearchController extends ControllerWithEvents {

	static Logger log;

	private static ResearchController instance = null;

	protected ResearchController() {
		super();
	}

	private void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");

	}

	public static ResearchController getInstance() {
		if (instance == null) {
			synchronized (ResearchController.class) {
				if (instance == null) {
					instance = new ResearchController();
					log = LogManager.getLogger(instance.getClass());
					instance.init();
				}
			}
		}
		return instance;
	}

	public List<Research> getResearches(Player p) throws GamesiteException {
		try {
			List<Research> out = new ArrayList<>();
			SearchFilter sf = new SearchFilter();
			sf.addParam("playerId", p.getId());
			List<Research> researchesDone = MongoDao.getInstance(Research.class).findByFilter(sf);
			out.addAll(researchesDone);
			List<Researches> rdEnum = researchesDone.stream().map(rd -> rd.getResearchEnum()).collect(Collectors.toList());
			Researches[] values = Researches.values();
			for (Researches r : values) {
				if (researchesDone == null || researchesDone.isEmpty() || !rdEnum.contains(r)) {
					Research newRes = new Research();
					newRes.setResearchEnum(r);
					newRes.setPointsDone(0L);
					out.add(newRes);
				}
			}
			return out;
		} catch (Exception e) {
			throw GamesiteException.managedException(e);
		}

	}

	public Research doResearch(Player player, Researches research, long points) throws GamesiteException {
		try {

			Long mandatory = research.getResearchPoints();
			List<Research> out = new ArrayList<>();
			SearchFilter sf = new SearchFilter();
			sf.addParam("playerId", player.getId());
			List<Research> researchesDone = MongoDao.getInstance(Research.class).findByFilter(sf);
			if (!isResercheable(researchesDone, research, player, points)) {
				throw new GamesiteException("Research blocked");
			}
			List<Research> found = researchesDone.stream().filter(rd -> rd.getResearchEnum().equals(research)).collect(Collectors.toList());
			Research r = null;
			if (found != null && researchesDone.size() == 1) {
				r = found.get(0);
			} else {
				r = new Research();
				r.setPointsDone(0L);
				r.setResearchEnum(research);
				r.setPlayerId(player.getId());
			}

			if (r.getPointsDone() + points <= mandatory) {
				r.setPointsDone(r.getPointsDone() + points);
			}

			boolean result = MongoDao.getInstance(Research.class).saveOrUpdate(r);
			if (!result) {
				throw new GamesiteException("Failed to save research");
			}
			this.fireEvent(Event.research, r, points);
			return r;

		} catch (Exception e) {
			throw GamesiteException.managedException(e);
		}
	}

	public boolean isResercheable(List<Research> researchesDone, Researches researchToDo, Player p, Long points) {
		if (p == null || p.getPoints() < points) {
			return false;
		}
		List<Researches> collect = researchesDone.stream().filter(rd -> rd.getPointsDone() == rd.getResearchEnum().getResearchPoints()).flatMap(
				rd -> rd.getResearchEnum().getChildren().stream()).collect(Collectors.toList());
		collect.addAll(Researches.start.getChildren());
		if (!collect.contains(researchToDo)) {
			return false;
		}
		return true;
	}

	@Override
	public List<String> onEvent(Event e, BaseEntity be, Object... args) {
		super.onEvent(e, be, args);
		List<String> result = EventRulesExecutor.getInstance().executeRules(this.getClass().getSimpleName(), e, be, args);
		return result;
	}
}
