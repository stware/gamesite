package it.sturrini.gamesite.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.dao.SearchFilter;
import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.map.Map;
import it.sturrini.gamesite.model.map.MapTypes;

public class MapController extends ControllerWithEvents {

	static Logger log;

	private static MapController instance = null;

	protected MapController() {
		super();
	}

	private void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");

	}

	public static MapController getInstance() {
		if (instance == null) {
			synchronized (MapController.class) {
				if (instance == null) {
					instance = new MapController();
					log = LogManager.getLogger(instance.getClass());
					instance.init();
				}
			}
		}
		return instance;
	}

	public Map create(MapTypes type, Player p) throws Exception {
		try {
			Map map = new Map();
			map.setMapType(type);
			map.setPlayerId(p.getId());
			boolean result = MongoDao.getInstance(Map.class).saveOrUpdate(map);
			if (result) {
				List<String> errors = fireEvent(Event.create, map);
				if (errors.size() > 0) {
					throw new GamesiteException(errors);
				}
				return map;
			} else {
				throw new GamesiteException();
			}
		} catch (Exception e) {
			if (e instanceof GamesiteException) {
				throw e;
			} else {
				throw new GamesiteException();
			}
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

	public Map getById(String id) throws GamesiteException {

		if (id == null) {
			throw new GamesiteException();
		}
		Map p = (Map) MongoDao.getInstance(Map.class).findById(id);
		if (p != null) {
			return p;
		} else {
			throw new GamesiteException();
		}

	}

	public List<Map> find(SearchFilter sf) throws GamesiteException {

		List<Map> maps = MongoDao.getInstance(Map.class).findByFilter(sf);
		if (maps != null) {
			return maps;
		} else {
			throw new GamesiteException();
		}

	}

	public Map updateMap(String playerId, String mapId, Map map) throws GamesiteException {
		try {
			Player player = (Player) MongoDao.getInstance(Player.class).findById(playerId);
			if (player == null || player.getId() == null) {
				throw new GamesiteException("Player not found");
			}
			boolean mapSaved = MongoDao.getInstance(Map.class).saveOrUpdate(map);
			if (!mapSaved) {
				throw new GamesiteException("Error saving map");
			}
			return map;
		} catch (Exception e) {
			if (e instanceof GamesiteException) {
				throw e;
			} else {
				throw new GamesiteException(e);
			}
		}
	}
}
