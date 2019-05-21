package it.sturrini.gamesite.controllers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import it.sturrini.gamesite.controllers.game.GameContext;

/**
 * Application Lifecycle Listener implementation class InitContextListener
 */
public class InitContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public InitContextListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("Starting up!");
		GameContext gc = GameContext.getInstance();
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		GameContext.getInstance().shutdown();
	}

}
