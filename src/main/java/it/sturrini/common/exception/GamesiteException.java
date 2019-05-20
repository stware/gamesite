package it.sturrini.common.exception;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GamesiteException extends Exception {

	private static Logger log = LogManager.getLogger(GamesiteException.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 6257346726125992824L;

	private List<String> errors;

	public GamesiteException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GamesiteException(List<String> errors) {
		super();
		this.errors = errors;
	}

	public GamesiteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public GamesiteException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public GamesiteException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public GamesiteException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public static GamesiteException managedException(Exception e) {
		if (e instanceof GamesiteException) {
			return (GamesiteException) e;
		} else {
			log.error(e);
			return new GamesiteException(e);
		}
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
