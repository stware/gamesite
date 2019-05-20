package it.sturrini.gamesite.mongo;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import it.sturrini.common.Constants;
import it.sturrini.gamesite.dao.mongo.MongoUtils;

public class TestMongoConnection {

	@Test
	public void test() {

		MongoClientURI uri = new MongoClientURI(Constants.DB_CONNECTION_URL);

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("gamesite");
		MongoCollection<Document> players = database.getCollection("players");
		Assert.assertTrue("Connection Unsuccesful!", players != null);
		long countDocuments = -1L;
		countDocuments = players.count();
		Assert.assertTrue("Connection Unsuccesful!", countDocuments >= 0);
		MongoClient client = MongoUtils.getClient();
		Assert.assertTrue("Connection Unsuccesful!", client != null);

	}

}
