package it.sturrini.gamesite.model.research;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import it.sturrini.gamesite.dao.mongo.ResearchesDeserializer;
import it.sturrini.gamesite.model.BaseEntity;

public class Research extends BaseEntity {

	private static Logger log = LogManager.getLogger(Research.class);

	private Long pointsDone;

	private String playerId;

	@JsonDeserialize(using = ResearchesDeserializer.class)
	private Researches researchEnum;

	public Research() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Researches getResearchEnum() {
		return researchEnum;
	}

	public void setResearchEnum(Researches researchEnum) {
		this.researchEnum = researchEnum;
	}

	public Long getPointsDone() {
		return pointsDone;
	}

	public void setPointsDone(Long pointsDone) {
		this.pointsDone = pointsDone;
	}

}
