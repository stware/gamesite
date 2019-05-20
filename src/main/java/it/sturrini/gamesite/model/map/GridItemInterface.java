package it.sturrini.gamesite.model.map;

import java.util.List;

import it.sturrini.gamesite.model.Dimension;

public interface GridItemInterface {

	public String name();

	public String label();

	public String labelNormalized();

	public Dimension getDimension();

	public int getMaxNumber();

	public List<LandTypes> getLandTypes();

	public Long getPoints();
}
