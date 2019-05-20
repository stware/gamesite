package it.sturrini.gamesite.api.conf;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(JacksonConfigurator.class);

	protected final ObjectMapper mapper = new ObjectMapper();

	public JacksonConfigurator() {
		configure();
	}

	protected void configure() {
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// mapper.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	@Override
	public ObjectMapper getContext(Class<?> c) {
		return mapper;
	}

}
