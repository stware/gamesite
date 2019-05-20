package it.sturrini.gamesite.events;

import java.util.List;

public interface EventRule {

	boolean isExecutable(Event e, Object source);

	List<String> execute(Event e, Object source);

}
