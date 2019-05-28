package it.sturrini.gamesite.controllers.actionrules;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.MapElementController;
import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.model.Dimension;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.model.actions.Action;
import it.sturrini.gamesite.model.actions.MoveMapElementAction;
import it.sturrini.gamesite.model.map.Map;
import it.sturrini.gamesite.model.map.MapElement;

public class MapElementActionRule extends ActionBaseRule implements ActionRule {

	private static Logger log = LogManager.getLogger(MapElementActionRule.class);

	private MoveMapElementAction nb;

	public MapElementActionRule(Action a) {
		super(a);
		try {
			nb = (MoveMapElementAction) this.a;
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public boolean isExecutable() {
		return nb != null && nb.getMapElement() != null && !(nb.getTo() == null && nb.getFrom() == null);
	}

	@Override
	public List<String> execute() {

		List<String> out = new ArrayList<>();
		try {
			Dimension bd = getMapElementDimension();
			Map pc = getPlayerMap();

			if (nb.getTo() != null && nb.getFrom() == null) {
				// New Map Element
				if (pc.reachedMaxNumber(this.nb.getMapElementEnum())) {
					out.add("Reached the max number for this Map Element");
				}
				MapElement me = new MapElement(this.nb.getMapElementEnum());
				boolean isPlaceable = pc.isPlaceable(me, this.nb.getTo());
				if (!isPlaceable) {
					out.add("The building is not placeable here!");
				} else {
					boolean connected = pc.isConnected(this.nb.getMapElementEnum(), null, this.nb.getTo(), new ArrayList<>());
					MapElement mapElement = MapElementController.getInstance().create(this.nb.getMapElementEnum(), this.a.getPlayer(), pc, this.nb.getTo(), connected);
					boolean result = pc.place(mapElement, this.nb.getTo());

				}
			} else if (nb.getTo() == null && nb.getFrom() != null) {
				// remove
				MapElement element = (MapElement) MongoDao.getInstance(MapElement.class).findById(this.nb.getElementId());
				if (!pc.isPresent(element, this.nb.getFrom())) {
					out.add("The map element is not present here!");
				} else {
					Dimension position = nb.getFrom();
					pc.remove(element, this.nb.getFrom());
					boolean result = MapElementController.getInstance().delete(element);
					if (!result) {
						out.add("Cannot delete map element");
					}

				}
			} else {
				// move
				Dimension to = nb.getTo();
				Dimension from = nb.getFrom();
				MapElement me = (MapElement) MongoDao.getInstance(MapElement.class).findById(this.nb.getElementId());
				if (!pc.isPresent(me, this.nb.getFrom())) {
					out.add("The Map Element is not present here!");
				} else if (!pc.isPlaceable(me, this.nb.getTo())) {
					out.add("The Map Element is not placeable here!");
				} else {
					pc.move(me, this.nb.getFrom(), this.nb.getTo());
					me.setPosition(this.nb.getTo());
					boolean result = MapElementController.getInstance().update(me.getId(), me);
					if (!result) {
						out.add("Cannot update map element");
					}
				}
			}
			System.out.println(pc);
			MongoDao.getInstance(Map.class).saveOrUpdate(pc);

		} catch (Exception e) {
			log.error(e);
			out.add(e.getMessage());
		}
		return out;
	}

	private Dimension getMapElementDimension() throws GamesiteException {
		try {
			return this.nb.getMapElementEnum().getDimension();
		} catch (Exception e) {
			log.error(e);
			throw new GamesiteException(e);
		}

	}

	private Map getPlayerMap() throws GamesiteException {
		try {
			Player player = this.nb.getPlayer();
			Map city = (Map) MongoDao.getInstance(Map.class).findById(player.getCityId());
			if (city == null) {
				throw new GamesiteException("Citynot found for player " + player.getId());
			}
			return city;
		} catch (Exception e) {
			log.error(e);
			throw new GamesiteException(e);
		}

	}

}
