package it.sturrini.gamesite.model.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.actionrules.ActionsEnum;
import it.sturrini.gamesite.dao.mongo.GridItemInterfaceDeserializer;
import it.sturrini.gamesite.model.Dimension;
import it.sturrini.gamesite.model.map.Buildings;
import it.sturrini.gamesite.model.map.GridItemInterface;
import it.sturrini.gamesite.model.map.LandTypes;
import it.sturrini.gamesite.model.map.Streets;

public class MoveMapElementAction extends Action {

	public MoveMapElementAction() {
		super();
		this.type = ActionsEnum.MoveMapElement;

	}

	public MoveMapElementAction(String type, String playerId) throws GamesiteException {
		super(type, playerId);

	}

	@JsonDeserialize(using = GridItemInterfaceDeserializer.class)
	private GridItemInterface mapElement;
	private String elementId;
	private Dimension from;
	private Dimension to;

	/**
	 *
	 */
	private static final long serialVersionUID = -8478331016459497127L;

	public void setType(String type) throws GamesiteException {
		if (ActionsEnum.valueOf(type) != null) {
			this.type = ActionsEnum.valueOf(type);
		} else {
			throw new GamesiteException("Action not found");
		}
	}

	public String getMapElement() {
		return mapElement.name();
	}

	@JsonIgnore
	public GridItemInterface getMapElementEnum() {
		return mapElement;
	}

	public void setMapElement(String type) {
		GridItemInterface gii = null;
		try {
			gii = Buildings.valueOf(type);
		} catch (Exception e) {

		}
		this.mapElement = gii;
		if (gii != null) {
			return;
		}
		try {
			gii = LandTypes.valueOf(type);
		} catch (Exception e) {

		}
		this.mapElement = gii;
		if (gii != null) {
			return;
		}
		try {
			gii = Streets.valueOf(type);
		} catch (Exception e) {

		}
		this.mapElement = gii;
		if (gii != null) {
			return;
		}
	}

	public Dimension getTo() {
		return to;
	}

	public void setTo(Dimension position) {
		this.to = position;
	}

	public Dimension getFrom() {
		return from;
	}

	public void setFrom(Dimension position) {
		this.from = position;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

}
