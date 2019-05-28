package it.sturrini.gamesite.model.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.MapElementController;
import it.sturrini.gamesite.dao.SearchFilter;
import it.sturrini.gamesite.model.BaseEntity;
import it.sturrini.gamesite.model.Dimension;
import it.sturrini.gamesite.model.Grid;

public class Map extends BaseEntity {

	private static Logger log = LogManager.getLogger(Map.class);

	/**
	 *
	 */
	private static final long serialVersionUID = -4397662008810591930L;

	private Grid<GridItem> grid;

	private MapTypes mapType;

	private String playerId;

	private Long people;

	public Map() throws InstantiationException, IllegalAccessException {
		super();
		grid = new Grid<>(GridItem.class, 4, 4, 4);
		people = 0L;
	}

	public Grid<GridItem> getGrid() {
		return grid;
	}

	public void setGrid(Grid<GridItem> grid) {
		this.grid = grid;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(mapType.label() + " of player " + playerId + "\n");
			int x = grid.getSizeX();
			int y = grid.getSizeY();
			for (int i = 0; i < x; i++) {
				sb.append("| ");
				for (int j = 0; j < y; j++) {
					GridItem item;
					item = grid.getItem(i, j);
					sb.append(item.getLabelNormalized() + " |");

				}
				sb.append("\n");
			}

		} catch (GamesiteException e) {
			log.error(e);
			return "Map [grid=" + grid + ", playerId=" + playerId + "]";
		}
		return sb.toString();
	}

	@JsonIgnore
	public boolean isPlaceable(MapElement me, Dimension position) throws GamesiteException {
		try {
			GridItemInterface gii = me.getType();
			String elementId = me.getId();
			Dimension bd = gii.getDimension();
			int sizeX = this.getGrid().getSizeX();
			int sizeY = this.getGrid().getSizeY();
			if (position.getX() + bd.getX() > sizeX || position.getY() + bd.getY() > sizeY) {
				return false;
			}
			List<LandTypes> landTypes = gii.getLandTypes();
			boolean out = true;
			for (int i = position.getX().intValue(); i < position.getX() + bd.getX().intValue(); i++) {
				for (int j = position.getY().intValue(); j < position.getY() + bd.getY().intValue(); j++) {
					GridItem item = this.grid.getItem(i, j);
					if ((item.getElementId() != null && elementId != null && !item.getElementId().equals(elementId)) &&
							(!(item.getTypeEnum() instanceof LandTypes) || !landTypes.contains(item.getTypeEnum()))) {
						out = false;
						break;
					}
				}
			}
			return out;
		} catch (Exception e) {
			throw GamesiteException.managedException(e);
		}
	}

	@JsonIgnore
	public boolean isPresent(MapElement me, Dimension position) throws GamesiteException {
		try {
			GridItemInterface gii = me.getType();
			String elementId = me.getId();
			Dimension bd = gii.getDimension();
			int sizeX = this.getGrid().getSizeX();
			int sizeY = this.getGrid().getSizeY();
			if (position.getX() + bd.getX() > sizeX || position.getY() + bd.getY() > sizeY) {
				return false;
			}

			boolean out = true;
			for (int i = position.getX().intValue(); i < position.getX() + bd.getX().intValue(); i++) {
				for (int j = position.getY().intValue(); j < position.getY() + bd.getY().intValue(); j++) {
					GridItem item = this.grid.getItem(i, j);
					if (!item.getTypeEnum().getClass().equals(gii.getClass()) || !item.getElementId().equals(elementId)) {
						out = false;
						break;
					}
				}
			}
			return out;
		} catch (Exception e) {
			throw GamesiteException.managedException(e);
		}
	}

	@JsonIgnore
	public boolean place(MapElement me, Dimension position) throws GamesiteException {
		try {
			GridItemInterface gii = me.getType();
			String elementId = me.getId();
			if (!isPlaceable(me, position) && !reachedMaxNumber(gii)) {
				throw new GamesiteException("The building is not placeable here!");
			}
			Dimension bd = gii.getDimension();
			int sizeX = this.getGrid().getSizeX();
			int sizeY = this.getGrid().getSizeY();
			if (position.getX() + bd.getX() > sizeX || position.getY() + bd.getY() > sizeY) {
				return false;
			}
			List<LandTypes> landTypes = gii.getLandTypes();
			boolean out = true;
			for (int i = position.getX().intValue(); i < position.getX() + bd.getX().intValue(); i++) {
				for (int j = position.getY().intValue(); j < position.getY() + bd.getY().intValue(); j++) {
					GridItem item = new GridItem(gii, elementId, new Dimension((long) i, (long) j));
					this.grid.addItem(item, i, j);
				}
			}
			return out;
		} catch (Exception e) {
			throw GamesiteException.managedException(e);
		}
	}

	@JsonIgnore
	public int count(GridItemInterface gii) throws GamesiteException {
		int number = 0;
		Long area = gii.getDimension().getX() * gii.getDimension().getY();
		for (int i = 0; i < grid.getSizeX(); i++) {
			for (int j = 0; j < grid.getSizeY(); j++) {
				GridItem item = grid.getItem(i, j);
				if (item.getTypeEnum().equals(gii)) {
					number++;
				}
			}
		}
		return number / area.intValue();
	}

	@JsonIgnore
	public boolean reachedMaxNumber(GridItemInterface gii) throws GamesiteException {

		int number = 0;
		Long area = gii.getDimension().getX() * gii.getDimension().getY();
		for (int i = 0; i < grid.getSizeX(); i++) {
			for (int j = 0; j < grid.getSizeY(); j++) {
				GridItem item = grid.getItem(i, j);
				if (item.getTypeEnum().equals(gii)) {
					number++;
				}
			}
		}
		int num = number / area.intValue();
		return num >= gii.getMaxNumber();
	}

	@JsonIgnore
	public boolean remove(MapElement me, Dimension position) throws GamesiteException {
		try {
			GridItemInterface gii = me.getType();
			String elementId = me.getId();
			if (!isPresent(me, position)) {
				throw new GamesiteException("The building is not present here!");
			}
			Dimension bd = gii.getDimension();
			int sizeX = this.getGrid().getSizeX();
			int sizeY = this.getGrid().getSizeY();

			boolean out = true;
			for (int i = position.getX().intValue(); i < position.getX() + bd.getX().intValue(); i++) {
				for (int j = position.getY().intValue(); j < position.getY() + bd.getY().intValue(); j++) {
					GridItem item = new GridItem(LandTypes.land, "", new Dimension((long) i, (long) j));
					this.grid.addItem(item, i, j);
				}
			}
			return out;
		} catch (Exception e) {
			throw GamesiteException.managedException(e);
		}
	}

	@JsonIgnore
	public boolean move(MapElement me, Dimension from, Dimension to) throws GamesiteException {
		try {
			boolean out = true;
			out = remove(me, from);
			if (!out) {
				return out;
			}
			out = place(me, to);
			return out;
		} catch (Exception e) {
			throw GamesiteException.managedException(e);
		}
	}

	public MapTypes getMapType() {
		return mapType;
	}

	public void setMapType(MapTypes mapType) {
		this.mapType = mapType;
	}

	@JsonIgnore
	public boolean isConnected(GridItemInterface gii, String id, Dimension to, List<String> excluded) throws GamesiteException {
		if (excluded == null) {
			excluded = new ArrayList<>();
		}
		if (id != null) {
			excluded.add(id);
		}
		if (gii.equals(Buildings.TownHall)) {
			return true;
		}
		Dimension size = gii.getDimension();
		List<Dimension> boundaries = getBoundaries(to, size);
		List<GridItem> gridItems = boundaries.stream().map(d -> {
			try {
				return this.getGrid().getItem(d.getX().intValue(), d.getY().intValue());
			} catch (GamesiteException e) {
				log.error("Something not worked", e);
				return null;
			}
		}).collect(Collectors.toList());
		List<GridItem> connectedList = gridItems.stream().filter(gi -> gi.getTypeEnum().equals(Buildings.TownHall)).collect(Collectors.toList());
		List<GridItem> toRemove = new ArrayList<>();
		for (GridItem gridItem : connectedList) {
			if (excluded.contains(gridItem.getElementId())) {
				toRemove.add(gridItem);
			}
		}
		connectedList.removeAll(toRemove);
		if (connectedList.size() > 0) {
			return true;
		}
		List<GridItem> streetList = gridItems.stream().filter(gi -> gi.getTypeEnum().equals(Streets.Street)).collect(Collectors.toList());
		if (streetList.size() == 0) {
			return false;
		}
		for (GridItem gi : streetList) {
			GridItemInterface otherGii = gi.getTypeEnum();
			SearchFilter sf = new SearchFilter();
			java.util.Map<String, Object> params = new HashMap<>();
			params.put("position.x", gi.getPosition().getX());
			params.put("position.y", gi.getPosition().getY());
			sf.setParams(params);
			List<MapElement> found = MapElementController.getInstance().find(sf);
			String id2 = found.get(0).getId();
			boolean connected = isConnected(otherGii, id2, gi.getPosition(), excluded);
			if (connected) {
				return true;
			}
		}
		return false;
	}

	private List<Dimension> getBoundaries(Dimension to, Dimension size) {

		List<Dimension> out = new ArrayList<>();
		for (int i = to.getX().intValue(); i < to.getX().intValue() + size.getX(); i++) {
			if (to.getY() - 1 >= 0) {
				Dimension d2 = new Dimension((long) i, to.getY() - 1);
				out.add(d2);
			}
			for (int j = to.getY().intValue(); j < to.getY().intValue() + size.getY(); j++) {
				if (j == to.getY().intValue() && (i - 1) >= 0) {
					Dimension d = new Dimension((long) i - 1, (long) j);
					out.add(d);
				}
				if (j == to.getY().intValue() + size.getY() - 1 && (i + 1) <= this.getGrid().getSizeX()) {
					Dimension d = new Dimension((long) i + 1, (long) j);
					out.add(d);
				}
			}
			if (to.getY().intValue() + size.getY() <= this.getGrid().getSizeY()) {
				Dimension d3 = new Dimension((long) i, to.getY().intValue() + size.getY());
				out.add(d3);
			}
		}
		return out;
	}

	public void addPeople(Long people) {
		this.people += people;
	}

	public void removePeople(Long people) {
		this.people -= people;
	}

	public Long getPeople() {
		return people;
	}

	public void setPeople(Long people) {
		this.people = people;
	}

}
