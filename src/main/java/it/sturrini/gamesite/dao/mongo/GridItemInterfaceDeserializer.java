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

import it.sturrini.gamesite.model.map.Buildings;
import it.sturrini.gamesite.model.map.GridItemInterface;
import it.sturrini.gamesite.model.map.LandTypes;
import it.sturrini.gamesite.model.map.Streets;

public class GridItemInterfaceDeserializer extends JsonDeserializer<GridItemInterface> {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(GridItemInterfaceDeserializer.class);

	@Override
	public GridItemInterface deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

		JsonNode tree = jp.getCodec().readTree(jp);
		if (tree instanceof TextNode) {
			String type = tree.textValue();
			if (type == null) {
				return LandTypes.land;
			}
			GridItemInterface gii = null;

			try {
				gii = LandTypes.valueOf(type);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (gii != null) {
				return gii;
			}

			try {
				gii = Buildings.valueOf(type);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (gii != null) {
				return gii;
			}

			try {
				gii = Streets.valueOf(type);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (gii != null) {
				return gii;
			}
		}
		if (tree instanceof NullNode) {
			return LandTypes.nul;
		}
		if (tree instanceof ObjectNode) {
			JsonNode type = tree.get("type");
			System.out.println(type);
		}
		return Buildings.TownHall;
	}

}
