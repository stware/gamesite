package it.sturrini.gamesite.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.gamesite.controllers.player.PlayerController;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.server.GameServer;

public class GameContext {

	static Logger log;

	private static GameServer gameServer = null;

	private static GameContext instance = null;

	private static List<Player> players;

	public static final String SERVER_NAME = "Alpha";

	protected GameContext() {

	}

	private static void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");
		gameServer = GameServer.getInstance(SERVER_NAME);
		players = new ArrayList<>();
		PlayerController.getInstance();
	}

	public static GameContext getInstance() {
		if (instance == null) {
			synchronized (GameContext.class) {
				if (instance == null) {
					instance = new GameContext();
					log = LogManager.getLogger(instance.getClass());
					init();
				}
			}
		}
		return instance;
	}

	public static GameServer getGameServer() {
		return gameServer;
	}
}
