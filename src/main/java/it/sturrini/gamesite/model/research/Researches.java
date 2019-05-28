package it.sturrini.gamesite.model.research;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Researches {

	start("Start", 0L), simpleHome("Simple Home", 10L), simpleWork("Simple Work", 10L);

	private Researches(String label, Long researchPoints) {
		this.label = label;
		this.researchPoints = researchPoints;

	}

	private void init() {
		children.put(Researches.start, Arrays.asList(new Researches[] { Researches.simpleHome }));
		children.put(Researches.simpleHome, Arrays.asList(new Researches[] { Researches.simpleWork }));

	}

	private Map<Researches, List<Researches>> children = new HashMap<>();

	private String label;

	private Long researchPoints;

	public String getLabel() {
		return label;
	}

	public Long getResearchPoints() {
		return researchPoints;
	}

	public List<Researches> getChildren() {
		init();
		return children.get(this);
	}

	public String getName() {
		return this.name();
	}

}
