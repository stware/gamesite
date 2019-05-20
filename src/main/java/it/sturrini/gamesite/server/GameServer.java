package it.sturrini.gamesite.server;

import it.sturrini.common.DateUtils;

public class GameServer {

	private Long serverStartTime;

	private String serverName;

	private static GameServer instance = null;

	protected GameServer(String serverName) {
		super();
		init(serverName);
	}

	private void init(String serverName) {
		this.serverStartTime = DateUtils.nowInMillis();
		this.serverName = serverName;
	}

	public static GameServer getInstance(String serverName) {
		if (instance == null) {
			synchronized (GameServer.class) {
				if (instance == null) {
					instance = new GameServer(serverName);
				}
			}
		}
		return instance;
	}

	public Long getServerTime() {
		return DateUtils.nowInMillis() - serverStartTime;
	}

}
