package it.sturrini.gamesite.controllers.actionrules;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.gamesite.controllers.researches.ResearchController;
import it.sturrini.gamesite.model.actions.Action;
import it.sturrini.gamesite.model.actions.ResearchAction;
import it.sturrini.gamesite.model.research.Research;

public class ResearchActionRule extends ActionBaseRule implements ActionRule {

	private static Logger log = LogManager.getLogger(ResearchActionRule.class);

	private ResearchAction ra;

	public ResearchActionRule(Action a) {
		super(a);
		try {
			ra = (ResearchAction) this.a;
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public boolean isExecutable() {
		return ra != null && ra.getResearchEnum() != null && ra.getPoints() > 0;
	}

	@Override
	public List<String> execute() {

		List<String> out = new ArrayList<>();
		try {
			Research research = ResearchController.getInstance().doResearch(this.ra.getPlayer(), ra.getResearchEnum(), ra.getPoints());
		} catch (Exception e) {
			log.error(e);
			out.add(e.getMessage());
		}
		return out;
	}

}
