package it.sturrini.gamesite.api.conf;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base REST response.
 */
public class BaseResponse<T> {

	protected Status status;

	protected String errorCode;
	protected String message;

	// Informazioni di debug
	protected Map<String, String[]> request;
	protected Map<String, List<String>> headers;
	@JsonIgnore
	protected long startTime;
	protected long elapsedTime;

	protected T body;

	public BaseResponse() {
		status = Status.OK;
	}

	public BaseResponse(Status status, T body) {
		this.status = status;
		this.body = body;
	}

	public void startTime() {
		startTime = System.nanoTime();
	}

	public void endTime() {
		if (startTime > 0) {
			elapsedTime = System.nanoTime() - startTime;
		}
	}

	public void setBody(T body) {
		this.body = body;

		endTime();
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return the body
	 */
	public T getBody() {
		return body;
	}

	/**
	 * @param status
	 *        the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *        the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *        the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the request
	 */
	public Map<String, String[]> getRequest() {
		return request;
	}

	/**
	 * @param request
	 *        the request to set
	 */
	public void setRequest(Map<String, String[]> request) {
		this.request = request;
	}

	/**
	 * @return the elapsedTime
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime
	 *        the elapsedTime to set
	 */
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the headers
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *        the headers to set
	 */
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

}
