package it.sturrini.gamesite.api.conf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.spi.DefaultOptionsMethodException;

@Provider
// SWAGGER
// @Hidden
public class OptionsMethodMapper implements ExceptionMapper<DefaultOptionsMethodException> {

	private static final Log log = LogFactory.getLog(OptionsMethodMapper.class);

	@Context
	private HttpServletRequest request;

	@Override
	public Response toResponse(DefaultOptionsMethodException e) {
		String allowOriginHeader = null;

		String origin = request.getHeader("origin");
		if (origin != null) {
			allowOriginHeader = origin;
		} else {
			HostInfo hiReferer = HostInfo.fromUrl(request.getHeader("referer"));
			allowOriginHeader = hiReferer.getScheme() + "://" + hiReferer.getHost() + ":" + hiReferer.getPort();
		}

		// https://github.com/resteasy/Resteasy/blob/master/resteasy-jaxrs/src/main/java/org/jboss/resteasy/plugins/interceptors/CorsFilter.java
		log.debug("Enabling XSS for url: " + request.getRequestURI() + " from: " + allowOriginHeader);

		// @formatter:off
		ResponseBuilder rb = Response.ok()
			.header("Access-Control-Allow-Origin","*")
			.header("Access-Control-Allow-Headers", "Content-Type, Content-Disposition, Authorization, Pragma, Accept, Origin, X-Requested-With, X-XSRF-Token")
			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS")
			.header("Access-Control-Allow-Credentials", "true");
		// @formatter:on
		return rb.build();
	}

}
