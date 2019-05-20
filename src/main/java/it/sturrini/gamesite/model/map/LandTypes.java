package it.sturrini.gamesite.model.map;

import java.util.Arrays;
import java.util.List;

import it.sturrini.common.StringUtil;
import it.sturrini.gamesite.model.Dimension;

public enum LandTypes implements GridItemInterface {

	land("Land"), nul("Null");

	private String label;

	private LandTypes(String label) {
		this.label = label;
	}

	@Override
	public String label() {
		return this.label;
	}

	@Override
	public String labelNormalized() {
		return StringUtil.leftPad(this.label, 15);
	}

	@Override
	public Dimension getDimension() {
		return new Dimension(1L, 1L);
	}

	@Override
	public int getMaxNumber() {
		return Integer.MAX_VALUE;
	}

	@Override
	public List<LandTypes> getLandTypes() {
		return Arrays.asList(new LandTypes[] { LandTypes.land });
	}

	@Override
	public Long getPoints() {

		return 0L;
	}

}
