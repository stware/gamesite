package it.sturrini.gamesite.api.conf;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import it.sturrini.common.Constants;

@Produces(Constants.REST_API_JSON_IO_FORMAT)
public class BaseResource {

	@Context
	protected HttpServletRequest request;

	@Context
	protected HttpHeaders headers;

	@HeaderParam("Authorization")
	// SWAGGER
	// @Parameter(hidden = true)
	protected String token;

	protected <T> BaseResponse<T> buildBaseResponse(Status status) {
		BaseResponse<T> res = new BaseResponse<>();

		res.setRequest(request.getParameterMap());
		res.setHeaders(headers.getRequestHeaders());

		res.startTime();

		if (status != null) {
			res.setStatus(status);
		}

		return res;
	}

	protected <T> BaseResponse<T> buildBaseResponse() {
		return buildBaseResponse(null);
	}
}
