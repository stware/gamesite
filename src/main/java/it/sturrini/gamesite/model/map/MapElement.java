package it.sturrini.gamesite.model.map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import it.sturrini.gamesite.dao.mongo.GridItemInterfaceDeserializer;
import it.sturrini.gamesite.model.BaseEntity;
import it.sturrini.gamesite.model.Dimension;

public class MapElement extends BaseEntity {

	private static Logger log = LogManager.getLogger(MapElement.class);

	/**
	 *
	 */
	private static final long serialVersionUID = -4397662008810591930L;

	@JsonDeserialize(using = GridItemInterfaceDeserializer.class)
	GridItemInterface type;

	private String playerId;

	private String mapId;

	private Dimension position;

	private Long level;

	private boolean connected;

	public MapElement() {
		super();
	}

	public MapElement(GridItemInterface type) {
		super();
		this.type = type;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public GridItemInterface getType() {
		return type;
	}

	public void setType(GridItemInterface type) {
		this.type = type;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public Dimension getPosition() {
		return position;
	}

	public void setPosition(Dimension position) {
		this.position = position;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

}
