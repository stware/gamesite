package it.sturrini.gamesite.timedexecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.sturrini.common.DateUtils;
import it.sturrini.gamesite.controllers.player.PlayerController;

public class TimedExecutor {

	private static TimedExecutor instance = null;

	static Logger log;

	private static ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	ScheduledFuture<?> scheduledFuture;

	protected TimedExecutor() {

	}

	private static void init() {
		log.debug("Starting " + instance.getClass().getSimpleName() + "...");
		instance.start();
	}

	public static TimedExecutor getInstance() {
		if (instance == null) {
			synchronized (TimedExecutor.class) {
				if (instance == null) {
					instance = new TimedExecutor();
					log = LogManager.getLogger(instance.getClass());
					init();
				}
			}
		}
		return instance;
	}

	private void start() {
		scheduledFuture = ses.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);
	}

	Runnable task = () -> {
		Long now = DateUtils.nowInMillis();
		// log.debug("Executing at " + DateUtils.print(now));
		PlayerController.getInstance().addPoint(now);
	};

	public void shutdown() {
		log.debug("Stopping scheduled future...");
		scheduledFuture.cancel(false);
		if (scheduledFuture.isCancelled()) {
			log.debug("Stopping Scheduler Executor Service...");
			ses.shutdown();
		}

	}

}
