package it.sturrini.gamesite.model.map;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import it.sturrini.common.StringUtil;
import it.sturrini.gamesite.model.Dimension;

/**
 * Tutti gli edifici con le varie caratteristiche
 *
 * @author sturrini
 */
public enum Buildings implements GridItemInterface {

	TownHall("Town Hall", 4L, 4L, new String[] { "land" }, 1, 10L, false);

	private Buildings(String buildingName, Long x, Long y, String[] landTypes, int maxOccurrencies, Long points, boolean needsStreet) {
		this.name = buildingName;
		this.dimension = new Dimension(x, y);
		this.landTypes = Arrays.asList(landTypes).stream().map(s -> LandTypes.valueOf(s)).collect(Collectors.toList());
		this.maxNumber = maxOccurrencies;
		this.points = points;
		this.needsStreet = needsStreet;

	}

	private String name;
	private Dimension dimension;
	private List<LandTypes> landTypes;
	private int maxNumber;
	private Long points;
	private boolean needsStreet;

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
		return maxNumber;
	}

	@Override
	public Long getPoints() {
		return points;
	}

}
