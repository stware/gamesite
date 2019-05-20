package it.sturrini.gamesite.model.map;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import it.sturrini.common.StringUtil;
import it.sturrini.gamesite.model.Dimension;

/**
 * Tutte le strade con le varie caratteristiche
 *
 * @author sturrini
 */
public enum Streets implements GridItemInterface {

	Street("Street", 1L, 1L, new String[] { "land" }, 1);

	private Streets(String buildingName, Long x, Long y, String[] landTypes, int points) {
		this.name = buildingName;
		this.dimension = new Dimension(x, y);
		this.landTypes = Arrays.asList(landTypes).stream().map(s -> LandTypes.valueOf(s)).collect(Collectors.toList());
		this.points = points;

	}

	private String name;
	private Dimension dimension;
	private List<LandTypes> landTypes;
	private int points;

	public String getName() {
		return name;
	}

	@Override
	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public List<LandTypes> getLandTypes() {
		return landTypes;
	}

	@Override
	public String label() {
		return this.name;
	}

	@Override
	public String labelNormalized() {
		return StringUtil.leftPad(this.name, 15);
	}

	@Override
	public int getMaxNumber() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Long getPoints() {

		return 1L;
	}

}
