package it.sturrini.gamesite.dao.mongo;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class LongDeserializer extends JsonDeserializer<Long> {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(LongDeserializer.class);

	@Override
	public Long deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

		JsonNode tree = jp.getCodec().readTree(jp);
		JsonNode jsonNode = tree.get("$numberLong");
		if (jsonNode != null) {
			Long id = jsonNode.asLong();

			return id;
		} else {
			return null;
		}
	}

}
