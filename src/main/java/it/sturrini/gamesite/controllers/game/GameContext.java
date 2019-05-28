package it.sturrini.gamesite.controllers.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.gamesite.controllers.ControllerWithEvents;
import it.sturrini.gamesite.controllers.player.PlayerController;
import it.sturrini.gamesite.events.Event;
import it.sturrini.gamesite.events.EventRulesExecutor;
import it.sturrini.gamesite.model.BaseEntity;
import it.sturrini.gamesite.model.Player;
import it.sturrini.gamesite.server.GameServer;
import it.sturrini.gamesite.timedexecutor.TimedExecutor;

public class GameContext extends ControllerWithEvents {

	static Logger log;

	private static GameServer gameServer = null;

	private static GameContext instance = null;

	private static List<Player> onlinePlayers;

	public static final String SERVER_NAME = "Alpha";

	protected GameContext() {

	}

	private static void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");
		gameServer = GameServer.getInstance(SERVER_NAME);
		onlinePlayers = new ArrayList<>();
		PlayerController.getInstance().registerListener(instance);
		TimedExecutor.getInstance();
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

	@Override
	public List<String> onEvent(Event e, BaseEntity be, Object... args) {
		super.onEvent(e, be, args);
		List<String> result = EventRulesExecutor.getInstance().executeRules(this.getClass().getSimpleName(), e, be, args);
		return result;
	}

	public void addPlayer(Player p) {
		onlinePlayers.add(p);
	}

	public void shutdown() {
		log.debug("Stopping " + instance.getClass().getSimpleName() + "...");
		TimedExecutor.getInstance().shutdown();

	}
}
