package it.sturrini.gamesite.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import it.sturrini.gamesite.api.conf.GenericExceptionMapper;
import it.sturrini.gamesite.api.conf.JacksonConfigurator;
import it.sturrini.gamesite.api.conf.OptionsMethodMapper;
import it.sturrini.gamesite.api.conf.ResponseHeadersFilter;

public class GamesiteApplication extends Application {

	// private Set<Object> singletons = new HashSet<Object>();
	//
	// public GamesiteApplication() {
	// singletons.add(new PlayerService());
	// }
	//
	// @Override
	// public Set<Object> getSingletons() {
	// return singletons;
	// }
	//
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> res = new HashSet<>();
		res.add(JacksonConfigurator.class);
		res.add(GenericExceptionMapper.class);
		res.add(OptionsMethodMapper.class);

		// Filtri
		res.add(ResponseHeadersFilter.class);

		// Endpoints
		res.add(PlayerService.class);
		res.add(PlayerActionsService.class);
		res.add(MapService.class);
		res.add(ResearchService.class);
		return res;
	}

	public GamesiteApplication() {

	}

}