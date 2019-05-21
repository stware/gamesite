package it.sturrini.gamesite.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.dao.SearchFilter;
import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.model.Dimension;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.map.GridItemInterface;
import it.sturrini.gamesite.model.map.Map;
import it.sturrini.gamesite.model.map.MapElement;

public class MapElementController extends ControllerWithEvents {

	static Logger log;

	private static MapElementController instance = null;

	protected MapElementController() {
		super();
	}

	private void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");

	}

	public static MapElementController getInstance() {
		if (instance == null) {
			synchronized (MapElementController.class) {
				if (instance == null) {
					instance = new MapElementController();
					log = LogManager.getLogger(instance.getClass());
					instance.init();
				}
			}
		}
		return instance;
	}

	public MapElement create(GridItemInterface type, Player p, Map m, Dimension position, boolean connected) throws Exception {
		try {
			MapElement me = new MapElement();
			me.setLevel(1L);
			me.setMapId(m.getId());
			me.setPlayerId(p.getId());
			me.setPosition(position);
			me.setType(type);
			me.setConnected(connected);
			boolean result = MongoDao.getInstance(MapElement.class).saveOrUpdate(me);
			if (result) {
				fireEvent(Event.create, me);
				return me;
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

	public List<MapElement> find(SearchFilter sf) throws GamesiteException {

		List<MapElement> players = MongoDao.getInstance(MapElement.class).findByFilter(sf);
		if (players != null && players.size() > 0) {
			return players;
		} else {
			throw new GamesiteException();
		}

	}

	public boolean update(String mapElementId, MapElement me) throws GamesiteException {
		try {
			MapElement found = (MapElement) MongoDao.getInstance(MapElement.class).findById(mapElementId);
			if (found == null || found.getId() == null) {
				throw new GamesiteException("Map Element not found");
			}
			me.set_id(found.get_id());
			boolean mapSaved = MongoDao.getInstance(MapElement.class).saveOrUpdate(me);
			if (!mapSaved) {
				throw new GamesiteException("Error saving map");
			}
			fireEvent(Event.update, me);
			return mapSaved;
		} catch (Exception e) {
			if (e instanceof GamesiteException) {
				throw e;
			} else {
				throw new GamesiteException(e);
			}
		}
	}

	public boolean delete(MapElement element) {
		boolean result = MongoDao.getInstance(MapElement.class).delete(element);
		if (result) {
			fireEvent(Event.delete, element);
		}
		return result;

	}
}
