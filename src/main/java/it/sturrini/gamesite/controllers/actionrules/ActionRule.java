package it.sturrini.gamesite.controllers.actionrules;

import java.util.List;

public interface ActionRule {

	boolean isExecutable();

	List<String> execute();

}
