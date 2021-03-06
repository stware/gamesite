package it.sturrini.gamesite.events;

import java.util.List;

public interface EventRule {

	boolean isExecutable(String caller, Event e, Object source, Object... args);

	List<String> execute(String caller, Event e, Object source, Object... args);

}
