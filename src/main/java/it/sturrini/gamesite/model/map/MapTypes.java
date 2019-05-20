package it.sturrini.gamesite.model.map;

import java.util.Arrays;
import java.util.List;

import it.sturrini.common.StringUtil;

public enum MapTypes {

	city("City"), battlefield("Battlefield"), world("World"), region("Region");

	private String label;

	private MapTypes(String label) {
		this.label = label;
	}

	public String label() {
		return this.label;
	}

	public String labelNormalized() {
		return StringUtil.leftPad(this.label, 15);
	}

	public List<MapTypes> getMapTypes() {
		return Arrays.asList(MapTypes.values());
	}

}
