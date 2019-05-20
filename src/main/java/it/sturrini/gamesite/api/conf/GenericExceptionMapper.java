package it.sturrini.gamesite.api.conf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Provider
// SWAGGER
// @Hidden
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Log log = LogFactory.getLog(GenericExceptionMapper.class);

	@Context
	private HttpServletRequest request;

	@Override
	public Response toResponse(Throwable t) {
		log.error(HTTPServletUtil.printHTTPRequest(request), t);
		return ResourceUtil.buildErrorResponse(t);
	}

}
