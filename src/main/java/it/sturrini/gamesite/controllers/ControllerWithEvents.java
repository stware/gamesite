package it.sturrini.gamesite.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.events.EventListener;
import it.sturrini.gamesite.model.BaseEntity;

public abstract class ControllerWithEvents implements EventListener {

	private static Logger log = LogManager.getLogger(ControllerWithEvents.class);

	public ControllerWithEvents() {
		super();
		listeners = new ArrayList<>();
	}

	protected List<EventListener> listeners;

	public void registerListener(EventListener listener) {
		log.debug(this.getClass().getSimpleName() + ": Adding listener " + listener.getClass().getSimpleName());
		listeners.add(listener);
	}

	protected List<String> fireEvent(Event event, BaseEntity be) {
		log.debug(this.getClass().getSimpleName() + ": Firing event " + event.getType() + " on entity " + be.getClass().getSimpleName());
		List<String> result = this.listeners.stream().map(listener -> listener.onEvent(event, be)).flatMap(Collection::stream).collect(Collectors.toList());
		return result;
	}

	@Override
	public List<String> onEvent(Event e, BaseEntity be) {
		log.debug(this.getClass().getSimpleName() + ": Event " + e.getType() + " fired on entity " + be);
		return new ArrayList<>();

	}

}
