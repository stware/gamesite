package it.sturrini.gamesite.model.actions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.actionrules.ActionsEnum;
import it.sturrini.gamesite.dao.mongo.LongDeserializer;
import it.sturrini.gamesite.dao.mongo.ResearchesDeserializer;
import it.sturrini.gamesite.model.research.Researches;

public class ResearchAction extends Action {

	public ResearchAction() {
		super();
		this.type = ActionsEnum.Research;

	}

	public ResearchAction(String type, String playerId) throws GamesiteException {
		super(type, playerId);

	}

	@JsonDeserialize(using = ResearchesDeserializer.class)
	private Researches researchEnum;
	@JsonDeserialize(using = LongDeserializer.class)
	private Long points;

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

	public Researches getResearchEnum() {
		return researchEnum;
	}

	public void setResearchEnum(String researchEnum) {
		this.researchEnum = Researches.valueOf(researchEnum);
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

}
