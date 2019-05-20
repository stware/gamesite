package it.sturrini.gamesite.dao.mongo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mongodb.MongoClientURI;

import it.sturrini.common.Constants;

public class MongoUtils {

	private static final Logger log = LogManager.getLogger(MongoUtils.class);

	private static MongoClient client = null;

	public static MongoClient getClient() {
		try {
			if (client == null) {

				MongoClientURI uri = new MongoClientURI(Constants.DB_CONNECTION_URL);

				client = new MongoClient(uri);
				System.out.println("Client created");
			}
		} catch (Throwable t) {
			log.warn("Unable to configure MongoClient: " + t.getMessage());
		} finally {
			if (client == null) {
				log.warn("MongoClient is not configured: it is not bound in JNDI.");
			}
		}

		return client;
	}

	public static void closeClient() {
		if (client != null) {
			client.close();
		}
	}

}
