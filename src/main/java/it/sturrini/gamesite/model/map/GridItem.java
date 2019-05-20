package it.sturrini.gamesite.model.map;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import it.sturrini.gamesite.dao.mongo.GridItemInterfaceDeserializer;
import it.sturrini.gamesite.model.Dimension;

@JsonPropertyOrder({ "type" })
public class GridItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8468460931255309219L;

	@JsonDeserialize(using = GridItemInterfaceDeserializer.class)
	private GridItemInterface type;

	private String elementId;

	private Dimension position;

	public GridItem() {
		super();
		type = LandTypes.land;
	}

	public GridItem(String type) {
		super();
		this.type = LandTypes.valueOf(type);
	}

	public GridItem(GridItemInterface type, String elementId) {
		super();
		this.type = type;
		this.elementId = elementId;
	}

	public GridItem(GridItemInterface type, String elementId, Dimension position) {
		super();
		this.type = type;
		this.elementId = elementId;
		this.position = position;
	}

	public String getType() {
		return type.name();
	}

	@JsonIgnore
	public String getLabel() {
		return type.label();
	}

	@JsonIgnore
	public String getLabelNormalized() {
		return type.labelNormalized();
	}

	@JsonIgnore
	public GridItemInterface getTypeEnum() {
		return type;
	}

	public String getElementId() {
		return elementId;
	}

	@Override
	public String toString() {
		return "GridItem [type=" + type + ", elementId=" + elementId + "]";
	}

	public Dimension getPosition() {
		return position;
	}

}
