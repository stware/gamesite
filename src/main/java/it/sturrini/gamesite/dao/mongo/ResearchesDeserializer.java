package it.sturrini.gamesite.dao.mongo;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import it.sturrini.gamesite.model.research.Researches;

public class ResearchesDeserializer extends JsonDeserializer<Researches> {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(ResearchesDeserializer.class);

	@Override
	public Researches deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

		JsonNode tree = jp.getCodec().readTree(jp);
		if (tree instanceof TextNode) {
			String type = tree.textValue();
			if (type == null) {
				return Researches.start;
			}
			Researches gii = null;

			try {
				gii = Researches.valueOf(type);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (gii != null) {
				return gii;
			}

		}
		if (tree instanceof NullNode) {
			return Researches.start;
		}
		if (tree instanceof ObjectNode) {
			JsonNode type = tree.get("name");
			System.out.println(type);
			return Researches.valueOf(type.asText());
		}
		return Researches.start;
	}

}
