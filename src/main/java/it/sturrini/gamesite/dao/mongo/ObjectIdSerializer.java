package it.sturrini.gamesite.dao.mongo;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(ObjectIdSerializer.class);

	@Override
	public void serialize(ObjectId objid, JsonGenerator jsongen, SerializerProvider provider) throws IOException {
		if (objid == null) {
			jsongen.writeNull();
		} else {
			jsongen.writeString(objid.toString());
		}

	}

}
