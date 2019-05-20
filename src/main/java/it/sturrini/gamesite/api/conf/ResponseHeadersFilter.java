package it.sturrini.gamesite.api.conf;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import it.sturrini.common.Constants;

@Priority(Priorities.HEADER_DECORATOR)
// SWAGGER
// @Hidden
public class ResponseHeadersFilter implements ContainerResponseFilter {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(ResponseHeadersFilter.class);

	@Override
	public void filter(ContainerRequestContext reqCtx, ContainerResponseContext respCtx) throws IOException {
		MultivaluedMap<String, Object> headers = respCtx.getHeaders();

		headers.add("X-Powered-By", "STware");
		headers.add("X-API-Version", Constants.REST_API_VERSION);

		// Sono stati documentati dei casi in cui, nonostante non sia utilizzato
		// il metodo OPTIONS, il browser richiede la presenza degli header di
		// seguito indicati
		String remoteHost = reqCtx.getHeaders().getFirst("origin");
		String localHost = reqCtx.getUriInfo().getAbsolutePath().getHost();
		boolean sameHosts = remoteHost != null && localHost != null && remoteHost.equals(localHost);

		if (!HttpMethod.OPTIONS.equals(reqCtx.getMethod()) && !sameHosts) {

			// log.debug("Setting response headers for url: " + reqCtx.getMethod() + " " + reqCtx.getUriInfo().getPath());

			headers.add("Access-Control-Allow-Origin", "*");
			headers.add("Access-Control-Allow-Headers", "Content-Type, Content-Disposition, Authorization, Pragma, Accept, Origin, X-Requested-With, X-XSRF-Token");
			headers.add("Access-Control-Expose-Headers", "Content-Type, Content-Disposition, Authorization, Pragma, Accept, Origin, X-Requested-With, X-XSRF-Token");
			headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
			headers.add("Access-Control-Allow-Credentials", "true");
		}

		headers.add("Pragma", "no-cache");
		headers.add("Cache-Control", "max-age=0,must-revalidate");
		headers.add("Expires", "0");
	}

}
