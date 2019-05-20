package it.sturrini.gamesite.controllers.player;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import it.sturrini.common.DateUtils;
import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.ControllerWithEvents;
import it.sturrini.gamesite.controllers.GameContext;
import it.sturrini.gamesite.controllers.MapController;
import it.sturrini.gamesite.controllers.MapElementController;
import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.dao.SearchFilter;
import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.events.EventRulesExecutor;
import it.sturrini.gamesite.model.BaseEntity;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.map.Map;
import it.sturrini.gamesite.model.map.MapTypes;

public class PlayerController extends ControllerWithEvents {

	static Logger log;

	private static PlayerController instance = null;

	protected PlayerController() {
		super();
	}

	private void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");
		MapElementController.getInstance().registerListener(this);
	}

	public static PlayerController getInstance() {
		if (instance == null) {
			synchronized (PlayerController.class) {
				if (instance == null) {
					instance = new PlayerController();
					log = LogManager.getLogger(instance.getClass());
					instance.init();
				}
			}
		}
		return instance;
	}

	public Player signup(Player p) throws GamesiteException {

		if (p.getNickname() == null || p.getNickname().isEmpty() || p.getPassword() == null || p.getPassword().isEmpty()) {
			throw new GamesiteException();
		}
		SearchFilter sf = new SearchFilter();
		sf.getParams().put("nickname", p.getNickname());
		List<Player> found = MongoDao.getInstance(Player.class).findByFilter(sf);
		if (found != null && found.size() > 0) {
			throw new GamesiteException();
		}
		Player toSave;
		try {

			toSave = new Player();
			toSave.setNickname(p.getNickname());
			toSave.setPassword(p.getPassword());

			toSave.setEmail(p.getEmail());
			boolean result = MongoDao.getInstance(Player.class).saveOrUpdate(toSave);
			if (result) {
				Map city = MapController.getInstance().create(MapTypes.city, toSave);
				toSave.setCityId(city.getId());
				MongoDao.getInstance(Player.class).saveOrUpdate(toSave);
				return p;
			} else {
				throw new GamesiteException();
			}
		} catch (Exception e) {
			throw new GamesiteException();
		}

	}

	public String login(Player p) throws GamesiteException {

		if (p.getNickname() == null || p.getNickname().isEmpty() || p.getPassword() == null || p.getPassword().isEmpty()) {
			throw new GamesiteException();
		}
		SearchFilter sf = new SearchFilter();
		sf.getParams().put("nickname", p.getNickname());
		sf.getParams().put("password", p.getPassword());
		List<Player> players = MongoDao.getInstance(Player.class).findByFilter(sf);
		if (players.contains(p)) {
			List<Player> collect = players.stream().filter(pl -> pl.equals(p)).collect(Collectors.toList());
			if (collect == null || collect.isEmpty()) {
				throw new GamesiteException();
			}
			Player logged = collect.get(0);
			String out = "Welcome, " + logged.getNickname() + "! The server " + DateUtils.sdf.format(GameContext.getInstance().getGameServer().getServerTime());
			return out;
		} else {
			throw new GamesiteException();
		}

	}

	public Player update(String playerId, Player p) throws GamesiteException {

		if (playerId == null || p.getNickname() == null || p.getNickname().isEmpty() || p.getEmail() == null || p.getEmail().isEmpty()) {
			throw new GamesiteException();
		}
		Player logged = (Player) MongoDao.getInstance(Player.class).findById(playerId);
		if (logged != null && logged.getNickname().equals(p.getNickname())) {
			p.set_id(new ObjectId(playerId));
			MongoDao.getInstance(Player.class).saveOrUpdate(p);
			return p;
		} else {
			throw new GamesiteException();
		}

	}

	public Player getPlayer(String playerId) throws GamesiteException {

		if (playerId == null) {
			throw new GamesiteException();
		}
		Player p = (Player) MongoDao.getInstance(Player.class).findById(playerId);
		if (p != null) {
			return p;
		} else {
			throw new GamesiteException();
		}

	}

	public List<Player> findPlayers(SearchFilter sf) throws GamesiteException {

		List<Player> players = MongoDao.getInstance(Player.class).findByFilter(sf);
		if (players != null) {
			return players;
		} else {
			throw new GamesiteException();
		}

	}

	@Override
	public List<String> onEvent(Event e, BaseEntity be) {
		super.onEvent(e, be);
		List<String> result = EventRulesExecutor.getInstance().executeRules(e, be);
		return result;
	}

}