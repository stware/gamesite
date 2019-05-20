package it.sturrini.gamesite.model.actions;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.exception.GamesiteException;
import it.sturrini.gamesite.controllers.actionrules.ActionsEnum;
import it.sturrini.gamesite.dao.MongoDao;
import it.sturrini.gamesite.model.Player;

public abstract class Action implements Serializable {

	private static Logger log = LogManager.getLogger(Action.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 8164786525774824135L;

	protected ActionsEnum type;

	protected Player player;

	public Action() {
		super();
	}

	public Action(String type, String platerId) throws GamesiteException {
		super();
		init(type, platerId);

	}

	/**
	 * @param type
	 * @param playerId
	 * @throws GamesiteException
	 */
	private void init(String type, String playerId) throws GamesiteException {
		try {
			if (type == null || playerId == null) {
				throw new GamesiteException("Player or type of the action not specified");
			}
			if (ActionsEnum.valueOf(type) != null) {
				this.type = ActionsEnum.valueOf(type);
			} else {
				throw new GamesiteException("Action not found");
			}
			player = (Player) MongoDao.getInstance(Player.class).findById(playerId);
			if (player == null) {
				throw new GamesiteException("Player not found");
			}
		} catch (Exception e) {
			if (e instanceof GamesiteException) {
				throw e;
			} else {
				log.error(e);
				throw new GamesiteException(e);
			}
		}

	}

	private void initPlayer(String playerId) throws GamesiteException {
		try {
			if (playerId == null) {
				throw new GamesiteException("Player of the action not specified");
			}
			player = (Player) MongoDao.getInstance(Player.class).findById(playerId);
			if (player == null) {
				throw new GamesiteException("Player not found");
			}
		} catch (Exception e) {
			if (e instanceof GamesiteException) {
				throw e;
			} else {
				log.error(e);
				throw new GamesiteException(e);
			}
		}

	}

	public ActionsEnum getType() {
		return type;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(String playerId) throws GamesiteException {
		initPlayer(playerId);
	}

}
