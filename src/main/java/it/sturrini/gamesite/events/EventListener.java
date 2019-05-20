package it.sturrini.gamesite.events;

import java.util.List;

import it.sturrini.gamesite.model.BaseEntity;

public interface EventListener {

	List<String> onEvent(Event e, BaseEntity be);

}
