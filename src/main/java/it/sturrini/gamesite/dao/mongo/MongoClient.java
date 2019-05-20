package it.sturrini.gamesite.dao.mongo;

import java.net.UnknownHostException;

import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoClient extends com.mongodb.MongoClient {

	private String database;

	public MongoClient(MongoClientURI uri) throws UnknownHostException {
		super(uri);
		this.database = uri.getDatabase();
	}

	public MongoDatabase getDatabase() {
		return super.getDatabase(database);
	}
}