package it.sturrini.gamesite.dao.mongo;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(ObjectIdDeserializer.class);

	@Override
	public ObjectId deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

		JsonNode tree = jp.getCodec().readTree(jp);
		String id = tree.get("$oid").asText();

		return new ObjectId(id);
	}

}
